(ns katas.kata-17-interpreter)

;; ─── Kata 17: Interpreter ──────────────────────────────────────────────
;;
;; The capstone: a tree-walking interpreter for a tiny Lisp. Source is
;; already Clojure data — no parser. `evaluate` takes an environment and
;; an expression and returns its value.
;;
;; Classification:
;;   - a symbol            → look it up in the env; bound to nil/false is
;;                           still bound (presence, not truthiness);
;;                           unbound → throw
;;                           ex-info {:type :unbound-symbol :sym s}
;;   - a non-seq (number,
;;     string, keyword,
;;     vector, map, bool,
;;     nil)                → self-evaluating, returns itself
;;   - an empty form       → itself
;;   - otherwise           → a composite; dispatch on its head
;;
;; Special forms / operators:
;;   (do e…)            implicit-do, value of the last (or nil)
;;   (let [b v …] body) sequential bindings extend a scope; the body is
;;                      an implicit do; the scope does not leak, and
;;                      never writes into an atom env
;;   (if c t e)         lazy in the untaken branch
;;   (def sym e)        binds sym in an ATOM env for every later form and
;;                      returns the value; a map env throws
;;                      {:type :immutable-env}. A fn created directly
;;                      in the atom env sees later defs, itself
;;                      included, so it can recurse.
;;   (fn [params…] body…) a closure capturing the current env; the body
;;                      is an implicit do
;;   (+ - * /), (= < >) arithmetic / comparison on evaluated args; the
;;                      only built-ins — any other symbol must be bound
;;   (f arg…)           call a closure; non-callable → throw
;;                      {:type :not-callable :value v}
;;
;; Examples:
;;   (evaluate {} '(+ 1 2 3))                       => 6
;;   (evaluate '{x 10} 'x)                          => 10
;;   (evaluate {} '(let [x 1 y (+ x 2)] (* x y)))   => 3
;;   (evaluate {} '((fn [x] (* x x)) 3))            => 9
;;   (let [e (atom {})]
;;     (evaluate e '(def fact (fn [n] (if (= n 0) 1 (* n (fact (- n 1)))))))
;;     (evaluate e '(fact 5)))                       => 120

;; helpers defined above `evaluate` may call it
(declare evaluate)

(defn evaluate [env expr]
  ;; TODO
)
