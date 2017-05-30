(ns backstage-labels.effects
  (:require [re-frame.core :as re-frame]))

;; Focuses and selects an element that matches the provided ID. Typically used
;; for input elements.
(re-frame/reg-fx
 :select-element
 (fn [value]
   (->> value
        (.getElementById js/document)
        .select)))
