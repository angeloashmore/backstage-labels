(ns backstage-labels.views
  (:require [re-frame.core :as re-frame :refer [subscribe]]
            [cljs-css-modules.macro :as css-modules]
            [backstage-labels.panels.home :as home]
            [backstage-labels.panels.about :as about]))

(css-modules/defstyle style
  [".container" {:display "flex"
                 :flex "1 1 auto"
                 :font-family "system, -apple-system, BlinkMacSystemFont, Helvetica Neue, Lucida Grande"
                 :user-select "none"}])

(defmulti panels identity)
(defmethod panels :home-panel [] [home/main])
(defmethod panels :about-panel [] [about/main])
(defmethod panels :default [] [:div])

(defn main-panel
  []
  (let [active-panel (subscribe [:active-panel])]
    (fn []
      [:div {:class (:container style)}
       (panels @active-panel)])))
