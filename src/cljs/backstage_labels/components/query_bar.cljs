(ns backstage-labels.components.query-bar
  (:require [cljs-css-modules.macro :refer-macros [defstyle]]))

(defstyle style
  [".query-bar" {:font-size "32px"}])

(defn main
  [{:keys [query on-save]}]
  (let [save #(on-save (-> % str clojure.string/trim))]
    [:input {:auto-focus true
             :class-name (:query-bar style)
             :on-change #(save (-> % .-target .-value))
             :type "text"
             :value query}]))