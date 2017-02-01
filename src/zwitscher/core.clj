;; core.clj - gw core namespace
(ns zwitscher.core
  (:gen-class)
  (:require [mount.core :refer [start]]
            [zwitscher.config :refer [config]]
            [zwitscher.routes :refer [routes]]
            [ring.adapter.jetty :refer [run-jetty]]
            [zwitscher.database]))

(defn
  boot
  "Starts the application's stateful parts"
  { :added "0.1.0" }
  []
  (start
    #'zwitscher.config/config
    #'zwitscher.database/db))

(defn
  -main
  "Starts the web server"
  { :added "0.1.0" }
  []
  (boot)
  (run-jetty routes { :port (:port config) }))
