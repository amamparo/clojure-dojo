(ns katas.kata-08-bowling-test
  (:require [clojure.test :refer [deftest is testing]]
            [matcher-combinators.test]
            [katas.kata-08-bowling :refer [score]]))

(deftest gutter-game
  (is (match? 0 (score (vec (repeat 20 0))))))

(deftest all-ones
  (is (match? 20 (score (vec (repeat 20 1))))))

(deftest perfect-game
  (is (match? 300 (score (vec (repeat 12 10))))))

(deftest one-spare-then-bonus
  (testing "frame 1 is a spare; the next roll counts twice"
    (is (match? 16 (score (into [5 5 3] (repeat 17 0)))))))

(deftest one-strike-then-bonus
  (testing "frame 1 is a strike; the next two rolls count twice"
    (is (match? 24 (score (into [10 3 4] (repeat 16 0)))))))

(deftest tenth-frame-spare
  (is (match? 17 (score (into (vec (repeat 18 0)) [5 5 7])))))

(deftest tenth-frame-strike
  (is (match? 30 (score (into (vec (repeat 18 0)) [10 10 10])))))

(deftest mixed-game
  (testing "the canonical mixed-game test from the bowling kata"
    (is (match? 133
                (score [1 4 4 5 6 4 5 5 10 0 1 7 3 6 4 10 2 8 6])))))
