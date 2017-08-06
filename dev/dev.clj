(ns dev
  (:require [integrant.repl :refer [prep init go reset halt set-prep!]]
            [integrant.core :as ig]
            [integrant-example.system :as system]
            [integrant-example.routes :as routes]
            [io.pedestal.http :as http]))

;; switch to dev ns
;; user=> (dev)
;;

;; create a prep fn that returns a config map
(set-prep! (constantly
             {::system/server {::http/routes (ig/ref ::system/routes)
                               ::http/type   :jetty
                               ::http/port   8888
                               ::http/join?  false
                               :env          :dev}
              ::system/routes routes/routes}))

;; loads the above config into integrant.repl.state/config
;; dev=> (prep)
;;
;; turn the config into a running system stored in integrant.repl.state/system
;; dev=> (init)
;;
;; go = prep + init
;; dev=> (go)
;;
;; reload source files and restart system
;; dev=> (reset)
;;
;; dev=> (halt)
