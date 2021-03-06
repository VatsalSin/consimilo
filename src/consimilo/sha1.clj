(ns consimilo.sha1
  (:import (java.security MessageDigest)))

(defn- get-hash
  "Returns 'type' hash (ex: sha1) for data"
  [type data]
  (.digest (MessageDigest/getInstance type) (.getBytes data)))

(defn- sha1-hash
  "Returns the sha1 hash of data"
  [data]
  (get-hash "sha1" data))

(defn get-hash-bigint
  "Converts the sha1 hash into a Java bigint"
  [data]
  (bigint (sha1-hash data)))