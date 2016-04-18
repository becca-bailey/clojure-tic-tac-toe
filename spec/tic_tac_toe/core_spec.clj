(ns tic-tac-toe.core-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer :all]
            [tic-tac-toe.ui :refer :all]
            [tic-tac-toe.board :refer :all]
            [tic-tac-toe.ai :refer :all]))

(def new-board " 0 | 1 | 2 \n---------\n 3 | 4 | 5 \n---------\n 6 | 7 | 8 ")
(def first-move-x [0 1 2 3 "X" 5 6 7 8])
(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def o-wins [0 1 2 "O" "O" "O" 7 8 9])
(def o-winning-move ["X" "O" 2 3 "O" "X" 6 7 8])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])

(describe "tic tac toe"
  (describe "core"
    (context "#switch-player"
      (it "returns the opposite marker"
        (should= "O" (switch-player "X"))
        (should= "X" (switch-player "O"))))

    (context "game-state"
      (it "has a board"
        (should= initial-board (:board initial-game-state)))

      (it "has a curent player"
        (should= (or "X" "O") (:current-player initial-game-state)))

      (it "has a turn counter"
        (should= 0 (:turn-counter initial-game-state)))

      (context "#progress-game-state"
        (it "creates a copy of the current game state with updated board, current player, and turn counter"
          (let [updated-state (progress-game-state initial-game-state first-move-x)]
            (should= first-move-x (:board updated-state))
            (should=
             (inc (:turn-counter initial-game-state)) (:turn-counter updated-state))
            (should=
              (switch-player
                (:current-player initial-game-state)) (:current-player updated-state))))))

    (context "#game-over"
      (it "returns true if a player has won or if game is tied"
        (let [tie-state (game-state tie-game "X" 9)
              x-win-state (game-state x-wins "X" 3)
              first-move-state (game-state first-move-x "O" 1)]
          (should= true (game-over tie-game))
          (should= true (game-over x-win-state))
          (should= false (game-over first-move-state))))))


  (context "UI"
    (context "#display-board"
      (it "prints an empty board"
        (should= new-board (display-board initial-board))))

    (context "#get-spot"

      (around [it]
        (with-out-str (it)))

      (it "returns user input as an integer"
        (should= 4
          (with-in-str "4"
            (get-spot "X")))))

    (context "#display-welcome-message"

      (it "prints a welcome message to the console"
        (should-be-a String (with-out-str (display-welcome-message)))))

    (context "#display-winner"

      (it "displays a message with the winner's marker"
        (should-contain "X" (with-out-str (display-winner "X"))))))

  (context "Board"

    (context "#place-marker"
      (it "places a marker on the board"
        (should= [0 1 2 3 "X" 5 6 7 8] (place-marker initial-board 4 "X"))))

    (context "#three-in-a-row"
      (it "returns true if the given three spaces have the expected marker"
        (should= true (three-in-a-row x-wins [0 4 8] "X"))
        (should= false (three-in-a-row x-wins [0 1 2] "X"))))

    (context "#is-winner"
      (it "returns true if a given player has won the game"
        (should= true (is-winner x-wins "X"))
        (should= true (is-winner o-wins "O"))))

    (context "#available-spots"
      (it "returns a collection of spots without X or O"
        (should= [2 3 6 7 8] (available-spots o-winning-move))))

    (context "#won"
      (it "returns true if any player has won the game"
        (should= true (won x-wins))
        (should= true (won o-wins))
        (should= false (won tie-game))))

    (context "#tie"
      (it "returns true if there is a tie"
        (should= true (tie tie-game)))

      (it "returns false if there is a winner"
        (let [game-with-winner ["O" "X" "O" "X" "X" "O" "O" "X" "X"]]
          (should= false (tie game-with-winner))))))

  (context "AI"
    (context "#score"
      (it "returns 10 if the current player has won the game"
        (should= 10 (score x-wins "X")))

      (it "returns -10 if the opponent has won the game"
        (should= -10 (score o-wins "X")))

      (it "returns 0 if there is no winner"
        (should= 0 (score tie-game "X"))))

    (context "#get-computer-move"
      (it "returns the winning move"
        (should= 7 (get-computer-move o-winning-move))))

    (context "#minimax"
      (it "returns a score if playing in the given spot will end the game"
        (let [x-will-win-state (game-state ["X" 1 2 3 "X" 5 6 7 8] "X" 2)]
          (should-not-be-nil (minimax 8 x-will-win-state)))))))
