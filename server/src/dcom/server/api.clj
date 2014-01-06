(ns dcom.server.api
  (:require [ring.middleware.cors :as cors]
            [ring.middleware.format :refer [wrap-restful-format]]
            [compojure.response :as response]
            [datomic.api :as d :refer [db q]]
            [compojure.core :refer [GET defroutes]]))

;(def conn
;  (d/connect "datomic:dev://localhost:4334/test"))

;(defn get-message []
;  (mapv first (q '[:find ?n :where [?e :drawing/name ?n]] (db conn))))

(defroutes handler
           (GET "/" [] {:body {:foo :bar :baz :qux}}))

(def app
  (-> handler
      (cors/wrap-cors
        :access-control-allow-origin #"http://localhost:8000"
        :access-control-allow-methods ["GET" "POST" "DELETE"]
        :access-control-allow-headers ["Content-Type"]
        :access-control-allow-credentials "true")
      (wrap-restful-format :formats [:edn])))