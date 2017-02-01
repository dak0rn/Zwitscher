;; security.clj - security services
(ns zwitscher.services.security
  (:require [buddy.sign.jwt :as jwt]
            [buddy.hashers :as hashers]
            [clj-time.core :as time]
            [zwitscher.config :refer [config]]))

(defn
  encrypt
  "Encrypts the given plain text using the configuration hashing algorithm"
  { :added "0.1.0" }
  [ input ]
  (hashers/derive input { :alg (get-in config [:security :hashing-algorithm]) }))

(defn
  verify
  "Verifies the given plain text with the given encrypted text"
  { :added "0.1.0" }
  [ plain encrypted ]
  (hashers/check plain encrypted))

(defn
  create-jwt
  "Creates a jwt from the given optional claims. They are merged with the default
   claimes defined in the configuration."
  { :added "0.1.0" }

  ;; w/out additional claims
  ([] (create-jwt {}))

  ;; w/ additional claims
  ([ claims ]
   (let [ jwt-config (get-in config [:security :jwt])
          ttl (:ttl jwt-config)
          ;; Expiration timestamp
          exp (time/plus (time/now) (time/seconds ttl))
          ;; Merge claims with default claims from the config
          all-claims (merge claims { :exp exp :rem ttl } (:claims jwt-config)) ]
     (jwt/sign all-claims (:key jwt-config)))))

(defn
  get-claims
  "Validates the given jwt and returns its claims if it is valid"
  { :added "0.1.0" }
  [ token ]
  (let [ enckey (get-in config [:security :jwt :key])  ]
    (jwt/unsign token enckey)))


(defn
  add-jwt-header
  "Adds the given jwt to the header configured in the config in the given
   ring response map"
  { :added "0.1.0" }
  [ response token ]
  (when response
    (let [ header-name (get-in config [:security :cookie-name]) ]
      (assoc-in response [ :cookies header-name ] { :value token :http-only true :path "/" }))))

(defn
  remove-jwt-header
  "Removes the JWT header from the given response"
  { :added "0.1.0" }
  [ response ]
  (when response
    (let [ header-name (get-in config [:security :cookie-name]) ]
      (assoc-in response [ :cookies header-name ] { :value "" :http-only true :max-age -1 }))))

