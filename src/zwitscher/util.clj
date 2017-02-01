;; util.clj - (mostly useful) utility functions
(ns zwitscher.util
  (:import [java.util UUID]))

(defn
  to-uuid
  "Converts the given string to a Java UUID"
  {:added "0.1.0"}
  [str]
  (UUID/fromString str))
