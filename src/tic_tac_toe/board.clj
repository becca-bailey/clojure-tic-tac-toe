(ns tic-tac-toe.board)

(def initial-board [0 1 2 3 4 5 6 7 8])

(defn place-marker [board spot marker]
  (assoc board spot marker))

(defn replace-with-player-marker [players spots current-spot]
  (let [[player-1 player-2] players [player-1-spots player-2-spots] spots]
    (cond
      (contains? player-1-spots current-spot)
      player-1
      (contains? player-2-spots current-spot)
      player-2
      :else current-spot)))

(defn make-board [players-and-spots]
   (let [players (keys players-and-spots) spots (vals players-and-spots)]
    (into [] (map #(replace-with-player-marker players spots %) initial-board))))

(def winning-combinations [[0 1 2] [3 4 5] [6 7 8]
                           [0 3 6] [1 4 7] [2 5 8]
                           [0 4 8] [2 4 6]])

(defn marker-is-in-spot [board spot marker]
  (= marker (nth board spot)))

(defn spot-is-empty [board spot]
  (= (nth board spot) spot))

(defn is-a-winning-combination? [board set-of-possible-moves marker]
  (loop [possible-win-spots set-of-possible-moves]
    (cond
      (empty? possible-win-spots)
      true
      (marker-is-in-spot board (first possible-win-spots) marker)
      (recur (rest possible-win-spots))
      :spot-is-empty false)))

(defn available-spots [board]
  (filter integer? board))
