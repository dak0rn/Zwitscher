;; config.clj - the configuration
(ns zwitscher.config
  (:require [environ.core :refer [env]]
            [mount.core :refer [defstate]]))

(defn-
  create-configuration
  "Creates a configuration data structure from the environment"
  { :added "0.1.0" }
  []
  {
   :port (Integer/parseInt (env :port))
   :listen (env :listen-addr)

   :security {
              :hashing-algorithm :pbkdf2+sha256
              :cookie-name "zw"

              :jwt {
                    :claims {
                             :iss "zwitscher"
                             :aud "zwitscher.herokuapp.com"
                             }

                    :key (env :jwt-key)

                    :ttl (Integer/parseInt (env :jwt-ttl))
                    }
              }

   :database (env :database-url)
   :redis {
           :url (env :redis-url)
           :prefix (env :redis-prefix)
           }

   :dev-mode (= "development" (env :zwitscher-env))
   })

;; The exported database state
(defstate config
  :start (create-configuration))

(defn
  is-dev
  "Determines if the application is running in development mode"
  { :added "0.1.0" }
  []
  (config :dev-mode))
