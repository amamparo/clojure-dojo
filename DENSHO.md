# DENSHO

Read top to bottom; the order is the curriculum. At each `> ## 🥋`
break you have been given everything the next kata needs — and nothing
it doesn't. Solve it before reading on. Tests are the oracle: `just
test N` runs kata N. Some katas hand you working code to read and
extend instead of a blank stub; the brief says which. `just fix`
rewrites your files to the layout `just format` checks; `just check`
runs lint, that check and every kata's tests, so it stays red until all
17 are solved. §N means section N of this document.

This document, the tests and the REPL (§1) are all you need. No web,
no clojuredocs, no LLM.



## 0. The mental model

Four ideas underlie the rest.

**Code is data.** Source is nested lists, vectors and maps; there is
no separate AST — the source *is* the tree. That is why the last kata,
an interpreter, needs no parser.

**Values are immutable.** A vector, map or set never changes once
built. `assoc`, `conj` and friends return new collections that share
structure with the old, so copying is cheap; these are the
*persistent* data structures — the old version stays valid.

**Functions are the abstraction.** Where other languages reach for a
class, an object or a loop, Clojure reaches for a function. State,
when genuinely needed, lives in explicit references you *dereference*
— read the current value out (§21) — and is the exception, not the
default.

**You are on a host.** Clojure runs on the JVM; a host call looks like
`(Math/sqrt n)` (§16). Interop is rarely needed here; the standard
library covers these katas.



## 1. Syntax

Everything is a prefix-form expression in parentheses (prefix = the
operator comes first, then its arguments):

```clojure
(+ 1 2)            ; => 3
(println "hello")  ; prints hello, returns nil
```

The first thing inside the parens is a function (or a *special form* —
a primitive the compiler handles directly, like `if` — or a macro).
The rest are arguments. No commas; whitespace separates. Commas, if you
write them, are whitespace — sometimes used in maps for readability.

Define a global with `def`, a function with `defn`. A docstring is a
string between the name and the argument vector:

```clojure
(def pi 3.14159)

(defn square
  "Multiplies x by itself."
  [x]
  (* x x))

(square 5)  ; => 25
```

Note `[x]` — argument lists are *vectors*, not parenthesised. One of
the few places the syntax distinguishes structure from invocation.

**Comments:**
- `;` end-of-line comment
- `;;` line comment on its own line (idiomatic for narrative comments;
  the kata stubs use `;; TODO`)
- `#_form` skip the very next form entirely
- `(comment ...)` a block of forms ignored on load, runnable in the
  REPL (the interactive Clojure prompt)

There is no `return`: a function returns the value of the last
expression in its body.

> **Truthiness.** `nil` and `false` are the *only* falsy values.
> Everything else is truthy — including `0`, `""`, `[]`, and `{}`. An
> empty collection is **not** falsy; test emptiness with `(seq coll)`
> (nil when empty) or `empty?`.

**Run it.** `just repl` starts a REPL from the repo root with `src`
and `test` on the classpath (it runs `clj -M:test`, which adds line
editing through rlwrap, or `clojure -M:test` without it); the prompt is
`user=>`. Type a form, read its value. Load a kata and refer its
functions with one line, and repeat that line after every edit to the
file:

```clojure
(require '[katas.kata-01-fizzbuzz :refer [fizzbuzz]] :reload)
```

The REPL documents itself — these are the offline clojuredocs:

```clojure
(doc take)              ; docstring and argument lists
(source take)           ; the source
(dir clojure.string)    ; a namespace's public names
(find-doc "separator")  ; every docstring containing the text
(apropos "join")        ; every name containing the text
```

They live in `clojure.repl`; the REPL that `just repl` starts refers
them into `user`. From any other namespace call `(clojure.repl/doc f)`.
Any REPL client does the same three things: send a form, show its
value, reload the namespace.



## 2. The built-in data types

| Type | Literal | Notes |
|---|---|---|
| Long | `42` | 64-bit integer by default |
| Double | `1.5` | double-precision float |
| Ratio | `1/3` | exact rationals |
| BigInt | `42N` | arbitrary precision |
| String | `"hi"` | immutable |
| Character | `\a` | a single character |
| Boolean | `true` `false` | |
| Nil | `nil` | absence of a value; `nil?` tests for it |
| Keyword | `:foo` `:user/name` | interned; map keys and enum-like constants |
| Symbol | `foo` | a name; bare it is looked up, `'foo` quotes it so it stays a name (§20) |
| Vector | `[1 2 3]` | indexed; `conj` appends to the end |
| List | `'(1 2 3)` | linked; `conj` prepends; quoted, else `(1 2 3)` calls `1` |
| Map | `{:a 1, :b 2}` | unordered; keys can be any value |
| Set | `#{1 2 3}` | unordered; unique values |

`+ - * /` take any number of arguments: `(- 10 3 2)` → `5`,
`(* 2 3 4)` → `24`. Division of integers is exact: `(/ 9 3)` → `3`,
but `(/ 2 3)` → `2/3`, a ratio, never `0.6666666666666666`. Ratio
arithmetic that lands on a whole number yields a BigInt, printed with
an `N`: `(* 2/3 (+ 4 5))` → `6N`. `(= 6 6N)` is true.

`(mod 17 5)` → `2`, the remainder. `(zero? (mod n d))` is the idiomatic
"is `n` divisible by `d`?".

`inc` and `dec` add and subtract one: `(inc 4)` → `5`, `(dec 4)` →
`3`. The predicates `zero?`, `pos?`, `neg?`, `even?`, `odd?` test one
number and return a boolean: `(even? 4)` → `true`.

**Keywords as functions.** A keyword invoked on a map looks itself up:

```clojure
(:name {:name "Ann", :role "engineer"})  ; => "Ann"
```



## 3. Control flow

`if` is an *expression* — it evaluates to the chosen branch's value:

```clojure
(if (> x 0) :positive :non-positive)
```

