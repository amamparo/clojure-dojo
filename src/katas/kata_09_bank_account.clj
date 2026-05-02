(ns katas.kata-09-bank-account)

;; ─── Kata 9: Bank account ──────────────────────────────────────────────
;;
;; You are learning: identity vs. value, atoms, `swap!`, `deref` (`@`),
;; throwing structured errors with `ex-info`, and writing code that is
;; safe under concurrent updates.
;;
;; Model an account as an atom holding
;;   {:balance <number>, :history [[<op> <amount>] ...]}
;;
;; Implement:
;;
;;   (open-account)        ; balance 0,    history [[:open 0]]
;;   (open-account 100)    ; balance 100,  history [[:open 100]]
;;   (balance acct)        ; the current balance
;;   (deposit! acct n)     ; mutate, append [:deposit n] to history,
;;                         ; return the NEW balance.
;;   (withdraw! acct n)    ; mutate, append [:withdraw n], return balance.
;;   (history acct)        ; vector of [op amount] entries in order.
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
