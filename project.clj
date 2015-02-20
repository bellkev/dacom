;   Copyright (c) 2014 Kevin Bell. All rights reserved.
;   See the file license.txt for copying permission.

(defproject dacom "0.1.4"
  :description "A skeleton app built with datomic, compojure, and om"
  :url "https://github.com/bellkev/dacom"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2850"]
                 [ring "1.3.2"]
                 [ring-cors "0.1.6"]
                 [ring-middleware-format "0.4.0"]
                 [compojure "1.3.2"]
                 [cljs-ajax "0.3.10"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.7.3"]
                 [sablono "0.3.4"]
                 [com.datomic/datomic-free "0.9.5130"]]
  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-ring "0.9.1"]
            [lein-resource "14.10.1"]
            [lein-httpd "1.0.0"]
            [lein-shell "0.4.0"]
            [fsrun "0.1.2"]]
  :source-paths ["src"]
  :target-path "target/"
  :uberjar-exclusions [#".*\.cljs"]
  :cljsbuild {:builds {:dev {:source-paths ["utils/src" "src"]
                             :compiler {:output-to "static/js/main.js"
                                        :output-dir "static/js"
                                        :optimizations :none
                                        :pretty-print true
                                        :source-map true}}
                       :prod {:source-paths ["src"]
                              :compiler {:output-to "dist/static/js/main.js"
                                         :optimizations :advanced
                                         :pretty-print false
                                         ;; From Om jar
                                         :preamble ["react/react.min.js"]
                                         :externs ["react/externs/react.js"]}}}}
  :ring {:init dacom.server/init-conn
         :handler dacom.server/app}
  :profiles {:dev {;; This needs to be here because of https://github.com/cemerick/austin/issues/23
                    :plugins [[com.cemerick/austin "0.1.6"]]
                    :source-paths ["utils/src"]
                    :repl-options {:init-ns dacom.repl}
                    :resource {:resource-paths ["web-resources/pages"]
                               :target-path "static"
                               :extra-values {:scripts [{:src "../bower_components/react/react.js"}
                                                        {:src "js/goog/base.js"}
                                                        {:src "js/main.js"}
                                                        {:body "goog.require('dacom.client')"}
                                                        {:body "goog.require('dacom.repl')"}]}}}
             :db [:dev {:main dacom.db}]
             :prod {:main dacom.server
                    :target-path "dist/server/"
                    :resource {:resource-paths ["web-resources/pages"]
                               :target-path "dist/static"
                               :extra-values {:scripts [{:src "js/main.js"}]}}}
             :uberjar {:omit-source true
                       :aot :all}}
  :aliases {"bower" ["shell" "bower" "install"]
            "less-debug" ["shell" "lessc" "web-resources/stylesheets/style.less" "static/css/style.css"
                          "--include-path=bower_components/bootstrap/less/" "--source-map"]
            "less-prod" ["shell" "lessc" "web-resources/stylesheets/style.less" "dist/static/css/style.css"
                         "--include-path=bower_components/bootstrap/less/" "--compress"]
            "watch-less" ["fschange" "web-resources/stylesheets/*" "less-debug"]
            "install-db" ["with-profile" "db" "run"]
            "run-client" ["do" "bower," "cljsbuild" "once" "dev," "less-debug," "resource," "httpd" "8000"]
            "run-server" ["ring" "server-headless"]
            "dist" ["with-profile" "prod" "do" "bower," "uberjar," "cljsbuild" "once" "prod," "less-prod,"
                    "resource"]}
  :clean-targets [:target-path :compile-path "static" "dist"])
