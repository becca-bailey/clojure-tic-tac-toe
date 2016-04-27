(ns tic-tac-toe.player-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.player :as player]))

(def human-player (player/human "X"))
(def computer-player (player/computer "O"))

(describe "player"
  (it "gets initialized with a marker"
    (should= "X" (:marker human-player))
    (should= "O" (:marker computer-player)))

  (it "has a player-type"
    (should= :human (:player-type human-player))
    (should= :computer (:player-type computer-player))))
