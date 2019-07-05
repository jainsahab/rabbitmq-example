(ns rabbitmq.tutorials.receive-logs
  (:require [langohr.core :as lc]
            [langohr.channel :as lch]
            [langohr.exchange :as le]
            [langohr.queue :as lq]
            [langohr.basic :as lb]
            [langohr.consumers :as lcons]))

(def ^{:const true} exchange "clojure-exchange")
(def ^{:const true} retry-exchange "retry-clojure-exchange")

(defn handle-delivery
  "Handles message delivery"
  [ch metadata payload]
  (println (format " [x] %s" (String. payload "UTF-8")))
  (Thread/sleep 5000)
  (lb/publish ch retry-exchange "" payload)
  (lb/ack ch (:delivery-tag metadata) false))


(defn -main
  [& args]
  (with-open [conn (lc/connect {:host "localhost" :port 5672})]
    (let [ch              (lch/open conn)
          {:keys [queue]} (lq/declare ch "clojure-queue" {:durable true :auto-delete false})]
      (le/fanout ch exchange {:durable true})
      (le/direct ch retry-exchange {:durable true})
      (lq/bind ch queue exchange)
      (println " [*] Waiting for logs. To exit press CTRL+C")
      (lcons/blocking-subscribe ch queue handle-delivery))))
