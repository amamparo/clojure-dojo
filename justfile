# clojure-dojo task runner. Requires `just`, the Clojure CLI, and a POSIX
# sh: every recipe runs under it, and format and fix also use `find`.

zprint := 'clojure -M:zprint --url-only file:.zprint.edn'
clj_files := '$(find src test -name "*.clj")'

[private]
default:
    @just --list

# Examples:
#   just test                  every test in the repo
#   just test 1                every test for kata 1
#   just test 1 empty-input    the test `empty-input` in kata 1

# Run tests: all, one kata, or one test — just test [kata [test-name]]
test *args:
    clojure -M:test -m runner {{ args }}

# Start a REPL on src and test; line editing via rlwrap, if installed.
repl:
    @if [ -t 0 ] && command -v rlwrap >/dev/null 2>&1; \
    then exec clj -M:test; else exec clojure -M:test; fi

# Lint src and test with clj-kondo.
lint:
    clojure -M:clj-kondo --lint src test

# Check formatting without changing files; name each file that needs it.
format:
    {{ zprint }} -fsc {{ clj_files }}

# Reformat src and test in place; name each file changed.
fix:
    {{ zprint }} -fsw {{ clj_files }}

# Lint, format check, then every test; stops at the first failing step.
check: lint format test
