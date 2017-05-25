(ns backstage-labels.panels.home
  (:require [cljs-css-modules.macro :as css-modules]
            [garden.units :as units]

            ;; Left Column
            [backstage-labels.components.filter-query-bar :as filter-query-bar]
            [backstage-labels.components.filter-collection-bar :as filter-collection-bar]
            [backstage-labels.components.labels-list :as labels-list]
            [backstage-labels.components.queue-all-filtered-bar :as queue-all-filtered-bar]

            ;; Right Column
            [backstage-labels.components.queue-actions-header-bar :as queue-actions-header-bar]
            [backstage-labels.components.print-options-bar :as print-options-bar]
            [backstage-labels.components.queue-list :as queue-list]
            [backstage-labels.components.empty-queue-bar :as empty-queue-bar]))

(css-modules/defstyle style
  [".container" {:display "flex"}]

  [".left-column" {:display "flex"
                   :flex-direction "column"
                   :width (units/percent 60)}]

  [".right-column" {:display "flex"
                    :flex-direction "column"
                    :width (units/percent 40)}])

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
    [queue-actions-header-bar/main]
    [print-options-bar/main]
    [queue-list/main]
    [empty-queue-bar/main]]])
