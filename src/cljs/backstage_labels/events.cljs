(ns backstage-labels.events
  (:require [day8.re-frame.async-flow-fx]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
            [backstage-labels.config :as config]
            [backstage-labels.db :as db]))

;; -- Boot ---------------------------------------------------------------------

(defn boot-flow []
  {:first-dispatch [:request-db-release]
   :rules [{:when :seen?        :events :request-db-release-success  :dispatch-n (list [:request-labels] [:request-collections])}
           {:when :seen?        :events :request-labels-success      :dispatch   [:set-labels]}
           {:when :seen?        :events :request-collections-success :dispatch   [:set-collections]}
           {:when :seen-any-of? :events [:request-db-release-failure :request-labels-failure :request-collections-failure] :dispatch [:boot-failure] :halt? true}]})

(defn- set-initial-loading
  [db]
  (-> db
      (assoc :collections-loading true)
      (assoc :labels-loading true)))

(re-frame/reg-event-fx
 :boot
 (fn [_ _]
   {:db (-> db/default-db
            (set-initial-loading))
    :async-flow (boot-flow)}))

(re-frame/reg-event-db
 :boot-failure
 (fn [db _]
   (assoc db :failed true)))

(re-frame/reg-event-fx
 :request-db-release
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             config/db-release-uri
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:request-db-release-success]
                 :on-failure      [:request-db-release-failure]}}))

(re-frame/reg-event-db
 :request-db-release-success
 (fn [db [_ release]]
   (assoc db :db-release-tag (:tag_name release))))

(re-frame/reg-event-db
 :request-db-release-failure
 (fn []))

;; -- Routing ------------------------------------------------------------------

(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

;; -- Collections --------------------------------------------------------------

(re-frame/reg-event-fx
 :request-collections
 (fn [{db :db} _]
   (let [tag (:db-release-tag db)]
     {:http-xhrio {:method          :get
                   :uri             (str "https://github.com/angeloashmore/boh-labels-db/releases/download/" tag "/collections.json")
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:request-collections-success]
                   :on-failure      [:request-collections-failure]}})))

(re-frame/reg-event-fx
 :request-collections-success
 (fn []))

(re-frame/reg-event-fx
 :request-collections-failure
 (fn []))

(re-frame/reg-event-db
 :set-collections
 (fn [db [_ collections]]
   (assoc db :collections collections)))

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

(re-frame/reg-event-fx
 :request-labels
 (fn [{db :db} _]
   (let [tag (:db-release-tag db)]
     {:http-xhrio {:method          :get
                   :uri             (str "https://github.com/angeloashmore/boh-labels-db/releases/download/" tag "/labels.json")
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:request-labels-success]
                   :on-failure      [:request-labels-failure]}})))

(re-frame/reg-event-fx
 :request-labels-success
 (fn []))

(re-frame/reg-event-fx
 :request-labels-failure
 (fn []))

(re-frame/reg-event-db
 :set-labels
 (fn [db [_ labels]]
   (assoc db :labels labels)))

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
