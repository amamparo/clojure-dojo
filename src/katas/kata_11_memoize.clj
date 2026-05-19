(ns katas.kata-11-memoize)

;; ─── Kata 11: my-memoize ───────────────────────────────────────
;;
;; Implement `my-memoize`. Given a function `f`, it returns a new
;; function with the same input/output behaviour, except that for any
;; given argument list, `f` is only ever called the first time —
;; every subsequent call with those same arguments returns the cached
;; value without invoking `f` again.
;;
;;   (def slow-square
;;     (my-memoize (fn [n] (Thread/sleep 1000) (* n n))))
;;
;;   (slow-square 5)   ; takes 1s; returns 25
;;   (slow-square 5)   ; instant; returns 25
;;
;; Constraints:
;;
;;   - Works for any number of arguments — zero, one, many.
;;   - Caches nil and false results too. A function that returns nil
;;     should not be re-called on the same arguments.
;;   - Each call to `my-memoize` returns an independent function. Two
;;     memoized versions of the same `f` do not share a cache.
;;
;; Clojure has a built-in `memoize` with this contract. Implement
;; your own; do not use `memoize`.

(defn my-memoize [f]
  ;; TODO
)
