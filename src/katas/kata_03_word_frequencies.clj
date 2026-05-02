(ns katas.kata-03-word-frequencies)

;; ─── Kata 3: Word frequencies ──────────────────────────────────────────
;;
;; You are learning: maps as data, `reduce` (and meeting `frequencies`),
;; sorting with custom comparators, and normalising input.
;;
;; A "word" is any non-empty token after:
;;   1. splitting on whitespace,
;;   2. lower-casing,
;;   3. trimming the characters .,!?;:"'()[] from each end.
;;
;;   (word-frequencies "")            => {}
;;   (word-frequencies "Hello world") => {"hello" 1, "world" 1}
;;   (word-frequencies "Hello, hello world! World.")
;;     => {"hello" 2, "world" 2}
;;
;; `top-n` returns a vector of `[word count]` pairs of length at most n,
;; sorted by count (descending), with ties broken alphabetically (asc).
;;
;;   (top-n "the cat sat on the mat" 2)
;;     => [["the" 2] ["cat" 1]]
;;   (top-n "c b a" 5)
;;     => [["a" 1] ["b" 1] ["c" 1]]
;;
;; If two pairs have the same count, the word that sorts earlier (with
;; `compare`) comes first.

(defn word-frequencies [s]
  ;; TODO
  )

(defn top-n [s n]
  ;; TODO
  )
