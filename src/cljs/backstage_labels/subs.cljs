(ns backstage-labels.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

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

;; -- Releases -----------------------------------------------------------------

(re-frame/reg-sub
 :db-release-tag
 (fn [db _]
   (:db-release-tag db)))

(re-frame/reg-sub
 :db-release-tags
 (fn [db _]
   (:db-release-tags db)))

;; -- Collections --------------------------------------------------------------

(re-frame/reg-sub
 :collections
 (fn [db _]
   (:collections db)))

;; -- Labels -------------------------------------------------------------------

(re-frame/reg-sub
 :labels
 (fn [db _]
   (:labels db)))

;; -- Filters ------------------------------------------------------------------

(re-frame/reg-sub
 :filter-collection
 (fn [db _]
   (:filter-collection db)))

(re-frame/reg-sub
 :filter-query
 (fn [db _]
   (:filter-query db)))

;; -- Print options ------------------------------------------------------------

(re-frame/reg-sub
 :print-option-template
 (fn [db _]
   (:print-option-template db)))

;; -- Queue --------------------------------------------------------------------

(re-frame/reg-sub
 :queue
 (fn [db _]
   (:queue db)))
