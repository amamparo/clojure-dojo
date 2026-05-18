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

The dojo is intentionally offline / self-contained: SENSEI.md and the test
suites are the only references a learner needs. Don't send the user to
clojuredocs or the web.

> **`just`-driven, no prescribed IDE.** Tasks run through a `justfile`:
> `just test` (eftest — `## Running tests`), `just lint` (clj-kondo —
> `## Linting`), `just format` / `just fix` (cljfmt check / rewrite —
> `## Formatting`), `just check` (format + test, fail-fast), and
> `just repl` (a plain `clojure -M:test` REPL for experimentation).
> Backing aliases are in `deps.edn`: `:test`, `:cljfmt`, `:clj-kondo`.
> The old kata-runner REPL *loop* (`dev/user.clj`, `(run k)`, the
> `clj -A:test` entrypoint, `clj -M:test:kaocha`) is gone — do not
> resurrect that mechanism; `just repl` is a bare REPL, not the loop.
> No editor or IDE is prescribed — never tell the user to use a
> specific one.

## Running tests

Tests are plain `clojure.test` (`deftest`/`is`/`testing`), run through
the `justfile`:

```sh
just test                       # every test in the repo
just test 9                     # every test for kata 9
just test 9 history-is-ordered  # one test by name, in kata 9
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
(what `just test` runs) resolves; `:cljfmt` is the `-T` formatter tool
alias (`## Formatting`); `:clj-kondo` runs the linter via
`clojure -M:clj-kondo` (`## Linting`). All are test/tooling only, never
used to solve katas.

## Formatting

`cljfmt` is wired as the `:cljfmt` `-T` tool alias, scoped to `src test`:
`clojure -T:cljfmt check` reports formatting drift (`just format`);
`clojure -T:cljfmt fix` rewrites files in place (`just fix`). Config
lives in `cljfmt.edn` (auto-discovered): cljfmt's defaults plus
`:remove-consecutive-blank-lines?` — the rule this was adopted for,
collapsing runs of blank lines to one — and an `:extra-indents` entry
teaching it the custom `when-let*` macro (kata 14).

Use `:extra-indents`, never `:indents`, for custom macros: a top-level
`:indents` key *replaces* cljfmt's built-in indent rules (it then mangles
every `defn`/`let`), whereas `:extra-indents` merges onto them. Any future
let-style binding macro a kata introduces needs its own `:extra-indents`
entry or `fix` will misalign its body. At adoption the whole tree already
passed `check`.

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

Katas ship as stubs with `;; TODO` bodies, while the tests are written
against the intended solution. A freshly-cloned repo therefore has **red
tests by design** — that is not breakage. clj-kondo is likewise red on a
fresh clone (unused params/requires in the stubs, plus an unresolved
symbol at every `when-let*` call); `.clj-kondo/config.edn` turns
`:unused-binding`/`:unused-namespace` off for the `katas.*` ns-group and
`:lint-as`-es `when-let*` to `clojure.core/let`, so a clean clone lints
clean — don't revert that thinking it's a bug. Claude's role here is to
*review* a solution the user wrote, or assist when asked — not to
pre-emptively fill in stubs.

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