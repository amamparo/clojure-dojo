(ns katas.kata-06-run-length-encoding)

;; ─── Kata 6: Run-length encoding ───────────────────────────────────────
;;
;; Collapse maximal runs of equal adjacent elements into [count element]
;; pairs, and invert that. `partition-by` gives you the runs directly;
;; `mapcat` + `repeat` rebuilds. Design the pair so they round-trip.
;;
;; ── encode ─────────────────────────────────────────────────────────────
;;
;; Return a vector of [run-length element] pairs, one per maximal run of
;; equal adjacent elements. Non-adjacent equal elements are separate
;; runs. Works on strings (a seq of chars) and on any seq.
;;
;;   (encode "aaabbbcca")  => [[3 \a] [3 \b] [2 \c] [1 \a]]
;;   (encode [1 1 1 2 2 1]) => [[3 1] [2 2] [1 1]]
;;   (encode "")           => []
;;   (encode [7])          => [[1 7]]
;;
;; ── decode ─────────────────────────────────────────────────────────────
;;
;; The inverse: expand each [n x] into n copies of x, concatenated. The
;; result is a *seq of elements* — decoding char runs yields chars, never
;; a re-joined string.
;;
;;   (decode [[3 \a] [1 \b]]) => (\a \a \a \b)
;;   (decode [[2 1] [3 2]])   => (1 1 2 2 2)
;;   (decode [])              => ()
;;
;; For every xs, `(decode (encode xs))` equals the original elements.

(defn encode [xs]
  ;; TODO
)

(defn decode [pairs]
  ;; TODO
)
