(ns joaoapel.api-test
  (:require [clojure.test :refer :all]
            [joaoapel.api :refer :all]))

(def tg-request (array-map
	:request-method :post
	:body (array-map
		"message" (array-map
			"message_id" 25
			"chat" (array-map
				"id" 790472911
				"first_name" "Daku"
				"username" "kuraz"
			)
			"voice" (array-map
				"file_name" "vazio.ogg"
				"mime_type" "audio/ogg"
				"file_id" "BQACAgQAAxkBAAMZYvtDpi3ixYR3KvXlE_rRpIkw068AAmMOAALCZNhTZzhNVpkjKoIpBA"
				"file_unique_id" "AgADYw4AAsJk2FM"
				"file_size" 92856
				"caption" "VAZIOOOO"
			)
		)
	)
))


(deftest test-download-file
	(is (not (nil? (download-file "http://localhost:9999/file-doesnotexist/unknown.jpg" "/tmp/medgas-test.jpg"))))
	(is (= "/tmp/medgas-898000.jpg" (download-file "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/Bird_-_24.12.2007.jpg/286px-Bird_-_24.12.2007.jpg" "/tmp/medgas-898000.jpg")))
	(is (.exists (java.io.File. "/tmp/medgas-898000.jpg")))
)

(deftest test-get-file-uri
	(let [file-uri (get-file-uri tg-request)]
		(is (not (nil? file-uri)))
		(is (= (count file-uri) 2))
		(is (not (nil? (re-matches (re-pattern "^http.+ogg$") (first file-uri)))))
		(is (not (nil? (re-matches (re-pattern "^/tmp.+ogg$") (last file-uri)))))
	)
)

(deftest test-bothandler
	(let [invalid-doc-format (assoc (dissoc 
			(get-in tg-request [:body "message" "document"]) "mime_type")
		 	"mime_type" "audio/x-mp3")

		  inv-tg-msg (assoc (dissoc (get-in tg-request [:body "message"]) "document") "document" invalid-doc-format)
		  inv-tg-request (assoc {:request-method :post} :body inv-tg-msg)
		]
		(is (nil? (:error (bot-handler tg-request))))
		(is (not (nil? (:error (bot-handler inv-tg-request)))))
		(is (= 404 (:status (bot-handler {:request-method :get}))))
	)
)


(deftest test-router
	(let [
		dummy-request-welcome {:uri "/welcome"}
		dummy-request-bot-post {:uri "/tg-bot/medgascheck-v1" :request-method :post}
		dummy-request-bot-put {:uri "/tg-bot/medgascheck-v1" :request-method :put}]

		(is (= 200 (:status ((router) dummy-request-welcome))))
		(is (not (nil? (:error ((router) dummy-request-bot-post)))))
		(is (= 404 (:status ((router) dummy-request-bot-put))))
	)
)