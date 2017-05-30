(ns backstage-labels.components.form
  (:require [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.config :as config]))

(defstyle style
  [".select" {:background-color (:background--field config/theme)
              :background-image "url(./images/dropdown.svg)";
              :background-position [["right" (units/px 8) "center"]]
              :background-repeat "no-repeat"
              :border 0
              :border-radius (units/px 4)
              :box-shadow [[0 0 0 (units/px 0.5) (color/rgba 0 0 0 0.07)]
                           [0 (units/px 0.5) (units/px 0.5) (color/rgba 0 0 0 0.2)]]
              :font-size (units/px 12)
              :padding-bottom (units/px 4)
              :padding-left (units/px 8)
              :padding-right (units/px+ (units/px 8) (units/px 9) (units/px 8))
              :padding-top (units/px 4)
              :outline 0}]

  [".label" {:color (:text--secondary config/theme)
             :font-size (units/px 12)
             :margin-right (units/px 10)}])

(defn select
  "Styled select component."
  [{:keys [class on-change]} & children]
  [:select {:class (garden.util/space-join [(:select style) class])
            :on-change on-change}
   children])

(defn label
  "Styled label component."
  ([{:keys [class]} & children]
   [:label {:class (garden.util/space-join [(:label style) class])}
    children])
  ([& children]
   [:label {:class (:label style)}
    children]))
