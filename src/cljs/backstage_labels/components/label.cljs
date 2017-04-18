(ns backstage-labels.components.label)

(defn details
  [{:keys [key category metadata]}]
  (let [info (cons category (vals metadata))
        i    (atom -1)]
    [:div {:class-name "flex-auto"}
     [:span {:class-name "block pb1 f4 tracked"} key]
     [:ul {:class-name "overflow-hidden nowrap pa0 f7 gray"}
      (for [item info]
        (do
          (swap! i inc)
          [:li {:key @i :class-name "dib mr2"} item]))]]))

(defn main
  [{:keys [label accessory-left accessory-right on-double-click]}]
  [:li {:class-name "flex pa2 ph3"
        :on-double-click on-double-click}
   (when-not (nil? accessory-left)
     [accessory-left {:label label}])
   [details label]
   (when-not (nil? accessory-right)
     [accessory-right {:label label}])])
