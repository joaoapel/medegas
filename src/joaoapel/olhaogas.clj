(ns joaoapel.olhaogas
  (:gen-class)
  (:require [clojure.java.io :as io]
            [badigeon.javac :as javac])
  (:import (be.tarsos.dsp.pitch PitchDetectionHandler PitchProcessor PitchDetectionResult PitchProcessor$PitchEstimationAlgorithm)
           (be.tarsos.dsp.io.jvm AudioDispatcherFactory JVMAudioInputStream)
           (be.tarsos.dsp AudioDispatcher AudioEvent AudioProcessor)))

;;tentativa funcionando
(defn detection-pitch [source]
  (let [dispatcher (AudioDispatcherFactory/fromFile (io/file source) 2048 1024)
        algorithm (PitchProcessor$PitchEstimationAlgorithm/YIN)
        pitch-detection (PitchDetectionResult.)
        handler (.handlePitch())]))


(defn detection-pitch [source]
  (let [dispatcher (AudioDispatcherFactory/fromFile (io/file source) 2048 1024)
        algorithm (PitchProcessor$PitchEstimationAlgorithm/YIN)
        audio-processor (.addAudioProcessor())
        pitch-detection (PitchDetectionResult.)
        handler (.handlePitch(pitch-detection))]
    (audio-processor dispatcher)))



;;Colocar ponto no fim do algoritmo não funciona
(defn detection-pitch [source]
  (let [dispatcher (AudioDispatcherFactory/fromFile (io/file source) 2048 1024)
        algorithm (PitchProcessor$PitchEstimationAlgorithm/YIN)
        pitch-processor (.getPitch())
        audio-processor (.addAudioProcessor())
        pitch-detection (PitchDetectionResult.)
        handler (.handlePitch())]
    (-> (.run dispatcher)
        audio-processor
        pitch-detection

        handler)))



(detection-pitch "./cheio.wav")

(defn detection-pitch [source]
  (let [dispatcher (AudioDispatcherFactory/fromFile (io/file source) 2048 1024)
        detector (PitchProcessor$PitchEstimationAlgorithm/YIN)
        handler (.handlePitch())
        result (PitchDetectionResult.)
        audio-event (TarsosDSPAudioFormat)]
    (handler result audio-event)))

(def classe-audio (detection-pitch "./cheio.wav"))

(class classe-audio)

 ;; Compile java sources under the src-java directory
(defn java-compiler [x]
  (javac/javac x {;; Emit class files to the target/classes directory
                  :compile-path "target/classes"
                  ;; Additional options used by the javac command
                  :javac-options ["-cp" "src:target/classes" "-target" "7"
                                  "-source" "7" "-Xlint:-options"]}))



(java-compiler "src-java/TarsosDSP/")

(def processor PitchProcessor$PitchEstimationAlgorithm)

(defn detect [x]
  PitchDetectionResult x)

(defn import-audio-tarsos [x buffersize bufferoverlap]
 (AudioDispatcherFactory/fromFile x buffersize bufferoverlap))

(defn pdh [audio-event]
  (.getPitch audio-event))

(.getFormat file-tarsos)
;; => #object[be.tarsos.dsp.io.TarsosDSPAudioFormat 0x51b96a25
;; "PCM_SIGNED 16000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian"]

(PitchProcessor$PitchEstimationAlgorithm/YIN file-tarsos 1)


(comment
  Parameters:
    sampleRate - The requested sample rate must be supported by the capture device. Nonstandard sample rates can be problematic!
    audioBufferSize - The size of the buffer defines how much samples are processed in one step. Common values are 1024,2048.
    bufferOverlap - How much consecutive buffers overlap (in samples). Half of the AudioBufferSize is common.
    Returns a new audioprocessor

    ,)


(defn import-audio-java [x]
  (io/file x))


(def file-cheio (import-audio-java "./cheio.wav"))

(class file-cheio)
;; => java.io.File

(println file-cheio)

(def file-tarsos (import-audio-tarsos file-cheio 2048 1024))

(class file-tarsos)
;; => be.tarsos.dsp.AudioDispatcher

(defn pitch-handler []
  (PitchDetectionHandler PitchDetectionResult AudioEvent))

(defn audioprocessor [x]
  AudioProcessor x)

(audioprocessor file-tarsos)

(class file-tarsos)

(defn pitch-processor [x]
  PitchProcessor x)

(pitch-processor PitchProcessor$PitchEstimationAlgorithm/YIN)

(PitchProcessor PitchProcessor$PitchEstimationAlgorithm)





(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
;; => #'joaoapel.olhaogas/-main
;; => #'joaoapel.olhaogas/-main


(comment

  void connectsAudioDispatchertoMicrophone() {}

  AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

  PitchDetectionHandler pdh = new PitchDetectionHandler() {}
     @Override
     public void handlePitch(final PitchDetectionResult result, AudioEvent e) {}
        final float pitchInHz = result.getPitch();
        runOnUiThread(new Runnable() {})
           @Override
           public void run() {}
              if (pitchInHz > 1)
                 Log.d(TAG, "pitchInHz: " + pitchInHz);


        ;

  ;
  AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,)
          22050,
          1024,
          pdh;
  dispatcher.addAudioProcessor(p);

  thread = new Thread(dispatcher, "Audio Dispatcher");
  thread.start();




  ,)
