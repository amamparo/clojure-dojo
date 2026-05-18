(ns katas.kata-12-rpn)

;; ─── Kata 12: RPN calculator ──────────────────────────────────
;;
;; `evaluate` takes a sequence of tokens. Each token is either:
;;   - a number (long or double), pushed onto the stack
;;   - an operator from this set, given as a symbol or string:
;;       +  -  *  /             arithmetic, pop two, push result
;;       dup                    duplicate top of stack
;;       drop                   discard top of stack
;;       swap                   swap top two
;;
;; For arithmetic, the deeper operand is the LEFT-hand side:
;;   [3 4 -]   ⇒ 3 - 4 = -1
;;   [10 2 /]  ⇒ 10 / 2 = 5
;;
;; The result is the entire stack at the end, in order from BOTTOM to TOP.
;;
;;   (evaluate [1 2 '+])             => [3]
;;   (evaluate [3 4 '- 5 '*])        => [-5]
;;   (evaluate [1 2 3 'swap 'drop])  => [1 3]
;;   (evaluate [4 'dup '*])          => [16]
;;
;; Tokens may be passed as either symbols (`'+`) or strings (`"+"`).
;;
;; Errors (use `ex-info`):
;;   - operator needs more operands than the stack has → {:type
;;   :stack-underflow}
;;   - unknown token                                   → {:type :unknown-token,
;;   :token t}

(defn evaluate [tokens]
  ;; TODO
)
