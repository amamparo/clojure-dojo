# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

A personal practice space for Clojure katas. [DENSHO.md](DENSHO.md) is the
kata-aligned learning narrative — its sections (§0–§25) are a
concept progression, each kata gated by a
`> ## 🥋 Complete kata N ("…") before continuing` checkpoint placed
exactly where the prose above it has equipped the reader for that kata.
[README.md](README.md) is the human quickstart; this file is authoritative
for Claude.

When writing or reviewing Clojure here, follow the [Community Clojure Style
Guide](https://github.com/bbatsov/clojure-style-guide); there is no vendored
style file in this repo.

The dojo is intentionally offline / self-contained: DENSHO.md and the test
suites are the only references a learner needs. Don't send the user to
clojuredocs or the web.

> **`just`-driven, no prescribed IDE.** Tasks run through a `justfile`:
> `just test` (eftest — `## Running tests`), `just lint` (clj-kondo —
> `## Linting`), `just format` / `just fix` (zprint check / rewrite —
> `## Formatting`), and `just check` (format + test, fail-fast).
> Backing aliases are in `deps.edn`: `:test`, `:zprint`, `:clj-kondo`.
> The old REPL loop (`clj -A:test`, `dev/user.clj`, `(run k)`) and
> `clj -M:test:kaocha` are gone — do not resurrect them; there is no
> `just repl`. No editor or IDE is prescribed — never tell the user
> to use a specific one.

## Running tests

Tests are plain `clojure.test` (`deftest`/`is`/`testing`), run through
the `justfile`:

```sh
just test                        # every test in the repo
just test 13                     # every test for kata 13
just test 13 history-is-ordered  # one test by name, in kata 13
```

`just test` shells out to `clojure -M:test -m runner`. `test/runner.clj`
(namespace `runner`) is a small **eftest** entrypoint: `find-tests
"test"`, filtered by the zero-padded `kata-NN-` namespace tag and
optionally a `deftest` name, run with eftest's pretty (colored)
reporter. It exits non-zero if anything fails, so `just test` fails the
recipe / CI.

If `clojure` reports an empty classpath or a missing namespace, the
`deps.edn` project wasn't picked up; running `clojure -M:test -m runner`
from the project root is the sanity check. Clojure 1.12 is happiest on
JDK 21; on much newer JDKs you may hit reflective-access or library
oddities — try a 21 LTS if so.

## deps.edn

Minimal by design: `:paths ["src" "resources"]`. Clojure 1.12.5, three
aliases: `:test` (`:extra-paths ["test"]`) adds **eftest** and
**matcher-combinators** and puts the test sources — including
`test/runner.clj` — on the classpath, so `clojure -M:test -m runner`
(what `just test` runs) resolves; `:zprint` (just the **zprint** dep)
is the formatter, run via stock `clojure -M:zprint -m zprint.main`
(`## Formatting`); `:clj-kondo` runs the linter via
`clojure -M:clj-kondo` (`## Linting`). All are test/tooling only, never
used to solve katas.

## Formatting

`zprint` is the formatter: the `:zprint` alias is just the zprint dep,
driven through zprint's own CLI. `just format` runs `clojure -M:zprint
-m zprint.main --url-only file://…/.zprint.edn -sc $(find src test
-name '*.clj')` (summary check, non-zero exit on drift); `just fix` is
the same with `-sw` (writes in place). Scope is `src test`.

The single source of formatting truth is **`.zprint.edn`** at the repo
root. zprint.main does not auto-discover a project config (only
`$HOME/.zprintrc`), so the `justfile` loads it explicitly via
`--url-only` plus an absolute `file://` URL built from
`justfile_directory()`. `--url-only` (not `--url`) means a personal
`~/.zprintrc` is ignored — formatting is deterministic across machines
and CI. No `cljfmt.edn`, no runner namespace:

- `:style :community` — tracks the bbatsov Community Style Guide.
- `:width 80` — hard line limit; zprint reflows to fit.
- `:parse {:interpose "\n\n"}` — forces exactly one blank line between
  top-level forms (inserts where missing, collapses extras). Replaces
  the old `:remove-consecutive-blank-lines?` *and* adds separation
  cljfmt could not.
- `:fn-map` — `defn`/`defn-`/`defmacro` → `:arg2`, keeping the
  argument vector on the name line (the conventional Clojure/bbatsov
  shape, what cljfmt produced); `when-let*` → `:binding` so the
  let-style binding macro defined in the macros kata
  (`kata_16_macros`, `katas.kata-16-macros/when-let*`) indents like
  `let`. The exact `.zprint.edn` entry is `"when-let*" :binding`
  (keyed by the bare symbol name, so it applies wherever `when-let*`
  is defined or called). A future let-style binding macro needs its
  own `:binding` entry or `fix` misaligns its body.

zprint is idempotent here (`fix` then `check` is clean; the whole tree
passes `check`). With the `:arg2` mapping the shape matches the
established Clojure/bbatsov style — arglist on the name line, body
indented — and zprint only reflows lines past `:width`. The one
residual quirk vs. cljfmt: an empty `;; TODO` stub closes with its
final `)` at column 0 (see `## Kata file conventions`) — intended,
not a bug to "fix".

## Linting

