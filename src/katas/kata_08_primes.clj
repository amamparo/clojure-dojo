(ns katas.kata-08-primes)

;; ─── Kata 8: Primes ────────────────────────────────────────────────────
;;
;; Build an infinite lazy seq of primes. `primes` is the seq itself;
;; `nth-prime` is a lookup into it, NOT a second search. Filtering an
;; infinite generator stays lazy and infinite — forcing a deep prefix
;; must neither hang nor overflow the stack.
;;
;; ── prime? ─────────────────────────────────────────────────────────────
;;
;; True iff n is a prime integer (> 1, no divisor in 2..√n).
;;
;;   (prime? 2)  => true
;;   (prime? 97) => true
;;   (prime? 1)  => false
;;   (prime? 91) => false        ; 7 * 13
;;
;; ── primes ─────────────────────────────────────────────────────────────
;;
;; The infinite, lazy, ascending seq of all primes. A `def` may hold it
;; directly. (Stubbed as nil — replace the body.)
;;
;;   (take 5 primes)               => (2 3 5 7 11)
;;   (take-while #(< % 30) primes) => (2 3 5 7 11 13 17 19 23 29)
;;   (nth primes 999)              => 7919
;;
;; ── nth-prime ──────────────────────────────────────────────────────────
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
