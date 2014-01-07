(ns dcom.client
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer [html] :include-macros true]
            [cljs.core.async :as async :refer [chan <! put!]]
            [ajax.core :as ajax]
            [clojure.browser.repl]))

;===============================================================================
; for dev
;===============================================================================

(ajax/GET "http://localhost:8080"
          {:handler (fn [response]
                      (js/eval response)
                      (.log js/console "Connected to Austin nREPL"))
           :error-handler (fn [e]
                            (.log js/console (str "Unable to connect to nREPL")))
           :format (ajax/raw-format)})

;===============================================================================
; Om app
;===============================================================================

(def server-chan (chan))

(ajax/GET "http://localhost:3000/"
          {:handler (fn [response]
                      (put! server-chan response))})

(defn widget [data owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (go
        (om/set-state!
          owner
          :om-message
          (str (:compojure-message (<! server-chan)) ", and Om!"))))
    om/IRender
    (render [_]
      (html [:p (om/get-state owner :om-message)]))))

(om/root {} widget (.getElementById js/document "app"))