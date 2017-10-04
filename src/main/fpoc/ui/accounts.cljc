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
    [comp-class {:keys [number balance name] :as props}]
    {:account/number number
     :account/name name
     :account/balance balance})
  static om/Ident
  (ident [this props]
    (do
      (pprint (str "here's the props given for Account ident" props))
      [:accounts/by-number (:account/number props)]) )
  static om/IQuery
  (query [this] [:account/number :account/name :account/balance ])
  Object
  (render [this]
    (let [{:keys [account/number account/name account/balance] :as data} (om/props this)]
      (dom/div #js {:className "container-fluid col-12"}
               (pprint (str "in acc, data is " data))
               (dom/label #js {:className "col-3"} "Account Number")
               (dom/label #js {:className "col-3" } number)
               (dom/br nil)
               (dom/label #js {:className "col-3"} "Name")
               (dom/label #js {:className "col-3" } name)
               (dom/br nil)
               (dom/label #js {:className "col-3"} "Balance")
               (dom/label #js {:className "col-3" } balance)))))

(def ui-account (om/factory Account
                            {:keyfn :account/number}
                            ))


(defui AccountList
  static fc/InitialAppState
  (initial-state [comp-class {:keys [] :as props}]
    (do
      (pprint (str "initial-state" comp-class props))
      {:accountList/accounts {}}))

  )

(def ui-account-list (om/factory AccountList ))

(defui ^:once AccountsPage
  static u/InitialAppState
  (initial-state [this params]
    (do
      (pprint (str "initial-state" this params))

      ;(df/load this :accounts {})
      ;(df/load-action )
      ;(df/load-action)
      {:id :accounts
       :accountData []
       :myAccountData []}  )
    )
  static om/IQuery
  (query [this] [:id
                 [:current-user '_]
                 [:accountData '_]])
  static om/Ident
  (ident [this props] [:accounts :page])
  Object
  (render [this]
        (let [
          {:keys [current-user accountData]} (om/props this)

              ;;; don't load in render, make a button and put this in fn ... like login
              ;accounts (df/load this :accounts Account {:params {:token (current-user :token)} })
              ]

          (dom/div #js {}
                   (pprint (str "current user token=" (current-user :token)))
                   (pprint (str "accountData size" (count accountData)))
                   (pprint accountData)
                   (pprint (first accountData))
                   (map #(pprint (str "an account " %)) accountData)
                   ;(map ui-account accountData)

                   (ui-account (first accountData))
                   )


      )))

(def ui-accounts-page (om/factory AccountsPage ))