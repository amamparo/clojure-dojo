(ns katas.kata-08-primes-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-08-primes :refer [prime? primes nth-prime]]))

(deftest prime-predicate
  (is (true? (prime? 2)))
  (is (true? (prime? 3)))
  (is (true? (prime? 97)))
  (is (false? (prime? 0)))
  (is (false? (prime? 1)))
  (is (false? (prime? 4)))
  (is (false? (prime? 91)))
  (is (false? (prime? 100))))

(deftest prime-predicate-matches-known-list
  (is (= [2 3 5 7 11 13 17 19 23 29] (filter prime? (range 30)))))

(deftest primes-prefix
  (is (= [2 3 5 7 11] (take 5 primes)))
  (is (= [2 3 5 7 11 13 17 19 23 29] (take 10 primes))))

(deftest primes-is-lazy-and-infinite
  (testing "forcing a deep prefix neither hangs nor overflows the stack"
    (is (= 7919 (nth primes 999)))))

(deftest primes-take-while
  (is (= [2 3 5 7 11 13 17 19 23 29] (take-while #(< % 30) primes))))

(deftest nth-prime-cases
  (is (= 2 (nth-prime 0)))
  (is (= 3 (nth-prime 1)))
  (is (= 31 (nth-prime 10)))
  (is (= 547 (nth-prime 100))))
