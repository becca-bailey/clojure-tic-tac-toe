(ns tic-tac-toe.ui-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.ui :refer :all]))

(def new-board "\n 0 | 1 | 2 \n-----------\n 3 | 4 | 5 \n-----------\n 6 | 7 | 8 \n\n")
(def initial-board [0 1 2 3 4 5 6 7 8])

(describe "UI"
  (context "#display-board"
    (it "prints an empty board"
      (should= new-board (with-out-str (display-board initial-board)))))

  (context "#get-spot"
    (around [it]
      (with-out-str (it)))

    (it "returns user input as an integer"
      (should= 4
        (with-in-str "4"
          (get-spot "X" [0 1 2 3 4 5 6 7 8]))))

    (it "prints an error message if invalid input is given."
      (should-contain "Sorry, that's not valid input. Try again!"
        (with-in-str "i\n1"
          (with-out-str (get-spot "X" [0 1 2 3 4 5 6 7 8])))))

    (it "prints an error message if a spot is not available on the board"
      (should-contain "Sorry, that spot is already taken. Try again!"
        (with-in-str "0\n1"
          (with-out-str (get-spot "X" ["O" 1 "X" 3 4 5 6 7 8]))))))

  (context "#display-welcome-message"
    (it "prints a welcome message to the console"
      (should-be-a String (with-out-str (display-welcome-message)))))

  (context "#display-winner"
    (it "displays a message with the winner's marker"
      (should-contain "X" (with-out-str (display-winner "X"))))))
