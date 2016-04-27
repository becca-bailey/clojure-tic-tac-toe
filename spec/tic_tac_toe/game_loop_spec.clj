(ns tic-tac-toe.game-loop-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-loop :as game-loop]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.ui :as ui]
            [tic-tac-toe.ai :as ai]))

(defn current-player-type [game-state]
  (:player-type (game/current-player game-state)))

(def state-with-computer-player (game/progress-game-state 4 game-loop/initial-state))

(describe "Game loop"
  (context "#move"
    (around [it]
      (with-out-str (it)))

    (it "should ask for user input if the current player is a human"
      (should= :human (current-player-type game-loop/initial-state))
      (should-invoke ui/get-spot {:with ["X" '(0 1 2 3 4 5 6 7 8)]}
        (game-loop/move game-loop/initial-state)))

    (it "should call #best-computer-move if it is called on a game state with a current computer player"
      (should= :computer (current-player-type state-with-computer-player))
      (should-invoke ai/best-computer-move {:with [state-with-computer-player]}
        (game-loop/move state-with-computer-player)))))
