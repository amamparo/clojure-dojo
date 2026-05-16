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

The prose in this repo was written with Claude as an editing partner.
The kata-solving discipline is to do it without one — use AI for what
isn't the lesson; do the lesson yourself.

## Setup

You need:

- a JDK — [Adoptium Temurin install guide](https://adoptium.net/installation/)
- the [Clojure CLI](https://clojure.org/guides/install_clojure)
- a willingness to type things into a REPL

With those installed, start from the project directory:

```sh
clj -A:test
```

You'll see a `Ready.` banner with example commands. Type one of those
to begin; [SENSEI.md](SENSEI.md) tells you where to go next.
