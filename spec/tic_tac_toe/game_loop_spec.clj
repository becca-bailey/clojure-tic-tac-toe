(ns tic-tac-toe.game-loop-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-loop :as game-loop]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.ui :as ui]
            [tic-tac-toe.ai :as ai]))

; (def initial-state
;   (game/game-state board/initial-board "X" 0))

; (describe "Game loop"
;   (context "#move"
;     (around [it]
;       (with-out-str (it)))
;
;     (it "should call #get-spot if it is called on a game state with a current human player"
;       (should-invoke ui/get-spot (with-in-str "4" (game-loop/move initial-state))))))

    ; (it "should call #best-computer-move if it is called on a game state with a current computer player"
    ;   (should-invoke ai/best-computer-move (game-loop/move initial-state)))))
