(ns katas.kata-13-shapes)

;; ─── Kata 13: Shapes ──────────────────────────────────────
;;
;; ── Part A: protocol & records ──────────────────────────────
;;
;; The `Shape` protocol has two methods: `area` and `perimeter`.
;; Implement records `Circle` and `Rectangle` that satisfy it.
;;
;;   (area      (->Circle 1))           ; => Math/PI         (≈ 3.14159…)
;;   (perimeter (->Circle 1))           ; => (* 2 Math/PI)
;;   (area      (->Rectangle 3 4))      ; => 12
;;   (perimeter (->Rectangle 3 4))      ; => 14
;;
;; ── Part B: multimethod ────────────────────────────────────
;;
;; `describe` dispatches on a shape map's `:kind` key and returns a short
;; human-readable string. Implement it for four shapes — circle, rectangle,
;; triangle, square — plus a default for unknown :kinds. Notice that the
;; multimethod axis covers four shapes; the protocol axis above covers two.
;; That asymmetry is the point of this kata.
;;
;;   (describe {:kind :circle    :r 2})       ; => "circle with radius 2"
;;   (describe {:kind :rectangle :w 3 :h 4})  ; => "rectangle 3 by 4"
;;   (describe {:kind :triangle  :a 3 :b 4})  ; => "right triangle 3, 4"
;;   (describe {:kind :square    :s 4})       ; => "square with side 4"
;;   (describe {:kind :pentagon})             ; => "unknown shape: :pentagon"
;;
;; ── Reflection ──────────────────────────────────────────
;;
;; A protocol groups all behaviours of one TYPE together (closed set of
;; methods, open set of types). A multimethod groups all implementations
;; of one BEHAVIOUR together (open set of methods AND types). To add a
;; new shape on the protocol axis you would define a whole new record; to
;; add one on the multimethod axis you only add a `defmethod`. That
;; asymmetry is the practical difference between the two tools.
;;
;; In production Clojure code you will reach for plain maps far more often
;; than for `defrecord` — see SENSEI §20. This kata introduces records
;; because protocols are records' canonical implementation type, and you
;; should have seen the tool.

(defprotocol Shape
  (area [shape])
  (perimeter [shape]))

(defrecord Circle [r]
  Shape
    (area [_]
          ;; TODO
    )
    (perimeter [_]
               ;; TODO
    ))

(defrecord Rectangle [w h]
  Shape
    (area [_]
          ;; TODO
    )
    (perimeter [_]
               ;; TODO
    ))

(defmulti describe :kind)

;; TODO: defmethod describe :circle, :rectangle, :triangle, :square, :default
