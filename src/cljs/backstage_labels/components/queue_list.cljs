(ns backstage-labels.components.queue-list
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.config :as config]
            [backstage-labels.components.label :as label]))

(defstyle style
  [".container" {:background-color (:background--secondary config/theme)
                 :box-shadow [["inset" 0 (units/px -0.5) 0 (color/rgba 0 0 0 0.15)]]
                 :flex "1 1 auto"
                 :list-style-type "none"
                 :margin 0
                 :padding 0
                 :overflow "scroll"}]

  [".accessory-left" {:background-color (:background--field config/theme)
                      :border-radius (units/px 4)
                      :box-shadow [[0 0 0 (units/px 0.5) (color/rgba 0 0 0 0.07)]
                                   [0 (units/px 0.5) (units/px 0.5) (color/rgba 0 0 0 0.2)]]
                      :font-size (units/px 14)
                      :height (units/px 30)
                      :padding 0
                      :text-align "center"
                      :width (units/px 30)}]

  [".accessory-right" {:align-items "center"
                       :display "flex"
                       :flex "none"
                       :justify-content "center"}])

(defn accessory-left
  "Left accessory for queue item"
  [index qty]
  (let [set-queue-qty #(re-frame/dispatch [:set-queue-qty index %])]
    (fn [_]
      [:input {:class (:accessory-left style)
               :on-change #(set-queue-qty (-> % .-target .-value int))
               :value qty}])))

(defn accessory-right
  "Right accessory for queue item"
  [index]
  (let [dequeue-label #(re-frame/dispatch [:dequeue-label index])]
    (fn [_]
      [:div {:class (:accessory-right style)
             :on-click dequeue-label}
       [:img {:src "./images/remove.svg"}]])))

(defn item
  "Label item for queue list."
  [index [id qty]]
  (let [label (re-frame/subscribe [:label id])]
    [:li {:key index}
     [label/main {:label @label
                  :accessory-left (accessory-left index qty)
                  :accessory-right (accessory-right index)}]]))

(defn main
  "List of queue items."
  []
  (let [queue (re-frame/subscribe [:queue])]
    [:ul {:class (:container style)}
     ;; Required to invoke doall since map-indexed returns a lazy seq.
     (->> @queue (map-indexed item) doall)]))
