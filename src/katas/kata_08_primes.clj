(ns katas.kata-08-primes)

;; ─── Kata 8: Primes ────────────────────────────────────────────────────
;;
;; ── prime? ─────────────────────────────────────────────────────────────
;;
;; True iff n is prime: an integer > 1 whose only divisors are 1 and n.
;;
;;   (prime? 2)  => true
;;   (prime? 97) => true
;;   (prime? 1)  => false
;;   (prime? 91) => false        ; 7 * 13
;;
;; ── primes ─────────────────────────────────────────────────────────────
;;
;; The infinite, lazy, ascending seq of all primes.
;; (Stubbed as nil — replace the body.)
;;
;;   (take 5 primes)               => (2 3 5 7 11)
;;   (take-while #(< % 30) primes) => (2 3 5 7 11 13 17 19 23 29)
;;   (nth primes 999)              => 7919
;;   (nth primes 9999)             => 104729
;;   (first (filter #(> % 1000000) primes)) => 1000003
;;
;; ── nth-prime ──────────────────────────────────────────────────────────
;;
;; The nth prime, counting from 0. n is a non-negative integer.
;;
;;   (nth-prime 0)   => 2
;;   (nth-prime 10)  => 31
;;   (nth-prime 100) => 547

(defn prime? [n]
  ;; TODO
)

(def primes
  ;; TODO
  nil)

(defn nth-prime [n]
  ;; TODO
)
