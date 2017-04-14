(ns backstage-labels.filters.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(re-frame/reg-sub
 :filter-collection
 (fn [db _]
   (:filter-collection db)))

(re-frame/reg-sub
 :filter-query
 (fn [db _]
   (:filter-query db)))
