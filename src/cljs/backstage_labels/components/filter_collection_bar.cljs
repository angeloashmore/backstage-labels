(ns backstage-labels.components.filter-collection-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :as css-modules]
            [garden.units :as units]
            [garden.color :as color]))

(css-modules/defstyle style
  [".container" {:align-items "center"
                 :background-color "#fff"
                 :box-shadow [["inset" 0 (units/px -0.5) 0 (color/rgba 0 0 0 0.15)]
                              ["inset" (units/px -0.5) 0 0 (color/rgba 0 0 0 0.15)]]
                 :display "flex"
                 :flex "none"
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".label" {:color "#7f7f7f"
             :font-size (units/px 12)
             :margin-right (units/px 10)}]

  [".select" {:background-color "#fff"
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

     ;; Label
     [:div {:class (:label style)}
      "Collection:"]

     ;; Select
     [:select {:class     (:select style)
               :on-change #(set-collection (-> % .-target .-value))}
      (map option (cons all @collections))]]))
