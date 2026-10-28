(ns katas.kata-16-macros-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-16-macros :refer [when-let* infix]]))

(deftest single-binding-truthy (is (= 6 (when-let* [a 6] a))))

(deftest single-binding-falsey
  (is (nil? (when-let* [a nil] :unreached)))
  (is (nil? (when-let* [a false] :unreached))))

(deftest multiple-bindings-all-truthy (is (= 3 (when-let* [a 1 b 2] (+ a b)))))

(deftest later-bindings-see-earlier (is (= 6 (when-let* [a 2 b (* a 3)] b))))

(deftest each-init-is-evaluated-once-in-order
  (let [log (atom [])]
    (is (= [1 2]
           (when-let* [a (do (swap! log conj :a) 1)
                       b (do (swap! log conj :b) 2)]
             [a b])))
    (is (= [:a :b] @log))))

(deftest short-circuits-on-nil
  (let [log (atom [])]
    (is (nil? (when-let* [a (do (swap! log conj :a) 1)
                          b (do (swap! log conj :b) nil)
                          c (do (swap! log conj :c) :nope)]
                :unreached)))
    (is (= [:a :b] @log) "a binding after a nil RHS must NOT be evaluated")))

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
  (is (= 3 (when-let* [{:keys [x y]} {:x 1, :y 2}] (+ x y))))
  (is (nil? (when-let* [{:keys [x]} nil] :unreached))))

(deftest hygienic-does-not-capture
  (testing "the macro's internal temp must not shadow a caller's name"
    (is (= (into [1] (repeat 12 :outer))
           (let [v :outer
                 val :outer
                 value :outer
                 temp :outer
                 tmp :outer
                 t :outer
                 x :outer
                 g :outer
                 res :outer
                 result :outer
                 init :outer
                 test :outer]
             (when-let* [a 1]
               [a v val value temp tmp t x g res result init test]))))
    (is (= [1 2 3] (when-let* [v 1 temp 2 x 3] [v temp x])))))

(defn- root-cause [^Throwable t]
  (if-let [c (.getCause t)]
    (recur c)
    t))

(defn- expansion-root-cause [form]
  (try [:ok (macroexpand-1 form)]
       (catch Throwable t [:threw (root-cause t)])))

(deftest malformed-bindings-throw
  ;; The macro must throw IllegalArgumentException while expanding.
  ;; The compiler wraps a macro-expansion throw, so assert on the
  ;; underlying cause rather than the wrapper class.
  (testing "an odd number of binding forms is rejected at macroexpansion"
    (let [[status cause] (expansion-root-cause
                          '(katas.kata-16-macros/when-let* [a 1 b]))]
      (is (= :threw status) "macroexpansion must throw")
      (is (instance? IllegalArgumentException cause))))
  (testing "a non-vector bindings form is rejected at macroexpansion"
    (let [[status cause] (expansion-root-cause
                          '(katas.kata-16-macros/when-let* (a 1) :x))]
      (is (= :threw status) "macroexpansion must throw")
      (is (instance? IllegalArgumentException cause)))))

(deftest infix-leaves (is (= 1 (infix 1))) (is (= 42 (infix 42))))

(deftest infix-simple
  (is (= 3 (infix (1 + 2))))
  (is (= 6 (infix (2 * 3))))
  (is (= -1 (infix (3 - 4))))
  (is (= 4 (infix (8 / 2)))))

(deftest infix-nested
  (is (= 14 (infix (2 + (3 * 4)))))
  (is (= 2 (infix ((10 - 2) / (1 + 3)))))
  (is (= 25 (infix ((2 + 3) * (10 / 2))))))

(defmacro add-via-infix [a b] `(infix (~a + ~b)))

(deftest infix-rewrites-a-generated-list
  (testing "the form may come from another macro's expansion"
    (is (= 3 (add-via-infix 1 2)))))

(deftest infix-uses-surrounding-bindings
  (testing "operands are emitted as code, not evaluated by the macro"
    (let [x 5] (is (= 30 (infix (x * (x + 1))))))
    (let [a 10 b 4] (is (= 6 (infix (a - (b + 0))))))))
