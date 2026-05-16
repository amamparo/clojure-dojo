# clojure-dojo

Clojure, by hand.

## The guide

[SENSEI.md](SENSEI.md). Thirteen katas, in order. No clock. No streak.

Written for engineers who think in Python or Java and want, for a while, to
think in something else. Read a little. Stop. Write.

## The katas

Each kata is a stub with a failing test. The red is the question; you write
the answer. The test going green is a quiet yes — nothing to chase.

Nothing here needs the internet. The REPL is the manual (`doc`, `source`,
`apropos`); the tests are the answer.

## Requirements

- [Clojure CLI](https://clojure.org/guides/install_clojure) (uses `deps.edn`)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) with the
  [Cursive](https://cursive-ide.com/) plugin — the dev + test environment

## Setup

Open the project in IntelliJ, then right-click `deps.edn` →
**Add as deps.edn project** and enable the **`:test`** alias in the Clojure
Deps tool window. Without this the module classpath is empty and test runs
fail with `ClassNotFoundException: clojure.main`.

## Common tasks

Inside Cursive:

- **Run all tests:** Run tests in project (or run a `…_test` namespace).
- **Run one kata:** run its `…_test` namespace.
- **Run a single test:** put the caret in the `deftest` → Run test under caret.
- **REPL:** jack in via Cursive, or from a terminal:

```sh
clj -A:test     # REPL with the test path on the classpath
```

## Style

Follow the [Community Clojure Style Guide](https://github.com/bbatsov/clojure-style-guide).