;   Copyright (c) 2014 Kevin Bell. All rights reserved.
;   See the file license.txt for copying permission.

(ns dacom.client
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer [html] :include-macros true]
            [cljs.core.async :as async :refer [chan <! put!]]
            [ajax.core :as ajax]))

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
      (html [:div.message
             [:div.container
              [:h1 (om/get-state owner :om-message)]
              [:p (str "If you can read the message above, then you have successfully "
                       "launched your brand-new DACOM-based webapp.")]]]))))

(om/root {} widget (.getElementById js/document "app"))