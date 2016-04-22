(ns tic-tac-toe.game-loop
  (:require [tic-tac-toe.board :refer :all])
  (:require [tic-tac-toe.ui :refer :all])
  (:require [tic-tac-toe.game :refer :all])
  (:require [tic-tac-toe.ai :refer :all]))

(def initial-board [0 1 2 3 4 5 6 7 8])

(defn play []
  (do (display-welcome-message)
    (display-board initial-board)))
