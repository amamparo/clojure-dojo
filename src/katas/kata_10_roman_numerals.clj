(ns katas.kata-10-roman-numerals)

;; ─── Kata 10: Roman numerals ───────────────────────────────────────────
;;
;; Convert between integers 1–3999 and Roman numerals, both ways.
;;
;; Symbols: I 1, V 5, X 10, L 50, C 100, D 500, M 1000. Write symbols
;; largest first; no symbol repeats more than three times. Six pairs
;; subtract instead: IV 4, IX 9, XL 40, XC 90, CD 400, CM 900.
;;
;; ── ->roman ────────────────────────────────────────────────────────────
;;
;;   (->roman 1)     => "I"
;;   (->roman 4)     => "IV"
;;   (->roman 9)     => "IX"
;;   (->roman 58)    => "LVIII"      ; 50 + 5 + 1 + 1 + 1
;;   (->roman 1994)  => "MCMXCIV"    ; 1000 + 900 + 90 + 4
;;   (->roman 3888)  => "MMMDCCCLXXXVIII"
;;
;; ── roman-> ────────────────────────────────────────────────────────────
;;
;; The inverse. Assume well-formed input (canonical forms only).
;;
;;   (roman-> "I")        => 1
;;   (roman-> "IV")       => 4
;;   (roman-> "MCMXCIV")  => 1994
;;
;; ── round-trip ─────────────────────────────────────────────────────────
;;
;; For every n in 1..3999, `(roman-> (->roman n))` must equal n. The
;; tests check this across the whole range.

(defn ->roman [n]
  ;; TODO
)

(defn roman-> [s]
  ;; TODO
)
