(ns joaoapel.olhaogas
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.math.numeric-tower :as math]
            [badigeon.javac :as javac]
            #_[flow-storm.api :as fsa]
            [clojure.java.shell :as sh])
  (:import  (be.tarsos.dsp.pitch PitchDetectionHandler PitchDetector PitchProcessor
                                PitchDetectionResult PitchProcessor$PitchEstimationAlgorithm)
           (be.tarsos.dsp.io.jvm AudioDispatcherFactory JVMAudioInputStream)
           (be.tarsos.dsp AudioDispatcher AudioEvent AudioProcessor)))


;;https://github.com/clojure/tools.deps.alpha/wiki/Tools
;;Clojure Extended: Java interop (English Edition), Ivan Grishaev


;;Para ligar o debugger SPC o t comando:
;; clj -Sdeps '{:deps {jpmonettas/flow-storm-debugger {:mvn/version "0.3.3"}}}' -m flow-storm-debugger.server

;;(sh/sh "clj -Sdeps '{:deps {jpmonettas/flow-storm-debugger {:mvn/version "0.3.3"}}}' -m flow-storm-debugger.server")

#_(fsa/connect)

#_(sh/sh "pwd")




(defn m [pitches]
  (/ (apply + pitches) (count pitches)))

(defn detection-pitch [source]
  (let [dispatcher (AudioDispatcherFactory/fromFile (io/file source) 2048 1024)
        algorithm (PitchProcessor$PitchEstimationAlgorithm/YIN)
        pitch (atom[])
        pdh (reify PitchDetectionHandler
              (handlePitch [this result event]
                (let [p (.getPitch result)]
                  (when (pos? p)
                    (swap! pitch conj p)))))
        pitch-processor (PitchProcessor. algorithm 44100 2048 pdh)]
    (.addAudioProcessor dispatcher pitch-processor)
    (.run dispatcher)
    (m @pitch)))




(defn fullness [medido]
  (let [d (- 3222 2755)
        m (- 3222 (detection-pitch medido))]
    (/ m d)))



(detection-pitch "./resources/cheio/a1.wav")
;; => 336.3449350992839

(detection-pitch "./resources/cheio/a2.wav")
;; => 358.95327654751867


(detection-pitch "./resources/cheio/a3.wav")
;; => 48.72014570236206

(detection-pitch "./resources/vazio/v2.wav")
;; => 55.05217695236206



(detection-pitch "./resources/vazio/v3.wav")
;; => 51.68473434448242






(detection-pitch "./vazio.wav")
;; => 3222.609619140625


(detection-pitch "./usado.wav")
;; => 3054.1989822387695


(detection-pitch "./cheio.wav")
;; => 2755.599168346774
;;




(comment
  "
   PitchDetectionHandler handler = new PitchDetectionHandler() {
        @Override
        public void handlePitch(PitchDetectionResult pitchDetectionResult,
                AudioEvent audioEvent) {
            System.out.println(audioEvent.getTimeStamp() + " " pitchDetectionResult.getPitch());
        }
    };
    AudioDispatcher adp = AudioDispatcherFactory.fromDefaultMicrophone(2048, 0);
    adp.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.YIN, 44100, 2048, handler));
    adp.run();
")
;; => nil



;;-----------------------------------------------------------------
;;TESTE COM A BIBLIOTECA BADIGEON (USAR CÓDIGO JAVA DIRETO)
;;-----------------------------------------------------------------
;; Compile java sources under the src-java directory

(comment
  (defn java-compiler [x]
    (javac/javac x {;; Emit class files to the target/classes directory
                    :compile-path "target/classes"
                  ;; Additional options used by the javac command
                    :javac-options ["-cp" "src:target/classes" "-target" "7"
                                    "-source" "11" "-Xlint:-options"]}))



  (java-compiler "src-java/TarsosDSP/"))
;;-----------------------------------------------------------------
;;-----------------------------------------------------------------

(defn -main
  "Chama a função principal de detecção."
  [args]
  (println "O seu botijão está com a seguinte porcentagem de gás: " (math/ceil (* (fullness args) 100))))
