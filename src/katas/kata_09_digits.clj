(ns katas.kata-09-digits)

;; ─── Kata 9: Digits ────────────────────────────────────────────────────
;;
;; Integer-arithmetic recursion with `loop`/`recur` and `quot`/`rem`.
;; Each function carries an accumulator that does not fit a plain fold;
;; deep iterations MUST use `recur`, not self-calls, so a long chain does
;; not overflow the stack. Inputs are non-negative integers.
;;
;; ── digits ─────────────────────────────────────────────────────────────
;;
;; The base-10 digits, most significant first.
;;
;;   (digits 0)    => [0]
;;   (digits 1234) => [1 2 3 4]
;;   (digits 9009) => [9 0 0 9]
;;
;; ── digit-sum ──────────────────────────────────────────────────────────
;;
;; The sum of the digits.
;;
;;   (digit-sum 0)    => 0
;;   (digit-sum 1234) => 10
;;   (digit-sum 999999) => 54
;;
;; ── digital-root ───────────────────────────────────────────────────────
;;
;; Repeatedly apply digit-sum until a single digit remains.
;;
;;   (digital-root 1234)      => 1
;;   (digital-root 123456789) => 9
;;
;; ── collatz-length ─────────────────────────────────────────────────────
;;
;; Number of steps to reach 1 under the Collatz map (n/2 if even, else
;; 3n+1). (collatz-length 1) => 0.
;;
;;   (collatz-length 3)  => 7
;;   (collatz-length 27) => 111

(defn digits [n]
  ;; TODO
)

(defn digit-sum [n]
  ;; TODO
)

(defn digital-root [n]
  ;; TODO
)

(defn collatz-length [n]
  ;; TODO
)
