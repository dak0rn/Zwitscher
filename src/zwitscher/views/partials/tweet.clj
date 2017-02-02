;; tweet.clj - renderer for a tweet
(ns zwitscher.views.partials.tweet
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hiccup.form :refer [form-to]]
            [hiccup.core :refer [h]]
            [zwitscher.util :refer [format-date-time]]))

(defn
  render-tweet
  "Renders a given tweet"
  {:added "0.1.0"}
  [tweet]
  (let [name (-> tweet :name h)
        liked (:liked tweet)]
    [:div.box.tweet-box
     [:p.title.is-5
      [:a {:href (str "/@" name)} (str "@" name)]
      [:time (format-date-time (:ts tweet))]
      ]

     [:p (-> tweet :text h)]

     [:div.box-actions
      (cond
        (= false liked) (form-to ["POST" "/like"]
                                 (anti-forgery-field)
                                 [:input {:type "hidden" :value (:idtweet tweet) :name "tweet"}]
                                 [:button.button.is-link
                                  [:span.fa.fa-heart-o]])
        (= true liked) (form-to ["POST" "/dislike"]
                                 (anti-forgery-field)
                                 [:input {:type "hidden" :value (:idtweet tweet) :name "tweet"}]
                                 [:button.button.is-link
                                  [:span.fa.fa-heart]])

        :else (form-to ["POST" "/delete"]
                       (anti-forgery-field)
                       [:input {:type "hidden" :value (:idtweet tweet) :name "tweet"}]
                       [:button.button.is-link
                        [:span.fa.fa-trash-o]])
        )
      ]
     ]))
