(ns backstage-labels.views
  (:require [re-frame.core :as re-frame :refer [subscribe]]
            [backstage-labels.panels.home :as home]
            [backstage-labels.panels.about :as about]))

(defmulti panels identity)
(defmethod panels :home-panel [] [home/main])
(defmethod panels :about-panel [] [about/main])
(defmethod panels :default [] [:div])

(defn main-panel
  []
  (let [active-panel (subscribe [:active-panel])]
    (fn []
      ;; Set cursor to pointer to simulate a native app.
      [:div {:class "flex flex-auto sans-serif us--none fsa"
             :style {:cursor "default"}}
       (panels @active-panel)])))
