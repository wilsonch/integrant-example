(ns integrant-example.system
  (:require [integrant.core :as ig]
            [io.pedestal.http :as http]
            [integrant-example.server :as server]
            [integrant-example.routes :as routes]))

(def config
  {::server {::http/routes (ig/ref ::routes/routes)
             ::http/type   :jetty
             ::http/port   8888}

   ::routes routes/routes})

(defmethod ig/init-key ::server [_ service-map]
  (server/create-server service-map))

(defmethod ig/init-key ::routes [_ _]
  routes/routes)
