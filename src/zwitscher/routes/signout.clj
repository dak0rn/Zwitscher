;; signout.clj - the sign out route
(ns zwitscher.routes.signout
  (:require [compojure.core :refer [GET]]
            [ring.util.response :refer [redirect-after-post]]
            [zwitscher.config :refer [config]]
            [zwitscher.services.security :refer [invalidate-token!]]))

(defn-
  get-signout
  "Handler for GET /signout"
  {:added "0.1.0"}
  [request]
  (if-let [jwt (get-in request [ :cookies (get-in config [ :security :cookie-name ]) :value ]) ]
    (do (invalidate-token! jwt)
        (redirect-after-post "/signin"))
    (redirect-after-post "/signup")))

(def routes [
             (GET "/signout" request (get-signout request))
             ])
