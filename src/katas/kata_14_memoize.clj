(ns katas.kata-14-memoize)

;; ─── Kata 14: my-memoize ───────────────────────────────────────────────
;;
;; A function that returns a function, closing over a private atom cache.
;; Two subtleties the tests pin down: the returned fn is VARIADIC (keyed
;; on the whole args vector, so any arity works), and falsey results must
;; be cached too — use `contains?` on the cache, not the truthiness of
;; the cached value, to decide whether to recompute.
;;
;; ── my-memoize ─────────────────────────────────────────────────────────
;;
;; Wrap f; compute once per distinct argument list, then serve cached.
;;
;;   (def f (my-memoize (fn [x] (do-expensive x))))
;;   (f 4) ; computes
;;   (f 4) ; cached, body not re-run
;;   (f)   ; zero-arity works; nil/false results are cached
;;
;; Distinct memoized fns have independent caches.
;;
;; ── memoize-with ───────────────────────────────────────────────────────
;;
;; Like my-memoize, but the cache key is `(apply key-fn args)` rather
;; than the args themselves — distinct argument lists with the same key
;; share a cached result.
;;
;;   (def g (memoize-with count (fn [s] (expensive s))))
;;   (g "ab")  ; computes, key 2
;;   (g "cd")  ; same key 2 → cached, body not re-run

(defn my-memoize [f]
  ;; TODO
)

(defn memoize-with [key-fn f]
  ;; TODO
)
