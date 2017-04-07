(ns backstage-labels.events
  (:require [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
            [backstage-labels.db :as db]))

;; (def labels-db-releases-uri
;;   "https://api.github.com/repos/angeloashmore/boh-labels-db/releases/latest")

;; (re-frame/reg-event-fx
;;  :request-labels-db
;;  (fn [_ _]
;;    {:http-xhrio {:method          :get
;;                  :uri             labels-db-releases-uri
;;                  :format          (ajax/json-request-format)
;;                  :response-format (ajax/json-request-format {:keywords? true})
;;                  :on-success      [:request-labels-db-success]
;;                  :on-failure      [:request-labels-db-failure]}
;;     :db (assoc db :loading true)}))

;; (re-frame/reg-event-fx
;;  :request-labels-db-success
;;  (fn [db [_ response]]
;;    {:http-xhrio {:method          :get
;;                  :uri             labels-db-releases-uri
;;                  :format          (ajax/json-request-format)
;;                  :response-format (ajax/json-request-format {:keywords? true})
;;                  :on-success      [:request-labels-db-success]
;;                  :on-failure      [:request-labels-db-failure]}
;;     :db (assoc db :loading true)}))

(re-frame/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

;; -- Routing ------------------------------------------------------------------

(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;; -- Filters ------------------------------------------------------------------

(re-frame/reg-event-db
 :set-filter-collection
 (fn [db [_ id]]
   (assoc db :filter-collection id)))

(re-frame/reg-event-db
 :set-filter-query
 (fn [db [_ query]]
   (assoc db :filter-query query)))

;; -- Print options ------------------------------------------------------------

(re-frame/reg-event-db
 :set-print-option-template
 (fn [db [_ template]]
   (assoc db :print-option-template template)))

;; -- Queue --------------------------------------------------------------------

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
