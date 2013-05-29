(defproject org.scheibenkaes/coffee "0.1.0-SNAPSHOT"
  :description "Coffee Caller WebSocket Server"
  :url "http://scheibenkaes.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [http-kit "2.1.2"]]
  :main coffee.core)
