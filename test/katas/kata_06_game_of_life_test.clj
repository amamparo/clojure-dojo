(ns katas.kata-06-game-of-life-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-06-game-of-life :refer [step]]))

(deftest empty-world (is (= #{} (step #{}))))

(deftest single-cell-dies
  (testing "a lone cell dies of underpopulation" (is (= #{} (step #{[0 0]})))))

(deftest pair-dies
  (testing "two cells with no other neighbours both die"
    (is (= #{} (step #{[0 0] [1 0]})))))

(deftest block-is-still-life
  (let [block #{[0 0] [1 0] [0 1] [1 1]}] (is (= block (step block)))))

(deftest blinker-oscillates
  (let [horiz #{[0 0] [1 0] [2 0]}
        vert #{[1 -1] [1 0] [1 1]}]
    (is (= vert (step horiz)))
    (is (= horiz (step vert)))))

(deftest birth-with-three-neighbours
  (testing "a dead cell with exactly 3 live neighbours is born"
    (let [l-shape #{[0 0] [1 0] [0 1]}]
      ;; [1 1] has neighbours [0 0] [1 0] [0 1] — exactly 3 → birth
      (is (contains? (step l-shape) [1 1])))))

(deftest overpopulation-kills
  (testing "a live cell with more than 3 live neighbours dies"
    (let [crowd #{[0 0] [-1 0] [1 0] [0 -1] [0 1]}]
      ;; centre has 4 live neighbours → dies
      (is (not (contains? (step crowd) [0 0]))))))

(deftest world-is-translation-invariant
  (testing "shifting the world by a fixed offset shifts the result"
    (let [blinker #{[0 0] [1 0] [2 0]}
          shift (fn [s [dx dy]] (set (map (fn [[x y]] [(+ x dx) (+ y dy)]) s)))]
      (is (= (shift (step blinker) [10 -3]) (step (shift blinker [10 -3])))))))
