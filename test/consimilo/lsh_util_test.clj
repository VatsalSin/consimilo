(ns consimilo.lsh-util-test
  (:require [clojure.test :refer :all]
            [consimilo.lsh-util :refer :all]
            [consimilo.lsh-query :refer :all]))

(deftest get-hashranges-test
  (testing "all ranges less than k * num trees"
    (is (every? #(>= (* 5 8) %) (map #(apply max %) (get-hashranges 5 8)))))
  (testing "buckets are the same size"
    (is (apply = (map #(- (second %) (first %)) (get-hashranges 5 8))))))

(deftest get-range-test
  (testing "returns correct number of buckets"
    (is (= 40 (get-range 5 8)))))

(deftest keyword-int-test
  (testing "returns int turned into keyword"
    (is (= :0 (keywordize 0)))))

(deftest build-hashtables-test
  (testing "initializes empty hashtables structure"
    (is (= {:0 {} :1 {} :2 {}}
           (build-hashtables 3)))))

(deftest build-sorted-hashtables-test
  (testing "initializes empty sorted-hashtables structure"
    (is (= {:0 [] :1 [] :2 []}
           (build-sorted-hashtables 3)))))

(deftest v=v-test
  (testing "vector1 = vector2"
    (is (true? (v=v [1 2 3] [1 2 3])))
    (is (false? (v=v [1 2 2] [0 2 2])))
    (is (false? (v=v [1 2 2] [1 0 2])))
    (is (false? (v=v [1 2 2] [1 2 3])))))

(deftest v>=v-test
  (testing "vector1 >= vector2"
    (is (true? (v>=v [2 3 3] [1 2 3])))
    (is (true? (v>=v [2 3 3] [2 2 3])))
    (is (true? (v>=v [2 3 3] [2 3 2])))
    (is (true? (v>=v [2 3 3] [2 3 3])))
    (is (false? (v>=v [1 3 3] [2 3 3])))
    (is (false? (v>=v [2 2 3] [2 3 3])))
    (is (false? (v>=v [2 3 2] [2 3 3])))))

(deftest coll-prefix-test
  (testing "get first k elements of collection"
    (is (= [1 2 3] (coll-prefix [1 2 3 4 5] 3))))
  (testing "get first k elements of empty collection"
    (is (= [] (coll-prefix [] 3)))))

(deftest slice-test
  (let [private-slice #'consimilo.lsh-util/slice]
    (testing "slice of empty coll"
      (is (= ()
             (private-slice 0 10 []))))
    (testing "slice at begining"
      (is (= '(1 2 3)
             (private-slice 0 3 [1 2 3 4 5]))))
    (testing "slice in middle"
      (is (= '(2 3 4)
             (private-slice 1 4 [1 2 3 4 5]))))))

(deftest slice-minhash-test
  (testing "returns sequence of slices"
    (is (= '((1 2 3) (4 5 6))
           (slice-minhash [1 2 3 4 5 6] [[0 3] [3 6]])))))

(deftest tree-keys-test
  (testing "correct keywords"
    (is (= [:0 :1 :2]
           (tree-keys 3)))))

(deftest pred-search-test
  (let [private-pred-search #'consimilo.lsh-query/pred-search
        sorted-vec [[0 1 2] [1 2 3] [2 3 4] [3 4 5] [4 5 6] [5 6 7] [6 7 8] [7 8 9] [8 9 0]]]
    (testing "search for min"
      (is (= 2
           (private-pred-search #(>= (compare (get sorted-vec %) [2 3 4]) 0) (count sorted-vec)))))))
