(ns tic-tac-toe.core-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.core :refer :all]
            [tic-tac-toe.ui :refer :all]))

(def new-board " 0 | 1 | 2 \n---------\n 3 | 4 | 5 \n---------\n 6 | 7 | 8 ")
(def initial-board [0 1 2 3 4 5 6 7 8])
(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])

(describe "tic tac toe"
  (context "ui"
    (it "prints an empty board"
      (should= new-board (display-board initial-board))))

  (it "places a marker on the board"
    (should= [0 1 2 3 "X" 5 6 7 8] (place-marker initial-board 4 "X")))

  (context "#three-in-a-row"
    (it "returns true if the given three spaces have the expected marker"
      (should= true (three-in-a-row x-wins [0 4 8] "X"))
      (should= false (three-in-a-row x-wins [0 1 2] "X"))))

  (context "#is-winner"
    (it "returns true if a given player has won the game"
      (should= true (is-winner x-wins "X")))))
