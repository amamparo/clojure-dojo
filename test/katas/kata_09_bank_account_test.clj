(ns katas.kata-09-bank-account-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-09-bank-account :as bank]))

(deftest opening-balance
  (is (= 0   (bank/balance (bank/open-account))))
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
    (try (bank/withdraw! a 11)
         (is false "should have thrown")
         (catch clojure.lang.ExceptionInfo e
           (is (= :insufficient-funds (:type (ex-data e))))))
    (testing "balance is unchanged after a failed withdrawal"
      (is (= 10 (bank/balance a))))))

(deftest invalid-amount
  (let [a (bank/open-account 10)]
    (try (bank/deposit! a -1)
         (is false "deposit of -1 should throw")
         (catch clojure.lang.ExceptionInfo e
           (is (= :invalid-amount (:type (ex-data e))))))
    (try (bank/withdraw! a -1)
         (is false "withdraw of -1 should throw")
         (catch clojure.lang.ExceptionInfo e
           (is (= :invalid-amount (:type (ex-data e))))))))

(deftest history-is-ordered
  (let [a (bank/open-account 100)]
    (bank/deposit! a 20)
    (bank/withdraw! a 5)
    (is (= [[:open 100] [:deposit 20] [:withdraw 5]]
           (bank/history a)))))

(deftest concurrent-deposits
  (testing "no updates lost under contention"
    (let [a     (bank/open-account 0)
          tasks (doall (for [_ (range 1000)]
                         (future (bank/deposit! a 1))))]
      (run! deref tasks)
      (is (= 1000 (bank/balance a)))
      (is (= 1001 (count (bank/history a)))))))
