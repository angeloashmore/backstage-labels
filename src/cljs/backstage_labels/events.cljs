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
  {:first-dispatch [:request-release-tags]
   :rules [{:when     :seen?
            :events   :set-release-tags
            :dispatch [:set-release-tag :latest]}]})

;; Sets the default app-db and fires off the boot flow.
(re-frame/reg-event-fx
 :boot
 (fn [_ _]
   {:db         db/default-db
    :async-flow (boot-flow)}))

;; -- Routing ------------------------------------------------------------------

(re-frame/reg-event-db
 :set-active-panel
 [re-frame/debug]
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;; -- Releases -----------------------------------------------------------------

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
;; The :release-tag key in app-db allows the ability to set the current
;; database version viewable in the app. This provides pseudo-time-traveling
;; when labels from an older release are needed.
;;
;; Due to the indexing method of the label data, the queue must be cleared
;; before loading another database release (label UUID reuse/unavailability).

;; Sets the list of available label DB release tags.
(re-frame/reg-event-db
 :set-release-tags
 (fn [db [_ tags]]
   (assoc db :release-tags tags)))

;; Sets the current label DB release tag, empties the label queue, and
;; dispatches :request-collections and :request-labels.
(re-frame/reg-event-fx
 :set-release-tag
 (fn [{db :db} [_ provided-tag]]
   (let [latest-tag (first (:release-tags db))
         tag        (if (= provided-tag :latest)
                      latest-tag
                      provided-tag)]
     {:db         (assoc db :release-tag tag)
      :dispatch-n (list [:empty-queue]
                        [:request-collections tag]
                        [:request-labels tag])})))

;; Requests all available label DB releases. Dispatches
;; :request-release-tags-success on success, or :request-release-tags-failure on
;; failure.
(re-frame/reg-event-fx
 :request-release-tags
 (fn [{db :db} _]
   {:db         (assoc db :release-tags-loading true)
    :http-xhrio {:method          :get
                 :uri             config/releases-uri
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:request-release-tags-success]
                 :on-failure      [:request-release-tags-failure]}}))

;; Sets label DB releases list.
;;
;; Dispatched on successful network request of label DB releases.
(re-frame/reg-event-fx
 :request-release-tags-success
 (fn [{db :db} [_ releases]]
   (let [tags (->> releases
                   (map :tag_name)
                   (map keyword))]
     {:db       (assoc db :release-tags-loading false)
      :dispatch [:set-release-tags tags]})))

;; Sets the whole app as failed.
;;
;; Dispatched on unsuccessful network request of label DB releases.
(re-frame/reg-event-fx
 :request-release-tags-failure
 (fn [{db :db} _]
   {:db       (assoc db :release-tags-loading false)
    :dispatch [:app-failure]}))

;; -- Collections --------------------------------------------------------------

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

;; -- Labels -------------------------------------------------------------------

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

;; Queues label at end of vector. If the most recently queued label is the same
;; ID, this adds the quantity to the existing quantity. Doing so ensures labels
;; are queued in the same order as entered.
;;
;; Queue items are stored in a two element vector:
;;
;;   [label-id qty]
(re-frame/reg-event-db
 :queue-label
 (fn [db [_ id qty]]
   (let [latest     (-> db :queue peek)
         latest-id  (first latest)
         latest-qty (last latest)
         last-index (-> db :queue count)]
     (if (= id latest-id)
       (update-in db [:queue] assoc last-index [id (+ qty latest-qty)])
       (update-in db [:queue] conj [id qty])))))

;; Dequeues label at the specific index.
(re-frame/reg-event-db
 :dequeue-label
 (fn [db [_ index]]
   (update-in db [:queue] #(vec (concat (subvec % 0 (- index 1))
                                        (subvec % (+ index 1)))))))

;; Removes all queued labels.
(re-frame/reg-event-db
 :empty-queue
 (fn [db _]
   (assoc db :queue (-> db :queue empty))))

;; Sets the quantity of a queued label at the specified index.
(re-frame/reg-event-db
 :set-queue-qty
 (fn [db [_ index qty]]
   (let [[id _] (nth (:queue db) index)]
     (update-in db [:queue] assoc index [id qty]))))
