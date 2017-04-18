(ns backstage-labels.components.queue-bar
  (:require [reagent.format :as format :refer [pluralize]]))

(defn queue-bar
  [queue-count]
  [:div {:class-name "flex items-center justify-between h3 pa3 bg-light-gray"
         :style      {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15)"
                      :-webkit-app-region "drag"}}
   ;; Quantity
   [:div {:class-name "f6 fw2 gray"}
    (str queue-count " label" (when (not= queue-count 1) "s"))]

   ;; Print button
   [:button {:class-name "outline-0 bn pa2 ph3 f6 fw5 blue bg-white br-pill shadow-1"
             :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
    "Print"]])

(defn template-bar
  [{:keys [templates template set-print-option-template]}]
  [:div {:class-name "flex items-center pa2 ph3 bg-light-gray"
         :style      {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15)"}}
   ;; Label
   [:div {:class-name "f7 gray mr2"}
    "Template:"]

   ;; Select
   [:select {:class-name "input-reset flex flex-auto bn br2 pa1 ph2 f7 bg-white outline-0"
             :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
    (for [[k v] templates]
      [:option {:key k} v])]])

(defn main
  [{:keys [queue-count templates template set-print-option-template]}]
  [:div {:class-name "flex flex-column flex-none"}
   [queue-bar queue-count]
   [template-bar {:templates templates
                  :template  template
                  :set-print-option-template set-print-option-template}]])
