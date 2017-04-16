(ns backstage-labels.components.queue-bar
  (:require [reagent.format :as format :refer [pluralize]]))

(defn main
  [{:keys [queue]}]
  (let [qty (count queue)]
    [:div {:class-name "flex items-center justify-between h3 pa3 bg-light-gray"
           :style {;;:height "3.7rem"
                   :box-shadow "0 0.5px 0 rgba(0, 0, 0, 0.15)"}}
     ;; Quantity
     [:div {:class-name "f6 fw2 gray"}
      (format/pluralize queue "label")]

     ;; Print button
     [:button {:class-name "outline-0 bn pa2 ph3 f6 fw5 blue bg-white br-pill shadow-1"
               :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
      "Print"]]))
