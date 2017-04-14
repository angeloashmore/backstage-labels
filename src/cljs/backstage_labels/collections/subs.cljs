(ns backstage-labels.collections.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(re-frame/reg-sub
 :collections
 (fn [db _]
   (:collections db)))
