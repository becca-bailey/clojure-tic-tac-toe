(ns tic-tac-toe.game
  (:require [tic-tac-toe.board :refer [won? tie? is-winner place-marker]]))

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

(defn game-over? [game-state]
  (or (won? (:board game-state)) (tie? (:board game-state))))

(defn winner [game-state]
  (if (game-over? game-state)
    (cond
      (is-winner (:board game-state) "X")
      "X"
      (is-winner (:board game-state) "O")
      "O"
      :else :tie)))

(defn recreate-game-state [spot current-game-state]
  (let [marker (:current-player current-game-state)
        board (:board current-game-state)]
    (let [progressed-board
           (place-marker board spot marker)]
      (progress-game-state current-game-state progressed-board))))
