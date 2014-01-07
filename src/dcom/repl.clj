(ns dcom.repl
  (:require [cemerick.austin.repls :refer (browser-connected-repl-js)]
            [compojure.route :refer (resources)]
            [compojure.core :refer (GET defroutes)]
            [ring.middleware.cors :as cors]
            ring.adapter.jetty))

(defroutes handler
           (GET "/" []
                (browser-connected-repl-js)))

(def app
  (-> handler
      (cors/wrap-cors
        :access-control-allow-origin #"http://localhost:8000"
        :access-control-allow-methods ["GET"]
        :access-control-allow-headers ["Content-Type"])))

(defn run
  []
  (defonce ^:private server
           (ring.adapter.jetty/run-jetty #'app {:port 8080 :join? false}))
  server)

; Start the REPL
(defn browser-repl []
  (def repl-env (reset! cemerick.austin.repls/browser-repl-env
                        (cemerick.austin/repl-env)))
  (run)
  (cemerick.austin.repls/cljs-repl repl-env))

(defn br [] (browser-repl))