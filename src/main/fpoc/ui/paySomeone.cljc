(ns fpoc.ui.paySomeone
  (:require [om.next :as om :refer [defui]]
            [fulcro.client.core :as u]
            [fulcro.i18n :refer [tr]]
            [om.dom :as dom]
            [fulcro.client.mutations :as m]
            ;[fpoc.ui.accounts :as accounts]
    #?@(:clj  [
            [clojure.pprint :refer [pprint]]]
        :cljs [[cljs.pprint :refer [pprint]]])
            ))


(defui PaySomeoneLocal
  Object
  (render [this]
    (dom/div nil (tr "Local"))))

(def ui-paysomeone-local (om/factory PaySomeoneLocal))

(defui PaySomeoneInternational
  Object
  (render [this]
           (dom/div nil (tr "International"))))

(def ui-paysomeone-international (om/factory PaySomeoneInternational))

(defui ^:once PaySomeonePage
  static u/InitialAppState
  (initial-state [this params]
    {:id :paySomeone
     ;:accountData1 (u/get-initial-state accounts/Account params)
     :ui/internationalPayment false
     })
  static om/IQuery
  (query [this] [:id
                 [:current-user '_]
                 [:accountData1 '_]
                 [:ui/internationalPayment '_] ])
  static om/Ident
  (ident [this props]
    (do
      (pprint (str "PaySomeonePg ident " this props) )
      [:paySomeone :page]) )
  Object
  (render [this]
    (let [{:keys [current-user accountData1 ui/internationalPayment]} (om/props this)]
      (dom/div #js {}
               (tr "Pay Someone page")
               (if internationalPayment
                 (ui-paysomeone-international accountData1)
                 (ui-paysomeone-local accountData1))))))
