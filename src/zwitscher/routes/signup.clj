;; signup.clj - The signup route
(ns zwitscher.routes.signup
  (:require [compojure.core :refer [GET POST]]
            [validateur.validation :as v]
            [zwitscher.is :refer [password? username?]]
            [zwitscher.services.user :refer [create-user]]
            [zwitscher.services.security :refer [encrypt add-jwt-header create-jwt]]
            [ring.util.response :refer [redirect-after-post]]
            [zwitscher.views.signup :refer [render-signup]]))

(defn-
 inline-validator
 "Creates a higher order function for validation"
 {:added "0.1.0"}
 [pred prop]
 (fn [values]
   (let [value (get values prop)]
     (if (pred value)
       [true {}]
       [false {prop #{"invalid"}}]))))

;; Validation schemes
(def get-invalids (v/validation-set
             (inline-validator username? :name)
             (inline-validator password? :pass)
             ))

;; Handler functions

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

(defn-
  post-signup
  "Handler for POST /signup/do"
  {:added "0.1.0"}
  [request]
  (let [params (:params request)
        name (:pref-name params)
        pass (:pref-pass params)
        invalids (get-invalids {:name name :pass pass})
        ]
    (cond
      (:pass invalids) (redirect-after-post "/signup?np=true")
      (:name invalids) (redirect-after-post "/signup?nn=true")
      :else (try
              (let [user (create-user name (encrypt pass))]
                (add-jwt-header (redirect-after-post "/")
                                (create-jwt {:jti (:iduser user)}))
                )
              (catch org.postgresql.util.PSQLException ex
                (if (= "23505" (.getSQLState ex))
                  (redirect-after-post "/signup?e=true")
                  (throw ex))
                )
              )
      )))

(def routes [
             (GET "/signup" request (get-signup request))
             (POST "/signup/do" request (post-signup request))
             ])
