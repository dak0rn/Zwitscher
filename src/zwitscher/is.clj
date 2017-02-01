;; is.clj - validation namespace
(ns zwitscher.is)

(defmacro
  defn-truthy-is
  "Defines a test that checks whether the given value is truthy and then
   tests the passed tests"
  { :added "0.1.0" }
  [ name docstr attr-map params body ]
  (let [ param (first params) ]
    `(defn ~name
       ~docstr
       ~attr-map
       [ ~param ]
       (and (truthy? ~param) ~body))))

(defn
  falsey?
  "Determines if the given value is falsey, that is, it is neither false nor nil"
  { :added "0.1.0" }
  [ what ]
  (or (nil? what) (false? what)))

(defn
  truthy?
  "Determines if the given value is truthy, that is it is not falsy (wohoo)"
  { :added "0.1.0" }
  [ what ]
  (not (falsey? what)))

(defn-truthy-is
  password?
  "Determines if the given value is a more or less secure password"
  {:added "0.1.0"}
  [what]
  (and (not (empty? what)) (>= 8 (count what))))

(def ^:private username-regex #"[a-zA-Z0-9]")

(defn-truthy-is
  username?
  "Determines if the given value is a valid user name"
  {:added "0.1.0"}
  [what]
  (->> what
       (re-matches username-regex)
       nil?
       not))
