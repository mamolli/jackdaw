(ns jackdaw.serdes.edn
  (:require [clojure.edn :refer [read-string]]
            [jackdaw.serdes.fn :as j.s.fn])
  (:import java.nio.charset.StandardCharsets
           org.apache.kafka.common.serialization.Serdes))

(set! *warn-on-reflection* true)

(defn to-bytes
  "Convert a string to byte array."
  [data]
  (.getBytes ^String data StandardCharsets/UTF_8))

(defn from-bytes
  "Converts a byte array to a string."
  [^bytes data]
  (String. data StandardCharsets/UTF_8))

(defn serializer
  "Returns an EDN serializer."
  []
  (j.s.fn/new-serializer {:serialize (fn [_ _ data]
                                       (when data
                                         (to-bytes (prn-str data))))}))

(defn deserializer
  "Returns an EDN deserializer."
  []
  (j.s.fn/new-deserializer {:deserialize (fn [_ _ data]
                                           (read-string (from-bytes data)))}))

(defn serde
  "Returns EDN serde."
  []
  (Serdes/serdeFrom (serializer) (deserializer)))