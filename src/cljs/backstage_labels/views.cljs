(ns backstage-labels.views
  (:require [re-frame.core :as re-frame]
            [backstage-labels.routes :as routes]))

;; --------------------

(defn home-panel []
  (let [initialized? (re-frame/subscribe [:initialized?])
        failed (re-frame/subscribe [:failed])
        db-release-tag (re-frame/subscribe [:db-release-tag])
        db-release-tags (re-frame/subscribe [:db-release-tags])
        collections (re-frame/subscribe [:collections])
        labels (re-frame/subscribe [:labels])]
    (fn []
      [:div (str "Did the app fail? " @failed)
       [:div (str "Initialized? " @initialized?)]
       [:div (str "DB release tag: " @db-release-tag)]
       [:div (str "DB release tags: " @db-release-tags)]
       [:div (str "How many collections? " (count @collections))]
       [:div (str "How many labels? " (count @labels))]
       [:div [:a {:href (routes/url-for :about)} "go to About Page"]]])))

(defn about-panel []
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
