;; say.clj - handler for texts to be tweeted
(ns zwitscher.routes.say
  (:require [compojure.core :refer [POST]]
            [zwitscher.services.tweets :as tweets]
            [ring.util.response :refer [redirect-after-post]]
            [zwitscher.views.dashboard :refer [render-dashboard]]
            ))

(defn-
  post-say
  "Handler for POST /say"
  {:added "0.1.0"}
  [request]
  (let [user (:zwitscher-session request)
        text (-> request :params :text)]
    (if (or (empty? text)
            (> (count text) 145))
      (redirect-after-post "/?tl=true")
      (do
        (tweets/insert-tweet user text)
        (redirect-after-post "/")))
    ))

(def routes [
             (POST "/say" request (post-say request))
             ])
