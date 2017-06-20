(ns integrant-example.server
  (:require [io.pedestal.http :as http]))

(defn create-server [service-map]
  (http/start (http/create-server service-map)))
