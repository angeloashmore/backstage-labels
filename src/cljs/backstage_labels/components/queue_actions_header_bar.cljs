(ns backstage-labels.components.queue-actions-header-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :as css-modules]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.config :as config]))

(css-modules/defstyle style
  [".container" {:align-items "center"
                 :background-color (:background--secondary config/theme)
                 :box-shadow [["inset" 0 (units/px -0.5) 0 (color/rgba 0 0 0 0.15)]]
                 :display "flex"
                 :flex "none"
                 :height (units/px 56)
                 :justify-content "space-between"
                 :padding (units/px 15)}]

  [".count" {:color (:text--secondary config/theme)
             :font-size (units/px 14)
             :font-weight 200}]

  [".button" {:background-color (:background--field config/theme)
              :border-style "none"
              :border-radius (units/px 9999)
              :border-width 0
              :box-shadow [[0 0 0 (units/px 0.5) (color/rgba 0 0 0 0.07)]
                           [0 (units/px 0.5) (units/px 0.5) (color/rgba 0 0 0 0.2)]]
              :color (:tint config/theme)
              :font-size (units/px 14)
              :font-weight 600
              :outline 0
              :padding [[(units/px 5) (units/px 14)]]}
   ["&:active" {:color (:tint--active config/theme)}]])

(defn main
  "Displays queue count and print button."
  []
  (let [queue-count (re-frame/subscribe [:queue-count])]
    [:div {:class (:container style)}
     ;; Count
     [:div {:class (:count style)}
      (str @queue-count " label" (when (not= @queue-count 1) "s"))]

     ;; Print button
     [:button {:class (:button style)}
      "Print"]]))
