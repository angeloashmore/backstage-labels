(ns backstage-labels.db
  (:require [cljs.spec :as s]
            [backstage-labels.config :as config]))

(def default-db
  {:failed false
   :db-release-tag nil

   ;; Routing
   :active-panel :home-panel

   ;; Collections
   :collections (sorted-map)
   :collections-loading false
   :collections-error nil

   ;; Filters
   :filter-collection nil
   :filter-query nil

   ;; Labels
   :labels (sorted-map)
   :labels-loading false
   :labels-error nil

   ;; Print options
   :print-option-template config/default-print-option-template

   ;; Queue
   :queue (sorted-map)})
