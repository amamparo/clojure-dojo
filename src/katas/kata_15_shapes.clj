(ns katas.kata-15-shapes)

;; ─── Kata 15: Shapes ───────────────────────────────────────────────────
;;
;; This is NOT a blank stub. The protocol, both record shells, the
;; `defmulti` and its `:default` method are given. Fill the method bodies,
;; add one `defmethod` per listed :kind, and write `total-area`.
;;
;; ── Shape protocol + records ───────────────────────────────────────────
;;
;; Implement `area`/`perimeter` for `Circle` (field r) and `Rectangle`
;; (fields w h). `defrecord` generates `->Circle`/`->Rectangle`.
;;
;;   (area (->Circle 1))           => Math/PI
;;   (perimeter (->Rectangle 3 4)) => 14
;;
;; ── describe (multimethod) ─────────────────────────────────────────────
;;
;; A multimethod dispatching on the map's `:kind`:
;;
;;   (describe {:kind :circle :r 2})        => "circle with radius 2"
;;   (describe {:kind :rectangle :w 3 :h 4}) => "rectangle 3 by 4"
;;   (describe {:kind :triangle :a 3 :b 4})  => "right triangle 3, 4"
;;   (describe {:kind :square :s 4})         => "square with side 4"
;;   (describe {:kind :pentagon})            => "unknown shape: :pentagon"
;;
;; ── total-area ─────────────────────────────────────────────────────────
;;
;; Sum `area` over a seq of Shape records ((total-area []) => 0).

(defprotocol Shape
  (area [shape])
  (perimeter [shape]))

(defrecord Circle [r]
  Shape
    (area [_] ;; TODO
    )
    (perimeter [_] ;; TODO
    ))

(defrecord Rectangle [w h]
  Shape
    (area [_] ;; TODO
    )
    (perimeter [_] ;; TODO
    ))

(defmulti describe :kind)

;; TODO: defmethod describe for :circle :rectangle :triangle :square

(defmethod describe :default [{:keys [kind]}] (str "unknown shape: " kind))

(defn total-area [shapes]
  ;; TODO
)
