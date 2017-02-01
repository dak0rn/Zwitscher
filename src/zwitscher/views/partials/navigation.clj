;; navigation.clj - navigation views for the application
(ns gw.web.views.partials.navigation)

(def links [
            { :url "/dashboard" :title "Dashboard" }
            { :url "/reports" :title "Reports" }
            { :url "/settings" :title "Settings" }
            ])

(defn
  navigation
  "Returns the navigation for the recruiter"
  { :added "0.1.0" }
  []
  [ :nav.navbar
   [ :section.navbar-section.top-nav
    [ :a.navbar-brand { :href "/dashboard" } "github-weekly" ]
    [ :label.navbar-toggle { :for "toggle-state" }
     [ :span ]
     [ :span ]
     [ :span ]
    ]
    [ :input#toggle-state { :type "checkbox" } ]
    [ :div.navbar-contents
     (for [ link links ]
       [ :a.btn.btn-link { :href (:url link) } (:title link) ])
     ]
    ]
   ]
  )
