(ns backstage-labels.components.queue-all-filtered-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.config :as config]))

(defstyle style
  [".container" {:align-items "center"
                 :background-color (:background--secondary config/theme)
                 :box-shadow [["inset" (units/px -0.5) 0 0 (color/rgba 0 0 0 0.15)]]
                 :display "flex"
                 :flex "none"
                 :justify-content "flex-end"
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".button" {:color (:tint config/theme)
              :display "block"
              :font-size (units/px 12)}
   ["&:active" {:color (:tint--active config/theme)}]])

(defn main
  "Displays queue all filtered labels button."
  []
  (let [labels    (re-frame/subscribe [:labels-filtered])
        ids       (keys @labels)
        queue-all #(re-frame/dispatch [:queue-labels ids])]
    [:div {:class (:container style)}
     [:a {:class (:button style)
          :on-click queue-all}
      "Add All Visible"]]))
