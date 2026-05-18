(ns katas.kata-02-word-frequencies-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-02-word-frequencies :refer [word-frequencies top-n]]))

(deftest empty-string
  (is (= {} (word-frequencies "")))
  (is (= {} (word-frequencies "   "))))

(deftest single-word
  (is (= {"hello" 1} (word-frequencies "hello")))
  (is (= {"hello" 1} (word-frequencies "Hello")))
  (is (= {"hello" 1} (word-frequencies "hello!"))))

(deftest case-insensitive
  (is (= {"hello" 3} (word-frequencies "Hello hello HELLO"))))

(deftest punctuation-stripped
  (is (= {"hello" 2, "world" 2}
         (word-frequencies "Hello, hello world! World."))))

(deftest internal-punctuation-kept
  (testing "an apostrophe in the middle of a word stays"
    (is (= {"don't" 2} (word-frequencies "don't, don't!")))))

(deftest top-n-ordering
  (is (= [["the" 2] ["cat" 1]] (top-n "the cat sat on the mat" 2))))

(deftest top-n-ties-alphabetical
  (is (= [["a" 1] ["b" 1] ["c" 1]] (top-n "c b a" 5))))

(deftest top-n-bounded (is (= 2 (count (top-n "a b c d e" 2)))))

(deftest top-n-zero (is (= [] (top-n "anything" 0))))

(deftest top-n-empty-input (is (= [] (top-n "" 5))))
