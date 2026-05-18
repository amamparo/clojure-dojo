(ns katas.kata-06-game-of-life)

;; ─── Kata 6: Conway's Game of Life ────────────────────────────────────
;;
;; Represent a generation as a SET of `[x y]` integer pairs — exactly
;; the coordinates of the alive cells. Dead cells are everything else.
;;
;; A neighbour of `[x y]` is one of the eight cells whose coordinates
;; differ by ±1 (and is not the cell itself).
;;
;; Rules, applied simultaneously to produce the next generation:
;;
;;   - A live cell with 2 or 3 live neighbours survives.
;;   - A dead cell with exactly 3 live neighbours is born.
;;   - All other cells are dead next generation.
;;
;; `step` advances the world by one generation.
;;
;;   (step #{}) => #{}
;;
;;   ;; A blinker oscillates between horizontal and vertical:
;;   (step #{[0 0] [1 0] [2 0]})  => #{[1 -1] [1 0] [1 1]}
;;   (step #{[1 -1] [1 0] [1 1]}) => #{[0 0] [1 0] [2 0]}
;;
;;   ;; A 2×2 block is a still life:
;;   (step #{[0 0] [1 0] [0 1] [1 1]})
;;     => #{[0 0] [1 0] [0 1] [1 1]}

(defn step [alive]
  ;; TODO
)
