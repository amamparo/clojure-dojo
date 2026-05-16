(ns katas.kata-07-roman-numerals)

;; ─── Kata 7: Roman numerals ────────────────────────────────────────────
;;
;; You are learning: lookup tables, `reduce`, threading, recursion, and
;; round-trip thinking — writing two functions that should be inverses.
;;
;; Roman symbols:
;;   I=1  V=5  X=10  L=50  C=100  D=500  M=1000
;; Subtractive forms:
;;   IV=4  IX=9  XL=40  XC=90  CD=400  CM=900
;;
;; Implement two functions, defined for integers in the range 1..3999.
;;
;;   (int->roman 4)         => "IV"
;;   (int->roman 1994)      => "MCMXCIV"
;;   (int->roman 3999)      => "MMMCMXCIX"
;;
;;   (roman->int "IV")      => 4
;;   (roman->int "MCMXCIV") => 1994
;;
;; The two functions must round-trip:
;;   (-> n int->roman roman->int) = n  for all n in 1..3999.

(defn int->roman [n]
  ;; TODO
  )

(defn roman->int [s]
  ;; TODO
  )
