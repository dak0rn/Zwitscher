;; bin.clj - command line utilities
(ns zwitscher.bin
  (:require [zwitscher.core :refer [boot]]
            [clojure.java.io :as io]
            [ragtime.jdbc :as ragtime]
            [clojure.java.jdbc :refer [db-do-commands]]
            [zwitscher.database :refer [db]]
            [ragtime.repl :as ragfns]))

(defn-
  create-migrate-config
  "Creates the configuration for the migrations"
  { :added "0.1.0" }
  []
  {
    :datastore (ragtime/sql-database db)
    :migrations (ragtime/load-resources "migrations")
  })

(defn
  migrate
  "Set up the database environment"
  { :added "0.1.0" }
  []
  (boot)
  (let [config (create-migrate-config)]
    (ragfns/migrate config)))

(defn
  demigrate
  "Tears down the database again"
  { :added "0.1.0" }
  []
  (boot)
  (let [config (create-migrate-config)]
    (ragfns/rollback config)))

(defn-
  sql-file?
  "Determines if the given file is an SQL file"
  { :added "0.1.0" }
  [ file ]
  (-> file .getName (.endsWith ".sql")))

(defn-
  sql-files
  "Given a collection of files, finds all sql files"
  { :added "0.1.0" }
  [ files ]
  (println "Filtering files")
  (filter sql-file? files))

(defn-
  load-sql-file
  "Loads the given SQL file and returns a collection of queries to be executed"
  { :added "0.1.0" }
  [ file ]
  (let [ contents (slurp file) ]
    (.split contents "--;;")))

(defn
  seed
  "Inserts default values in the existing database"
  { :added "0.1.0" }
  []
  (boot)
  (let [ seeding-files (->> "resources/seedings" io/file .listFiles sql-files (sort-by #(.getName %)))
         sql-statements (mapcat load-sql-file seeding-files)
       ]
    ;; Executes a bunch of SQL statements in order wrapped in a transaction
    (db-do-commands db sql-statements)))
