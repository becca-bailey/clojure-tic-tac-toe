(ns tic-tac-toe.ai
  (:require [tic-tac-toe.board :refer :all]
            [tic-tac-toe.game :refer [game-over switch-player progress-game-state]]))

(defn score [board player-marker]
  (if (is-winner board player-marker)
    10
    (if (is-winner board (switch-player player-marker))
      -10
      0)))

(defn get-computer-move [board] 7)

(defn possible-game-state [spot current-game-state]
  (let [marker (:current-player current-game-state)]
    (let [progressed-board
           (place-marker (:board current-game-state) spot marker)]
      (progress-game-state current-game-state progressed-board))))

(defn minimax [spot current-game-state]
  (let [scores {}
        possible-game (possible-game-state spot current-game-state)]
    (if (game-over possible-game)
      (score (:board possible-game) (:current-player possible-game)))))
