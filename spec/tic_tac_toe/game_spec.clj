(ns tic-tac-toe.game-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.player :as player]))

(def default-players
  [(player/human "X") (player/computer "O")])

(def first-move-x (board/make-board {"X" #{4}}))
(def x-wins (board/make-board {"X" #{0 4 8}}))
(def tie-game (board/make-board {"X" #{1 3 4 6 8} "O" #{0 2 5 7}}))

(describe "Game"
  (context "#switch-player"
    (it "returns the opposite marker"
      (let [[player1 player2] (:players game/initial-state)]
        (should= player2 (game/switch-player player1 game/initial-state))
        (should= player1 (game/switch-player player2 game/initial-state)))))

  (context "game-state"
    (it "has a board"
      (should= game/initial-board (:board game/initial-state)))

    (it "has a first player"
      (should= (player/human "X") (:first-player game/initial-state)))

    (it "has a turn counter"
      (should= 0 (:turn-counter game/initial-state)))

    (context "#set-turn-counter"
      (it "returns 0 given an empty board"
        (should= 0 (game/set-turn-counter game/initial-board))))

    (context "#update-board"
      (it "creates a copy of the current game state with updated board and turn counter"
        (let [updated-state (game/update-board game/initial-state first-move-x)]
          (should= first-move-x (:board updated-state))
          (should=
           (inc (:turn-counter game/initial-state)) (:turn-counter updated-state)))))

    (context "#game-over"
      (it "returns true if a player has won or if game is tied"
        (let [first-move-state (game/game-state first-move-x default-players)
              x-win-state (game/game-state x-wins default-players)]
          (should= true (game/game-over? tie-game))
          (should= true (game/game-over? x-win-state))
          (should= false (game/game-over? first-move-state)))))

    (context "#progress-game-state"
      (it "returns a state with an increased turn counter"
        (should= (inc (:turn-counter game/initial-state))
                 (:turn-counter (game/progress-game-state 0 game/initial-state))))

      (it "returns a state with an updated board"
        (should= (board/place-marker (:board game/initial-state) 0 "X")
                 (:board (game/progress-game-state 0 game/initial-state)))))

    (context "#current-player"
      (it "returns the current player based on the first player and the game state"
        (should= (player/human "X") (game/current-player game/initial-state))))))
