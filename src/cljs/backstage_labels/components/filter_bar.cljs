(ns backstage-labels.components.filter-bar)

(defn query-bar
  [{:keys [query set-query]}]
  (let [save #(set-query (-> % str clojure.string/trim))]
    [:input {:auto-focus true
             :class-name "flex flex-auto h3 outline-0 pa3 f3 fw3 tracked bg-light-gray bw0"
             :style      {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15), inset -0.5px 0 0 rgba(0, 0, 0, 0.15)"}
             :on-change  #(save (-> % .-target .-value))
             :type       "text"
             :value      query}]))

(defn collection-bar
  [{:keys [collections collection set-collection]}]
  (let [all-val "all"
        save    #(set-collection (if (= % all-val) nil (-> % keyword)))]
    [:div {:class-name "flex items-center pa2 ph3 bg-white"
           :style      {:box-shadow "inset 0 -0.5px 0 rgba(0, 0, 0, 0.15), inset -0.5px 0 rgba(0, 0, 0, 0.15)"}}
     ;; Label
     [:div {:class-name "f7 gray mr2"}
      "Collection:"]

     ;; Select
     [:select {:class-name "input-reset flex flex-auto bn br2 pa1 ph2 f7 bg-white outline-0"
               :style {:box-shadow "0 0 0 0.5px rgba(0, 0, 0, 0.07), 0 0.5px 0.5px rgba(0, 0, 0, 0.2)"}
               :on-change #(save (-> % .-target .-value))}
      [:option {:key nil :value all-val} "All labels"]
      (for [[k v] collections]
        [:option {:key k :value k} (:key v)])]]))

(defn main
  [{:keys [query set-query collections collection set-collection]}]
  [:div {:class-name "flex flex-column flex-none"}
   [query-bar {:query query
               :set-query set-query}]
   [collection-bar {:collections    collections
                    :collection     collection
                    :set-collection set-collection}]])
