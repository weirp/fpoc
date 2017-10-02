(ns fpoc.ui.accounts
  (:require [om.next :as om :refer [defui]]
            [fulcro.client.core :as u]
            [fulcro.i18n :refer [tr]]
            [om.dom :as dom]
            [fulcro.client.mutations :as m]
            [fulcro.client.data-fetch :as df]
            [taoensso.timbre :as timbre]
            [fpoc.ui.user :as user]
            [fulcro.ui.bootstrap3 :as b]))

(defui Account
  static om/Ident
  (ident [this props] [:accounts/by-number (:account/number props)])
  static om/IQuery
  (query [this] [:account/number :account/name :account/balance ])
  Object
  (render [this]
    (let [{:keys [account/number account/name account/balance]} (om/props this)]
      (dom/div nil
               (b/label nil "Account Number")
               (dom/h4 nil number)
               (dom/p nil name)
               (dom/p nil balance)))))

(def ui-account (om/factory Account {:keyfn :account/number}))


(defui ^:once AccountsPage
  static u/InitialAppState
  (initial-state [this params] {:id :accounts})
  static om/IQuery
  (query [this] [:id  [:current-user '_]  ])
  static om/Ident
  (ident [this props] [:accounts :page])
  Object
  (render [this]
        (let [
          {:keys [current-user]} (om/props this)

          accounts (df/load this :accounts Account {:current-user current-user :xx "xxxx"})
          ]
      (dom/div #js {}
               (tr "Accounts page")
               (map ui-account accounts)
               ))))