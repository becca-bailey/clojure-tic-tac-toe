(ns tic-tac-toe.game-loop
  (:require [tic-tac-toe.board :refer :all])
  (:require [tic-tac-toe.ui :refer :all])
  (:require [tic-tac-toe.game :refer :all])
  (:require [tic-tac-toe.ai :refer :all]))

(defn move [game-state]
  (if (= "X" (:current-player game-state))
    (get-spot "X")
    (best-computer-move game-state)))

(defn play []
  (do (display-welcome-message)
    (loop [game-state initial-game-state
           last-move nil]
      (do
        (if (not (= 0 (:turn-counter game-state)))
          (confirm-move last-move (switch-player (:current-player game-state))))
        (if (game-over? game-state)
          (if (= :tie (winner game-state))
            (display-tie)
            (display-winner (winner game-state)))
          (do
            (display-board (:board game-state))
            (let [player-move (move game-state)]
              (recur (recreate-game-state player-move game-state) player-move))))))))
