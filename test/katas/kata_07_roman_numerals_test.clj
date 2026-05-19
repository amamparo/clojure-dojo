(ns katas.kata-07-roman-numerals-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-07-roman-numerals :refer [int->roman roman->int]]))

(def cases
  [[1 "I"] [2 "II"] [3 "III"] [4 "IV"] [5 "V"] [9 "IX"] [10 "X"] [40 "XL"]
   [49 "XLIX"] [50 "L"] [90 "XC"] [99 "XCIX"] [100 "C"] [400 "CD"]
   [444 "CDXLIV"] [500 "D"] [900 "CM"] [999 "CMXCIX"] [1000 "M"]
   [1994 "MCMXCIV"] [2024 "MMXXIV"] [3888 "MMMDCCCLXXXVIII"]
   [3999 "MMMCMXCIX"]])

(deftest int->roman-test
  (doseq [[n s] cases] (is (= s (int->roman n)) (str "n=" n))))

(deftest roman->int-test
  (doseq [[n s] cases] (is (= n (roman->int s)) (str "s=" s))))

(deftest round-trip
  (testing "every value in 1..3999 round-trips"
    (let [bad (filter (fn [n]
                        (not= n
                              (-> n
                                  int->roman
                                  roman->int)))
                      (range 1 4000))]
      (is (empty? bad) (str "first failures: " (vec (take 5 bad)))))))
