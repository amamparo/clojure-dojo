# Clojure Style Guide (Compressed)

> Adapted from the [Community Clojure Style Guide](https://github.com/bbatsov/clojure-style-guide) by Bozhidar Batsov and the editor team. Licensed under [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/deed.en_US). This file is a scannable digest; for full rationale, sidebars, and history see the upstream guide at <https://guide.clojure.style>.

---

## Layout

- **80-char lines.** Up to 100 (or 120 max) by team agreement.
- **Spaces, not tabs.**
- **2-space body indent** for `def*`, `let`, `loop`, `when`, `cond`, `case`, `with-*`, etc.
- **Align function args vertically** when they span multiple lines.
  ```clojure
  (filter even?
          (range 1 10))
  ```
- **One-space arg indent** when no arg is on the function-name line. (Lists are aligned the same as vectors and sets.)
  ```clojure
  (filter
   even?
   (range 1 10))
  ```
- **Align `let` bindings and map keys vertically.**
- **No spaces inside brackets**: `(foo (bar))`, not `( foo ( bar ) )`.
- **No commas in vectors/lists.** Commas in maps are optional and judicious.
- **Gather trailing parens on one line.** Exception: `(comment …)` blocks may keep the closer on its own line.
- **One blank line between top-level forms**; never inside a `defn` body. Group related `def`s without blank lines.
- Unix line endings, final newline, no trailing whitespace.
- **One file per namespace, one namespace per file.**

## Namespace Declaration

- **Avoid single-segment namespaces** and **> 5 segments**.
- Every file starts with a comprehensive `ns` form: `:refer-clojure` → `:require` → `:import`. One clause per line.
- **`:require :as` > `:require :refer` > `:require :refer :all`. Never `:use`.**
- **Sort requires/imports.**
- Prefer the **idiomatic alias**:

  | Namespace | Alias | Namespace | Alias |
  |---|---|---|---|
  | `clojure.string` | `str` | `clojure.spec.alpha` | `s` |
  | `clojure.set` | `set` | `clojure.pprint` | `pp` |
  | `clojure.java.io` | `io` | `clojure.java.shell` | `sh` |
  | `clojure.math` | `math` | `clojure.tools.logging` | `log` |
  | `clojure.walk` | `walk` | `clojure.core.async` | `async` |
  | `clojure.zip` | `zip` | `clojure.edn` | `edn` |
  | `clojure.datafy` | `datafy` | `clojure.tools.cli` | `cli` |
  | `clojure.data.csv` | `csv` | `clojure.data.xml` | `xml` |
  | `clojure.core.matrix` | `mat` | `clojure.core.protocols` | `p` |
  | `clojure.core.reducers` | `r` | | |

  Common community aliases: `cheshire.core`→`json`, `clj-yaml.core`→`yaml`, `clj-http.client`→`http`, `hugsql.core`→`sql`, `java-time`→`time`, `next.jdbc`→`jdbc`.
- **Custom alias recipe**: drop leading parts; keep enough trailing parts for uniqueness; eliminate redundant words like `core`/`clj`. Aliases may contain dots (`clj-time.format` → `time.format`). Be consistent project-wide.

## Naming

| Thing | Convention | Example |
|---|---|---|
| Namespace segment | `lisp-case` | `bruce.project-euler` |
| Function / var | `lisp-case` | `some-fn`, `max-size` |
| Protocol / record / type | `CapitalCase`, acronyms uppercase | `HttpRequest`, `XMLParser` |
| Predicate | trailing `?` | `palindrome?` |
| Side-effect / non-STM-safe | trailing `!` | `save-user!` |
| Conversion | `->`, not `to` | `f->c` |
| Dynamic var | `*earmuffs*` | `^:dynamic *a*` |
| Constant | normal name (no special notation) | `max-size` |
| Unused binding | `_` or `_name` | `(dotimes [_ 3] …)` |

**Idiomatic locals.** In functions: `f g h` (fns), `n` (size), `i` (index), `x y` (numbers), `xs` (seq), `m` (map), `k ks v vs` (keys/vals), `s` (string), `re` (regex), `sym` (symbol), `coll`, `pred`, `& more`, `xf` (transducer), `ns` (namespace). In macros: `expr`, `body`, `binding`. In protocol/record/`reify` methods: `this` first.

## Functions

- For `defn` without docstring, the arg vector may be on the same line as the name.
- For `defmethod`, put the `dispatch-val` on the same line as the name.
- Sort multi-arity from fewest → most args.
- **≤ 10 LOC per function** (5 ideal). **≤ 3–4 positional params**; otherwise pass a map.
- **Pre/post conditions** over manual checks in the body.

## Idioms

- **No `require`/`refer` outside the REPL** — use the `ns` form.
- **Avoid forward references**; use `declare` if truly needed.
- **Higher-order fns over `loop`/`recur`** when they fit.
- **No `def` inside a function**; use `let`. Don't shadow `clojure.core` names.
- **`alter-var-root`**, not `def`, to mutate a var.
- **Nil-pun with `seq`**: `(when (seq xs) …)`, not `(when-not (empty? xs) …)`.
- **`vec`** over `(into [] …)`. **`boolean`** for actual coercion.
- **`when`** for single-branch `if`. **`if-let` / `when-let`** over `let`+`if/when`. **`if-not`, `when-not`, `not=`** over `(not …)` wrappers.
- **`printf`** over `(print (format …))`.
- **Chained comparisons**: `(< 5 x 10)`, not `(and (> x 5) (< x 10))`.
- Function literals: `%` for one arg, `%1 %2 …` for many. Don't wrap a fn in `#(…)` if the fn alone works (`(filter even? xs)`, not `(filter #(even? %) xs)`). Don't use `#(…)` for multi-form bodies — use `fn`.
- **Anonymous fns over `complement`/`comp`/`partial`** for clarity. (`comp` is genuinely good in transducer chains.)
- **Threading macros (`->`, `->>`)** over deep nesting. Drop optional parens around 0-arg steps; align args vertically.
- **`cond` catch-all is `:else`**, not `true`. **`condp` > `cond`** when pred & expr don't change. **`case` > `condp`** when tests are compile-time constants.
- **Short clauses in `cond`.** If a clause must span lines, separate clauses with blank lines or `;` comments.
- **Sets as predicates**: `(remove #{1} xs)`, `(filter #{\a \e \i \o \u} s)`.
- **`(inc x)` / `(dec x)` / `(pos? x)` / `(neg? x)` / `(zero? x)`** over arithmetic shorthands and comparisons against 0.
- **`list*`** over nested `cons`.
- **Sugared Java interop:**
  - construct: `(ArrayList. 100)`
  - static method: `(Math/pow 2 10)`
  - instance method: `(.substring s 1 3)`
  - static field: `Integer/MAX_VALUE`
  - instance field: `(.someField o)`
- Compact metadata: `^:private`, not `^{:private true}`.
- Mark private with `defn-` or `^:private`. Access a private var (e.g. for testing) via `@#'some.ns/var`.
- Beware *what* metadata attaches to: `(def ^:private a {})` tags the **var**; `(def a ^:private {})` tags the **value**.

## Data Structures

- **Vectors over lists** for generic data.
- **Keywords for map keys.**
- **Literal collection syntax** (`[1 2 3]`, `#{1 2 3}`). Caveat: literal `#{(f) (g)}` throws at runtime if the values are equal — use `hash-set` when values are computed.
- **Avoid index access**; destructure: `(let [[x y] point] …)`.
- **Keyword as fn for lookup**: `(:name m)`. Avoid `(get m :name)` (verbose) and `(m :name)` (NPE on nil).
- Collections are fns of their elements: `(filter #{\a \e} s)`. Keywords are fns of collections: `((juxt :a :b) m)`.
- **Avoid transients** outside hot paths; **avoid Java collections and arrays** outside interop or primitive performance.

## Types & Records

- Construct with `(->Foo a b)` or `(map->Foo {…})`, not `(Foo. a b)`. (`map->` is records only.)
- Add a custom constructor (e.g. `make-foo`) for validation, but **don't shadow** `->Foo`.

## Mutation

**Refs**

- Wrap I/O in `io!` so it fails loudly inside a transaction.
- Avoid `ref-set` — prefer `alter` / `commute`.
- Keep transactions small; no I/O inside `dosync`.
- Don't mix short and long transactions on the same ref (starvation).

**Agents**

- `send` for CPU-bound work (fixed pool); `send-off` for blocking/I/O work (unbounded pool).

**Atoms**

- Don't `swap!` inside `dosync` — atom updates run on every retry.
- Prefer `swap!` over `reset!`.

## Math & Strings

- **`clojure.math/*`** over `Math/*` (Clojure 1.11+).
- **`clojure.string/*`** over `.toUpperCase`, `.indexOf`, etc.

## Exceptions

- Reuse standard JVM types (`IllegalArgumentException`, `IllegalStateException`, `IOException`); use `ex-info` for data-carrying errors. Don't define custom exception types.
- **`with-open` over `try`/`finally` close.**
- **Don't `catch Throwable`.** Catch specific Errors only when truly necessary.

## Macros

- **Don't write a macro if a function will do.**
- **Sketch the call site first**, then write the macro.
- **Break complex macros into helper functions** (which can be tested independently).
- **A macro should be sugar over a real function**; keep logic in the function.
- **Use syntax-quote (`` ` ``) and unquote (`~`, `~@`)**, not manual `list`/`cons`.

## Common Metadata

| Key | Use |
|---|---|
| `:added "0.5"` | Version a public API was introduced. |
| `:changed "0.6"` | Version semantics changed (sparingly). |
| `:deprecated "0.5"` | Deprecation marker — pair with `:superseded-by "newer-fn"`. Don't put the deprecation note in the docstring. |
| `:see-also ["other-fn"]` | Explicit cross-references. |
| `:no-doc true` | Hide from generated API docs (Codox/cljdoc). |
| `:style/indent N` | CIDER indentation hint for custom macros. |

SemVer tip: omit the patch — `"0.5"`, not `"0.5.0"`.

## Comments

| Form | Use |
|---|---|
| `;;;;` | Section heading |
| `;;;` | Top-level commentary outside any form |
| `;;` | Line comment **above** the code, indented to it |
| `;` | Margin (end-of-line) comment |
| `#_form` | Comment out one form (preferred over `;;` for that) |

- One space after the semicolons.
- Capitalize and punctuate full sentences.
- No superfluous comments (`(inc counter) ; increments counter`).
- Keep comments up to date — stale ones are worse than none.
- Refactor instead of explaining bad code.

**Annotations** sit on the line *above* the code, in the form `KEYWORD: note (initials YY-MM-DD)`. Indent continuation lines under the first character of the note.

| | |
|---|---|
| `TODO` | Missing feature/functionality. |
| `FIXME` | Broken code that needs fixing. |
| `OPTIMIZE` | Slow or inefficient code. |
| `HACK` | Smelly code; refactor away. |
| `REVIEW` | Confirm intended behavior. |

EOL annotations with no note (`; OPTIMIZE`) are the exception, not the rule. Custom keywords are fine if documented in the project README.

## Documentation

- **Use docstrings**, not `:doc` metadata, where possible.
- **First line is a complete, capitalized sentence summary** — tooling shows just this.
- **Markdown is supported** (cljdoc): tables, code fences, links.
- Wrap **arg names in backticks** in the docstring; wrap **var references in backticks**, optionally with `[[other-fn]]` for link targets.
- Well-formed English; one space between sentences. Indent continuation lines by 2 spaces. No leading or trailing whitespace.
- **Place the docstring after the function name**, not after the arg vector.
- Exception: `defprotocol` method docstrings come *after* the arg vector.

## Testing

- Tests live in `test/yourproject/`, in `yourproject.foo-test` namespaces, files named `foo_test.clj`.
- `deftest` names end in `-test`.
- **Group with `testing`** for context on failure. Add a message to `is` when the failure isn't self-explanatory.
- **`are`** for tabular tests over repeated `is` forms.
- One concept per `deftest`; sub-cases via `testing`.
- **`with-redefs` only at external boundaries** (HTTP, DB, clock); prefer dependency injection.
- **`thrown?` / `thrown-with-msg?`** to assert exceptions.
- For maps with dynamic fields: [`match?`](https://github.com/nubank/matcher-combinators) for structural assertions; `thrown-match?` for `ex-info` data.

## Library Organization

- Follow [Maven Central naming](https://central.sonatype.org/pages/choosing-your-coordinates.html) for `groupId`/`artifactId` (e.g. `com.stuartsierra/component`). Org-as-groupId (`cider/cider-nrepl`) is also common.
- **Minimize dependencies.** Three lines copied beats hundreds of vars pulled in.
- **Ship core and tooling integrations as separate artifacts**, so consumers aren't forced into your build choices.

## Existential

- Be functional; mutate only when it pays.
- Be consistent — with this guide, with the project, with the function.
- Use common sense; know when to break a rule.

## Tools

- **Linters:** [kibit](https://github.com/jonase/kibit), [clj-kondo](https://github.com/clj-kondo/clj-kondo).
- **Formatters:** [cljfmt](https://github.com/weavejester/cljfmt), [cljstyle](https://github.com/greglook/cljstyle), [zprint](https://github.com/kkinnear/zprint).
- Run them in CI to keep the project consistent.
- Emacs `clojure-mode` matches this guide out of the box; other editors may need configuration.
