(defproject clojure-email-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [com.draines/postal "1.11.3"]]
  :main ^:skip-aot clojure-email-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
