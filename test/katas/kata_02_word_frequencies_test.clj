(ns katas.kata-02-word-frequencies-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as m]
            [katas.kata-02-word-frequencies :refer [word-frequencies top-n]]))

(deftest empty-string
  (is (match? (m/equals {}) (word-frequencies "")))
  (is (match? (m/equals {}) (word-frequencies "   "))))

(deftest single-word
  (is (match? (m/equals {"hello" 1}) (word-frequencies "hello")))
  (is (match? (m/equals {"hello" 1}) (word-frequencies "Hello")))
  (is (match? (m/equals {"hello" 1}) (word-frequencies "hello!"))))

(deftest case-insensitive
  (is (match? (m/equals {"hello" 3}) (word-frequencies "Hello hello HELLO"))))

(deftest punctuation-stripped
  (is (match? (m/equals {"hello" 2, "world" 2})
              (word-frequencies "Hello, hello world! World."))))

(deftest internal-punctuation-kept
  (testing "an apostrophe in the middle of a word stays"
    (is (match? (m/equals {"don't" 2}) (word-frequencies "don't, don't!")))))

(deftest top-n-ordering
  (is (match? [["the" 2] ["cat" 1]]
              (top-n "the cat sat on the mat" 2))))

(deftest top-n-ties-alphabetical
  (is (match? [["a" 1] ["b" 1] ["c" 1]]
              (top-n "c b a" 5))))

(deftest top-n-bounded
  (is (match? 2 (count (top-n "a b c d e" 2)))))

(deftest top-n-zero
  (is (match? [] (top-n "anything" 0))))

(deftest top-n-empty-input
  (is (match? [] (top-n "" 5))))
