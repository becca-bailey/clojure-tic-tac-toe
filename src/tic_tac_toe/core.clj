(ns tic-tac-toe.core)

(defn switch-player [player-marker]
  (if (= "X" player-marker) "O" "X"))
