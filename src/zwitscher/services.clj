;; services.clj - helper functions for services
(ns zwitscher.services
  (:require [zwitscher.database :as database]))

(defmacro
  def-db-service
  "Defines a services that uses database connection. Automatically
   creates a multi-arity function that passes the database connection
   named 'db' as last parameter, either from the imported namespace
   or from the passed database parameter.

  (def-db-service name doc { }
    [ arg1 arg2 ] (...))

  becomes

  (defn name doc { }
    ([ arg1 arg2 ] (name arg1 arg2 db))
    ([ arg1 arg2 db] (...)))
  "
  { :added "0.1.0" }
  [ name docstr attr-map args & body ]
  (let [ all-args (conj args 'db) ]
    `(defn ~name ~docstr ~attr-map

       ([~@args] (~name ~@args database/db))

       ([~@all-args] ~@body))))
