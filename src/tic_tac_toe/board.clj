(ns tic-tac-toe.board)

(def winning-combinations [[0 1 2] [3 4 5] [6 7 8]
                           [0 3 6] [1 4 7] [2 5 8]
                           [0 4 8] [2 4 6]])

(defn place-marker [board spot marker]
  (assoc board spot marker))

(defn marker-is-in-spot [board spot marker]
  (= marker (nth board spot)))

(defn spot-is-empty [board spot]
  (= (nth board spot) spot))

(defn three-in-a-row [board set-of-three marker]
  (loop [possible-win-spots set-of-three]
    (cond
      (empty? possible-win-spots)
      true
      (marker-is-in-spot board (first possible-win-spots) marker)
      (recur (rest possible-win-spots))
      :spot-is-empty false)))

(defn is-winner? [board marker]
  (loop [possible-wins winning-combinations]
    (cond
      (empty? possible-wins)
      false
      (three-in-a-row board (first possible-wins) marker)
      true
      :else (recur (rest possible-wins)))))

(defn available-spots [board]
  (filter integer? board))

(defn won? [board]
  (or (is-winner? board "X") (is-winner? board "O")))

(defn tie? [board]
  (and (every? string? board) (not (won? board))))

(defn winner [board]
  (cond
    (is-winner? board "X")
    "X"
    (is-winner? board "O")
    "O"))
