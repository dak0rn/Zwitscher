;; user.clj - services for users
(ns zwitscher.services.user
  (:require [hugsql.core :refer [def-db-fns]]
            [clojure.java.jdbc :refer [with-db-transaction]]
            [zwitscher.services.security :refer [verify]]
            [zwitscher.services :refer [def-db-service]]))

(def-db-fns "sql/user.sql")

(def-db-service
  create-user
  "Creates a new user for the given username and password.
   The new user is returned"
  { :added "0.1.0" }
  [ username password ]
  (query-create-user db {:name username :pass password}))

(def-db-service
  get-by-id
  "Returns the user with the given ID"
  { :added "0.1.0" }
  [ userid ]
  (query-get-by-id db { :uid userid }))

(def-db-service
  get-by-username
  "Returns the user with the given username"
  { :added "0.1.0" }
  [ username ]
  (query-get-by-username db { :name username }))

(def-db-service
  get-enabled-by-id
  "Selets an enabled user identified by the given ID"
  {:added "0.1.0"}
  [id]
  (query-get-enabled-by-id db {:uid id}))

(def-db-service
  get-for-session
  "Given a user name and a plain text password, returns the
   user entitity for that information or nil"
  {:added "0.1.0"}
  [name pass]
  (when-let [user (get-by-username name)]
    (when (verify pass (:password user))
      user)
    ))

(def-db-service
  get-all
  "Returns all users"
  {:added "0.1.0"}
  []
  (query-all-users db))

(def-db-service
  set-password
  "Sets the password for the given user
   Expects the given password to be encrypted already"
  {:added "0.1.0"}
  [user password]
  (query-set-password db {:pass password :uid (:iduser user)}))
