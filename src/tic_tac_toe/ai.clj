(ns tic-tac-toe.ai
  (:require [tic-tac-toe.board :refer :all]
            [tic-tac-toe.game :refer [game-over switch-player progress-game-state]]))

(defn score [board player-marker]
  (if (is-winner board player-marker)
    10
    (if (is-winner board (switch-player player-marker))
      -10
      0)))

(defn possible-game-state [spot current-game-state]
  (let [marker (:current-player current-game-state)
        board (:board current-game-state)]
    (let [progressed-board
           (place-marker board spot marker)]
      (progress-game-state current-game-state progressed-board))))

(defn minimax [spot current-game-state]
  (loop [spot spot
         scores {}
         possible-game (possible-game-state spot current-game-state)]
    (let [board (:board possible-game)
          marker (:current-player possible-game)]
      (println possible-game)
      (if (game-over possible-game)
        (score board (:current-player possible-game))))))
        ; for each available spot
          ; recursively calculate the score

          ; if current player is computer
            ; return the spot with the max score

          ; else
            ;return the spot with the min score

(defn score-for-each-possible-move [current-game-state]
  (for [possible-move (available-spots (:board current-game-state))]
    [possible-move (minimax possible-move current-game-state)]))

(defn best-computer-move [current-game-state]
  (let [scores (score-for-each-possible-move current-game-state)]))
    ; for each key/value pair
      ; find max score
      ; return the best mvoe


; (defn best-computer-move [current-game-state]
;   (loop [scores         {}
;          possible-moves (available-spots (:board current-game-state))]
;     (if (= possible-moves [])
;       (first (first (filter (fn [[spot score]] (= (apply max (vals scores)) score)) scores)))
;       (recur (assoc scores (first possible-moves) (minimax (first possible-moves) current-game-state))
;         (rest possible-moves)))))
