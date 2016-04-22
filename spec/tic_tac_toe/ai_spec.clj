(ns tic-tac-toe.ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer :all]
            [tic-tac-toe.board :refer :all]
            [tic-tac-toe.ai :refer :all]))

(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def o-wins [0 1 2 "O" "O" "O" 7 8 9])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])
(def o-winning-move ["X" "O" 2 3 "O" "X" 6 7 8])
(def initial-state (game-state [0 1 2 3 4 5 6 7 8] "X" 0))
(def x-will-win-state (game-state ["X" 1 2 3 "X" 5 6 7 8] "X" 2))
(def o-will-win-state (game-state ["O" "O" 2 3 "X" 5 "X" 7 8] "O" 4))
(def either-will-win-state (game-state ["O" "O" 2 3 "X" 5 "X" 7 8] "X" 4))
(def will-tie-state (game-state ["O" 1 "O" "X" "X" "O" "X" "O" "X"] "X" 8))

(defn random-move [game-state]
  (let [random-spot
          (rand-nth (available-spots (:board game-state)))]
    (recreate-game-state random-spot game-state)))

(defn ai-move [game-state]
  (let [ai-spot (best-computer-move game-state)]
    (recreate-game-state ai-spot game-state)))

(defn test-game [initial-game-state]
  (loop [current-game-state initial-game-state]
    (if (game-over? current-game-state)
      (if (tie? (:board current-game-state))
        :tie
        (if (is-winner (:board current-game-state) "O")
          :computer-win
          :computer-lose))
      (if (= "O" (:current-player current-game-state))
        (recur (ai-move current-game-state))
        (recur (random-move current-game-state))))))

(defn random-first-move []
  (recreate-game-state (rand-nth initial-board) initial-game-state))

(context "AI"

  (context "#recreate-game-state"
    (it "returns a game state with a switched player"
      (should= (switch-player (:current-player initial-state))
               (:current-player (recreate-game-state 0 initial-state))))

    (it "returns a state with an increased turn counter"
      (should= (inc (:turn-counter initial-state))
               (:turn-counter (recreate-game-state 0 initial-state))))

    (it "returns a state with an updated board"
      (should= (place-marker (:board initial-state) 0 "X")
               (:board (recreate-game-state 0 initial-state)))))

  (context "#best-computer-move"
    (it "returns the available spot when given a board with only one available move"
      (should= (first (available-spots (:board will-tie-state))) (best-computer-move will-tie-state)))

    (it "returns the first available move"
      (let [two-moves-state
            (game-state ["X" "O" "X" "X" "O" "O" 6 "X" 8] "O" 7)]
        (should= 6 (best-computer-move two-moves-state))))

    (it "chooses the best move when two moves are available"
      (let [two-moves-state-2 (game-state ["O" 1 "X" "X" "O" "O" "X" 7 "X"] "O" 7)]
        (should= 7 (best-computer-move two-moves-state-2)))))

    ; (it "always wins or ties against a human player"
    ;   (let [three-moves-state (game-state ["O" "X" 2 "X" "X" "O" "O" 7 8] "O" 6)]
    ;      (should-not= :computer-lose (test-game three-moves-state)))))

      ; (dotimes [_ 100]
      ;   (should-not= :computer-lose (test-game random-first-move)))))

  (context "#minimax"
    (it "returns a score if playing in the given spot will end the game"
      (should-not-be-nil (minimax 8 x-will-win-state))
      (should-not-be-nil (minimax 1 will-tie-state)))

    (it "returns the score of -10 if playing in a given spot will block the opponent from winning"
      (should= -10 (minimax 8 x-will-win-state)))

    (it "returns the score of 0 if playing in a given spot will result in a tie"
      (should= 0 (minimax 1 will-tie-state)))

    (it "returns the score of -10 if playing in a given spot will result in a win for opponent"
      (should= -10 (minimax 2 o-will-win-state)))))
