(ns backstage-labels.components.app
  (:require [cljs-css-modules.macro :refer-macros [defstyle]]))

(defstyle style
  [".main" {:display "flex"
            :font-family "BlinkMacSystemFont"
            :height "100%"
            :user-select "none"}])

(defn main
  [{:keys [panel]}]
  (fn []
    [:div {:class-name (:main style)} panel]))
