(ns backstage-labels.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [reg-sub]]
            [backstage-labels.collections.subs]
            [backstage-labels.filters.subs]
            [backstage-labels.labels.subs]
            [backstage-labels.queue.subs]
            [backstage-labels.releases.subs]))

(re-frame/reg-sub
 :failed
 (fn [db]
   (:failed db)))

;; -- Boot ---------------------------------------------------------------------

(re-frame/reg-sub
 :initialized?
 (fn [db _]
   (and (not (empty? db))
        (not (empty? (:collections db)))
        (not (empty? (:labels db))))))

;; -- Routing ------------------------------------------------------------------

(re-frame/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

;; -- Print options ------------------------------------------------------------

(re-frame/reg-sub
 :print-option-template
 (fn [db _]
   (:print-option-template db)))
