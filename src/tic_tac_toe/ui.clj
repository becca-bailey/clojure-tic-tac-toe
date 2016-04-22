(ns tic-tac-toe.ui)

(defn display-board [board]
  (println (str "\n " (first board) " | " (nth board 1) " | " (nth board 2) " \n"
                  "----------\n"
                  " " (nth board 3) " | " (nth board 4) " | " (nth board 5) " \n"
                  "----------\n"
                  " " (nth board 6) " | " (nth board 7) " | " (nth board 8) " \n")))

(defn display-welcome-message []
  (println "Welcome to Tic Tac Toe in Clojure!"))

(defn get-user-input []
  (Integer. (read-line)))

(defn get-spot [user-marker]
   (println (str user-marker ": Where would you like to play? "))
   (get-user-input))

(defn display-winner [marker]
  (println (str marker " wins!!!")))

(defn display-tie []
  (println "It's a tie!"))

(defn confirm-move [move player]
  (if (= player "O")
    (println (str "Computer played at spot " move))
    (println (str "You played at spot " move))))
