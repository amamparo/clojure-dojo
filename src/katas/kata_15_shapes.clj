(ns katas.kata-15-shapes)

;; ─── Kata 15: Shapes ───────────────────────────────────────────────────
;;
;; The two polymorphism tools, side by side, so you feel the tradeoff.
;; Protocols dispatch on a TYPE (closed methods, open types); a new type
;; means a whole new record. Multimethods dispatch on a FUNCTION of the
;; args (open methods and types); a new case means one more `defmethod`.
;;
;; ── Shape protocol + records ───────────────────────────────────────────
;;
;; Implement `area`/`perimeter` for `Circle` (field r) and `Rectangle`
;; (fields w h). `defrecord` generates `->Circle`/`->Rectangle`.
;;
;;   (area (->Circle 1))           => Math/PI
;;   (perimeter (->Rectangle 3 4)) => 14
;;   (satisfies? Shape (->Circle 1)) => true
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

(defmethod describe :default
  [{:keys [kind]}]
  ;; TODO — plus :circle :rectangle :triangle :square methods
  (str "unknown shape: " kind))

(defn total-area [shapes]
  ;; TODO
)
