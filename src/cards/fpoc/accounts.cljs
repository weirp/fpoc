(ns fpoc.accounts
  (:require [devcards.core :as rc :refer-macros [defcard]]
            [om.next :as om :refer-macros [defui]]
            [fpoc.ui.accounts :as accounts]
            [om.dom :as dom]))

(defcard AccountCard
         "# An Account Component"
         (accounts/ui-account {:account/number "12343" :account/name "test account" :account/balance 100.21 }))

(defcard AccountPageCard
         "# An AccountPage Component"
         (accounts/ui-accounts-page {:accountData1 [{:account/number "12343" :account/name "test account" :account/balance 100.21 }]
                                     :current-user {:token "xhsjxdhwxwbuw"}}))

(defcard AccountPageMultiAccountCard
         "# An Account Page with more than one account"
         (accounts/ui-accounts-page {:accountData1 [{:account/number "12343" :account/name "test account" :account/balance 100.21 }
                                                   {:account/number "918282" :account/name "second account" :account/balance 765.33 }]
                                     :current-user {:token "^&*@HJ@!*&(bnb"}}))