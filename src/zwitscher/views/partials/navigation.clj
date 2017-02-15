;; navigation.clj - navigation views for the application
(ns zwitscher.views.partials.navigation)

(def links [
            { :url "/" :title "Home" }
            ])

(defn
  navigation
  "Returns the navigation"
  { :added "0.1.0" }
  []
  [:nav.nav
   [:div.nav-left
    [:a.nav-item.zwitscher {:href "/"} "Zwitscher"]
    ]

   [:label.nav-toggle {:for "nav-state"} [:span] [:span] [:span]]
   [:input#nav-state {:type "checkbox"}]

   [:div.nav-right.nav-menu
    [:a.nav-item.discover-link {:href "/discover"} "Discover" ]
    [:div.nav-item [:a {:href "/"} "Home"]
     ]
    [:a.nav-item {:href "/signout"} "Sign out"]
    ]
   ]
  )

(defn
  empty
  "Returns the empty navigation for non-authenticated pages"
  { :added "0.1.0" }
  []
  [:nav.nav
   [:div.nav-left
    [:a.nav-item.zwitscher {:href "/"} "Zwitscher"]
    ]
   ]
  )
