(ns runner
  "CLI test runner (eftest). Invoked by the justfile:
     just test                 every test in the repo
     just test 1               every test for kata 1
     just test 1 some-test     the test `some-test` in kata 1
   Under the hood: clojure -M:test -m runner [k [test-name]]."
  (:require [clojure.string :as str]
            [eftest.report.pretty :as pretty]
            [eftest.runner :as ef]))

(defn- all-tests []
  (ef/find-tests "test"))

(defn- kata-tests
  "Test vars whose namespace carries the zero-padded `kata-NN-` tag."
  [k]
  (let [tag (format "kata-%02d-" k)]
    (filter #(str/includes? (str (ns-name (:ns (meta %)))) tag)
            (all-tests))))

(defn- run-tests!
  "Run `tests` with eftest's pretty reporter; exit non-zero on any failure."
  [tests]
  (let [{:keys [fail error] :or {fail 0 error 0}}
        (ef/run-tests tests {:report pretty/report})]
    (System/exit (if (pos? (+ fail error)) 1 0))))

(defn- parse-kata [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException _
      (binding [*out* *err*]
        (println (str "Not a kata number: " s)))
      (System/exit 2))))

(defn -main
  [& args]
  (case (count args)
    0 (run-tests! (all-tests))
    1 (run-tests! (kata-tests (parse-kata (first args))))
    2 (let [[k nm] args
            k      (parse-kata k)
            vs     (filter #(= nm (name (:name (meta %)))) (kata-tests k))]
        (if (seq vs)
          (run-tests! vs)
          (binding [*out* *err*]
            (println (str "No test `" nm "` in kata " k))
            (System/exit 2))))
    (binding [*out* *err*]
      (println "usage: just test [kata] [test-name]")
      (System/exit 2))))
