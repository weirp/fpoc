(ns generate-i18n-cljc
  (:require [fulcro.gettext :as g]))

(g/deploy-translations {:ns "fpoc.locales" :src "src/main" :po "i18n"})
(System/exit 0)
