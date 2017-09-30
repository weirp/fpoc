(ns fpoc.client-test-main
  (:require fpoc.tests-to-run
            [fulcro-spec.selectors :as sel]
            [fulcro-spec.suite :as suite]))

(enable-console-print!)

(suite/def-test-suite client-tests {:ns-regex #"fpoc\..*-spec"}
  {:default   #{::sel/none :focused}
   :available #{:focused}})

