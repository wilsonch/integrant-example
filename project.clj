(defproject integrant-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [io.pedestal/pedestal.service "0.5.2"]
                 [io.pedestal/pedestal.route   "0.5.2"]
                 [io.pedestal/pedestal.jetty   "0.5.2"]
                 [org.slf4j/slf4j-simple       "1.7.21"]
                 [integrant "0.4.0"]
                 [integrant/repl "0.2.0"]]

  :source-paths ["src"]

  :resource-paths ["resources"]

  :repl-options {:init-ns user
                 :welcome (println "Type (dev) to load dev ns, then (go) to run system; (reset) to reload system")}

  :profiles {:dev {:source-paths ["dev"]}})
