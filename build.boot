(set-env!
 :source-paths    #{"src/cljs"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs            "2.0.0"      :scope "test"]
                 [adzerk/boot-cljs-repl       "0.3.3"      :scope "test"]
                 [adzerk/boot-reload          "0.5.1"      :scope "test"]
                 [com.cemerick/piggieback     "0.2.1"      :scope "test"]
                 [org.clojure/tools.nrepl     "0.2.12"     :scope "test"]
                 [weasel                      "0.7.0"      :scope "test"]
                 [org.clojure/clojurescript   "1.9.494"]
                 [reagent                     "0.6.1"]
                 [re-frame                    "0.9.2"]
                 [day8.re-frame/async-flow-fx "0.0.6"]
                 [day8.re-frame/http-fx       "0.1.3"]
                 [bidi                        "2.0.16"]
                 [kibu/pushy                  "0.3.7"]
                 [re-frisk                    "0.4.4"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]])

(deftask build-main
  "Build main entry point for debug."
  []
  (comp (cljs :ids #{"js/main"}
              :compiler-options {:closure-defines {'backstage-labels.main/dev? true}
                                 :asset-path "target/js/main.out"})))

(deftask build-renderer
  "Build renderer entry point for debug."
  []
  (comp (cljs :ids #{"js/renderer"}
              :compiler-options {:asset-path "js/renderer.out"})))

(deftask debug
  "Debug build."
  []
  (comp (build-main)
        (build-renderer)
        (target)))

(deftask dev
  "Build and setup development environment."
  []
  (comp (watch)
        (speak)
        (cljs-repl :ids #{"js/renderer"})
        (reload :ids #{"js/renderer"}
                :ws-host "localhost"
                :target-path "target")
        (build-renderer)
        (build-main)
        (target)))

(deftask release
  "Release build."
  []
  (comp (cljs :ids #{"js/main"}
              :optimizations :simple)
        (cljs :ids #{"js/renderer"}
              :optimizations :advanced)
        (target :dir #{"release"})))
