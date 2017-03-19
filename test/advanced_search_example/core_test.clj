(ns advanced-search-example.core-test
  (:require [midje.sweet :refer :all]
            [advanced-search-example.core :refer :all]
            [perm.QmP6ExyLbbqHnKK6o53jMiVvPhxHcc9G3jyxTgXLD4ca6H :as clg]
            [clojure.set :as set]))

; keyword-index
(fact
 (clg/simulate-with keyword-index :search.com
                    (clg/fct [:search.com/doc 1234 {} "Foo goes in to a bar..."]))
 => (contains [["foo" 1234] ["bar" 1234]])
 (clg/simulate-with keyword-index :search.com
                    (clg/fct [:search.com/doc 1234 {} "a is are am I you the in on at for"]))
 => empty?
 (clg/simulate-with keyword-index :search.com
                    (clg/fct [:search.com/doc 1234 {:foo "this is Foo"
                                                    :bar "this is bar..."
                                                    :baz [1 2 3]} ""]))
 => (contains [["foo" 1234] ["bar" 1234]]))

; param-index
(fact
 (clg/simulate-with param-index :search.com
                    (clg/fct [:search.com/doc 1234 {:foo 2
                                                    :bar [1 2 3]} "some text"]))
 => (contains [[[:foo 2] 1234] [[:bar [1 2 3]] 1234]]))


(def search-rules (map (fn [[k v]] @v) (ns-publics 'advanced-search-example.core)))
; param-keyword
(fact
 (-> (clg/simulate-rules-with search-rules :search.com
                              (clg/fct [:search.com/doc 1234 {:foo "hello, world"
                                                              :bar [1 2 3]} "some text"]))
     (get [:advanced-search-example.core/param-keyword 2]))
 => (contains [[[:foo "hello"] 1234]]))

; search
(fact
 (clg/run-query search-rules
                [:search.com/search "foo"] 2 :search.com #{:me}
                (clg/fct [:search.com/doc 1234 {} "Foo goes into a bar..."] :writers #{:search.com}))
 => #{[{} "Foo goes into a bar..."]}
 (clg/run-query search-rules
                [:search.com/search "foo bar"] 2 :search.com #{:me}
                (clg/fct [:search.com/doc 1234 {} "Foo goes into a bar..."] :writers #{:search.com})
                (clg/fct [:search.com/doc 1234 {} "Foo runs into a car..."] :writers #{:search.com}))
 => #{[{} "Foo goes into a bar..."]})
