(ns katas.kata-15-shapes-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [katas.kata-15-shapes :refer
             [Shape area perimeter describe total-area ->Circle ->Rectangle]]))

(deftest circle-area
  (is (= Math/PI (area (->Circle 1))))
  (is (= (* 4 Math/PI) (area (->Circle 2)))))

(deftest circle-perimeter
  (is (= (* 2 Math/PI) (perimeter (->Circle 1))))
  (is (= (* 6 Math/PI) (perimeter (->Circle 3)))))

(deftest rectangle-area-and-perimeter
  (is (= 12 (area (->Rectangle 3 4))))
  (is (= 14 (perimeter (->Rectangle 3 4))))
  (is (= 1 (area (->Rectangle 1 1))))
  (is (= 4 (perimeter (->Rectangle 1 1)))))

(deftest records-satisfy-shape
  (is (satisfies? Shape (->Circle 1)))
  (is (satisfies? Shape (->Rectangle 1 2))))

(deftest describe-each-kind
  (is (= "circle with radius 2" (describe {:kind :circle, :r 2})))
  (is (= "rectangle 3 by 4" (describe {:kind :rectangle, :w 3, :h 4})))
  (is (= "right triangle 3, 4" (describe {:kind :triangle, :a 3, :b 4})))
  (is (= "square with side 4" (describe {:kind :square, :s 4}))))

(deftest describe-unknown-falls-through
  (testing "an unknown :kind hits the default and names the kind"
    (is (= "unknown shape: :pentagon" (describe {:kind :pentagon})))
    (is (str/starts-with? (describe {:kind :hexagon}) "unknown shape"))))

(deftest total-area-sums-records
  (is (= (+ Math/PI 6) (total-area [(->Circle 1) (->Rectangle 2 3)])))
  (is (= 0 (total-area [])))
  (is (= (* 2 Math/PI) (total-area [(->Circle 1) (->Circle 1)]))))
