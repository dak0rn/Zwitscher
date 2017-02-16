;; settings.clj - the settings route
(ns zwitscher.routes.settings
  (:require [compojure.core :refer [GET POST]]
            [zwitscher.views.settings :refer [render-settings]]
            [zwitscher.is :refer [password?]]
            [zwitscher.services.user :refer [set-password]]
            [zwitscher.services.security :refer [encrypt]]
            [ring.util.response :refer [redirect-after-post]]))

(defn-
  get-settings
  "Handler for GET /settings"
  {:added "0.1.0"}
  [request]
  (let [saved (-> request :params :s)
        invalid-inputs (-> request :params :i)]
    (render-settings :invalid invalid-inputs
                     :saved saved)))

(defn-
  post-settings
  "Handler for POST /settings"
  {:added "0.1.0"}
  [request]
  (let [password (-> request :params :p1)
        confirmation (-> request :params :p2)]
    (if (or (not (password? password)) (not= password confirmation))
      ;; Passwords are invalid or not equal
      (redirect-after-post "/settings?i=t")

      ;; Update the password and redirect the user back
      (do
        (set-password (:zwitscher-session request)
                      (encrypt password))
        (redirect-after-post "/settings?s=t")
        )
      )
    ))


(def routes [
             (GET "/settings" request (get-settings request))
             (POST "/settings" request (post-settings request))
             ])
