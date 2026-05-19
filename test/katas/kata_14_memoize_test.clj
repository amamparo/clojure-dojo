(ns katas.kata-14-memoize-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [katas.kata-14-memoize :refer [my-memoize memoize-with]]))

(deftest returns-same-results
  (let [f (my-memoize (fn [x] (* x x)))]
    (is (= 16 (f 4)))
    (is (= 25 (f 5)))
    (is (= 0 (f 0)))))

(deftest computes-once-per-arg
  (let [calls (atom 0)
        f (my-memoize (fn [x] (swap! calls inc) (* x x)))]
    (is (= 16 (f 4)))
    (is (= 16 (f 4)))
    (is (= 16 (f 4)))
    (is (= 1 @calls))
    (is (= 25 (f 5)))
    (is (= 2 @calls))))

(deftest supports-multiple-arities
  (let [calls (atom 0)
        f (my-memoize (fn [& xs] (swap! calls inc) (apply + xs)))]
    (is (= 6 (f 1 2 3)))
    (is (= 6 (f 1 2 3)))
    (is (= 10 (f 1 2 3 4)))
    (is (= 0 (f)))
    (is (= 0 (f)))
    (is (= 3 @calls))))

(deftest caches-falsey-results
  (testing "a cached nil/false must not be recomputed"
    (let [calls (atom 0)
          f (my-memoize (fn [_] (swap! calls inc) nil))]
      (is (nil? (f :x)))
      (is (nil? (f :x)))
      (is (= 1 @calls)))
    (let [calls (atom 0)
          g (my-memoize (fn [_] (swap! calls inc) false))]
      (is (false? (g :y)))
      (is (false? (g :y)))
      (is (= 1 @calls)))))

(deftest distinct-memoized-fns-have-distinct-caches
  (let [a (my-memoize (constantly 1))
        b (my-memoize (constantly 2))]
    (is (= 1 (a :k)))
    (is (= 2 (b :k)))))

(deftest concurrent-calls-keep-cache
  (testing "the cache is not lost under concurrent first-calls"
    (let [calls (atom 0)
          f (my-memoize (fn [x] (swap! calls inc) (* x 2)))
          tasks (doall (for [_ (range 500)] (future (f 21))))]
      (is (every? #{42} (map deref tasks)))
      (is (= 42 (f 21))))))

(deftest memoize-with-collapses-keys
  (let [calls (atom 0)
        f (memoize-with count (fn [s] (swap! calls inc) (str/upper-case s)))]
    (is (= "AB" (f "ab")))
    (is (= "AB" (f "cd")))
    (testing "same key (count 2) → body ran only once" (is (= 1 @calls)))
    (is (= "XYZ" (f "xyz")))
    (is (= 2 @calls))))
