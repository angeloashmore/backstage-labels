(ns backstage-labels.releases.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
            [ajax.core :as ajax]
            [backstage-labels.config :as config]))

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
