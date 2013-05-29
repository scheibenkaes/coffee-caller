(ns coffee.core
  (:use coffee.server))

(defn -main [& args]
  (println "Starting server")
  (start-server))
