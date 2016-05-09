(ns tic-tac-toe.board
  (:require [clojure.math.numeric-tower :as math]))

(defn initial-board [grid-size]
  (into [] (range (math/expt grid-size 2))))

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

(defn make-board [grid-size players-and-spots]
   (let [players (keys players-and-spots) spots (vals players-and-spots)]
    (into [] (map #(replace-with-player-marker players spots %) (initial-board grid-size)))))

(defn grid-size [board]
  (math/sqrt (count board)))

(defn row-start [grid-size]
  (filter #(= 0 (mod % grid-size)) (range (math/expt grid-size 2))))

(defn col-start [grid-size]
  (range grid-size))

(defn diagonal-start [grid-size]
   (conj [0] (dec grid-size)))

(defn rows [grid-size]
  (map #(into [] (range % (+ % grid-size))) (row-start grid-size)))

(defn add-to-col [col-start grid-size]
  (reduce #(conj %1 (+ col-start (* %2 grid-size))) [] (range grid-size)))

(defn cols [grid-size]
  (map #(add-to-col % grid-size) (col-start grid-size)))

(defn left-to-right-diagonal [diagonal-start grid-size]
  (reduce #(conj %1 (+ diagonal-start (* %2 (inc grid-size)))) [] (range grid-size)))

(defn right-to-left-diagonal [diagonal-start grid-size]
  (reduce #(conj %1 (+ diagonal-start (* %2 (dec grid-size)))) [diagonal-start] (range 1 grid-size)))

(defn diagonals [grid-size]
  (let [[top-left top-right] (diagonal-start grid-size)]
      [(left-to-right-diagonal top-left grid-size)
       (right-to-left-diagonal top-right grid-size)]))

(defn winning-combinations [grid-size]
  (apply concat [(rows grid-size) (cols grid-size) (diagonals grid-size)]))

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

(defn add-space [n]
  (if (= (count (str n)) 1)
    (str n " ")
    n))

(defn column-padding [row size]
  (if (> size 3)
    (str " " (clojure.string/join " | " (map add-space row)) " ")
    (str " " (clojure.string/join " | " row) " ")))

(defn generate-board-string [board]
  (let [size (grid-size board)]
    (let [rows (map #(column-padding % size) (partition size board))
          divider (str "\n" (clojure.string/join (repeat (count (first rows)) "-")) "\n")]
      (clojure.string/join divider rows))))
