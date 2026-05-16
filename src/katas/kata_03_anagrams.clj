(ns katas.kata-03-anagrams
  (:require [clojure.string :as str]))

;; ─── Kata 3: Anagrams ──────────────────────────────────────────────────
;;
;; Two strings are anagrams iff one is a rearrangement of the letters of
;; the other (case-insensitive). Treat the entire string as letters; do
;; not strip whitespace or punctuation.
;;
;; `anagrams?` is true when its two arguments are anagrams of each other.
;;
;;   (anagrams? "listen" "silent") => true
;;   (anagrams? "Listen" "Silent") => true
;;   (anagrams? "abc"    "abd")    => false
;;   (anagrams? "abc"    "ab")     => false
;;   (anagrams? ""       "")       => true
;;
;; `group-anagrams` groups a sequence of strings into vectors of mutual
;; anagrams. Groups appear in the order of their first member; strings
;; inside a group appear in input order.
;;
;;   (group-anagrams ["eat" "tea" "tan" "ate" "nat" "bat"])
;;     => [["eat" "tea" "ate"] ["tan" "nat"] ["bat"]]
;;
;;   (group-anagrams []) => []
;;
;; Hint: pick a canonical form (e.g. the sorted lower-cased letters) and
;; let `group-by` do the heavy lifting.

(defn anagrams? [a b]
  ;; TODO
  )

(defn group-anagrams [strings]
  ;; TODO
  )
