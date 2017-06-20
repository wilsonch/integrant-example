(ns dev
  (:require [integrant.repl :refer [prep init go reset halt set-prep!]]
            [integrant.core :as ig]
            [integrant-example.system :as system]
            [integrant-example.routes :as routes]
            [io.pedestal.http :as http]))

(set-prep! (constantly
             {::system/server {::http/routes (ig/ref ::system/routes)
                               ::http/type   :jetty
                               ::http/port   8888}
              ::system/routes routes/routes}))

;; switch to dev ns
;; user=> (dev)
;;
;; loads the above config into integrant.repl.state/config
;; dev=> (prep)
;;
;; initialise the system into integrant.repl.state/system
;; dev=> (init)
;;
;; go = prep + init
;; dev=> (go)
;;
;; reload source files and restart system
;; dev=> (reset)
;;
;; dev=> (halt)
