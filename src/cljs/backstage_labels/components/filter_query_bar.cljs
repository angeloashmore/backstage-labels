(ns backstage-labels.components.filter-query-bar
  (:require [clojure.string :as string]
            [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.config :as config]))

(defstyle style
  [".container" {:background-color (:background--secondary config/theme)}]

  [".input" {:background-color "transparent"
             :background-image "url(./images/search.svg)"
             :background-position [[(units/px+ (units/px 1.5) (units/px 16)) "center"]]
             :background-repeat "no-repeat"
             :border 0
             :box-shadow [["inset" 0 (units/px -0.5) 0 (color/rgba 0 0 0 0.15)]
                          ["inset" (units/px -0.5) 0 0 (color/rgba 0 0 0 0.15)]]
             :font-size (units/px 24)
             :font-weight 300
             :height (units/percent 100)
             :letter-spacing (units/px 1)
             :margin 0
             :padding 0
             :padding-left (units/px 50)
             :outline 0
             :width (units/percent 100)}])

(defn main
  "Displays query filter input."
  []
  (let [query     (re-frame/subscribe [:filter-query])
        resolve   #(if (string/blank? %) nil %)
        set-query #(re-frame/dispatch [:set-filter-query (resolve %)])]
    [:div {:class (:container style)}
     [:input {:id        config/filter-query-id
              :class     (:input style)
              :type      "text"
              :value     @query
              :on-change #(set-query (-> % .-target .-value))}]]))
