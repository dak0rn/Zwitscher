;; security.clj - security services
(ns zwitscher.services.security
  (:require [buddy.sign.jwt :as jwt]
            [buddy.hashers :as hashers]
            [zwitscher.services.redis :as redis]
            [clj-time.core :as time]
            [clj-time.coerce :as time-coerce]
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

(defn-
  get-jwt-ttl
  "Returns the JWT TTL as clj-time"
  {:added "0.1.0"}
  []
  (get-in config [:security :jwt :ttl]))

(defn
  create-jwt
  "Creates a jwt from the given optional claims. They are merged with the default
   claimes defined in the configuration."
  { :added "0.1.0" }

  ;; w/out additional claims
  ([] (create-jwt {}))

  ;; w/ additional claims
  ([ claims ]
   (let [jwt-config (get-in config [:security :jwt])
         ttl (get-jwt-ttl)
         ;; Expiration timestamp
         exp (time-coerce/to-long (time/plus (time/now) (time/seconds ttl)))
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


(defn
  issue-token!
  "Issues a token for the given user id. The token will be stored in
   redis"
  {:added "0.1.0"}
  [userid]
  (let [token (create-jwt {:jti userid})]
    (redis/set token token :ex (get-jwt-ttl))
    token))

(defn
  reissue-token!
  "Revokes the given token and issues a new one for the given user id"
  {:added "0.1.0"}
  [token userid]
  (redis/del token)
  (issue-token! userid))

(defn
  token-valid?
  "Determines if the given token is valid, that is, if it is (still)
   stored in redis"
  {:added "0.1.0"}
  [token]
  (if (or (nil? token) (empty? token))
    false
    (-> token
        redis/get
        nil?
        not)))

(defn
  invalidate-token!
  "Invalides the given token by removing it from redis"
  {:added "0.1.0"}
  [token]
  (redis/del token))
