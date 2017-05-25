(ns backstage-labels.components.print-options-bar
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
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".label" {:color (:text--secondary config/theme)
             :font-size (units/px 12)
             :margin-right (units/px 10)}]

  [".select" {:background-color (:background--field config/theme)
              :background-image "url(./images/dropdown.svg)";
              :background-position [["right" (units/px 8) "center"]]
              :background-repeat "no-repeat"
              :border 0
              :border-radius (units/px 4)
              :box-shadow [[0 0 0 (units/px 0.5) (color/rgba 0 0 0 0.07)]
                           [0 (units/px 0.5) (units/px 0.5) (color/rgba 0 0 0 0.2)]]
              :display "flex"
              :flex "1 1 auto"
              :font-size (units/px 12)
              :padding-bottom (units/px 4)
              :padding-left (units/px 8)
              :padding-right (units/px 8)
              :padding-top (units/px 4)
              :outline 0}])

(defn option
  "Option for select element."
  [[key name]]
  [:option {:key key} name])

(defn main
  "Displays print options."
  []
  (let [templates                 config/templates
        template                  (re-frame/subscribe [:print-option-template])
        set-print-option-template #(re-frame/dispatch [:set-print-option-template %])]
    [:div {:class (:container style)}
     ;; Label
     [:div {:class (:label style)} "Template:"]

     ;; Select
     [:select {:class (:select style)}
      (map option templates)]]))
