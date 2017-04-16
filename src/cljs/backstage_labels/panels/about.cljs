(ns backstage-labels.panels.about
  (:require [backstage-labels.routes :as routes]))

(defn main
  []
  (fn []
    [:div "This is the About Page"
     [:div [:a {:href (routes/url-for :home)} "go to Home Page"]]]))
