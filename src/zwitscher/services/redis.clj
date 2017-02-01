;; redis.clj - redis connection
(ns zwitscher.services.redis
  (:require [zwitscher.config :refer [config]]
            [taoensso.carmine :as car]))

(defmacro
  redis
  "Executes a number of given redis commands using the configured
   configuration"
  { :added "0.1.0" }
  [ & commands ]
  `(car/wcar { :spec { :uri (get-in config [ :redis :url ]) } } ~@commands))

;; Redis functions with injected prefix

(defmacro
  set
  "Sets a given value in redis"
  { :added "0.1.0" }
  [ key & rest ]
  `(redis (car/set (str (get-in config [ :redis :prefix ]) ~key) ~@rest)))

(defmacro
  get
  "Gets a value from redis"
  { :added "0.1.0" }
  [ key  ]
  `(redis (car/get (str (get-in config [ :redis :prefix ]) ~key))))

(defmacro
  del
  "Delete a value"
  { :added "0.1.0" }
  [ key & rest ]
  `(redis (car/del (str (get-in config [ :redis :prefix ]) ~key) ~@rest)))
