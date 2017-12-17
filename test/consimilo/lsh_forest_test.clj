(ns consimilo.lsh-forest-test
  (:require [clojure.test :refer :all]
            [consimilo.lsh-forest :refer :all]
            [consimilo.lsh-util :refer [v=v keyword-int]]
            [consimilo.minhash :refer [build-minhash]]
            [consimilo.lsh-state :refer [mighty-atom sort-tree]]))

(def minhash1 (build-minhash ["1" "2" "3"]))
(def minhash2 (build-minhash ["1" "2" "10"]))
(def minhash3 (build-minhash ["32" "64" "128"]))

(deftest populate-hashtables-test
  (testing "updates might-atom :hashtables entry"
    (with-redefs [mighty-atom (atom {})]
      (let [private-populate-hashtables #'consimilo.lsh-state/populate-hastables!]
        (private-populate-hashtables "a" minhash1)
        (is (not (empty? (get-in @mighty-atom [:hashtables :0]))))))))

(deftest populate-keys-test
  (testing "updates might-atom :keys entry"
    (with-redefs [mighty-atom (atom {})]
      (let [private-populate-keys #'consimilo.lsh-state/populate-keys!]
        (private-populate-keys "a" minhash1)
        (is (not (empty? (get-in @mighty-atom [:keys :a]))))))))

(deftest lsh-forest-integration-test
  (testing "lsh forest returns best match on query"
    (dorun
      (map-indexed #(add-lsh! (str (inc %)) %2) [minhash1 minhash2 minhash3]))
    (index!)
    (is (= :1 (keyword-int (first (query-forest minhash1 1)))))))
