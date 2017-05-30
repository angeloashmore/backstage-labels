(ns backstage-labels.config
  (:require [garden.color :as color]))

;; Boolean set using compiler debug flag.
(def debug?
  ^boolean js/goog.DEBUG)

;; Available templates.
(def templates {:master-pack "Master Pack"
                :master-pack-thin "Master Pack Thin"
                :overstock "Overstock"
                :physical-inventory "Physical Inventory"
                :shelf "Shelf"})

;; Default template.
(def default-print-option-template
  :master-pack)

;; URI used to fetch release data.
(def releases-uri
  "https://api.github.com/repos/angeloashmore/boh-labels-db/releases")

;; URI used to fetch release downloads.
(def downloads-uri
  "https://github.com/angeloashmore/boh-labels-db/releases/download/")

;; ID used for the filter query input element.
(def filter-query-id
  "filter-query")

;; App theme information.
(def theme {:background            (color/hsl 0 0 100)
            :background--secondary (color/hsl 0 0 95)
            :background--field     (color/hsl 0 0 100)
            :text                  (color/hsl 0 0 0)
            :text--secondary       (color/hsl 0 0 60)
            :tint                  (color/hsl 211 100 50)
            :tint--active          (color/hsl 211 100 40)})
