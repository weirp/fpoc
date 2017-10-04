(ns fpoc.ui.accountLimits
  (:require [om.next :as om :refer [defui]]
            [fulcro.client.core :as u]
            [fulcro.i18n :refer [tr]]
            [om.dom :as dom]
            [fulcro.client.mutations :as m]))

(defui ^:once AccountLimitsPage
  static u/InitialAppState
  (initial-state [this params] {:id :accountLimits})
  static om/IQuery
  (query [this] [:id])
  static om/Ident
  (ident [this props] [:accountLimits :page])
  Object
  (render [this]
    (dom/div #js {} (tr "Account Limits page"))))
