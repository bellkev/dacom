(ns dacom.repl
  (:require [ajax.core :as ajax]
            [clojure.browser.repl]))

(ajax/GET "http://localhost:8080"
          {:handler (fn [response]
                      (js/eval response)
                      (.log js/console "Connected to Austin nREPL"))
           :error-handler (fn [e]
                            (.log js/console (str "Unable to connect to nREPL")))
           :format (ajax/raw-format)})
