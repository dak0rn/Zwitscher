;; stream.clj - Stream for a user
(ns zwitscher.views.stream
  (:require [hiccup.core :refer [h]]
            [zwitscher.views.partials.tweet :refer [render-tweet]]
            [zwitscher.views.partials.document :refer [document]]
            [hiccup.form :refer [form-to]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [zwitscher.views.partials.navigation :refer [navigation]]))

(defn-
  render-sidelink
  "Renders a link in the sidebar panel"
  {:added "0.1.0"}
  [title url icon]
  [:a.panel-block {:href url}
   [:span.panel-icon
    [:span {:class (str "fa fa-" icon)}]]
   (h title)
   ])

(defn
  render-stream
  "Renders the stream of a user"
  {:added "0.1.0"}
  [user & {:keys [tweets follows]}]
  (let [name (-> user :name h)]
    (document {:title (str "@" name) :body-class "dashboard"}
              (navigation)
              [:main.dashboard-container
               [:div.columns

                [:div.column.is-8
                 [:h4.title.is-4 (str "All tweets from @" name)]
                 [:div.tweet-stream
                  (doall
                   (map render-tweet tweets))
                  [:hr]
                  ]
                 ]

                [:div.column.is-4
                 [:div.card
                  [:div.card-content
                   [:div.media
                    [:div.media-content
                     [:p.title.is-4
                      [:a {:href (str "/@" name)} (str "@" name)]
                      ]
                     [:p.title.is-6 (str (:follower_count user) " followers")]
                     [:p.title.is-6 (str (:tweet_count user) " tweets")]
                     ]
                    ]
                   ]
                  ]

                 [:div.panel.dashboard-panel
                  (render-sidelink "Followers" (str "/@" name "/followers") "users")
                  (if follows
                    (render-sidelink "Unfollow" (str "/" (:iduser user) "/u") "unlink")
                    (render-sidelink "Follow" (str "/" (:iduser user) "/f") "link"))
                  ]
                 ]

                ]
               ]))
  )
