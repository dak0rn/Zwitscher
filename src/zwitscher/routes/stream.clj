;; stream.clj - a user's stream
(ns zwitscher.routes.stream
  (:require [compojure.core :refer [GET]]
            [zwitscher.services.tweets :refer [get-tweets follows?]]
            [zwitscher.services.user :as user]
            [zwitscher.database :refer [db]]
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
                     :tweets tweets
                     :follows follows)))
  )

(def routes [
             (GET "/@:name" request (get-stream request))
             ])
