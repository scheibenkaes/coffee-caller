(ns coffee.server
  (:use compojure.core
        compojure.route
        org.httpkit.server))

(def server (atom nil))

(def open-connections (ref []))

(defroutes server-routes
  (GET "/ws" []
       (fn [req]
         (with-channel req channel
           (dosync
            (alter open-connections conj channel))
           (println "WS connection opened")
           (on-close channel (fn [status]
                               (dosync
                                (alter open-connections
                                       #(remove (partial = channel) %)))
                               (println "Channel closed " status)
                               (println "Connections left " (count @open-connections))))
           (on-receive channel (fn [data]
                                 (println "Data " data))))))
  (resources "public"))

(defn start-server []
  (reset! server (run-server server-routes {:port 8080})))

(defn stop-server []
  (when @server
    (server)))

