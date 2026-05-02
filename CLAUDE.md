# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

See [README.md](README.md) for the command list and [STYLEGUIDE.md](STYLEGUIDE.md) for Clojure style. The notes below are conventions Claude should preserve when generating or modifying code.

## Kata file conventions

- One file per kata at `src/katas/kata_NN_<slug>.clj`, namespace `katas.kata-NN-<slug>`. Test at `test/katas/kata_NN_<slug>_test.clj`, namespace `katas.kata-NN-<slug>-test`. Tests `:refer [...]` the specific vars under test (or `:as` alias for wider surfaces — see the bank-account kata).
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

- Kata numbers encode a deliberate complexity / learning progression. When inserting a new kata at position K, renumber everything ≥ K — move the file, update the `ns` form, the `;; ─── Kata N:` header, and the matching test's `ns` + `:require`. The files are the source of truth; there is no kata list in [README.md](README.md).
- `just test-kata N` and `just review N` derive the kata namespace from the filename (`printf '%02d'` of N + the slug). Slugs must match between source and test files.

## Resources directory

`deps.edn` puts `resources/` on the classpath; `.gitignore` excludes `resources/inputs/day*.txt` while keeping `*_sample.txt`. AoC-style katas with private inputs are wired up but none exist yet.
