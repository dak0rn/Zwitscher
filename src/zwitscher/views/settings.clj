;; settings.clj - settings view
(ns zwitscher.views.settings
  (:require [hiccup.core :refer [h]]
            [zwitscher.views.partials.tweet :refer [render-tweet]]
            [zwitscher.views.partials.document :refer [document]]
            [hiccup.form :refer [form-to]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [zwitscher.views.partials.navigation :refer [navigation]]))

(defn
  render-settings
  "Renders the settings view"
  {:added "0.1.0"}
  [& {:keys [saved invalid]}]
  (document {:title "Settings"}
            (navigation)
            [:main.dashboard-container
             [:h4.title.is-4 "Settings"]
             [:a.button.mb-15 {:href "/"} "&larr; Back to my stream"]
             (form-to ["POST" "/settings"]
                      (anti-forgery-field)
                      [:div.box
                       [:p.title.is-5 "Change your password"]

                       (when saved
                         [:div.notification.is-success
                          [:a.delete {:href "/settings"}] "Your password has been changed"])

                       (when invalid
                         [:div.notification.is-warning
                          [:a.delete {:href "/settings"}]
                          "Please enter a (more or less) secure password and ensure the confirmation password matches"])

                       [:div.columns
                        [:div.column.is-6
                         [:input.input {:type "password"
                                        :name "p1"
                                        :required true
                                        :placeholder "New password"}]]
                        [:div.column.is-6
                         [:input.input {:type "password"
                                        :name "p2"
                                        :required true
                                        :placeholder "Confirmation"}]]]
                       [:button.button.is-primary.is-pulled-right "Save"]
                       [:div.is-clearfix]
                       ]
                      )
             ]))
