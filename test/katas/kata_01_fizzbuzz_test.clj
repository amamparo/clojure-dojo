(ns katas.kata-01-fizzbuzz-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-01-fizzbuzz :refer [fizzbuzz]]))

(deftest empty-input (is (= [] (fizzbuzz 0))))

(deftest small-cases
  (is (= [1] (fizzbuzz 1)))
  (is (= [1 2] (fizzbuzz 2)))
  (is (= [1 2 "Fizz"] (fizzbuzz 3)))
  (is (= [1 2 "Fizz" 4 "Buzz"] (fizzbuzz 5))))

(deftest first-fifteen
  (is (= [1 2 "Fizz" 4 "Buzz" "Fizz" 7 8 "Fizz" "Buzz" 11 "Fizz" 13 14
          "FizzBuzz"]
         (fizzbuzz 15))))

(deftest length-matches
  (is (= 100 (count (fizzbuzz 100))))
  (is (= 1000 (count (fizzbuzz 1000)))))

(deftest returns-a-vector
  (testing "the result supports indexed access (it is a vector)"
    (is (vector? (fizzbuzz 10)))))

(deftest divisibility-rules
  (let [out (fizzbuzz 100)
        at #(nth out (dec %))]
    (testing "every multiple of 15 is FizzBuzz"
      (doseq [i (range 15 101 15)] (is (= "FizzBuzz" (at i)) (str "i=" i))))
    (testing "multiples of 3 not 5 are Fizz"
      (doseq [i (range 1 101)
              :when (and (zero? (mod i 3)) (pos? (mod i 5)))]
        (is (= "Fizz" (at i)) (str "i=" i))))
    (testing "multiples of 5 not 3 are Buzz"
      (doseq [i (range 1 101)
              :when (and (zero? (mod i 5)) (pos? (mod i 3)))]
        (is (= "Buzz" (at i)) (str "i=" i))))
    (testing "non-multiples of 3 or 5 are integers equal to i"
      (doseq [i (range 1 101)
              :when (and (pos? (mod i 3)) (pos? (mod i 5)))]
        (is (= i (at i)) (str "i=" i))))))
