;; static.clj - middleware for statis resources
;; Jumps in for the stuff not handled by the ring-defaults middleware
(ns zwitscher.middlewares.static
  (:require [ring.util.response :refer [resource-response content-type]]))

;; Things we want to serve
(def ^:private files {
                      "/signup" "signup.html"
})

(defn-
  serve-file
  "Serves the given HTML file"
  { :added "0.1.0" }
  [ file ]
  (content-type (resource-response file { :root "www" }) "text/html"))

(def m-serve (memoize serve-file))

(defn
  wrap-static
  "Middleware that serves static files"
  { :added "0.1.0" }
  [next]
  (fn [request]
    (if-let [file (get files (:uri request))]
      (m-serve file)
      (next request))))
