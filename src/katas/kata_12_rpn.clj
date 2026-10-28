(ns katas.kata-12-rpn)

;; ─── Kata 12: RPN ──────────────────────────────────────────────────────
;;
;; `evaluate` takes a sequence of tokens. Each token is either:
;;   - a number (long, double, or ratio), pushed onto the stack
;;   - one of the symbols `+ - * /`: pops two values and pushes the result
;;
;; The DEEPER operand is the left-hand side:
;;   (evaluate [3 4 '-])  => -1    ; 3 - 4
;;   (evaluate [10 2 '/]) => 5     ; 10 / 2 (division is exact: [7 2 '/] => 7/2)
;;
;; A well-formed expression leaves exactly ONE value on the stack;
;; `evaluate` returns that single value (not a stack).
;;
;;   (evaluate [3])                  => 3
;;   (evaluate [1 2 '+])             => 3
;;   (evaluate [3 4 '- 5 '*])        => -5
;;   (evaluate [7 2 '/])             => 7/2
;;   (evaluate [15 7 1 1 '+ '- '/ 3 '*]) => 9
;;
;; Errors (thrown with `ex-info`):
;;   - a symbol outside that set
;;       → {:type :unknown-op, :op <the-symbol>}
;;       (checked before the stack, so ['pow] is :unknown-op)
;;   - an operator with fewer than two operands on the stack
;;       → {:type :stack-underflow}
;;   - an empty input, or more than one value left at the end
;;       → {:type :malformed}

(defn evaluate [tokens]
  ;; TODO
)
