(ns backstage-labels.filters.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db]]))

(re-frame/reg-event-db
 :set-filter-collection
 (fn [db [_ id]]
   (assoc db :filter-collection id)))

(re-frame/reg-event-db
 :set-filter-query
 (fn [db [_ query]]
   (assoc db :filter-query query)))
