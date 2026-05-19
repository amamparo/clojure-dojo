# clojure-dojo

A self-contained Clojure kata dojo for a programmer new to Clojure, and
to functional programming itself. [DENSHO.md](DENSHO.md) is the learning
narrative; the katas live in `src/katas`. Most are `;; TODO` stubs;
katas 7 and 13 instead hand you working-but-flawed code to read and fix.
Each kata's tests are already written against the intended solution, and
are red until you make them pass.

## The discipline

You solve the katas yourself. [DENSHO.md](DENSHO.md) is the only text
you need: no Google, no LLM. DENSHO and the tests are
the whole oracle. The point is to finish with the language in your
hands.

## What you need

- a JDK — [Adoptium](https://adoptium.net/installation/); the 21 LTS is
  the happy path (the dojo runs Clojure 1.12 on JDK 21)
- the [Clojure CLI](https://clojure.org/guides/install_clojure)
- [`just`](https://github.com/casey/just#installation) — tests, lint,
  and formatting are all `just` recipes

## Where to start

Open [DENSHO.md](DENSHO.md) at the top and work straight down; the order
is the curriculum. It gates each kata with a
`> ## 🥋 Complete kata N` break, placed exactly where the prose above
has equipped you for it. At each break, solve that kata in `src/katas`
and make its tests green:

```sh
just test                 # every test in the repo
just test 1               # every test for kata 1
just test 1 some-test     # one test by name, in kata 1
```

A fresh clone lints clean and is formatted; the tests are red until you
solve the katas.

## Tasks

| command                | what                                        |
|------------------------|---------------------------------------------|
| `just test [k [name]]` | run tests — all, kata `k`, or one named     |
| `just lint`            | clj-kondo over `src` and `test`             |
| `just format`          | check formatting (zprint; no changes)       |
| `just fix`             | reformat `src` and `test` in place (zprint) |
| `just check`           | `format` then `test` (fail-fast)            |

## Katas

[DENSHO.md](DENSHO.md) is authoritative for order and pacing; the list
below is orientation only. Katas 7 and 13 ship working-but-flawed code
to read, fix, and extend; the rest are blank stubs.

1. [FizzBuzz](src/katas/kata_01_fizzbuzz.clj)
2. [Temperature](src/katas/kata_02_temperature.clj)
3. [Word frequencies](src/katas/kata_03_word_frequencies.clj)
4. [Roster](src/katas/kata_04_roster.clj)
5. [Anagrams](src/katas/kata_05_anagrams.clj)
6. [Run-length encoding](src/katas/kata_06_run_length_encoding.clj)
7. [Inventory](src/katas/kata_07_inventory.clj) — extend existing code
8. [Primes](src/katas/kata_08_primes.clj)
9. [Digits](src/katas/kata_09_digits.clj)
10. [Roman numerals](src/katas/kata_10_roman_numerals.clj)
11. [Bowling](src/katas/kata_11_bowling.clj)
12. [RPN](src/katas/kata_12_rpn.clj)
13. [Bank account](src/katas/kata_13_bank_account.clj) — extend existing code
14. [my-memoize](src/katas/kata_14_memoize.clj)
15. [Shapes](src/katas/kata_15_shapes.clj)
16. [Macros](src/katas/kata_16_macros.clj)
17. [Tiny interpreter](src/katas/kata_17_interpreter.clj)

## License

[MIT](LICENSE).
