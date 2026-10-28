(ns katas.kata-16-macros)

;; ─── Kata 16: Macros ───────────────────────────────────────────────────
;;
;; ── when-let* ──────────────────────────────────────────────────────────
;;
;; Like `when-let`, but for MANY bindings. Bind each pair in turn, as
;; `let` does — a right-hand side sees every name bound before it. At the
;; first falsey value, yield nil WITHOUT evaluating the remaining right-hand
;; sides or the body. If all are truthy, run the body as an implicit `do`.
;; Left-hand sides may destructure. Be hygienic — a user binding named like
;; an internal temp must still work. Reject a non-vector or odd-length
;; bindings form at macroexpansion by throwing IllegalArgumentException
;; (the compiler wraps it; the test asserts on the cause).
;;
;;   (when-let* [a 1 b 2] (+ a b))  => 3
;;   (when-let* [a 1 b nil c (throw (ex-info "boom" {}))] :x)
;;     => nil   ; the throw never runs
;;   (when-let* [{:keys [x y]} {:x 1 :y 2}] (+ x y)) => 3
;;
;; ── infix ──────────────────────────────────────────────────────────────
;;
;; Rewrite a parenthesised infix form (operand op operand) into prefix,
;; recursively. Operands are EMITTED as code, not evaluated by the macro
;; (so surrounding lexical bindings work). A number or a symbol is
;; itself.
;;
;;   (infix 42)                  => 42
;;   (infix (1 + 2))             => 3
;;   (infix (2 + (3 * 4)))       => 14
;;   (let [x 5] (infix (x * (x + 1)))) => 30

(defmacro when-let* [bindings & body]
  ;; TODO
)

(defmacro infix [form]
  ;; TODO
)
