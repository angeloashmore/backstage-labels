(ns backstage-labels.panels.home
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [forest.macros :refer-macros [defstylesheet]]
            [backstage-labels.config :as config]
            [backstage-labels.components.filter-bar :as filter-bar]
            [backstage-labels.components.label :as label]
            [backstage-labels.components.queue-bar :as queue-bar]))

(defstylesheet styles
  [.main-class        {:composes "flex flex-auto"}]
  [.left-class        {:composes "flex flex-none flex-column w-60"}]
  [.right-class       {:composes "flex flex-auto flex-column"}]
  [.labels-list-class {:composes "flex-auto overflow-scroll ma0 pa0"
                       :box-shadow "inset -0.5px 0 0 rgba(0, 0, 0, 0.15)"}]
  [.queue-list-class  {:composes "flex-auto overflow-scroll ma0 pa0"}])

(defn label-accessory-right
  [queue-label]
  (fn []
    [:a {:on-click queue-label} "Add"]))

(defn labels-list
  [{:keys [labels queue-label]}]
  [:ul {:class labels-list-class}
   (for [[id l] labels]
     [label/main {:key id
                  :label l
                  :accessory-right (label-accessory-right #(queue-label id))
                  :on-double-click #(dispatch [:queue-label id])}])])

(defn queue-list
  [{:keys [queue]}]
  (let [i (atom -1)]
    [:ul {:class queue-list-class}
     (for [[id qty] queue]
       (do
         (swap! i inc)
         [:li {:key @i} (str id " - " qty)]))]))

(defn left-column
  []
  (let [labels-filtered       (subscribe [:labels-filtered])
        collections           (subscribe [:collections])
        query                 (subscribe [:filter-query])
        collection            (subscribe [:filter-collection])
        set-filter-query      #(dispatch [:set-filter-query %])
        set-filter-collection #(dispatch [:set-filter-collection %])
        queue-label           #(dispatch [:queue-label %])]
    [:div {:class left-class}
     [filter-bar/main {:collections           @collections
                       :query                 @query
                       :collection            @collection
                       :set-filter-query      set-filter-query
                       :set-filter-collection set-filter-collection}]
     [labels-list {:labels      @labels-filtered
                   :queue-label queue-label}]]))

(defn right-column
  []
  (let [queue                     (subscribe [:queue])
        queue-count               (subscribe [:queue-count])
        templates                 config/templates
        template                  (subscribe [:print-option-template])
        set-print-option-template #(dispatch [:set-print-option-template %])]
    [:div {:class right-class}
     [queue-bar/main {:queue-count               @queue-count
                      :templates                 templates
                      :template                  @template
                      :set-print-option-template set-print-option-template}]
     [queue-list {:queue @queue}]]))

(defn main
  []
  [:div {:class main-class}
   [left-column]
   [right-column]])
