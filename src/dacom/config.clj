;   Copyright (c) 2014 Kevin Bell. All rights reserved.
;   See the file license.txt for copying permission.

(ns dacom.config
  (:require [clojure.java.io :as io])
  (:import (java.io FileNotFoundException PushbackReader)))

(def config-paths
  ; Highest priority first
  ["config.edn"
   "config/dev-config.edn"])

(defn try-read [path]
  (try (with-open [r (io/reader path)]
         (read (PushbackReader. r)))
       (catch FileNotFoundException e nil)))

(defn read-config []
  (first (keep try-read config-paths)))