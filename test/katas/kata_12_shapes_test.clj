(ns katas.kata-12-shapes-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-12-shapes :as shapes
             :refer [Shape area perimeter describe
                     ->Circle ->Rectangle ->RightTriangle ->Square]]))

(deftest circle-area
  (is (match? Math/PI         (area (->Circle 1))))
  (is (match? (* 4 Math/PI)   (area (->Circle 2)))))

(deftest circle-perimeter
  (is (match? (* 2 Math/PI)   (perimeter (->Circle 1))))
  (is (match? (* 6 Math/PI)   (perimeter (->Circle 3)))))

(deftest rectangle
  (is (match? 12 (area (->Rectangle 3 4))))
  (is (match? 14 (perimeter (->Rectangle 3 4))))
  (is (match? 1  (area (->Rectangle 1 1))))
  (is (match? 4  (perimeter (->Rectangle 1 1)))))

(deftest right-triangle
  (is (match? 6  (area (->RightTriangle 3 4))))
  (is (match? 12 (perimeter (->RightTriangle 3 4))))
  (testing "perimeter uses the hypotenuse"
    (is (match? 30 (perimeter (->RightTriangle 5 12))))))

(deftest square
  (is (match? 1  (area (->Square 1))))
  (is (match? 4  (perimeter (->Square 1))))
  (is (match? 16 (area (->Square 4))))
  (is (match? 16 (perimeter (->Square 4))))
  (is (match? 25 (area (->Square 5))))
  (is (match? 20 (perimeter (->Square 5)))))

(deftest all-records-satisfy-shape
  (is (satisfies? Shape (->Circle 1)))
  (is (satisfies? Shape (->Rectangle 1 2)))
  (is (satisfies? Shape (->RightTriangle 3 4)))
  (is (satisfies? Shape (->Square 5))))

(deftest describe-circle
  (is (match? "circle with radius 2" (describe {:kind :circle :r 2}))))

(deftest describe-rectangle
  (is (match? "rectangle 3 by 4" (describe {:kind :rectangle :w 3 :h 4}))))

(deftest describe-triangle
  (is (match? "right triangle 3, 4" (describe {:kind :triangle :a 3 :b 4}))))

(deftest describe-square
  (is (match? "square with side 4" (describe {:kind :square :s 4}))))

(deftest describe-unknown-falls-through
  (testing "an unknown :kind falls through to a default that mentions it"
    (is (str/starts-with? (describe {:kind :pentagon}) "unknown shape"))))
