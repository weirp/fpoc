(ns fpoc.ui.root
  (:require
    [fulcro.client.mutations :as mut]
    [fulcro.client.core :as fc]
    [fulcro.client.util :as util]
    [fulcro.client.routing :refer [defrouter set-route]]
    [fulcro.client.mutations :as m]
    [fulcro.client.logging :as log]
    [om.dom :as dom]
    [fpoc.ui.html5-routing :as r]
    [fpoc.ui.login :as l]
    [fpoc.ui.user :as user]
    [fpoc.ui.main :as main]
    [fpoc.ui.preferences :as prefs]
    [fpoc.ui.profile :as profile]
    [fpoc.ui.accountLimits :as accountLimits]
    [fpoc.ui.accounts :as accounts]
    [fpoc.ui.new-user :as nu]
    [fpoc.api.mutations :as api]
    [om.next :as om :refer [defui]]
    [fulcro.server-render :as ssr]
    [fulcro.i18n :refer [tr]]
    [fulcro.ui.bootstrap3 :as b]
    [fulcro.client.data-fetch :as df]

    [fpoc.api.operations :as ops]


    #?@(:clj  [[taoensso.timbre :as logx]]
        :cljs [[goog.log :as logx]])

    #?@(:clj  [[clojure.pprint :refer [pprint]]]
        :cljs [[cljs.pprint :refer [pprint]]])
    ))

(defrouter Pages :page-router
  (ident [this props] [(:id props) :page])
  :login l/LoginPage
  :new-user nu/NewUser
  :preferences prefs/PreferencesPage
  :accounts accounts/AccountsPage
  :profile profile/ProfilePage
  :accountLimits accountLimits/AccountLimitsPage
  :main main/MainPage)

(def ui-pages (om/factory Pages))

