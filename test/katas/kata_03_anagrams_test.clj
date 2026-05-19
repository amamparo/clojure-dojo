(ns katas.kata-03-anagrams-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-03-anagrams :refer [anagrams? group-anagrams]]))

(deftest anagrams?-true-cases
  (is (anagrams? "listen" "silent"))
  (is (anagrams? "abc" "cab"))
  (is (anagrams? "" "")))

(deftest anagrams?-case-insensitive
  (is (anagrams? "Listen" "Silent"))
  (is (anagrams? "Tea" "EAT")))

(deftest anagrams?-false-cases
  (is (not (anagrams? "abc" "abd")))
  (is (not (anagrams? "abc" "ab")))
  (is (not (anagrams? "aab" "abb"))))

(deftest group-anagrams-empty (is (= [] (group-anagrams []))))

(deftest group-anagrams-singletons
  (is (= [["a"] ["b"] ["c"]] (group-anagrams ["a" "b" "c"]))))

(deftest group-anagrams-mixed
  (is (= [["eat" "tea" "ate"] ["tan" "nat"] ["bat"]]
         (group-anagrams ["eat" "tea" "tan" "ate" "nat" "bat"]))))

(deftest group-anagrams-preserves-group-order
  (testing "groups appear in the order their first member appears"
    (is (= [["bat"] ["eat" "tea"] ["tan" "nat"]]
           (group-anagrams ["bat" "eat" "tea" "tan" "nat"])))))

(deftest group-anagrams-preserves-within-group-order
  (is (= [["abc" "cba" "bac"]] (group-anagrams ["abc" "cba" "bac"]))))
