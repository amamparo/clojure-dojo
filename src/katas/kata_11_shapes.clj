(ns katas.kata-11-shapes)

;; ─── Kata 11: Shapes ───────────────────────────────────────────────────
;;
;; You are learning: polymorphism in Clojure with two complementary
;; tools — `defprotocol` + `defrecord` (dispatch on the host type) and
;; `defmulti` + `defmethod` (dispatch on an arbitrary function of the
;; arguments). Doing the same domain twice makes the trade-off concrete.
;;
;; ── Part A: protocol & records ────────────────────────────────────────
;;
;; The `Shape` protocol has two methods: `area` and `perimeter`.
;; Implement records `Circle`, `Rectangle`, and `RightTriangle` that
;; satisfy it.
;;
;;   (area      (->Circle 1))         ; => Math/PI         (≈ 3.14159…)
;;   (perimeter (->Circle 1))         ; => (* 2 Math/PI)
;;   (area      (->Rectangle 3 4))    ; => 12
;;   (perimeter (->Rectangle 3 4))    ; => 14
;;   (area      (->RightTriangle 3 4)); => 6
;;   (perimeter (->RightTriangle 3 4)); => 12   (3 + 4 + 5)
;;
;; ── Part B: multimethod ───────────────────────────────────────────────
;;
;; `describe` dispatches on a shape map's `:kind` key and returns a short
;; human-readable string.
;;
;;   (describe {:kind :circle    :r 2})       ; => "circle with radius 2"
;;   (describe {:kind :rectangle :w 3 :h 4})  ; => "rectangle 3 by 4"
;;   (describe {:kind :triangle  :a 3 :b 4})  ; => "right triangle 3, 4"
;;   (describe {:kind :pentagon})             ; => "unknown shape: :pentagon"
;;
;; ── Reflection ────────────────────────────────────────────────────────
;;
;; Notice: a protocol groups all behaviours of one TYPE together (closed
;; set of methods, open set of types). A multimethod groups all
;; implementations of one BEHAVIOUR together (open set of methods AND
;; types). Reach for whichever axis you expect to extend.

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

(defrecord RightTriangle [a b]
  Shape
  (area [_]
    ;; TODO
    )
  (perimeter [_]
    ;; TODO
    ))

(defmulti describe :kind)

;; TODO: defmethod describe :circle, :rectangle, :triangle, :default
