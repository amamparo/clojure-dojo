(ns katas.kata-09-digits-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-09-digits :refer
             [digits digital-root collatz-length]]))

(deftest digits-cases
  (is (= [0] (digits 0)))
  (is (= [7] (digits 7)))
  (is (= [1 0] (digits 10)))
  (is (= [1 2 3 4] (digits 1234)))
  (is (= [9 0 0 9] (digits 9009)))
  (is (vector? (digits 1234))))

(deftest digital-root-cases
  (is (= 0 (digital-root 0)))
  (is (= 7 (digital-root 7)))
  (is (= 4 (digital-root 49)))
  (is (= 1 (digital-root 1234)))
  (is (= 9 (digital-root 99999)))
  (is (= 9 (digital-root 123456789)))
  (is (= 9 (digital-root 987654321))))

(deftest collatz-length-cases
  (is (= 0 (collatz-length 1)))
  (is (= 1 (collatz-length 2)))
  (is (= 7 (collatz-length 3)))
  (is (= 8 (collatz-length 6)))
  (is (= 111 (collatz-length 27)))
  (is (= 524 (collatz-length 837799))))

(deftest collatz-deep-does-not-overflow
  (testing "self-calls overflow the stack at this depth; recur does not"
    ;; 2^100000 as a BigInt: 100000 halvings, every step even
    (is (= 100000 (collatz-length (apply * (repeat 100000 2N)))))))
