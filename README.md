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
list below is for orientation only.

1. [FizzBuzz](src/katas/kata_01_fizzbuzz.clj)
2. [Word frequencies](src/katas/kata_02_word_frequencies.clj)
3. [Anagrams](src/katas/kata_03_anagrams.clj)
4. [Run-length encoding](src/katas/kata_04_run_length_encoding.clj)
5. [Primes](src/katas/kata_05_primes.clj)
6. [Game of Life](src/katas/kata_06_game_of_life.clj)
7. [Roman numerals](src/katas/kata_07_roman_numerals.clj)
8. [Bowling](src/katas/kata_08_bowling.clj)
9. [Bank account](src/katas/kata_09_bank_account.clj)
10. [Assignments](src/katas/kata_10_assignments.clj)
11. [my-memoize](src/katas/kata_11_memoize.clj)
12. [RPN](src/katas/kata_12_rpn.clj)
13. [Shapes](src/katas/kata_13_shapes.clj)
14. [when-let\*](src/katas/kata_14_when_let_star.clj)
15. [Tiny interpreter](src/katas/kata_15_interpreter.clj)

## License

[MIT](LICENSE).