(defn ui-login-stats [loading? user logout-fn]
  (dom/p #js {:className "navbar-text navbar-right"}
    ;(when loading? (b/badge {} "..."))
    (user/ui-user user)
    ;(dom/br nil)
    (dom/a #js {:onClick logout-fn :className "btn btn-primary logout"} (tr "Log out"))))

(defn ui-login-button [loading? login-fn]
  (dom/p #js {:className "navbar-right"}
    (when loading?
      (dom/span #js {:className "navbar-text badge"} "..."))
    (b/button {:className "navbar-btn btn btn-primary login" :onClick login-fn} (tr "Sign in"))))

(defn ui-navbar [this]
  (let [login      #(r/nav-to! this :login)
        logout     #(om/transact! this `[(api/logout {}) (r/set-route! {:handler :login}) :current-user])
        {:keys [ui/loading-data current-user]} (om/props this)
        logged-in? (contains? current-user :name)
        loadAccounts (fn []
                           ;(df/load this :waiv-user user/User { :post-mutation `api/login-complete-waiv
                           ;                                    :params {:username username :password password}
                           ;                                    :refresh       [:logged-in? :current-user]})
                           ;(df/load this :accountData1 accounts/Account {:params        {:token (current-user :token) :target [:accountData1]}})
                           )]

    (dom/div #js {:className "container col-12"}
             (dom/div #js {:className "navbar navbar-expand navbar-light bg-brand"}
                      (dom/div #js {:className "container-fluid"}
                               (dom/div #js {:className "navbar-header col-2"}
                                        (dom/span #js {:className "navbar-brand col-6"}
                                                  (dom/span nil (dom/img #js {
                                                                              ;:className "img-circle"
                                                                              ;:width "80"
                                                                              :src "/images/logo.png"
                                                                              :alt "waiv logo"}))
                                                  (dom/a #js {:onClick #(om/transact! this `[(m/change-locale {:lang :en})]) :href "#"} "en") " | "
                                                  (dom/a #js {:onClick #(om/transact! this `[(m/change-locale {:lang :es})]) :href "#"} "es") " | "
                                                  (dom/a #js {:onClick #(om/transact! this `[(m/change-locale {:lang :mi_NZ})]) :href "#"} "mi_NZ")))


                               (dom/div #js {:className "col-2"}

                                        (if logged-in?
                                          (ui-login-stats loading-data current-user logout)
                                          (ui-login-button loading-data login)))))

             (dom/div #js {:className "navbar navbar-expand navbar-light bg-light col-12"}
                      (dom/div #js {:className "collapse navbar-collapse col-10"}
                               (when logged-in?
                                 (dom/div #js {:className "nav navbar-nav col-11"}
                                          ;; More nav links here
                                          (dom/a #js {:className "nav-item nav-link active" :onClick #(r/nav-to! this :profile)} (tr "Profile"))
                                          (dom/a #js {:className "nav-item nav-link active"
                                                      :onClick
                                                                 #(r/nav-to! this :accounts)
                                          ;#(om/transact! this `[(loadAccounts)])
                                                      } (tr "Account"))
                                          ;#(om/transact! this `[ (pprint "at start of transact!")  (app/ensure-report-loaded {}) (r/nav-to! ~this :accounts) ])
                                          ;#(r/nav-to! this :accounts)
                                          ;#(om/transact! this `[(ops/check-accounts-loaded {:comp ~this}) (r/nav-to! ~this :accounts)] )
                                          ;#(om/transact! this `[ (r/nav-to! ~this :accounts)] )


                                          (dom/a #js {:className "nav-item nav-link active" :onClick #(r/nav-to! this :accountLimits)} (tr "Account Limits"))

                                          (dom/a #js {:className "nav-item nav-link active" :onClick #(r/nav-to! this :main)} (tr "Main"))
                                          (dom/a #js {:className "nav-item nav-link active" :onClick #(r/nav-to! this :preferences)} (tr "Preferences"))

                                          (dom/a #js {:className "nav-item nav-link active" :onClick #(om/transact! this `[m/change-locale {:lang :es}])} (tr "load accounts") )
                                          )))))))

;; Add other modals here.
(defui ^:once Modals
  static om/IQuery
  (query [this] [{:welcome-modal (om/get-query b/Modal)}])
  static fc/InitialAppState
  (initial-state [this params] {:welcome-modal (fc/get-initial-state b/Modal {:id :welcome :backdrop true})})
  Object
  (render [this]
    (let [{:keys [welcome-modal]} (om/props this)]
      (b/ui-modal welcome-modal
        (b/ui-modal-title nil
          (dom/b nil (tr "Welcome!")))
        (b/ui-modal-body nil
          (dom/p #js {:className b/text-info} (tr "Glad you could join us!")))
        (b/ui-modal-footer nil
          (b/button {:onClick #(om/transact! this `[(b/hide-modal {:id :welcome})])} (tr "Thanks!")))))))

(defui ^:once Root
  static fc/InitialAppState
  (initial-state [c p] (merge
                         {; Is there a user logged in?
                          :logged-in?   false
                          ; Is the UI ready for initial render? This avoids flicker while we figure out if the user is already logged in
                          :ui/ready?    false
                          ; What are the details of the logged in user
                          :current-user nil
                          :root/modals  (fc/get-initial-state Modals {})
                          :pages        (fc/get-initial-state Pages nil)}
                         r/app-routing-tree))
  static om/IQuery
  (query [this] [:ui/react-key :ui/ready? :logged-in?
                 {:current-user (om/get-query user/User)}
                 {:root/modals (om/get-query Modals)}
                 fulcro.client.routing/routing-tree-key     ; TODO: Check if this is needed...seemed to affect initial state from ssr
                 :ui/loading-data {:pages (om/get-query Pages)}])
  Object
  (render [this]
    (let [{:keys [ui/ready? ui/loading-data ui/react-key pages welcome-modal current-user logged-in?] :or {react-key "ROOT"}} (om/props this)]
      (dom/div #js {:key react-key}
        (ui-navbar this)
        (when ready?
          (ui-pages pages))))))
