(ns katas.kata-02-temperature)

;; ─── Kata 2: Temperature ───────────────────────────────────────────────
;;
;; Pure arithmetic. The point is exactness: use exact ratios so integer
;; inputs that land on integers stay integers (never 100.0000001), and
;; practise multi-arity and chained comparisons.
;;
;; ── c->f / f->c ────────────────────────────────────────────────────────
;;
;; Convert Celsius to Fahrenheit and back. Use `9/5` and `5/9` (exact
;; ratios), not `1.8`/`0.555…`, so exact inputs round-trip exactly.
;;
;;   (c->f 0)    => 32
;;   (c->f 100)  => 212
;;   (c->f 37)   => 493/5       ; exact; (double …) => 98.6
;;   (f->c 32)   => 0
;;   (f->c 212)  => 100
;;   (f->c -40)  => -40
;;   (f->c (c->f 0))   => 0     ; exact round-trip
;;
;; ── comfortable? ───────────────────────────────────────────────────────
;;
;; Multi-arity predicate. With one argument, test whether a Celsius
;; temperature is in the default comfortable range 18..24 inclusive.
;; With three, test against an explicit inclusive [lo hi]. The
;; one-argument form should delegate to the three-argument form. Use the
;; chained comparison `(<= lo c hi)`.
;;
;;   (comfortable? 18)         => true
;;   (comfortable? 24)         => true
;;   (comfortable? 17)         => false
;;   (comfortable? 21 22 25)   => false
;;   (comfortable? 0 0 100)    => true

(defn c->f [c]
  ;; TODO
)

(defn f->c [f]
  ;; TODO
)

(defn comfortable? ([c]
                    ;; TODO
                   )
  ([c lo hi]
   ;; TODO
  ))
