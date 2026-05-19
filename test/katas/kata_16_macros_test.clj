(ns katas.kata-16-macros-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-16-macros :refer [when-let* infix]]))

(deftest single-binding-truthy (is (= 6 (when-let* [a 6] a))))

(deftest single-binding-falsey
  (is (nil? (when-let* [a nil] :unreached)))
  (is (nil? (when-let* [a false] :unreached))))

(deftest multiple-bindings-all-truthy (is (= 3 (when-let* [a 1 b 2] (+ a b)))))

(deftest later-bindings-see-earlier (is (= 6 (when-let* [a 2 b (* a 3)] b))))

(deftest short-circuits-on-nil
  (let [evaluated? (atom false)]
    (when-let* [a 1 b nil c (do (reset! evaluated? true) :nope)] :unreached)
    (is (false? @evaluated?) "binding after a nil RHS must NOT be evaluated")))

(deftest short-circuits-on-false
  (is (nil? (when-let* [a 1
                        b false
                        c (throw (ex-info "must not run" {}))]
              :unreached))))

(deftest body-is-implicit-do
  (let [seen (atom [])]
    (when-let* [a 1]
      (swap! seen conj :one)
      (swap! seen conj :two)
      :result)
    (is (= [:one :two] @seen))))

(deftest destructuring-lhs
  (is (= 3 (when-let* [{:keys [x y]} {:x 1, :y 2}] (+ x y)))))

(deftest hygienic-does-not-capture
  (testing "a user binding named like an internal temp still works"
    (is (= [1 2] (when-let* [temp 1 other 2] [temp other])))))

(defn- root-cause [^Throwable t]
  (if-let [c (.getCause t)]
    (recur c)
    t))

(deftest uneven-bindings-throws
  (testing "an odd number of binding forms is rejected at macroexpansion"
    ;; The macro must throw IllegalArgumentException while expanding.
    ;; The compiler wraps a macro-expansion throw, so assert on the
    ;; underlying cause rather than the wrapper class.
    (let [outcome
          (try [:ok (macroexpand-1 '(katas.kata-16-macros/when-let* [a 1 b]))]
               (catch Throwable t [:threw (root-cause t)]))]
      (is (= :threw (first outcome)) "macroexpansion must throw")
      (is (instance? IllegalArgumentException (second outcome))))))

(deftest infix-atoms (is (= 1 (infix 1))) (is (= 42 (infix 42))))

(deftest infix-simple
  (is (= 3 (infix (1 + 2))))
  (is (= 6 (infix (2 * 3))))
  (is (= -1 (infix (3 - 4))))
  (is (= 4 (infix (8 / 2)))))

(deftest infix-nested
  (is (= 14 (infix (2 + (3 * 4)))))
  (is (= 2 (infix ((10 - 2) / (1 + 3)))))
  (is (= 25 (infix ((2 + 3) * (10 / 2))))))

(deftest infix-uses-surrounding-bindings
  (testing "operands are emitted as code, not evaluated by the macro"
    (let [x 5] (is (= 30 (infix (x * (x + 1))))))
    (let [a 10 b 4] (is (= 6 (infix (a - (b + 0))))))))
