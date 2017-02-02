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

(def-db-service
  insert-tweet
  "Inserts a new tweet for the user"
  {:added "0.1.0" :with-transaction true}
  [user tweet]
  (let [uid (:iduser user)]
    (with-db-transaction [trx db]
      (query-insert-tweet trx {:uid uid :text tweet})
      (query-increase-tweet-count trx {:uid uid}))))

(def-db-service
  get-tweets
  "Returns the tweet for a particular user"
  {:added "0.1.0"}
  [user]
  (query-user-tweets db {:uid (:iduser user)}))

(def-db-service
  follows?
  "Determines if a user follows another"
  {:added "0.1.0"}
  [follower followed]
  (let [who (:iduser follower)
        whom (:iduser followed)]
    (-> (query-get-follow-entry db {:who who :whom whom})
        nil?
        not)
    ))
