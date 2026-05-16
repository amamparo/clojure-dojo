# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

A personal practice space for Clojure katas. [SENSEI.md](SENSEI.md) is the
kata-aligned learning narrative — its sections are a
concept progression, each kata gated by a `> Enough for Kata N` checkpoint.
[README.md](README.md) is the human quickstart; this file is authoritative
for Claude.

When writing or reviewing Clojure here, follow the [Community Clojure Style
Guide](https://github.com/bbatsov/clojure-style-guide); there is no vendored
style file in this repo.

The dojo is intentionally offline / self-contained: SENSEI.md plus the REPL
(`doc`, `source`, `dir`, `apropos`, `find-doc`) and the test suites are the
only references a learner needs. Don't send the user to clojuredocs or the
web — point them at the REPL.

> **No CLI scaffolding, no prescribed IDE.** This repo deliberately has no
> `justfile`, `tests.edn`, CLI test runner, or formatter (just a minimal
> `.clj-kondo/config.edn` for editor analysis); the loop
> is REPL-driven — SENSEI §1 has the primer. Stale commands
> (`just test`, `clj -M:test:kaocha`, `clj -T:cljfmt fix`) no longer work;
> do not resurrect them. No editor or IDE is prescribed — never tell the
> user to use a specific one.

## Running tests

Tests are plain `clojure.test` (`deftest`/`is`/`testing`), run **from a
REPL** — there is no CLI test runner. Start a REPL with `clj -A:test`,
which auto-loads `dev/user.clj` (`clojure.test` as `t`, and `(run k)` by
kata number). `clj -M:test` does **not** run tests (`:test`
has no `:main-opts`). Then:

```clojure
(run 9)                                          ; whole kata
(run 9 "history-is-ordered")                     ; one test (string or symbol)
(t/run-tests 'katas.kata-09-bank-account-test)   ; what (run 9) wraps
```

`run-tests` is variadic over required test namespaces. The user's editor
integration runs the same `clojure.test` calls — whichever editor that is.

If a REPL or editor can't find `clojure.main` or shows an empty classpath,
the `deps.edn` project wasn't picked up; a plain terminal `clj -A:test`
always works with a correct Clojure CLI install and is the sanity check.
Clojure 1.12 is happiest on JDK 21; on much newer JDKs you may hit
reflective-access or library oddities — try a 21 LTS if so.

## deps.edn

Minimal by design: `:paths ["src" "resources" "dev"]` — `dev` is a **base**
path (not an alias) so `dev/user.clj`'s `user` namespace is an
unconditional source root for every editor; `clojure.main` auto-loads the
`user` namespace, so never rename it and never move `dev` into an alias.
Clojure 1.12.4, one alias `:test` (`:extra-paths ["test"]`, no `:main-opts`)
that adds **eftest** for colored test output — dev/test tooling only, never
used to solve katas. Start the REPL with `clj -A:test`.

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
  ;; <prose description with example invocations and expected results>

  (defn name [args]
    ;; TODO
    )
  ```

- Kata numbers encode a deliberate complexity / learning progression and are
  referenced externally. When inserting a new kata at position K, renumber
  everything ≥ K. For each moved kata that means: move the source file and
  update its `ns` form and `;; ─── Kata N:` header; move the test file and
  update its `ns` + `:require`; and update the matching `> Enough for
  Kata N` checkpoint in [SENSEI.md](SENSEI.md). The files are the source of
  truth — there is no kata list in the README.

## Resources directory

`deps.edn` puts `resources/` on the classpath. `.gitignore` excludes
`resources/inputs/day*.txt` (real AoC inputs the author asks not to share)
while keeping `*_sample.txt`. AoC-style katas with private inputs are wired
up but none exist yet.