`when` is `if` with only a then-branch and an *implicit `do`*: the body
is any number of expressions, evaluated in order, the last is the
value, the rest run only for side effects (printing, mutation). `if`
has no implicit `do` — each branch is a single expression; `(do a b
c)` evaluates all three and has the value of `c`. That is the usual
reason to choose `when`. `when-not` is `when` with the test negated.

```clojure
(when (seq xs)
  (println "got items")
  (first xs))
```

The `println` runs for its effect; `(first xs)` is the value.

`cond` for multi-way branching; `:else` is the conventional catch-all
(just a truthy keyword):

```clojure
(cond
  (zero? n) :zero
  (pos? n) :positive
  :else :negative)
```

`cond` returns `nil` if nothing matches.

`=` is value equality: variadic (`(= 1 1 1)` → `true`) and structural
(`(= [1 2] '(1 2))` → `true` — sequential collections compare by
contents; maps and sets likewise). It does not cross numeric
categories: integers of any size are `=` (`(= 1 1N)` → `true`), but an
integer never equals a float: `(= 1 1.0)` → `false`. `not=` is its
negation. `not` negates any value: `(not nil)` → `true`, `(not 0)` →
`false`.

`and` and `or` short-circuit and return a *value*, not just a boolean.
`(and a b c)` evaluates left to right and yields the first falsey value
or, if none, the last value; `(or a b c)` yields the first truthy value
or the last. So `(and n (pos? n))` is a guarded test — `pos?` never
sees a `nil` — and `(or x default)` is "x unless it is nil/false".
They are the building blocks of compound predicates throughout the
katas.



## 4. `let` and destructuring

`let` introduces local bindings; they are sequential — each sees the
previous:

```clojure
(let [x 1
      y (+ x 2)
      z (* y 3)]
  (+ x y z))   ; => 13
```

*Destructure* — bind pieces of a collection by mirroring its shape — on
the left of any binding, and in function parameters:

```clojure
(let [[a b & more] [1 2 3 4 5]]      ; a=1, b=2, more=(3 4 5)
  ...)

(let [{:keys [name role]} {:name "Ann", :role "eng"}]
  ...)                                ; name="Ann", role="eng"

(defn greet [{:keys [name]}]          ; destructure a parameter
  (str "Hello, " name))
```

A binding you must write but do not use is named `_`, or `_sku` when
the name helps the reader — the convention for "ignored", and the
linter does not flag it.



## 5. Functions in depth

Multi-arity (overload by argument count) — and a lower-arity body
should usually call a higher-arity one rather than repeat logic:

```clojure
(defn greet
  ([] (greet "world"))
  ([name] (str "Hello, " name)))
```

Variadic with `&` — the extra arguments arrive as a seq (`nil` when
there are none):

```clojure
(defn sum [& nums] (apply + nums))

(sum 1 2 3)  ; => 6
```

`apply` spreads a seq into positional arguments: `(apply + [1 2 3])`
is `(+ 1 2 3)`.

Anonymous functions, two forms:

```clojure
(fn [x] (* x x))     ; longhand
#(* % %)             ; shorthand; %, %1, %2 for args
```

Three rules, in order:

1. Prefer the bare function — `(filter even? xs)`, not `#(even? %)`.
2. Reach for `#(…)` for a short single-form body — `#(* % %)`.
3. Reach for `fn` for anything multi-form, or where naming the
   parameter aids the reader.

> **`#(…)` is always a call.** The first element of its body is called:
> `#([% %])` calls the vector with no arguments and throws `Wrong number
> of args (0) passed to: clojure.lang.PersistentVector`. To return a
> literal, use `fn`: `(fn [x] [x x])`.

Functions are values: pass them, return them, store them. The standard
library is built on *higher-order functions* (functions taking or
returning functions).



## 6. Sequences — `map`, `filter`, `reduce`

The *seq* abstraction is Clojure's uniform "first element, then the
rest" view over data. Almost every collection is seq-able, and the core
sequence functions work over all of them the same way.

```clojure
(map inc [1 2 3])           ; => (2 3 4)
(filter even? (range 10))   ; => (0 2 4 6 8)
(remove even? (range 10))   ; => (1 3 5 7 9)
(reduce + 0 [1 2 3 4])      ; => 10
(reduce + [1 2 3 4])        ; => 10
```

`remove` is `filter` with the predicate negated. `reduce` is the fold:
carry an accumulator across the seq. `(reduce f init coll)` — and `f`
takes `[acc x]`; without `init`, the first element is the initial
accumulator. Most loops you would write in other languages are a
`reduce` here.

`range` is a lazy seq of integers: `(range 5)` → `(0 1 2 3 4)`,
`(range 1 6)` → `(1 2 3 4 5)`, `(range 0 10 2)` → `(0 2 4 6 8)`;
`(range 1 (inc n))` is 1..n inclusive.

`for` is a list comprehension — it builds a lazy seq by iterating
bindings — not a loop:

```clojure
(for [i (range 5)
      :when (odd? i)]
  (* i i))           ; => (1 9)
```

`:when` here is a *modifier clause* inside the bindings — it filters
iterations (not the `when` macro of §3); `(doc for)` lists the others.
`doseq` has the same shape but is for side effects and returns nil.

`vec` turns a seq into a vector; `into` pours one collection into
another; `mapv` is the eager, vector-returning `map`:

```clojure
(vec (map inc [1 2 3]))      ; => [2 3 4]
(mapv inc [1 2 3])           ; => [2 3 4]
(into {} [[:a 1] [:b 2]])    ; => {:a 1, :b 2}
```

These round out the basics:

```clojure
(count [1 2 3])        ; => 3
(first [1 2 3])        ; => 1
(rest [1 2 3])         ; => (2 3)
(last [1 2 3])         ; => 3
(take 2 [1 2 3])       ; => (1 2)
(drop 2 [1 2 3])       ; => (3)
(nth [1 2 3] 1)        ; => 2
(map-indexed (fn [i x] [i x]) [:a :b])   ; => ([0 :a] [1 :b])
```

`nth` is zero-based; `map-indexed` pairs each element with its index.

