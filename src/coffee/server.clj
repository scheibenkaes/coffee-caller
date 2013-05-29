(ns coffee.server
  (:use compojure.core
        compojure.route
        org.httpkit.server))

(def server (atom nil))

(defroutes server-routes
  (GET "/ws" []
       (fn [req]
         (with-channel req channel
           (on-close channel (fn [status]
                               (println "Channel closed " status)))
           (on-receive channel (fn [data]
                                 (println "Data " data))))))
  (resources "public"))

(defn start-server []
  (reset! server (run-server server-routes {:port 8080})))

(defn stop-server []
  (when @server
    (server)))

