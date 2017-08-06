(ns integrant-example.routes)

(defn respond-hello [request]
      {:status 200 :body "Hello, world!"})

(def routes
  #{["/greet" :get respond-hello :route-name :greet]})