> **Laziness.** `(map f xs)` returns a *lazy* seq; nothing is computed
> until something pulls on it, so a side effect inside a lazy `map` may
> not run. Force it with `doall`, or use the eager `mapv`. The converse
> surprise: seqs over vectors and finite ranges are realised 32 elements
> at a time, so `(first (map println [1 2 3]))` prints all three and
> `map` looks eager. How many times a side effect inside `map` runs is
> unspecified. Keep side effects out of `map`; use `doseq` for them.

You now have enough for the first kata. Two more things to read before
it: the test file, and what a failure looks like.

**Reading a test file.** `just test 1` runs
`test/katas/kata_01_fizzbuzz_test.clj`, whose header pulls three names
from `clojure.test` and the function under test into scope:

```clojure
(ns katas.kata-01-fizzbuzz-test
  (:require [clojure.test :refer [deftest is testing]]
            [katas.kata-01-fizzbuzz :refer [fizzbuzz]]))
```

`deftest` names a test; `(is (= expected actual))` asserts, expected
first; `testing` labels a group of assertions; `vector?` is true of a
vector and nothing else. On the untouched stub the test at line 5
fails like this:

```text
FAIL in katas.kata-01-fizzbuzz-test/empty-input (kata_01_fizzbuzz_test.clj:5)
expected: []
  actual: nil
```

The first line names the test, then the file and line of the failing
`is`. `expected` is the literal from the test; `actual` is what your
function returned — `nil` here because the stub still returns nil. A
`testing` label or an `is` message prints between the header and
`expected`. A predicate assertion such as `(is (vector? (fizzbuzz
10)))` reports `(not (vector? nil))` as its actual.

When the two sides partly agree, a `diff:` follows: `-` holds what only
`expected` has and `+` what only `actual` has. For sequences this goes
position by position, with `nil` where they agree; for maps it lists
only the differing keys. Either line can be missing. When nothing lines
up — an empty `expected`, an `actual` of `nil`, maps that differ at
every key — there is no `diff:`; compare the two lines by eye. An
element missing from a sequence shifts every later position, so trust
only the first non-`nil` position.

**Reading an ERROR.** If a file does not compile, no test runs: the run
prints `Syntax error compiling at (katas/kata_NN_….clj:LINE:COL).` (or
`reading source` for an unclosed form) and the message. That line is
the location; ignore the `Full report at:` path.

An `ERROR` block means an exception escaped. Its header names where it
was thrown, often a Java file inside Clojure. Next comes
`expected:`/`actual:`, or `Uncaught exception, not in assertion.` when
it escaped outside an `is`; then the exception's class, its message and
the stack. The first frame from your source (`kata_NN_….clj`, not
`…_test.clj`) is the line that failed. If only the test file appears,
the test called what your function returned — an unfinished stub
returns `nil`. The messages that account for most errors:

| Message | Meaning |
|---|---|
| `Unable to resolve symbol: x in this context` | compile error: `x` is misspelt, or defined below its first use (§15) |
| `EOF while reading` | compile error: an unclosed `(`, `[` or `"` |
| `class java.lang.String cannot be cast to class java.lang.Number` | arithmetic on a non-number; the first class is what you passed |
| `class java.lang.Long cannot be cast to class clojure.lang.IFn` | a value used as a function: an extra pair of parentheses, or an unquoted list |
| `Wrong number of args (2) passed to: …` | an arity mismatch; the function is named at the end |
| `Cannot invoke "…" because "…" is null` | a `nil` was called as a function or passed to one that cannot take it: a stub's return value, or a lookup that found nothing |

At the REPL `*e` holds the last exception; `(ex-data *e)` returns the
data map attached to it, if any (§20).

> ## 🥋 Complete kata 1 ("FizzBuzz") before continuing



## 7. Numbers, arity, and exactness in practice

A checkpoint on §2 and §5, plus one idiom: `<`, `<=`, `>` and `>=` take
any number of arguments and check the whole chain: `(< 1 2 3)` →
`true`, `(< 1 3 2)` → `false`. The tests check predicates with
`true?`/`false?`, which accept only `true` and `false` themselves; a
comparison returns exactly those.

> ## 🥋 Complete kata 2 ("temperature") before continuing



## 8. Threading macros

Nested expressions read inside-out. The threading macros invert that.
(No relation to concurrency threads — *threading* here is feeding a
value step by step through a pipeline.)

`->` (thread-first) passes the value as the **first** argument to each
form; `->>` (thread-last) as the **last**:

```clojure
(-> {:a 1}
    (assoc :b 2)
    (update :a inc))            ; => {:a 2, :b 2}

(->> (range 100)
     (filter even?)
     (map #(* % %))
     (take 5))                  ; => (0 4 16 36 64)
```

A step that takes only the threaded value may be written bare:
`(->> xs (map inc) vec)` is `(vec (map inc xs))`.

Rule of thumb: data-shaped operations (`assoc`, `update`) go in `->`;
sequence pipelines go in `->>`. Most pipelines you write are `->>`.



## 9. Namespaces and `clojure.string`

A namespace is a unit of code. The first form in every file declares it
and the namespaces it requires:

```clojure
(ns shop.report
  (:require [clojure.string :as str]))
```

`:as` gives the required namespace a short alias: you then call
`str/split`, `str/lower-case`, `str/upper-case`, `str/join`,
`str/blank?`, `str/starts-with?`, etc. The conventional alias for
`clojure.string` is `str`; follow it. `:refer` brings names in
unqualified — the test files do this for `deftest`, `is` and `testing`,
and for the functions under test:

```clojure
(:require [clojure.test :refer [deftest is testing]])
```

`defn-` defines a function private to its namespace; the test files
use it for helpers.

`str/split` splits a string on a regex, written as a `#"..."` literal.
In a regex, `[…]` matches one character from a class — `[0-9]` a digit,
`[a-z]` a lower-case letter, `[0-9.]` a digit or a dot; one class may
hold several ranges and characters. `[^…]` negates a class, and `+`
means one or more of what precedes it:

