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
    [:div.flex.flex-none.items-stretch.h3.bg-light-gray.shadow-hairline--bottom-right-i
     ;; Icon
     [:div.flex.flex-none.items-center.justify-center.w2.mh2
      [:img {:src "./images/search.svg"}]]

     ;; Input
     [:input#filter-query.flex-auto.db.outline-0.ma0.ph0.pv3.f3.fw3.tracked.bw0.bg-transparent
      {:on-change #(set-query (-> % .-target .-value))
       :type      "text"
       :value     @query}]]))

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
    [:div.flex.flex-none.items-center.pa2.ph3.bg-white.shadow-hairline--bottom-right-i
     ;; Label
     [:div.f7.gray.mr2 "Collection:"]

     ;; Select
     [:select.input-reset.flex.flex-auto.bn.br2.pa1.ph2.f7.bg-white.outline-0
      {:style     {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}
       :on-change #(set-collection (-> % .-target .-value))}
      [:option {:key nil :value all-val} "All labels"]
      (map option @collections)]]))

(defn labels-list-item-accessory-left
  "Left accessory for label item"
  [_]
  [:div.flex.flex-none.items-center.justify-center.w2.mh2
   [:img {:src "./images/label.svg"}]])

(defn labels-list-item-accessory-right
  "Right accessory for label item"
  [id]
  (let [queue-label #(dispatch [:queue-label id])]
    (fn [_]
      [:div.flex.flex-none.items-center.justify-center.w2.mh2
       {:on-click queue-label}
       [:img {:src "./images/add.svg"}]])))

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
    [:ul.flex-auto.overflow-scroll.list.ma0.pa0.shadow-hairline--bottom-right-i
     (map labels-list-item @filtered-labels)]))

(defn queue-all-filtered-bar
  "Displays queue all filtered labels button."
  []
  (let [labels    (subscribe [:labels-filtered])
        ids       (keys @labels)
        queue-all #(dispatch [:queue-labels ids])]
    [:div.flex.flex-none.items-center.justify-end.pa2.ph3.bg-light-gray.shadow-hairline--right-i
     [:a.db.pv1.pl2.f7.blue
      {:on-click queue-all}
      "Add All Visible"]]))

;; -- Right Column -------------------------------------------------------------

(defn queue-actions-header-bar
  "Displays queue count and print button."
  []
  (let [queue-count (subscribe [:queue-count])]
    [:div.flex.flex-none.items-center.justify-between.h3.pa3.bg-light-gray.shadow-hairline--bottom-i.draggable
     ;; Count
     [:div.f6.fw2.gray
      (str @queue-count " label" (when (not= @queue-count 1) "s"))]

     ;; Print button
     [:button.outline-0.bn.pa2.ph3.f6.fw5.blue.bg-white.br-pill
      {:style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
      "Print"]]))

(defn print-options-bar
  "Displays print options."
  []
  (let [templates                 config/templates
        template                  (subscribe [:print-option-template])
        set-print-option-template #(dispatch [:set-print-option-template %])
        option                    (fn [[key name]]
                                    [:option {:key key} name])]
    [:div.flex.flex-none.items-center.pa2.ph3.bg-light-gray.shadow-hairline--bottom-i
     ;; Label
     [:div.mr2.f7.gray "Template:"]

     ;; Select
     [:select.input-reset.flex.flex-auto.bn.br2.pa1.ph2.f7.bg-white.outline-0
      {:style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}}
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
    [:ul.flex-auto.overflow-scroll.list.ma0.pa0.bg-light-gray.shadow-hairline--bottom-i
     ;; Required to invoke doall since map-indexed returns a lazy seq.
     (->> @queue (map-indexed queue-list-item) doall)]))

(defn empty-queue-bar
  "Displays empty queue button."
  []
  (let [empty-queue #(dispatch [:empty-queue])]
    [:div.flex.flex-none.items-center.justify-end.pa2.ph3.bg-light-gray
     [:a.db.pv1.pl2.f7.blue
      {:on-click empty-queue}
      "Clear All"]]))

;; -- Main ---------------------------------------------------------------------

(defn main
  []
  [:div.flex.flex-auto
   ;; Left column
   [:div.flex.flex-none.flex-column.w-60
    [filter-query-bar]
    [filter-collection-bar]
    [labels-list]
    [queue-all-filtered-bar]]

   ;; Right column
   [:div.flex.flex-auto.flex-column
    [queue-actions-header-bar]
    [print-options-bar]
    [queue-list]
    [empty-queue-bar]]])
