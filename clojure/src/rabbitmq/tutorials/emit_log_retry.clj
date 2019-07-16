(ns rabbitmq.tutorials.emit-log-retry
  (:require [langohr.core :as lc]
            [langohr.channel :as lch]
            [langohr.exchange :as le]
            [langohr.basic :as lb]
            [clojure.string :as s])
  (:import (java.util Scanner)))

(def ^{:const true} exchange-name "retry-feature-exchange")

(defn -main
  [& args]
  (with-open [conn (lc/connect)]
    (let [ch (lch/open conn)
          scanner (Scanner. System/in)]
      (le/topic ch exchange-name {:durable true :auto-delete false})
      (while true ((fn []
                     (println "Enter message")
                     (let [payload (.nextLine scanner)]
                       (lb/publish ch exchange-name "messages.key" payload)
                       (println (format " [x] Sent %s" payload)))))))))
