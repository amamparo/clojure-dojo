# List recipes when invoked with no arguments
default:
    @just --list

# Run tests: `just test` (all), `just test 1` (kata 1), `just test 1 empty-input` (one test)
test N='' TEST='':
    #!/usr/bin/env bash
    set -euo pipefail
    if [ -z "{{N}}" ]; then
        clj -M:test:kaocha
    else
        slug=$(ls src/katas | sed -n "s/^kata_$(printf '%02d' {{N}})_\(.*\)\.clj$/\1/p" | tr '_' '-')
        ns="katas.kata-$(printf '%02d' {{N}})-${slug}-test"
        if [ -z "{{TEST}}" ]; then
            clj -M:test:kaocha --focus "$ns"
        else
            clj -M:test:kaocha --focus "$ns/{{TEST}}"
        fi
    fi

# Start a REPL with test deps loaded
repl:
    clj -A:test

# Review kata N's solution with Claude, e.g. `just review 1` (prints to stdout)
review N:
    @src=$(ls src/katas/kata_$(printf '%02d' {{N}})_*.clj 2>/dev/null | head -1); \
    test=$(ls test/katas/kata_$(printf '%02d' {{N}})_*_test.clj 2>/dev/null | head -1); \
    if [ -z "$src" ]; then echo "no kata {{N}} found"; exit 1; fi; \
    command -v claude >/dev/null 2>&1 || { echo "claude CLI not on PATH"; exit 1; }; \
    claude -p "Review my Clojure solution in $src. The kata prompt is in the file's header comment; tests live at $test. Style guide: STYLEGUIDE.md. Call out idiomatic-style issues, correctness bugs, and edge cases the tests don't cover. Be specific and concise. This is a one-shot non-interactive review — do not end with a follow-up question or offer further help." --allowed-tools "Read Grep Glob Bash"

# Check formatting (does not modify files)
fmt-check:
    clj -T:cljfmt check

# Format all source and test files in place
fmt:
    clj -T:cljfmt fix

# Lint with clj-kondo (install: brew install borkdude/brew/clj-kondo)
lint:
    @command -v clj-kondo >/dev/null 2>&1 || { echo "clj-kondo not installed. install with: brew install borkdude/brew/clj-kondo"; exit 1; }
    clj-kondo --lint src test
