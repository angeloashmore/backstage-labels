(ns backstage-labels.db
  (:require [cljs.spec :as s]
            [backstage-labels.config :as config]))

(def default-db
  {:failed false

   ;; Routing
   :active-panel :home-panel

   ;; DB releases
   :release-tag nil
   :release-tags []
   :release-tags-loading false

   ;; Collections
   :collections []
   :collections-loading false

   ;; Filters
   :filter-collection nil
   :filter-query nil

   ;; Labels
   :labels {}
   :labels-loading false

   ;; Print options
   :print-option-template config/default-print-option-template

   ;; Queue
   :queue []})
