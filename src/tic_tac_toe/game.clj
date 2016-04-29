(ns tic-tac-toe.game
  (:require [tic-tac-toe.board :as board]
            [tic-tac-toe.player :as player]))

(def initial-board [0 1 2 3 4 5 6 7 8])

(defn set-turn-counter [board]
  (count (filter string? board)))

(defn game-state [board players]
  (let [[player-1 player-2] players]
    {:board board :players [player-1 player-2] :first-player player-1 :turn-counter (set-turn-counter board)}))

(def initial-state (game-state initial-board [(player/human "X") (player/computer "O")]))

(defn switch-player [player game-state]
  (let [[player-1 player-2] (:players game-state)]
    (if (= player-1 player)
      player-2
      player-1)))

(defn current-player [game-state]
  (if (even? (:turn-counter game-state))
    (:first-player game-state)
    (switch-player (:first-player game-state) game-state)))

(defn update-board [game-state updated-board]
    (merge game-state
      {:board updated-board}
      {:turn-counter (inc (:turn-counter game-state))}))

(defn game-over? [game-state players]
  (or (board/won? (:board game-state) players) (board/tie? (:board game-state) players)))

(defn current-player [game-state]
  (if (even? (:turn-counter game-state))
    (:first-player game-state)
    (switch-player (:first-player game-state) game-state)))

(defn progress-game-state [spot current-game-state]
  (let [progressed-board
         (board/place-marker (:board current-game-state) spot (:marker (current-player current-game-state)))]
      (update-board current-game-state progressed-board)))
