;; singup.clj - Signup view
(ns zwitscher.views.signup
  (:require [hiccup.core :refer [h]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hiccup.form :refer [form-to]]
            [zwitscher.views.partials.document :refer [document]]
            [zwitscher.views.partials.navigation :as nav]))

(defn
  render-signup
  "Renders the signup page"
  {:added "0.1.0"}
  [& {:keys [exists name-missing password-missing]}]
  (document {:title "Sign up for Zwitscher" :body-class "zwitscher-signin"}
            (nav/empty)
            [:div.flex-wrap
             [:main.container
              [:div.columns
               [:div.column.is-8.mt-2em

                [:h1.title "Sign up to get access" ]
                [:h2.subtitle "You won't regret it"]

                [:div.box
                 [:blockquote "The best place for all your thoughts"]
                 "(John Doe)"
                 ]

                [:div.box
                 [:blockquote "The product I always wanted to create"]
                 "(Jay Norris, CEO Lifeinvader)"
                 ]
                ]
               [:div.column.is-4
                [:div.panel.sign-panel
                 [:div.panel-heading
                  "Sign up for " [:span.zwitscher "Zwitscher"]
                  ]
                 (when (or exists name-missing password-missing)
                   [:div.panel-block

                    (when exists [:div.notification.is-warning
                                  [:a.delete {:href "/signup"} ]
                                  "There is already a user with that name. Please choose a different one"
                                  ])

                    (when name-missing [:div.notification.is-warning
                                        [:a.delete {:href "/signup"} ]
                                        "Please enter a name"
                                        ])

                    (when password-missing [:div.notification.is-warning
                                            [:a.delete {:href "/signup"} ]
                                            "Please enter a more or less secure password"
                                            ])

                    ])
                 [:div.panel-block
                  (form-to ["POST" "/signup/do"]
                           (anti-forgery-field)
                           [:label.label "Your username"]
                           [:div.control
                            [:input.input {:type "text"
                                           :placeholder ""
                                           :name "pref-name"
                                           :required true}]
                            ]
                           [:label.label "Your password"]
                           [:div.control
                            [:input.input {:type "password"
                                           :placeholder "**********"
                                           :name "pref-pass"
                                           :required true
                                           }]
                            ]
                           [:div.sign-bar
                            [:button.button.is-primary "Sign up"]
                            [:a.button.is-link.mr-15 {:href "/signin"} "Already have an account?"]
                            ]
                           [:div.is-clearfix])
                  ]
                 ]]]
              ]]
            ))
