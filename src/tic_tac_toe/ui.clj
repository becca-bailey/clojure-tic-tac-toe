(ns tic-tac-toe.ui)

(defn display-board [board]
  (print-str (str " " (first board) " | " (nth board 1) " | " (nth board 2) " \n"
                  "---------\n"
                  " " (nth board 3) " | " (nth board 4) " | " (nth board 5) " \n"
                  "---------\n"
                  " " (nth board 6) " | " (nth board 7) " | " (nth board 8) " ")))


(defn get-user-input []
  (do (flush) (Integer. (read-line))))

(defn get-spot [user-marker]
   (do (print (str user-marker ": Where would you like to play? "))
      (get-user-input)))
