(ns tic-tac-toe.game-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.player :as player]))

(def player-1 (player/human "X"))
(def player-2 (player/computer "O"))

(def first-move-x (board/make-board 3 {"X" #{4}}))
(def x-wins (board/make-board 3 {"X" #{0 4 8}}))
(def tie-board (board/make-board 3 {"X" #{1 3 4 6 8} "O" #{0 2 5 7}}))
(def first-move-state (game/game-state first-move-x))
(def x-win-state (game/game-state x-wins))
(def tie-state (game/game-state tie-board))

(describe "Game"
  (context "#switch-player"
    (it "returns the opposite marker"
      (let [[player1 player2] (:players game/initial-state)]
        (should= player2 (game/switch-player player1 game/initial-state))
        (should= player1 (game/switch-player player2 game/initial-state)))))

  (context "game-state"
    (it "has a board"
      (should= game/initial-board (:board game/initial-state)))

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
        (should (game/game-over? tie-state))
        (should (game/game-over? x-win-state))
        (should-not (game/game-over? first-move-state))))

    (context "#progress-game-state"
      (it "returns a state with an increased turn counter"
        (should= (inc (:turn-counter game/initial-state))
                 (:turn-counter (game/progress-game-state 0 game/initial-state))))

      (it "returns a state with an updated board"
        (should= (board/place-marker (:board game/initial-state) 0 "X")
                 (:board (game/progress-game-state 0 game/initial-state)))))

    (context "#current-player"
      (it "returns the current player based on the first player and the game state"
        (should= (player/human "X") (game/current-player game/initial-state))))

    (context "#winner"
      (it "returns winning player"
        (should= player-1 (game/winner x-win-state)))

      (it "returns false if the game is a tie"
        (should-not (game/winner tie-state))))

    (context "#is-winner?"
      (it "returns true if a given player has won the game"
        (should (game/is-winner? x-win-state player-1))
        (should-not (game/is-winner? x-win-state player-2))
        (should-not (game/is-winner? tie-state player-1))
        (should-not (game/is-winner? tie-state player-2))))

    (context "#won?"
      (it "returns true if any player has won the game"
        (should (game/won? x-win-state))
        (should-not (game/won? tie-state))))

    (context "#tie?"
      (it "returns true if there is a tie"
        (should (game/tie? tie-state)))

      (it "returns false if there is a winner"
        (should-not (game/tie? x-win-state))))))
