(ns backstage-labels.labels.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
            [ajax.core :as ajax]
            [backstage-labels.config :as config]))

;; Requests labels for provided tag. Dispatches :request-labels-success on
;; success, or :request-labels-failure on failure.
(re-frame/reg-event-fx
 :request-labels
 (fn [{db :db} [_ tag]]
   (let [uri (str config/downloads-uri (name tag) "/labels.json")]
     {:db         (assoc db :labels-loading true)
      :http-xhrio {:method          :get
                   :uri             uri
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:request-labels-success]
                   :on-failure      [:request-labels-failure]}})))

;; Sets labels list.
;;
;; Dispatched on successful network request of labels.
(re-frame/reg-event-db
 :request-labels-success
 (fn [db [_ labels]]
   (-> db
       (assoc :labels labels)
       (assoc :labels-loading false))))

;; Sets the whole app as failed.
;;
;; Dispatched on unsuccessful network request of labels.
(re-frame/reg-event-fx
 :request-labels-failure
 (fn [{db :db} _]
   {:db       (assoc db :labels-loading false)
    :dispatch [:app-failure]}))
