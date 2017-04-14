(ns backstage-labels.labels.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(re-frame/reg-sub
 :labels
 (fn [db _]
   (:labels db)))
