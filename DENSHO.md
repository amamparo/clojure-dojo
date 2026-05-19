# DENSHO

The transmission. Read top to bottom; the order is the curriculum. At
each `> ## 🥋` break you have been given everything the next kata
needs — and nothing it doesn't. Solve it before reading on. Tests are
the oracle: `just test N` runs kata N. Some katas hand you working code
to read and extend instead of a blank stub; the brief says which.

This document is the only text you need. No web, no clojuredocs, no
LLM. The point is to finish with the language in your hands.



## 0. The mental model

Four ideas to absorb first. The rest is mechanical.

**Code is data.** A Clojure program is a tree of lists, vectors, and
maps; the compiler reads and evaluates that tree. There is no separate
AST (abstract syntax tree) — the source *is* the tree. This is why
macros (much later) are tractable, and why the final kata, an
interpreter, needs no parser.

**Values are immutable.** A vector, map, or set, once constructed,
cannot change. `assoc`, `conj`, and friends return *new* collections
that structurally share most of their innards with the old (cheap —
roughly O(log32 n); these are the *persistent* data structures, where
"persistent" means the old version stays valid, nothing to do with
disk). New to most coming from other languages, this removes a whole
class of bug within a week.

**Functions are the abstraction.** Where other languages reach for a
class, an object, or a loop, Clojure reaches for a function. State,
when genuinely needed, lives in explicit reference types you
*dereference* — read the current value out — and is the exception, not
the default.

**You are on a host.** Clojure runs on the JVM with its library
ecosystem; *interop* — calling host methods directly — is one
character of syntax away. You will barely need it; the standard library
covers these katas.

A note for the reader new to Clojure and to functional programming:
the first few sections will feel slow. Fluency precedes speed. The
katas are calibrated to that.



## 1. Syntax — the whole language in five minutes

Everything is a prefix-form expression in parentheses (prefix = the
operator comes first, then its arguments):

```clojure
(+ 1 2)            ; => 3
(println "hello")  ; => prints, returns nil
```

The first thing inside the parens is a function (or a *special form* —
a primitive the compiler handles directly, like `if` — or a macro).
The rest are arguments. No commas; whitespace separates. Commas, if you
write them, are whitespace — sometimes used in maps for readability.

Define a global with `def`, a function with `defn`:

```clojure
(def pi 3.14159)

(defn square [x]
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

There is no `return`. The value of any form is its last expression.

> ⚠️ **Gotcha — truthiness.** `nil` and `false` are the *only* falsy
> values. Everything else is truthy — including `0`, `""`, `[]`, and
> `{}`. An empty collection is **not** falsy; test emptiness with
> `(seq coll)` (nil when empty) or `empty?`. The single most common
> newcomer bug.



## 2. The built-in data types

| Type | Literal | Notes |
|---|---|---|
| Long | `42` | 64-bit integer by default |
| Double | `1.5` | double-precision float |
| Ratio | `1/3` | exact rationals — they just work |
| BigInt | `42N` | arbitrary precision |
| String | `"hi"` | immutable |
| Character | `\a` | a single character |
| Boolean | `true` `false` | |
| Nil | `nil` | absence of a value |
| Keyword | `:foo` `:user/name` | interned; map keys and enum-like constants |
| Symbol | `'foo` | a name; bare it is looked up, `'foo` quotes it as data |
| Vector | `[1 2 3]` | indexed; `conj` appends to the end |
| List | `'(1 2 3)` | linked; `conj` prepends; quoted, else `(1 2 3)` calls `1` |
| Map | `{:a 1, :b 2}` | hash-map; keys can be any value |
| Set | `#{1 2 3}` | hash-set |

Ratios matter early: `(/ 9 5)` is `9/5`, exact, not `1.8`. Integer
arithmetic that lands on an integer stays an integer (`(* 5/9 (- 212 32))`
→ `100`, not `100.0000001`). Kata 2 depends on this.

Integer division comes in three pieces: `(quot 17 5)` → `3` (floor of
the division), `(rem 17 5)` → `2` (remainder), `(mod 17 5)` → `2`
(remainder, but with the sign of the divisor — `(mod -1 3)` → `2`).
`(zero? (mod n d))` is the idiomatic "is `n` divisible by `d`?". Kata 1
needs `mod`; the digit-arithmetic kata needs `quot`/`rem`.

**Keywords as functions.** A keyword invoked on a map looks itself up:

