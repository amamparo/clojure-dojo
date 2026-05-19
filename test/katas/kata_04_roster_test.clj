(ns katas.kata-04-roster-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-04-roster :refer
             [add-player record-game team-points leaderboard]]))

(defn- sample []
  (-> {}
      (add-player 1 "Ann" :red)
      (add-player 2 "Bo" :blue)
      (add-player 3 "Cy" :red)
      (record-game 1 10)
      (record-game 1 5)
      (record-game 2 5)
      (record-game 3 7)))

(deftest add-player-shape
  (let [r (add-player {} 1 "Ann" :red)]
    (is (= "Ann" (get-in r [1 :name])))
    (is (= :red (get-in r [1 :team])))
    (is (= 0 (get-in r [1 :stats :games])))
    (is (= 0 (get-in r [1 :stats :points])))))

(deftest add-player-replaces
  (let [r (-> {}
              (add-player 1 "Ann" :red)
              (add-player 1 "Ann2" :blue))]
    (is (= "Ann2" (get-in r [1 :name])))
    (is (= :blue (get-in r [1 :team])))))

(deftest record-game-accumulates
  (let [r (sample)]
    (is (= 15 (get-in r [1 :stats :points])))
    (is (= 2 (get-in r [1 :stats :games])))
    (is (= 5 (get-in r [2 :stats :points])))
    (is (= 1 (get-in r [2 :stats :games])))))

(deftest record-game-does-not-touch-others
  (let [r (sample)]
    (is (= 7 (get-in r [3 :stats :points])))
    (is (= "Bo" (get-in r [2 :name])))))

(deftest input-is-not-mutated
  (testing "record-game returns a new roster; the argument is unchanged"
    (let [r0 (add-player {} 1 "Ann" :red)
          _r1 (record-game r0 1 10)]
      (is (= 0 (get-in r0 [1 :stats :points]))))))

(deftest team-points-sums
  (let [r (sample)]
    (is (= 22 (team-points r :red)))
    (is (= 5 (team-points r :blue)))
    (is (= 0 (team-points r :green)))))

(deftest leaderboard-orders-and-projects
  (is (= [{:name "Ann", :points 15} {:name "Cy", :points 7}
          {:name "Bo", :points 5}]
         (leaderboard (sample)))))

(deftest leaderboard-tie-broken-by-name
  (let [r (-> {}
              (add-player 1 "Zoe" :red)
              (add-player 2 "Amy" :red)
              (record-game 1 5)
              (record-game 2 5))]
    (is (= [{:name "Amy", :points 5} {:name "Zoe", :points 5}]
           (leaderboard r)))))

(deftest leaderboard-keys-are-exactly-name-and-points
  (let [row (first (leaderboard (sample)))]
    (is (= #{:name :points} (set (keys row))))))
