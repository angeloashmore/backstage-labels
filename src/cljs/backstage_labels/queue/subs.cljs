(ns backstage-labels.queue.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(re-frame/reg-sub
 :queue
 (fn [db _]
   (:queue db)))
