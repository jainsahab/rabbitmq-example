(ns rabbitmq.tutorials.emit-log
  (:require [langohr.core :as lc]
            [langohr.channel :as lch]
            [langohr.exchange :as le]
            [langohr.basic :as lb]
            [clojure.string :as s])
  (:import (java.util Scanner)))

(def ^{:const true} exchange-name "clojure-exchange")

(defn -main
  [& args]
  (with-open [conn (lc/connect)]
    (let [ch (lch/open conn)
          scanner (Scanner. System/in)]
      (le/fanout ch exchange-name {:durable true :auto-delete false})
      (while true ((fn []
                     (println "Enter message")
                     (let [payload (.nextLine scanner)]
                       (lb/publish ch exchange-name "" payload)
                       (println (format " [x] Sent %s" payload)))))))))
