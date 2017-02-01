;; root.clj - the root namespace
(ns zwitscher.routes.root
  (:require [compojure.core :refer [GET]]
            ))

(defn-
  get-root
  "Handler for GET /"
  {:added "0.1.0"}
  [request]
  {
   :body "hello, world"
   :headers {
             "Content-Type" "text/plain"
             }
  })

(def routes [
             (GET "/" request (get-root request))
             ])
