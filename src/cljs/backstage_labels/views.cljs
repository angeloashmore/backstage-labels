(ns backstage-labels.views
  (:require [re-frame.core :as re-frame :refer [subscribe]]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [backstage-labels.panels.home :as home]
            [backstage-labels.panels.about :as about]))

(defstyle style
  [".main" {:display "flex"
            :font-family "BlinkMacSystemFont"
            :height "100%"
            :user-select "none"}])

(defmulti panels identity)
(defmethod panels :home-panel [] [home/main])
(defmethod panels :about-panel [] [about/main])
(defmethod panels :default [] [:div])

(defn main-panel
  []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div {:class-name (:main style)} (panels @active-panel)])))
