(ns joaoapel.olhaogas
  (:gen-class)
  (:require [clojure.java.io :as io]
            [badigeon.javac :as javac])
  (:import (be.tarsos.dsp.pitch PitchDetectionHandler PitchDetector PitchProcessor
                                PitchDetectionResult PitchProcessor$PitchEstimationAlgorithm)
           (be.tarsos.dsp.io.jvm AudioDispatcherFactory JVMAudioInputStream)
           (be.tarsos.dsp AudioDispatcher AudioEvent AudioProcessor)))


;;https://github.com/clojure/tools.deps.alpha/wiki/Tools
;;Clojure Extended: Java interop (English Edition), Ivan Grishaev

;;Usar reify na interface
(defn detection-pitch [source]
  (let [dispatcher (AudioDispatcherFactory/fromFile (io/file source) 2048 1024)
        algorithm (PitchProcessor$PitchEstimationAlgorithm/YIN)
        pitch-detection (PitchDetectionResult.)
        pdh (reify PitchDetectionHandler
              (handlePitch [this result event]
                (println result event)))
        pitch (atom[])]
    (-> dispatcher)))



(detection-pitch "./cheio.wav")


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



;;-----------------------------------------------------------------
;;TESTE COM A BIBLIOTECA BADIGEON
;;-----------------------------------------------------------------
;; Compile java sources under the src-java directory


(defn java-compiler [x]
  (javac/javac x {;; Emit class files to the target/classes directory
                  :compile-path "target/classes"
                  ;; Additional options used by the javac command
                  :javac-options ["-cp" "src:target/classes" "-target" "7"
                                  "-source" "11" "-Xlint:-options"]}))



(java-compiler "src-java/TarsosDSP/")
;;-----------------------------------------------------------------
;;-----------------------------------------------------------------


;;-----------------------------------------------------------------
;; TESTES COM A TRANSFORMAÇÃO DO AUDIO
;;-----------------------------------------------------------------
;;Transformando Java IO para formato do Tarsos


(defn import-audio-tarsos [x buffersize bufferoverlap]
 (AudioDispatcherFactory/fromFile x buffersize bufferoverlap))



(comment
  Parameters:
    sampleRate - The requested sample rate must be supported by the capture device. Nonstandard sample rates can be problematic!
    audioBufferSize - The size of the buffer defines how much samples are processed in one step. Common values are 1024,2048.
    bufferOverlap - How much consecutive buffers overlap (in samples). Half of the AudioBufferSize is common.
    Returns a new audioprocessor

    ,)


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
