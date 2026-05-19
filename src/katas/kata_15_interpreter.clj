(ns katas.kata-15-interpreter)

;; ─── Kata 15: Tiny interpreter ─────────────────────────────────────
;;
;; Capstone: a small expression interpreter that puts together everything
;; from the previous katas. Source is already a Clojure data structure,
;; so there is no parser to write — `evaluate` walks expressions directly.
;;
;; `evaluate` takes an env and an expression, and returns the resulting value.
;;
;; ── The environment ────────────────────────────────────
;;
;; `env` is either:
;;   - a plain map  (immutable; used inside let/fn bodies)
;;   - an atom wrapping a map  (mutable; used at top level so `def`
;;     can extend it)
;;
;; Symbol lookup derefs env if it's an atom, then looks up by symbol.
;; `def` requires env to be an atom; throws otherwise.
;;
;; ── Self-evaluating ────────────────────────────────────
;;
;; Numbers, strings, booleans, keywords, nil, vectors, maps, and sets
;; evaluate to themselves.
;;
;; ── Symbol ───────────────────────────────────────────
;;
;;   look up in env. If absent, throw ex-info {:type :unbound-symbol,
;;   :sym <sym>}.
;;
;; ── Composite forms ────────────────────────────────────
;;
;; Lists (and cons cells) dispatch on the first element:
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
;;   (def sym e)
;;     evaluate e, bind sym to the result in env. Returns the value.
;;     Requires env to be an atom; throws ex-info {:type :immutable-env}
;;     otherwise.
;;
;;   (fn [params...] body1 body2 ...)
;;     construct a closure — a callable value that captures env.
;;     When invoked, evaluates body in env extended with parameter
;;     bindings. Body is an implicit `do`.
;;
;;   (+ a b ...)  (- a b ...)  (* a b ...)  (/ a b ...)
;;     arithmetic, ≥1 args, applied with `clojure.core/+` etc.
;;
;;   (= a b)  (< a b)  (> a b)
;;     comparisons, exactly 2 args.
;;
;;   (f arg ...)   — anything else, with a non-special-form head
;;     evaluate `f` and the args; if `f` is a closure, apply it.
;;     If `f` resolves to something non-callable, throw ex-info
;;     {:type :not-callable, :value <evaluated f>}. If `f` is an
;;     unbound symbol, :unbound-symbol propagates from its lookup.
;;
;; ── Examples ─────────────────────────────────────────────
;;
;;   (evaluate {} 42)                        => 42
;;   (evaluate '{x 10} 'x)                   => 10
;;   (evaluate {} '(+ 1 2 3))                => 6
;;   (evaluate {} '(let [x 1, y (+ x 2)]
;;                   (* x y)))                => 3
;;   (evaluate {} '(if (< 1 2) :yes :no))    => :yes
;;   (evaluate {} '(do (+ 1 1) (+ 2 2)))     => 4
;;
;;   (evaluate {} '((fn [x] (* x x)) 5))     => 25
;;   (evaluate '{base 10}
;;             '((fn [x] (+ base x)) 5))     => 15
;;
;;   (let [env (atom {})]
;;     (evaluate env '(def square (fn [x] (* x x))))
;;     (evaluate env '(square 5)))           => 25
;;
;; Closures capture the env reference, so a fn bound via `def` in an
;; atom env can recursively call itself by name — the lookup sees
;; whatever the atom contains at call time.

(defn evaluate [env expr]
  ;; TODO
)
