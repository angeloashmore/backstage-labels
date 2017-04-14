(ns backstage-labels.collections.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
            [ajax.core :as ajax]
            [backstage-labels.config :as config]))

;; Requests collections for provided tag. Dispatches
;; :request-collections-success on success, or :request-collections-failure on
;; failure.
(re-frame/reg-event-fx
 :request-collections
 (fn [{db :db} [_ tag]]
   (let [uri (str config/downloads-uri (name tag) "/collections.json")]
     {:db         (assoc db :collections-loading true)
      :http-xhrio {:method          :get
                   :uri             uri
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:request-collections-success]
                   :on-failure      [:request-collections-failure]}})))

;; Sets collections list.
;;
;; Dispatched on successful network request of collections.
(re-frame/reg-event-db
 :request-collections-success
 (fn [db [_ collections]]
   (-> db
       (assoc :collections collections)
       (assoc :collections-loading false))))

;; Sets the whole app as failed.
;;
;; Dispatched on unsuccessful network request of collections.
(re-frame/reg-event-fx
 :request-collections-failure
 (fn [{db :db} _]
   {:db       (assoc db :collections-loading false)
    :dispatch [:app-failure]}))
