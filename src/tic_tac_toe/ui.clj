(ns tic-tac-toe.ui
  (:require [tic-tac-toe.player :as player]
            [tic-tac-toe.board :as board]
            [io.aviso.ansi :as ansi]
            [clojure.string :as string]))

(defn clear-screen []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))

(defn is-not-a-number? [input]
  (not (some (set (map str (range 16))) (vector input))))

(defn color-player-markers [char]
  (if (re-find #"\A[a-zA-Z!@#$%^&*<>?~]" char)
    (ansi/bold-cyan char)
    char))

(defn add-color [board-string]
  (let [chars (string/split board-string #"|")]
    (string/join (map color-player-markers chars))))

(defn display-board [board]
  (clear-screen)
  (println (str "\n" (add-color (board/generate-board-string board)) "\n")))

(defn display-welcome-message []
  (println "Welcome to Tic Tac Toe in Clojure!\n"))

(defn is-an-available-spot? [available-spots input]
  (some (set (map str available-spots)) (vector input)))

(defn print-error [message]
  (println (str "Sorry, " message ". Try again!")))

(defn get-user-input
  ([condition error-message]
    (loop [user-input (read-line)]
      (if (condition user-input)
        user-input
        (do (print-error error-message)
          (recur (read-line))))))
  ([condition error-message possible-input]
    (loop [user-input (read-line)]
      (if (condition possible-input user-input)
        user-input
        (do (print-error error-message)
          (recur (read-line)))))))

(defn get-spot [available-spots]
  (println (str "Where would you like to play? Available spots: " (clojure.string/join " " available-spots)))
  (Integer. (get-user-input is-an-available-spot? "that's not an available spot" available-spots)))

(defn is-a-single-character? [input]
  (= (count input) 1))

(defn is-a-valid-marker? [input]
  (cond
    (not (is-a-single-character? input))
    (println "Marker must be a single character.")
    (not (is-not-a-number? input))
    (println "Marker cannot be a number."))
  (and (is-a-single-character? input) (is-not-a-number? input)))

(defn is-a-valid-computer-marker? [human-player input]
  (if (= (:marker human-player) input)
    (println "Markers cannot be the same."))
  (and (is-a-valid-marker? input) (not (= (:marker human-player) input))))

(defmulti get-marker-choice :player-type)

(defmethod get-marker-choice :human [& players]
  (println "What would you like your marker to be?")
  (get-user-input is-a-valid-marker? "that's not a valid marker"))

(defmethod get-marker-choice :computer [& players]
  (println "Choose a character for the computer's marker.")
  (get-user-input is-a-valid-computer-marker? "that's not a valid marker" (last players)))

(defn display-winner [player]
  (if (= (:player-type player) :computer)
    (println "Computer wins!!")
    (println "You win!!")))

(defn display-tie []
  (println "It's a tie!"))

(defn confirm-move [move player]
  (if (= (:player-type player) :computer)
    (println (str "Computer played at spot " move "\n"))
    (println (str "You played at spot " move "\n"))))

(defn y-or-n [input]
  (some #{"y" "n"} (list input)))

(defn player-would-like-to-continue? []
  (println "Do you want to play again? y/n")
  (= "y" (get-user-input y-or-n "please choose y or n")))

(defn goodbye []
  (println "Goodbye!"))
