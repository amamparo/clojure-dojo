(ns katas.kata-02-temperature)

;; ─── Kata 2: Temperature ───────────────────────────────────────────────
;;
;; ── c->f / f->c ────────────────────────────────────────────────────────
;;
;; Convert Celsius to Fahrenheit and back.
;;
;;   (c->f 0)    => 32
;;   (c->f 100)  => 212
;;   (c->f 37)   => 493/5       ; exact; (double …) => 98.6
;;   (f->c 32)   => 0
;;   (f->c 212)  => 100
;;   (f->c -40)  => -40
;;   (f->c 100)  => 340/9
;;   (f->c (c->f 0))   => 0     ; exact round-trip
;;   (results may print as 32N, a BigInt; (= 32 32N) is true)
;;
;; ── comfortable? ───────────────────────────────────────────────────────
;;
;; Multi-arity predicate. With one argument, test whether a Celsius
;; temperature is in the default comfortable range 18..24 inclusive.
;; With three, test against an explicit inclusive [lo hi].
;;
;;   (comfortable? 18)         => true
;;   (comfortable? 24)         => true
;;   (comfortable? 17)         => false
;;   (comfortable? 21 22 25)   => false
;;   (comfortable? 26 22 25)   => false
;;   (comfortable? 0 0 100)    => true

(defn c->f [c]
  ;; TODO
)

(defn f->c [f]
  ;; TODO
)

(defn comfortable?
  ([c]
   ;; TODO
  )
  ([c lo hi]
   ;; TODO
  ))
