(ns fpoc.ui.user
  (:require
    [om.dom :as dom]
    [om.next :as om :refer [defui]]
    [fulcro.client.core :as uc]))

(defui ^:once User
  static om/IQuery
  (query [this] [:uid :name :email :token])
  static om/Ident
  (ident [this props] [:user/by-email (:email props)])
  Object
  (render [this]
    (dom/span #js {:className "text-white"} (get (om/props this) :name))))

(def ui-user (om/factory User {:keyfn :email}))
