(ns fpoc.api.ops2
  (:require [fulcro.client.mutations :refer [defmutation]]
            [taoensso.timbre :as timbre]))

(defmutation set-thing
             [{:keys [val]}]

             (action [{:keys [state]}]
                     (swap! state assoc :ui/internationalPayment val)))
