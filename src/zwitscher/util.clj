;; util.clj - (mostly useful) utility functions
(ns zwitscher.util
  (:require [clj-time.format :as tf])
  (:import [java.util UUID]))

(defn
  to-uuid
  "Converts the given string to a Java UUID"
  {:added "0.1.0"}
  [str]
  (UUID/fromString str))

(defn
  format-date-time
  "Formats a given clj-time object"
  {:added "0.1.0"}
  [obj]
  (tf/unparse (tf/formatter "YYYY-MM-dd HH:mm:ss") obj))
