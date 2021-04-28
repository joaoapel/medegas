(ns joaoapel.olhaogas-test
  (:require [clojure.test :refer :all]
            [clojure.math.numeric-tower :as math]
            [joaoapel.olhaogas :refer :all]))


(deftest detection-pitch-test
  (testing "Testando a função principal"
    (is (= 3223.0 (math/ceil (detection-pitch "./vazio.wav"))))))
