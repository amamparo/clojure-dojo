(ns katas.kata-07-inventory)

;; ─── Kata 7: Inventory ─────────────────────────────────────────────────
;;
;; This is NOT a blank stub. It is a small, working — but incomplete and
;; subtly wrong — namespace. Read it with the method from DENSHO §15
;; (Reading an unfamiliar namespace): the `ns`, then each def in order,
;; and the test file as the precise contract.
;;
;; An inventory is a map of sku → item. An item looks like:
;;
;;   {:name "Widget" :qty 3 :price 250 :reorder 5}
;;
;; Three functions are implemented. One fails a test: find it from
;; `just test 7` and fix it without rewriting it. `restock-report` is
;; unimplemented.
;;
;; `restock-report` returns a map of sku → order-quantity for every
;; low-stock item, where the order quantity brings stock to one above
;; the reorder point: (inc reorder) - qty. Well-stocked skus do not
;; appear; every reported quantity is > 0.
;;
;;   (def inv (-> {}
;;                (add-item "A1" "Widget" 3 250 5)
;;                (add-item "B2" "Gadget" 10 100 4)))
;;
;;   (low-stock inv)        => ["A1"]      ; sorted by sku
;;   (restock-report inv)   => {"A1" 3}

(defn add-item [inv sku name qty price reorder]
  (assoc inv sku {:name name, :qty qty, :price price, :reorder reorder}))

(defn total-value [inv]
  (reduce (fn [acc {:keys [qty price]}] (+ acc (* qty price))) 0 (vals inv)))

(defn low-stock [inv]
  (->> inv
       (filter (fn [[_sku item]] (< (:qty item) (:reorder item))))
       (map key)
       sort
       vec))

(defn restock-report [inv]
  ;; TODO
)
