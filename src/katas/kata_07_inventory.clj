(ns katas.kata-07-inventory)

;; ─── Kata 7: Inventory ─────────────────────────────────────────────────
;;
;; This is NOT a blank stub. It is a small, working — but incomplete and
;; subtly wrong — namespace, the kind you inherit on day one. Read it
;; with the method from the guide: the `ns`, then each def in order, and
;; the test file as the precise contract.
;;
;; An inventory is a map of sku → item. An item looks like:
;;
;;   {:name "Widget" :qty 3 :price 250 :reorder 5}
;;
;; `add-item` and `total-value` are CORRECT — leave them alone.
;;
;; `low-stock` has ONE defect the tests expose: an item whose quantity is
;; exactly at its reorder point IS low stock (a boundary condition).
;; Find and fix it; do not rewrite the function.
;;
;; `restock-report` is UNIMPLEMENTED. It returns a map of sku →
;; order-quantity for every low-stock item, where the order quantity
;; brings stock to one above the reorder point: (inc reorder) - qty.
;; Well-stocked skus do not appear; every reported quantity is > 0.
;;
;;   (def inv (-> {}
;;                (add-item "A1" "Widget" 3 250 5)
;;                (add-item "B2" "Gadget" 10 100 4)))
;;
;;   (low-stock inv)        => ["A1"]      ; sorted by sku
;;   (restock-report inv)   => {"A1" 3}    ; (inc 5) - 3

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
