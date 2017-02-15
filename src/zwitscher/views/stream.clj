;; stream.clj - Stream for a user
(ns zwitscher.views.stream
  (:require [hiccup.core :refer [h]]
            [zwitscher.views.partials.tweet :refer [render-tweet]]
            [zwitscher.views.partials.document :refer [document]]
            [hiccup.form :refer [form-to]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [zwitscher.views.partials.navigation :refer [navigation]]))

(defn
  s-pluralize
  "Given a noun whose plural is built by adding 's', returns the correct
   for the given count."
  {:added "0.1.0"}
  [noun count]
  (if (= 1 count)
    noun
    (str noun \s)))

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
  render-follower
  "Renders a given follower"
  {:added "0.1.0"}
  [follower]
  (let [at-name (str "@" (-> follower :name h))
        followers (:follower_count follower)
        tweets (:tweet_count follower)]
    [:div.box.follower-box
     [:p.title.is-6
      [:a {:href (str "/" at-name)} at-name]]

     [:div.follower-stats
      [:span (str (h followers) " " (s-pluralize "follower" followers)) ]
      [:span (str (h tweets) " " (s-pluralize "tweet" tweets))]
      ]
     ]))

(defn
  render-stream
  "Renders the stream of a user"
  {:added "0.1.0"}
  [user session & {:keys [tweets follows]}]
  (let [name (-> user :name h)
        follower-count (:follower_count user)
        tweet-count (:tweet_count user)]
    (document {:title (str "@" name) :body-class "dashboard"}
              (navigation)
              [:main.dashboard-container
               [:div.columns

                [:div.column.is-8
                 [:h4.title.is-4 (str "All tweets from @" name)]
                 [:div.tweet-stream
                  (doall
                   (map (render-tweet session) tweets))
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
                     [:p.title.is-6 (str (h follower-count) " " (s-pluralize "follower" follower-count))]
                     [:p.title.is-6 (str (h tweet-count) " " (s-pluralize "tweet" tweet-count))]
                     ]
                    ]
                   ]
                  ]

                 [:div.panel.dashboard-panel
                  (render-sidelink "Followers" (str "/@" name "/followers") "users")
                  (if follows
                    (render-sidelink "Unfollow" (str "/u/" (:iduser user) ) "unlink")
                    (render-sidelink "Follow" (str "/f/" (:iduser user) ) "link"))
                  ]
                 ]

                ]
               ]))
  )

(defn
  render-followers
  "Renders the followers for the given user"
  {:added "0.1.0"}
  [user followers & {:keys [follows]}]
  (let [name (-> user :name h)
        at-name (str "@" name)
        follower-count (:follower_count user)
        tweet-count (:tweet_count user)]
    (document {:title (str at-name "'s followers")}
              (navigation)
              [:main.dashboard-container
               [:div.columns

                [:div.column.is-8
                 [:h4.title.is-4 (str "People following @" name)]
                 [:a.button {:href (str "/" at-name)} (str "&larr; Back to " at-name)]
                 [:div.follower-stream
                  (doall
                   (map render-follower followers))
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
                     [:p.title.is-6 (str (h follower-count) " " (s-pluralize "follower" follower-count))]
                     [:p.title.is-6 (str (h tweet-count) " " (s-pluralize "tweet" tweet-count))]
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
               ]
              )))
