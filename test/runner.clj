(ns runner
  "CLI test runner (eftest). Invoked by the justfile:
     just test                 every test in the repo
     just test 1               every test for kata 1
     just test 1 empty-input   the test `empty-input` in kata 1
   Under the hood: clojure -M:test -m runner [k [test-name]].
   Loads only the requested kata's test namespaces. Runs serially, one
   namespace at a time in name order and in source order within each, and
   prints one summary. Exit 0 when green, 1 on any failure, 2 on bad input."
  (:require [clojure.string :as str]
            [clojure.test :as test]
            [clojure.tools.namespace.find :as find]
            [eftest.report.pretty :as pretty]
            [eftest.runner :as ef]
            [matcher-combinators.config :as mc]
            [puget.printer :as puget]))

(defn- die
  "Print `msg` to stderr and exit 2."
  [msg]
  (binding [*out* *err*] (println msg))
  (System/exit 2))

(defn- test-namespaces
  "Namespace symbols declared under test/, read from the `ns` forms; nothing
   is loaded."
  []
  (find/find-namespaces-in-dir (java.io.File. "test")))

(defn- load-tests
  "Require `nss`, then return their test vars."
  [nss]
  (run! require nss)
  (ef/find-tests nss))

(defn- all-tests [] (load-tests (test-namespaces)))

(defn- kata-tests
  "Test vars of the namespaces carrying the zero-padded `kata-NN-` tag. Only
   those namespaces load, so a broken file elsewhere cannot block kata `k`."
  [k]
  (let [tag (format "kata-%02d-" k)
        nss (filter #(str/includes? (name %) tag) (test-namespaces))]
    (if (seq nss)
      (load-tests nss)
      (die (str "No kata " k)))))

(defn- trace-depth
  "Stack frames to print for `t`: at least 8, and always through the first
   frame from a `katas.` namespace in the root cause, the trace printed."
  [t]
  (let [root (when (instance? Throwable t)
               (last (take-while some? (iterate #(.getCause ^Throwable %) t))))
        i (when root
            (first (keep-indexed
                    (fn [i ^StackTraceElement e]
                      (when (str/starts-with? (.getClassName e) "katas.") i))
                    (.getStackTrace ^Throwable root))))]
    (max 8 (inc (or i 0)))))

(defn- readable
  "matcher-combinators tags a mismatch for `print-method`; eftest's pretty
   printer ignores that, so pre-render it."
  [m]
  (cond-> m
    (= :matcher-combinators.clj-test/mismatch (type (:actual m)))
    (update :actual #(symbol (pr-str %)))))

(defn- run-tests!
  "Run `vars` serially: one eftest run per namespace in name order (eftest
   shuffles namespaces otherwise), vars in source order within each. One
   report format everywhere; colour only on a terminal. Print one merged
   summary; exit 1 on any failure or error."
  [vars]
  (when (empty? vars) (die "No tests found"))
  (let [tty? (some? (System/console))
        opts {:report (fn [m]
                        (when-not (= :summary (:type m))
                          (binding [test/*stack-trace-depth*
                                    (trace-depth (:actual m))]
                            (pretty/report (readable m))))),
              :multithread? false,
              :capture-output? false}
        by-ns (sort-by key (group-by (comp str :ns meta) vars))]
    (when-not tty? (mc/disable-ansi-color!))
    (binding [pretty/*fonts* (if tty?
                               pretty/*fonts*
                               (update-vals pretty/*fonts* (constantly "")))
              puget/*options* (cond-> puget/*options*
                                (not tty?) (assoc :color-markup nil))]
      (let [summaries (mapv (fn [[_ vs]]
                              (ef/run-tests (sort-by (comp :line meta) vs)
                                            opts))
                            by-ns)
            {:keys [fail error], :as summary}
            (apply merge-with + (map #(dissoc % :type) summaries))]
        (pretty/report (assoc summary :type :summary))
        (System/exit (if (pos? (+ fail error)) 1 0))))))

(defn- parse-kata [s]
  (try (Integer/parseInt s)
       (catch NumberFormatException _ (die (str "Not a kata number: " s)))))

(defn -main [& args]
  (case (count args)
    0 (run-tests! (all-tests))
    1 (run-tests! (kata-tests (parse-kata (first args))))
    2 (let [[k nm] args
            k (parse-kata k)
            vars (sort-by (comp :line meta) (kata-tests k))
            names (map (comp name :name meta) vars)
            vs (filter #(= nm (name (:name (meta %)))) vars)]
        (if (seq vs)
          (run-tests! vs)
          (die (format "No test `%s` in kata %d; its tests: %s"
                       nm
                       k
                       (str/join " " names)))))
    (die "usage: just test [kata [test-name]]")))
