(ns tic-tac-toe.ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.ai :as ai]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.player :as player]))

(def default-players
  [(player/human "X") (player/computer "O")])

(def x-wins (board/make-board {"X" #{0 4 8}}))
(def o-wins (board/make-board {"O" #{3 4 5}}))
(def tie-game (board/make-board {"X" #{1 3 4 6 8 "O" #{2 5 7}}}))
(def initial-state
  (game/game-state board/initial-board default-players))
(def x-will-win-state
  (game/game-state (board/make-board {"X" #{0 4} "O" #{1 3}}) default-players))
(def o-will-win-state
  (game/game-state (board/make-board {"X" #{4 6} "O" #{0 1}}) default-players))
(def either-will-win-state
  (game/game-state (board/make-board {"X" #{4 6} "O" #{0 1}}) default-players))
(def will-tie-state
  (game/game-state (board/make-board {"X" #{3 4 6 8}"O" #{0 2 5 7}}) default-players))
(def available-winning-move
  (game/game-state (board/make-board {"X" #{3 5 8} "O" #{1 7}}) default-players))

(defn random-move [game-state]
  (let [random-spot
          (rand-nth (board/available-spots (:board game-state)))]
    (game/progress-game-state random-spot game-state)))

(defn ai-move [game-state]
  (let [ai-spot (ai/best-computer-move game-state)]
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
  (context "#ai/best-computer-move"
    (it "returns the available spot when given a board with only one available move"
      (should= (first (board/available-spots (:board will-tie-state))) (ai/best-computer-move will-tie-state)))

    (it "chooses the best move when two moves are available"
      (let [two-moves-state-2 (game/game-state ["O" 1 "X" "X" "O" "O" "X" 7 "X"] default-players)
            two-moves-state (game/game-state ["X" "O" "X" "X" "O" "O" 6 "X" 8] default-players)]
        (should= 7 (ai/best-computer-move two-moves-state-2))
        (should= 6 (ai/best-computer-move two-moves-state))))

    (it "chooses the best move when there are three moves available"
      (let [three-moves-state (game/game-state ["O" "X" 2 "X" "X" "O" "O" 7 8] default-players)]
         (should= 7 (ai/best-computer-move three-moves-state))))

    (it "blocks the opponent from winning"
      (should= 8 (ai/best-computer-move x-will-win-state)))

    (it "goes for the winning move when one is available"
      (let [available-winning-move-2 (game/game-state ["O" "O" 2 "X" 4 5 6 7 "X"] default-players)]
         (should= 2 (ai/best-computer-move available-winning-move-2))))

    (it "always wins or ties against a random player"
      (dotimes [_ 100]
        (should-not= :computer-lose (test-game random-first-move)))))

    ; (it "chooses a spot spot when given an empty board"
    ;   (should= 8 (ai/best-computer-move initial-state))))

    ; ^ this test passes, but takes a long time.

  (context "#minimax"
    (context "when depth is 0"
      (it "returns a score if playing in the given spot will end the game"
        (should-not-be-nil (ai/minimax 8 x-will-win-state x-will-win-state 0))
        (should-not-be-nil (ai/minimax 1 will-tie-state will-tie-state 0)))

      (it "returns the score of 10 if playing in a given spot will block the opponent from winning"
        (should= 10 (ai/minimax 8 x-will-win-state x-will-win-state 0)))

      (it "returns the score of 0 if playing in a given spot will result in a tie"
        (should= 0 (ai/minimax 1 will-tie-state will-tie-state 0)))

      (it "returns a score of 10 if playing in a given spot will help result in a win"
        (should= 10 (ai/minimax 4 available-winning-move available-winning-move 0))))))
