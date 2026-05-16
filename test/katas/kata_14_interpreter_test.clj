(ns katas.kata-14-interpreter-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-14-interpreter :refer [evaluate]]))

(deftest self-evaluating
  (is (match? 42    (evaluate {} 42)))
  (is (match? "hi"  (evaluate {} "hi")))
  (is (match? true  (evaluate {} true)))
  (is (match? false (evaluate {} false)))
  (is (match? nil   (evaluate {} nil)))
  (is (match? :kw   (evaluate {} :kw))))

(deftest symbol-lookup
  (is (match? 10    (evaluate '{x 10} 'x)))
  (is (match? "abc" (evaluate '{s "abc"} 's))))

(deftest unbound-symbol
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :unbound-symbol}
                     (evaluate {} 'missing))))

(deftest arithmetic
  (is (match? 6  (evaluate {} '(+ 1 2 3))))
  (is (match? -4 (evaluate {} '(- 1 2 3))))
  (is (match? 24 (evaluate {} '(* 2 3 4))))
  (is (match? 5  (evaluate {} '(+ 2 3))))
  (is (match? 2  (evaluate {} '(/ 8 2 2)))))

(deftest comparisons
  (is (true?  (evaluate {} '(= 1 1))))
  (is (false? (evaluate {} '(= 1 2))))
  (is (true?  (evaluate {} '(< 1 2))))
  (is (false? (evaluate {} '(< 2 1))))
  (is (true?  (evaluate {} '(> 2 1)))))

(deftest if-truthy
  (is (match? :yes (evaluate {} '(if true :yes :no))))
  (is (match? :no  (evaluate {} '(if false :yes :no))))
  (is (match? :no  (evaluate {} '(if nil :yes :no)))))

(deftest if-evaluates-its-condition
  (is (match? :yes (evaluate {} '(if (< 1 2) :yes :no)))))

(deftest do-form
  (is (match? 4 (evaluate {} '(do 1 2 3 4))))
  (is (nil? (evaluate {} '(do)))))

(deftest let-sequential
  (is (match? 3 (evaluate {} '(let [x 1, y 2] (+ x y)))))
  (is (match? 9 (evaluate {} '(let [x 1, y (+ x 2), z (* y 3)] z)))))

(deftest let-shadows
  (is (match? 99 (evaluate '{x 1} '(let [x 99] x))))
  (testing "shadow does not leak outside the let"
    (is (match? 1 (evaluate '{x 1} '(do (let [x 99] x) x))))))

(deftest let-implicit-do
  (is (match? :last (evaluate {} '(let [x 1] :first :middle :last)))))

(deftest unknown-form
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :unknown-form}
                     (evaluate {} '(banana 1 2)))))

(deftest nested-program
  (is (match? 16
              (evaluate '{base 4}
                        '(let [n base
                               m (* n n)]
                           (if (> m 10) m :small))))))
