(ns katas.kata-03-word-frequencies
  (:require [clojure.string :as str]))

;; ─── Kata 3: Word frequencies ──────────────────────────────────────────
;;
;; Count words in a line of text. First with a pipeline; then a sorted
;; view of the result.
;;
;; ── Part A — `word-counts` ─────────────────────────────────────────────
;;
;; Given a string, return a map of word → occurrence count. Words are
;; maximal runs of letters/digits, compared case-insensitively;
;; punctuation and surrounding whitespace are separators and never
;; appear in a word. An empty or punctuation-only string has no words.
;;
;;   (word-counts "the cat the dog")
;;     => {"the" 2, "cat" 1, "dog" 1}
;;   (word-counts "The CAT, the cat!")
;;     => {"the" 2, "cat" 2}
;;   (word-counts "")        => {}
;;   (word-counts "  ...  ") => {}
;;
;; Hint: `(str/split s #"[^a-z0-9]+")` splits on runs of non-word
;; characters — but lower-case first, and note that splitting a string
;; that starts with a separator yields a leading "" you must drop.
;;
;; ── Part B — `top-n` ───────────────────────────────────────────────────
;;
;; Return the `n` most frequent words as a vector of [word count] pairs,
;; highest count first. Break ties alphabetically by the word (ascending),
;; so the result is fully deterministic.
;;
;;   (top-n "the cat the dog the bird a cat" 2)
;;     => [["the" 3] ["cat" 2]]
;;   (top-n "b b a a c" 3)
;;     => [["a" 2] ["b" 2] ["c" 1]]
;;   (top-n "" 5) => []

(defn word-counts [s]
  ;; TODO
)

(defn top-n [s n]
  ;; TODO
)
