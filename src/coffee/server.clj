(ns coffee.server
  (:use compojure.core
        compojure.route
        org.httpkit.server))

(def server (atom nil))

(def open-connections (ref []))

(defn remove-connection [channel]
  (dosync
   (alter open-connections
          #(remove (partial = channel) %))))

(defn save-connection [channel]
  (dosync
   (alter open-connections conj channel)))

(defn send-to-all [connections data]
  (doseq [con connections]
    (send! con data)))

(defn ws-handler [req]
  (with-channel req channel
    (save-connection channel)
    (println "WS connection opened")
    (on-close channel (fn [status]
                        (remove-connection channel)
                        (println "Channel closed " status)
                        (println "Connections left " (count @open-connections))))
    (on-receive channel (fn [data]
                          (println "Data " data)
                          (send-to-all @open-connections data)))))

(defroutes server-routes
  (GET "/ws" [] ws-handler)
  (resources "public"))

(defn start-server []
  (reset! server (run-server server-routes {:port 8080})))

(defn stop-server []
  (when @server
    (server)))

