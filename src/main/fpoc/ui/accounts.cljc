(ns fpoc.ui.accounts
  (:require [om.next :as om :refer [defui]]
            [fulcro.client.core :as u]
            [fulcro.i18n :refer [tr]]
            [om.dom :as dom]
            [fulcro.client.mutations :as m]
            [fulcro.client.data-fetch :as df]
            [taoensso.timbre :as timbre]
            [fpoc.ui.user :as user]
            [fulcro.ui.bootstrap3 :as b]
    #?@(:clj  [[clojure.pprint :refer [pprint]]]
        :cljs [[cljs.pprint :refer [pprint]]])))

(defui Account
  static om/Ident
  (ident [this props] [:accounts/by-number (:account/number props)])
  static om/IQuery
  (query [this] [:account/number :account/name :account/balance ])
  Object
  (render [this]
    (let [{:keys [account/number account/name account/balance]} (om/props this)]
      (b/container-fluid {}
               (b/row {}
                        (b/col {:xs 6} "Account Number")
                        (b/col {:xs 3} number)
                         )
               (b/row {}
                        (dom/p nil (str "Name: " name) )
                        (dom/p nil (str "Balance: " balance) ))
               ))))

(def ui-account (om/factory Account {:keyfn :account/number}))



(defui ^:once AccountsPage
  static u/InitialAppState
  (initial-state [this params]
    (do
      (pprint "in initial-state for AccountsPage")
      (pprint this)
      (pprint params)
      ;(df/load this :accounts {})
      ;(df/load-action )
      {:id :accounts :accountData (om/get-query Account)}  )
    )
  static om/IQuery
  (query [this] [:id
                 [:current-user '_]
                 [:accountData '_]

                  ])
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
                   (tr "Accounts page")
                   (dom/p nil "C got " (if current-user "yiisss" "Nothing!"))
                   (map ui-account accountData)
                   (dom/p nil (current-user :name))
                   )


      )))