```clojure
(:name {:name "Ann", :role "engineer"})  ; => "Ann"
```

That trick is everywhere in idiomatic Clojure. Memorise it.



## 3. Control flow

`if` is an *expression* — it evaluates to the chosen branch's value:

```clojure
(if (> x 0) :positive :non-positive)
```

`when` is `if` with only a then-branch and an *implicit `do`*: the body
is any number of expressions, evaluated in order, the last is the
value, the rest run only for side effects (printing, mutation). `if`
has no implicit `do` — each branch is a single expression. That is the
usual reason to choose `when`.

```clojure
(when (seq xs)
  (println "got items")   ; side effect; value discarded
  (first xs))             ; ← returned
```

`cond` for multi-way branching; `:else` is the conventional catch-all
(just a truthy keyword):

```clojure
(cond
  (zero? n) :zero
  (pos? n)  :positive
  :else     :negative)
```

`case` for compile-time-constant dispatch — an O(1) jump table, faster
than a `cond` scan when applicable:

```clojure
(case role
  :admin            "all access"
  (:guest :visitor) "anonymous"   ; a list label matches any element
  "none")                         ; trailing default, no test value
```

`cond` returns `nil` if nothing matches; `case` with no match and no
default **throws**.

> ⚠️ **Gotcha — `case` labels are not evaluated.** `(case x, limit :hit,
> :miss)` matches the *symbol* `limit`, not its value. If a branch's
> match must be computed, use `cond`.

`and` and `or` short-circuit and return a *value*, not just a boolean.
`(and a b c)` evaluates left to right and yields the first falsey value
or, if none, the last value; `(or a b c)` yields the first truthy value
or the last. So `(and (number? x) (pos? x))` is a guarded test, and
`(or x default)` is "x unless it is nil/false". They are the building
blocks of compound predicates throughout the katas.



## 4. `let` and destructuring

`let` introduces local bindings; they are sequential — each sees the
previous:

```clojure
(let [x 1
      y (+ x 2)
      z (* y 3)]
  (+ x y z))   ; => 1 + 3 + 9 = 13
```

*Destructure* — bind pieces of a collection by mirroring its shape — on
the left of any binding, and in function parameters:

```clojure
(let [[a b & rest] [1 2 3 4 5]]      ; a=1, b=2, rest=(3 4 5)
  ...)

(let [{:keys [name role]} {:name "Ann", :role "eng"}]
  ...)                                ; name="Ann", role="eng"

(defn greet [{:keys [name]}]          ; destructure a parameter
  (str "Hello, " name))
```



## 5. Functions in depth

Multi-arity (overload by argument count) — and a lower-arity body
should usually call a higher-arity one rather than repeat logic:

```clojure
(defn greet
  ([] (greet "world"))
  ([name] (str "Hello, " name)))
```

Variadic with `&`:

```clojure
(defn sum [& nums] (apply + nums))
```

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

Functions are values: pass them, return them, store them. The standard
library is built on *higher-order functions* (functions taking or
returning functions). You will use them constantly — far more than the
occasional callback in other languages.



## 6. Sequences — `map`, `filter`, `reduce`

The *seq* abstraction is Clojure's uniform "first element, then the
rest" view over data. Almost every collection is seq-able, and the core
sequence functions work over all of them the same way.

```clojure
(map inc [1 2 3])           ; => (2 3 4)
(filter even? (range 10))   ; => (0 2 4 6 8)
(reduce + 0 [1 2 3 4])      ; => 10
(reduce + [1 2 3 4])        ; => 10 (first element is the init)
```

`reduce` is the fold: carry an accumulator across the seq. `(reduce f
init coll)` — and `f` takes `[acc x]`. Most loops you would write in
other languages are a `reduce` here.

`range` is a lazy seq of integers: `(range 5)` → `(0 1 2 3 4)`,
`(range 1 6)` → `(1 2 3 4 5)`, `(range 0 10 2)` → `(0 2 4 6 8)`.

`for` is a list comprehension (build a seq by iterating bindings), not
a loop:

```clojure
(for [i (range 5)
      :when (odd? i)]
  (* i i))           ; => (1 9)
```

`:when` here is a *modifier clause* inside the bindings — it filters
iterations (not the `when` macro of §3); `:let [...]` and `:while test`
are siblings. `doseq` has the same shape but is for side effects and
returns nil.

