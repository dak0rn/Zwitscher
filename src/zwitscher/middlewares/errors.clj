;; errors.clj - middleware that handles errors
(ns zwitscher.middlewares.errors
  (:require [ring.util.response :refer [response content-type status]]
            [zwitscher.config :refer [is-dev]]))

(defn
  wrap-errors
  "Middleware that catches errors and displays an error message.
   Will render a stack trace in development mode"
  { :added "0.1.0" }
  [next]
  (fn [request]
    (try
      (next request)
      (catch java.lang.Throwable ex
        (println "Error during request" ex)
        (if (is-dev)
          ;; TODO Add rendering / logging here
          (content-type (status (response (str "Dat error: \n" ex)) 500) "text/plain")
          (content-type (response "Error") "text/html"))
        ))))
