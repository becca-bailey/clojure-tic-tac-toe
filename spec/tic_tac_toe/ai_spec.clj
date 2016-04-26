(ns tic-tac-toe.ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.ai :refer :all]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]))

(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def o-wins [0 1 2 "O" "O" "O" 7 8 9])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])
(def o-winning-move ["X" "O" 2 3 "O" "X" 6 7 8])
(def initial-state (game/game-state [0 1 2 3 4 5 6 7 8] "X" 0))
(def x-will-win-state (game/game-state ["X" "O" 2 3 "X" 5 6 7 8] "O" 3))
(def o-will-win-state (game/game-state ["O" "O" 2 3 "X" 5 "X" 7 8] "O" 4))
(def either-will-win-state (game/game-state ["O" "O" 2 3 "X" 5 "X" 7 8] "X" 4))
(def will-tie-state (game/game-state ["O" 1 "O" "X" "X" "O" "X" "O" "X"] "X" 8))
(def available-winning-move (game/game-state [0 "O" 2 "X" 4 "X" 6 "O" "X"] "O" 5))

(defn random-move [game-state]
  (let [random-spot
          (rand-nth (board/available-spots (:board game-state)))]
    (game/progress-game-state random-spot game-state)))

(defn ai-move [game-state]
  (let [ai-spot (best-computer-move game-state)]
    (game/progress-game-state ai-spot game-state)))

(defn test-game [initial-game-state]
  (loop [current-game-state initial-game-state]
    (if (game/game-over? current-game-state)
      (cond
        (board/tie? (:board current-game-state))
        :tie
        (board/is-winner? (:board current-game-state) "O")
        :computer-win
        (board/is-winner? (:board current-game-state) "X")
        :computer-lose)
      (if (= "O" (:current-player current-game-state))
        (recur (ai-move current-game-state))
        (recur (random-move current-game-state))))))

(defn random-first-move []
  (game/progress-game-state (rand-nth game/initial-board) initial-state))

(describe "AI"
  (context "#best-computer-move"
    (it "returns the available spot when given a board with only one available move"
      (should= (first (board/available-spots (:board will-tie-state))) (best-computer-move will-tie-state)))

    (it "chooses the best move when two moves are available"
      (let [two-moves-state-2 (game/game-state ["O" 1 "X" "X" "O" "O" "X" 7 "X"] "O" 7)
            two-moves-state (game/game-state ["X" "O" "X" "X" "O" "O" 6 "X" 8] "O" 7)]
        (should= 7 (best-computer-move two-moves-state-2))
        (should= 6 (best-computer-move two-moves-state))))

    (it "chooses the best move when there are three moves available"
      (let [three-moves-state (game/game-state ["O" "X" 2 "X" "X" "O" "O" 7 8] "O" 6)]
         (should= 7 (best-computer-move three-moves-state))))

    (it "blocks the opponent from winning"
      (let [x-will-win-state (game/game-state ["0" 1 2 "X" "X" 5 6 "O" "X"] "O" 5)]
         (should= 5 (best-computer-move x-will-win-state))))

    (it "goes for the winning move when one is available"
      (let [available-winning-move-2 (game/game-state ["O" "O" 2 "X" 4 5 6 7 "X"] "O" 4)]
         (should= 2 (best-computer-move available-winning-move-2))))

    (it "always wins or ties against a random player"
      (dotimes [_ 100]
        (should-not= :computer-lose (test-game random-first-move)))))

    ; (it "chooses a spot spot when given an empty board"
    ;   (should= 8 (best-computer-move initial-state))))

    ; ^ this test passes, but takes a long time.

  (context "#minimax"
    (context "when depth is 0"
      (it "returns a score if playing in the given spot will end the game"
        (should-not-be-nil (minimax 8 x-will-win-state x-will-win-state 0))
        (should-not-be-nil (minimax 1 will-tie-state will-tie-state 0)))

      (it "returns the score of 10 if playing in a given spot will block the opponent from winning"
        (should= 10 (minimax 8 x-will-win-state x-will-win-state 0)))

      (it "returns the score of 0 if playing in a given spot will result in a tie"
        (should= 0 (minimax 1 will-tie-state will-tie-state 0)))

      (it "returns a score of 10 if playing in a given spot will help result in a win"
        (should= 10 (minimax 4 available-winning-move available-winning-move 0))))))
