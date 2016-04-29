(ns tic-tac-toe.ui
  (:require [tic-tac-toe.player :as player]))

(defn display-board [board]
  (println (str "\n " (first board) " | " (nth board 1) " | " (nth board 2) " \n"
                  "-----------\n"
                  " " (nth board 3) " | " (nth board 4) " | " (nth board 5) " \n"
                  "-----------\n"
                  " " (nth board 6) " | " (nth board 7) " | " (nth board 8) " \n")))

(defn display-welcome-message []
  (println "Welcome to Tic Tac Toe in Clojure!"))

(defn is-an-available-spot? [available-spots input]
  (some (set (map str available-spots)) (vector input)))

(defn print-error [message]
  (println (str "Sorry, " message ". Try again!")))

(defn is-a-number? [input]
  (some #{"0" "1" "2" "3" "4" "5" "6" "7" "8" "9"} (list input)))

(defn get-user-input
  ([condition error-message]
   (loop [user-input (read-line)]
    (if (condition user-input)
      user-input
      (do (print-error error-message)
        (recur (read-line))))))
  ([condition error-message available-spots]
   (loop [user-input (read-line)]
    (if (condition available-spots user-input)
     user-input
      (do (print-error error-message)
        (recur (read-line)))))))

(defn get-spot [user-marker available-spots]
  (println (str user-marker ": Where would you like to play? Available spots: " (clojure.string/join " " available-spots)))
  (Integer. (get-user-input is-an-available-spot? "that's not an available spot" available-spots)))

(defn is-a-single-character? [input]
  (= (count input) 1))

(defmulti get-marker-choice :player-type)

(defmethod get-marker-choice :human [player]
  (println "Choose a character to use as your marker.")
  (get-user-input is-a-single-character? "your choice must be a single character"))

(defmethod get-marker-choice :computer [player]
  (println "Choose a character for the computer's marker.")
  (get-user-input is-a-single-character? "your choice must be a single character"))

(defn display-winner [marker]
  (println (str marker " wins!!!")))

(defn display-tie []
  (println "It's a tie!"))

(defn confirm-move [move player]
  (if (= player "O")
    (println (str "Computer played at spot " move))
    (println (str "You played at spot " move))))

(defn player-would-like-to-continue []
  (println "Do you want to play again? y/n")
  (= "y" (get-user-input #(= (some #{"y" "n"} (list %))) "please choose 'y' or 'n'")))

(defn clear-screen []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))

(defn goodbye []
  (println "Goodbye!"))
