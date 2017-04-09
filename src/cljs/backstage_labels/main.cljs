(ns backstage-labels.main
  (:require [cljs.nodejs :as nodejs]))

(def electron (nodejs/require "electron"))
(def app (.-app electron))
(def browser-window (.-BrowserWindow electron))

;; Debug switch
;; can be overridden by :closure-defines at compile time
(goog-define dev? false)

(def main-window (atom nil))

(def index-url
  (str "file://" (.getAppPath app) "/index.html"))

(defn- new-window
  [width height & opts]
  (->> (apply hash-map opts)
       (merge {:width width :height height})
       clj->js
       browser-window.))

(defn- init-browser
  []
  (reset! main-window
          (new-window 800 600
                      :autoHideMenuBar true
                      :webPreferences {:nodeIntegration false}))
  (.loadURL @main-window index-url)
  (when dev?
    (.openDevTools @main-window #js {:mode "undocked"}))
  (.on @main-window "closed" #(reset! main-window nil)))

(defn init
  []
  (.on app "window-all-closed"
       #(when-not (= (.platform nodejs/process "darwin"))
          (.quit app)))
  (.on app "ready" init-browser)
  (set! *main-cli-fn* (fn [] nil)))
