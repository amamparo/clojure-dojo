(ns katas.kata-04-run-length-encoding)

;; ─── Kata 4: Run-length encoding ───────────────────────────────────────
;;
;; Implement `encode` and `decode`. They work on any seqable input
;; whose elements support equality.
;;
;;   (encode "")            => []
;;   (encode "a")           => [[1 \a]]
;;   (encode "aaabbc")      => [[3 \a] [2 \b] [1 \c]]
;;   (encode [1 1 2 2 2])   => [[2 1] [3 2]]
;;   (encode [:a :a :b])    => [[2 :a] [1 :b]]
;;
;;   (decode [])               => []
;;   (decode [[3 \a] [1 \b]])  => [\a \a \a \b]
;;   (decode [[2 1] [3 2]])    => [1 1 2 2 2]
;;
;; Round-trip property:
;;   (= (vec xs) (decode (encode xs)))   for any seqable `xs`.

(defn encode [xs] (map (juxt count first) (partition-by identity xs)))

(defn decode [pairs] (mapcat #(apply repeat %) pairs))