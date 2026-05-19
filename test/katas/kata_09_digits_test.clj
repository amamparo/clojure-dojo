(ns katas.kata-09-digits-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-09-digits :refer
             [digits digit-sum digital-root collatz-length]]))

(deftest digits-cases
  (is (= [0] (digits 0)))
  (is (= [7] (digits 7)))
  (is (= [1 0] (digits 10)))
  (is (= [1 2 3 4] (digits 1234)))
  (is (= [9 0 0 9] (digits 9009))))

(deftest digit-sum-cases
  (is (= 0 (digit-sum 0)))
  (is (= 7 (digit-sum 7)))
  (is (= 10 (digit-sum 1234)))
  (is (= 54 (digit-sum 999999))))

(deftest digit-sum-agrees-with-digits
  (testing "digit-sum equals the sum of the digit vector"
    (doseq [n [0 7 58 1234 99999 1000000]]
      (is (= (reduce + (digits n)) (digit-sum n)) (str "n=" n)))))

(deftest digital-root-cases
  (is (= 0 (digital-root 0)))
  (is (= 7 (digital-root 7)))
  (is (= 1 (digital-root 1234)))
  (is (= 9 (digital-root 99999)))
  (is (= 9 (digital-root 123456789))))

(deftest digital-root-is-single-digit
  (doseq [n [0 5 49 1234 987654321]]
    (is (<= 0 (digital-root n) 9) (str "n=" n))))

(deftest collatz-length-cases
  (is (= 0 (collatz-length 1)))
  (is (= 1 (collatz-length 2)))
  (is (= 7 (collatz-length 3)))
  (is (= 8 (collatz-length 6)))
  (is (= 111 (collatz-length 27))))

(deftest collatz-deep-does-not-overflow
  (testing "a long Collatz chain runs without a stack overflow"
    (is (= 524 (collatz-length 837799)))))
