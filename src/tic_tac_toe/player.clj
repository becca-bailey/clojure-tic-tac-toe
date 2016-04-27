(ns tic-tac-toe.player)

(defn human [marker]
  {:marker marker :player-type :human})

(defn computer [marker]
  {:marker marker :player-type :computer})
