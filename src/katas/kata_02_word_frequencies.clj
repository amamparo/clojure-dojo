(ns katas.kata-02-word-frequencies
  (:require [clojure.string :as str]))

;; ─── Kata 2: Word frequencies ──────────────────────────────────────────
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

(defn clean [word]
  (-> word
      str/lower-case
      (str/replace #"^[.,!?;:\"'()\[\]]+|[.,!?;:\"'()\[\]]+$" "")))

(defn words [string]
  (->> (str/split string #"\s+")
       (map clean)
       (remove empty?)))

(defn word-frequencies [string] (frequencies (words string)))

(defn top-n [string limit]
  (into []
        (take limit)
        (sort-by (juxt #(- (val %)) key) (word-frequencies string))))