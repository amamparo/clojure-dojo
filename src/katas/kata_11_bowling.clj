(ns katas.kata-11-bowling)

;; ─── Kata 11: Bowling ──────────────────────────────────────────────────
;;
;; Score a ten-pin bowling game from a flat vector of rolls. Ten frames; a
;; frame is two rolls, or one roll if it is a strike (all ten pins). A spare
;; (ten pins in two rolls) scores 10 plus the next roll; a strike scores 10
;; plus the next two rolls. `rolls` is always a complete, legal game; do not
;; validate it.
;;
;; ── score ──────────────────────────────────────────────────────────────
;;
;;   (score (vec (repeat 20 0)))             => 0    ; gutter game
;;   (score (vec (repeat 20 1)))             => 20
;;   (score (vec (repeat 12 10)))            => 300  ; perfect game
;;   (score (into [5 5 3 0] (repeat 16 0)))  => 16   ; spare: 10 + next roll
;;   (score (into [10 3 4] (repeat 16 0)))   => 24   ; strike: 10 + next two
;;
;; Tenth-frame bonus rolls (after a 10th strike/spare) count only as the
;; bonus — they do not start a new frame:
;;
;;   (score (into (vec (repeat 18 0)) [10 10 10])) => 30  ; not an 11th frame
;;
;; A known sample game:
;;
;;   (score [1 4 4 5 6 4 5 5 10 0 1 7 3 6 4 10 2 8 6]) => 133

(defn score [rolls]
  ;; TODO
)
