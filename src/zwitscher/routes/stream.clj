;; stream.clj - a user's stream
(ns zwitscher.routes.stream
  (:require [compojure.core :refer [GET POST]]
            [zwitscher.services.tweets :refer [get-tweets
                                               follows?
                                               delete-tweet!
                                               like-tweet!
                                               dislike-tweet!
                                               follow-user
                                               unfollow-user
                                               get-followers
                                               get-following]]
            [zwitscher.services.user :as user]
            [zwitscher.database :refer [db]]
            [zwitscher.util :refer [to-uuid]]
            [ring.util.response :refer [redirect-after-post]]
            [clojure.java.jdbc :refer [with-db-transaction]]
            [zwitscher.views.stream :refer [render-stream render-followers]]))

(defn-
  get-stream
  "A handler for GET /@username"
  {:added "0.1.0"}
  [request]
  (with-db-transaction [trx db]
    (let [name (-> request :params :name)
          user (user/get-by-username name trx)]
      (if (nil? user)
        (redirect-after-post "/?e=nf")
        (let [tweets (get-tweets user trx)
              follows (follows? (:zwitscher-session request) user)]
            (render-stream user
                           (:zwitscher-session request)
                           :tweets tweets
                           :follows follows)))))
  )

(defn-
  delete-tweet
  "Handler for POST /delete"
  {:added "0.1.0"}
  [request]
  (let [user (:zwitscher-session request)
        tweet (-> request :params :tweet to-uuid)
        ]
    (delete-tweet! user tweet)
    (redirect-after-post "/?d=t")))

(defn-
  like-tweet
  "Handler for POST /like"
  {:added "0.1.0"}
  [request]
  (let [user (:zwitscher-session request)
        tweet (-> request :params :tweet to-uuid)]
    (like-tweet! user tweet)
    (redirect-after-post "/?l=t")))

(defn-
  dislike-tweet
  "Handler for POST /dislike"
  {:added "0.1.0"}
  [request]
  (let [user (:zwitscher-session request)
        tweet (-> request :params :tweet to-uuid)]
    (dislike-tweet! user tweet)
    (redirect-after-post "/?dl=t")))

(defn-
  get-user-followers
  "Handler for GET /@:name/followers"
  {:added "0.1.0"}
  [request]
  (with-db-transaction [trx db]
    (let [name (-> request :params :name)
          target (user/get-by-username name trx)]
      (if target
        (let [followers (get-followers target trx)
              follows (follows? (:zwitscher-session request) target)]
          (render-followers target followers
                            :follows follows))

        (redirect-after-post "/?nf=t")
        ))))

(defn-
  get-follow-user
  "Handler for GET /f/:uid"
  {:added "0.1.0"}
  [request]
  (let [user-id (-> request :params :uid to-uuid)
        current (:zwitscher-session request)]
    (with-db-transaction [trx db]
      (if-let [target (user/get-by-id user-id trx)]
        ;; Found a user
        (do
          (follow-user current target trx)
          (redirect-after-post "/?f=t"))

        ;; No user found
        (redirect-after-post "/?e=nf")
        ))))

(defn-
  get-unfollow-user
  "Handler for GET /u/:uid"
  {:added "0.1.0"}
  [request]
  (let [user-id (-> request :params :uid to-uuid)
        current (:zwitscher-session request)]
    (with-db-transaction [trx db]
      (if-let [target (user/get-by-id user-id trx)]
        (do
          (unfollow-user current target trx)
          (redirect-after-post "/?f=t"))
        (redirect-after-post "/?e=nf")
        ))))

(defn-
  get-user-following
  "Handler for GET /following"
  {:added "0.1.0"}
  [request]
  (let [user (:zwitscher-session request)
        following (get-following user)]
    (render-followers user
                     following
                     :follows following
                     :page-title "My followers"
                     :title "My followers"
                     :back-link ""
                     :hide-unfollow-link true
                     :back-title "&larr; Back to my profile")
    ))

(def routes [
             (GET "/@:name" request (get-stream request))
             (GET "/@:name/followers" request (get-user-followers request))
             (POST "/delete" request (delete-tweet request))
             (POST "/like" request (like-tweet request))
             (POST "/dislike" request (dislike-tweet request))
             (GET "/f/:uid" request (get-follow-user request))
             (GET "/u/:uid" request (get-unfollow-user request))
             (GET "/following" request (get-user-following request))
             ])
