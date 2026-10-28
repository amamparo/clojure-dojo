(ns katas.kata-05-anagrams)

;; ─── Kata 5: Anagrams ──────────────────────────────────────────────────
;;
;; Two words are anagrams if one is a rearrangement of the other's
;; letters, ignoring case.
;;
;; ── canonical ──────────────────────────────────────────────────────────
;;
;;   (canonical word) → the same value for any two anagrams, a different
;;   value otherwise. Case-insensitive.
;;
;;   (= (canonical "Listen") (canonical "silent"))  => true
;;   (= (canonical "abc") (canonical "cab"))        => true
;;   (= (canonical "abc") (canonical "abd"))        => false
;;   (= (canonical "aab") (canonical "abb"))        => false
;;
;; ── anagram-groups ─────────────────────────────────────────────────────
;;
;; Given a seq of words, return only the groups that actually contain an
;; anagram pair — i.e. drop singletons. Each group is a vector of the
;; original words. Groups are ordered by the position of each group's
;; first word in the input; within a group, words keep input order and
;; original casing. The result is a vector of such groups.
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
