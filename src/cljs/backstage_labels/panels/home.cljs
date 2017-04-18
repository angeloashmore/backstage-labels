(ns backstage-labels.panels.home
  (:require [re-frame.core :as re-frame :refer [subscribe]]
            [backstage-labels.routes :as routes]
            [backstage-labels.config :as config]
            [backstage-labels.components.filter-bar :as filter-bar]
            [backstage-labels.components.label :as label]
            [backstage-labels.components.queue-bar :as queue-bar]))

(defn label-accessory-right
  [id]
  (fn []
    [:a {:on-click #(re-frame/dispatch [:queue-label id])} "Add"]))

(defn labels-list
  [labels]
  [:ul {:class-name "flex-auto overflow-scroll list ma0 pa0",
        :style      {:box-shadow "inset -0.5px 0 0 rgba(0, 0, 0, 0.15)"}}
   (for [[id l] labels]
     [label/main {:key id
                  :label l
                  :accessory-right (label-accessory-right id)
                  :on-double-click #(re-frame/dispatch [:queue-label id])}])])

(defn main
  []
  (let [query       (re-frame/subscribe [:filter-query])
        collections (re-frame/subscribe [:collections])
        collection  (re-frame/subscribe [:filter-collection])
        labels-filtered (re-frame/subscribe [:labels-filtered])
        queue       (re-frame/subscribe [:queue])
        queue-count (re-frame/subscribe [:queue-count])
        templates   config/templates
        print-option-template (re-frame/subscribe [:print-option-template])]
    [:div {:class-name "flex flex-auto"}

     ;; Left column
     [:div {:class-name "flex flex-column w-60"}
      [filter-bar/main {:query @query
                        :set-query #(re-frame/dispatch [:set-filter-query %])
                        :collections @collections
                        :collection @collection
                        :set-collection #(re-frame/dispatch [:set-filter-collection %])}]

      ;; Labels list
      [labels-list @labels-filtered]]

     ;; Right column
     [:div {:class-name "flex flex-auto flex-column"}
      [queue-bar/main {:queue-count @queue-count
                       :templates templates
                       :template @print-option-template
                       :set-print-option-template #(re-frame/dispatch [:set-print-option-template %])}]

      ;; Queue list
      [:ul {:class-name "overflow-scroll list ma0 pa0"}
       (let [i (atom -1)]
         (for [[id qty] @queue]
           (do
             (swap! i inc)
             [:li {:key @i} (str id " - " qty)])))]
      ]]))
