(ns advanced-search-example.core
  (:require [perm.QmP6ExyLbbqHnKK6o53jMiVvPhxHcc9G3jyxTgXLD4ca6H :as clg]
            [permacode.core :as perm]
            [clojure.string :as str]
            [clojure.set :as set]))

(perm/pure
 (def stop-words #{"a" "is" "are" "am" "i" "you" "the" "in" "on" "at" "for"})

 (defn keywords [text]
   (->> (str/split text #"[ .?:;,!]")
        (map str/lower-case)
        (filter (comp not stop-words))))
 
 (clg/defrule keyword-index [keywd id]
   [:search.com/doc id params text] (clg/by-anyone)
   (for [text (set/union #{text} (vals params))])
   (when (string? text))
   (for [keywd (keywords text)]))

 (clg/defrule param-index [[k v] id]
   [:search.com/doc id params text] (clg/by-anyone)
   (for [[k v] params]))

 (clg/defrule param-keyword [[param kw] id]
   [param-index [param text] id] (clg/by-anyone)
   (when (string? text))
   (for [kw (keywords text)]))

 (clg/defclause search-1
   :search.com/search [search-str] [params text]
   (let [search-words (keywords search-str)
         first-word (first search-words)])
   [keyword-index first-word id] (clg/by :search.com)
   [:search.com/doc id params text] (clg/by-anyone)
   (when (every? (partial contains? (set (keywords text))) search-words))))


