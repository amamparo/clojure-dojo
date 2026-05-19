(ns katas.kata-12-rpn-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-12-rpn :refer [evaluate]]))

(deftest single-number (is (= 3 (evaluate [3]))))

(deftest simple-addition (is (= 3 (evaluate [1 2 '+]))))

(deftest subtraction-order
  (testing "for `a b -`, the deeper element is the left operand"
    (is (= -1 (evaluate [3 4 '-])))
    (is (= 2 (evaluate [5 3 '-])))))

(deftest multiplication-and-grouping
  (is (= 20 (evaluate [2 3 '+ 4 '*])))
  (is (= 17 (evaluate [5 1 2 '+ 4 '* '+]))))

(deftest division-is-exact
  (is (= 3 (evaluate [6 2 '/])))
  (is (= 7/2 (evaluate [7 2 '/]))))

(deftest longer-expression
  ;; 15 7 1 1 + - / 3 *  ==  ((15 / (7 - (1+1))) * 3) = (15/5)*3 = 9
  (is (= 9 (evaluate [15 7 1 1 '+ '- '/ 3 '*]))))

(deftest unknown-op
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :unknown-op, :op 'pow}
                     (evaluate [2 3 'pow]))))

(deftest stack-underflow
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :stack-underflow}
                     (evaluate [1 '+])))
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :stack-underflow}
                     (evaluate ['+]))))

(deftest malformed-empty
  (is
   (thrown-match? clojure.lang.ExceptionInfo {:type :malformed} (evaluate []))))

(deftest malformed-leftover
  (testing "more than one value left on the stack is malformed"
    (is (thrown-match? clojure.lang.ExceptionInfo
                       {:type :malformed}
                       (evaluate [1 2 3 '+])))))
