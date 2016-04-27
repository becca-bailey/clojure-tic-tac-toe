(ns tic-tac-toe.ai
  (:require [tic-tac-toe.board :as board]
            [tic-tac-toe.game :as game]))

(def starting-score 10)

(defn score [player current-game-state depth]
  (let [board (:board current-game-state)]
    (cond
      (board/is-winner? board (:marker player))
      (- starting-score depth)
      (board/is-winner? board (:marker (game/switch-player player current-game-state)))
      (- depth starting-score)
      :is-tie 0)))

(declare score-for-each-possible-move)

(defn return-min-or-max [coll original-game-state current-game-state]
  (if (= (game/current-player current-game-state) (game/current-player original-game-state))
    (apply max (vals coll))
    (apply min (vals coll))))

(defn minimax [spot original-game-state current-game-state depth]
  (let [possible-game-state (game/progress-game-state spot current-game-state)]
    (if (game/game-over? possible-game-state)
      (score (game/current-player original-game-state) possible-game-state depth)
      (return-min-or-max (score-for-each-possible-move original-game-state possible-game-state depth) original-game-state possible-game-state))))

(defn score-for-each-possible-move [original-game-state game-state depth]
  (let [possible-moves (board/available-spots (:board game-state))]
    (zipmap possible-moves (map #(minimax % original-game-state game-state (inc depth)) possible-moves))))

(defn best-computer-move [current-game-state]
  (let [scores (score-for-each-possible-move current-game-state current-game-state 0)]
    (key (apply max-key val scores))))
