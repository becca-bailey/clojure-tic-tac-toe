(ns tic-tac-toe.core-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer :all]
            [tic-tac-toe.ui :refer :all]))

(def first-move-x [0 1 2 3 "X" 5 6 7 8])
(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def o-wins [0 1 2 "O" "O" "O" 7 8 9])
(def o-winning-move ["X" "O" 2 3 "O" "X" 6 7 8])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])

(describe "tic tac toe"
  (describe "game"
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
          (should= false (game-over first-move-state)))))))