`vec` turns a seq into a vector; `into` pours one collection into
another; `mapv`/`filterv` are eager vector-returning variants:

```clojure
(vec (map inc [1 2 3]))      ; => [2 3 4]
(mapv inc [1 2 3])           ; => [2 3 4]
(into {} [[:a 1] [:b 2]])    ; => {:a 1, :b 2}
```

`count`, `first`, `rest`, `last`, `take`, `drop`, `nth` round out the
basics. `(nth v i)` indexes; `(map-indexed (fn [i x] …) xs)` pairs each
element with its index.

> ⚠️ **Gotcha — laziness is invisible until it bites.** `(map f xs)`
> returns a *lazy* seq; nothing is computed until something pulls on
> it. You will not notice until a side effect inside a lazy `map`
> mysteriously does not run. Force it with `doall`, or use the eager
> `mapv`/`filterv`.

You now have enough for the first kata: building a vector by mapping a
multi-way decision over a range.

> ## 🥋 Complete kata 1 ("FizzBuzz") before continuing



## 7. Numbers, arity, and exactness in practice

Nothing new to learn — a checkpoint that consolidates §2 and §5. Kata 2
is pure arithmetic: exact ratios so integer inputs stay integers
(`(* c 9/5)`, never `1.8`), a multi-arity predicate whose one-argument
form delegates to its three-argument form, and the chained comparison
`(<= lo c hi)` (Clojure's comparison operators take any number of
arguments and check the whole chain).

> ## 🥋 Complete kata 2 ("temperature") before continuing



## 8. Threading macros

Nested expressions read inside-out, which is painful. The threading
macros invert that. (No relation to concurrency threads — *threading*
here is feeding a value step by step through a pipeline.)

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

Rule of thumb: data-shaped operations (`assoc`, `update`) go in `->`;
sequence pipelines go in `->>`. Most pipelines you write are `->>`.



## 9. Namespaces and `clojure.string`

A namespace is a unit of code. The first form in every file declares it
and its imports:

```clojure
(ns katas.kata-03-word-frequencies
  (:require [clojure.string :as str]))
```

You then call `str/split`, `str/lower-case`, `str/join`, `str/blank?`,
etc. The conventional alias for `clojure.string` is `str`; for
`clojure.set`, `set`. Mismatched aliases across a codebase are a small
but real friction — follow the convention.

`(str/split s #"[^a-z0-9]+")` splits on a regex (a `#"..."` literal).
Splitting a string that *starts* with a separator yields a leading
empty string you must drop — `remove str/blank?` is the usual cleanup.

`(str a b c)` is string concatenation and the universal "to string".



## 10. Working with maps

Maps are the workhorse. Learn these cold:

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

`fnil` wraps a function to substitute a default for a `nil` argument —
the polite way to handle a missing key inside `update`/`update-in`:
`(update-in m [:a :n] (fnil + 0) 3)` works whether or not `:a`/`:n`
existed.

Every function here returns a **new** map. The input is never mutated —
this is the whole point of kata 4 (build, read, and transform nested
maps without ever changing one).



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
vector of their results, and Clojure compares vectors element-wise — so
`(sort-by (juxt :last :first) users)` sorts by last name, ties broken
by first name.

**Mixed directions.** Comparators have no per-key direction; negate a
numeric key to flip it. `(comp - :score)` is "minus the :score"
(`comp` composes functions right-to-left), giving descending score:

```clojure
(sort-by (juxt (comp - :score) :name) results)
;; score descending, then name ascending — a fully deterministic order
```

**Map entries.** Iterating a map yields `[k v]` pairs; `key` and `val`
pull them apart. A map entry also compares `=` to the plain
two-element vector `[k v]`, so you may return entries directly and test
or destructure them as `[k v]` without converting. `(sort-by val
(frequencies s))` sorts entries by count, and `(sort-by (juxt (comp -
val) key) (frequencies s))` sorts them by count descending, ties broken
by key ascending. That is exactly how kata 3's `top-n` (and, next, kata
4's `leaderboard`) gets a fully deterministic order with a tie-breaker.

> ## 🥋 Complete kata 3 ("word frequencies") before continuing



## 12. Type predicates

A *predicate* is a function returning truthy/falsey, named with a
trailing `?` by convention. Each built-in data type from §2 has one;
they answer "what is this?" and are the backbone of dispatching on
heterogeneous data (the RPN evaluator, the macros kata, the capstone
interpreter all turn on them).