```clojure
(str/split "2026-10-27" #"[^0-9]+")   ; => ["2026" "10" "27"]
(str/split "--10.27" #"[^0-9]+")      ; => ["" "10" "27"]
(str/split "" #"[^0-9]+")             ; => [""]
```

`str/split` can return empty strings: a leading one when the string
starts with a separator, and `[""]` for the empty string itself.

`(str a b c)` is string concatenation and the universal "to string";
`str/join` does the same over a collection, with or without a
separator:

```clojure
(str "a" 1 :k)            ; => "a1:k"
(str/join ", " [1 2 3])   ; => "1, 2, 3"
(str/join [\a \b])        ; => "ab"
(str/lower-case "MiXeD")  ; => "mixed"
```



## 10. Working with maps

The core map operations:

```clojure
(get m :k)            ; lookup, nil if missing
(get m :k :default)   ; with a default
(:k m)                ; same as (get m :k); idiomatic for keyword keys
(m :k)                ; a map is also a function of its key; (map m ks)
                      ;   looks every k up — but throws if m is nil

(assoc m :k v)        ; new map with :k = v
(assoc m :a 1 :b 2)   ; several at once
(dissoc m :k)         ; new map without :k
(update m :k inc)     ; new map, :k replaced by (inc (:k m))
(update m :k + 5)     ; extra args passed to the fn: (+ (:k m) 5)
(update m :k (fnil inc 0))  ; treat a missing key as 0

(assoc-in  m [:a :b :c] v)  ; nested assoc
(update-in m [:a :b] inc)   ; nested update
(get-in    m [:a :b :c])    ; nested lookup, nil if any level missing

(keys m) (vals m)
(merge m1 m2)               ; right wins on conflicts
(select-keys m [:a :b])     ; project to a subset of keys
(contains? m :k)            ; key present? (works on sets too)
```

`frequencies` builds a map of element → count from any seq:

```clojure
(frequencies "hello")   ; => {\h 1, \e 1, \l 2, \o 1}
```

A set is a function of its elements, returning the element or `nil`;
`set` builds one from any collection, dropping duplicates:

```clojure
(set [1 1 2])            ; => #{1 2}
(set (keys {:a 1}))      ; => #{:a}
(#{1 2} 2)               ; => 2
(#{1 2} 5)               ; => nil
```

Every function here returns a **new** map. The input is never mutated.



## 11. Sorting

```clojure
(sort [3 1 2])                       ; => (1 2 3)
(sort > [3 1 2])                     ; => (3 2 1)
(sort-by :age users)                 ; by a key function
(sort-by (juxt :last :first) users)  ; multi-key
```

A comparator is a 2-arg fn returning negative/zero/positive, or a 2-arg
predicate like `<`/`>`.

**Multi-key sorts.** `juxt` packs N functions into one that returns a
vector of their results, and vectors compare by length, then element
by element — so `(sort-by (juxt :last :first) users)` sorts by last
name, ties broken by first name.

**Mixed directions.** Comparators have no per-key direction; negate a
numeric key to flip it. `(comp - :age)` is "minus the :age" (`comp`
composes functions right-to-left), giving descending age:

```clojure
(sort-by (juxt (comp - :age) :last) users)
;; age descending, then last name ascending — a fully deterministic order
```

**Map entries.** Iterating a map yields `[k v]` pairs; `key` and `val`
pull them apart. A map entry also compares `=` to the plain
two-element vector `[k v]`, so you may return entries directly and test
or destructure them as `[k v]` without converting:

```clojure
(sort-by val {:b 2, :a 1})   ; => ([:a 1] [:b 2])
```

> ## 🥋 Complete kata 3 ("word frequencies") before continuing



## 12. Map surgery

Nothing new — a checkpoint. §10 is the toolkit for building, reading
and updating nested maps without changing one; §11 orders the result.

> ## 🥋 Complete kata 4 ("roster") before continuing



## 13. `group-by` and canonical forms

```clojure
(group-by odd? [1 2 3 4 5])        ; => {true [1 3 5], false [2 4]}
(group-by count ["a" "bb" "c"])    ; => {1 ["a" "c"], 2 ["bb"]}
```

`group-by` keeps each bucket's elements in input order, but the map it
returns has no *guaranteed* key order. Maps of up to 8 keys happen to
keep insertion order; larger ones do not — never rely on it.
(`identity` returns its argument unchanged.)

```clojure
(keys (group-by identity (range 8)))   ; => (0 1 2 3 4 5 6 7)
(keys (group-by identity (range 9)))   ; => (0 7 1 4 6 3 2 5 8)
```

`distinct` keeps the first occurrence of each value, in input order:

```clojure
(distinct [1 2 1 3 2])   ; => (1 2 3)
```

The pattern: pick a *canonical form* — one normalized representative
that all equivalent inputs share, expressed as a key function — and
let `group-by` bucket by it. Grouping integers by residue, for
instance, uses the remainder as the canonical form; every number with
the same remainder lands in the same bucket:

```clojure
(group-by #(mod % 3) (range 7))   ; => {0 [0 3 6], 1 [1 4], 2 [2 5]}
```

Strings are seqable: every sequence function accepts one as a seq of
characters:

```clojure
(seq "ab")   ; => (\a \b)
```

> ## 🥋 Complete kata 5 ("anagrams") before continuing



## 14. More sequence operations

```clojure
(partition 3 [1 2 3 4 5 6])         ; => ((1 2 3) (4 5 6))
(partition 2 1 [1 2 3 4])           ; => ((1 2) (2 3) (3 4))
(partition-by odd? [1 1 2 3 3 3])   ; => ((1 1) (2) (3 3 3))
(mapcat reverse [[1 2] [3 4]])      ; => (2 1 4 3)
(repeat 3 :x)                       ; => (:x :x :x)
(take 4 (iterate inc 0))            ; => (0 1 2 3)
```

`partition` chunks a seq into groups of n; with a step of 1 the groups
overlap. `partition-by f` starts a new group each time `(f x)` changes.

