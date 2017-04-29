(ns backstage-labels.components.label)

(defn details
  [{:keys [key category metadata]}]
  (let [info (cons category (vals metadata))
        i    (atom -1)]
    [:div {:class "flex-auto mh3"}
     [:span {:class "block pb1 f4 tracked"} key]
     [:ul {:class "overflow-hidden nowrap pa0 f7 gray"}
      (for [item info]
        (do
          (swap! i inc)
          [:li {:key @i :class "dib mr2"} item]))]]))

(defn main
  [{:keys [label accessory-left accessory-right on-double-click]}]
  [:div {:class           "flex items-center pa2 ph3"
         :on-double-click on-double-click}
   (when-not (nil? accessory-left)
     [accessory-left {:label label}])
   [details label]
   (when-not (nil? accessory-right)
     [accessory-right {:label label}])])
