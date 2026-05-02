(ns katas.kata-12-when-let-star-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-12-when-let-star :refer [when-let*]]))

(deftest single-binding-truthy
  (is (= 6 (when-let* [a 6] a))))

(deftest single-binding-falsey
  (is (nil? (when-let* [a nil] :unreached)))
  (is (nil? (when-let* [a false] :unreached))))

(deftest multiple-bindings-all-truthy
  (is (= 3 (when-let* [a 1
                       b 2]
             (+ a b)))))

(deftest later-bindings-see-earlier
  (is (= 6 (when-let* [a 2
                       b (* a 3)]
             b))))

(deftest short-circuits-on-nil
  (let [evaluated? (atom false)]
    (when-let* [a 1
                b nil
                c (do (reset! evaluated? true) :nope)]
      :unreached)
    (is (false? @evaluated?)
        "binding after a nil RHS must NOT be evaluated")))

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
  (is (= 3 (when-let* [{:keys [x y]} {:x 1 :y 2}]
             (+ x y)))))

(deftest uneven-bindings-throws
  (testing "an odd number of binding forms is rejected at macroexpansion"
    (is (thrown? IllegalArgumentException
                 (macroexpand-1
                   '(katas.kata-12-when-let-star/when-let* [a 1 b]))))))
