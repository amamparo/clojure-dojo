(ns katas.kata-06-run-length-encoding-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-06-run-length-encoding :refer [encode decode]]))

(deftest encode-string
  (is (= [[3 \a] [3 \b] [2 \c] [1 \a]] (encode "aaabbbcca"))))

(deftest encode-vector (is (= [[3 1] [2 2] [1 1]] (encode [1 1 1 2 2 1]))))

(deftest encode-edge-cases
  (is (= [] (encode "")))
  (is (= [] (encode [])))
  (is (= [[1 7]] (encode [7])))
  (is (= [[1 \a] [1 \b] [1 \c]] (encode "abc"))))

(deftest non-adjacent-equals-are-separate-runs
  (is (= [[1 1] [1 2] [1 1]] (encode [1 2 1]))))

(deftest decode-basic
  (is (= '(\a \a \a \b) (decode [[3 \a] [1 \b]])))
  (is (= '(1 1 2 2 2) (decode [[2 1] [3 2]]))))

(deftest decode-empty (is (= '() (decode []))))

(deftest decode-returns-elements-not-a-string
  (testing "decoding char runs yields a seq of chars, never a string"
    (is (= '(\x \x) (decode [[2 \x]])))
    (is (not (string? (decode [[2 \x]]))))))

(deftest round-trip
  (doseq [xs ["" "a" "aaabbbcca" "abcabc" "zzzzzzzzzz" [1 1 1 2 2 1] [:a :a :b]
              [7] (range 5)]]
    (is (= (or (seq xs) '()) (decode (encode xs)))
        (str "round-trip failed for " (pr-str xs)))))
