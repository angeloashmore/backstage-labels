(ns backstage-labels.config
  (:require [garden.color :as color]))

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

(def theme {:background            (color/hsl 0 0 100)
            :background--secondary (color/hsl 0 0 95)
            :background--field     (color/hsl 0 0 100)
            :text                  (color/hsl 0 0 0)
            :text--secondary       (color/hsl 0 0 60)
            :tint                  (color/hsl 211 100 50)
            :tint--active          (color/hsl 211 100 40)})
