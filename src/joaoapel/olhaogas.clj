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




(defn fullness [vazio cheio medido]
  (let [d (- vazio cheio)
        m (- vazio medido)]
    (/ m d)))

(fullness (detection-pitch "./vazio.wav") (detection-pitch "./cheio.wav") (detection-pitch "./usado.wav"))

(detection-pitch "./vazio.wav")
;; => 3222.609619140625

(math/ceil (detection-pitch "./vazio.wav"))


(detection-pitch "./usado.wav")
;; => 3054.1989822387695

(defn filtro [x]
  (when (and (<= x 3300) (>= x 2600)) x))

(filtro 3000)

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
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
