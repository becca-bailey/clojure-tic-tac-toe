(ns tic-tac-toe.ai

  (:require [tic-tac-toe.board :as board]
            [tic-tac-toe.game :as game]))

(def starting-score 10)

(defn score [player current-game-state depth]
  (let [board (:board current-game-state)]
    (cond
      (game/is-winner? current-game-state player)
      (- starting-score depth)
      (game/is-winner? current-game-state (game/switch-player player current-game-state))
      (- depth starting-score)
      :no-winner 0)))

(def memoize-score (memoize score))

(declare score-for-each-possible-move)

(defn return-min-or-max [coll player current-game-state]
  (if (= (game/current-player current-game-state) player)
    (apply max (vals coll))
    (apply min (vals coll))))

(def max-depth 4) 

(defn is-max-depth? [game-state depth]
  (and (>= (count (:board game-state)) 16) (= depth max-depth)))

(defn minimax [spot player current-game-state depth]
  (let [possible-game-state (game/progress-game-state spot current-game-state)]
    (if (or (game/game-over? possible-game-state) (is-max-depth? possible-game-state depth))
      (score player possible-game-state depth)
      (return-min-or-max (score-for-each-possible-move player possible-game-state depth) player possible-game-state))))

(def memoize-minimax (memoize minimax))

(defn score-for-each-possible-move [player game-state depth]
  (let [possible-moves (board/available-spots (:board game-state))]
    (zipmap possible-moves (map #(memoize-minimax % player game-state (inc depth)) possible-moves))))

(defn best-computer-move [current-game-state]
  (let [scores (score-for-each-possible-move (game/current-player current-game-state) current-game-state 0)]
    (key (apply max-key val scores))))

(defn human-move-simulation [current-game-state]
  (let [scores (score-for-each-possible-move (game/current-player current-game-state) current-game-state 0)]
     (let [min-score (apply min-key val scores)
           max-score (apply max-key val scores)]
        (let [median-scores (filter #(and (> (val %) (val min-score)) (< (val %) (val max-score))) scores)]
          (if (empty? median-scores)
            (key max-score)
            (key (first median-scores)))))))
