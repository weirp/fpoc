(ns fpoc.api.operations
  (:require
    #?@(:clj  [[clojure.pprint :refer [pprint]]]
        :cljs [[cljs.pprint :refer [pprint]]])

    [fulcro.client.mutations :as m :refer [defmutation]]
    [fpoc.ui.accounts :as accounts]
    [fulcro.client.data-fetch :as df]
    [fulcro.client.routing :as r]
    ;[fpoc.ui.html5-routing :as r5]
    ))

(defmutation check-accounts-loaded
             [args]

             (action [actionArgs]
                     ;; keys of actionArgs=(:query-root :path :pathopt :reconciler :ast :state :component :parser :logger :shared :target)
                     (let [comp (args :comp)
                           state (actionArgs :state)]
                       (pprint "check loaded")
                       (pprint (str "args = " args))
                       (pprint (str "comp = " comp)  )
                       (pprint (str "actionArgs (keys)=" (keys actionArgs)))
                       (pprint (str "actionArgs =" actionArgs))
                       (pprint (str "state=" state))
                       (pprint (str "@state=" @state))
                       (pprint (str "@state keys = " (keys @state)))
                       (let [need-to-load? (= nil (get-in @state [:accountData] nil))]
                         (pprint (str "need to load = " need-to-load?))
                         (when need-to-load?
                           ;(let [acc (df/load comp [:accountData] accounts/Account {:remote true})]
                           ;  ;(swap! (env :state) :accountData acc)
                           ;  )
                           )
                         ;(r/route-to-impl! comp {:handler :accounts} )
                         ;(r5/nav-to! comp :accounts)
                         )
                       )
                 )
             )

(defmethod m/mutate 'app/ensure-report-loaded [{:keys [state] :as env} k {:keys [report-id]}]
  (let [when-loaded (get-in @state [:accountData] 0)
        is-missing? (= 0 when-loaded)]
    ; if missing, put placeholder
    ; if too old, add remote load to Fulcro queue (see data-fetch for remote-load and load-action)
    {:action (fn []
               (pprint "in ensure-report-loaded")
               ;(when is-missing? (swap! state add-report-placeholder report-id))
               ;(when is-missing? (df/load-action env [:accountData] {}))
               )
     ;:remote (when is-missing? (df/remote-load env))

     }))

