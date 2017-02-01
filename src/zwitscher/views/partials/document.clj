;; document.clj - document definition for routes
(ns zwitscher.views.partials.document
  (:require [hiccup.core :refer [h html]]
            [hiccup.page :refer [include-css include-js]]))

;; Mapping of script names to urls
(def ^:private scripts {
})

;; Additional stylesheets
(def ^:private stylesheets {
})

(defn
  document
  "Renders the required structure for a casual HTML page
   Expects a map with a title, stylesheets and scripts. All of them
   are optional.
   The following keys are supported in the options map:

   - :scripts    Vector of additional scripts (see def script)
   - :stylesheets Vector of additional stylesheets (see def stylesheets)"
  { :added "0.1.0" }
  [ options & children ]
  (let [ scriptfiles (or (:scripts options) [])
        styles (or (:stylesheets options) [])]
    (html
     [ :html
      [ :head
       [ :title (h (:title options)) ]
       [ :meta { :name "viewport" :content "width=device-width, initial-scale=1.0" } ]
       (include-css "/font-awesome.min.css")
       (when (> (count scriptfiles) 0)
         (include-js "/scripts/feel.js?ver=0.9"))
       (for [ script scriptfiles ]
         (include-js (get scripts script)))
       (for [ style styles ]
         (include-css (get stylesheets style)))
       ]
      [ :body children ]])))
