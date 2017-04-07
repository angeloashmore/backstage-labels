(ns backstage-labels.config)

(def debug?
  ^boolean js/goog.DEBUG)

(def default-print-option-template
  :master-pack)

(def db-release-uri
  "https://api.github.com/repos/angeloashmore/boh-labels-db/releases/latest")
