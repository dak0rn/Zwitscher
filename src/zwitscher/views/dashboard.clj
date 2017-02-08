;; dashboard.clj - The current user's dashboard
(ns zwitscher.views.dashboard
  (:require [hiccup.core :refer [h]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hiccup.form :refer [form-to]]
            [zwitscher.views.partials.tweet :refer [render-tweet]]
            [zwitscher.views.stream :refer [s-pluralize]]
            [zwitscher.views.partials.document :refer [document]]
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
  render-dashboard
  "Renders the given user's dashboard"
  {:added "0.1.0"}
  [user & {:keys [tweets]}]
  (let [follower-count (:follower_count user)
        tweet-count (:tweet_count user)]
    (document {:title "Zwitscher" :body-class "dashboard" :scripts ["dashboard"]}
              (navigation)
              [:main.dashboard-container
               [:div.columns

                [:div.column.is-8
                 (form-to ["POST" "/say"]
                          (anti-forgery-field)
                          [:textarea.textarea {:placeholder "Say something..."
                                               :required true
                                               :name "text"
                                               :id "say-box"}]
                          [:span#count ]
                          [:button.button.is-primary.pull-right {:id "submit"}
                           [:span.fa.fa-send]
                           ]
                          [:div.is-clearfix]
                          )

                 [:div.tweet-stream.mt-15
                  (doall
                   (map (render-tweet user) tweets))
                  [:hr]
                  ]
                 ]

                [:div.column.is-4
                 [:div.card
                  [:div.card-content
                   [:div.media
                    [:div.media-content
                     [:p.title.is-4
                      [:a {:href (str "/@" (h (:name user)))} (->> user :name h (str "@"))]
                      ]
                     [:p.title.is-6 (str (h follower-count) " " (s-pluralize "follower" follower-count))]
                     [:p.title.is-6 (str (h tweet-count) " " (s-pluralize "tweet" tweet-count))]
                     ]
                    ]
                   ]
                  ]

                 [:div.panel.dashboard-panel
                  (render-sidelink "Settings" "/settings" "cog")
                  (render-sidelink "Following" "/following" "users")
                  ]
                 ]

                ]
               ]))
  )
