(ns backstage-labels.components.queue-all-filtered-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :as css-modules]
            [garden.units :as units]
            [garden.color :as color]))

(css-modules/defstyle style
  [".container" {:align-items "center"
                 :background-color "#f2f2f2"
                 :box-shadow ["inset" (units/px -0.5) 0 0 (color/rgba 0 0 0 0.15)]
                 :display "flex"
                 :flex "none"
                 :justify-content "flex-end"
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".button" {:color "#007aff"
              :display "block"
              :font-size (units/px 12)}
   ["&:active" {:color "#0063cc"}]])

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
