(ns backstage-labels.config)

(def debug?
  ^boolean js/goog.DEBUG)

(def templates {:master-pack "Master Pack"
                :master-pack-thin "Master Pack Thin"
                :overstock "Overstock"
                :physical-inventory "Physical Inventory"
                :shelf "Shelf"})

(def default-print-option-template
  :master-pack)

(def releases-uri
  "https://api.github.com/repos/angeloashmore/boh-labels-db/releases")

(def downloads-uri
  "https://github.com/angeloashmore/boh-labels-db/releases/download/")
