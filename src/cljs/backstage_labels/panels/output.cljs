(ns backstage-labels.panels.output
  (:require [re-frame.core :as re-frame]))

(defn item
  [index [id qty]]
  [:li {:key index}
   id])

(defn main
  []
  (let [queue (re-frame/subscribe [:queue])]
    [:ul
     (map-indexed item @queue)]))
