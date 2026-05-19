(ns katas.kata-10-roman-numerals-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-10-roman-numerals :refer [->roman roman->]]))

(deftest to-roman-singles
  (is (= "I" (->roman 1)))
  (is (= "II" (->roman 2)))
  (is (= "III" (->roman 3)))
  (is (= "IV" (->roman 4)))
  (is (= "V" (->roman 5)))
  (is (= "IX" (->roman 9)))
  (is (= "X" (->roman 10))))

(deftest to-roman-composites
  (is (= "LVIII" (->roman 58)))
  (is (= "XLII" (->roman 42)))
  (is (= "XCIX" (->roman 99)))
  (is (= "CDXLIV" (->roman 444)))
  (is (= "MCMXCIV" (->roman 1994)))
  (is (= "MMMCMXCIX" (->roman 3999)))
  (is (= "MMMDCCCLXXXVIII" (->roman 3888))))

(deftest from-roman-singles
  (is (= 1 (roman-> "I")))
  (is (= 4 (roman-> "IV")))
  (is (= 5 (roman-> "V")))
  (is (= 9 (roman-> "IX")))
  (is (= 10 (roman-> "X"))))

(deftest from-roman-composites
  (is (= 58 (roman-> "LVIII")))
  (is (= 444 (roman-> "CDXLIV")))
  (is (= 1994 (roman-> "MCMXCIV")))
  (is (= 3999 (roman-> "MMMCMXCIX"))))

(deftest round-trip-full-range
  (testing "every integer 1..3999 survives ->roman then roman->"
    (is (every? (fn [n] (= n (roman-> (->roman n)))) (range 1 4000)))))
