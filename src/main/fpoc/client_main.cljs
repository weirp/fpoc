(ns fpoc.client-main
  (:require [fpoc.client :refer [app]]
            [fulcro.client.core :as core]
            translations.es
            [fpoc.ui.root :as root]))

;; In dev mode, we mount from cljs/user.cljs
(reset! app (core/mount @app root/Root "app"))
