(ns backstage-labels.components.labels-list-actions-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.config :as config]
            [backstage-labels.components.form :as form]))

(defstyle style
  [".container" {:align-items "center"
                 :background-color (:background--secondary config/theme)
                 :box-shadow [["inset" (units/px -0.5) 0 0 (color/rgba 0 0 0 0.15)]]
                 :display "flex"
                 :flex "none"
                 :justify-content "space-between"
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".button" {:color (:tint config/theme)
              :display "block"
              :font-size (units/px 12)}
   ["&:active" {:color (:tint--active config/theme)}]])

(defn release-tag-option
  "Option for release tag select element."
  [tag]
  [:option {:key tag :value tag} tag])

(defn main
  "Displays list of release tags to set release tag and queue all filtered
  labels button."
  []
  (let [release-tags    (re-frame/subscribe [:release-tags])
        labels-filtered (re-frame/subscribe [:labels-filtered])
        set-release-tag #(re-frame/dispatch [:set-release-tag %])
        queue-labels    #(re-frame/dispatch [:queue-labels %])]
    [:div {:class (:container style)}
     [:div
      [form/select {:class     (:release-tags style)
                    :on-change #(set-release-tag (-> % .-target .-value))}
       (map release-tag-option @release-tags)]]
     [:a {:class    (:button style)
          :on-click #(-> @labels-filtered keys queue-labels)}
      "Add All Visible"]]))
