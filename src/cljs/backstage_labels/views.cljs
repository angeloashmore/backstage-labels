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
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div {:class-name "flex h-100 sans-serif us--none"}
       (panels @active-panel)])))
