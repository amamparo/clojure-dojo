(ns katas.kata-13-bank-account-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-13-bank-account :as bank]))

(deftest opening-balance
  (is (= 0 (bank/balance (bank/open-account))))
  (is (= 100 (bank/balance (bank/open-account 100)))))

(deftest deposit-and-balance
  (let [a (bank/open-account)]
    (is (= 50 (bank/deposit! a 50)))
    (is (= 75 (bank/deposit! a 25)))
    (is (= 75 (bank/balance a)))))

(deftest withdraw-and-balance
  (let [a (bank/open-account 100)]
    (is (= 60 (bank/withdraw! a 40)))
    (is (= 60 (bank/balance a)))))

(deftest insufficient-funds
  (let [a (bank/open-account 10)]
    (is (thrown-match? clojure.lang.ExceptionInfo
                       {:type :insufficient-funds}
                       (bank/withdraw! a 11)))
    (testing "balance and history unchanged after a failed withdrawal"
      (is (= 10 (bank/balance a)))
      (is (= 1 (count (bank/history a)))))))

(deftest invalid-amount
  (let [a (bank/open-account 10)]
    (is (thrown-match? clojure.lang.ExceptionInfo
                       {:type :invalid-amount}
                       (bank/deposit! a -1)))
    (is (thrown-match? clojure.lang.ExceptionInfo
                       {:type :invalid-amount}
                       (bank/withdraw! a -1)))))

(deftest history-is-ordered
  (let [a (bank/open-account 100)]
    (bank/deposit! a 20)
    (bank/withdraw! a 5)
    (is (match? [{:op :open, :amount 100} {:op :deposit, :amount 20}
                 {:op :withdraw, :amount 5}]
                (bank/history a)))))

(deftest history-addressable-by-field
  (let [a (bank/open-account 100)]
    (bank/deposit! a 20)
    (is (= :open (:op (first (bank/history a)))))
    (is (= 100 (:amount (first (bank/history a)))))
    (is (= [:open :deposit] (mapv :op (bank/history a))))))

;; ── Event sourcing: balance is a pure projection of the log ────────────
;; apply-event is the pure step ([balance event] -> balance); replay folds
;; it over the whole history from the empty state (0). These must be pure
;; and public — the account is only a thin atom façade over the log.

(deftest apply-event-is-a-pure-step
  (is (= 100 (bank/apply-event 0 {:op :open, :amount 100})))
  (is (= 130 (bank/apply-event 100 {:op :deposit, :amount 30})))
  (is (= 70 (bank/apply-event 100 {:op :withdraw, :amount 30}))))

(deftest replay-reconstructs-balance-from-the-log
  (is (= 0 (bank/replay [])))
  (is (= 115
         (bank/replay [{:op :open, :amount 100} {:op :deposit, :amount 20}
                       {:op :withdraw, :amount 5}]))))

(deftest balance-is-derived-not-stored
  (testing "balance is exactly the replay of the account's own history"
    (let [a (bank/open-account 100)]
      (bank/deposit! a 20)
      (bank/withdraw! a 5)
      (is (= (bank/balance a) (bank/replay (bank/history a))))
      (testing "the atom stores the log only, no separate :balance field"
        (is (= [:history] (keys @a)))))))

(deftest concurrent-deposits
  (testing "no updates lost under contention (the deposit! bug)"
    (let [a (bank/open-account 0)
          tasks (doall (for [_ (range 1000)] (future (bank/deposit! a 1))))]
      (run! deref tasks)
      (is (= 1000 (bank/balance a)))
      (is (= 1001 (count (bank/history a)))))))

(deftest concurrent-mixed
  (testing "deposits and withdrawals interleaved stay consistent"
    (let [a (bank/open-account 100000)
          ds (doall (for [_ (range 500)] (future (bank/deposit! a 2))))
          ws (doall (for [_ (range 500)] (future (bank/withdraw! a 1))))]
      (run! deref (concat ds ws))
      (is (= 100500 (bank/balance a)))
      (is (= 1001 (count (bank/history a)))))))
