(ns katas.kata-08-bowling)

;; ─── Kata 8: Bowling scorer ────────────────────────────────────────────
;;
;; You are learning: recursion with explicit accumulators, destructuring,
;; turning rules into a small state machine.
;;
;; `score` takes a flat vector of rolls (each 0..10) for a complete game
;; and returns the integer total.
;;
;; A game has 10 frames:
;;   - Strike (a single roll of 10): the frame ends after that one roll;
;;     bonus = the next two rolls.
;;   - Spare (two rolls summing to 10): the frame ends; bonus = the next
;;     one roll.
;;   - Otherwise the frame is two rolls; no bonus.
;;
;; In the 10th frame, a strike grants two extra rolls; a spare grants
;; one. Those extras do NOT begin a new frame.
;;
;; You may assume the input is well-formed (a complete, legal game).
;; Convention: strikes occupy ONE slot in the rolls vector. A perfect
;; game is therefore [10 10 10 10 10 10 10 10 10 10 10 10] (12 rolls).
;;
;; Examples:
;;   (score (vec (repeat 20 0)))   => 0     ; gutter game
;;   (score (vec (repeat 20 1)))   => 20    ; all ones
;;   (score (vec (repeat 12 10)))  => 300   ; perfect game

(defn score [rolls]
  ;; TODO
  )
