(ns tic-tac-toe.game-loop-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-loop :as game-loop]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.ui :as ui]
            [tic-tac-toe.ai :as ai]
            [tic-tac-toe.player :as player]))

(def player-1 (player/human "X"))
(def player-2 (player/computer "O"))

(def tie-board (board/make-board {"X" #{1 3 4 6 8} "O" #{0 2 5 7}}))
(def x-wins (board/make-board {"X" #{0 4 8} "O" #{1 7}}))
(def x-wins-state (game/game-state x-wins))
(def tie-state (game/game-state tie-board))
(def will-tie-state
  (game/game-state (board/make-board {"X" #{3 4 6 8} "O" #{0 2 5 7}})))

(def winning-combinations-3x3 [[0 1 2] [3 4 5] [6 7 8]
                               [0 3 6] [1 4 7] [2 5 8]
                               [0 4 8] [2 4 6]])

(defn current-player-type [game-state]
  (:player-type (game/current-player game-state)))

(def state-with-computer-player (game/progress-game-state 4 game-loop/initial-3x3-state))

(describe "Game loop"
  (context "#move"
    (around [it]
      (with-out-str (it)))

    (it "should ask for user input if the current player is a human"
      (should= :human (current-player-type game-loop/initial-3x3-state))
      (should-invoke ui/get-spot {:with ['(0 1 2 3 4 5 6 7 8)]}
        (game-loop/move game-loop/initial-3x3-state)))

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
      (should-invoke ui/confirm-move {:with [4 (player/human "X")]}
        (game-loop/display-last-move 4 state-with-computer-player)))

    (it "returns nil if there is no last move (ex. for an initial board state)"
      (should-be-nil (game-loop/display-last-move nil game-loop/initial-3x3-state))))

  (context "#set-player-markers"
    (around [it]
      (with-out-str (it)))

    (it "initializes a game state with an empty board"
      (should= game-loop/initial-3x3
        (:board
          (with-in-str "X\nO"
            (game-loop/set-player-markers game-loop/initial-3x3-state)))))

    (it "sets the marker for the human player"
       (should= "$"
         (with-in-str "$\n#"
           (:marker (first (:players (game-loop/set-player-markers game-loop/initial-3x3-state)))))))

    (it "sets the marker for the computer"
      (should= "#"
        (with-in-str "$\n#"
          (:marker (second (:players (game-loop/set-player-markers game-loop/initial-3x3-state))))))))

  (context "#play"

   (around [it]
     (with-out-str (it)))

   (it "calls #player-would-like-to-continue if game is an an end state"
     (should-invoke ui/player-would-like-to-continue? {:with []}
       (with-in-str "n"
         (game-loop/play tie-state)))

     (should-invoke ui/player-would-like-to-continue? {:with []}
       (with-in-str "n"
         (game-loop/play x-wins-state))))

   (it "ends the game if the board has a winner"
     (should-invoke ui/display-winner {:with [player-1]}
        (with-in-str "n"
          (game-loop/play x-wins-state))))

   (it "ends the game if the board is a tie"
     (should-invoke ui/display-tie {:with []}
       (with-in-str "n"
         (game-loop/play tie-state))))

   (it "ends the game if the user inputs 'n' in an end-game state"
     (should-invoke ui/goodbye {:with []}
       (with-in-str "n"
         (game-loop/play tie-state))))

   (it "recurs the game loop is the user inputs 'y' in an end-game-state"
     (should-invoke game-loop/play {:with [tie-state]}
       (with-in-str "y\nn"
         (game-loop/play tie-state)))

    (around [it]
      (with-out-str (it)))

    (it "ends the game if the board has a winner"
      (should-invoke ui/display-winner {:with [player-1]}
         (with-in-str "n"
           (game-loop/play x-wins-state))))

    (it "ends the game if the board is a tie"
      (should-invoke ui/display-tie {:with []}
        (with-in-str "n"
          (game-loop/play tie-state)))))

   (it "calls #move and recurs if the game is not an an end state"
    (should-invoke game/game-over? {:with [will-tie-state] :return [false]}
      (with-in-str "1\nn"
         (game-loop/play will-tie-state)))))

  (context "#main"
    (around [it]
      (with-out-str (it)))

    (it "calls the #game-setup function"
      (should-invoke game-loop/game-setup {:with []}
        (with-redefs [game-loop/play (fn [game-state] true)]
          (with-in-str "X\nO"
            (game-loop/-main)))))

    (it "calls #play with a new game"
      (should-invoke game-loop/play {:with [game-loop/initial-3x3-state]}
        (with-in-str "X\nO"
          (game-loop/-main))))

    (it "starts a new game with a 4x4 board if '4x4' is input as a command line argument"
      (should-invoke game-loop/play {:with [game-loop/initial-4x4-state]}
        (with-in-str "X\nO"
          (game-loop/-main "4x4"))))))
