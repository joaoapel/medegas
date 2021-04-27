(ns joaoapel.olhaogas
  (:gen-class)
  (:require [clojure.java.io :as io]
            [badigeon.javac :as javac])
  (:import  (be.tarsos.dsp.pitch PitchDetectionHandler PitchDetector PitchProcessor
                                PitchDetectionResult PitchProcessor$PitchEstimationAlgorithm)
           (be.tarsos.dsp.io.jvm AudioDispatcherFactory JVMAudioInputStream)
           (be.tarsos.dsp AudioDispatcher AudioEvent AudioProcessor)))


;;https://github.com/clojure/tools.deps.alpha/wiki/Tools
;;Clojure Extended: Java interop (English Edition), Ivan Grishaev


(defn m [pitches]
  (/ (apply + pitches) (count pitches)))




;;Usar reify na interface
(defn detection-pitch [source]
  (let [dispatcher (AudioDispatcherFactory/fromFile (io/file source) 2048 1024)
        algorithm (PitchProcessor$PitchEstimationAlgorithm/YIN)
        pitch (atom[])
        pdh (reify PitchDetectionHandler
              (handlePitch [this result event]
                (let [p (.getPitch result)]
                  (when (<= 2600 p 3300)
                    (swap! pitch conj p)))))

        pitch-processor (PitchProcessor. algorithm 44100 2048 pdh)]
    (.addAudioProcessor dispatcher pitch-processor)
    (.run dispatcher)
    (m @pitch)))

(require '[clojure.java.shell :as sh])

(sh/sh "pwd")

(defn fullness [vazio cheio medido]
  (let [d (- vazio cheio)
        m (- vazio medido)]
    (/ m d)))

(fullness (detection-pitch "./vazio.wav") (detection-pitch "./cheio.wav") 3054)

(detection-pitch "./vazio.wav")
;; => 3222.609619140625


(detection-pitch "./usado.wav")
;; => 3054.1989822387695



(detection-pitch "./cheio.wav")
;; => 2755.599168346774

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
