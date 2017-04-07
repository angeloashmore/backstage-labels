(set-env!
 :source-paths    #{"src/cljs"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs            "2.0.0"      :scope "test"]
                 [adzerk/boot-cljs-repl       "0.3.3"      :scope "test"]
                 [adzerk/boot-reload          "0.5.1"      :scope "test"]
                 [pandeiro/boot-http          "0.7.6"      :scope "test"]
                 [com.cemerick/piggieback     "0.2.1"      :scope "test"]
                 [org.clojure/tools.nrepl     "0.2.12"     :scope "test"]
                 [weasel                      "0.7.0"      :scope "test"]
                 [org.clojure/clojurescript   "1.9.494"]
                 [reagent                     "0.6.1"]
                 [re-frame                    "0.9.2"]
                 [re-frisk                    "0.4.4"]
                 [day8.re-frame/async-flow-fx "0.0.6"]
                 [day8.re-frame/http-fx       "0.1.3"]
                 [bidi                        "2.0.16"]
                 [kibu/pushy                  "0.3.7"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]])

(deftask build []
  (comp (speak)

        (cljs)
        ))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)

        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none}
                 reload {:on-jsload 'backstage-labels.core/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))
