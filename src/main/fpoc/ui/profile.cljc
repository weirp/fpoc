(ns fpoc.ui.profile
  (:require [om.next :as om :refer [defui]]
            [fulcro.client.core :as u]
            [fulcro.i18n :refer [tr]]
            [om.dom :as dom]
            [fulcro.client.mutations :as m]))

(defui ^:once ProfilePage
  static u/InitialAppState
  (initial-state [this params] {:id :profile})
  static om/IQuery
  (query [this] [:id :pjw])
  static om/Ident
  (ident [this props] [:profile :page])
  Object
  (render [this]
    (dom/div #js {} (tr "Profile page"))))
