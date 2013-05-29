(ns coffee.server
  (:use compojure.core
        compojure.route
        org.httpkit.server))

(def server (atom nil))

(defroutes server-routes
  (GET "/" [] "Hello World"))

(defn start-server []
  (reset! server (run-server server-routes {:port 8080})))

(defn stop-server []
  (when @server
    (server)))

