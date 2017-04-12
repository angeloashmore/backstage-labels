(ns backstage-labels.db
  (:require [cljs.spec :as s]
            [backstage-labels.config :as config]))

(def default-db
  {:failed false

   ;; Routing
   :active-panel :home-panel

   ;; DB releases
   :db-release-tag nil
   :db-release-tags ()
   :db-release-tags-loading false

   ;; Collections
   :collections (sorted-map)
   :collections-loading false

   ;; Filters
   :filter-collection nil
   :filter-query nil

   ;; Labels
   :labels (sorted-map)
   :labels-loading false

   ;; Print options
   :print-option-template config/default-print-option-template

   ;; Queue
   :queue (sorted-map)})