`clj-kondo` is the `:clj-kondo` alias (`:main-opts ["-m"
"clj-kondo.main"]`, pinned `2026.04.15`). `just lint` runs
`clojure -M:clj-kondo --lint src test`, reading `.clj-kondo/config.edn`
(which also backs editor analysis). A fresh clone must lint **clean**:
"Learning-repo model" explains why the kata stub noise is configured
out, so a dirty `just lint` is a real finding, not stub noise.
`just check` gates formatting and tests only — run `just lint`
separately.

## Learning-repo model

Most katas ship as stubs with `;; TODO` bodies; a couple ship
working-but-flawed code (see "Kata file conventions" — the
extend-code variant). Either way the tests are written against the
intended solution, so a freshly-cloned repo has **red tests by
design** — that is not breakage. clj-kondo is likewise red on a fresh
clone (unused params/requires in the stubs, plus unresolved/
mis-parsed symbols at every `when-let*` and `infix` use in the macros
kata). `.clj-kondo/config.edn` turns `:unused-binding`/
`:unused-namespace` off for the `katas.*` ns-group and `:lint-as`-es
the two macros kata vars:

```clojure
:lint-as {katas.kata-16-macros/when-let* clojure.core/let
          katas.kata-16-macros/infix     clojure.core/quote}
```

`when-let*` is linted as `clojure.core/let` (it is a `let`-style
binding macro); `infix` is linted as `clojure.core/quote` because it
takes a *parenthesised arithmetic form as data* (e.g. `(1 + 2)`) — so
without this kondo would read `1` in call position as "a number is not
a function". (There is no separate `when-let*` kata in the final
layout — both macros live in `kata_16_macros`,
`katas.kata-16-macros`.) With this config a clean clone lints clean —
don't revert it thinking it's a bug. Claude's role here is to *review*
a solution the user wrote, or assist when asked — not to pre-emptively
fill in stubs or "fix" the inherited code in the extend-code katas.

## Kata file conventions

- The final layout is **17 katas**, zero-padded, in this order:
  `kata_01_fizzbuzz`, `kata_02_temperature`,
  `kata_03_word_frequencies`, `kata_04_roster`, `kata_05_anagrams`,
  `kata_06_run_length_encoding`, `kata_07_inventory`,
  `kata_08_primes`, `kata_09_digits`, `kata_10_roman_numerals`,
  `kata_11_bowling`, `kata_12_rpn`, `kata_13_bank_account`,
  `kata_14_memoize`, `kata_15_shapes`, `kata_16_macros`,
  `kata_17_interpreter`. README's "## Katas" list mirrors this for
  orientation only; DENSHO.md (§0–§25) is authoritative for order and
  for where each kata is attempted.
- One file per kata at `src/katas/kata_NN_<slug>.clj`, namespace
  `katas.kata-NN-<slug>`. Test at `test/katas/kata_NN_<slug>_test.clj`,
  namespace `katas.kata-NN-<slug>-test`. Tests `:refer [...]` the specific
  vars under test (or `:as` alias for wider surfaces — see the bank-account
  kata, `kata_13`).
- The **default** kata shape is a `;; TODO` stub — one or more `defn`
  bodies left unimplemented, tests written against the intended
  solution. A stub follows this fixed shape; `just fix` (zprint)
  enforces it exactly, so don't hand-fight it:

  ```clojure
  (ns katas.kata-NN-slug)

  ;; ─── Kata N: Title ────────────────────────────────────────────────────
  ;;
  ;; <prose description with example invocations and expected results>

  (defn name [args]
    ;; TODO
  )
  ```

- **Sanctioned variant — extend / fix existing code.** A kata may
  instead ship a small, *working but deliberately incomplete-or-buggy*
  namespace: real definitions to read, one (or more) carrying a defect
  the tests expose and/or one left as a `;; TODO` to implement. This is
  an intentional deviation from the stub-only convention above — it
  directly serves the "contribute to an existing codebase" goal (read
  an unfamiliar namespace, fix it in its existing style, leave correct
  code alone). In the final layout exactly **two** katas are this
  variant: **`kata_07_inventory`** (one boundary-condition bug in
  `low-stock`, plus `restock-report` unimplemented) and
  **`kata_13_bank_account`** (a `deref`/compute/`reset!` concurrency
  bug in `deposit!`, plus `withdraw!` unimplemented). DENSHO §15 and
  §21 set these up and the kata file headers state precisely which
  functions are correct vs. to-fix. Such a file still passes `just
  fix`/`just lint` clean on a fresh clone and its tests are still red
  by design until fixed. Don't pre-emptively fix it; Claude's job is
  to review the user's fix or assist on request.
- Kata numbers encode a deliberate complexity / learning progression and are
  referenced externally. When inserting a new kata at position K, renumber
  everything ≥ K. For each moved kata that means: move the source file and
  update its `ns` form and `;; ─── Kata N:` header; move the test file and
  update its `ns` + `:require`; and update the matching
  `> ## 🥋 Complete kata N ("…") before continuing` checkpoint in
  [DENSHO.md](DENSHO.md) (and DENSHO's body text where it names the
  kata). The files are the source of truth — README's list is
  orientation only.

## Resources directory

`deps.edn` puts `resources/` on the classpath. `.gitignore` excludes
`resources/inputs/day*.txt` (real AoC inputs the author asks not to share)
while keeping `*_sample.txt`. AoC-style katas with private inputs are wired
up but none exist yet.