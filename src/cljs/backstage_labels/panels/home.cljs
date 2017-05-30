(ns backstage-labels.panels.home
  (:require [cljs-css-modules.macro :refer-macros [defstyle]]
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

(defstyle style
  [".container" {:display "grid"
                 :grid-template-rows [["auto" "auto" "minmax(0, 1fr)" "auto"]]
                 :grid-template-columns [["minmax(0, 2fr)" "minmax(250px, 1fr)"]]
                 :height (units/percent 100)}])

(defn main
  []
  [:div {:class (:container style)}
   [filter-query-bar/main]
   [queue-actions-header-bar/main]

   [filter-collection-bar/main]
   [print-options-bar/main]

   [labels-list/main]
   [queue-list/main]

   [queue-all-filtered-bar/main]
   [empty-queue-bar/main]])
