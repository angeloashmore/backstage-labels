(ns backstage-labels.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [backstage-labels.routes :as routes]))

(defn query-bar
  [{:keys [query on-change]}]
  (let [save #(on-change (-> % str clojure.string/trim))]
    [:input {:type "text"
             :value query
             :auto-focus true
             :on-change #(save (-> % .-target .-value))}]))

;; --------------------

(defn home-panel
  []
  (let [filter-query (re-frame/subscribe [:filter-query])]
    [query-bar {:query @filter-query
                :on-change #(re-frame/dispatch [:set-filter-query %])}]))

(defn about-panel
  []
  (fn []
    [:div "This is the About Page"
     [:div [:a {:href (routes/url-for :home)} "go to Home Page"]]]))

;; --------------------

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      (panels @active-panel))))
