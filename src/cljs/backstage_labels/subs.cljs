(ns backstage-labels.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub subscribe]]))

(reg-sub
 :failed
 (fn [db]
   (:failed db)))

;; -- Boot ---------------------------------------------------------------------

(reg-sub
 :initialized?
 (fn [db _]
   (and (not (empty? db))
        (not (empty? (:release-tags db)))
        (not (empty? (:collections db)))
        (not (empty? (:labels db))))))

;; -- Routing ------------------------------------------------------------------

(reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

;; -- Releases -----------------------------------------------------------------

(reg-sub
 :release-tag
 (fn [db _]
   (:release-tag db)))

(reg-sub
 :release-tags
 (fn [db _]
   (:release-tags db)))

(reg-sub
 :release-tags-loading
 (fn [db _]
   (:release-tags-loading db)))

;; -- Collections --------------------------------------------------------------

(reg-sub
 :collections
 (fn [db _]
   (:collections db)))

(reg-sub
 :collections-loading
 (fn [db _]
   (:collections-loading db)))

;; -- Labels -------------------------------------------------------------------

(reg-sub
 :labels
 (fn [db _]
   (:labels db)))

(reg-sub
 :labels-filtered
 (fn [db _]
   (let [{:keys [labels filter-collection]} db
         allowed (get-in db [:collections filter-collection :label_ids] [])]
     (if (nil? filter-collection)
       labels
       (select-keys labels (map keyword allowed))))))

(reg-sub
 :label
 :<- [:labels]
 (fn [labels query-v]
   (let [[_ id] query-v]
     (get labels id))))

(reg-sub
 :labels-loading
 (fn [db _]
   (:labels-loading db)))

;; -- Filters ------------------------------------------------------------------

(reg-sub
 :filter-collection
 (fn [db _]
   (:filter-collection db)))

(reg-sub
 :filter-query
 (fn [db _]
   (:filter-query db)))

;; -- Print options ------------------------------------------------------------

(reg-sub
 :print-option-template
 (fn [db _]
   (:print-option-template db)))

;; -- Queue --------------------------------------------------------------------

(reg-sub
 :queue
 (fn [db _]
   (:queue db)))

(reg-sub
 :queue-count
 (fn [db _]
   (reduce #(+ %1 (peek %2)) 0 (:queue db))))
