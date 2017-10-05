(ns fpoc.ui.accounts
  (:require [om.next :as om :refer [defui]]
            [fulcro.client.core :as u]
            [fulcro.i18n :refer [tr]]
            [om.dom :as dom]
            [fulcro.client.mutations :as m]
            [fulcro.client.data-fetch :as df]
            [taoensso.timbre :as timbre]
            [fpoc.ui.user :as user]

    #?@(:clj  [
            [clojure.pprint :refer [pprint]]]
        :cljs [[cljs.pprint :refer [pprint]]])
            [fulcro.client.core :as fc]))

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
               (dom/label #js {:className "col-3"} "Account Number")
               (dom/label #js {:className "col-3" } number)
               (dom/br nil)
               (dom/label #js {:className "col-3"} "Name")
               (dom/label #js {:className "col-3" } name)
               (dom/br nil)
               (dom/label #js {:className "col-3"} "Balance")
               (dom/label #js {:className "col-3" } balance)))))

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