# clojure-dojo

A personal practice space for Clojure katas.

## The guide

[SENSEI.md](SENSEI.md) is a fast ramp into Clojure, ordered so each section
unlocks the next kata in this repo. Written assuming you're fluent in Python
and Java but new to functional programming — **read and follow it.**

It's meant to be skimmed top-to-bottom once, then revisited per-section as you
sit down with each kata. At each checkpoint, stop reading and go write code.

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

Tests are plain `clojure.test`. Katas ship as `;; TODO` stubs with the tests
written against the intended solution, so a fresh clone has **failing tests by
design** — solve the kata to make them green.

## Style

Follow the [Community Clojure Style Guide](https://github.com/bbatsov/clojure-style-guide).