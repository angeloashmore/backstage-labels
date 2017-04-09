(ns backstage-labels.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [backstage-labels.config :as config]
            [backstage-labels.db :as db]))

;; -- Boot ---------------------------------------------------------------------

(re-frame/reg-event-fx
 :boot
 (fn [_ _]
   {:db       (-> db/default-db
                  (assoc :collections-loading true)
                  (assoc :labels-loading true))
    :dispatch [:request-db-release-tags]}))

(re-frame/reg-event-db
 :app-failure
 (fn [db _]
   (assoc db :failed true)))

;; -- Routing ------------------------------------------------------------------

(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;; -- DB Releases --------------------------------------------------------------
;;
;; The label database is available as tagged releases separated in two files:
;;
;;   - collections.json
;;       - Groups of labels (e.g. iPad, iPhone, MacBook)
;;       - References label data by label UUID (i.e. normalized)
;;
;;   - labels.json
;;       - All label data
;;       - Each label identified by a UUID
;;
;; The :db-release-tag key in app-db allows the ability to set the current
;; database version viewable in the app. This provides pseudo-time-traveling
;; when labels from an older release are needed.
;;
;; Due to the indexing method of the label data, the queue must be cleared
;; before loading another database release (label UUID reuse/unavailability).

;; Sets the list of available label DB release tags.
(re-frame/reg-event-db
 :set-db-release-tags
 (fn [db [_ tags]]
   (assoc db :db-release-tags tags)))

;; Sets the current label DB release tag using the desired index of
;; :db-release-tags and dispatches :request-collections and :request-labels.
(re-frame/reg-event-fx
 :set-db-release-tag
 (fn [{:keys [db]} [_ index]]
   {:db         (assoc db :db-release-tag index)
    :dispatch-n (list [:request-collections]
                      [:request-labels])}))

;; Requests all available label DB releases. Dispatches
;; :request-db-release-tags-success on success, or
;; :request-db-release-tags-failure on failure.
(re-frame/reg-event-fx
 :request-db-release-tags
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             config/db-releases-uri
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:request-db-release-tags-success]
                 :on-failure      [:request-db-release-tags-failure]}}))

;; Sets label DB releases list and sets the current tag to the latest.
;;
;; Dispatched on successful network request of label DB releases.
(re-frame/reg-event-fx
 :request-db-release-tags-success
 (fn [_ [_ releases]]
   (let [tags   (map :tag_name releases)]
     {:dispatch-n (list [:set-db-release-tags tags]
                        [:set-db-release-tag 0])})))

;; Sets the whole app as failed.
;;
;; Dispatched on unsuccessful network request of label DB releases.
(re-frame/reg-event-fx
 :request-db-release-tags-failure
 (fn [_ _]
   {:dispatch [:app-failure]}))

;; -- Collections --------------------------------------------------------------

;; Requests collections from current :db-release-tag. Dispatches
;; :request-collections-success on success, or :request-collections-failure on
;; failure.
(re-frame/reg-event-fx
 :request-collections
 (fn [{db :db} _]
   (let [index (:db-release-tag db)
         tags  (:db-release-tags db)
         tag   (nth tags index)
         uri   (str config/db-downloads-uri tag "/collections.json")]
     {:http-xhrio {:method          :get
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
   (assoc db :collections collections)))

;; Sets the whole app as failed.
;;
;; Dispatched on unsuccessful network request of collections.
(re-frame/reg-event-fx
 :request-collections-failure
 (fn [_ _]
   {:dispatch [:app-failure]}))

;; -- Filters ------------------------------------------------------------------

(re-frame/reg-event-db
 :set-filter-collection
 (fn [db [_ id]]
   (assoc db :filter-collection id)))

(re-frame/reg-event-db
 :set-filter-query
 (fn [db [_ query]]
   (assoc db :filter-query query)))

;; -- Labels --------------------------------------------------------------

;; Requests labels from current :db-release-tag. Dispatches
;; :request-labels-success on success, or :request-labels-failure on failure.
(re-frame/reg-event-fx
 :request-labels
 (fn [{db :db} _]
   (let [index (:db-release-tag db)
         tags  (:db-release-tags db)
         tag   (nth tags index)
         uri   (str config/db-downloads-uri tag "/labels.json")]
     {:http-xhrio {:method          :get
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
   (assoc db :labels labels)))

;; Sets the whole app as failed.
;;
;; Dispatched on unsuccessful network request of labels.
(re-frame/reg-event-fx
 :request-labels-failure
 (fn [_ _]
   {:dispatch [:app-failure]}))

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
