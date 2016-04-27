(ns tic-tac-toe.ui)

(defn display-board [board]
  (println (str "\n " (first board) " | " (nth board 1) " | " (nth board 2) " \n"
                  "-----------\n"
                  " " (nth board 3) " | " (nth board 4) " | " (nth board 5) " \n"
                  "-----------\n"
                  " " (nth board 6) " | " (nth board 7) " | " (nth board 8) " \n")))

(defn display-welcome-message []
  (println "Welcome to Tic Tac Toe in Clojure!"))

(defn is-a-number? [input]
  (some #{"0" "1" "2" "3" "4" "5" "6" "7" "8" "9"} (list input)))

(defn get-user-input [condition error-message]
  (loop [user-input (read-line)]
    (if (condition user-input)
      user-input
      (do (println (str "Sorry, " error-message ". Try again!"))
        (recur (read-line))))))

(defn get-integer-spot []
  (Integer. (get-user-input is-a-number? "that's not valid input")))

(defn is-an-available-spot? [available-spots input]
  (some (set available-spots) (vector input)))

(defn get-spot [user-marker available-spots]
   (println (str user-marker ": Where would you like to play? Available spots: " (clojure.string/join " " available-spots)))
   (loop [user-input (get-integer-spot)]
    (if (is-an-available-spot? available-spots user-input)
      user-input
      (do (println "Sorry, that spot is already taken. Try again!")
        (recur (get-integer-spot))))))

(defn is-a-single-character? [input]
  (= (count input) 1))

(defmulti get-marker-choice :player-type)

(defmethod get-marker-choice :human [player]
  (println "Choose a character to use as your marker.")
  (get-user-input is-a-single-character? "your choice must be a single character"))

(defmethod get-marker-choice :computer [player]
  (println "Choose a character for the computer's marker.")
  (get-user-input is-a-single-character? "your choice must be a single character"))

(defn get-integer-spot []
  (Integer. (get-user-input is-a-number? "that's not valid input")))

(defn is-an-available-spot? [available-spots input]
  (some (set available-spots) (vector input)))

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

(defn clear-screen []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))
