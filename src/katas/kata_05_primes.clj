(ns katas.kata-05-primes)

;; ─── Kata 5: Lazy primes ──────────────────────────────────────────────
;;
;; `prime?`: predicate on a non-negative integer.
;;
;;   (prime? 0)  => false
;;   (prime? 1)  => false
;;   (prime? 2)  => true
;;   (prime? 97) => true
;;   (prime? 99) => false
;;
;; `primes`: a lazy seq of all prime numbers, in ascending order.
;;
;;   (take 10 primes)   => (2 3 5 7 11 13 17 19 23 29)
;;   (nth primes 24)    => 97        ; the 25th prime, 0-indexed
;;
;; Constraint: `primes` MUST be lazy. Do not bake in an upper bound.

(defn prime? [n]
  ;; TODO
  false)

(def primes
  ;; TODO: a lazy seq of all primes in ascending order
  ())
