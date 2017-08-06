(ns integrant-example.system
  (:require [integrant.core :as ig]
            [io.pedestal.http :as http]
            [integrant-example.server :as server]
            [integrant-example.routes :as routes]))

(defmethod ig/init-key ::routes [_ _]
  routes/routes)

(defmethod ig/init-key ::server [_ service-map]
  (server/create-server service-map))

(defmethod ig/halt-key! ::server [_ service-map]
  (http/stop service-map))