`mapcat` is `map` then `concat`: use it when each input yields zero or
more outputs. `repeat` makes n copies of a value; `iterate` is the
infinite seq `x`, `(f x)`, `(f (f x))`, …

```clojure
(mapcat (fn [x] [x (* 10 x)]) [1 2])   ; => (1 10 2 20)
```

> ## 🥋 Complete kata 6 ("run-length encoding") before continuing



## 15. Reading an unfamiliar namespace, and the REPL as an instrument

No new syntax — a method for reading, and safely changing, code you
did not write.

**Read a namespace top-down.** The `ns` form tells you its
dependencies and aliases. Then read the `def`/`defn` forms in order.
Definitions resolve top-down: a name must be defined above its first
use or the compiler reports `Unable to resolve symbol`; `(declare
name)` reserves a name for a definition further down. For each
function, the *data shape* it builds or consumes is the thing to pin
down first — find one concrete example (a docstring, a test, a
literal) and hold it in your head while you read.

**The data shape is the contract.** Clojure APIs are plain maps,
vectors, and keywords. "What keys does this map have, at what nesting?"
answers most questions. `get-in`/`assoc-in` paths in the code spell out
the structure.

**Tests are executable documentation.** When a docstring is thin, the
test file states the contract precisely: inputs in, expected values
out. Read the test before the implementation. In this dojo `just test
N` is that oracle; a failing assertion names the exact discrepancy.

**The REPL is how you investigate, not just run.** A REPL evaluates one
form at a time against the live namespace; the commands — `just repl`,
`require` with `:reload`, `doc`, `source`, `dir` — are in §1. The loop
for understanding or extending unfamiliar code:

- evaluate a function on a small literal input and look at the value;
- build the input data shape by hand, transform it step by step, and
  watch each intermediate;
- when a pipeline is wrong, paste it into the REPL and peel `->>`
  stages off the end until the output is right — the stage you removed
  is the bug.

**Changing inherited code.** Match the surrounding style. Keep the data
shape stable so existing callers and tests keep working — maps that
*grow* a key rarely break anyone; maps that *change* a key's meaning
break everyone. Make the failing test pass without rewriting code that
already works.

> ## 🥋 Complete kata 7 ("inventory") before continuing



## 16. Laziness in earnest

So far you have consumed lazy seqs; now build one.

A lazy seq computes its elements only when pulled. The simplest source
is a filter over an infinite generator:

```clojure
(def evens (filter even? (range)))   ; (range) is infinite
(take 5 evens)                       ; => (0 2 4 6 8)
```

`(range)` with no argument is an infinite lazy seq; so is `(iterate
#(* 2 %) 1)` — `1 2 4 8 …`. Filtering or mapping an infinite seq stays
lazy and infinite. `take`, `take-while`, and `nth` pull a finite
prefix:

```clojure
(take-while #(< % 12) evens)   ; => (0 2 4 6 8 10)
(nth evens 100)                ; => 200
```

Anything that pulls every element of an infinite seq — `count`, `vec`,
`last`, or the REPL printing it — never returns; look at `(take 5
evens)`, not bare `evens`. A REPL or `just test` that goes silent is
stuck in such a pull, or in a `loop` whose stop test never holds.
Ctrl-C ends it; rerun `just repl` and reload, since its definitions are
gone. `just test N test-name` narrows a run to one test.

Three predicates over a seq: `(some pred coll)` returns the first
truthy `(pred x)`, or `nil` when there is none; `(not-any? pred coll)`
is its negation (true iff `pred` holds for no element); `(every? pred
coll)` is the universal.

```clojure
(some even? [1 3 4])       ; => true
(some even? [1 3 5])       ; => nil
(not-any? even? [1 3 5])   ; => true
(every? odd? [1 3 5])      ; => true
```

`not-any?` and `every?` return exactly `true` or `false`; `some` does
not. Keep a predicate (a `?` function) strictly boolean — the tests
compare with `true?`/`false?`, and `nil` is not `false`.

A search for a divisor of `n` can stop at √n: a composite number
always has a factor at or below its square root. `(Math/sqrt n)` is
host interop (§0): `Class/method` calls a static method of a host
class. It returns a double; `(long x)` truncates a double to an
integer:

```clojure
(Math/sqrt 10)          ; => 3.1622776601683795
(long (Math/sqrt 10))   ; => 3
```

> ## 🥋 Complete kata 8 ("primes") before continuing



## 17. Recursion — `loop`/`recur`

The host does not optimise tail calls automatically (a *tail call* is a
call that is the last thing a function does). Clojure exposes them with
`recur`, which jumps back to an enclosing `loop` (or the function
itself) with new binding values:

```clojure
(defn factorial [n]
  (loop [n n
         acc 1]
    (if (zero? n)
      acc
      (recur (dec n) (* acc n)))))

(factorial 5)    ; => 120
(factorial 20)   ; => 2432902008176640000
```

`recur` must be in tail position; the compiler errors otherwise —
by design, since the alternative is a stack overflow. `loop`
establishes the rebindable names and their initial values; each
`recur` supplies the next values; an `if`/`cond` decides when to stop
and return the accumulator. `zero?`, `nil?` (true only of `nil`) and
`empty?` are the usual stopping tests.

Integer division comes in three pieces: `(quot 17 5)` → `3` (integer
quotient, truncated toward zero), `(rem 17 5)` → `2` (remainder, sign
of the dividend), `(mod 17 5)` → `2` (remainder, sign of the divisor).
The difference shows only with negatives: `(quot -17 5)` → `-3`,
`(rem -17 5)` → `-2`, `(mod -17 5)` → `3`.

Prefer `reduce` or a higher-order function when the shape fits. Reach
for `loop`/`recur` when the iteration carries several running values
or its length depends on the data — Euclid's gcd, which replaces
`[a b]` by `[b (rem a b)]` until `b` is zero. Deep iteration must use
`recur`, not self-calls, to avoid overflowing the stack.

> ## 🥋 Complete kata 9 ("digits") before continuing



## 18. Table-driven algorithms

