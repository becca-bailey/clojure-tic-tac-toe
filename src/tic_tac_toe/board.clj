(ns tic-tac-toe.board
  (:require [clojure.math.numeric-tower :as math]))

(def initial-board [0 1 2 3 4 5 6 7 8])

(def blank-4x4 [0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15])

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

(defn cols [grid-size]
  (for [col-start (col-start grid-size)]
   (loop [col (vector col-start)]
     (if (= grid-size (count col))
      col
      (recur (conj col (+ col-start (* (count col) grid-size)))))))) 

(defn diagonals [grid-size]
  (for [diagonal-start (diagonal-start grid-size)]
    (loop [diagonal (vector diagonal-start)]
      (if (= grid-size (count diagonal)) 
        diagonal
        (if (= 0 diagonal-start)     
          (recur (conj diagonal (+ (inc grid-size) (last diagonal))))
          (recur (conj diagonal (+ (dec grid-size) (last diagonal)))))))))

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

(defn generate-board-string [board]
  (let [size (grid-size board)]
    (let [rows (map #(str " " (clojure.string/join " | " %) " ") (partition size board))
          divider (str "\n" (clojure.string/join (repeat (+ (dec size) (* 3 size)) "-")) "\n")]
      (clojure.string/join divider rows)))) 

