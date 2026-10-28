(ns katas.kata-03-word-frequencies)

;; ─── Kata 3: Word frequencies ──────────────────────────────────────────
;;
;; Count words in a line of text; then rank them by frequency.
;;
;; ── word-counts ────────────────────────────────────────────────────────
;;
;; Given a string, return a map of word → occurrence count. Words are
;; maximal runs of ASCII letters/digits (`a-z`, `0-9` after lower-casing),
;; compared case-insensitively; every other character separates words and
;; never appears in one. An empty or punctuation-only string has no words.
;;
;;   (word-counts "the cat the dog")
;;     => {"the" 2, "cat" 1, "dog" 1}
;;   (word-counts "The CAT, the cat!")
;;     => {"the" 2, "cat" 2}
;;   (word-counts "")        => {}
;;   (word-counts "  ...  ") => {}
;;
;; ── top-n ──────────────────────────────────────────────────────────────
;;
;; Return the `n` most frequent words as a vector of [word count] pairs,
;; highest count first. Break ties alphabetically by the word (ascending),
;; so the result is fully deterministic.
;;
;;   (top-n "the cat the dog the bird a cat" 2)
;;     => [["the" 3] ["cat" 2]]
;;   (top-n "b b a a c" 3)
;;     => [["a" 2] ["b" 2] ["c" 1]]
;;   (top-n "only" 5) => [["only" 1]]
;;   (top-n "" 5) => []

(defn word-counts [s]
  ;; TODO
)

(defn top-n [s n]
  ;; TODO
)
