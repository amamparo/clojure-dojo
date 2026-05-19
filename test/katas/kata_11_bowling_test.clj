(ns katas.kata-11-bowling-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-11-bowling :refer [score]]))

(deftest gutter-game (is (= 0 (score (vec (repeat 20 0))))))

(deftest all-ones (is (= 20 (score (vec (repeat 20 1))))))

(deftest one-spare
  (testing "spare in frame 1: 10 + next roll (3), then 3 + 0"
    (is (= 16 (score (into [5 5 3 0] (vec (repeat 16 0))))))))

(deftest one-strike
  (testing "strike in frame 1: 10 + next two (3,4), then 3+4"
    (is (= 24 (score (into [10 3 4] (vec (repeat 16 0))))))))

(deftest perfect-game (is (= 300 (score (vec (repeat 12 10))))))

(deftest all-spares-of-five
  (testing "ten 5+5 spares, each + next roll of 5, plus final bonus"
    (is (= 150 (score (vec (repeat 21 5)))))))

(deftest spare-in-tenth-frame
  (testing "18 zeros then 5,5 (spare) + 5 bonus = 15"
    (is (= 15 (score (into (vec (repeat 18 0)) [5 5 5]))))))

(deftest strike-in-tenth-frame
  (testing "18 zeros then 10 + two bonus rolls 7,2 = 19"
    (is (= 19 (score (into (vec (repeat 18 0)) [10 7 2]))))))

(deftest does-not-start-an-eleventh-frame
  (testing "bonus rolls after the 10th frame are bonus only"
    ;; nine open 0-frames, 10th frame strike + bonus 10,10
    ;; = frame10: 10 + 10 + 10 = 30, total 30 (no 11th frame counted)
    (is (= 30 (score (into (vec (repeat 18 0)) [10 10 10]))))))

(deftest mixed-game
  ;; A well-known sample game scoring 133.
  (is (= 133 (score [1 4 4 5 6 4 5 5 10 0 1 7 3 6 4 10 2 8 6]))))
