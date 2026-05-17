(ns user
  "REPL bootstrap; clojure.main auto-loads the `user` ns (don't rename). Start: clj -A:test"
  (:require [clojure.string :as str]
            [eftest.runner :as ef]
            [eftest.report.pretty :as pretty]))

(defn- kata-tests
  "Test vars belonging to kata k (matched on the zero-padded ns name)."
  [k]
  (let [tag (format "kata-%02d-" k)]
    ;; rescan on every call so katas added mid-session are picked up
    (filter #(str/includes? (str (ns-name (:ns (meta %)))) tag)
            (ef/find-tests "test"))))

(defn run
  "Run a kata's tests by number — failures red, passes green.
     (run 1)                   all of kata 1's tests
     (run 1 \"first-fifteen\")   one test by name"
  ([k]
   (ef/run-tests (kata-tests k) {:report pretty/report}))
  ([k test-name]
   (let [nm (name test-name)
         vs (filter #(= nm (name (:name (meta %)))) (kata-tests k))]
     (if (seq vs)
       (ef/run-tests vs {:report pretty/report})
       (throw (ex-info (str "No test `" test-name "` in kata " k)
                       {:kata k :test test-name}))))))

(println "Ready.

  (run 1)                   run all of kata 1's tests
  (run 1 \"first-fifteen\")   run one test by name

Loop: solve a kata in src/katas, re-evaluate it, (run <n>) until green.")
