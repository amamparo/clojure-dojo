# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

A personal practice space for Clojure katas. [STYLEGUIDE.md](STYLEGUIDE.md) is
the style you must follow when writing or modifying Clojure (a compressed
digest of the community guide). [GUIDE.md](GUIDE.md) is the kata-aligned
learning narrative for Java/Python engineers — its section order tracks the
kata numbering. [README.md](README.md) is the human quickstart; the Commands section
below is the authoritative reference for Claude.

## Commands

Use the `just` recipes (run `just` alone to list them):

```sh
just test            # run ALL kata tests (Kaocha)
just test 9          # run kata 9's tests only
just test 9 history-is-ordered   # run a single deftest in kata 9
just fmt             # cljfmt: format src + test in place
just fmt-check       # cljfmt: check only, no writes
just lint            # clj-kondo over src test
just repl            # REPL with test deps (clj -A:test)
just review 9        # one-shot `claude -p` review of kata 9's solution
```

`just test N` derives the namespace from the file: zero-padded number +
slug with `_`→`-`. `just review N` globs the file by zero-padded number.
Both require the source and test slugs to match exactly.

## Test runner

Tests run on **Kaocha**, configured in `tests.edn` (documentation reporter,
`:randomize? false` for determinism, stacktrace filtering down to ~5
non-framework frames). The alias split in `deps.edn` is deliberate:

- `:test` carries the test path + Kaocha dep but **no `:main-opts`** — so it
  is safe for an editor REPL jack-in (Calva). `clj -A:test` = REPL.
- `:kaocha` adds the runner `:main-opts`. Run tests with `clj -M:test:kaocha`
  (this is what `just test` invokes; `clj -M:test` alone does NOT run tests).
- Select a subset with Kaocha's `--focus <ns>` or `--focus <ns>/<deftest>`.

This is a learning repo: katas ship as stubs with `;; TODO` bodies, while the
tests are written against the intended solution. A freshly-cloned repo
therefore has **red tests by design** — that is not breakage. Claude's usual
role here is to *review* a solution the user wrote (`just review N`) or assist
when asked, not to pre-emptively fill in stubs.

## Kata file conventions

- One file per kata at `src/katas/kata_NN_<slug>.clj`, namespace
  `katas.kata-NN-<slug>`. Test at `test/katas/kata_NN_<slug>_test.clj`,
  namespace `katas.kata-NN-<slug>-test`. Tests `:refer [...]` the specific
  vars under test (or `:as` alias for wider surfaces — see the bank-account
  kata).
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
  Kata N` checkpoint in [GUIDE.md](GUIDE.md). The files are the source of
  truth — there is no kata list in the README.

## Resources directory

`deps.edn` puts `resources/` on the classpath. `.gitignore` excludes
`resources/inputs/day*.txt` (real AoC inputs the author asks not to share)
while keeping `*_sample.txt`. AoC-style katas with private inputs are wired
up but none exist yet.
