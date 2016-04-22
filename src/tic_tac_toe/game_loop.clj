(ns tic-tac-toe.game-loop
  (:require [tic-tac-toe.board :as board])
  (:require [tic-tac-toe.ui :as ui])
  (:require [tic-tac-toe.game :as game])
  (:require [tic-tac-toe.ai :as ai]))

(defn move [game-state]
  (if (= "X" (game/current-player game-state))
    (ui/get-spot "X")
    (ai/best-computer-move game-state)))

(def initial-state (game/game-state [0 1 2 3 4 5 6 7 8] "X" 0))

(defn play []
  (ui/clear-screen)
  (ui/display-welcome-message)
  (loop [game-state initial-state
          last-move nil]
    (do
      (if last-move
        (ui/confirm-move last-move (game/switch-player (game/current-player game-state))))
      (ui/display-board (:board game-state))
      (cond
        (game/winner game-state)
        (ui/display-winner (game/winner game-state))
        (board/tie? (:board game-state))
        (ui/display-tie)
        :no-winner-or-tie
          (let [next-move (move game-state)]
            (ui/clear-screen)
            (recur (game/progress-game-state next-move game-state) next-move))))))
