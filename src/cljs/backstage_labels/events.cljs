(ns backstage-labels.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
            [day8.re-frame.async-flow-fx]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [backstage-labels.config :as config]
            [backstage-labels.db :as db]))

(re-frame/reg-event-db
 :app-failure
 (fn [db _]
   (assoc db :failed true)))

;; -- Boot ---------------------------------------------------------------------

;; Async flow for boot process. Requests DB release tags, then sets selected tag
;; to latest (resolved in :set-db-release-tag event handler to latest tag name).
(defn boot-flow
  []
  {:first-dispatch [:request-db-release-tags]
   :rules [{:when     :seen?
            :events   :set-db-release-tags
            :dispatch [:set-db-release-tag :latest]}]})

;; Sets the default app-db and fires off the boot flow.
(re-frame/reg-event-fx
 :boot
 (fn [_ _]
   {:db         db/default-db
    :async-flow (boot-flow)}))

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

;; Sets the current label DB release tag, empties the label queue, and
;; dispatches :request-collections and :request-labels.
(re-frame/reg-event-fx
 :set-db-release-tag
 (fn [{db :db} [_ provided-tag]]
   (let [latest-tag (first (:db-release-tags db))
         tag        (if (= provided-tag :latest)
                      latest-tag
                      provided-tag)]
     {:db         (assoc db :db-release-tag tag)
      :dispatch-n (list [:empty-queue]
                        [:request-collections]
                        [:request-labels])})))

;; Requests all available label DB releases. Dispatches
;; :request-db-release-tags-success on success, or
;; :request-db-release-tags-failure on failure.
(re-frame/reg-event-fx
 :request-db-release-tags
 (fn [{db :db} _]
   {:db         (assoc db :db-release-tags-loading true)
    :http-xhrio {:method          :get
                 :uri             config/db-releases-uri
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:request-db-release-tags-success]
                 :on-failure      [:request-db-release-tags-failure]}}))

;; Sets label DB releases list with :latest injected as the first element.
;;
;; Dispatched on successful network request of label DB releases.
(re-frame/reg-event-fx
 :request-db-release-tags-success
 (fn [{db :db} [_ releases]]
   (let [tags (->> releases
                   (map :tag_name)
                   (map keyword))]
     {:db       (assoc db :db-release-tags-loading false)
      :dispatch [:set-db-release-tags tags]})))

;; Sets the whole app as failed.
;;
;; Dispatched on unsuccessful network request of label DB releases.
(re-frame/reg-event-fx
 :request-db-release-tags-failure
 (fn [{db :db} _]
   {:db       (assoc db :db-release-tags-loading false)
    :dispatch [:app-failure]}))

;; -- Collections --------------------------------------------------------------

;; Requests collections from current :db-release-tag. Dispatches
;; :request-collections-success on success, or :request-collections-failure on
;; failure.
(re-frame/reg-event-fx
 :request-collections
 (fn [{db :db} _]
   (let [tag   (:db-release-tag db)
         uri   (str config/db-downloads-uri (name tag) "/collections.json")]
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
   (let [tag   (:db-release-tag db)
         uri   (str config/db-downloads-uri (name tag) "/labels.json")]
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
