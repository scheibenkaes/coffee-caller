(ns coffee.upd
  (:import java.net.MulticastSocket))

(def port 8005)

(def group (java.net.InetAddress/getByName "228.0.0.5"))

(def socket (atom nil))

(defn run-sock [receive-fn]
  (let [buf (byte-array 1024)
        pack (java.net.DatagramPacket. buf (count buf))]
    (println "Receiving")
    (.receive @socket pack)
    (receive-fn (-> pack .getData String.))
    (recur receive-fn)))

(defn send-multicast [^String data]
  (let [bytes (.getBytes data)
        pack (java.net.DatagramPacket. bytes (count bytes) group 8005)]
    (try
      (.send @socket pack)
      (catch Exception e
        (println (format "Error on sending multicast package %s.\n%s" data e))))))

(defn init [msg-fn]
  (let [sock (doto (MulticastSocket. port)
               (.joinGroup group))]
    (reset! socket sock)
    (-> (Thread. #(run-sock msg-fn)) .start)))

