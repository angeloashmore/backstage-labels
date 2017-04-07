(ns backstage-labels.db
  (:require [cljs.spec :as s]
            [backstage-labels.config :as config]))

(def default-db
  {:active-panel :home-panel

   :collections (sorted-map)
   :collections-loading false
   :collections-error nil

   :filter-collection nil
   :filter-query nil

   :labels (sorted-map)
   :labels-loading false
   :labels-error nil

   :print-option-template config/default-print-option-template

   :queue (sorted-map)})
