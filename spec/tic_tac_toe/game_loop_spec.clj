(ns tic-tac-toe.game-loop-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-loop :as game-loop]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.ui :as ui]
            [tic-tac-toe.ai :as ai]
            [tic-tac-toe.player :as player]))

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
        (game-loop/move state-with-computer-player))))

  (context "#game-setup"
    (around [it]
      (with-out-str (it)))

    (it "should clear the screen"
      (with-in-str "X\nO"
        (should-invoke ui/clear-screen {:with []} (game-loop/game-setup))))

    (it "should display a welcome message"
      (with-in-str "X\nO"
        (should-invoke ui/display-welcome-message {:with []} (game-loop/game-setup)))))

  (context "#display-last-move"
    (around [it]
      (with-out-str (it)))

    (it "should call #confirm-move unless last move is nil"
      (should-invoke ui/confirm-move {:with [4 (player/human "X")]} (game-loop/display-last-move 4 state-with-computer-player)))

    (it "returns nil if there is no last move (ex. for an initial board state)"
      (should-be-nil (game-loop/display-last-move nil game-loop/initial-state))))

  (context "#initial-state-with-player-markers"
    (around [it]
      (with-out-str (it)))

    (it "initializes a game state with an empty board"
      (should= game-loop/initial-board
        (:board
          (with-in-str "X\nO"
            (game-loop/initial-state-with-player-markers)))))

    (it "sets the marker for the human player"
       (should= "$"
         (with-in-str "$\n#"
           (:marker (first (:players (game-loop/initial-state-with-player-markers)))))))

    (it "sets the marker for the computer"
      (should= "#"
        (with-in-str "$\n#"
          (:marker (second (:players (game-loop/initial-state-with-player-markers)))))))))
