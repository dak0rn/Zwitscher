;; database.clj - database connection namespace
(ns zwitscher.database
  (:require [mount.core :refer [defstate]]
            [clj-time.jdbc]
            [zwitscher.config :refer [config]]))

(defstate db
  :start (:database config))
;; TODO Add aconnection pooling using the c3p0 library
