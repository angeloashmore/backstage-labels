(ns backstage-labels.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
            [day8.re-frame.async-flow-fx]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [backstage-labels.config :as config]
            [backstage-labels.db :as db]
            [backstage-labels.collections.events]
            [backstage-labels.filters.events]
            [backstage-labels.labels.events]
            [backstage-labels.queue.events]
            [backstage-labels.releases.events]))

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
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;; -- Print options ------------------------------------------------------------

(re-frame/reg-event-db
 :set-print-option-template
 (fn [db [_ template]]
   (assoc db :print-option-template template)))
