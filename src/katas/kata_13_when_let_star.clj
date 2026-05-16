(ns katas.kata-13-when-let-star)

;; ─── Kata 13: when-let* ───────────────────────────────────────────────────
;;
;; `when-let*` is `clojure.core/when-let` extended to multiple bindings.
;; It evaluates each binding form in order. If any binding's value is
;; logical false (nil or false), the macro short-circuits and returns
;; nil — without evaluating any subsequent binding forms or the body.
;; Otherwise it evaluates the body in the scope of all bindings and
;; returns the value of the last expression.
;;
;;   (when-let* [a 1
;;               b 2]
;;     (+ a b))                                ; => 3
;;
;;   (when-let* [a 1
;;               b nil
;;               c (throw (ex-info "x" {}))]   ; never evaluated
;;     :unreached)                             ; => nil
;;
;; Constraints:
;;   - `bindings` must be a vector with an even number of forms.
;;     Otherwise throw IllegalArgumentException at macroexpansion time.
;;   - Destructuring patterns on the LHS must work (you don't need to
;;     special-case this — `let` already supports destructuring).
;;   - Be hygienic: do not leak intermediate symbols that could collide
;;     with names the user wrote.

(defmacro when-let* [bindings & body]
  ;; TODO
  )
