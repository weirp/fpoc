(ns fpoc.ui.accounts
  (:require [om.next :as om :refer [defui]]
            [fulcro.client.core :as u]
            [fulcro.i18n :refer [tr]]
            [om.dom :as dom]
            [fulcro.client.mutations :as m]
            [fulcro.client.data-fetch :as df]
            [taoensso.timbre :as timbre]
            [fpoc.ui.user :as user]
            [fpoc.ui.paySomeone :as paySomeone]
    [fpoc.api.ops2 :as ops2]

    #?@(:clj  [
            [clojure.pprint :refer [pprint]]]
        :cljs [[cljs.pprint :refer [pprint]]])
            [fulcro.client.core :as fc]
            [fpoc.ui.html5-routing :as r]))

(defui Account
  static fc/InitialAppState
  (initial-state
    [comp-class {:keys [number balance name] :or {number "nah" balance "nah" name "nah"} :as props}]
    {:account/number number
     :account/name name
     :account/balance balance})
  static om/Ident
  (ident [this props]
    (do
      (pprint (str "Account ident,props=" props))
      [:accounts/by-number :testssss]) )
  static om/IQuery
  (query [this] [:account/number :account/name :account/balance ])
  Object
  (render [this]
    (pprint "Account render")
    (let [{:keys [account/number account/name account/balance] :or {account/number "undef" account/name "undef" account/balance "undef"} :as data} (om/props this)]
      (dom/div #js {:className "container-fluid col-12"}
               (pprint (str "rendering Account, data is " data))
               (dom/br nil)
               (dom/label #js {:className "col-3"} (tr "Account Number"))
               (dom/label #js {:className "col-2" } number)
               (dom/button #js {:className "btn btn-primary"
                                :onClick  (fn [evt]
                                            (om/transact! this `[(ops2/set-thing {:val false})])
                                            (r/nav-to! this :paySomeone {:ui/internationalPayment false}))} (tr "Account to Account Transfer"))
               (dom/br nil)
               (dom/label #js {:className "col-3"} (tr "Name"))
               (dom/label #js {:className "col-3" } name)
               (dom/br nil)
               (dom/label #js {:className "col-3"} (tr "Balance"))
               (dom/label #js {:className "col-2" } balance)
               (dom/button #js {:className "btn btn-primary"
                                :onClick  (fn [evt]
                                            (om/transact! this `[(ops2/set-thing {:val true})])
                                            (r/nav-to! this :paySomeone {:ui/internationalPayment true}))} (tr "Send Money Abroad"))
               (dom/br nil)))))

(def ui-account (om/factory Account {:keyfn :account/number}))


(defui ^:once AccountsPage
  static u/InitialAppState
  (initial-state [this params]
    (do
      (pprint (str "Account:initial-state" this params))

      ;(df/load this :accounts {})
      ;(df/load-action )
      ;(df/load-action)
      {:id :accounts
       :accountData1 (fc/get-initial-state Account params)}  )
    )
  static om/IQuery
  (query [this]
    (do
      (pprint "AccountsPage: query")
      [:id
       [:current-user '_]
       [:accountData1 '_]
       ]) )
  static om/Ident
  (ident [this props]
    (do
      (pprint (str "AccountsPage:ident " this props))
      [:accounts :page]) )
  Object
  (render [this]
        (let [{:keys [current-user accountData1] :or {current-user "undefined" accountData1 "undefined"}} (om/props this)]
          (dom/div #js {}
                   (pprint (str "rendering AccountsPage: current user token=" (current-user :token)
                                " accountData=" accountData1))
                   ;(ui-account (first accountData))
                   (ui-account accountData1)
                   ))))

(def ui-accounts-page (om/factory AccountsPage ))