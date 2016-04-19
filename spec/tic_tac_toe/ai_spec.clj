(ns tic-tac-toe.ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :refer [game-state game-over progress-game-state switch-player]]
            [tic-tac-toe.board :refer [place-marker available-spots]]
            [tic-tac-toe.ai :refer :all]))

(def x-wins ["X" 1 2 3 "X" 5 6 7 "X"])
(def o-wins [0 1 2 "O" "O" "O" 7 8 9])
(def tie-game ["O" "X" "O" "X" "X" "O" "X" "O" "X"])
(def o-winning-move ["X" "O" 2 3 "O" "X" 6 7 8])
(def initial-state (game-state [0 1 2 3 4 5 6 7 8] "X" 0))
(def x-will-win-state (game-state ["X" 1 2 3 "X" 5 6 7 8] "X" 2))
(def will-tie-state (game-state ["O" 1 "O" "X" "X" "O" "X" "O" "X"] "X" 8))

(defn random-move [game-state]
  (let [random-spot
          (rand-nth (available-spots (:board game-state)))]
    (possible-game-state random-spot game-state)))

(defn ai-move [game-state]
  (let [ai-spot (get-computer-move (:board game-state))]
    (possible-game-state ai-spot game-state)))

(defn test-games
  [initial-game-state {:random true}]
  (loop [current-game-state initial-game-state
         states []]
    (if (game-over current-game-state)
      states
      (recur (random-move current-game-state) (conj states current-game-state))))
  [initial-game-state {:random false}]
  (loop [current-game-state initial-game-state
         states []]
    (if (game-over current-game-state)
      states
      (recur (ai-move current-game-state) (conj states current-game-state)))))

(context "AI"
  (context "#score"
    (it "returns 10 if the current player has won the game"
      (should= 10 (score x-wins "X")))

    (it "returns -10 if the opponent has won the game"
      (should= -10 (score o-wins "X")))

    (it "returns 0 if there is no winner"
      (should= 0 (score tie-game "X"))))

  (context "#possible-game-state"
    (it "returns a game state with a switched player"
      (should= (switch-player (:current-player initial-state))
               (:current-player (possible-game-state 0 initial-state))))

    (it "returns a state with an increased turn counter"
      (should= (inc (:turn-counter initial-state))
               (:turn-counter (possible-game-state 0 initial-state))))

    (it "returns a state with an updated board"
      (should= (place-marker (:board initial-state) 0 "X")
               (:board (possible-game-state 0 initial-state)))))

  (context "#get-computer-move"
    (it "returns the winning move"
      (should= 7 (get-computer-move o-winning-move))))

  (context "#minimax"
    (it "returns a score if playing in the given spot will end the game"
        (should-not-be-nil (minimax 8 x-will-win-state)))))
