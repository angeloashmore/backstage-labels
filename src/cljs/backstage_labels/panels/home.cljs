(ns backstage-labels.panels.home
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [cljs-css-modules.macro :as css-modules]
            [garden.units :as units]
            [backstage-labels.config :as config]
            [backstage-labels.components.filter-query-bar :as filter-query-bar]
            [backstage-labels.components.filter-collection-bar :as filter-collection-bar]
            [backstage-labels.components.labels-list :as labels-list]
            [backstage-labels.components.queue-all-filtered-bar :as queue-all-filtered-bar]
            [backstage-labels.components.label :as label]))

;; -- Right Column -------------------------------------------------------------

(defn queue-actions-header-bar
  "Displays queue count and print button."
  []
  (let [queue-count (subscribe [:queue-count])]
    [:div {:class "flex flex-none items-center justify-between h3 pa3 bg-light-gray draggable",
           :style {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15)"}}
     ;; Count
     [:div {:class "f6 fw2 gray"}
      (str @queue-count " label" (when (not= @queue-count 1) "s"))]

     ;; Print button
     [:button {:class "outline-0 bn pa2 ph3 f6 fw5 blue bg-white br-pill"
               :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07),
                                    0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
      "Print"]]))

(defn print-options-bar
  "Displays print options."
  []
  (let [templates                 config/templates
        template                  (subscribe [:print-option-template])
        set-print-option-template #(dispatch [:set-print-option-template %])
        option                    (fn [[key name]]
                                    [:option {:key key} name])]
    [:div {:class "flex flex-none items-center pa2 ph3 bg-light-gray"
           :style {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15)"}}
     ;; Label
     [:div {:class "mr2 f7 gray"} "Template:"]

     ;; Select
     [:select {:class "input-reset flex flex-auto bn br2 pa1 ph2 f7 bg-white outline-0"
               :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07),
                                    0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
      (map option templates)]]))

(defn queue-list-item-accessory-left
  "Left accessory for queue item"
  [qty]
  (fn [_]
    [:input {:class "input-reset h2 w2 mh2 pv1 ph2 bn br2 f5 bg-white outline-0"
             :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07),
                                  0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}
             :value qty}]))

(defn queue-list-item-accessory-right
  "Right accessory for queue item"
  [index]
  (let [dequeue-label #(dispatch [:dequeue-label index])]
    (fn [_]
      [:div {:class "flex flex-none items-center justify-center w2 mh2"
             :on-click dequeue-label}
       [:img {:src "./images/remove.svg"}]])))

(defn queue-list-item
  "Label item for queue list."
  [index [id qty]]
  (let [label (subscribe [:label id])]
    [:li {:key index}
     [label/main {:label @label
                  :accessory-left (queue-list-item-accessory-left qty)
                  :accessory-right (queue-list-item-accessory-right index)}]]))

(defn queue-list
  "List of queue items."
  []
  (let [queue (subscribe [:queue])
        item  (fn [index [id qty]]
                [:li {:key index} (str id " - " qty)])]
    [:ul {:class "flex-auto overflow-scroll list ma0 pa0 bg-light-gray"
          :style {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15)"}}
     ;; Required to invoke doall since map-indexed returns a lazy seq.
     (->> @queue (map-indexed queue-list-item) doall)]))

(defn empty-queue-bar
  "Displays empty queue button."
  []
  (let [empty-queue #(dispatch [:empty-queue])]
    [:div {:class "flex flex-none items-center justify-end pa2 ph3 bg-light-gray"}
     [:a {:class "db pv1 pl2 f7 blue"
          :on-click empty-queue}
      "Clear All"]]))

;; -- Main ---------------------------------------------------------------------

(css-modules/defstyle style
  [".container" {:display "flex"
                 :flex "1 1 auto"}]

  [".left-column" {:display "flex"
                   :flex-grow 1
                   :flex-direction "column"}]

  [".right-column" {:display "flex"
                    :flex-shrink 0
                    :flex-direction "column"
                    :width (units/px 250)}])

(defn main
  []
  [:div {:class (:container style)}
   ;; Left column
   [:div {:class (:left-column style)}
    [filter-query-bar/main]
    [filter-collection-bar/main]
    [labels-list/main]
    [queue-all-filtered-bar/main]]

   ;; Right column
   [:div {:class (:right-column style)}
    [queue-actions-header-bar]
    [print-options-bar]
    [queue-list]
    [empty-queue-bar]]])
