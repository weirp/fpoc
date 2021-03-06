(ns fpoc.api.read
  (:require
    [fpoc.api.mutations :as m]
    [fulcro.server :refer [defquery-entity defquery-root]]
    [taoensso.timbre :as timbre]
    [fpoc.api.user-db :as users]
    [fulcro.server :as server]
    [fpoc.restBackend :as rbe]))

;; SERVER READ IMPLEMENTATION. We're using `fulcro-parser`. You can either use defmulti on the multimethods
;; (see fulcro.server defmulti declarations) or the defquery-* helper macros.
(defn attempt-login
  "The `request` will be available in the `env` of action. This is a normal Ring request (session is under :session)."
  [user-db request username password]
  (let [user        (users/get-user user-db username password)
        real-uid    (:uid user)
        secure-user (select-keys user [:name :uid :email])]
    ;(Thread/sleep 300)       ; pretend it takes a while to auth a user
    (if user
      (do
        (timbre/info "Logged in user " user)
        (timbre/info "request is : " request)
        (server/augment-response secure-user
          (fn [resp] (assoc-in resp [:session :uid] real-uid))))
      (do
        (timbre/error "Invalid login using email: " username)
        {:login/error "Invalid credentials"}))))

(defn attempt-waiv-login
  [request username password]

  )

(defquery-root :current-userx
  "Answer the :current-user query. This is also how you log in (passing optional username and password)"
  (value [{:keys [request user-db] :as env} {:keys [username password] :as params}]
         (timbre/info "in current user read ")
         (timbre/info "env keys=" (keys env))
         (timbre/info "env=" env)
         (timbre/info "params keys=" (keys params) )
         (timbre/info "params=" params)
    (if (and username password)
      (attempt-login user-db request username password)
      (let [resp (-> (users/get-user user-db (-> request :session :uid))
                   (select-keys [:uid :name :email]))]
        (timbre/info "Current user: " resp)
        resp))))

(defquery-root :bankinfo
   "Load bank info from rest server"
     (value [env params]
            (let [bi (rbe/getBankInfo2)]
              (timbre/info "reading bankInfo from rest server")
              bi)))

(defquery-root :current-user
     (value [{:keys [request] :as env} {:keys [username password :as params]}]
            (timbre/info "in waiv-user read")
            (let [loggy-in (rbe/waivLogin username password)]
              (timbre/info "remote login result=" loggy-in)
              {:name username :email username :uid "121212121" :token (loggy-in :token)})))

(defquery-root :accountData
     (value [{:keys [request ast] :as env} {:keys [token] :as params} ]
            (timbre/info "")
            (timbre/info "")
            (timbre/info "in read :accounts")
            (timbre/info "env=" env)
            (timbre/info "ast=" ast)
            (timbre/info "params" params)

            (let [accounts (rbe/loadAccounts token)]
              ;(timbre/info "returning " result)
              accounts)))

(defquery-root :accountData1
               (value [{:keys [request ast] :as env} {:keys [token] :as params} ]
                      (timbre/info "")
                      (timbre/info "")
                      (timbre/info "in read :accounts")
                      (timbre/info "env=" env)
                      (timbre/info "ast=" ast)
                      (timbre/info "params" params)

                      (let [accounts (rbe/loadAccounts1 token)]
                        accounts)))

(defquery-root :pjw
               (value [{:keys [request ast] :as env} {:keys [token] :as params}]
                  (timbre/info "in read :pjw")
                      {}))

(defquery-root :token
     (value [{:keys [request] :as env} {:keys [test token] :as params} ]
          (timbre/info "in read :atoken")
         (timbre/info "env=" env)
         (timbre/info "params" params)
            {}))