A `reduce` (or a `loop`) over an ordered table of `[value output]`
rows replaces a long `cond` ladder: each row is tried in order, and
the data decides how many times it applies. Making change from an
ordered coin table:

```clojure
(def coins [[25 :quarter] [10 :dime] [5 :nickel] [1 :penny]])

(defn change [amount]
  (first (reduce (fn [[acc n] [value coin]]
                   [(into acc (repeat (quot n value) coin)) (rem n value)])
                 [[] amount]
                 coins)))

(change 68)   ; => [:quarter :quarter :dime :nickel :penny :penny :penny]
```

The accumulator carries two things — the output so far and what is
left to convert — so it is a pair, destructured on the way in and
rebuilt on the way out. Rows are value-first, largest first; the
greedy walk is correct only because the table is ordered.

`partition` with a step of 1 pairs each element with its successor; the
4-argument form pads a short final window so the last element still
gets one:

```clojure
(partition 2 1 [:a :b :c])          ; => ((:a :b) (:b :c))
(partition 2 1 [:end] [:a :b :c])   ; => ((:a :b) (:b :c) (:c :end))
```

`zipmap` builds a map by pairing two seqs positionally — keys with
values:

```clojure
(zipmap [:a :b :c] [1 2 3])   ; => {:a 1, :b 2, :c 3}
```

It is the natural way to build a lookup table from parallel sequences.

> ## 🥋 Complete kata 10 ("roman numerals") before continuing



## 19. Recursion — state machines

Some loops are not a fold: how much you consume each step depends on
what you just saw — a *state machine*. The defining property is
**variable lookahead** — depending on the current item you consume one
item, or two, or three.

```clojure
(defn run [cmds]
  (loop [cmds cmds
         pos 0
         path [0]]
    (let [[c & more] cmds]
      (cond
        (nil? c) path
        (= c :fwd) (recur more (inc pos) (conj path (inc pos)))
        (= c :goto) (let [[target & more2] more]   ; consumes TWO
                      (recur more2 target (conj path target)))))))

(run [:fwd :fwd :goto 7 :fwd])   ; => [0 1 2 7 8]
```

The `:fwd` branch `recur`s with `more` — one item consumed; the
`:goto` branch destructures one more value off the front and `recur`s
with `more2` — two consumed. A `reduce` sees one item per step, so it
can follow this only by carrying a mode flag in its accumulator; `loop`
takes what it needs directly. `(nil? c)` on the empty input terminates
and returns the accumulator.

> ## 🥋 Complete kata 11 ("bowling") before continuing



## 20. Type predicates, dispatch tables and structured errors

A *predicate* is a function named with a trailing `?` by convention;
the core ones return exactly `true` or `false` (§16). Each built-in
data type from §2 has one; they answer "what is this?" and are the
backbone of dispatching on heterogeneous data.

```clojure
(number?  42)      (number?  1.5)    ; => true   (any numeric type)
(integer? 42)      (integer? 1.5)    ; => true, then false
(string?  "hi")    (symbol? 'x)      ; => true   (note 'x — quoted)
(fn?  inc)         (fn? :k)          ; => true, then false
```

**`if-let` and `when-let`** bind one binding form — a name, or a
destructuring pattern (§4) — to a test's value and branch on it; the
bindings are in scope only in the truthy branch. Exactly one binding
pair is allowed.

```clojure
(if-let [x (first [4 5])] (inc x) :empty)   ; => 5
(if-let [x (first [])] (inc x) :empty)      ; => :empty
(when-let [x (first [4 5])] (* x 10))       ; => 40
(when-let [x (first [])] (* x 10))          ; => nil
```

**Symbols, keywords and quote.** A keyword `:foo` evaluates to itself.
A symbol `foo` is a name: evaluated, it is looked up; quoted, `'foo`,
it stays a symbol — data you can compare, store, and use as a map key.
Quote applies to any form: `'(1 2)` is a list, `'{x 10}` a map keyed by
the symbol `x`. `'foo` is reader shorthand for `(quote foo)`, the form
test output prints.

```clojure
(fn? +)            ; => true    the function
(fn? '+)           ; => false   the name of the function
(get '{x 10} 'x)   ; => 10
```

`case` dispatches on compile-time constants — constant-time, unlike a
`cond` scan. A list label matches any of its elements; a trailing
single form is the default; with no match and no default, `case`
**throws**:

```clojure
(case role
  :admin "all access"
  (:guest :visitor) "anonymous"
  "none")
```

> **`case` labels are not evaluated.** `(case x, limit :hit, :miss)`
> matches the *symbol* `limit`, not its value. If a branch's match must
> be computed, use `cond`.

**A map of functions is a dispatch table.** Before any heavier
polymorphism, notice that a map from a key to a function is often all
you need:

```clojure
(def hello {:en #(str "Hello, " %), :fr #(str "Bonjour, " %)})

((hello :fr) "Ann")   ; => "Bonjour, Ann"

(if-let [f (hello lang)]
  (f who)
  (throw (ex-info "unknown language" {:lang lang})))
```

A miss returns `nil`, so `if-let` is the natural guard.

**A vector is a stack.** `conj` pushes, `peek` reads the top, `pop`
drops it:

```clojure
(conj [1 2] 3)   ; => [1 2 3]
(peek [1 2 3])   ; => 3
(pop [1 2 3])    ; => [1 2]
(peek [])        ; => nil
(pop [])         ; throws IllegalStateException
```

**Structured errors with `ex-info`.** You almost never define a custom
exception class in Clojure. `ex-info` attaches an arbitrary data map to
an exception; the consumer matches on a key in it, never on the message
string. `throw` raises; `try` wraps a body and `catch` names the
exception class and a binding for it:

```clojure
(defn fetch [m k]
  (if (contains? m k)
    (get m k)
    (throw (ex-info "missing key" {:type :missing-key, :key k}))))

(try
  (fetch {:a 1} :b)
  (catch clojure.lang.ExceptionInfo e
    (ex-data e)))   ; => {:type :missing-key, :key :b}
```

