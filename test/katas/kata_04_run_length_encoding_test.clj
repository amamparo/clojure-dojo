(ns katas.kata-04-run-length-encoding-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-04-run-length-encoding :refer [encode decode]]))

(deftest encode-empty
  (is (= [] (encode "")))
  (is (= [] (encode [])))
  (is (= [] (encode nil))))

(deftest encode-single
  (is (= [[1 \a]] (encode "a")))
  (is (= [[1 :x]] (encode [:x]))))

(deftest encode-mixed
  (is (= [[3 \a] [2 \b] [1 \c]] (encode "aaabbc")))
  (is (= [[2 1] [3 2]]          (encode [1 1 2 2 2])))
  (is (= [[1 1] [1 2] [1 3]]    (encode [1 2 3]))))

(deftest decode-empty
  (is (= [] (decode []))))

(deftest decode-mixed
  (is (= [\a \a \a \b] (decode [[3 \a] [1 \b]])))
  (is (= [1 1 2 2 2]   (decode [[2 1] [3 2]]))))

(deftest round-trip
  (doseq [xs ["" "a" "abc" "aaabbc" "zzzzzzz"
              [] [1] [1 1 2 2 3]
              [:a :a :b :b :b :a]]]
    (is (= (vec xs) (decode (encode xs)))
        (str "xs=" (pr-str xs)))))
