(ns tic-tac-toe.core-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.core :refer :all]
            [tic-tac-toe.ui :refer :all]
            [tic-tac-toe.board :refer :all]
            [tic-tac-toe.ai :refer :all]))

(def new-board " 0 | 1 | 2 \n---------\n 3 | 4 | 5 \n---------\n 6 | 7 | 8 ")
(def initial-board [0 1 2 3 4 5 6 7 8])
(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def o-wins [0 1 2 "O" "O" "O" 7 8 9])
(def o-winning-move ["X" "O" 2 3 "O" "X" 6 7 8])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])

(describe "tic tac toe"
  (describe "core"
    (context "#switch-player"
      (it "returns the opposite marker"
        (should= "O" (switch-player "X"))
        (should= "X" (switch-player "O")))))
        
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
        (should= true (is-winner o-wins "O")))))

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
        (should= 7 (get-computer-move o-winning-move))))))
