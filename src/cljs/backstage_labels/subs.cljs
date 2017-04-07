(ns backstage-labels.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :initialized?
 (fn [db _]
   (and (not (empty? db))
        (not (empty? (:collections db)))
        (not (empty? (:labels db))))))

(re-frame/reg-sub
 :name
 (fn [db]
   (:name db)))

;; -- Routing ------------------------------------------------------------------

(re-frame/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

;; -- Filters ------------------------------------------------------------------

(re-frame/reg-sub
 :filters
 (fn [db _]
   {:collection (:filter-collection db)
    :query      (:filter-query db)}))

;; -- Print options ------------------------------------------------------------

(re-frame/reg-sub
 :print-options
 (fn [db _]
   {:template (:print-option-template db)}))

;; -- Queue --------------------------------------------------------------------

(re-frame/reg-sub
 :queue
 (fn [db _]
   (:queue db)))
