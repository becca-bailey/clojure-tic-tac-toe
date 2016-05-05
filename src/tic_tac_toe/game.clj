(ns tic-tac-toe.game
  (:require [tic-tac-toe.board :as board]
            [tic-tac-toe.player :as player]))

(def initial-board [0 1 2 3 4 5 6 7 8])

(defn set-turn-counter [board]
  (count (filter string? board)))

(defn game-state 
  ([board]
   {:board board 
    :players [(player/human "X") (player/computer "O")] 
    :turn-counter (set-turn-counter board)})
  ([board players]
    (let [[player-1 player-2] players]
      {:board board 
       :players [player-1 player-2]
       :turn-counter (set-turn-counter board)})))

(def initial-state (game-state initial-board [(player/human "X") (player/computer "O")]))

(defn switch-player [player game-state]
  (let [[player-1 player-2] (:players game-state)]
    (if (= player-1 player)
      player-2
      player-1)))

(defn current-player [game-state]
  (let [[player-1 player-2] (:players game-state)]
    (if (even? (:turn-counter game-state))
      player-1
      player-2)))

(defn update-board [game-state updated-board]
    (merge game-state
      {:board updated-board}
      {:turn-counter (inc (:turn-counter game-state))}))

(defn progress-game-state [spot current-game-state]
  (let [progressed-board
         (board/place-marker (:board current-game-state) spot (:marker (current-player current-game-state)))]
    (update-board current-game-state progressed-board)))

(defn is-winner? [game-state player]
  (loop [possible-wins (board/winning-combinations (board/grid-size (:board game-state)))] 
    (cond
      (empty? possible-wins)
      false
      (board/is-a-winning-combination? (:board game-state) (first possible-wins) (:marker player))
      true
      :else (recur (rest possible-wins)))))

(defn won? [game-state]
  (let [[player-1 player-2] (:players game-state)]
    (or (is-winner? game-state player-1) (is-winner? game-state player-2))))

(defn tie? [game-state]
  (and (every? string? (:board game-state)) (not (won? game-state))))

(defn game-over? [game-state]
  (or (won? game-state) (tie? game-state)))

(defn winner [game-state]
  (let [[player-1 player-2] (:players game-state)]
    (cond
      (is-winner? game-state player-1)
      player-1
      (is-winner? game-state player-2)
      player-2
      :no-winner false)))
