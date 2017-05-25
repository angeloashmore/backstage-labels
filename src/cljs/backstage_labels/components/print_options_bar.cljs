(ns backstage-labels.components.print-options-bar
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [garden.color :as color]
            [backstage-labels.config :as config]
            [backstage-labels.components.form :as form]))

(defstyle style
  [".container" {:align-items "center"
                 :background-color (:background--secondary config/theme)
                 :box-shadow [["inset" 0 (units/px -0.5) 0 (color/rgba 0 0 0 0.15)]]
                 :display "flex"
                 :padding [[(units/px 10) (units/px 15)]]}]

  [".select" {:flex "1 1 auto"}])

(defn option
  "Option for select element."
  [[key name]]
  [:option {:key key} name])

(defn main
  "Displays print options."
  []
  (let [templates                 config/templates
        template                  (re-frame/subscribe [:print-option-template])
        set-print-option-template #(re-frame/dispatch [:set-print-option-template %])]
    [:div {:class (:container style)}
     [form/label "Template:"]
     [form/select {:class (:select style)}
      (map option templates)]]))
