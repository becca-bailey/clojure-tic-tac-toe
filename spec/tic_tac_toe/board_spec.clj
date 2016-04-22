(ns tic-tac-toe.board-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.board :refer :all]))

(def initial-board [0 1 2 3 4 5 6 7 8])
(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def o-wins [0 1 2 "O" "O" "O" 7 8 9])
(def o-winning-move ["X" "O" 2 3 "O" "X" 6 7 8])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])
(def first-move-x [0 1 2 3 "X" 5 6 7 8])

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

  (context "#won?"
    (it "returns true if any player has won the game"
      (should= true (won? x-wins))
      (should= true (won? o-wins))
      (should= false (won? tie-game))))

  (context "#spot-is-empty"
    (it "returns true if a spot is empty"
      (should= true (spot-is-empty first-move-x 0))
      (should= false (spot-is-empty first-move-x 4))))

  (context "#tie?"
    (it "returns true if there is a tie"
      (should= true (tie? tie-game)))

    (it "returns false if there is a winner"
      (let [game-with-winner ["O" "X" "O" "X" "X" "O" "O" "X" "X"]]
        (should= false (tie? game-with-winner))))))