**A note on the tests.** Most use `(is (= expected actual))` — plain
equality. A few use matcher-combinators' `(is (thrown-match? ExClass
data-shape expr))`: it asserts that the form throws and that its
`ex-data` matches the shape partially — extra keys in the actual data
are fine. On a mismatch, `actual:` marks each differing value as
`(mismatch (expected …) (actual …))`; when the form throws nothing it
reads `the expected exception wasn't thrown`.

> ## 🥋 Complete kata 12 ("RPN") before continuing



## 21. Identity vs value — atoms

Until now everything has been *pure* (output depends only on inputs; no
side effects). Real systems hold state. Clojure separates the two: a
*value* is immutable; an *identity* — a thing that holds different
values over time — is an explicit reference you `deref`.

```clojure
(def counter (atom 0))
(deref counter)        ; => 0
@counter               ; => 0    reader shorthand for deref
(swap! counter inc)    ; => 1    replaces with (inc current), atomically
(swap! counter + 10)   ; => 11   replaces with (+ current 10)
(reset! counter 100)   ; => 100  replaces unconditionally; returns new
```

> **Gotcha — `swap!` retries, so its function must be pure.** The
> function you pass to `swap!` can run **more than once** (it is retried
> under contention — another thread updated the same atom). No I/O, no
> side effects, no `reset!`-after-`deref` inside it. The read *and* the
> update must happen together, inside the swap function:

```clojure
;; WRONG — read and write are two separate steps; loses updates
(reset! scores (assoc @scores player pts))

;; RIGHT — read+update is one atomic step
(swap! scores assoc player pts)
```

That bug — `deref`, compute, `reset!` — is the single most common state
bug in real Clojure.

**Threads.** `(future body)` runs `body` on another thread and returns
a reference; `deref`/`@` on it blocks until the body has finished and
yields its value. `(run! f coll)` calls `f` on each element for its
side effects and returns `nil` — `doseq` as a function. `for` is lazy
(§6), so a `for` that creates futures must be wrapped in `doall`, or
no future starts until something pulls on the seq:

```clojure
(def f (future (+ 1 2)))
@f                                                       ; => 3
(def tasks (doall (for [i (range 3)] (future (* i i)))))
(map deref tasks)                                        ; => (0 1 4)
(run! deref tasks)                                       ; => nil
```

The concurrency tests fire hundreds of futures at one atom and wait
for all of them.

A function passed to `swap!` may `throw`; the exception propagates and
the atom keeps its value:

```clojure
(def cap (atom 3))
(swap! cap (fn [n]
             (if (< n 3) (inc n) (throw (ex-info "full" {:n n})))))
;; throws; nothing was committed
@cap   ; => 3
```

> ## 🥋 Complete kata 13 ("bank account") before continuing



## 22. Closures over state — functions that remember

A function that returns a function, where the returned function closes
over a private atom, is the idiomatic way to build a stateful function
without exposing the state:

```clojure
(defn counter-fn []
  (let [n (atom 0)]
    (fn [] (swap! n inc))))

(def c (counter-fn))
(c) (c) (c)   ; => 1, then 2, then 3 — n is private to c
```

A rest parameter `& args` is a seq, and `nil` for a zero-argument call
(§5); both are valid map keys.

`(constantly v)` returns a function that ignores its arguments and
returns `v` — a trivially cheap `f` for a test:

```clojure
((constantly 7) 1 2)   ; => 7
```

> ## 🥋 Complete kata 14 ("my-memoize") before continuing



## 23. Real polymorphism — protocols, records, multimethods

Two complementary tools, open along different axes.

**Protocols** group methods by *type*: a closed set of methods, an open
set of types implementing them — like an interface, except
`extend-protocol` adds an implementation to a type after the fact,
including types you do not own (a `String`, a host class), without
touching its source.

```clojure
(defprotocol Describable
  (summary [x]))

(defrecord Book [title pages]
  Describable
    (summary [_] (str title ", " pages " pp")))

(summary (->Book "SICP" 657))   ; => "SICP, 657 pp"
```

Inside a method body the record's fields are plain locals — `title`
and `pages` above. The method's first parameter is the record itself;
it is `_` here because the fields are already in scope.

`defrecord` generates a positional constructor, `->Book`. Records
behave as maps — keyword-lookup them. The record's name is its class,
and `instance?` asks whether a value is of a given class:

```clojure
(:pages (->Book "SICP" 657))           ; => 657
(instance? Book (->Book "SICP" 657))   ; => true
```

**Multimethods** dispatch on an arbitrary function of the arguments:
open set of methods *and* of types — most flexible, slightly slower.

```clojure
(defmulti price :tier)                 ; dispatch fn = (:tier arg)
(defmethod price :member [{:keys [base]}] (* 0.9 base))
(defmethod price :default [{:keys [base]}] base)

(price {:tier :member, :base 100})   ; => 90.0
(price {:tier :guest, :base 100})    ; => 100
```

`(methods price)` maps each dispatch value to its method — here
`:member` and `:default`.

A static field reads with the same `Class/member` syntax as §16's
static method: `Math/PI` is `3.141592653589793`.

**Which:** implementations cluster around a *type* → protocol (faster,
easier to navigate); around a *behaviour* keyed by data, not a host
type → multimethod. Adding a type costs one implementation on either
axis: a `defrecord` body (or `extend-protocol` for a type you do not
own) versus one `defmethod`. Adding an operation: a new method in an
existing protocol means editing the `defprotocol` and every implementer
— a missing one fails at call time; a new protocol extended to each
type, like a new `defmulti` with its `defmethod`s, leaves existing code
untouched.

**Caveat:** most real Clojure uses plain maps and never reaches for
`defrecord` — maps compose with the whole library, grow fields without
breaking callers, survive REPL reloads. Reach for a record when you
genuinely need protocol dispatch on a named type.

Reloading a namespace redefines its protocols and record classes; a
record built before the reload no longer satisfies the protocol:
`No implementation of method: :summary of protocol: #'user/Describable
found for class: user.Book`. Rebuild such values after every
`:reload`.

