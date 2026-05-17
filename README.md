# clojure-dojo

Clojure katas with a guide, for a programmer new to Clojure — and to
functional programming itself. The katas are `;; TODO` stubs in
`src/katas`; their tests are already written, and red until you make
them pass.

## The discipline

You solve the katas yourself. The REPL is your reference —
`(doc f)`, `(source f)`, `(apropos "...")` — and
[SENSEI.md](SENSEI.md) is the only text you need. No Google, no
clojuredocs, no LLM tab-completing your way to green. The point is to
finish with the language in your fingers, not in your bookmarks.

## Setup

You need:

- a JDK — [Adoptium Temurin install guide](https://adoptium.net/installation/)
- the [Clojure CLI](https://clojure.org/guides/install_clojure)

With those installed, start a REPL from the project directory:

```sh
clj -A:test
```

From there the REPL says what to run, and SENSEI says where to go next.

## Katas

[SENSEI.md](SENSEI.md) is the authoritative learning narrative; the
list below is for orientation.

1. [**FizzBuzz**](src/katas/kata_01_fizzbuzz.clj) — first refactor target; solve naively, then again with threading.
2. [**Word frequencies**](src/katas/kata_02_word_frequencies.clj) — `frequencies`, `into`, basic map manipulation.
3. [**Anagrams**](src/katas/kata_03_anagrams.clj) — `group-by` on a canonical-form key.
4. [**Run-length encoding**](src/katas/kata_04_run_length_encoding.clj) — `partition-by`.
5. [**Primes**](src/katas/kata_05_primes.clj) — `lazy-seq`, infinite sequences.
6. [**Game of Life**](src/katas/kata_06_game_of_life.clj) — sets as data and as predicates.
7. [**Roman numerals**](src/katas/kata_07_roman_numerals.clj) — `loop`/`recur`.
8. [**Bowling**](src/katas/kata_08_bowling.clj) — state machine with variable lookahead.
9. [**Bank account**](src/katas/kata_09_bank_account.clj) — atoms, `swap!`, `ex-info` for structured errors.
10. [**Assignments**](src/katas/kata_10_assignments.clj) — synthesis of atoms + maps + errors.
11. [**RPN**](src/katas/kata_11_rpn.clj) — maps as dispatch tables.
12. [**Shapes**](src/katas/kata_12_shapes.clj) — protocols, records, and multimethods.
13. [**when-let***](src/katas/kata_13_when_let_star.clj) — macros: controlled evaluation and binding introduction.
14. [**Tiny interpreter**](src/katas/kata_14_interpreter.clj) — capstone; source-as-data, closures, environments.

## License

[MIT](LICENSE).
