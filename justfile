# clojure-dojo task runner. Requires `just` and the Clojure CLI.

# Run tests with eftest. Examples:
#   just test                  every test in the repo
#   just test 1                every test for kata 1
#   just test 1 some-test      the test `some-test` in kata 1
test *args:
    clojure -M:test -m runner {{args}}

# Lint src and test with clj-kondo.
lint:
    clojure -M:clj-kondo --lint src test

# Check formatting without changing files (cljfmt).
format:
    clojure -T:cljfmt check

# Reformat src and test in place (cljfmt).
fix:
    clojure -T:cljfmt fix

# CI-style gate: formatting check, then the full test suite (fail-fast).
check: format test
