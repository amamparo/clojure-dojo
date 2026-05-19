(ns katas.kata-05-anagrams
  (:require [clojure.string :as str]))

;; ─── Kata 5: Anagrams ──────────────────────────────────────────────────
;;
;; Two words are anagrams if one is a rearrangement of the other's
;; letters, ignoring case. The trick is a *canonical form*: a normalized
;; representative that all equivalent inputs share. Pick one well and
;; `group-by` does the rest.
;;
;; ── canonical ──────────────────────────────────────────────────────────
;;
;;   (canonical word) → the same value for any two anagrams, a different
;;   value otherwise. Case-insensitive. (The sorted, lower-cased letters
;;   are one such form.)
;;
;;   (canonical "Listen") = (canonical "silent")  => true
;;   (canonical "abc")    = (canonical "cab")      => true
;;   (canonical "abc")    = (canonical "abd")      => false
;;
;; ── anagram-groups ─────────────────────────────────────────────────────
;;
;; Given a seq of words, return only the groups that actually contain an
;; anagram pair — i.e. drop singletons. Each group is a vector of the
;; original words (original casing, original order of first appearance).
;; The result is a vector of such groups, ordered by the position where
;; each group's first word appeared in the input.
;;
;;   (anagram-groups ["listen" "silent" "cat" "dog" "act" "god"])
;;     => [["listen" "silent"] ["cat" "act"] ["dog" "god"]]
;;
;;   (anagram-groups ["abc" "def" "ghi"])  => []
;;
;;   (anagram-groups ["Eric" "rice" "Rice" "ICE"])
;;     => [["Eric" "rice" "Rice"]]

(defn canonical [word]
  ;; TODO
)

(defn anagram-groups [words]
  ;; TODO
)
