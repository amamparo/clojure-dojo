# clojure-dojo task runner. Requires `just` and the Clojure CLI.

# Run tests with eftest. Examples:
#   just test                  every test in the repo
#   just test 1                every test for kata 1
#   just test 1 some-test      the test `some-test` in kata 1
test *args:
    clojure -M:test -m runner {{ args }}

# Lint src and test with clj-kondo.
lint:
    clojure -M:clj-kondo --lint src test

# Check formatting without changing files (zprint; config: .zprint.edn).
format:
    clojure -M:zprint -m zprint.main --url-only "file://{{ justfile_directory() }}/.zprint.edn" -sc $(find src test -name '*.clj')

# Reformat src and test in place (zprint; config: .zprint.edn).
fix:
    clojure -M:zprint -m zprint.main --url-only "file://{{ justfile_directory() }}/.zprint.edn" -sw $(find src test -name '*.clj')

# CI-style gate: formatting check, then the full test suite (fail-fast).
check: format test
