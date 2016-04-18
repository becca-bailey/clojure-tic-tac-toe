(ns tic-tac-toe.ai
  (:require [tic-tac-toe.board :refer :all]
            [tic-tac-toe.core :refer :all]))

(defn score [board player-marker]
  (if (is-winner board player-marker)
    10
    (if (is-winner board (switch-player player-marker))
      -10
      0)))

(defn get-computer-move [board] 7)

(defn possible-game-state [spot current-game-state]
  (progress-game-state current-game-state
    (place-marker (:board current-game-state) spot (:current-player current-game-state))))

; (defn minimax [spot current-game-state]
;   (let [scores {}
;         possible-game (possible-game-state spot current-game-state)]))
