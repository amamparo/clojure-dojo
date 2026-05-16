(ns katas.kata-09-bank-account-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-09-bank-account :as bank]))

(deftest opening-balance
  (is (match? 0   (bank/balance (bank/open-account))))
  (is (match? 100 (bank/balance (bank/open-account 100)))))

(deftest deposit-and-balance
  (let [a (bank/open-account)]
    (is (match? 50 (bank/deposit! a 50)))
    (is (match? 75 (bank/deposit! a 25)))
    (is (match? 75 (bank/balance a)))))

(deftest withdraw-and-balance
  (let [a (bank/open-account 100)]
    (is (match? 60 (bank/withdraw! a 40)))
    (is (match? 60 (bank/balance a)))))

(deftest insufficient-funds
  (let [a (bank/open-account 10)]
    (is (thrown-match? clojure.lang.ExceptionInfo
                       {:type :insufficient-funds}
                       (bank/withdraw! a 11)))
    (testing "balance is unchanged after a failed withdrawal"
      (is (match? 10 (bank/balance a))))))

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
    (is (match? [[:open 100] [:deposit 20] [:withdraw 5]]
                (bank/history a)))))

(deftest concurrent-deposits
  (testing "no updates lost under contention"
    (let [a     (bank/open-account 0)
          tasks (doall (for [_ (range 1000)]
                         (future (bank/deposit! a 1))))]
      (run! deref tasks)
      (is (match? 1000 (bank/balance a)))
      (is (match? 1001 (count (bank/history a)))))))
