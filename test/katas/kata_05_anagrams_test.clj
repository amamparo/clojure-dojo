(ns katas.kata-05-anagrams-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-05-anagrams :refer [canonical anagram-groups]]))

(deftest canonical-is-an-equivalence-key
  (is (= (canonical "Listen") (canonical "silent")))
  (is (= (canonical "abc") (canonical "cab")))
  (is (= (canonical "Dormitory") (canonical "dirtyroom")))
  (is (not= (canonical "abc") (canonical "abd")))
  (is (not= (canonical "abc") (canonical "abcd")))
  (is (not= (canonical "aab") (canonical "abb")))
  (is (not= (canonical "ad") (canonical "bc"))))

(deftest groups-basic
  (is (= [["listen" "silent"] ["cat" "act"] ["dog" "god"]]
         (anagram-groups ["listen" "silent" "cat" "dog" "act" "god"]))))

(deftest result-is-a-vector-of-vectors
  (is (vector? (anagram-groups [])))
  (let [r (anagram-groups ["ab" "ba" "cd" "dc"])]
    (is (vector? r))
    (is (= [true true] (mapv vector? r)))))

(deftest singletons-dropped
  (is (= [] (anagram-groups ["abc" "def" "ghi"])))
  (is (= [["ab" "ba"]] (anagram-groups ["ab" "zz" "ba" "qq"])))
  (testing "same letter set, different counts, is not an anagram"
    (is (= [] (anagram-groups ["aab" "abb"])))))

(deftest case-insensitive-grouping-keeps-original-casing
  (is (= [["Eric" "rice" "Rice"]]
         (anagram-groups ["Eric" "rice" "Rice" "ICE"]))))

(deftest group-order-follows-first-appearance
  (testing "groups appear in the order their first word first appeared"
    (is (= [["ab" "ba"] ["cd" "dc"]] (anagram-groups ["ab" "cd" "dc" "ba"])))
    ;; 9 groups: past 8 keys, group-by's map order is no longer insertion
    ;; order.
    (is (= [["ab" "ba"] ["cd" "dc"] ["ef" "fe"] ["gh" "hg"] ["ij" "ji"]
            ["kl" "lk"] ["mn" "nm"] ["op" "po"] ["qr" "rq"]]
           (anagram-groups ["ab" "cd" "ef" "gh" "ij" "kl" "mn" "op" "qr"
                            "ba" "dc" "fe" "hg" "ji" "lk" "nm" "po" "rq"])))))

(deftest within-group-order-is-input-order
  (is (= [["tac" "act" "cat"]] (anagram-groups ["tac" "act" "cat"]))))

(deftest empty-input (is (= [] (anagram-groups []))))
