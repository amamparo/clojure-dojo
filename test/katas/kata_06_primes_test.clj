(ns katas.kata-06-primes-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-06-primes :refer [prime? primes]]))

(deftest prime?-edge-cases
  (is (false? (prime? 0)))
  (is (false? (prime? 1)))
  (is (true?  (prime? 2)))
  (is (true?  (prime? 3)))
  (is (false? (prime? 4))))

(deftest prime?-known-values
  (is (true?  (prime? 97)))
  (is (true?  (prime? 101)))
  (is (false? (prime? 99)))
  (is (false? (prime? 1001))))

(deftest primes-first-ten
  (is (= [2 3 5 7 11 13 17 19 23 29]
         (vec (take 10 primes)))))

(deftest primes-nth
  (is (= 97 (nth primes 24))))

(deftest primes-take-while
  (is (= [2 3 5 7 11 13 17 19]
         (vec (take-while #(< % 20) primes)))))

(deftest primes-is-a-seq
  (testing "primes is seqable (a list, lazy seq, or similar)"
    (is (seq? (seq primes)))))
