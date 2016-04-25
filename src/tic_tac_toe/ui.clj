(ns tic-tac-toe.ui)

(defn display-board [board]
  (println (str "\n " (first board) " | " (nth board 1) " | " (nth board 2) " \n"
                  "-----------\n"
                  " " (nth board 3) " | " (nth board 4) " | " (nth board 5) " \n"
                  "-----------\n"
                  " " (nth board 6) " | " (nth board 7) " | " (nth board 8) " \n")))

(defn display-welcome-message []
  (println "Welcome to Tic Tac Toe in Clojure!"))

(defn get-user-input []
  (loop [user-input (read-line)]
    (if (some #{"0" "1" "2" "3" "4" "5" "6" "7" "8" "9"} (list user-input))
      (Integer. user-input)
      (do (println "Sorry, that's not valid input. Try again!")
        (recur (read-line))))))

(defn get-spot [user-marker available-spots]
   (println (str user-marker ": Where would you like to play? Available spots: " (clojure.string/join " " available-spots)))
   (loop [user-input (get-user-input)]
    (if (some (set available-spots) (vector user-input))
      user-input
      (do (println "Sorry, that spot is already taken. Try again!")
        (recur (get-user-input))))))

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
