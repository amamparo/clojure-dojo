# clojure-dojo

A personal practice space for Clojure katas.

## Requirements

- [Clojure CLI](https://clojure.org/guides/install_clojure) (uses `deps.edn`)
- [just](https://github.com/casey/just) for the task runner (optional but convenient)
- [clj-kondo](https://github.com/clj-kondo/clj-kondo) for linting (optional)

## Common tasks

```sh
just              # list recipes
just test         # run all tests
just test-kata 1  # run a single kata's tests
just review 1     # have Claude review a kata's solution (requires `claude` CLI)
just repl         # REPL with test deps loaded
just fmt          # format with cljfmt
just fmt-check    # check formatting only
just lint         # clj-kondo
```

Or directly with the Clojure CLI:

```sh
clj -M:test                  # run all tests
clj -A:test                  # REPL with test deps
clj -T:cljfmt fix            # format
```

## Style

See [STYLEGUIDE.md](STYLEGUIDE.md) — a compressed digest of the [Community Clojure Style Guide](https://github.com/bbatsov/clojure-style-guide).
