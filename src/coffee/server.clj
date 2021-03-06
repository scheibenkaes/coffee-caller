(ns coffee.server
  (:use compojure.core
        compojure.route
        org.httpkit.server)
  (:require [coffee.upd :as udp]))

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
  (println "sending to all connections " (count @connections))
  (doseq [con @connections]
    (try
      (send! con data)
      (catch Exception e
        (println e)))))

(defn ws-handler [req]
  (with-channel req channel
    (save-connection channel)
    (println "WS connection opened")
    (on-close channel (fn [status]
                        (remove-connection channel)
                        (println "Channel closed " status)
                        (println "Connections left " (count @open-connections))))
    (on-receive channel (fn [data]
                          (println "Received " data " from WebSocket")
                          (udp/send-multicast (str data))))))

(defroutes server-routes
  (GET "/ws" [] ws-handler)
  (resources "public"))

(defn start-server []
  (udp/init #(send-to-all open-connections %))
  (reset! server (run-server server-routes {:port 8080})))

(defn stop-server []
  (when @server
    (server)))

