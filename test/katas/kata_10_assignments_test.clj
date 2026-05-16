(ns katas.kata-10-assignments-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-10-assignments :refer [interpret]]))

(deftest empty-program
  (is (match? {} (interpret []))))

(deftest single-set
  (is (match? '{x 5} (interpret [[:set 'x 5]]))))

(deftest multiple-sets
  (is (match? '{x 5 y 3 z 7}
              (interpret [[:set 'x 5]
                          [:set 'y 3]
                          [:set 'z 7]]))))

(deftest set-can-rebind
  (is (match? '{x 8}
              (interpret [[:set 'x 5]
                          [:set 'x 8]]))))

(deftest add-uses-prior-bindings
  (is (match? '{x 5 y 3 sum 8}
              (interpret [[:set 'x 5]
                          [:set 'y 3]
                          [:add 'x 'y 'sum]]))))

(deftest mul-uses-prior-bindings
  (is (match? '{a 2 b 3 c 6}
              (interpret [[:set 'a 2]
                          [:set 'b 3]
                          [:mul 'a 'b 'c]]))))

(deftest chained-operations
  (testing "(2 * 3) = 6; 2 + 6 = 8"
    (is (match? '{a 2 b 3 c 6 d 8}
                (interpret [[:set 'a 2]
                            [:set 'b 3]
                            [:mul 'a 'b 'c]
                            [:add 'a 'c 'd]])))))

(deftest dest-can-overwrite-a-source
  (testing "the destination symbol may already be bound"
    (is (match? '{x 8 y 3}
                (interpret [[:set 'x 5]
                            [:set 'y 3]
                            [:add 'x 'y 'x]])))))

(deftest unbound-first-source-throws
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :unbound :symbol 'x}
                     (interpret [[:add 'x 'y 'z]]))))

(deftest unbound-second-source-throws
  (is (thrown-match? clojure.lang.ExceptionInfo
                     {:type :unbound :symbol 'missing}
                     (interpret [[:set 'x 5]
                                 [:mul 'x 'missing 'z]]))))
