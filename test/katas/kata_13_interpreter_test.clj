(ns katas.kata-13-interpreter-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-13-interpreter :refer [evaluate]]))

(deftest self-evaluating
  (is (= 42    (evaluate {} 42)))
  (is (= "hi"  (evaluate {} "hi")))
  (is (= true  (evaluate {} true)))
  (is (= false (evaluate {} false)))
  (is (= nil   (evaluate {} nil)))
  (is (= :kw   (evaluate {} :kw))))

(deftest symbol-lookup
  (is (= 10    (evaluate '{x 10} 'x)))
  (is (= "abc" (evaluate '{s "abc"} 's))))

(deftest unbound-symbol
  (try (evaluate {} 'missing)
       (is false "should have thrown")
       (catch clojure.lang.ExceptionInfo e
         (is (= :unbound-symbol (:type (ex-data e)))))))

(deftest arithmetic
  (is (= 6  (evaluate {} '(+ 1 2 3))))
  (is (= -4 (evaluate {} '(- 1 2 3))))
  (is (= 24 (evaluate {} '(* 2 3 4))))
  (is (= 5  (evaluate {} '(+ 2 3))))
  (is (= 2  (evaluate {} '(/ 8 2 2)))))

(deftest comparisons
  (is (true?  (evaluate {} '(= 1 1))))
  (is (false? (evaluate {} '(= 1 2))))
  (is (true?  (evaluate {} '(< 1 2))))
  (is (false? (evaluate {} '(< 2 1))))
  (is (true?  (evaluate {} '(> 2 1)))))

(deftest if-truthy
  (is (= :yes (evaluate {} '(if true :yes :no))))
  (is (= :no  (evaluate {} '(if false :yes :no))))
  (is (= :no  (evaluate {} '(if nil :yes :no)))))

(deftest if-evaluates-its-condition
  (is (= :yes (evaluate {} '(if (< 1 2) :yes :no)))))

(deftest do-form
  (is (= 4   (evaluate {} '(do 1 2 3 4))))
  (is (nil? (evaluate {} '(do)))))

(deftest let-sequential
  (is (= 3 (evaluate {} '(let [x 1, y 2] (+ x y)))))
  (is (= 9 (evaluate {} '(let [x 1, y (+ x 2), z (* y 3)] z)))))

(deftest let-shadows
  (is (= 99 (evaluate '{x 1} '(let [x 99] x))))
  (testing "shadow does not leak outside the let"
    (is (= 1 (evaluate '{x 1} '(do (let [x 99] x) x))))))

(deftest let-implicit-do
  (is (= :last (evaluate {} '(let [x 1] :first :middle :last)))))

(deftest unknown-form
  (try (evaluate {} '(banana 1 2))
       (is false "should have thrown")
       (catch clojure.lang.ExceptionInfo e
         (is (= :unknown-form (:type (ex-data e)))))))

(deftest nested-program
  (is (= 16
         (evaluate '{base 4}
                   '(let [n base
                          m (* n n)]
                      (if (> m 10) m :small))))))
