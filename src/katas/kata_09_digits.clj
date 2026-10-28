(ns katas.kata-09-digits)

;; ─── Kata 9: Digits ────────────────────────────────────────────────────
;;
;; Inputs are non-negative integers, Long or BigInt, unless a function says
;; otherwise. collatz-length must follow chains of 100,000 steps and more
;; without overflowing the stack.
;;
;; ── digits ─────────────────────────────────────────────────────────────
;;
;; The base-10 digits as a vector, most significant first.
;;
;;   (digits 0)    => [0]
;;   (digits 1234) => [1 2 3 4]
;;   (digits 9009) => [9 0 0 9]
;;
;; ── digital-root ───────────────────────────────────────────────────────
;;
;; Replace n by the sum of its digits until one digit remains.
;;
;;   (digital-root 1234)      => 1
;;   (digital-root 123456789) => 9
;;
;; ── collatz-length ─────────────────────────────────────────────────────
;;
;; Number of steps to reach 1 under the Collatz map (n/2 if even, else
;; 3n+1). n is a positive integer; 0 never reaches 1.
;;
;;   (collatz-length 1)  => 0
;;   (collatz-length 3)  => 7
;;   (collatz-length 27) => 111

(defn digits [n]
  ;; TODO
)

(defn digital-root [n]
  ;; TODO
)

(defn collatz-length [n]
  ;; TODO
)
