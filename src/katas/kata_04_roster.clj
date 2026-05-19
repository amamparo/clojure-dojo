(ns katas.kata-04-roster)

;; ─── Kata 4: Roster ────────────────────────────────────────────────────
;;
;; Maps are the workhorse data structure. This kata is map surgery:
;; building, reading, and transforming nested maps without ever mutating
;; one. Every function returns a NEW value; the input is untouched.
;;
;; A roster is a map of player-id → player. A player looks like:
;;
;;   {:name "Ann" :team :red :stats {:games 3 :points 18}}
;;
;; ── add-player ─────────────────────────────────────────────────────────
;;
;;   (add-player roster id name team)
;;     → roster with id mapped to a fresh player on `team`, named `name`,
;;       with stats {:games 0 :points 0}. Replaces any existing entry.
;;
;; ── record-game ────────────────────────────────────────────────────────
;;
;;   (record-game roster id points)
;;     → roster where player `id` has :games incremented by 1 and
;;       :points increased by `points`. Other players untouched.
;;       `id` is guaranteed to exist.
;;
;; ── team-points ────────────────────────────────────────────────────────
;;
;;   (team-points roster team)
;;     → total :points across all players whose :team is `team`
;;       (0 if none).
;;
;; ── leaderboard ────────────────────────────────────────────────────────
;;
;;   (leaderboard roster)
;;     → vector of {:name :points} maps, highest points first, ties
;;       broken by :name ascending. Only those two keys, nothing else.
;;
;; Examples:
;;
;;   (def r (-> {}
;;              (add-player 1 "Ann" :red)
;;              (add-player 2 "Bo"  :blue)
;;              (record-game 1 10)
;;              (record-game 1 5)
;;              (record-game 2 5)))
;;
;;   (get-in r [1 :stats :points])  => 15
;;   (get-in r [1 :stats :games])   => 2
;;   (team-points r :red)           => 15
;;   (leaderboard r)
;;     => [{:name "Ann" :points 15} {:name "Bo" :points 5}]

(defn add-player [roster id name team]
  ;; TODO
)

(defn record-game [roster id points]
  ;; TODO
)

(defn team-points [roster team]
  ;; TODO
)

(defn leaderboard [roster]
  ;; TODO
)
