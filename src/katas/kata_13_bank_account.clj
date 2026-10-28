(ns katas.kata-13-bank-account)

;; ─── Kata 13: Bank account ─────────────────────────────────────────────
;;
;; This is NOT a blank stub. It is inherited, working code with one
;; concurrency bug. Read it with the method from DENSHO §15 (Reading an
;; unfamiliar namespace); the test file is the contract.
;;
;; The account is event-sourced. The atom holds ONLY an event log:
;; {:history [{:op :open :amount n} {:op :deposit :amount a} ...]}.
;; There is no stored :balance. `apply-event` is the pure step
;; ([balance event] -> balance); `replay` folds it over the whole log
;; from the empty balance 0; `balance` is just `(replay history)` — a
;; derived projection, recomputed on demand, never mutated.
;;
;; `open-account`, `balance`, `history`, `apply-event`, `replay`, and
;; `valid-amount!` are CORRECT — leave them alone.
;;
;; `deposit!` has ONE concurrency defect that `concurrent-deposits` exposes:
;; under contention it loses appended events. Fix it without changing its
;; contract: it still returns the balance its own deposit produced, even
;; under concurrent deposits.
;;
;; `withdraw!` is UNIMPLEMENTED. It returns the balance its own withdrawal
;; produced. It rejects an invalid amount as `deposit!` does
;; ({:type :invalid-amount}) and an amount above the balance with
;; (ex-info "insufficient funds" {:type :insufficient-funds}); a rejected
;; call leaves the log untouched. Concurrent calls must never take the
;; balance below 0. The tests cannot detect a violation, so this sentence
;; is the contract.
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

(defn open-account
  ([] (open-account 0))
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
