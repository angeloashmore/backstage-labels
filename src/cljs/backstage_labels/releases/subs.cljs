(ns backstage-labels.releases.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(re-frame/reg-sub
 :db-release-tag
 (fn [db _]
   (:db-release-tag db)))

(re-frame/reg-sub
 :db-release-tags
 (fn [db _]
   (:db-release-tags db)))
