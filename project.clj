;   Copyright (c) 2014 Kevin Bell. All rights reserved.
;   See the file license.txt for copying permission.

(def less-cmd
  ["shell" "lessc" "stylesheets/style.less"
   "static/css/style.css"
   "--include-path=bower_components/bootstrap/less/"])

(defproject dacom "0.1.0-SNAPSHOT"
  :description "A skeleton app built with datomic, compojure, and om"
  :url "https://github.com/bellkev/dacom"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [ring "1.2.1"]
                 [ring-cors "0.1.0"]
                 [ring-middleware-format "0.3.1"]
                 [compojure "1.1.6"]
                 [cljs-ajax "0.2.3"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.1.4"]
                 [sablono "0.1.5"]
                 [com.datomic/datomic-free "0.9.4384"]]
  :plugins [[lein-cljsbuild "1.0.1-SNAPSHOT"]
            [com.cemerick/austin "0.1.4-SNAPSHOT"]
            [lein-ring "0.8.8"]
            [lein-resource "0.3.1"]
            [lein-httpd "1.0.0"]
            [lein-shell "0.3.0"]
            [fsrun "0.1.2"]]
  :source-paths ["src"]
  :target-path "target/%s/"
  :omit-source true
  :uberjar-exclusions [#"src/dacom/repl.clj" #"src/dacom/client.cljs" #"src/dacom/db.clj"
                       #"src/dacom/resources/.*"]
  :cljsbuild {:builds {:dev {:source-paths ["src"]
                             :compiler {:output-to "static/js/main.js"
                                        :output-dir "static/js"
                                        :optimizations :none
                                        :pretty-print true
                                        :source-map true}}}}
  :ring {:handler dacom.server/app}
  :lesscss-paths "stylesheets"
  :lesscss-output-path "static/css"
  :profiles {:dev {:repl-options {:init-ns dacom.repl}
                   :resource {:resource-paths ["pages"]
                              :target-path "static"
                              :extra-values {:scripts [{:src "../bower_components/react/react.js"}
                                                       {:src "js/goog/base.js"}
                                                       {:src "js/main.js"}
                                                       {:body "goog.require('dacom.client')"}]}}}
             :db {:main dacom.db}
             :uberjar {:aot :all}}
  :aliases {"bower" ["shell" "bower" "install"]
            "less-debug" ~(conj less-cmd "--source-map")
            "less-prod" ~(conj less-cmd "--compress")
            "watch-less" ["fschange" "stylesheets/*" "less-debug"]
            "install-db" ["with-profile" "db" "run"]
            "run-client" ["do" "bower," "cljsbuild" "once," "less-debug," "resource," "httpd" "8000"]
            "run-server" ["ring" "server-headless"]}
  :clean-targets [:target-path :compile-path "static"])