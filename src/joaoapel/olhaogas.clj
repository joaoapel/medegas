(ns joaoapel.olhaogas
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:import (be.tarsos.dsp.pitch PitchDetectionResult PitchProcessor)
           (be.tarsos.dsp.io.jvm AudioDispatcherFactory)))

(defn processor [x]
  PitchProcessor x)

(defn detect [x]
  PitchDetectionResult x)

(defn import-audio-tarsos [x buffersize bufferoverlap]
  (AudioDispatcherFactory/fromFile x y z))

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




(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
;; => #'joaoapel.olhaogas/-main
;; => #'joaoapel.olhaogas/-main
