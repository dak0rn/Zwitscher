;; discover.clj - The face book
(ns zwitscher.routes.discover
  (:require [compojure.core :refer [GET]]
            [zwitscher.services.user :refer [get-all]]
            [zwitscher.views.discover :refer [render-discover]]))

(defn-
  get-discover
  "Handler for GET /discover"
  {:added "0.1.0"}
  [request]
  (let [users (get-all)]
    (render-discover users (:zwitscher-session request))))

(def routes [
             (GET "/discover" request (get-discover request))
             ])
