(ns tic-tac-toe.game-loop
  (:gen-class)
  (:require [tic-tac-toe.board :as board]
            [tic-tac-toe.ui :as ui]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.ai :as ai]
            [tic-tac-toe.player :as player]))

(defmulti move (fn [game-state] (:player-type (game/current-player game-state))))

(defmethod move :human [game-state]
  (ui/get-spot
    (:marker (game/current-player game-state)) (board/available-spots (:board game-state))))

(defmethod move :computer [game-state]
  (ai/best-computer-move game-state))

(def default-players
  [(player/human "X") (player/computer "O")])

(def initial-board [0 1 2 3 4 5 6 7 8])

(def initial-state (game/game-state [0 1 2 3 4 5 6 7 8] default-players))

(defn initial-state-with-player-markers []
  (let [[player-1 player-2] (:players initial-state)]
    (merge initial-state
      {:players [
                  (merge player-1
                    {:marker (ui/get-marker-choice player-1)})
                  (merge player-2
                    {:marker (ui/get-marker-choice player-2)})]})))

(defn game-setup []
  (ui/clear-screen)
  (ui/display-welcome-message)
  (initial-state-with-player-markers))

(defn display-last-move [last-move game-state]
  (if last-move
    (ui/confirm-move last-move (game/switch-player (game/current-player game-state) game-state))))

(defn play []
  (loop [game-state (initial-state-with-player-markers)
         last-move nil]
    (do
      (ui/display-board (:board game-state))
      (display-last-move last-move game-state)
      (cond
        (board/winner game-state)
        (ui/display-winner (board/winner game-state))
        (board/tie? (:board game-state))
        (ui/display-tie)
        :no-winner-or-tie
          (let [next-move (move game-state)]
            (ui/clear-screen)
            (recur (game/progress-game-state next-move game-state) next-move))))))

(defn -main []
  (game-setup)
  (play))
