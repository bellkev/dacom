;   Copyright (c) 2014 Kevin Bell. All rights reserved.
;   See the file license.txt for copying permission.

(ns dacom.server
  (:require [ring.middleware.cors :as cors]
            [ring.middleware.format :refer [wrap-restful-format]]
            [compojure.response :as response]
            [datomic.api :as d :refer [db q]]
            [compojure.core :refer [GET defroutes]]
            [dacom.config :refer [read-config]]))

(def conn
  (d/connect (:datomic-uri (read-config))))

(defn get-datomic-message []
  (ffirst (q '[:find ?m :where [?e :demo/message ?m]] (db conn))))

(defroutes handler
           (GET "/" [] {:body {:compojure-message (str (get-datomic-message) ", Compojure")}}))

(def app
  (-> handler
      (cors/wrap-cors
        :access-control-allow-origin #"http://localhost:8000"
        :access-control-allow-methods ["GET"]
        :access-control-allow-headers ["Content-Type"])
      (wrap-restful-format :formats [:edn])))