(ns dcom.client.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [ajax.core :as ajax]))

(js/alert "Hello from cljs!")

(defn widget [data]
  (om/component
    (dom/div nil "Hello world!")))

(om/root {} widget (.getElementById js/document "app"))

(ajax/GET "http://localhost:3000/" {:handler (fn [response] (js/alert (str response)))})