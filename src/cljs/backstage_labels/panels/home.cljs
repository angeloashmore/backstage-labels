(ns backstage-labels.panels.home
  (:require [re-frame.core :as re-frame :refer [subscribe]]
            [backstage-labels.routes :as routes]
            [backstage-labels.components.query-bar :as query-bar]))

(defn main
  []
  (let [filter-query (re-frame/subscribe [:filter-query])]
    [:div
     [:a {:href (routes/url-for :about)} "Go to about page"]
     [query-bar/main {:query @filter-query
                      :on-save #(re-frame/dispatch [:set-filter-query %])}]]))
