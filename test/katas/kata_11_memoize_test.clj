(ns katas.kata-11-memoize-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-11-memoize :refer [my-memoize]]))

(deftest single-arg
  (let [m (my-memoize (fn [n] (* n n)))]
    (is (= 25 (m 5)))
    (is (= 100 (m 10)))))

(deftest multi-arg
  (let [m (my-memoize +)]
    (is (= 6 (m 1 2 3)))
    (is (= 10 (m 1 2 3 4)))))

(deftest no-args
  (let [m (my-memoize (fn [] 42))]
    (is (= 42 (m)))
    (is (= 42 (m)))))

(deftest caches-result
  (let [calls (atom 0)
        f (fn [n] (swap! calls inc) (* n n))
        m (my-memoize f)]
    (m 5)
    (m 5)
    (m 5)
    (is (= 1 @calls))
    (testing "different args trigger a new call" (m 6) (is (= 2 @calls)))))

(deftest caches-nil-result
  (testing "a function returning nil is still cached"
    (let [calls (atom 0)
          f (fn [_] (swap! calls inc) nil)
          m (my-memoize f)]
      (is (nil? (m :x)))
      (is (nil? (m :x)))
      (is (= 1 @calls)))))

(deftest caches-false-result
  (testing "a function returning false is still cached"
    (let [calls (atom 0)
          f (fn [_] (swap! calls inc) false)
          m (my-memoize f)]
      (is (false? (m :x)))
      (is (false? (m :x)))
      (is (= 1 @calls)))))

(deftest instances-are-independent
  (testing "each my-memoize call returns a function with its own cache"
    (let [calls (atom 0)
          f (fn [n] (swap! calls inc) n)
          m1 (my-memoize f)
          m2 (my-memoize f)]
      (m1 5)
      (m2 5)
      (is (= 2 @calls)))))

(deftest preserves-return-values
  (let [m (my-memoize identity)]
    (is (= :a (m :a)))
    (is (= [1 2 3] (m [1 2 3])))
    (is (= {:k :v} (m {:k :v})))
    (is (false? (m false)))
    (is (nil? (m nil)))))
