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
        at #(nth out (dec %) nil) ; nil past the end
        wrong (fn [expected? idxs]
                (for [i idxs
                      :when (not (expected? i (at i)))]
                  [i (at i)]))]
    (testing "every multiple of 15 is FizzBuzz"
      (is (= [] (wrong (fn [_ v] (= "FizzBuzz" v)) (range 15 101 15)))))
    (testing "multiples of 3 not 5 are Fizz"
      (is (= []
             (wrong (fn [_ v] (= "Fizz" v))
                    (filter #(and (zero? (mod % 3)) (pos? (mod % 5)))
                            (range 1 101))))))
    (testing "multiples of 5 not 3 are Buzz"
      (is (= []
             (wrong (fn [_ v] (= "Buzz" v))
                    (filter #(and (zero? (mod % 5)) (pos? (mod % 3)))
                            (range 1 101))))))
    (testing "non-multiples of 3 or 5 are integers equal to i"
      (is (= []
             (wrong (fn [i v] (= i v))
                    (filter #(and (pos? (mod % 3)) (pos? (mod % 5)))
                            (range 1 101))))))))
