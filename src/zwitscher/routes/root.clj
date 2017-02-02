;; root.clj - the root namespace
(ns zwitscher.routes.root
  (:require [compojure.core :refer [GET]]
            [zwitscher.services.tweets :as tweets]
            [zwitscher.views.dashboard :refer [render-dashboard]]
            ))

(defn-
  get-root
  "Handler for GET /"
  {:added "0.1.0"}
  [request]
  (let [user (:zwitscher-session request)
        follower-tweets (tweets/get-follower-tweets user)]
    (render-dashboard user
                      :tweets follower-tweets)
    ))

(def routes [
             (GET "/" request (get-root request))
             ])
