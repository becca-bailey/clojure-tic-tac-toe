(ns tic-tac-toe.game-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer :all]
            [tic-tac-toe.board :as board]))

(def first-move-x [0 1 2 3 "X" 5 6 7 8])
(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])
(def x-win-state (game-state x-wins "X" 3))
(def tie-state (game-state tie-game "X" 9))
(def initial-state (game-state [0 1 2 3 4 5 6 7 8] "X" 0))


(describe "Game"
  (context "#switch-player"
    (it "returns the opposite marker"
      (should= "O" (switch-player "X"))
      (should= "X" (switch-player "O"))))

  (context "game-state"
    (it "has a board"
      (should= initial-board (:board initial-state)))

    (it "has a first player"
      (should= (or "X" "O") (:first-player initial-state)))

    (it "has a turn counter"
      (should= 0 (:turn-counter initial-state)))

    (context "#update-board"
      (it "creates a copy of the current game state with updated board and turn counter"
        (let [updated-state (update-board initial-state first-move-x)]
          (should= first-move-x (:board updated-state))
          (should=
           (inc (:turn-counter initial-state)) (:turn-counter updated-state))))))

  (context "#game-over"
    (it "returns true if a player has won or if game is tied"
      (let [first-move-state (game-state first-move-x "O" 1)]
        (should= true (game-over? tie-game))
        (should= true (game-over? x-win-state))
        (should= false (game-over? first-move-state)))))

  (context "#winner"
    (it "returns nil if the game has not been completed"
      (should-be-nil (winner initial-state)))

    (it "returns the winner's marker"
      (should= "X" (winner x-win-state)))

    (it "returns nil if the game is a tie"
      (should-be-nil (winner tie-state)))

    (context "#progress-game-state"

      (it "returns a state with an increased turn counter"
        (should= (inc (:turn-counter initial-state))
                 (:turn-counter (progress-game-state 0 initial-state))))

      (it "returns a state with an updated board"
        (should= (board/place-marker (:board initial-state) 0 "X")
                 (:board (progress-game-state 0 initial-state)))))

    (context "#current-player"
      (it "returns the current player based on the first player and the game state"
        (should= "X" (current-player initial-state))))))
