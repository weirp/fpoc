(ns cljs.user
  (:require
    [fulcro.client.core :as fc]
    [om.next :as om]

    [fpoc.client :as core]
    [fpoc.ui.root :as root]

    [cljs.pprint :refer [pprint]]
    [fulcro.client.logging :as log]))

(enable-console-print!)

(log/set-level :all)

(defn mount []
  (reset! core/app (fc/mount @core/app root/Root "app")))

(mount)

(defn app-state [] @(:reconciler @core/app))

(defn log-app-state [& keywords]
  (pprint (let [app-state (app-state)]
            (if (= 0 (count keywords))
              app-state
              (select-keys app-state keywords)))))

(defn dump-query [comp]
  (let [component (om/class->any (:reconciler @core/app) comp)]
    (om/full-query component)))

(defn dump-query-kw [kw]
  (let [component (om/ref->any (:reconciler @core/app) kw)]
    (om/full-query component)))

(defn q
  "Run the query of the given UI class and return the result as a map of the query that was run and the data that was returned.
  NOTE: If the component is mounted in several places on the UI, you may not get the expected result. Be sure to check
  the QUERY part of the result to see the query used."
  [ui-class]
  (let [query (dump-query ui-class)
        state (app-state)]
    {:QUERY  query
     :RESULT (om/db->tree query state state)}))

(defn qkw
  "Find a component that uses the given keyword in its query, then run that query against the app database and show
  the result. NOTE: If more than one component matches, your results may vary. Be sure to examine the query that
  was used."
  [query-kw]
  (let [query (dump-query-kw query-kw)
        state (app-state)]
    {:QUERY  query
     :RESULT (om/db->tree query state state)}))
