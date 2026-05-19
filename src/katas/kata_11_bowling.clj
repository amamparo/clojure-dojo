(ns katas.kata-11-bowling)

;; ─── Kata 11: Bowling ──────────────────────────────────────────────────
;;
;; Score a ten-pin bowling game from a flat vector of rolls. This is a
;; state machine with variable lookahead: a frame consumes ONE roll on a
;; strike, TWO otherwise, and a strike/spare bonus reaches FORWARD into
;; later rolls. Walk exactly ten frames; bonus rolls are scored as bonus,
;; never as an eleventh frame.
;;
;; ── score ──────────────────────────────────────────────────────────────
;;
;;   (score (vec (repeat 20 0)))    => 0      ; gutter game
;;   (score (vec (repeat 20 1)))    => 20
;;   (score (vec (repeat 12 10)))   => 300    ; perfect game
;;   (score [5 5 3 0 0 0 …])        => 16     ; spare: 10 + next roll
;;   (score [10 3 4 0 0 …])         => 24     ; strike: 10 + next two
;;
;; Tenth-frame bonus rolls (after a 10th strike/spare) count only as the
;; bonus — they do not start a new frame:
;;
;;   (score [… 18 zeros … 10 10 10]) => 30    ; not an 11th frame
;;
;; A known sample game:
;;
;;   (score [1 4 4 5 6 4 5 5 10 0 1 7 3 6 4 10 2 8 6]) => 133

(defn score [rolls]
  ;; TODO
)
