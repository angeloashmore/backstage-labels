(ns backstage-labels.panels.home
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [backstage-labels.config :as config]
            [backstage-labels.components.label :as label]))

;; -- Left Column --------------------------------------------------------------

(defn filter-query-bar
  "Displays query filter input."
  []
  (let [query     (subscribe [:filter-query])
        resolve   #(-> % str clojure.string/trim)
        set-query #(dispatch [:set-filter-query (resolve %)])]
    [:input {:class     "flex flex-none h3 outline-0 pa3 f3 fw3 tracked bg-light-gray bw0"
             :style     {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15), inset -0.5px 0 0 rgba(0, 0, 0, 0.15)"}
             :on-change #(set-query (-> % .-target .-value))
             :type      "text"
             :value     @query}]))

(defn filter-collection-bar
  "Displays list of collections to set filter."
  []
  (let [collections    (subscribe [:collections])
        collection     (subscribe [:filter-collection])
        all-val        "all"
        resolve        #(if (= % all-val) nil (-> % keyword))
        set-collection #(dispatch [:set-filter-collection (resolve %)])
        option         (fn [[id coll]]
                         [:option {:key id :value id} (:key coll)])]
    [:div {:class "flex flex-none items-center pa2 ph3 bg-white"
           :style {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15), inset -0.5px 0 rgba(0, 0, 0, 0.15)"}}
     ;; Label
     [:div {:class "f7 gray mr2"}
      "Collection:"]

     ;; Select
     [:select {:class     "input-reset flex flex-auto bn br2 pa1 ph2 f7 bg-white outline-0"
               :style     {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}
               :on-change #(set-collection (-> % .-target .-value))}
      [:option {:key nil :value all-val} "All labels"]
      (map option @collections)]]))

(defn labels-list-item-accessory-left
  "Left accessory for label item"
  [_]
  [:div ":)"])

(defn labels-list-item-accessory-right
  "Right accessory for label item"
  [id]
  (let [queue-label #(dispatch [:queue-label id])]
    (fn [_]
      [:a {:on-click queue-label} "Add"])))

(defn labels-list-item
  "Label item for labels list."
  [[id label]]
  [:li {:key id}
   [label/main {:label label
                :accessory-left  labels-list-item-accessory-left
                :accessory-right (labels-list-item-accessory-right id)
                :on-double-click #(dispatch [:queue-label id])}]])

(defn labels-list
  "List of labels."
  []
  (let [filtered-labels (subscribe [:labels-filtered])]
    [:ul {:class "flex-auto overflow-scroll list ma0 pa0"
          :style {:box-shadow "inset -0.5px 0 0 rgba(0, 0, 0, 0.15)"}}
     (map labels-list-item @filtered-labels)]))

;; -- Right Column -------------------------------------------------------------

(defn queue-actions-bar
  "Displays queue count and print button."
  []
  (let [queue-count (subscribe [:queue-count])]
    [:div {:class-name "flex flex-none items-center justify-between h3 pa3 bg-light-gray"
           :style      {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15)"
                        :-webkit-app-region "drag"}}
     ;; Count
     [:div {:class-name "f6 fw2 gray"}
      (str @queue-count " label" (when (not= @queue-count 1) "s"))]

     ;; Print button
     [:button {:class-name "outline-0 bn pa2 ph3 f6 fw5 blue bg-white br-pill shadow-1"
               :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
      "Print"]]))

(defn print-options-bar
  "Displays print options."
  []
  (let [templates                 config/templates
        template                  (subscribe [:print-option-template])
        set-print-option-template #(dispatch [:set-print-option-template %])
        option                    (fn [[key name]]
                                    [:option {:key key} name])]
    [:div {:class-name "flex flex-none items-center pa2 ph3 bg-light-gray"
           :style      {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15)"}}
     ;; Label
     [:div {:class-name "f7 gray mr2"}
      "Template:"]

     ;; Select
     [:select {:class-name "input-reset flex flex-auto bn br2 pa1 ph2 f7 bg-white outline-0"
               :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
      (map option templates)]]))

(defn queue-list-item-accessory-left
  "Left accessory for queue item"
  [qty]
  (fn [_]
    [:div qty]))

(defn queue-list-item-accessory-right
  "Right accessory for queue item"
  [index]
  (let [dequeue-label #(dispatch [:dequeue-label index])]
    (fn [_]
      [:a {:on-click dequeue-label} "X"])))

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
    [:ul {:class "flex-auto overflow-scroll list ma0 pa0 bg-light-gray"}
     ;; Required to invoke doall since map-indexed returns a lazy seq.
     (->> @queue (map-indexed queue-list-item) doall)]))

;; -- Main ---------------------------------------------------------------------

(defn main
  []
  [:div {:class "flex flex-auto"}
   ;; Left column
   [:div {:class "flex flex-none flex-column w-60"}
    [filter-query-bar]
    [filter-collection-bar]
    [labels-list]]

   ;; Right column
   [:div {:class "flex flex-auto flex-column"}
    [queue-actions-bar]
    [print-options-bar]
    [queue-list]]])
