(ns backstage-labels.renderer
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [backstage-labels.events]
            [backstage-labels.subs]
            [backstage-labels.routes :as routes]
            [backstage-labels.views :as views]
            [backstage-labels.config :as config]))

(defn dev-setup
  []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root
  []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init
  []
  (routes/app-routes)
  (re-frame/dispatch-sync [:boot])
  (dev-setup)
  (mount-root))
