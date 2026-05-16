(ns katas.kata-10-rpn-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-10-rpn :refer [evaluate]]))

(deftest empty-program
  (is (match? [] (evaluate []))))

(deftest just-numbers
  (is (match? [1 2 3] (evaluate [1 2 3]))))

(deftest basic-arithmetic
  (is (match? [3]   (evaluate [1 2 '+])))
  (is (match? [-1]  (evaluate [3 4 '-])))
  (is (match? [12]  (evaluate [3 4 '*])))
  (is (match? [5]   (evaluate [10 2 '/]))))

(deftest non-commutative-order
  (testing "deeper operand is left-hand side"
    (is (match? [-5] (evaluate [3 4 '- 5 '*])))     ; (3 - 4) * 5 = -5
    (is (match? [3]  (evaluate [8 2 '/ 1 '-])))))   ; (8 / 2) - 1 = 3

(deftest stack-ops
  (is (match? [16]  (evaluate [4 'dup '*])))
  (is (match? [1 3] (evaluate [1 2 3 'swap 'drop])))
  (is (match? [1 2] (evaluate [1 2 3 'drop])))
  (is (match? [2 1] (evaluate [1 2 'swap]))))

(deftest accepts-strings
  (is (match? [3]  (evaluate [1 2 "+"])))
  (is (match? [16] (evaluate [4 "dup" "*"]))))

(deftest stack-underflow-arith
  (is (thrown-match? clojure.lang.ExceptionInfo {:type :stack-underflow}
                     (evaluate ['+])))
  (is (thrown-match? clojure.lang.ExceptionInfo {:type :stack-underflow}
                     (evaluate [1 '+]))))

(deftest stack-underflow-stack-op
  (is (thrown-match? clojure.lang.ExceptionInfo {:type :stack-underflow}
                     (evaluate ['drop])))
  (is (thrown-match? clojure.lang.ExceptionInfo {:type :stack-underflow}
                     (evaluate [1 'swap]))))

(deftest unknown-token
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :unknown-token :token 'mod}
                     (evaluate [1 2 'mod]))))
