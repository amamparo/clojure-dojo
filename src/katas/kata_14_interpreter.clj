(ns katas.kata-14-interpreter)

;; ─── Kata 14: Tiny interpreter ──────────────────────────────────────────────
;;
;; Capstone: a small expression interpreter that puts together everything
;; from the previous katas.
;;
;; `evaluate` takes an env (a map of symbol → value) and an expression,
;; and returns the resulting value.
;;
;; Self-evaluating: numbers, strings, booleans, keywords, nil, vectors,
;; maps, and sets evaluate to themselves.
;;
;; Symbol:
;;   - look up in env. If absent, throw ex-info {:type :unbound-symbol,
;;     :sym <sym>}.
;;
;; Composite (lists / cons cells), dispatch on the first element:
;;
;;   (do e1 e2 ...)
;;     evaluate each in order, return the last (or nil if empty).
;;
;;   (let [x e1, y e2, ...] body1 body2 ...)
;;     sequential bindings — each RHS sees previously-bound names.
;;     Body is an implicit `do`.
;;
;;   (if c t e)
;;     evaluate c; if truthy evaluate t else evaluate e. `e` is required.
;;
;;   (+ a b ...)  (- a b ...)  (* a b ...)  (/ a b ...)
;;     arithmetic, ≥1 args, applied with `clojure.core/+` etc.
;;
;;   (= a b)  (< a b)  (> a b)
;;     comparisons, exactly 2 args.
;;
;; Anything else → throw ex-info {:type :unknown-form, :form expr}.
;;
;; Examples:
;;   (evaluate {} 42)                        => 42
;;   (evaluate '{x 10} 'x)                   => 10
;;   (evaluate {} '(+ 1 2 3))                => 6
;;   (evaluate {} '(let [x 1, y (+ x 2)]
;;                   (* x y)))                => 3
;;   (evaluate {} '(if (< 1 2) :yes :no))    => :yes
;;   (evaluate {} '(do (+ 1 1) (+ 2 2)))     => 4

(defn evaluate [env expr]
  ;; TODO
  )
