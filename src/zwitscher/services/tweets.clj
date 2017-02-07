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

(def-db-service
  delete-tweet!
  "Deletes the tweet with the given ID for the given user"
  {:added "0.1.0"}
  [user tweet-id]
  (with-db-transaction [trx db]
    (query-delete-tweet trx {:uid (:iduser user) :tid tweet-id})
    (query-decrease-tweet-count trx {:uid (:iduser user)})))

(def-db-service
  like-tweet!
  "Marks the tweet with the given id as being liked by the given user"
  {:added "0.1.0"}
  [user tweet-id]
  (query-like-tweet db {:uid (:iduser user) :tid tweet-id}))

(def-db-service
  dislike-tweet!
  "Marks the tweet with the given id as not being liked by the given user"
  {:added "0.1.0"}
  [user tweet-id]
  (query-dislike-tweet db {:uid (:iduser user) :tid tweet-id}))
