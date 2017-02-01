;; profiles.clj - profiles used by environ during the development
{
  :dev {
    :env {
      :port "22345"
      :listen-addr "0.0.0.0"
      :jwt-key "8901274h1FV)!@#LK!@#JK !#)()_kljlkFSAL:DFK:lijopi0gjoi4n@#$c97890" ;; This key is only for development, btw.
      :jwt-ttl "604800" ;; that's a week
      :database-url "postgres://zwitscher:password@localhost:5432/zwitscher"
      :zwitscher-env "development"
      :redis-url "redis://127.0.0.1:6379"
      :redis-prefix "zwitscher:"
    }
  }
}
