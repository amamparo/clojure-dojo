(ns katas.kata-17-interpreter)

;; ─── Kata 17: The interpreter ──────────────────────────────────────────
;;
;; The capstone: a tree-walking interpreter for a tiny Lisp. Source is
;; already Clojure data — no parser. `evaluate` takes an environment and
;; an expression and returns its value.
;;
;; Classification (the §12 predicates):
;;   - a symbol            → look it up in the env (unbound → throw
;;                           ex-info {:type :unbound-symbol :sym s})
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
;;                      an implicit do; the scope does not leak
;;   (if c t e)         lazy in the untaken branch
;;   (def sym e)        only valid when env is an ATOM; swap! the value
;;                      in; returns the value; an immutable (map) env
;;                      throws {:type :immutable-env}. A fn bound by def
;;                      can recurse (it captures the atom env).
;;   (fn [params…] body) a closure capturing the current env
;;   (+ - * /), (= < >) arithmetic / comparison on evaluated args
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

(defn evaluate [env expr]
  ;; TODO
)
