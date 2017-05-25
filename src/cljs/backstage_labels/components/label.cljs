(ns backstage-labels.components.label
  (:require [cljs-css-modules.macro :as css-modules]
            [garden.units :as units]
            [garden.color :as color]))

(css-modules/defstyle style
  [".container" {:align-items "center"
                 :display "flex"
                 :padding [[(units/px 10) (units/px 15)]]
                 :width (units/percent 100)}]

  [".accessory-left" {:flex "none"
                      :margin-right (units/px 15)}]

  [".accessory-right" {:flex "none"
                       :margin-left (units/px 15)}]

  [".details" {:flex "1 1 auto"}]

  [".key" {:display "block"
           :font-size (units/px 20)
           :letter-spacing (units/px 1)}]

  [".metadata" {:color "#999"
                :font-size (units/px 12)
                :margin 0
                :overflow "hidden"
                :padding 0
                :text-overflow "ellipsis"
                :white-space "nowrap"}]

  [".metadata-item" {:display "inline-block"
                     :margin-right (units/px 8)
                     :overflow "hidden"
                     :text-overflow "ellipsis"
                     :white-space "nowrap"}])

(defn details
  [{:keys [key category metadata]}]
  (let [info (cons category (vals metadata))
        i    (atom -1)]
    [:div {:class (:details style)}
     [:span {:class (:key style)} key]
     [:ul {:class (:metadata style)}
      (for [item info]
        (do
          (swap! i inc)
          [:li {:key @i
                :class (:metadata-item style)} item]))]]))

(defn main
  [{:keys [label accessory-left accessory-right on-double-click]}]
  [:div {:class           (:container style)
         :on-double-click on-double-click}
   (when-not (nil? accessory-left)
     [:div {:class (:accessory-left style)}
      [accessory-left {:label label}]])
   [details label]
   (when-not (nil? accessory-right)
     [:div {:class (:accessory-right style)}
      [accessory-right {:label label}]])])
