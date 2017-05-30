(ns backstage-labels.views
  (:require [re-frame.core :as re-frame]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [garden.units :as units]
            [backstage-labels.panels.home :as home]
            [backstage-labels.panels.output :as output]))

(defstyle style
  ;; Ensure only the active panel is visible when not printing.
  (at-media {:print false}
            [".container" {:height (units/vh 100)}]
            [".active-panel" {:height (units/percent 100)}]
            [".output-panel" {:display "none"}])

  ;; Ensure only the print panel is visible when printing.
  (at-media {:print :only}
            [".active-panel" {:display "none"}]))

(defmulti panels identity)
(defmethod panels :home-panel [] [home/main])
(defmethod panels :output-panel [] [output/main])
(defmethod panels :default [] [:div])

(defn main-panel
  []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div {:class (:container style)}
       [:div {:class (:active-panel style)}
        (panels @active-panel)]
       [:div {:class (:output-panel style)}
        (panels :output-panel)]])))
