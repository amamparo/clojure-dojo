(ns katas.kata-13-bank-account)

;; ─── Kata 13: Bank account ─────────────────────────────────────────────
;;
;; This is NOT a blank stub. It is inherited, working code with one
;; concurrency bug. Read it with the guide's method; the test file is
;; the contract (it includes concurrency tests).
;;
;; The account is event-sourced. The atom holds ONLY an event log:
;; {:history [{:op :open :amount n} {:op :deposit :amount a} ...]}.
;; There is no stored :balance. `apply-event` is the pure step
;; ([balance event] -> balance); `replay` folds it over the whole log
;; from the empty balance 0; `balance` is just `(replay history)` — a
;; derived projection, recomputed on demand, never mutated.
;;
;; `open-account`, `balance`, `apply-event`, `replay`, and
;; `valid-amount!` are CORRECT — leave them alone.
;;
;; `deposit!` is written the WRONG way: it `deref`s the atom, computes
;; the new log, and `reset!`s it — so concurrent deposits lose appended
;; events. Move the append INSIDE one `swap!` so log growth is atomic.
;; Do not change its contract (it still returns the new balance).
;;
;; `withdraw!` is UNIMPLEMENTED. Validate the amount first (outside the
;; swap, via valid-amount!). Then, atomically inside one `swap!`:
;; `replay` the current log to get the live balance; if the amount
;; exceeds it, reject — throw (ex-info {:type :insufficient-funds …}) —
;; leaving the log (hence balance AND history) untouched; otherwise
;; append a {:op :withdraw :amount a} event. Return the new balance.
;;
;;   (def a (open-account 100))
;;   (deposit! a 20)   => 120
;;   (withdraw! a 50)  => 70
;;   (history a) => [{:op :open :amount 100}
;;                   {:op :deposit :amount 20}
;;                   {:op :withdraw :amount 50}]
;;   (balance a) => 70   ; == (replay (history a))

(defn- valid-amount! [amount]
  (when-not (and (number? amount) (>= amount 0))
    (throw (ex-info "invalid amount" {:type :invalid-amount, :amount amount}))))

(defn apply-event [bal {:keys [op amount]}]
  (case op
    :open amount
    :deposit (+ bal amount)
    :withdraw (- bal amount)))

(defn replay [history] (reduce apply-event 0 history))

(defn open-account ([] (open-account 0))
  ([opening]
   (valid-amount! opening)
   (atom {:history [{:op :open, :amount opening}]})))

(defn balance [acct] (replay (:history @acct)))

(defn deposit! [acct amount]
  (valid-amount! amount)
  (let [a @acct]
    (reset! acct (update a :history conj {:op :deposit, :amount amount}))
    (balance acct)))

(defn withdraw! [acct amount]
  ;; TODO
)

(defn history [acct] (:history @acct))
