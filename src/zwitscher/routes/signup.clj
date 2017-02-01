;; signup.clj - The signup route
(ns zwitscher.routes.signup
  (:require [compojure.core :refer [GET POST]]
            [zwitscher.views.signup :refer [render-signup]]))

(defn-
  get-signup
  "Handler for GET /signup"
  {:added "0.1.0"}
  [request]
  (let [params (:params request)
        no-name (:nn params)
        no-pass (:np params)
        exists (:e params)]
    (render-signup :exists exists
                   :name-missing no-name
                   :password-missing no-pass)))

(def routes [
             (GET "/signup" request (get-signup request))
             ])
