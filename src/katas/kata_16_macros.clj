(ns katas.kata-16-macros)

;; ─── Kata 16: Macros ───────────────────────────────────────────────────
;;
;; Two macros, each of a kind that genuinely needs a macro.
;;
;; ── when-let* ──────────────────────────────────────────────────────────
;;
;; Like `when-let`, but for MANY bindings: evaluate each right-hand side
;; in order; if any is falsey, short-circuit to nil WITHOUT evaluating
;; the rest or the body; if all are truthy, bind them (left-hand sides
;; may destructure) and run the body as an implicit `do`. Be hygienic —
;; a user binding named like an internal temp must still work. Reject a
;; non-vector or odd-length bindings form at macroexpansion by throwing
;; IllegalArgumentException (the compiler wraps it; the test asserts on
;; the cause).
;;
;;   (when-let* [a 1 b 2] (+ a b))            => 3
;;   (when-let* [a 1 b nil c (boom)] :x)      => nil   ; (boom) not run
;;   (when-let* [{:keys [x y]} {:x 1 :y 2}] (+ x y)) => 3
;;
;; ── infix ──────────────────────────────────────────────────────────────
;;
;; Rewrite a parenthesised infix form (operand op operand) into prefix,
;; recursively. Operands are EMITTED as code, not evaluated by the macro
;; (so surrounding lexical bindings work). A non-list form is itself.
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
