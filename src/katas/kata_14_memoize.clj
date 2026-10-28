(ns katas.kata-14-memoize)

;; ─── Kata 14: my-memoize ───────────────────────────────────────────────
;;
;; ── my-memoize ─────────────────────────────────────────────────────────
;;
;; Wrap f; compute once per distinct argument list, then serve cached.
;;
;;   (def f (my-memoize (fn [& xs] (apply + xs))))
;;   (f 1 2 3) ; computes => 6
;;   (f 1 2 3) ; cached, body not re-run
;;   (f)       ; zero-arity works => 0; nil/false results are cached too
;;
;; Distinct memoized fns have independent caches. Safe under concurrent
;; calls: a result computed on any thread stays cached (f may run more
;; than once for an argument list first called on several threads).
;;
;; ── memoize-with ───────────────────────────────────────────────────────
;;
;; Like my-memoize, but the cache key is `(apply key-fn args)` rather
;; than the args themselves — distinct argument lists with the same key
;; share a cached result.
;;
;;   (def g (memoize-with count (fn [s] (apply str (reverse s)))))
;;   (g "ab")  ; computes, key 2 => "ba"
;;   (g "cd")  ; same key 2, cached => "ba"

(defn my-memoize [f]
  ;; TODO
)

(defn memoize-with [key-fn f]
  ;; TODO
)
