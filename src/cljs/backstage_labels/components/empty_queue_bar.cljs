(ns backstage-labels.components.empty-queue-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [backstage-labels.config :as config]))

(defstyle style
  [".container" {:align-items "center"
                 :background-color (:background--secondary config/theme)
                 :display "flex"
                 :flex "none"
                 :justify-content "flex-end"
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".button" {:color (:tint config/theme)
              :display "block"
              :font-size (units/px 12)}
   ["&:active" {:color (:tint--active config/theme)}]])

(defn main
  "Displays empty queue button."
  []
  (let [empty-queue #(re-frame/dispatch [:empty-queue])]
    [:div {:class (:container style)}
     [:a {:class (:button style)
          :on-click empty-queue}
      "Clear All"]]))
