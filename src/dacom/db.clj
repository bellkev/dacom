;   Copyright (c) 2014 Kevin Bell. All rights reserved.
;   See the file license.txt for copying permission.

(ns dacom.db
  "https://github.com/Datomic/day-of-datomic/blob/master/src/datomic/samples/io.clj
   and
   https://github.com/Datomic/day-of-datomic/blob/master/src/datomic/samples/schema.clj"
  (:require [datomic.api :as d :refer [db q]]
            [clojure.java.io :as io]
            [dacom.config :refer [read-config]])
  (:import datomic.Util))

;===============================================================================
; io utils
;===============================================================================

(defn read-all
  "Read all forms in f, where f is any resource that can
   be opened by io/reader"
  [f]
  (Util/readAll (io/reader f)))

(defn transact-all
  "Load and run all transactions from f, where f is any
   resource that can be opened by io/reader."
  [conn f]
  (doseq [txd (read-all f)]
    (d/transact conn txd))
  :done)

;===============================================================================
; schema utils
;===============================================================================

(defn cardinality
  "Returns the cardinality (:db.cardinality/one or
   :db.cardinality/many) of the attribute"
  [db attr]
  (->>
    (d/q '[:find ?v
           :in $ ?attr
           :where
           [?attr :db/cardinality ?card]
           [?card :db/ident ?v]]
         db attr)
    ffirst))

(defn has-attribute?
  "Does database have an attribute named attr-name?"
  [db attr-name]
  (-> (d/entity db attr-name)
      :db.install/_attribute
      boolean))

(defn has-schema?
  "Does database have a schema named schema-name installed?
   Uses schema-attr (an attribute of transactions!) to track
   which schema names are installed."
  [db schema-attr schema-name]
  (and (has-attribute? db schema-attr)
       (-> (d/q '[:find ?e
                  :in $ ?sa ?sn
                  :where [?e ?sa ?sn]]
                db schema-attr schema-name)
           seq boolean)))

(defn- ensure-schema-attribute
  "Ensure that schema-attr, a keyword-valued attribute used
   as a value on transactions to track named schemas, is
   installed in database."
  [conn schema-attr]
  (when-not (has-attribute? (d/db conn) schema-attr)
    (d/transact conn [{:db/id #db/id[:db.part/db]
                       :db/ident schema-attr
                       :db/valueType :db.type/keyword
                       :db/cardinality :db.cardinality/one
                       :db/doc "Name of schema installed by this transaction"
                       :db/index true
                       :db.install/_attribute :db.part/db}])))

(defn ensure-schemas
  "Ensure that schemas are installed.

      schema-attr   a keyword valued attribute of a transaction,
                    naming the schema
      schema-map    a map from schema names to schema installation
                    maps. A schema installation map contains two
                    keys: :txes is the data to install, and :requires
                    is a list of other schema names that must also
                    be installed
      schema-names  the names of schemas to install"
  [conn schema-attr schema-map & schema-names]
  (ensure-schema-attribute conn schema-attr)
  (doseq [schema-name schema-names]
    (if (has-schema? (d/db conn) schema-attr schema-name)
      (println "Schema" schema-name "already installed")
      (let [{:keys [requires txes]} (get schema-map schema-name)]
        (println "Installing schema" schema-name "...")
        (apply ensure-schemas conn schema-attr schema-map requires)
        (if txes
          (doseq [tx txes]
            ;; hrm, could mark the last tx specially
            (d/transact conn (cons {:db/id #db/id [:db.part/tx]
                                    schema-attr schema-name}
                                   tx)))
          (throw (ex-info (str "No data provided for schema" schema-name)
                          {:schema/missing schema-name})))))))

;===============================================================================
; install schema and sample data
;===============================================================================

(def db-uri (:datomic-uri (read-config)))

(d/create-database db-uri)

(def conn
  (d/connect db-uri))

(def schema-map (first (read-all "db-resources/schema.edn")))

(defn install-schema []
  (ensure-schemas conn :dacom/all-tx-tag schema-map :dacom/all))

(defn install-message []
  (let [result (q '[:find ?e :where [?e :demo/message]] (db conn))]
    (if (ffirst result)
      (println "Demo message already installed")
      (do
        (println "Installing demo message...")
        (d/transact conn [{:db/id (d/tempid :db.part/user)
                           :demo/message "Hello, from Datomic"}])))))

(defn -main [& args]
  (install-schema)
  (install-message)
  (System/exit 0))