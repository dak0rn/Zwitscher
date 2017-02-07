;; stream.clj - a user's stream
(ns zwitscher.routes.stream
  (:require [compojure.core :refer [GET POST]]
            [zwitscher.services.tweets :refer [get-tweets
                                               follows?
                                               delete-tweet!]]
            [zwitscher.services.user :as user]
            [zwitscher.database :refer [db]]
            [zwitscher.util :refer [to-uuid]]
            [ring.util.response :refer [redirect-after-post]]
            [clojure.java.jdbc :refer [with-db-transaction]]
            [zwitscher.views.stream :refer [render-stream]]))

(defn-
  get-stream
  "A handler for GET /@username"
  {:added "0.1.0"}
  [request]
  (with-db-transaction [trx db]
    (let [name (-> request :params :name)
          user (user/get-by-username name trx)
          tweets (get-tweets user trx)
          follows (follows? (:zwitscher-session request) user)]
      (render-stream user
                     (:zwitscher-session request)
                     :tweets tweets
                     :follows follows)))
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

(def routes [
             (GET "/@:name" request (get-stream request))
             (POST "/delete" request (delete-tweet request))
             ])
