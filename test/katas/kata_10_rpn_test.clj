(ns katas.kata-10-rpn-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-10-rpn :refer [evaluate]]))

(deftest empty-program
  (is (= [] (evaluate []))))

(deftest just-numbers
  (is (= [1 2 3] (evaluate [1 2 3]))))

(deftest basic-arithmetic
  (is (= [3]   (evaluate [1 2 '+])))
  (is (= [-1]  (evaluate [3 4 '-])))
  (is (= [12]  (evaluate [3 4 '*])))
  (is (= [5]   (evaluate [10 2 '/]))))

(deftest non-commutative-order
  (testing "deeper operand is left-hand side"
    (is (= [-5] (evaluate [3 4 '- 5 '*])))     ; (3 - 4) * 5 = -5
    (is (= [3]  (evaluate [8 2 '/ 1 '-])))))   ; (8 / 2) - 1 = 3

(deftest stack-ops
  (is (= [16]  (evaluate [4 'dup '*])))
  (is (= [1 3] (evaluate [1 2 3 'swap 'drop])))
  (is (= [1 2] (evaluate [1 2 3 'drop])))
  (is (= [2 1] (evaluate [1 2 'swap]))))

(deftest accepts-strings
  (is (= [3]  (evaluate [1 2 "+"])))
  (is (= [16] (evaluate [4 "dup" "*"]))))

(deftest stack-underflow-arith
  (try (evaluate ['+])
       (is false "should have thrown")
       (catch clojure.lang.ExceptionInfo e
         (is (= :stack-underflow (:type (ex-data e))))))
  (try (evaluate [1 '+])
       (is false "should have thrown")
       (catch clojure.lang.ExceptionInfo e
         (is (= :stack-underflow (:type (ex-data e)))))))

(deftest stack-underflow-stack-op
  (try (evaluate ['drop])
       (is false "should have thrown")
       (catch clojure.lang.ExceptionInfo e
         (is (= :stack-underflow (:type (ex-data e))))))
  (try (evaluate [1 'swap])
       (is false "should have thrown")
       (catch clojure.lang.ExceptionInfo e
         (is (= :stack-underflow (:type (ex-data e)))))))

(deftest unknown-token
  (try (evaluate [1 2 'mod])
       (is false "should have thrown")
       (catch clojure.lang.ExceptionInfo e
         (is (= :unknown-token (:type (ex-data e))))
         (is (= 'mod (:token (ex-data e)))))))
