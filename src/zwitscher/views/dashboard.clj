;; dashboard.clj - The current user's dashboard
(ns zwitscher.views.dashboard
  (:require [hiccup.core :refer [h]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hiccup.form :refer [form-to]]
            [zwitscher.util :refer [format-date-time]]
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

(defn-
  render-tweet
  "Renders a given tweet"
  {:added "0.1.0"}
  [tweet]
  (let [name (-> tweet :name h)]
    [:div.box.tweet-box
     [:p.title.is-5
      [:a {:href (str "/" name)} (str "@" name)]
      [:time (format-date-time (:ts tweet))]
      ]

     [:p (-> tweet :text h)]

     [:div.box-actions
      (form-to ["POST" "/like"]
               (anti-forgery-field)
               [:input {:type "hidden" :value (:idtweet tweet) :name "tweet"}]
               [:button.button.is-link
                [:span.fa.fa-heart-o]])
      ]
     ]))

(defn
  render-dashboard
  "Renders the given user's dashboard"
  {:added "0.1.0"}
  [user & {:keys [tweets]}]
  (document {:title "Zwitscher" :body-class "dashboard" :scripts ["dashboard"]}
            (navigation)
            [:main.dashboard-container
             [:div.columns

              [:div.column.is-8
               (form-to ["POST" "/say"]
                        (anti-forgery-field)
                        [:textarea.textarea {:placeholder "Say something..."
                                             :required true
                                             :id "say-box"}]
                        [:span#count ]
                        [:button.button.is-primary.pull-right {:id "submit"}
                         [:span.fa.fa-send]
                         ]
                        [:div.is-clearfix]
                        )

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
                    [:a {:href (str "/" (h (:name user)))} (->> user :name h (str "@"))]
                    ]
                   [:p.title.is-6 (str (:follower_count user) " followers")]
                   [:p.title.is-6 (str (:tweet_count user) " tweets")]
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
             ])
  )
