(defproject medegas "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
	[org.clojure/clojure "1.10.0"]
	[org.clojure/data.json "2.4.0"]
	[ring/ring-core "1.7.1"]
	[ring/ring-jetty-adapter "1.7.1"]
	[ring/ring-json "0.5.1"]
	[forma/jblas "1.2.1"]
	[com.github.wendykierp/JTransforms "3.1"]
	[http-kit "2.6.0"]
	[cfft "0.1.0"]
  ]
  :repl-options {:init-ns joaoapel.core})
