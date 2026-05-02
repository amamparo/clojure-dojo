# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A personal Clojure katas practice repo. Each kata is a stub file: a namespace, a structured prompt comment, and `;; TODO`-bodied function declarations. The user fills in implementations; the matching test file (already written) verifies them.

## Commands

```sh
just                  # list recipes
just test             # run all tests
just test-kata N      # run one kata's tests, e.g. `just test-kata 4`
just repl             # REPL with test deps loaded
just fmt              # format src + test in place (cljfmt)
just fmt-check        # check formatting only
just lint             # clj-kondo (install via `brew install borkdude/brew/clj-kondo`)
```

Equivalents without `just`: `clj -M:test`, `clj -A:test`, `clj -T:cljfmt fix`, `clj-kondo --lint src test`.

`just test-kata N` derives the test namespace from the kata filename — it `printf '%02d'`s `N`, finds `src/katas/kata_NN_<slug>.clj`, and runs `katas.kata-NN-<slug>-test`. Renumbering or renaming a kata file changes what `test-kata N` resolves to.

## Kata file conventions

- One file per kata at `src/katas/kata_NN_<slug>.clj`, namespace `katas.kata-NN-<slug>`. Test lives at `test/katas/kata_NN_<slug>_test.clj`, namespace `katas.kata-NN-<slug>-test`.
- Tests `:refer [...]` specific vars from the kata namespace (or `:as` alias when the surface is wider, see kata 09 bank-account).
- Each kata stub follows a fixed shape — preserve it when adding new katas:
  ```clojure
  (ns katas.kata-NN-slug)

  ;; ─── Kata N: Title ────────────────────────────────────────────────────
  ;;
  ;; You are learning: <comma-separated concepts/idioms>.
  ;;
  ;; <prose description, with example invocations and expected results>

  (defn name [args]
    ;; TODO
    )
  ```
- The kata number ordering encodes a deliberate complexity / learning progression. When inserting a new kata at position K, renumber everything ≥ K (move the file, update the `ns` form, the `;; ─── Kata N:` header, and the matching test's `ns` + `:require`). The numbers in [README.md](README.md) — there is none — are not the source of truth; the files are.

## Style

Project style is the [Clojure Community Style Guide](https://github.com/bbatsov/clojure-style-guide), digested in [STYLEGUIDE.md](STYLEGUIDE.md). Defer to that document for layout, naming, idioms, and macro/protocol conventions.

## Resources directory

`deps.edn` puts `resources` on the classpath and `.gitignore` excludes `resources/inputs/day*.txt` while keeping `*_sample.txt` — wiring is in place for Advent of Code style katas with private inputs, even though no such katas exist yet.