```clojure
(number?  42)      (number?  1.5)    ; => true   (any numeric type)
(integer? 42)      (integer? 1.5)    ; => true, then false
(string?  "hi")                      ; => true
(keyword? :k)      (symbol? 'x)      ; => true   (note 'x — quoted)
(boolean? true)    (nil? nil)        ; => true
(map?  {:a 1})     (set?  #{1})      ; => true
(vector? [1 2])    (list? '(1 2))    ; => true
(seq?  '(1 2))                       ; => true for a list or any seq
(coll? [1])        (coll? {:a 1})    ; => true for any collection
(fn?  inc)         (ifn? :k)         ; => true; ifn? also true of
                                     ;   keywords, maps, sets, vectors
                                     ;   — anything callable as a fn
```

Two that matter for the later katas:

- **`seq?` vs `list?`.** `list?` is true only of an actual list;
  `seq?` is true of a list *or* any lazy/seq value (what `map`,
  `filter`, `rest` return). When you walk a form that may be either,
  test `seq?`. `(seq coll)` (no `?`) is different again — it returns
  `nil` for an empty collection, the idiomatic emptiness test from §1.
- **Testing for an atom.** Reference types have no `atom?`. Use the
  host check `(instance? clojure.lang.Atom x)` — `instance?` asks
  whether `x` is of a given class, and is also how you test for a
  record type you defined (`(instance? Closure x)`). You will need
  both in the capstone.

`zipmap` builds a map by pairing two seqs positionally — keys with
values:

```clojure
(zipmap [:a :b :c] [1 2 3])      ; => {:a 1, :b 2, :c 3}
(zipmap "IVX" [1 5 10])          ; => {\I 1, \V 5, \X 10}
```

It is the natural way to build a lookup table from parallel sequences,
and to bind a list of parameter names to a list of argument values.

> ## 🥋 Complete kata 4 ("roster") before continuing



## 13. `group-by` and canonical forms

```clojure
(group-by odd? [1 2 3 4 5])   ; => {true [1 3 5], false [2 4]}
(group-by :role users)        ; => {:admin [...], :user [...]}
```

`group-by` keeps each bucket's elements in input order, but the map it
returns has **no** meaningful key order — `vals` over it is *not* in
first-appearance order. When the result must follow the order in which
each group first appeared, recover that yourself: pair each element
with its index via `map-indexed` (§6), then `sort-by` each group's
minimum index. `reduce`-ing a `{element → first-index}` map over the
indexed input is one clean way.

The pattern: pick a *canonical form* — one normalized representative
that all equivalent inputs share, expressed as a key function — let
`group-by` bucket by it, then post-process the buckets (`vals`,
`filter`, sort). For anagrams the canonical form of a word is its
sorted lower-cased letters: two words are anagrams iff their canonical
forms are equal.

> ## 🥋 Complete kata 5 ("anagrams") before continuing



## 14. More sequence operations

```clojure
(partition 3 [1 2 3 4 5 6])         ; => ((1 2 3) (4 5 6))
(partition 2 1 [1 2 3 4])           ; => ((1 2) (2 3) (3 4)) overlapping
(partition 2 1 [0] [1 2 3])         ; => ((1 2) (2 3) (3 0))  4-arg:
                                    ;   [0] pads a short final window
(partition-by odd? [1 1 2 3 3 3])   ; => ((1 1) (2) (3 3 3))
(mapcat reverse [[1 2] [3 4]])      ; => (2 1 4 3)  map then concat
(interpose "," ["a" "b" "c"])       ; => ("a" "," "b" "," "c")
(repeat 3 :x)                       ; => (:x :x :x)
(repeat :x)                         ; => infinite lazy (:x :x :x …)
(cycle [1 2 3])                     ; => infinite (1 2 3 1 2 3 …)
(iterate inc 0)                     ; => infinite (0 1 2 3 …)
```

`partition-by f` splits a seq into maximal runs equal under `f`. With
`identity` — the function that returns its argument unchanged — `f` is
"the element itself", so `(partition-by identity s)` groups consecutive
equal elements: run-length encoding falls straight out of it. `mapcat`
is `map` then `concat`: ideal when each input yields zero or more
outputs (decoding a run into N copies via `repeat`). Design
`encode`/`decode` as a matched pair so they round-trip.

