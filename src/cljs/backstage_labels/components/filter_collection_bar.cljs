(ns backstage-labels.components.filter-collection-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.config :as config]
            [backstage-labels.components.form :as form]))

(defstyle style
  [".container" {:align-items "center"
                 :background-color (:background config/theme)
                 :box-shadow [["inset" 0 (units/px -0.5) 0 (color/rgba 0 0 0 0.15)]
                              ["inset" (units/px -0.5) 0 0 (color/rgba 0 0 0 0.15)]]
                 :display "flex"
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".select" {:flex "1 1 auto"}])

(defn option
  "Option for select element."
  [[id coll]]
  [:option {:key id :value id} (:key coll)])

(defn main
  "Displays list of collections to set filter."
  []
  (let [collections    (re-frame/subscribe [:collections])
        collection     (re-frame/subscribe [:filter-collection])
        all            ["all-val" {:key "All labels"}]
        resolve        #(if (= % (nth all 0)) nil (-> % keyword))
        set-collection #(re-frame/dispatch [:set-filter-collection (resolve %)])]
    [:div {:class (:container style)}
     [form/label "Collection:"]
     [form/select {:class     (:select style)
                   :on-change #(set-collection (-> % .-target .-value))}
      (map option (cons all @collections))]]))
