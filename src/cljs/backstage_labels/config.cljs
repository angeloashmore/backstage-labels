(ns backstage-labels.config)

(def debug?
  ^boolean js/goog.DEBUG)

(def default-print-option-template
  :master-pack)

(def releases-uri
  "https://api.github.com/repos/angeloashmore/boh-labels-db/releases")

(def downloads-uri
  "https://github.com/angeloashmore/boh-labels-db/releases/download/")
