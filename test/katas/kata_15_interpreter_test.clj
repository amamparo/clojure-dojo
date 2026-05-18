(ns katas.kata-15-interpreter-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-15-interpreter :refer [evaluate]]))

(deftest self-evaluating
  (is (= 42 (evaluate {} 42)))
  (is (= "hi" (evaluate {} "hi")))
  (is (= true (evaluate {} true)))
  (is (= false (evaluate {} false)))
  (is (nil? (evaluate {} nil)))
  (is (= :kw (evaluate {} :kw))))

(deftest symbol-lookup
  (is (= 10 (evaluate '{x 10} 'x)))
  (is (= "abc" (evaluate '{s "abc"} 's))))

(deftest unbound-symbol
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :unbound-symbol}
                     (evaluate {} 'missing))))

(deftest arithmetic
  (is (= 6 (evaluate {} '(+ 1 2 3))))
  (is (= -4 (evaluate {} '(- 1 2 3))))
  (is (= 24 (evaluate {} '(* 2 3 4))))
  (is (= 5 (evaluate {} '(+ 2 3))))
  (is (= 2 (evaluate {} '(/ 8 2 2)))))

(deftest comparisons
  (is (true? (evaluate {} '(= 1 1))))
  (is (false? (evaluate {} '(= 1 2))))
  (is (true? (evaluate {} '(< 1 2))))
  (is (false? (evaluate {} '(< 2 1))))
  (is (true? (evaluate {} '(> 2 1)))))

(deftest if-truthy
  (is (= :yes (evaluate {} '(if true :yes :no))))
  (is (= :no (evaluate {} '(if false :yes :no))))
  (is (= :no (evaluate {} '(if nil :yes :no)))))

(deftest if-evaluates-its-condition
  (is (= :yes (evaluate {} '(if (< 1 2) :yes :no)))))

(deftest do-form
  (is (= 4 (evaluate {} '(do 1 2 3 4))))
  (is (nil? (evaluate {} '(do)))))

(deftest let-sequential
  (is (= 3 (evaluate {} '(let [x 1 y 2] (+ x y)))))
  (is (= 9 (evaluate {} '(let [x 1 y (+ x 2) z (* y 3)] z)))))

(deftest let-shadows
  (is (= 99 (evaluate '{x 1} '(let [x 99] x))))
  (testing "shadow does not leak outside the let"
    (is (= 1 (evaluate '{x 1} '(do (let [x 99] x) x))))))

(deftest let-implicit-do
  (is (= :last (evaluate {} '(let [x 1] :first :middle :last)))))

(deftest fn-identity (is (= 3 (evaluate {} '((fn [x] x) 3)))))

(deftest fn-squared (is (= 9 (evaluate {} '((fn [x] (* x x)) 3)))))

(deftest fn-captures-lexical-env
  (is (= 15 (evaluate '{base 10} '((fn [x] (+ base x)) 5)))))

(deftest fn-multiple-args (is (= 7 (evaluate {} '((fn [a b] (+ a b)) 3 4)))))

(deftest fn-bound-by-let
  (is (= 20 (evaluate {} '(let [double (fn [x] (* x 2))] (double 10))))))

(deftest fn-implicit-do-body
  (testing "body has multiple forms; only the last value is returned"
    (is (= 6 (evaluate {} '((fn [x] (+ x 1) (+ x 2) (+ x 3)) 3))))))

(deftest def-binds-in-atom-env
  (let [env (atom {})]
    (evaluate env '(def x 5))
    (is (= 5 (evaluate env 'x)))))

(deftest def-returns-value
  (let [env (atom {})] (is (= 5 (evaluate env '(def x 5))))))

(deftest def-supports-recursion
  (testing "a fn defined in an atom env can recursively call itself by name"
    (let [env (atom {})]
      (evaluate env '(def fact (fn [n] (if (= n 0) 1 (* n (fact (- n 1)))))))
      (is (= 120 (evaluate env '(fact 5)))))))

(deftest def-fails-on-immutable-env
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :immutable-env}
                     (evaluate {} '(def x 5)))))

(deftest call-of-unbound-symbol
  (testing "calling an unbound symbol bubbles :unbound-symbol from lookup"
    (is (thrown-match? clojure.lang.ExceptionInfo
                       {:type :unbound-symbol}
                       (evaluate {} '(banana 1 2))))))

(deftest nested-program
  (is (= 16
         (evaluate '{base 4}
                   '(let [n base m (* n n)] (if (> m 10) m :small))))))
