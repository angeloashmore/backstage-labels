(ns backstage-labels.components.query-bar)

(defn main
  [{:keys [query on-save]}]
  (let [save #(on-save (-> % str clojure.string/trim))]
    [:input {:auto-focus true
             :class-name "outline-0 f2"
             :on-change #(save (-> % .-target .-value))
             :type "text"
             :value query}]))
