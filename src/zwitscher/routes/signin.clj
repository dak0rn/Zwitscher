;; signin.clj - The signin route
(ns zwitscher.routes.signin
  (:require [compojure.core :refer [GET POST]]
            [validateur.validation :as v]
            [zwitscher.is :refer [password? username?]]
            [zwitscher.services.user :refer [get-for-session]]
            [zwitscher.services.security :refer [encrypt add-jwt-header issue-token!]]
            [ring.util.response :refer [redirect-after-post]]
            [zwitscher.routes.signup :refer [get-invalids]]
            [zwitscher.views.signin :refer [render-signin]]))

;; Handler functions

(defn-
  get-signin
  "Handler for GET /signin"
  {:added "0.1.0"}
  [request]
  (let [params (:params request)
        no-name (:nn params)
        no-pass (:np params)
        invalid (:i params)]
    (render-signin :invalid invalid
                   :name-missing no-name
                   :password-missing no-pass)))

(defn-
  post-signin
  "Handler for POST /signin/do"
  {:added "0.1.0"}
  [request]
  (let [params (:params request)
        name (:dat-name params)
        pass (:dat-pass params)
        invalids (get-invalids {:name name :pass pass})
        ]
    (cond
      (:pass invalids) (redirect-after-post "/signin?np=true")
      (:name invalids) (redirect-after-post "/signin?nn=true")
      :else (if-let [user (get-for-session name pass)]
              (add-jwt-header (redirect-after-post "/")
                              (issue-token! (:iduser user)))
              (redirect-after-post "/signin?i=true"))
      )))

(def routes [
             (GET "/signin" request (get-signin request))
             (POST "/signin/do" request (post-signin request))
             ])