> ## 🥋 Complete kata 6 ("run-length encoding") before continuing



## 15. Reading an unfamiliar namespace, and the REPL as an instrument

This is the section the north star turns on: most of your real work in
Clojure is reading code someone else wrote and changing it safely. No
new syntax — a method.

**Read a namespace top-down.** The `ns` form tells you its
dependencies and aliases. Then read the `def`/`defn` forms in order; a
file is usually written so earlier definitions are used by later ones.
For each function, the *data shape* it builds or consumes is the thing
to pin down first — find one concrete example (a docstring, a test, a
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
form at a time against the live namespace. The loop for understanding
or extending unfamiliar code:

- evaluate a function on a small literal input and look at the value;
- `(doc f)` and `(source f)` print a function's docstring and its
  source; `(dir some.ns)` lists its public vars;
- build the input data shape by hand, transform it step by step, and
  watch each intermediate;
- when a pipeline is wrong, paste it into the REPL and peel `->>`
  stages off the end until the output is right — the stage you removed
  is the bug.

No specific editor or REPL client is assumed; any will do. The skill is
the same everywhere: form in, value out, iterate.

**Changing inherited code.** Match the surrounding style. Keep the data
shape stable so existing callers and tests keep working — maps that
*grow* a key rarely break anyone; maps that *change* a key's meaning
break everyone. Make the failing test pass without rewriting code that
already works.

The next kata is not a blank stub: it is a small, working — but
incomplete and subtly wrong — namespace, the kind you inherit on day
one. Read it with the method above. One function has a defect the tests
expose (a boundary condition); one function is unimplemented. Fix and
extend in the existing style; leave the correct code alone.

> ## 🥋 Complete kata 7 ("inventory") before continuing



## 16. Laziness in earnest

You have *consumed* lazy seqs since kata 1. Now *build* one.

A lazy seq computes its elements only when pulled. The simplest source
is a filter over an infinite generator:

```clojure
(def evens (filter even? (range)))   ; (range) is infinite
(take 5 evens)                       ; => (0 2 4 6 8)
```

`(range)` with no argument is an infinite lazy seq; so is `(iterate inc
2)` — `2 3 4 5 …`. Filtering or mapping an infinite seq stays lazy and
infinite. `take`, `take-while`, and `nth` pull a finite prefix:

```clojure
(take-while #(< % 30) (filter even? (range)))   ; => (0 2 4 … 28)
```

When you need explicit control, `lazy-seq` wraps a body so it is not
evaluated until forced, and `cons` prepends one element to a (possibly
deferred) rest:

```clojure
(defn naturals-from [n]
  (lazy-seq (cons n (naturals-from (inc n)))))

(take 5 (naturals-from 1))   ; => (1 2 3 4 5)
```

That recursion never overflows the stack: `lazy-seq` defers the
recursive call until the next element is demanded. State the recursion
needs rides along as function arguments. A `def` may hold an infinite
lazy seq directly — handy for "the seq of all primes". The kata's
`primes` is exactly that; `nth-prime` is a lookup into it, not a
second search.

For the `prime?` predicate you need to ask "is there any divisor?".
`(some pred coll)` returns the first truthy `(pred x)` or `nil`;
`(not-any? pred coll)` is its negation (true iff `pred` holds for
none); `(every? pred coll)` is the universal. So "n is prime" is
`not-any?` a divisor in the candidate range — and the range need only
run to √n, because a composite always has a factor at or below its
square root (`(range 2 (inc (long (Math/sqrt n))))`; `Math/sqrt` is one
of the rare host-interop calls from §0). Equivalently, `empty?` over a
`filter` of the divisors says the same thing.

> ## 🥋 Complete kata 8 ("primes") before continuing



## 17. Recursion — `loop`/`recur`

The host does not optimise tail calls automatically (a *tail call* is a
call that is the last thing a function does). Clojure exposes them with
`recur`, which jumps back to an enclosing `loop` (or the function
itself) with new binding values:

```clojure
(defn factorial [n]
  (loop [n   n
         acc 1]
    (if (zero? n)
      acc
      (recur (dec n) (* acc n)))))
```

`recur` must be in tail position; the compiler errors otherwise — by
design, since the alternative is a stack overflow. `loop` establishes
the rebindable names and their initial values; each `recur` supplies
the next values; an `if`/`cond` decides when to stop and return the
accumulator.

Prefer `reduce` or a higher-order function when the shape fits. Reach
for `loop`/`recur` when you have a genuine accumulator that does not
fit a fold — integer-arithmetic digit problems (`quot`, `rem`), or an
iteration whose length is data-dependent (a Collatz chain). Deep
iteration must use `recur`, not self-calls, to avoid overflowing.

> ## 🥋 Complete kata 9 ("digits") before continuing



## 18. Table-driven algorithms

A `reduce` (or a `loop`) over an ordered table of `[value, output]`
rows replaces a long `cond` ladder. Roman numerals are the archetype:
an ordered table that *already includes* the subtractive forms
(`["CM" 900]`, `["IV" 4]`, …), walked greedily — at each step, the
largest row that still fits. The inverse direction reads two adjacent
symbols at a time (`partition 2 1`) and subtracts when a smaller
precedes a larger. Design the two directions to round-trip across the
whole range.

> ## 🥋 Complete kata 10 ("roman numerals") before continuing



## 19. Recursion — state machines

Some loops are not a fold: how much you consume each step depends on
what you just saw — a *state machine*. The defining property is
**variable lookahead** — depending on the current item you consume one
item, or two, or three.

```clojure
(defn run [cmds]
  (loop [cmds cmds
         pos  0
         path [0]]
    (let [[c & more] cmds]
      (cond
        (nil? c)       path
        (= c :fwd)     (recur more (inc pos) (conj path (inc pos)))
        (= c :goto)    (let [[target & more2] more]   ; consumes TWO
                         (recur more2 target (conj path target)))))))
```

Most branches `recur` with `more` — one item consumed. The `:goto`
branch destructures one more value off the front and `recur`s with
`more2` — two consumed. A `reduce` over `cmds` cannot express that;
the state rides along as loop bindings; `(nil? c)` on the empty input
terminates and returns the accumulator.

Not every state machine ends when its input runs out: some terminate on
a *counter* — a fixed number of steps (e.g. exactly ten frames) — and
consume whatever input remains as lookahead rather than as new steps.

Bowling is exactly this: a frame consumes one roll on a strike, two
otherwise, and the bonus reaches *forward* into later rolls. Walk
exactly ten frames; bonus rolls are scored as bonus, not as an
eleventh frame.

> ## 🥋 Complete kata 11 ("bowling") before continuing



## 20. Dispatch tables and structured errors

**A map of functions is a dispatch table.** Before any heavier
polymorphism, notice that a map from a key to a function is often all
you need:

```clojure
(def ops {'+ +, '- -, '* *, '/ /})
((ops '+) 2 3)        ; => 5
(if-let [f (ops sym)] (f a b) (handle-unknown sym))
```

It composes with `reduce` naturally: thread an accumulator (here, a
stack — `conj` pushes, `peek` reads the top, `pop` returns the rest)
through a token sequence, looking up each operator in the table. That
is the entire RPN evaluator.

**Structured errors with `ex-info`.** You almost never define a custom
exception class in Clojure. `ex-info` attaches an arbitrary data map to
an exception; the consumer matches on a key in it, never on the message
string:

```clojure
(throw (ex-info "stack underflow" {:type :stack-underflow}))

;; elsewhere
(catch clojure.lang.ExceptionInfo e
  (case (:type (ex-data e)) ...))
```

A note on the tests. Most use `(is (= expected actual))` — plain
equality. A few use matcher-combinators: `(is (match? shape actual))`
matches a map *partially* (extra keys in the actual value are fine),
and `(is (thrown-match? ExClass data-shape expr))` asserts both that
the form throws and that `ex-data` matches the given shape. Plain `=`
requires exact equality; `match?` is for asserting on `ex-data` or on
data that may carry more than the test pins down. Use whichever says
what you mean.

> ## 🥋 Complete kata 12 ("RPN") before continuing



## 21. Identity vs value — atoms

Until now everything has been *pure* (output depends only on inputs; no
side effects). Real systems hold state. Clojure separates the two: a
*value* is immutable; an *identity* — a thing that holds different
values over time — is an explicit reference you `deref`.

```clojure
(def counter (atom 0))
(deref counter)            ; => 0
@counter                   ; same thing
(swap! counter inc)        ; replace with (inc current), atomically; returns new
(swap! counter + 10)       ; replace with (+ current 10)
(reset! counter 100)       ; replace unconditionally
```

> ⚠️ **Gotcha — `swap!` retries, so its function must be pure.** The
> function you pass to `swap!` can run **more than once** (it is retried
> under contention — another thread updated the same atom). No I/O, no
> side effects, no `reset!`-after-`deref` inside it. The read *and* the
> update must happen together, inside the swap function:

```clojure
;; WRONG — read and write are two separate steps; loses updates
(reset! cache (assoc @cache k v))

;; RIGHT — read+update is one atomic step
(swap! cache assoc k v)
```

That bug — `deref`, compute, `reset!` — is the single most common state
bug in real Clojure. The next kata is again inherited code (not a blank
stub): a working account whose `deposit!` is written the wrong way and
fails a concurrency test until you move its append inside `swap!`. It
also leaves a function for you to implement. Use the §15 method: read
the correct functions for the data shape and house style; fix the one
that is wrong; fill in the missing one; do not disturb what works.

**Derive state, don't mutate it.** The deeper payoff of an atom is *not*
to mutate a value in place but to make a derived value cheap to
reconstruct from a record of what happened. Keep an append-only *log of
events* and `reduce` a pure step function over it to recover the current
state on demand — *event sourcing* in one sentence, and the same
`reduce`-with-accumulator from §20 (RPN), now over a domain log instead
of a token stream. The pure core is two functions: `apply-event`
(`[state event] → state`, the same `[acc x]` shape a `reduce` step
always has) and `replay` (a `reduce` of `apply-event` over the events,
from the empty state). Neither touches an atom — they are pure and
testable on plain vectors. The atom is then only a thin façade: it holds
**the event log and nothing else**, and the one mutating step appends a
single event in **one `swap!`** — exactly the read+update-together
discipline above, applied to log growth rather than to a counter. The
derived state is never stored: it is `(replay log)`, recomputed on
demand, so it can never drift from the events. Rejection leaves the log
— and therefore every projection of it — untouched: a malformed amount
is checked *before* the `swap!`, and a check that needs the live state
(an over-withdrawal) lives *inside* the swap function and `throw`s
there, which aborts the `swap!` without committing. The bank account is
built exactly
this way: the atom holds only `:history` (the event log); `balance` is
the projection `(replay history)`, not a field that is mutated; and
`apply-event`/`replay` are public so you can verify the projection
directly.

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

`memoize` is this pattern: wrap `f`, keep a private `{args → result}`
cache in an atom, compute once per distinct argument list. Two
subtleties the tests pin down: a *variadic* returned fn (`[& args]`,
key on the whole `args` vector) supports any arity; and the cache must
store falsey results too, so `contains?` on the cache — not the truthy-
ness of the cached value — decides whether to recompute.

> ## 🥋 Complete kata 14 ("my-memoize") before continuing



## 23. Real polymorphism — protocols, records, multimethods

Two complementary tools, open along different axes.

**Protocols** group methods by *type*: a closed set of methods, an open
set of types implementing them — like an interface, but you can extend
types you do not own.

```clojure
(defprotocol Shape
  (area [s])
  (perimeter [s]))

(defrecord Circle [r]
  Shape
    (area [_] (* Math/PI r r))
    (perimeter [_] (* 2 Math/PI r)))

(area (->Circle 3))   ; => 28.27…
```

`defrecord` generates a positional constructor `->Circle` and a
keyword one `map->Circle`. Records are maps underneath — `assoc` on
them, keyword-lookup them.

**Multimethods** dispatch on an arbitrary function of the arguments:
open set of methods *and* of types — most flexible, slightly slower.

```clojure
(defmulti describe :kind)                ; dispatch fn = (:kind arg)
(defmethod describe :circle [{:keys [r]}] (str "circle r " r))
(defmethod describe :default [{:keys [kind]}] (str "unknown: " kind))
```

**Which:** implementations cluster around a *type* → protocol (faster,
easier to navigate); around a *behaviour* keyed by data, not a host
type → multimethod; hierarchical dispatch → only multimethods. Adding a
type on the protocol axis means a whole new record; on the multimethod
axis, one more `defmethod`. That asymmetry is the kata.

**Caveat:** most real Clojure uses plain maps and never reaches for
`defrecord` — maps compose with the whole library, grow fields without
breaking callers, survive REPL reloads. Reach for a record when you
genuinely need protocol dispatch on a named type. This kata exists so
you have used the tool and felt the tradeoff.

> ## 🥋 Complete kata 15 ("shapes") before continuing



## 24. Macros — code as data, cashed in

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
compile time, leaving a flat nested form. When a macro takes a
*structured* input — a bindings vector, a parenthesised arithmetic
form — destructure that input as data and recurse on the rest. The §12
type predicates are how you tell which case you are in: `vector?` to
validate a bindings vector, `list?` to decide whether a form is a
composite to rewrite or an atom to emit as-is. Validate shape at
expansion time and `(throw (IllegalArgumentException. "…"))` on misuse;
the compiler will wrap that, so a test asserts on the *cause*.

A variadic short-circuiting `and` is the shape in miniature: a base
arity, a recursive arity that destructures the head off and self-calls
on the rest, spliced back in with `~@`, with a `gensym`ed binding so it
evaluates each argument once without capturing a caller's name.

```clojure
(defmacro my-and
  ([] true)
  ([x] x)
  ([x & more] `(let [v# ~x] (if v# (my-and ~@more) v#))))

(macroexpand-1 '(my-and a b c))
;; => (let [v# a] (if v# (my-and b c) v#))
```

Each `macroexpand-1` peels one operand and leaves a smaller `my-and`
call; the next step expands that, until the single- or zero-arg arity
ends it — a flat nest of `let`/`if`, no recursion left at run time.

**When to write one:** rarely. A function does not compose worse,
`apply`s, and keeps clean stack traces. Write a macro only to control
*evaluation* (do not evaluate this arg unless…) or *binding* (introduce
a name visible in a body). Both kata macros are of exactly those kinds:
`when-let*` controls evaluation and binding; `infix` rewrites a form
and emits code rather than evaluating operands itself.

> ## 🥋 Complete kata 16 ("macros") before continuing



## 25. The capstone — a tiny interpreter

Everything converges. A tree-walking interpreter for a small Lisp is
the natural closer because every piece is something you have already
built:

- Source is already a Clojure data structure — no parser.
- Self-evaluating values (numbers, strings, keywords, vectors, maps)
  return themselves.
- Symbol lookup is a map lookup.
- A composite form dispatches on its first element — the §20 dispatch
  pattern.
- `if`, `let`, `do`, `def`, `fn` are each a few lines of recursive
  evaluation over an environment.

The one piece of new wiring is node classification: `evaluate` decides
what an expression *is* before it can act — the §12 predicates do this.
A `symbol?` is an environment lookup; a non-`seq?` (number, string,
keyword, vector, map) is self-evaluating; an empty form is itself;
everything else is a composite to dispatch. The environment-as-atom
needs the §12 atom test (`instance? clojure.lang.Atom`) to distinguish
the mutable top-level env from the plain-map nested scopes, and a
closure value is recognised with `instance?` on the record type you
define. `zipmap` (§12) binds parameter names to argument values when
you apply a closure.

Two definitions, stated up front because they are the crux, not hints.
First: in `let`, each binding extends the environment the rest of the
bindings *and* the body see; the extension is local to the `let` —
shadowing does not leak out. Second: `def` must extend the environment
beyond a single form, so model the top-level env as an atom (plain maps
for nested `let`/`fn` scopes) that `def` can `swap!` into. A closure
captures that env reference, so a function bound by `def` sees later
`def`s — that is precisely how recursion through `def` works (kata 14's
closure-over-state, applied).

Beyond that classification step, the moving parts are ones you have
already built — recursion over a tree, a dispatch table, a closure over
state. The assembly is the work; lean on the earlier katas for each
piece.

> ## 🥋 Complete kata 17 ("interpreter") before continuing



## Going further

The dojo is self-contained; these are for after.

- **[Clojure cheatsheet](https://clojure.org/api/cheatsheet)** — every
  core function on one page, grouped by what it operates on.
  **[clojuredocs.org](https://clojuredocs.org)** adds community
  examples per function.
- **Joy of Clojure** (Fogus & Houser) or **Programming Clojure**
  (Miller, Halloway & Bedra) for a book.
- Rich Hickey's talks — "Simple Made Easy", "The Value of Values",
  "Hammock Driven Development" — for *why* the language is shaped as it
  is.
- **[4ever-clojure](https://4clojure.oxal.org/)** for more problems.

It will feel slow for the first few weeks. That is normal; fluency
comes before speed.
