(ns backstage-labels.panels.home
  (:require [re-frame.core :as re-frame :refer [subscribe]]
            [backstage-labels.routes :as routes]
            [backstage-labels.components.filter-bar :as filter-bar]
            [backstage-labels.components.queue-bar :as queue-bar]))

(defn main
  []
  (let [query       (re-frame/subscribe [:filter-query])
        collections (re-frame/subscribe [:collections])
        collection  (re-frame/subscribe [:filter-collection])
        queue       (re-frame/subscribe [:queue])]
    [:div {:class-name "flex flex-auto"}

     ;; Left column
     [:div {:class-name "flex flex-column w-60"}
      [filter-bar/main {:query @query
                        :set-query #(re-frame/dispatch [:set-filter-query %])
                        :collections @collections
                        :collection @collection
                        :set-collection #(re-frame/dispatch [:set-filter-collection %])}]
      [:div {:class-name "flex flex-auto"}
       "List of labels"]
      ]

     ;; Right column
     [:div {:class-name "flex flex-auto flex-column",
            :style {:box-shadow "-0.5px 0 0 rgba(0, 0, 0, 0.15)"}}
      [queue-bar/main {:queue @queue}]
      [:div {:class-name "flex flex-auto"}
       "List of queue"]
      ]]))
