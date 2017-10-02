(ns fpoc.accounts
  (:require [devcards.core :as rc :refer-macros [defcard]]
            [om.next :as om :refer-macros [defui]]
            [fpoc.ui.accounts :as accounts]
            [om.dom :as dom]))

(defcard AccountCard
         "# An Account Component"
         (accounts/ui-account {:account/number 12343 :account/name "test account" :account/balance 100.21 }))