(defproject zwitscher "0.1.0"
  :description "The next big social network"
  :url ""
  :main zwitscher.core
  :uberjar-name "zwitscher.jar"
  :profiles {:uberjar {:aot :all}}
  :min-lein-version "2.0.0"

  :resource-paths ["resources"]

  :dependencies [
    [org.clojure/clojure "1.8.0"]

    ;; Postgresql connection
    [org.postgresql/postgresql "9.4.1211"]

    ;; SQL library
    [com.layerware/hugsql "0.4.7"]

    ;; State management for code dependencies
    [mount "0.1.10"]

    ;; Hashing, encrypting and more
    [buddy "1.0.0"]

    ;; Route handling
    [ring "1.5.0"]
    [compojure "1.5.1"]
    [ring/ring-defaults "0.2.1"]
    [ring/ring-jetty-adapter "1.5.0"]

    ;; HTML rendering
    [hiccup "1.0.5"]

    ;; Stuff with time
    [clj-time "0.12.0"]

    ;; Working with the environment
    [environ "1.1.0"]

    ;; Migrations and seeding
    [ragtime "0.6.3"]

    ;; redis clietn
    [com.taoensso/carmine "2.15.0"]

    [com.novemberain/validateur "2.5.0"]
  ]

  :plugins [
    [lein-ring "0.9.7"]
    [lein-environ "1.1.0"]
  ]

  :ring {
    :handler zwitscher.routes/routes
    :init zwitscher.core/boot
    :port 22345
    :host "0.0.0.0"

    :nrepl {
      :start? true
      :port 9987
    }
  }

  :aliases {
    "migrate" ["run" "-m" "zwitscher.bin/migrate"]
    "demigrate" ["run" "-m" "zwitscher.bin/demigrate"]
    "add-migration" ["run" "-m" "zwitscher.bin/add-migration"]
    "seed" ["run" "-m" "zwitscher.bin/seed"]
  }
)
