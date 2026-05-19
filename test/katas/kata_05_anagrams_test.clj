(ns katas.kata-05-anagrams-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-05-anagrams :refer [canonical anagram-groups]]))

(deftest canonical-equal-for-anagrams
  (is (= (canonical "Listen") (canonical "silent")))
  (is (= (canonical "abc") (canonical "cab")))
  (is (= (canonical "Dormitory") (canonical "dirtyroom"))))

(deftest canonical-differs-for-non-anagrams
  (is (not= (canonical "abc") (canonical "abd")))
  (is (not= (canonical "abc") (canonical "abcd"))))

(deftest groups-basic
  (is (= [["listen" "silent"] ["cat" "act"] ["dog" "god"]]
         (anagram-groups ["listen" "silent" "cat" "dog" "act" "god"]))))

(deftest singletons-dropped
  (is (= [] (anagram-groups ["abc" "def" "ghi"])))
  (is (= [["ab" "ba"]] (anagram-groups ["ab" "zz" "ba" "qq"]))))

(deftest case-insensitive-grouping-keeps-original-casing
  (is (= [["Eric" "rice" "Rice"]]
         (anagram-groups ["Eric" "rice" "Rice" "ICE"]))))

(deftest group-order-follows-first-appearance
  (testing "groups appear in the order their first word first appeared"
    (is (= [["dog" "god"] ["cat" "act"]]
           (anagram-groups ["dog" "cat" "god" "act"])))))

(deftest within-group-order-is-input-order
  (is (= [["act" "cat" "tac"]] (anagram-groups ["act" "cat" "tac"]))))

(deftest empty-input (is (= [] (anagram-groups []))))
