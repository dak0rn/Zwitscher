;; routes.clj - application routes
(ns zwitscher.routes
  (:require [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [zwitscher.middlewares.session :refer [wrap-session]]
            [zwitscher.middlewares.errors :refer [wrap-errors]]
            [zwitscher.middlewares.static :refer [wrap-static]]
            [ring.util.response :refer [resource-response content-type]]

            ;; routes
            [zwitscher.routes.root]
            [zwitscher.routes.signup]
            [zwitscher.routes.signin]
            [zwitscher.routes.signout]
            [zwitscher.routes.say]
            [zwitscher.routes.stream]
            [zwitscher.routes.discover]
            [zwitscher.routes.settings]

            [compojure.core :as compojure]))

(def ^:private defaults
  (-> site-defaults
      (assoc :static { :resources "www" })))

(def ^:private route-handler
  (apply compojure/routes
    (concat
     zwitscher.routes.signup/routes
     zwitscher.routes.signin/routes
     zwitscher.routes.root/routes
     zwitscher.routes.signout/routes
     zwitscher.routes.say/routes
     zwitscher.routes.stream/routes
     zwitscher.routes.discover/routes
     zwitscher.routes.settings/routes
      )))

(def routes
  (-> route-handler

      wrap-errors
      wrap-session

      wrap-static
      (wrap-defaults defaults)))
