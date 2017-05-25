(ns backstage-labels.components.empty-queue-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :as css-modules]
            [garden.units :as units]))

(css-modules/defstyle style
  [".container" {:align-items "center"
                 :background-color "#f2f2f2"
                 :display "flex"
                 :flex "none"
                 :justify-content "flex-end"
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".button" {:color "#007aff"
              :display "block"
              :font-size (units/px 12)}
   ["&:active" {:color "#0063cc"}]])

(defn main
  "Displays empty queue button."
  []
  (let [empty-queue #(re-frame/dispatch [:empty-queue])]
    [:div {:class (:container style)}
     [:a {:class (:button style)
          :on-click empty-queue}
      "Clear All"]]))