> ## 🥋 Complete kata 15 ("shapes") before continuing



## 24. Macros

A macro runs at *compile time* and returns a form to be compiled in its
place. Because source is just nested lists, that form is ordinary data
you build with ordinary code.

- **`'`** (quote) — `'x` is the symbol `x`, not its value.
- **`` ` ``** (syntax-quote) — like quote, but namespace-qualifies
  symbols and permits splicing.
- **`~`** (unquote) — inside syntax-quote, evaluate and insert.
- **`~@`** (unquote-splicing) — splice a sequence, removing one nesting
  level.
- **`gensym` / `x#`** — a unique symbol so the macro cannot capture a
  name the caller wrote.

```clojure
(defmacro my-when [test & body]
  `(if ~test (do ~@body) nil))

(macroexpand-1 '(my-when (pos? x) (println x) x))
;; => (if (pos? x) (do (println x) x) nil)
```

**Hygiene.** If a macro introduces a binding, give it a `gensym`ed name
(`v#` auto-gensyms inside a syntax-quote) so it cannot *shadow* a
caller's name:

```clojure
(defmacro my-or [a b]
  `(let [v# ~a] (if v# v# ~b)))   ; v# => v__1234__auto__
```

**Recursive expansion.** A macro may call itself; each step happens at
compile time, leaving a flat nested form. To reject misuse, throw while
expanding: `(IllegalArgumentException. "…")` calls a host constructor,
`(ClassName. args)`. The compiler wraps an exception thrown during
expansion in one of its own, so a test digs for the *cause*:
`(.getCause t)` is instance interop, `(.method object args)`, and the
`^Throwable` before a parameter is a type hint naming the class whose
method to call — read past it. A macro that throws, or expands to code
that does not compile, breaks the test file itself: `just test N`
prints `Syntax error macroexpanding <macro> at (<test file>:line:col)`
and runs nothing. Find that call in the test file and `macroexpand-1`
it at the REPL.

```clojure
(defmacro my->
  ([x] x)
  ([x form & more] `(my-> (~(first form) ~x ~@(rest form)) ~@more)))

(macroexpand-1 '(my-> 5 (- 1) (* 2)))   ; => (user/my-> (- 5 1) (* 2))
(my-> 5 (- 1) (* 2))                    ; => 8
```

Syntax-quote qualifies every symbol except special forms and `x#`
names. One that resolves takes its var's namespace: `let` →
`clojure.core/let`, `my->` → `user/my->` (`user` is whichever namespace
defined the macro). One that does not takes the macro's own namespace,
so a plain `tmp` becomes `user/tmp`, which `let` cannot bind — `Call to
clojure.core/let did not conform to spec` — hence `v#` for a macro's
locals; `(macroexpand-1 '(my-or a b))` shows it as a numbered
`__auto__` symbol. Special forms such as `if` and `do` stay bare, which
is why the `my-when` expansion above looked clean. Each
`macroexpand-1` threads one form and leaves a smaller `my->` call; the
next step expands that, until the one-argument arity ends it — a flat
nest of calls, no recursion left at run time.

**`seq?` vs `list?`.** `list?` is true only of an actual list; `seq?`
is true of a list *or* any other seq. Forms straight from the reader
are lists; what syntax-quote, `map`, `filter` and `rest` build are seqs
— `(list? (rest [1 2 3]))` is false, `(seq? (rest [1 2 3]))` is true.
`(seq coll)` (no `?`) is different again: it returns `nil` for an empty
collection, the idiomatic emptiness test from §1.

**When to write one:** rarely. A function composes with other
functions, can be passed around and `apply`ed, and keeps clean stack
traces; a macro can do none of that. Write a macro only to control
*evaluation* (do not evaluate this arg unless…) or *binding* (introduce
a name visible in a body).

> ## 🥋 Complete kata 16 ("macros") before continuing



## 25. The capstone — a tiny interpreter

Every piece of a tree-walking interpreter is one you have already
built:

- Source is already a Clojure data structure — no parser.
- Self-evaluating values (numbers, strings, keywords, booleans, `nil`,
  vectors, maps) return themselves.
- Symbol lookup is a map lookup.
- A composite form dispatches on its first element — the §20 dispatch
  pattern.
- `if`, `let`, `do`, `def`, `fn` are each a few lines of recursive
  evaluation over an environment.

The atom of §21 has no `atom?`; test for one with the host check
`(instance? clojure.lang.Atom x)` (§23). The other kinds of value have
predicates of their own:

```clojure
(keyword? :k)      (boolean? false)   ; => true
(map? {:a 1})      (vector? [1 2])    ; => true
```

A value that contains itself prints forever. A closure represented as
a map or record that holds the atom env, stored in that same env, is
one: at the REPL, wrap such a `def` as `(do (evaluate e '(def …))
:ok)`, and expect a `StackOverflowError`, not a FAIL, when a test's
actual value is such a closure. A host `fn` prints as an opaque object
and has no such cycle.

> ## 🥋 Complete kata 17 ("interpreter") before continuing



## Going further

The dojo is self-contained; these are for after.

- **[Clojure cheatsheet](https://clojure.org/api/cheatsheet)** — every
  core function on one page, grouped by what it operates on.
  **[clojuredocs.org](https://clojuredocs.org)** adds community
  examples per function.
- **[clojure.org/reference](https://clojure.org/reference)** and
  **[clojure.org/guides](https://clojure.org/guides)** — the maintained
  official docs. Transducers, spec and core.async are the main things
  this dojo skipped; start there.
- **Joy of Clojure** (Fogus & Houser, 2nd ed.) or **Programming
  Clojure** (Miller, Halloway & Bedra, 3rd ed.) for a book; the
  language is stable, so older editions hold.
- Rich Hickey's talks — "Simple Made Easy", "The Value of Values",
  "Hammock Driven Development" — for *why* the language is shaped as it
  is.
- **[4ever-clojure](https://4clojure.oxal.org/)** for more problems.
