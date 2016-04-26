(ns tic-tac-toe.game
  (:require [tic-tac-toe.board :as board]))

(def initial-board [0 1 2 3 4 5 6 7 8])

(defn game-state [board first-player turn-counter]
  {:board board :first-player first-player :turn-counter turn-counter})

(defn switch-player [player-marker]
  (if (= "X" player-marker) "O" "X"))

(defn update-board [game-state updated-board]
    (merge game-state
      {:board updated-board}
      {:turn-counter (inc (:turn-counter game-state))}))

(defn game-over? [game-state]
  (or (board/won? (:board game-state)) (board/tie? (:board game-state))))

(defn winner [game-state]
  (if (game-over? game-state)
    (cond
      (board/is-winner? (:board game-state) "X")
      "X"
      (board/is-winner? (:board game-state) "O")
      "O")))
      
(defn current-player [game-state]
  (if (even? (:turn-counter game-state))
    (:first-player game-state)
    (switch-player (:first-player game-state))))

(defn progress-game-state [spot current-game-state]
  (let [progressed-board
         (board/place-marker (:board current-game-state) spot (current-player current-game-state))] ;)]
    (update-board current-game-state progressed-board)))
