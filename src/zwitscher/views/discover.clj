;; discover.clj - list of all registered users
(ns zwitscher.views.discover
  (:require [hiccup.core :refer [h]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hiccup.form :refer [form-to]]
            [zwitscher.views.stream :refer [s-pluralize]]
            [zwitscher.views.partials.document :refer [document]]
            [zwitscher.views.stream :refer [s-pluralize]]
            [zwitscher.views.partials.navigation :refer [navigation]]))

(defn-
  render-user
  "Renders a given user"
  {:added "0.1.0"}
  [user]
  (let [name (str "@" (-> user :name h))]
    [:div.box.user-box
     [:div.level
      [:div.level-item.has-text-left [:p.title.is-5 [:a {:href (str "/" name)} name]]]
      [:div.level-item.has-text-right
       (let [followers (:follower_count user)]
         [:span.follower-count (h followers) (s-pluralize " follower" followers)])
       (let [tweets (:tweet_count user)]
         [:span (h tweets) (s-pluralize " tweet" tweets)])
       ]
      ]
     ]))


(defn
  render-discover
  "Renders the discover page with a given list of users.
   The given user is exluded from the list"
  {:added "0.1.0"}
  [users current-user]
  (let [current-user-name (:name current-user)]
    (document {:title "Discover"}
              (navigation)
              [:main.dashboard-container
               [:h4.title.is-4 "Discover who to follow"]
               [:div.user-list
                (doall
                 (->> users
                      (filter #(not= current-user-name (:name %)))
                      (map render-user)
                      ))
                ]
               ])))
