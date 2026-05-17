(ns katas.kata-09-bank-account)

;; ─── Kata 9: Bank account ──────────────────────────────
;;
;; Model an account as an atom holding
;;   {:balance <number>
;;    :history [{:op <op-keyword> :amount <number>} ...]}
;;
;; Implement:
;;
;;   (open-account)        ; balance 0,    history [{:op :open :amount 0}]
;;   (open-account 100)    ; balance 100,  history [{:op :open :amount 100}]
;;   (balance acct)        ; the current balance
;;   (deposit! acct n)     ; mutate, append {:op :deposit :amount n} to
;;                         ; history, return the NEW balance.
;;   (withdraw! acct n)    ; mutate, append {:op :withdraw :amount n} to
;;                         ; history, return the new balance.
;;   (history acct)        ; vector of history entries in order.
;;
;; History entries are maps, not tuples — the shape can grow new fields
;; (timestamps, memos, ids) later without breaking callers or tests.
;;
;; Errors (use `ex-info`):
;;   - amount < 0 or non-numeric         → {:type :invalid-amount}
;;   - withdraw of more than balance     → {:type :insufficient-funds}
;;
;; Concurrency: deposits and withdrawals must not lose updates if called
;; from multiple threads. `swap!` already handles this — but only if you
;; do the read+update atomically inside its function, NOT by `deref`-ing
;; first and then `reset!`-ing.

(defn open-account
  ([] (open-account 0))
  ([opening]
   ;; TODO
   ))

(defn balance [acct]
  ;; TODO
  )

(defn deposit! [acct amount]
  ;; TODO
  )

(defn withdraw! [acct amount]
  ;; TODO
  )

(defn history [acct]
  ;; TODO
  )
