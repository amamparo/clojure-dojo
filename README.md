# clojure-dojo

A self-contained Clojure kata dojo for a programmer new to Clojure, and
to functional programming itself. [DENSHO.md](DENSHO.md) is the
curriculum; the katas live in `src/katas`. Most are `;; TODO` stubs;
katas 7 and 13 hand you working-but-flawed code to fix and extend, and
kata 15 a protocol/record/multimethod skeleton to complete. Each kata's
tests are already written against the intended solution; solving the
kata is making them green. You solve the katas yourself. No web, no
LLM: DENSHO, the tests and the REPL are the whole oracle.

## What you need

- a JDK — [Adoptium](https://adoptium.net/installation/); the 21 LTS is
  the happy path (the dojo runs Clojure 1.12 on JDK 21)
- the [Clojure CLI](https://clojure.org/guides/install_clojure)
- [`just`](https://github.com/casey/just#installation) — tests, lint,
  and formatting are all `just` recipes
- a POSIX `sh` with `find` on `PATH` — `just` runs every recipe with
  it; on Windows use Git Bash or WSL
- optional: [`rlwrap`](https://github.com/hanslub42/rlwrap) — line
  editing and history in `just repl`

## Where to start

Open [DENSHO.md](DENSHO.md) at the top and work straight down; the order
is the curriculum. It gates each kata with a
`> ## 🥋 Complete kata N` break, placed exactly where the prose above
has equipped you for it. At each break, solve that kata in `src/katas`
and make its tests green:

```sh
just test 1               # every test for kata 1
just test 1 empty-input   # one test by name, in kata 1
just test                 # every test in the repo
```

A fresh clone lints clean and is formatted; `just test` is red until you
solve the katas. A few tests start green: katas 7, 13 and 15 ship
partial code, and a stub that returns `nil` already satisfies some
assertions.

### When stuck

```sh
just repl                 # a REPL with src and test on the classpath
```

```clojure
;; load a kata; repeat after every edit
(require '[katas.kata-01-fizzbuzz :refer [fizzbuzz]] :reload)
(doc frequencies)         ; docstring and arglists
(find-doc "partition")    ; search every loaded docstring
```

Read the first FAIL or ERROR block: expected first, your result second;
DENSHO §6 shows how to read both, and §1 and §15 the REPL method. A run
or REPL that goes silent is stuck in an infinite seq or loop: Ctrl-C
ends it (DENSHO §16).

## Tasks

| command                | what                                                                                                |
|------------------------|-----------------------------------------------------------------------------------------------------|
| `just test [k [name]]` | run tests — all, kata `k`, or one named                                                             |
| `just repl`            | a REPL with `src` and `test` on the classpath                                                       |
| `just lint`            | clj-kondo over `src` and `test`                                                                     |
| `just format`          | check formatting (zprint; no changes); names each file that needs `just fix`                        |
| `just fix`             | reformat `src` and `test` in place (zprint)                                                         |
| `just check`           | `lint`, `format`, `test` in order; stops at the first failure; green only once every kata is solved |

## Katas

[DENSHO.md](DENSHO.md) is authoritative for order and pacing; the list
below is orientation only.

1. [FizzBuzz](src/katas/kata_01_fizzbuzz.clj)
2. [Temperature](src/katas/kata_02_temperature.clj)
3. [Word frequencies](src/katas/kata_03_word_frequencies.clj)
4. [Roster](src/katas/kata_04_roster.clj)
5. [Anagrams](src/katas/kata_05_anagrams.clj)
6. [Run-length encoding](src/katas/kata_06_run_length_encoding.clj)
7. [Inventory](src/katas/kata_07_inventory.clj) — fix and extend
8. [Primes](src/katas/kata_08_primes.clj)
9. [Digits](src/katas/kata_09_digits.clj)
10. [Roman numerals](src/katas/kata_10_roman_numerals.clj)
11. [Bowling](src/katas/kata_11_bowling.clj)
12. [RPN](src/katas/kata_12_rpn.clj)
13. [Bank account](src/katas/kata_13_bank_account.clj) — fix and extend
14. [my-memoize](src/katas/kata_14_memoize.clj)
15. [Shapes](src/katas/kata_15_shapes.clj) — complete the given skeleton
16. [Macros](src/katas/kata_16_macros.clj)
17. [Interpreter](src/katas/kata_17_interpreter.clj)

## License

[MIT](LICENSE).
