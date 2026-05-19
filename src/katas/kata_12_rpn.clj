(ns katas.kata-12-rpn)

;; ─── Kata 12: RPN calculator ───────────────────────────────────────────
;;
;; A dispatch table (a map of operator symbol → function) walked with a
;; `reduce` over a token sequence, threading a stack accumulator.
;;
;; `evaluate` takes a sequence of tokens. Each token is either:
;;   - a number (long, double, or ratio), pushed onto the stack
;;   - an operator symbol from this set, popping two and pushing one:
;;       +  -  *  /             arithmetic
;;
;; For arithmetic the DEEPER operand is the left-hand side:
;;   [3 4 -]   ⇒ 3 - 4 = -1
;;   [10 2 /]  ⇒ 10 / 2 = 5    (division is exact: [7 2 /] ⇒ 7/2)
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
;; Errors (use `ex-info`):
;;   - an operator with fewer than two operands on the stack
;;       → {:type :stack-underflow}
;;   - an unknown operator symbol
;;       → {:type :unknown-op, :op <the-symbol>}
;;   - an empty input, or more than one value left at the end
;;       → {:type :malformed}

(defn evaluate [tokens]
  ;; TODO
)
