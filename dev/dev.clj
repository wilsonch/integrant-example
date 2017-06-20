(ns dev
    (:require [integrant.repl :refer [prep init go reset halt set-prep!]]
              [integrant.core :as ig]))

(set-prep! (constantly {::foo {:example? true}}))

(defmethod ig/init-key ::foo [a b]
  (println a b)
  b)

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
