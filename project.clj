(defproject advanced-search-example "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [permacode/permacode "0.1.1-SNAPSHOT"]]
  :profiles {:dev {:dependencies [[midje "1.7.0"]]
                   :plugins [[permacode/permacode "0.1.1-SNAPSHOT"]]}
             :midje {}})


  
