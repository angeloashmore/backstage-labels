(ns backstage-labels.components.filter-query-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :as css-modules]
            [garden.units :as units]
            [garden.color :as color]))

(css-modules/defstyle style
  [".container" {:background-color "#f2f2f2"
                 :background-image "url(./images/search.svg)"
                 :background-position [[(units/px+ (units/px 1.5) (units/px 16)) "center"]]
                 :background-repeat "no-repeat"
                 :border-weight 0
                 :box-shadow [["inset" 0 (units/px -0.5) 0 (color/rgba 0 0 0 0.15)]
                              ["inset" (units/px -0.5) 0 0 (color/rgba 0 0 0 0.15)]]
                 :display "block"
                 :flex "none"
                 :font-size (units/px 24)
                 :font-weight 300
                 :height (units/px 56)
                 :letter-spacing (units/px 1)
                 :margin 0
                 :padding 0
                 :padding-left (units/px 50)
                 :outline 0}])

(defn main
  "Displays query filter input."
  []
  (let [query     (re-frame/subscribe [:filter-query])
        resolve   #(-> % str clojure.string/trim)
        set-query #(re-frame/dispatch [:set-filter-query (resolve %)])]
    [:input {:id        "filter-query"
             :class     (:container style)
             :type      "text"
             :value     @query
             :on-change #(set-query (-> % .-target .-value))}]))
