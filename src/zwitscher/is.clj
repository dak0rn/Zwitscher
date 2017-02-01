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
