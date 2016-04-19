(ns tic-tac-toe.ui-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.ui :refer :all]))

(def new-board " 0 | 1 | 2 \n---------\n 3 | 4 | 5 \n---------\n 6 | 7 | 8 ")
(def initial-board [0 1 2 3 4 5 6 7 8])

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
