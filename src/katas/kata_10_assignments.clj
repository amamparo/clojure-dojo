(ns katas.kata-10-assignments)

;; ─── Kata 10: Sequential assignments ─────────────────────────────
;;
;; `interpret` takes a vector of instructions and returns the final
;; environment — a map from symbol → value — produced by executing them
;; in order.
;;
;; Three instructions:
;;
;;   [:set sym value]      bind sym to the literal value
;;   [:add a b dest]       bind dest to (+ env[a] env[b])
;;   [:mul a b dest]       bind dest to (* env[a] env[b])
;;
;; Examples:
;;
;;   (interpret [])
;;     => {}
;;
;;   (interpret [[:set 'x 5]])
;;     => '{x 5}
;;
;;   (interpret [[:set 'x 5]
;;               [:set 'y 3]
;;               [:add 'x 'y 'sum]])
;;     => '{x 5, y 3, sum 8}
;;
;;   (interpret [[:set 'a 2]
;;               [:set 'b 3]
;;               [:mul 'a 'b 'c]
;;               [:add 'a 'c 'd]])
;;     => '{a 2, b 3, c 6, d 8}
;;
;; If `:add` or `:mul` references a symbol not in the environment, throw
;; an `ex-info` with at least `{:type :unbound, :symbol s}` in the data.
;;
;;   (interpret [[:add 'x 'y 'z]])
;;     ;; throws ex-info — :unbound 'x

(defn interpret [program]
  ;; TODO
)
