(ns backstage-labels.queue.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db]]))

(re-frame/reg-event-db
 :queue-label
 (fn [db [_ id qty]]
   (let [existing-qty (get-in db [:queue id] 0)
         new-qty      (+ existing-qty qty)]
     (assoc-in db [:queue id] new-qty))))

(re-frame/reg-event-db
 :dequeue-label
 (fn [db [_ id]]
   (update-in db [:queue] dissoc id)))

(re-frame/reg-event-db
 :empty-queue
 (fn [db _]
   (let [queue (:queue db)]
     (assoc db :queue (empty queue)))))

(re-frame/reg-event-db
 :set-queue-qty
 (fn [db [_ id qty]]
   (assoc-in db [:queue id] qty)))
