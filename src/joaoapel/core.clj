(ns joaoapel.core
	(:require
		[joaoapel.api :as mg-api] 
		[ring.adapter.jetty :as jetty]
	)
)


(defn -main
  [x]
  (println x "Hello, World!"))
