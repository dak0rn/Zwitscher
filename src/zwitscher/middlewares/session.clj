;; session.clj - session middleware
(ns zwitscher.middlewares.session
  (:require [zwitscher.config :refer [config]]
            [zwitscher.services.user :as user]
            [zwitscher.util :refer [to-uuid]]
            [zwitscher.services.security :refer [add-jwt-header
                                                 create-jwt
                                                 get-claims
                                                 token-valid?
                                                 reissue-token!]]
            [ring.util.response :refer [status redirect]]))

(defn-
  public?
  "Determines if the given route is public"
  { :added "0.1.0" }
  [path]
  (or (= "/signup" path)
      (= "/signup/do" path)
      (= "/signin/do" path)
      (= "/signout" path)
      (= "/signin" path)))

(defn-
  session-error
  "Sends a redirect to the signup page if no-redirect is false.
   Sends a 403 otherwise"
  { :added "0.1.0" }
  [ no-redirect ]
  (if no-redirect
    (status {} 403)
    (redirect "/signup")))


(defn
  wrap-session
  "Session middleware that loads the required session and redirect to the
   login if no session was found"
  { :added "0.1.0" }
  [next]
  (fn [request]
    (let [ path (:uri request)
          no-redirect (= "1" (get-in request [ :params :_ ]))]

      ;; See if the path is public
      (if (public? path)
        (next request)

        ;; Path is not public, try to fetch the session
        (try
          (let [ jwt (get-in request [ :cookies (get-in config [ :security :cookie-name ]):value ]) ]

            (if (not (token-valid? jwt))
              ;; Not JWT or invalid
              (session-error no-redirect)

              ;; Got a JWT
              (let [claims (get-claims jwt)
                    iduser (to-uuid (:jti claims)) ]
                (if-let [theuser (user/get-enabled-by-id iduser)]
                  ;; Got a session

                  ;; Process the request and inject a new cookie
                  (add-jwt-header (next (assoc request :zwitscher-session theuser))
                                  (reissue-token! jwt iduser))

                  (session-error no-redirect)))))

          ;; In case of an error, redirect to the login, but log the
          ;; error.
          (catch clojure.lang.ExceptionInfo error
            (println "Error in session middleware" error)
            (session-error no-redirect)))

        ))))

