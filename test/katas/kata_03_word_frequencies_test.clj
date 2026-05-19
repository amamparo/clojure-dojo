(ns katas.kata-03-word-frequencies-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-03-word-frequencies :refer [word-counts top-n]]))

(deftest counts-simple
  (is (= {"the" 2, "cat" 1, "dog" 1} (word-counts "the cat the dog"))))

(deftest counts-case-insensitively
  (is (= {"the" 2, "cat" 2} (word-counts "The CAT, the cat!"))))

(deftest punctuation-is-a-separator
  (is (= {"well" 1, "hello" 1, "there" 1}
         (word-counts "well... hello---there"))))

(deftest digits-are-word-characters
  (is (= {"abc123" 1, "x" 2} (word-counts "abc123 x x"))))

(deftest empty-and-punctuation-only
  (is (= {} (word-counts "")))
  (is (= {} (word-counts "   ")))
  (is (= {} (word-counts " ... -- !! "))))

(deftest leading-separator-no-empty-word
  (testing "a leading separator must not produce an empty-string word"
    (is (= {"hi" 1} (word-counts "  hi")))
    (is (not (contains? (word-counts ",hello world.") "")))))

(deftest top-n-orders-by-count
  (is (= [["the" 3] ["cat" 2]] (top-n "the cat the dog the bird a cat" 2))))

(deftest top-n-breaks-ties-alphabetically
  (is (= [["a" 2] ["b" 2] ["c" 1]] (top-n "b b a a c" 3))))

(deftest top-n-fewer-than-n-available (is (= [["only" 1]] (top-n "only" 5))))

(deftest top-n-empty (is (= [] (top-n "" 5))))
