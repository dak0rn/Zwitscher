;; navigation.clj - navigation views for the application
(ns zwitscher.views.partials.navigation)

(def links [
            { :url "/" :title "Home" }
            ])

(defn
  navigation
  "Returns the navigation for the recruiter"
  { :added "0.1.0" }
  []
  [:nav.nav
   [:div.nav-left
    [:a.nav-item.zwitscher {:href "/"} "Zwitscher"]
    ]

   [:span.nav-toggle [:span] [:span] [:span]]

   [:div.nav-right.nav-menu
    [:nav-item]
    ]
   ]
  )
