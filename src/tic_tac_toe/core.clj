(ns tic-tac-toe.core
  (:require [tic-tac-toe.board :refer [won tie]]))

(def initial-board [0 1 2 3 4 5 6 7 8])

(defn game-state [board current-player turn-counter]
  {:board board :current-player current-player :turn-counter turn-counter})

(def initial-game-state (game-state initial-board "X" 0))

(defn switch-player [player-marker]
  (if (= "X" player-marker) "O" "X"))

(defn progress-game-state [game-state updated-board]
  (merge
   (merge
    (merge game-state {:board updated-board}
      {:turn-counter (inc (:turn-counter game-state))}
      {:current-player (switch-player (:current-player game-state))}))))

(defn game-over [board]
  (or (won board) (tie board)))
