(ns coffee.upd
  (:import java.net.MulticastSocket))

(def port 8005)

(def group (java.net.InetAddress/getByName "228.0.0.5"))

(def socket (atom nil))

(defn init []
  (let [sock (doto (MulticastSocket. port)
               (.joinGroup group))]
    (reset! socket sock)
    sock))

