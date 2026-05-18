(ns katas.kata-09-bank-account-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-09-bank-account :as bank]))

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
    (testing "balance is unchanged after a failed withdrawal"
      (is (= 10 (bank/balance a))))))

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

(deftest history-shape-by-field
  (testing "individual entries are addressable by :op and :amount"
    (let [a (bank/open-account 100)]
      (bank/deposit! a 20)
      (bank/withdraw! a 5)
      (is (= 3 (count (bank/history a))))
      (is (= :open (:op (first (bank/history a)))))
      (is (= 100 (:amount (first (bank/history a)))))
      (is (= :withdraw (:op (last (bank/history a)))))
      (is (= 5 (:amount (last (bank/history a))))))
    (testing "history entries are appended in operation order"
      (let [a (bank/open-account 50)]
        (bank/deposit! a 10)
        (bank/withdraw! a 5)
        (is (= [:open :deposit :withdraw] (mapv :op (bank/history a))))))))

(deftest concurrent-deposits
  (testing "no updates lost under contention"
    (let [a (bank/open-account 0)
          tasks (doall (for [_ (range 1000)] (future (bank/deposit! a 1))))]
      (run! deref tasks)
      (is (= 1000 (bank/balance a)))
      (is (= 1001 (count (bank/history a)))))))
