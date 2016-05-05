(ns tic-tac-toe.board-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.player :as player]))

(def grid-three-string " 0 | 1 | 2 \n-----------\n 3 | 4 | 5 \n-----------\n 6 | 7 | 8 ")
(def grid-four-string " 0 | 1 | 2 | 3 \n---------------\n 4 | 5 | 6 | 7 \n---------------\n 8 | 9 | 10 | 11 \n---------------\n 12 | 13 | 14 | 15 ")

(def x-wins (board/make-board 3 {"X" #{0 4 8}}))
(def o-wins (board/make-board 3 {"O" #{3 4 5}}))
(def tie-game (board/make-board 3 {"X" #{1 3 4 6 8} "O" #{0 2 5 7}}))
(def first-move-x (board/make-board 3 {"X" #{4}}))

(def winning-combinations-3x3 [[0 1 2] [3 4 5] [6 7 8]
                               [0 3 6] [1 4 7] [2 5 8]
                               [0 4 8] [2 4 6]])

(def winning-combinations-4x4 [[0 1 2 3] [4 5 6 7] [8 9 10 11] [12 13 14 15]
                               [0 4 8 12] [1 5 9 13] [2 6 10 14] [3 7 11 15]
                               [0 5 10 15] [3 6 9 12]])

(describe "Board"
  (context "initial-board"
    (it "generates an empty board"
      (should= [0 1 2 3 4 5 6 7 8] (board/initial-board 3))
      (should= [0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15] (board/initial-board 4))))

  (context "#make-board"
    (it "returns a board with default markers given a map of spots and players"
      (should= first-move-x (board/make-board 3 {"X" #{4}}))
      (should= ["O" 1 2 3 "X" 5 6 7 8] (board/make-board 3 {"X" #{4} "O" #{0}})))

    (it "returns a board with custom markers given a map of spots and players"
      (should= [0 1 2 3 "A" 5 6 7 8] (board/make-board 3 {"A" #{4}}))
      (should= ["B" 1 2 3 "A" 5 6 7 8] (board/make-board 3 {"A" #{4} "B" #{0}}))))

  (context "#place-marker"
    (it "places a marker on the board"
      (should= first-move-x (board/place-marker (board/initial-board 3) 4 "X"))))

  (context "#is-a-winning-combination?"
    (it "returns true if the given three spaces have the expected marker"
      (should= true (board/is-a-winning-combination? x-wins [0 4 8] "X"))
      (should= false (board/is-a-winning-combination? x-wins [0 1 2] "X"))))

  (context "#available-spots"
    (it "returns a collection of spots without X or O"
      (should= [0 1 2 3 5 6 7 8] (board/available-spots first-move-x))))

  (context "#spot-is-empty"
    (it "returns true if a spot is empty"
      (should= true (board/spot-is-empty first-move-x 0))
      (should= false (board/spot-is-empty first-move-x 4))))

  (context "#grid-size"
    (it "returs grid size for a square board"
      (should= 3 (board/grid-size (board/initial-board 3)))
      (should= 4 (board/grid-size (board/initial-board 4)))
      (should= 3 (board/grid-size tie-game))))

  (context "#row-start"
    (it "returns a collection of spots in the beginning of a row based on the grid size"
      (should= [0 3 6] (board/row-start 3))
      (should= [0 4 8 12] (board/row-start 4))
      (should= [0 5 10 15 20] (board/row-start 5))))

  (context "#col-start"
    (it "returns a collecion of spots in the beginning of a column based on the grid size"
      (should= [0 1 2] (board/col-start 3))
      (should= [0 1 2 3] (board/col-start 4))
      (should= [0 1 2 3 4] (board/col-start 5))))

  (context "#diagonal-start"
    (it "returns a collection of spots in the beginning of a diagonal row based on the grid size"
      (should= [0 2] (board/diagonal-start 3))
      (should= [0 3] (board/diagonal-start 4))
      (should= [0 4] (board/diagonal-start 5))))

  (context "#rows"
    (it "returns a collection of rows based on the grid size"
      (should= [[0 1 2] [3 4 5] [6 7 8]]
               (board/rows 3))
      (should= [[0 1 2 3] [4 5 6 7] [8 9 10 11] [12 13 14 15]]
               (board/rows 4))))

  (context "#cols"
    (it "returns a collection of columns based on the grid size"
      (should= [[0 3 6] [1 4 7] [2 5 8]]
               (board/cols 3))
      (should= [[0 4 8 12] [1 5 9 13] [2 6 10 14] [3 7 11 15]]
               (board/cols 4))))

  (context "#diagonals"
    (it "returns a collection of diagonals based on the grid size"
      (should= [[0 4 8] [2 4 6]]
               (board/diagonals 3))
      (should= [[0 5 10 15] [3 6 9 12]]
               (board/diagonals 4))))

  (context "#winning-combinations"
    (it "returns a collection of winning combinations based on the grid size"
      (should= winning-combinations-3x3 (board/winning-combinations 3))
      (should= winning-combinations-4x4 (board/winning-combinations 4))))

  (context "#generate-board-string"
    (it "returns a string for the UI to display"
      (should= grid-three-string (board/generate-board-string (board/initial-board 3)))
      (should= grid-four-string (board/generate-board-string (board/initial-board 4))))))
