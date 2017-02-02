;; tweets.clj - services for tweets
(ns zwitscher.services.tweets
  (:require [hugsql.core :refer [def-db-fns]]
            [clojure.java.jdbc :refer [with-db-transaction]]
            [zwitscher.services :refer [def-db-service]]))

(def-db-fns "sql/tweets.sql")

(def-db-service
  get-follower-tweets
  "Given a user, returns the tweets of the followed users"
  {:added "0.1.0"}
  [user]
  (query-tweets-for-user db {:uid (:iduser user)}))
