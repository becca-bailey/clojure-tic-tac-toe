(ns tic-tac-toe.core-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.core :refer :all]
            [tic-tac-toe.ui :refer :all]))

(def new-board " 0 | 1 | 2 \n---------\n 3 | 4 | 5 \n---------\n 6 | 7 | 8 ")
(def initial-board [0 1 2 3 4 5 6 7 8])

(describe "ui"
  (it "prints an empty board"
    (should= new-board (display-board initial-board))))
