(ns coffee.repl
  (:use clojure.repl)
  (:import java.net.MulticastSocket
           java.net.DatagramPacket
           java.net.InetAddress)
  (:require [coffee.upd :as udp]))

(def sock (doto (MulticastSocket. 8484)
            (.joinGroup (InetAddress/getByName "228.0.0.5"))))

(defn send-msg [^String data]
  (udp/send-multicast sock data))

