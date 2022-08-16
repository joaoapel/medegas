(ns joaoapel.api
	(:require 
		[ring.adapter.jetty :as jetty]
		[ring.middleware.params :refer [wrap-params]]
		[ring.middleware.json :refer [wrap-json-body]]
     	[clojure.pprint]
     	[clojure.data.json :as cj]
     	[clojure.java.io :as io]
	)
)

(def TG_API_BASE "https://api.telegram.org/bot")
(def TG_API_KEY (System/getenv "TG_API_KEY"))

(defn register-web-hook [hook-uri]
	(slurp (str TG_API_BASE TG_API_KEY "/setWebhook?url=" hook-uri "/tg-bot/medgascheck-v1"))
)

(defn download-file [uri file-tmp-path]
;;download file to /tmp folder for further processing
;;returns absolute path to downloaded file

	(try 
	 	(with-open [
	 			in (io/input-stream uri)
	          	out (io/output-stream file-tmp-path)]
		(io/copy in out))

		(println "File downloaded to: " file-tmp-path)

		(catch Exception e
			(println "DOWNLOAD ERROR: " (.getMessage e))
		)
	)

	;;return absolute path to local file
	file-tmp-path
)

;;vamos fazer algum math
(defn process-signal [file-path]
	(Thread/sleep 2000)
	(rand 100)
)

(defn get-file-uri [request]
;;prepare remote file for download
;;return remote file uri and absolute path to local file

	(let [
		chat-id (get-in request [:body "message" "chat" "id"])
		message-id (get-in request [:body "message" "message_id"])
		file-id (get-in request [:body "message" "voice" "file_id"])

		;;Query remote file info
		file-resp (slurp (str TG_API_BASE TG_API_KEY "/getFile?file_id=" file-id))
		file-path (get-in (cj/read-str file-resp) ["result" "file_path"])

		;;construct full uri to file
		file-uri (str "https://api.telegram.org/file/bot" TG_API_KEY "/" file-path)
		;;construct local file uri
		file-tmp-path (str "/tmp/" file-id ".ogg")
		]

		[file-uri file-tmp-path]
	)
)

(defn bot-handler [request]
;;Resonsible for handling chat requests from telegram bot
;;Receive request from router and returns map in case of success or
	(if (not (= (:request-method request) :post))
		{:status 404 :body "Pagina nao existe :("}

		(let [voice-mime-type (get-in request [:body "message" "voice" "mime_type"])]
			;;(println (get-in request [:body "message"]))
			(if (not (= voice-mime-type "audio/ogg"))
				(do
					(println "Oooops, nao sei o que fazer como isso format")
					{:status 200 :error "Invalid format"}
				)

				(do
					;;get audio
					(let [chat-id (get-in request [:body "message" "chat" "id"])
						  fpath (get-file-uri request)
						  level (format "%.2f" (process-signal (download-file (nth fpath 0) (nth fpath 1))))]
						;;send response to user

						(slurp (str TG_API_BASE TG_API_KEY "/sendMessage?chat_id=" chat-id "&text=" level))
						{:status 200 :error nil :body level}
					)
				)
			)
		)
	)
)


(defn welcome-handler [request]
;;default handler
	{
		:status 200
		:body "Bem venido, para meu canal!"
	}
)

(def handler-map {
	:tg-bot #'bot-handler
	:welcome #'welcome-handler
})


(defn router []
;;A simple middleware responsible for dispatching incoming requests to appropriate handlers
;;Matches handler from handler-map and pass request object to handler
	(fn [request]

		;;for debugging purposes only
		(def update request)
		(clojure.pprint/pprint request)

		(if (= (:uri request) "/tg-bot/medgascheck-v1") 
			((:tg-bot handler-map) request)
			((:welcome handler-map) request))
	)
)

(def app-handler
	(->
	 (router) ;;dispatch incoming request to handler
	 (wrap-json-body) ;;parse incoming json-body to clojure data types
	 (wrap-params {:encoding "UTF-8"})
	)
)

(defonce server (atom nil))

(defn start-server [port]
	;;force exit if env variable is missing
	(if (nil? TG_API_KEY)
		(System/exit 1)
	)
	
	(swap! server (jetty/run-jetty #'app-handler {:port port :join? false}))
)

(defn stop-server []
	(swap! server (fn [_] nil))
)
