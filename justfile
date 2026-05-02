# List recipes when invoked with no arguments
default:
    @just --list

# Run all tests
test:
    clj -M:test

# Start a REPL with test deps loaded
repl:
    clj -A:test

# Run tests for a specific kata, e.g. `just test-kata 1`
test-kata N:
    clj -M:test -n katas.kata-$(printf '%02d' {{N}})-$(ls src/katas | sed -n "s/^kata_$(printf '%02d' {{N}})_\(.*\)\.clj$/\1/p" | tr '_' '-')-test

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
