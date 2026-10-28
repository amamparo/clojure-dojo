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
    (is (= 2 @calls))
    (is (= 16 (f 4)))
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
          tasks (doall (for [i (range 1000)] (future (f i))))]
      (is (= [] (take 5 (remove #(= (* 2 %) @(nth tasks %)) (range 1000))))
          "first five args whose call returned a wrong result")
      (let [n @calls]
        (run! f (range 1000))
        (is (= n @calls)
            "every key first computed concurrently stays cached")))))

(deftest memoize-with-collapses-keys
  (let [calls (atom 0)
        f (memoize-with count (fn [s] (swap! calls inc) (str/upper-case s)))]
    (is (= "AB" (f "ab")))
    (is (= "AB" (f "cd")))
    (testing "same key (count 2) → body ran only once" (is (= 1 @calls)))
    (is (= "XYZ" (f "xyz")))
    (is (= 2 @calls))
    (is (= "AB" (f "ef")))
    (is (= 2 @calls))))

(deftest memoize-with-caches-falsey-results
  (let [calls (atom 0)
        f (memoize-with identity (fn [_] (swap! calls inc) nil))]
    (is (nil? (f :x)))
    (is (nil? (f :x)))
    (is (= 1 @calls))))

(deftest memoize-with-fns-have-distinct-caches
  (let [a (memoize-with identity (constantly 1))
        b (memoize-with identity (constantly 2))]
    (is (= 1 (a :k)))
    (is (= 2 (b :k)))))
