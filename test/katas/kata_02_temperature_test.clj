(ns katas.kata-02-temperature-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-02-temperature :refer [c->f f->c comfortable?]]))

(deftest celsius-to-fahrenheit
  (is (= 32 (c->f 0)))
  (is (= 212 (c->f 100)))
  (is (= 98.6 (double (c->f 37)))))

(deftest fahrenheit-to-celsius
  (is (= 0 (f->c 32)))
  (is (= 100 (f->c 212)))
  (is (= -40 (f->c -40))))

(deftest round-trips-stay-exact
  (testing "integer values that land on integers do not drift to floats"
    (is (= 0 (f->c (c->f 0))))
    (is (= 100 (f->c (c->f 100))))
    (is (= -40 (c->f (f->c -40))))))

(deftest comfortable-default-range
  (is (true? (comfortable? 18)))
  (is (true? (comfortable? 21)))
  (is (true? (comfortable? 24)))
  (is (false? (comfortable? 17)))
  (is (false? (comfortable? 25)))
  (is (false? (comfortable? 30))))

(deftest comfortable-explicit-range
  (is (true? (comfortable? 21 0 100)))
  (is (true? (comfortable? 0 0 100)))
  (is (true? (comfortable? 100 0 100)))
  (is (false? (comfortable? 21 22 25)))
  (is (false? (comfortable? -5 0 100))))
