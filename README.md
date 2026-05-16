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
