(ns backstage-labels.components.labels-list
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.components.label :as label]))

(defstyle style
  [".container" {:box-shadow [["inset" 0 (units/px -0.5) 0 (color/rgba 0 0 0 0.15)]
                              ["inset" (units/px -0.5) 0 0 (color/rgba 0 0 0 0.15)]]
                 :list-style-type "none"
                 :margin 0
                 :overflow-y "scroll"
                 :overflow-x "hidden"
                 :padding 0}]

  [".accessory-left" {:margin-left (units/px+ (units/px 1.5) (units/px 4.5))
                      :margin-right (units/px 4.5)}]

  [".accessory-right" {:align-items "center"
                       :display "flex"
                       :flex "none"
                       :justify-content "center"}])

(defn accessory-left
  "Left accessory for label item"
  [_]
  [:img {:class (:accessory-left style)
         :src "./images/label.svg"}])

(defn accessory-right
  "Right accessory for label item"
  [id]
  (let [queue-label #(re-frame/dispatch [:queue-label id])]
    (fn [_]
      [:div {:class (:accessory-right style)
             :on-click queue-label}
       [:img {:src "./images/add.svg"}]])))

(defn item
  "Label item for labels list."
  [[id label]]
  [:li {:key id}
   [label/main {:label label
                :accessory-left  accessory-left
                :accessory-right (accessory-right id)
                :on-double-click #(re-frame/dispatch [:queue-label id])}]])

(defn main
  "List of labels."
  []
  (let [filtered-labels (re-frame/subscribe [:labels-filtered])]
    [:ul {:class (:container style)}
     (map item @filtered-labels)]))
