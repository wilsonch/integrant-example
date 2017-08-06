(ns integrant-example.routes
  (:require [clojure.data.json :as json]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.content-negotiation :as content-neg]
            [io.pedestal.interceptor.chain :refer [terminate]]
            [io.pedestal.interceptor.helpers :as interceptor]
            [io.pedestal.test :as test]
            [clj-time.core :refer [hours from-now]]
            [buddy.sign.jwt :as jwt]
            [buddy.auth.middleware :as middleware]
            [buddy.auth.backends.token :refer [jws-backend]]))

(def supported-types ["text/html" "application/edn" "application/json" "text/plain"])
(def content-neg-int (content-neg/negotiate-content supported-types))

(def secret "secret-stuff")
(def encryption {:alg :hs512})
(def auth-backend (jws-backend {:secret secret :options encryption}))

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok (partial response 200))
(def created (partial response 201))
(def accepted (partial response 202))
(def unauthorized (partial response 401 {:message "Unauthorized"}))
(def forbidden (partial response 403 {:message "Not allowed"}))

(defn coerce-body
  [context]
  (let [accepted (get-in context [:request :accept :field] "text/plain")
        response (get context :response)
        body (get response :body)
        coerced-body (case accepted
                       "text/html" body
                       "text/plain" body
                       "application/edn" (pr-str body)
                       "application/json" (json/write-str body))
        updated-response (assoc response
                           :headers {"Content-Type" accepted}
                           :body coerced-body)]
    (assoc context :response updated-response)))


(defonce database (atom {:users [{:username "rich" :password "hickey"}]}))

(defn valid-login? [{:keys [username password]}]
  (->> @database
       :users
       (filter #(and (= username (:username %)) (= password (:password %))))
       first))

(defn echo
  [context]
  (let [request (:request context)
        response (ok context)]
    (assoc context :response response)))

(defn login
  [{:keys [request] :as context}]
  (let [{:keys [json-params]} request
        valid? (valid-login? json-params)]
    (if-not valid?
      (assoc context :response (unauthorized))
      (let [claims {:user {:username (:username json-params)}
                    :exp  (-> 3 hours from-now)}
            token (jwt/sign claims secret encryption)]
        (assoc context :response (ok {:token token}))))))

(defn check-auth
  [{:keys [request] :as context}]
  (let [req (middleware/authentication-request request auth-backend)]
    (if (:identity req)
      (assoc context :request req)
      (-> context
          terminate
          (assoc :response (unauthorized))))))

(def login-interceptors
  [content-neg-int
   (body-params/body-params)
   (interceptor/around ::login login coerce-body)])

(def protected-interceptors
  [(interceptor/before ::check-auth check-auth)
   (interceptor/before ::protected echo)])

(def routes
  (route/expand-routes
    #{["/login" :post login-interceptors]
      ["/protected" :get protected-interceptors]}))

(comment
  ; curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '{"username":"rich", "password":"hickey"}' http://localhost:8888/login
  ; curl -H "Authorization: Token xxxx" localhost:8888/protected
  )