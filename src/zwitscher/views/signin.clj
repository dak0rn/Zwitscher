;; signin.clj - Signin route
(ns zwitscher.views.signin
  (:require [hiccup.core :refer [h]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hiccup.form :refer [form-to]]
            [zwitscher.views.partials.document :refer [document]]
            [zwitscher.views.partials.navigation :as nav]))

(defn
  render-signin
  "Renders the sign in page"
  {:added "0.1.0"}
  [& {:keys [invalid name-missing password-missing]}]
  (document {:title "Sign in to Zwitscher"}
            (nav/empty)
            [:main.container
             [:div.panel.sign-panel
              [:div.panel-heading
               "Sign in to " [:span.zwitscher "Zwitscher"]
               ]
              (when (or invalid name-missing password-missing)
                [:div.panel-block

                 (when invalid [:div.notification.is-warning
                               [:a.delete {:href "/signin"} ]
                               "Your credentials must have been invalid. Please try again."
                               ])

                 (when name-missing [:div.notification.is-warning
                                     [:a.delete {:href "/signin"} ]
                                     "Please enter your user name"
                                     ])

                 (when password-missing [:div.notification.is-warning
                                         [:a.delete {:href "/signin"} ]
                                         "Please enter your password"
                                         ])

                 ])
              [:div.panel-block
               (form-to ["POST" "/signin/do"]
                        (anti-forgery-field)
                        [:label.label "Your username"]
                        [:div.control
                         [:input.input {:type "text"
                                        :placeholder ""
                                        :name "dat-name"
                                        :required true}]
                         ]
                        [:label.label "Your password"]
                        [:div.control
                         [:input.input {:type "password"
                                        :placeholder "**********"
                                        :name "dat-pass"
                                        :required true
                                        }]
                         ]
                        [:div.sign-bar
                         [:button.button.is-primary "Sign in"]
                         [:a.button.is-link.mr-15 {:href "/signup"} "Don't have an account yet?"]
                         ]
                        [:div.is-clearfix])
               ]
              ]
             ]
            ))
