(ns fpoc.api.operations
  (:require
    #?@(:clj  [clojure.main
               [cljs.core :refer [deftype specify! this-as js-arguments]]
               [clojure.reflect :as reflect]
               [cljs.util]
               [clojure.pprint :refer [pprint]]]
        :cljs [[goog.string :as gstring]
               [goog.object :as gobj]
               [goog.log :as glog]
               [om.next.cache :as c]
               [cljs.pprint :refer [pprint]]])

    [fulcro.client.mutations :as m :refer [defmutation]]
            [fpoc.ui.accounts :as accounts]
            [fulcro.client.data-fetch :as df]
            [fulcro.client.routing :as r]
            ))

(defmutation check-accounts-loaded
             [{:keys [comp]}]


             (action [{:keys [state ]}]
                     (pprint "check loaded")
                     (pprint (str "comp = " comp)  )
                     (pprint state)
                     (pprint @state)
                     (let [need-to-load? (= nil (get-in @state [:accountData] nil))]
                    (pprint (str "need to load = " need-to-load?))
                       (when need-to-load?
                         ;(let [acc (df/load comp [:accountData] accounts/Account {:remote true})]
                         ;  ;(swap! (env :state) :accountData acc)
                         ;  )
                         )
                       (r/route-to-impl! comp {:handler :accounts} )
                       ))
             )


(defmutation check-accounts-loadedx
             [args]
             (action [env]
                     {}))
