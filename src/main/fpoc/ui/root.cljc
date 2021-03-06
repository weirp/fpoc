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
    [fpoc.ui.paySomeone :as paySomeone]
    [fpoc.ui.new-user :as nu]
    [fpoc.api.mutations :as api]
    [om.next :as om :refer [defui]]
    [fulcro.server-render :as ssr]
    [fulcro.i18n :refer [tr]]
    [fulcro.ui.bootstrap3 :as b]
    [fulcro.client.data-fetch :as df]

    [fpoc.api.operations :as ops]

    [fpoc.ui.paySomeone :as paySomeone]

    [om.next :as om]

    #?@(:clj  [[clojure.pprint :refer [pprint]]]
        :cljs [[cljs.pprint :refer [pprint]]])))

(defrouter Pages :page-router
           (ident [this props] [(:id props) :page])
           :login l/LoginPage
           :new-user nu/NewUser
           :preferences prefs/PreferencesPage
           :accounts accounts/AccountsPage
           :paySomeone paySomeone/PaySomeonePage
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
        {:keys [ui/locale] :or {ui/locale :en}} (om/props this)
        ]
    (pprint (str "in navbar locale=" locale))

    (dom/div #js {:className "container col-12"}
             (dom/div #js {:className "navbar navbar-expand navbar-light bg-brand"}
                      (dom/div #js {:className "container-fluid"}
                               (dom/div #js {:className "navbar-header col-9"}
                                        (dom/span #js {:className "navbar-brand col-6"}
                                                  (dom/span nil
                                                            (dom/img #js {:src "/images/logo.png" :alt "waiv logo"}))))

                               (dom/div #js {}
                                        (dom/select #js {:className "btn btn-secondary"
                                                         :id "languageSelector"
                                                         :onChange (fn [evt]
                                                                     (let [val (keyword (-> evt .-target .-value))]
                                                                       (pprint (str "in event hadler of locale select" val) )
                                                                       (om/transact! this `[(m/change-locale {:lang ~val})])))
                                                         :value (name locale)}

                                                    (dom/option #js {:value "en" } "English")
                                                    (dom/option #js {:value "es" } "Spanish")
                                                    (dom/option #js {:value "mi_NZ"} "Te Reo"))
                                        )
                               (dom/div #js {:className "col-2"}
                                        (if logged-in?
                                          (ui-login-stats loading-data current-user logout)
                                          (ui-login-button loading-data login)))))

             (dom/div #js {:className "navbar navbar-expand navbar-light bg-light col-12"}
                      (dom/div #js {:className "collapse navbar-collapse col-12"}
                               (when logged-in?
                                 (dom/div #js {:className "nav navbar-nav col-12"}
                                          ;; More nav links here
                                          (dom/a #js {:className "nav-item nav-link active col-2" :onClick #(r/nav-to! this :profile)} (tr "Profile"))
                                          (dom/a #js {:className "nav-item nav-link active col-2" :onClick #(r/nav-to! this :accounts)} (tr "Account"))
                                          (dom/a #js {:className "nav-item nav-link active col-2" :onClick #(r/nav-to! this :paySomeone)} (tr "Pay Someone"))
                                          (dom/a #js {:className "nav-item nav-link active col-3" :onClick #(r/nav-to! this :accountLimits)} (tr "Account Limits"))

                                          ;(dom/a #js {:className "nav-item nav-link active" :onClick #(r/nav-to! this :main)} (tr "Main"))
                                          ;(dom/a #js {:className "nav-item nav-link active col-4" :onClick #(r/nav-to! this :preferences)} (tr "Preferences"))

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
  (query [this] [:ui/react-key :ui/ready? :logged-in? :ui/locale
                 {:current-user (om/get-query user/User)}
                 {:root/modals (om/get-query Modals)}
                 fulcro.client.routing/routing-tree-key     ; TODO: Check if this is needed...seemed to affect initial state from ssr
                 :ui/loading-data {:pages (om/get-query Pages)}])
  Object
  (render [this]

    (let [{:keys [ui/ready? ui/locale ui/loading-data ui/react-key pages welcome-modal current-user logged-in?] :or {react-key "ROOT" ui/locale :en}} (om/props this)]
      (dom/div #js {:key react-key}
               (pprint (str "in root render, locale=" locale)  )
        (ui-navbar this)
        (when ready?
          (ui-pages pages))))))
