(ns katas.kata-10-roman-numerals)

;; ─── Kata 10: Roman numerals ───────────────────────────────────────────
;;
;; Convert between integers (1–3999) and Roman numerals, both ways. The
;; clean approach is an ordered table of [value, symbol] pairs that
;; already includes the subtractive forms (CM, CD, XC, XL, IX, IV) — then
;; a `reduce`-style greedy walk over it. No special cases, no `cond`
;; ladder.
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
;; The inverse. Assume well-formed input (only the canonical forms
;; `->roman` produces).
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
