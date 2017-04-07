(ns backstage-labels.renderer
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-frisk.core :refer [enable-re-frisk!]]
            [backstage-labels.events]
            [backstage-labels.subs]
            [backstage-labels.routes :as routes]
            [backstage-labels.views :as views]
            [backstage-labels.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-re-frisk!)
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:boot])
  (dev-setup)
  (mount-root))
