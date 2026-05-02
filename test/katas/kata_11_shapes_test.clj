(ns katas.kata-11-shapes-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [katas.kata-11-shapes :as shapes
             :refer [Shape area perimeter describe
                     ->Circle ->Rectangle ->RightTriangle]]))

(deftest circle-area
  (is (= Math/PI         (area (->Circle 1))))
  (is (= (* 4 Math/PI)   (area (->Circle 2)))))

(deftest circle-perimeter
  (is (= (* 2 Math/PI)   (perimeter (->Circle 1))))
  (is (= (* 6 Math/PI)   (perimeter (->Circle 3)))))

(deftest rectangle
  (is (= 12 (area (->Rectangle 3 4))))
  (is (= 14 (perimeter (->Rectangle 3 4))))
  (is (= 1  (area (->Rectangle 1 1))))
  (is (= 4  (perimeter (->Rectangle 1 1)))))

(deftest right-triangle
  (is (= 6  (area (->RightTriangle 3 4))))
  (is (= 12 (perimeter (->RightTriangle 3 4))))
  (testing "perimeter uses the hypotenuse"
    (is (= 30 (perimeter (->RightTriangle 5 12))))))

(deftest all-records-satisfy-shape
  (is (satisfies? Shape (->Circle 1)))
  (is (satisfies? Shape (->Rectangle 1 2)))
  (is (satisfies? Shape (->RightTriangle 3 4))))

(deftest describe-circle
  (is (= "circle with radius 2" (describe {:kind :circle :r 2}))))

(deftest describe-rectangle
  (is (= "rectangle 3 by 4" (describe {:kind :rectangle :w 3 :h 4}))))

(deftest describe-triangle
  (is (= "right triangle 3, 4" (describe {:kind :triangle :a 3 :b 4}))))

(deftest describe-unknown-falls-through
  (testing "an unknown :kind falls through to a default that mentions it"
    (is (str/starts-with? (describe {:kind :pentagon}) "unknown shape"))))
