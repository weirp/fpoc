(ns fpoc.restBackend
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [devtools.prefs :as prefs]
            [taoensso.timbre :as timbre]
            [clojure.set :as set]
            [fulcro.server :as server]))

(defn getBankInfo []
  (http/request {:url (str (:waivserver (server/load-config)) "/json/v1/api/bank")
                 :method :get             ; :post :put :head or other
                 :headers {"X-header" "value"
                           "Content-Type" "application/json"}
                 ;:query-params {"q" "foo, bar"} ;"Nested" query parameters are also supported
                 ;:form-params {"q" "foo, bar"} ; just like query-params, except sent in the body
                 ;:body (json/encode {"key" "value"}) ; use this for content-type json
                 :basic-auth ["waiv_api" "1testSecret4WA!V"]




                 :keepalive 3000          ; Keep the TCP connection for 3000ms
                 :timeout 1000      ; connection timeout and reading timeout 1000ms
                 :filter (http/max-body-filter (* 1024 100)) ; reject if body is more than 100k
                 :insecure? true ; Need to contact a server with an untrusted SSL cert?


                 :max-redirects 10 ; Max redirects to follow
                 ;; whether follow 301/302 redirects automatically, default to true
                 ;; :trace-redirects will contain the chain of the redirections followed.
                 :follow-redirects false
                 }))

(defn getBankInfo2 []
  (let [options {:headers {"X-header" "value"
                           "Content-Type" "application/json"}
                 :basic-auth ["waiv_api" "1testSecret4WA!V"]
                 :as :text}
        ;config (fulcro.server/load-config "config/dev.edn")
        base (:waivserver (server/load-config))
        {:keys [status headers body error] :as resp} @(http/get (str base "/json/v1/api/bank") options)]
    (if error
      (println "Failed, exception: " error)
      (println "HTTP GET success: " status))
    (if error
      error
      (json/read-str body))))

(defn waivLogin [user password]
  (let [options {:headers {"X-header" "value"
                           "Content-Type" "application/json"}
                 :basic-auth ["waiv_api" "1testSecret4WA!V"]
                 :as :text
                 :body (json/write-str {"username" user "password" password})}
        ;config (fulcro.server/load-config "config/dev.edn")
        base (:waivserver (server/load-config))  ;"http://localhost:9080/waiv-service"
        url (str base "/json/v1/api/user/authenticate")
        {:keys [status headers body error] :as resp} @(http/post url options)]
    (timbre/info "back from call " url)
    (timbre/info "body=" body)
    (timbre/info "data sb = " (json/write-str {"username" user "password" password}))
    (timbre/info "headers=" headers)

    (if (or error (not= status 200))
      (println "Failed, exception: " error ", status=" status)
      (println "HTTP GET success: " status))
    (if (or error (not= 200 status))
      error
      (do
        (timbre/info "error = " error ", status=" status)
        (timbre/info "body = " body)
        (timbre/info "cookie is " (headers :set-cookie))
        {:name user
         :email user
         :token (headers :set-cookie)})))
  )

(defn loadAccounts []
  (let [options {:headers {"X-header" "value"
                           "Content-Type" "application/json"}
                 :basic-auth ["waiv_api" "1testSecret4WA!V"]
                 :as :text}
        ;config (fulcro.server/load-config "config/dev.edn")
        base (:waivserver (server/load-config))  ;"http://localhost:9080/waiv-service"
        url (str base "/json/v1/api/account")
        {:keys [status headers body error] :as resp} @(http/get url options)]
    (timbre/info "back from call " url)
    (timbre/info "body=" body)
        (timbre/info "headers=" headers)

    (if (or error (not= status 200))
      (println "Failed, exception: " error ", status=" status)
      (println "HTTP GET success: " status))
    (if (or error (not= 200 status))
      error
      (do
        (timbre/info "error = " error ", status=" status)
        (timbre/info "body = " body)
        (timbre/info "cookie is " (headers :set-cookie))
        (json/read-str body))))
  )
