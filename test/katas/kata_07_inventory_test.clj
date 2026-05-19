(ns katas.kata-07-inventory-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-07-inventory :as inv]))

(defn- sample []
  (-> {}
      (inv/add-item "A1" "Widget" 3 250 5)
      (inv/add-item "B2" "Gadget" 10 100 4)
      (inv/add-item "C3" "Gizmo" 5 500 5)
      (inv/add-item "D4" "Doohickey" 0 75 2)))

(deftest add-item-shape
  (let [i (inv/add-item {} "A1" "Widget" 3 250 5)]
    (is (= {:name "Widget", :qty 3, :price 250, :reorder 5} (get i "A1")))))

(deftest total-value-sums-qty-times-price
  ;; 3*250 + 10*100 + 5*500 + 0*75 = 750 + 1000 + 2500 + 0
  (is (= 4250 (inv/total-value (sample))))
  (is (= 0 (inv/total-value {}))))

(deftest low-stock-includes-items-at-threshold
  (testing "an item exactly at its reorder point is low stock"
    ;; A1: 3 <= 5 yes.  B2: 10 <= 4 no.  C3: 5 <= 5 YES (boundary).
    ;; D4: 0 <= 2 yes.
    (is (= ["A1" "C3" "D4"] (inv/low-stock (sample))))))

(deftest low-stock-empty-when-all-stocked
  (let [i (-> {}
              (inv/add-item "X" "X" 100 1 5))]
    (is (= [] (inv/low-stock i))))
  (is (= [] (inv/low-stock {}))))

(deftest low-stock-sorted-by-sku
  (let [i (-> {}
              (inv/add-item "Z" "Z" 0 1 1)
              (inv/add-item "A" "A" 0 1 1)
              (inv/add-item "M" "M" 0 1 1))]
    (is (= ["A" "M" "Z"] (inv/low-stock i)))))

(deftest restock-report-orders-up-to-inc-reorder
  (let [r (inv/restock-report (sample))]
    ;; A1: qty 3 reorder 5 → (inc 5) - 3 = 3
    ;; C3: qty 5 reorder 5 → (inc 5) - 5 = 1   (boundary)
    ;; D4: qty 0 reorder 2 → (inc 2) - 0 = 3
    (is (= {"A1" 3, "C3" 1, "D4" 3} r))))

(deftest restock-report-excludes-well-stocked
  (let [r (inv/restock-report (sample))] (is (not (contains? r "B2")))))

(deftest restock-report-empty
  (is (= {} (inv/restock-report {})))
  (is (= {}
         (inv/restock-report (-> {}
                                 (inv/add-item "X" "X" 99 1 5))))))

(deftest restock-units-always-positive
  (testing "every reported order quantity is strictly positive"
    (is (every? pos? (vals (inv/restock-report (sample)))))))
