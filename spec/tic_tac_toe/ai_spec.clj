(ns tic-tac-toe.ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer [game-state]]
            [tic-tac-toe.ai :refer :all]))

(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def o-wins [0 1 2 "O" "O" "O" 7 8 9])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])
(def o-winning-move ["X" "O" 2 3 "O" "X" 6 7 8])

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
      (let [x-will-win-state (game-state ["X" 1 2 3 "X" 5 6 7 8] "X" 2)
            will-tie-state (game-state ["O" 1 "O" "X" "X" "O" "X" "O" "X"] "X" 8)]
        (should-not-be-nil (minimax 8 x-will-win-state))))))
