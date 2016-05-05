(ns tic-tac-toe.ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.ai :as ai]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.player :as player]))

(def player-1 (player/human "X"))
(def player-2 (player/computer "O"))

(def x-wins (board/make-board 3 {"X" #{0 4 8}}))
(def o-wins (board/make-board 3 {"O" #{3 4 5}}))
(def tie-game (board/make-board 3 {"X" #{1 3 4 6 8} "O" #{2 5 7}}))
(def initial-state
  (game/game-state board/initial-board))
(def x-will-win-state
  (game/game-state (board/make-board 3 {"X" #{0 4} "O" #{1 3}})))
(def x-will-win-state-4x4
  (game/game-state (board/make-board 4 {"X" #{0 5 10} "O" #{3 4}})))
(def o-will-win-state
  (game/game-state (board/make-board 3 {"X" #{4 6} "O" #{0 1}})))
(def will-tie-state
  (game/game-state (board/make-board 3 {"X" #{3 4 6 8} "O" #{0 2 5 7}})))
(def will-tie-state-4x4
  (game/game-state (board/make-board 4 {"X" #{2 3 4 5 9 12 14 15} "O" #{0 1 6 8 10 11 13}})))
(def available-winning-move
  (game/game-state (board/make-board 3 {"X" #{1 7} "O" #{3 5 8}})))
(def available-winning-move-4x4
  (game/game-state (board/make-board 4 {"X" #{0 5 6 12} "O" #{3 7 15}})))

(defn random-move [game-state]
  (let [random-spot
          (rand-nth (board/available-spots (:board game-state)))]
    (game/progress-game-state random-spot game-state)))

(defn ai-move [game-state]
  (let [ai-spot (ai/best-computer-move game-state)]
    (game/progress-game-state ai-spot game-state)))

(defn test-game [initial-game-state]
  (let [players (:players initial-game-state)]
    (let [[player-1 player-2] players]
      (loop [current-game-state initial-game-state]
        (if (game/game-over? current-game-state)
          (cond
            (game/tie? current-game-state)
            :tie
            (game/is-winner? current-game-state player-2)
            :computer-win
            (game/is-winner? current-game-state player-2)
            :computer-lose)
          (if (= player-2 (game/current-player current-game-state))
            (recur (ai-move current-game-state))
            (recur (random-move current-game-state))))))))

(defn random-first-move []
  (game/progress-game-state (rand-nth game/initial-board) initial-state))

(describe "AI"
  (context "#ai/best-computer-move"
    (it "returns the available spot when given a board with only one available move"
      (should= (first (board/available-spots (:board will-tie-state))) (ai/best-computer-move will-tie-state))
      (should= (first (board/available-spots (:board will-tie-state-4x4))) (ai/best-computer-move will-tie-state-4x4)))

    (it "chooses the best move when two moves are available"
      (let [two-moves-state-2 (game/game-state (board/make-board 3 {"X" #{2 3 6 8} "O" #{0 4 5}}))
            two-moves-state (game/game-state (board/make-board 3 {"X" #{0 2 3 7} "O" #{1 4 5}}))
            two-moves-state-4x4 (game/game-state (board/make-board 4 {"X" #{2 4 5 7 10 11 13 15} "O" #{0 1 6 8 9 12}}))]
        (should= 7 (ai/best-computer-move two-moves-state-2))
        (should= 6 (ai/best-computer-move two-moves-state))
        (should= 3 (ai/best-computer-move two-moves-state-4x4))))

    (it "chooses the best move when there are three moves available"
      (let [three-moves-state (game/game-state (board/make-board 3 {"X" #{1 3 4} "O" #{0 5 6}}))
            three-moves-state-4x4 (game/game-state (board/make-board 4 {"X" #{4 5 7 10 11 13 15} "O" #{0 1 6 8 9 12}}))]
         (should= 7 (ai/best-computer-move three-moves-state))
         (should= 3 (ai/best-computer-move three-moves-state-4x4))))

    (it "blocks the opponent from winning"
      (should= 8 (ai/best-computer-move x-will-win-state)))
      ; (should= 15 (ai/best-computer-move x-will-win-state-4x4))))

    (it "goes for the winning move when one is available"
      (let [available-winning-move-2 (game/game-state (board/make-board 3 {"X" #{0 1} "O" #{3 8}}))]
         (should= 2 (ai/best-computer-move available-winning-move-2)))))
         ; (should= 11 (ai/best-computer-move available-winning-move-4x4)))))

 ;   (it "always wins or ties against a random player")
 ;     (dotimes [_ 100]
 ;       (should-not= :computer-lose (test-game random-first-move))))

 ;    (it "chooses a spot spot when given an empty board"
 ;      (should= 8 (ai/best-computer-move initial-state))

    ; ^ this test passes, but takes a long time.

  (context "#minimax"
    (context "when depth is 0"
      (it "returns a score if playing in the given spot will end the game"
        (should-not-be-nil (ai/minimax 8 x-will-win-state x-will-win-state 0))
        (should-not-be-nil (ai/minimax 1 will-tie-state will-tie-state 0)))

      (it "returns the score of -10 if playing in a given spot will result in an opponent win"
        (should= -10 (ai/minimax 8 player-2 x-will-win-state 0)))

      (it "returns the score of 0 if playing in a given spot will result in a tie"
        (should= 0 (ai/minimax 1 player-1 will-tie-state 0))))))
