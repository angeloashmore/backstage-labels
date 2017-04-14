(ns backstage-labels.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [cljs-css-modules.macro :refer-macros [defstyle]]
            [backstage-labels.routes :as routes]
            [backstage-labels.filters.views :as filters-views]))

(defstyle style
  [".app" {:display "flex"
           :font-family "BlinkMacSystemFont"
           :height "100%"
           :user-select "none"}])

(defn home-panel
  []
  (let [filter-query (re-frame/subscribe [:filter-query])]
    [:div {:class-name (:home style)}
     [:a {:href (routes/url-for :about)} "Go to about page"]
     [filters-views/query-bar {:query @filter-query
                               :on-save #(re-frame/dispatch [:set-filter-query %])}]]))

(defn about-panel
  []
  (fn []
    [:div {:class-name (:about style)} "This is the About Page"
     [:div [:a {:href (routes/url-for :home)} "go to Home Page"]]]))

;; ---------------------------

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div {:class-name (:app style)}
       (panels @active-panel)])))
