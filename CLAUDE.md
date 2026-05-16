# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

A personal practice space for Clojure katas. [SENSEI.md](SENSEI.md) is the
kata-aligned learning narrative for Java/Python engineers — its sections are a
concept progression, each kata gated by a `> By this point…` checkpoint.
[README.md](README.md) is the human quickstart; this file is authoritative
for Claude.

When writing or reviewing Clojure here, follow the [Community Clojure Style
Guide](https://github.com/bbatsov/clojure-style-guide); there is no vendored
style file in this repo.

> **No CLI scaffolding.** This repo deliberately has no `justfile`,
> `tests.edn`, CLI test runner, formatter, or linter — everything runs
> inside **Cursive (IntelliJ)**. Commands in git history or stale notes
> (`just test`, `clj -M:test:kaocha`, `clj -T:cljfmt fix`) no longer work;
> do not resurrect them.

## Running tests

Tests are plain `clojure.test` (`deftest`/`is`/`testing`) and run **inside
Cursive** — Cursive executes `clojure.test` directly; Kaocha is not involved.

- **All tests:** Cursive "Run tests in project" (or run a test namespace).
- **One namespace:** open the `_test.clj` file, run the namespace.
- **One deftest:** put the caret in the `deftest` and "Run test under caret"
  (e.g. `history-is-ordered` in `kata_09_bank_account_test.clj`).
- **REPL:** `clj -A:test` (the `:test` alias adds the `test` path; no
  `:main-opts`). `clj -M:test` does **not** run tests.

**Critical setup gotcha.** Cursive's runner builds its classpath from the
IntelliJ module, which is only populated if the project was imported as a
deps.edn project. If `.idea/clojure-dojo.iml` has no `Deps: …` library
`orderEntry` lines, the generated command degrades to
`java -cp test clojure.main …` and dies with
`ClassNotFoundException: clojure.main`. Fix: right-click `deps.edn` →
**"Add as deps.edn project"**, then enable the **`:test` alias** in the
Clojure Deps tool window so the `test` path and test deps resolve onto the
classpath. The project SDK is pinned to **JDK 26** in `.idea/misc.xml`;
Clojure 1.12 is best-tested on JDK ≤ 21, so prefer a 21 LTS for the project
SDK if you hit reflective-access or library oddities.

## deps.edn

Minimal by design: `:paths ["src" "resources"]`, Clojure 1.12, and a single
`:test` alias = just `:extra-paths ["test"]`, **deliberately with no
`:main-opts`** so the alias is safe to select for an editor REPL jack-in
(selecting an alias with `:main-opts` would hijack the REPL). Tests use only
`clojure.test` (core), so the alias needs no `:extra-deps`.

## Learning-repo model

Katas ship as stubs with `;; TODO` bodies, while the tests are written
against the intended solution. A freshly-cloned repo therefore has **red
tests by design** — that is not breakage. Claude's role here is to *review* a
solution the user wrote, or assist when asked — not to pre-emptively fill in
stubs.

## Kata file conventions

- One file per kata at `src/katas/kata_NN_<slug>.clj`, namespace
  `katas.kata-NN-<slug>`. Test at `test/katas/kata_NN_<slug>_test.clj`,
  namespace `katas.kata-NN-<slug>-test`. Tests `:refer [...]` the specific
  vars under test (or `:as` alias for wider surfaces — see the bank-account
  kata, `kata_09`).
- Each stub follows a fixed shape — preserve it when adding new katas:

  ```clojure
  (ns katas.kata-NN-slug)

  ;; ─── Kata N: Title ────────────────────────────────────────────────────
  ;;
  ;; You are learning: <comma-separated concepts/idioms>.
  ;;
  ;; <prose description with example invocations and expected results>

  (defn name [args]
    ;; TODO
    )
  ```

- Kata numbers encode a deliberate complexity / learning progression and are
  referenced externally. When inserting a new kata at position K, renumber
  everything ≥ K. For each moved kata that means: move the source file and
  update its `ns` form and `;; ─── Kata N:` header; move the test file and
  update its `ns` + `:require`; and update the matching `> By this point …
  Kata N` checkpoint in [SENSEI.md](SENSEI.md). The files are the source of
  truth — there is no kata list in the README.

## Resources directory

`deps.edn` puts `resources/` on the classpath. `.gitignore` excludes
`resources/inputs/day*.txt` (real AoC inputs the author asks not to share)
while keeping `*_sample.txt`. AoC-style katas with private inputs are wired
up but none exist yet.