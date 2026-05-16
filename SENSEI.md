# Clojure for Java/Python Engineers — A Kata-Aligned Guide

## 0. Mental model

Three ideas to internalise up front. Everything else is mechanical.

**Code is data.** A Clojure program is a tree of lists, vectors, and maps. The compiler reads this tree and evaluates it. That's it. There's no separate "AST" — the source *is* the AST. This is why macros are tractable in Lisps: a macro is just a function from tree to tree.

**Values are immutable.** A vector, map, or set, once constructed, cannot be changed. `assoc`, `conj`, etc. return *new* collections that structurally share most of their innards with the old one (this is cheap — O(log32 n) for the persistent data structures). Coming from Python/Java this feels suffocating at first; you'll find within a week that "I cannot accidentally mutate this" is a load off your mind.

**Functions are the primary abstraction.** Where Java reaches for a class and Python reaches for an object, Clojure reaches for a function. State, when needed, is wrapped in explicit reference types (atoms, refs, agents) that you `deref` to get a value.

You're on the JVM. Java interop is one character of syntax away. The REPL is your IDE — get one running before reading further.

---

## 1. Syntax — the whole language in 5 minutes

Everything is a prefix-form expression in parentheses:

```clojure
(+ 1 2)            ; => 3
(println "hello")  ; => prints, returns nil
```

The first thing inside the parens is the function (or special form, or macro). The rest are arguments. No commas needed; whitespace separates. Commas are whitespace if you want to use them in maps for readability.

Define a global with `def`, a function with `defn`:

```clojure
(def pi 3.14159)

(defn square [x]
  (* x x))

(square 5)  ; => 25
```

Note `[x]` — argument lists are *vectors*, not parenthesised. This is one of the few cases where the syntax distinguishes structure from invocation.

**Comments:**
- `;` end-of-line comment
- `;;` line comment on its own line (idiomatic for narrative comments)
- `#_form` skip the next form entirely — useful for "comment out this expression"
- `(comment ...)` block of forms ignored when loaded; runnable in the REPL

**Truthiness:** `nil` and `false` are falsy. Everything else is truthy — including `0`, `""`, and empty collections. This differs from Python and is one of the most common bug sources for newcomers.

---

## 2. The built-in data types

| Type | Literal | Notes |
|---|---|---|
| Long | `42` | Java `long` by default |
| Double | `1.5` | Java `double` |
| Ratio | `1/3` | exact rationals, just work |
| BigInt | `42N` | arbitrary precision |
| String | `"hi"` | Java strings |
| Character | `\a` | Java `char` |
| Boolean | `true`, `false` | |
| Nil | `nil` | like Java `null` |
| Keyword | `:foo`, `:user/name` | interned, == in O(1); used as map keys and "constants" |
| Symbol | `'foo` | names that refer to vars; `'` quotes them |
| Vector | `[1 2 3]` | indexed access, conj appends to end |
| List | `'(1 2 3)` | linked list, conj prepends; quoted because `(1 2 3)` would try to call `1` |
| Map | `{:a 1, :b 2}` | hash-map; keys can be any value |
| Set | `#{1 2 3}` | hash-set |

**Keywords vs strings:** use keywords for map keys and enum-like constants. They're cheaper, they self-evaluate (don't need quoting), and they can be invoked as functions:

```clojure
(:name {:name "Aaron" :role "engineer"})  ; => "Aaron"
```

That last trick — keyword-as-function — is everywhere in idiomatic Clojure. Memorise it.

---

## 3. Control flow

`if` is an *expression*, not a statement. It returns the chosen branch's value. There is no `return`; the last expression in any form is its value.

```clojure
(if (> x 0) :positive :non-positive)
```

`when` is `if` with only a then-branch (and an implicit `do`):

```clojure
(when (seq xs)
  (println "got items")
  (first xs))
```

`cond` for multi-way branching. `:else` is the conventional catch-all (it's just a truthy keyword):

```clojure
(cond
  (zero? n)      :zero
  (pos? n)       :positive
  :else          :negative)
```

`case` for compile-time-constant dispatch — faster than `cond` when applicable:

```clojure
(case role
  :admin  "all access"
  :user   "limited"
  "none")  ; default (no key)
```

---

## 4. `let` and destructuring

`let` introduces local bindings. Bindings are sequential — each can see the previous:

```clojure
(let [x 1
      y (+ x 2)
      z (* y 3)]
  (+ x y z))   ; => 1 + 3 + 9 = 13
```

Destructure on the LHS of any binding:

```clojure
(let [[a b & rest] [1 2 3 4 5]]
  ;; a=1, b=2, rest=(3 4 5)
  )

(let [{:keys [name role]} {:name "Aaron" :role "engineer"}]
  ;; name="Aaron", role="engineer"
  )
```

Destructuring also works in function parameters, which is how you "name" map keys in an API without ceremony:

```clojure
(defn greet [{:keys [name]}]
  (str "Hello, " name))
```

---

## 5. Functions in depth

Multi-arity (overloading by argument count):

```clojure
(defn greet
  ([] (greet "world"))
  ([name] (str "Hello, " name)))
```

Variadic with `&`:

```clojure
(defn sum [& nums] (apply + nums))
```

Anonymous functions — two forms:

```clojure
(fn [x] (* x x))     ; longhand
#(* % %)             ; shorthand; %, %1, %2 for args
```

Don't use `#(…)` for multi-form bodies; use `fn`. Don't wrap a function in `#()` if the function alone works — `(filter even? xs)` not `(filter #(even? %) xs)`.

Functions are values: pass them, return them, store them. This will feel like Python lambdas, but used vastly more. The standard library is built around higher-order functions; you'll use them constantly.

---

## 6. Sequences — `map`, `filter`, `reduce`

The seq abstraction is Clojure's iterator protocol. Almost every collection can be viewed as a seq, and the core sequence functions work on all of them uniformly.

```clojure
(map inc [1 2 3])              ; => (2 3 4)
(filter even? (range 10))      ; => (0 2 4 6 8)
(reduce + 0 [1 2 3 4])         ; => 10
(reduce + [1 2 3 4])           ; => 10 (uses first elem as init)
```

`range` produces a (lazy) seq of integers. `(range 5)` → `(0 1 2 3 4)`. `(range 1 6)` → `(1 2 3 4 5)`.

`for` is a list comprehension, not a loop — same idea as Python's:

```clojure
(for [i (range 5)
      :when (odd? i)]
  (* i i))
;; => (1 9)
```

`vec` turns a seq into a vector; `into` pours one collection into another:

```clojure
(vec (map inc [1 2 3]))           ; => [2 3 4]
(into [] (map inc) [1 2 3])       ; => [2 3 4] (transducer form; same result)
(into {} [[:a 1] [:b 2]])         ; => {:a 1, :b 2}
```

**Sequences are lazy by default.** `(map f xs)` returns a lazy seq; nothing is computed until something pulls on it. This is mostly invisible until you do I/O inside a `map`, at which point you'll be confused why nothing happened. Force with `doall`, or use `mapv` / `filterv` for eager vectors.

> **By this point, you've learned enough to complete Kata 1 (FizzBuzz).** You now have `defn`, `cond`, `mod`, and `map` over `range` (or a `for` comprehension) — the whole kata. Stop reading and write it.

---

## 7. Threading macros

Reading nested expressions inside-out is painful. The threading macros invert that.

`->` (thread-first) takes a value and passes it as the **first** arg to each form:

```clojure
(-> "  Hello World  "
    .trim
    .toLowerCase
    (clojure.string/replace " " "-"))
;; => "hello-world"
```

`->>` (thread-last) passes as the **last** arg — typically used for seq pipelines:

```clojure
(->> (range 100)
     (filter even?)
     (map #(* % %))
     (take 5))
;; => (0 4 16 36 64)
```

Rule of thumb: data-shaped operations (`assoc`, `update`, Java method calls) go in `->`; sequence operations go in `->>`. Most Clojure pipelines you'll write are `->>`.

`as->` lets you name the intermediate value when neither position fits.

---

## 8. Working with maps

Maps are the workhorse data structure. Learn these by heart:

```clojure
(get m :k)            ; lookup, nil if missing
(get m :k :default)   ; with default
(:k m)                ; same as (get m :k); idiomatic for keyword keys
(m :k)                ; works too, but NPE if m is nil — avoid

(assoc m :k v)        ; new map with :k=v
(assoc m :a 1 :b 2)   ; multiple at once
(dissoc m :k)         ; new map without :k
(update m :k inc)     ; new map with :k replaced by (inc (:k m))
(update m :k (fnil inc 0))   ; treats missing key as 0

(assoc-in m [:a :b :c] v)   ; nested
(update-in m [:a :b] inc)
(get-in m [:a :b :c])

(keys m)  (vals m)
(merge m1 m2)         ; right wins on conflicts
(select-keys m [:a :b])
```

`frequencies` builds a map of element → count from any seq:

```clojure
(frequencies "hello")   ; => {\h 1, \e 1, \l 2, \o 1}
```

`fnil` is the polite way to handle nil defaults inside `update`:

```clojure
(update m :count (fnil inc 0))
```

---

## 9. Namespaces and `clojure.string`

A namespace is a unit of code. The first form in every file declares it and any imports:

```clojure
(ns katas.kata-03-anagrams
  (:require [clojure.string :as str]))
```

You then call `str/lower-case`, `str/split`, etc. The conventional aliases are worth memorising: `str`, `set`, `io`, `json`, `time`. Mismatched aliases across a codebase is a small but real friction.

`:require :refer [foo bar]` pulls specific names in directly. Use it sparingly — explicit aliasing tells the reader where a function comes from.

Use `clojure.string/...` for string ops, not Java methods, unless interop is more idiomatic for what you're doing. E.g. `str/lower-case` over `.toLowerCase`.

---

## 10. Sorting

```clojure
(sort [3 1 2])                       ; => (1 2 3)
(sort > [3 1 2])                     ; => (3 2 1)
(sort-by :age users)                 ; key fn
(sort-by (juxt :last :first) users)  ; multi-key
```

Custom comparators: a 2-arg fn returning negative/zero/positive, or a 2-arg predicate (`<`, `>`).

For "rank by count desc, ties alphabetical asc" you want `(sort-by (juxt #(- (val %)) key) m)` or similar — chew on that before reading on.

> **By this point, you've learned enough to complete Kata 2 (word frequencies).** `clojure.string/split` on whitespace, lower-case each word, trim punctuation with a regex or character set, count with `frequencies`, then `sort-by` for `top-n`.

---

## 11. `group-by`

```clojure
(group-by odd? [1 2 3 4 5])
;; => {true [1 3 5], false [2 4]}

(group-by :role users)
;; => {:admin [...], :user [...]}
```

The pattern: pick a *canonical form* (a key function), let `group-by` bucket things, then post-process. For anagrams, the canonical form of a word is its sorted lowercased letters.

> **By this point, you've learned enough to complete Kata 3 (anagrams).** Two strings are anagrams iff their canonical forms are equal. `group-anagrams` is `(vals (group-by canonical-form strings))` — but you'll want to think about preserving insertion order (hint: `group-by` already uses an array-map for small inputs, but don't rely on that — see what your tests demand).

---

## 12. More sequence operations

```clojure
(partition 3 [1 2 3 4 5 6])         ; => ((1 2 3) (4 5 6))
(partition 2 1 [1 2 3 4])           ; => ((1 2) (2 3) (3 4)) — overlapping windows
(partition-by odd? [1 1 2 3 3 3])   ; => ((1 1) (2) (3 3 3))
(mapcat reverse [[1 2] [3 4]])      ; => (2 1 4 3) — map then concat
(interpose "," ["a" "b" "c"])       ; => ("a" "," "b" "," "c")
(repeat 3 :x)                       ; => (:x :x :x)
(repeat :x)                         ; => infinite lazy seq of :x
(cycle [1 2 3])                     ; => infinite (1 2 3 1 2 3 ...)
(iterate inc 0)                     ; => infinite (0 1 2 3 ...)
```

`mapcat` is `map` then `concat` — perfect when each input produces 0 or more outputs.

`partition-by` splits a seq into runs of "equal under f" — run-length encoding falls out of it directly.

> **By this point, you've learned enough to complete Kata 4 (RLE).** `partition-by identity` then `(map (juxt count first) ...)`. Decode is `mapcat` of `repeat`. The trick is making both work seamlessly on strings, vectors, and lazy seqs — that's the seq abstraction earning its keep.

---

## 13. Laziness in earnest

You've been using lazy seqs (everything `map`/`filter`/`range` produces). Now you'll *build* one.

`lazy-seq` wraps a body so it's not evaluated until something pulls on it. The classic recursive lazy-seq pattern:

```clojure
(defn naturals-from [n]
  (lazy-seq (cons n (naturals-from (inc n)))))

(take 5 (naturals-from 1))  ; => (1 2 3 4 5)
```

That recursion never blows the stack because `lazy-seq` defers the recursive call until needed. Each element materialises on demand.

A `def` can hold a lazy seq directly — handy for "the seq of all X":

```clojure
(def all-evens (filter even? (range)))
(take 5 all-evens)   ; => (0 2 4 6 8)
```

Two caveats with lazy seqs:
1. Side effects inside laziness happen *when realised*, not when defined. Don't rely on ordering or "did it run."
2. Holding the head of a long lazy seq while walking it can pin the whole thing in memory ("head retention"). Usually not a problem; just be aware.

> **By this point, you've learned enough to complete Kata 5 (primes).** Trial division with a `prime?` predicate, then `(def primes (filter prime? (iterate inc 2)))` is a one-liner that works but is O(n√n) per prime. A faster idiomatic version uses `lazy-seq` with a recursive helper that tests candidates against the primes already produced — closer to what the kata description hints at. Write the simple version first, then the lazy-recursive one.

---

## 14. Sets — both predicate and data

Sets as predicates (the most underrated idiom):

```clojure
(filter #{1 2 3} (range 10))       ; => (1 2 3)
(remove #{\a \e \i \o \u} "hello") ; => (\h \l \l)
```

`#{...}` is just a set, and a set is a function of its elements (returning the element if present, `nil` otherwise). Truthy-on-hit is exactly what you want for `filter`/`remove`.

Set operations live in `clojure.set`:

```clojure
(require '[clojure.set :as set])
(set/union #{1 2} #{2 3})        ; => #{1 2 3}
(set/intersection #{1 2} #{2 3}) ; => #{2}
(set/difference #{1 2 3} #{2})   ; => #{1 3}
```

The Game of Life encoding — *the set of live cells is the world* — is a small revelation. The grid is implicitly infinite. To advance, generate every cell that could possibly change (the alive cells + their neighbours), tally each one's live-neighbour count with `frequencies` over `(mapcat neighbours alive)`, and keep the ones that survive or are born.

> **By this point, you've learned enough to complete Kata 6 (Game of Life).** Spend a minute on paper before coding. Two helpers (`neighbours` of a coordinate, and `step` of a world) are all you need.

---

## 15. Recursion — `loop`/`recur` and state machines

The JVM doesn't optimise tail calls automatically. Clojure exposes them via the explicit `recur` form, which jumps back to an enclosing `loop` or function with new bindings:

```clojure
(defn factorial [n]
  (loop [n n
         acc 1]
    (if (zero? n)
      acc
      (recur (dec n) (* acc n)))))
```

`recur` must be in tail position; the compiler will error if it isn't. This is intentional — you'd get a stack overflow otherwise.

For most problems, prefer `reduce` or a higher-order function over hand-rolled `loop`/`recur`. Reach for `loop` when you genuinely have an accumulator that doesn't fit a fold.

> **By this point, you've learned enough to complete Kata 7 (Roman numerals).** You'll want a lookup table (a vector of `[value symbol]` pairs in descending order), a `loop`/`recur` or `reduce`, and the inverse going the other way. Round-trip via your tests.

That covers the gentle case. The step up is a loop whose next move depends on what it just consumed — a *state machine*. Some problems don't fit a fold cleanly; bowling is the classic. The rules look at variable amounts of "lookahead" depending on what happened in the current frame. The shape is:

```clojure
(defn score [rolls]
  (loop [rolls rolls
         frame 1
         total 0]
    (cond
      (> frame 10) total
      ;; strike: take next-two as bonus, advance one roll, next frame
      ;; spare:  take next-one as bonus, advance two rolls, next frame
      ;; open:   sum two rolls, advance two rolls, next frame
      )))
```

The key habits:
- Destructure the head of `rolls` with `(let [[a b c & _] rolls] ...)`.
- Don't try to mutate; `recur` with new values.
- Frame counter as an explicit loop variable.

> **By this point, you've learned enough to complete Kata 8 (bowling).** Tip: in the 10th frame the bonus rolls don't start a new frame. Your loop's stopping condition is `frame > 10`, not `(empty? rolls)`.

---

## 16. Identity vs value — atoms, `ex-info`

So far, everything has been pure. Real systems have state. Clojure's answer: a *value* is immutable, but an *identity* — a thing that holds different values over time — is explicit.

```clojure
(def counter (atom 0))

(deref counter)   ; => 0
@counter          ; same thing
(swap! counter inc)        ; atomically replace value with (inc current); returns new value
(swap! counter + 10)       ; (apply + current 10)
(reset! counter 100)       ; replace unconditionally
```

`swap!` is *retried* under contention — the function you pass can run more than once. That's why it must be pure: no I/O, no side effects, no `reset!`-after-`deref`. Read+update happens *inside* the swap function, atomically.

```clojure
;; WRONG — race condition
(reset! account (- @account amount))

;; RIGHT
(swap! account update :balance - amount)
```

**Structured errors with `ex-info`:**

```clojure
(throw (ex-info "Insufficient funds"
                {:type :insufficient-funds
                 :balance current
                 :requested amount}))

;; later:
(catch clojure.lang.ExceptionInfo e
  (let [{:keys [type]} (ex-data e)]
    ...))
```

You almost never define a custom exception class in Clojure. `ex-info` carries arbitrary data; the consumer matches on `(:type (ex-data e))`.

> **By this point, you've learned enough to complete Kata 9 (bank account).** Read the kata's concurrency note carefully — the test fires 1,000 futures at one account. The point of the test is to make you put the read-update logic inside `swap!`, not around it.

---

## 17. Dispatch — maps of functions

Before reaching for the heavy polymorphism tools, notice that a map of functions is often all you need:

```clojure
(def ops
  {'+ +, '- -, '* *, '/ /})

(defn apply-op [stack op]
  (let [f (ops op)
        [b a & rest] stack]
    (cons (f a b) rest)))
```

That's a dispatch table. It composes with `reduce`:

```clojure
(reduce step initial-stack tokens)
```

For RPN, your token handler distinguishes "this is a number → push" from "this is an op → pop, apply, push." Strings vs symbols are normalised by going through `symbol`:

```clojure
(symbol "+")  ; => +
(symbol '+)   ; => +
```

> **By this point, you've learned enough to complete Kata 10 (RPN).** A `reduce` over tokens, with the accumulator being the stack. `dup`/`drop`/`swap` are stack-shape ops; `+`/`-`/`*`/`/` are arithmetic ops; reach for two dispatch tables or one with a small union type, your call.

---

## 18. Real polymorphism — protocols, records, multimethods

Two complementary tools. Both are open in different axes.

**Protocols** group methods by *type*. Closed set of methods, open set of types implementing them — like a Java interface, but you can add implementations to types you don't own.

```clojure
(defprotocol Shape
  (area [s])
  (perimeter [s]))

(defrecord Circle [r]
  Shape
  (area [_] (* Math/PI r r))
  (perimeter [_] (* 2 Math/PI r)))

(area (->Circle 3))   ; => 28.27...
```

A `defrecord` generates a `->Circle` positional constructor and a `map->Circle` keyword constructor. Records are maps under the hood — you can `assoc` on them, `:r` them.

**Multimethods** dispatch on an arbitrary function of the arguments. Open set of methods *and* open set of types — most flexible, slightly slower.

```clojure
(defmulti describe :kind)

(defmethod describe :circle [{:keys [r]}]
  (str "circle with radius " r))

(defmethod describe :default [{:keys [kind]}]
  (str "unknown shape: " kind))
```

**When to use which:**
- All implementations cluster around a *type*? Protocol. (Faster, IDE-friendly.)
- All implementations cluster around a *behaviour* and the dispatch key isn't a JVM type? Multimethod.
- Hierarchical dispatch (`derive`/`isa?`)? Only multimethods.

> **By this point, you've learned enough to complete Kata 11 (shapes).** The kata deliberately does both — it'll make the trade-off concrete.

---

## 19. Macros — code as data, finally cashed in

A macro is a function that runs at *compile time* and returns a form to be compiled in its place. Because Clojure source is just nested lists, that "form" is a regular Clojure data structure you build with regular Clojure code.

The tools:

- **`'`** (quote) — `'x` is the symbol `x`, not its value.
- **`` ` ``** (syntax-quote) — like quote, but namespace-qualifies symbols and lets you splice in values.
- **`~`** (unquote) — inside a syntax-quote, evaluate this and splice the value in.
- **`~@`** (unquote-splicing) — splice in a sequence, removing one level of nesting.
- **`gensym`** / **`x#`** — generate a unique symbol so your macro doesn't capture user-bound names.

Example — a trivial `when` that returns `nil` instead of evaluating its body if a condition is false:

```clojure
(defmacro my-when [test & body]
  `(if ~test (do ~@body) nil))
```

Inspect what a macro expands to with `macroexpand-1`:

```clojure
(macroexpand-1 '(my-when (pos? x) (println x) x))
;; => (if (pos? x) (do (println x) x) nil)
```

**Hygiene.** If your macro introduces a local binding (a `let`, a function parameter), use a `gensym`ed name so you don't shadow names the caller wrote. The `name#` reader-shorthand auto-gensyms inside a syntax-quote:

```clojure
(defmacro my-or [a b]
  `(let [v# ~a]
     (if v# v# ~b)))
```

The `v#` becomes a unique symbol like `v__1234__auto__`. You'd never accidentally collide with a user's `v`.

**When to write a macro.** Not often. If a function works, use a function — macros don't compose, can't be `apply`d, and obscure stack traces. Write a macro when you need to control *evaluation* (don't evaluate the second arg unless...) or *binding* (introduce a name visible in the body).

> **By this point, you've learned enough to complete Kata 12 (`when-let*`).** Sketch the expansion first — what should `(when-let* [a 1, b 2] (+ a b))` turn into? It's a nest of `when-let`s. Recurse over the binding pairs at macroexpansion time. Use auto-gensyms or explicit `gensym` for any temporaries.

---

## 20. The capstone — writing an interpreter

By the time you sit down with Kata 13, you have everything. A tree-walking interpreter for a Lisp-y language is a perfect closer because:

- Source code is already a Clojure data structure — no parser needed.
- Self-evaluating values are obvious — numbers, strings, keywords, etc. return themselves.
- Symbol lookup is a map lookup (`env`).
- Composite forms dispatch on the first element — exactly the multimethod-or-cond pattern you've seen.
- `let`'s sequential bindings, `do`'s sequence-of-expressions, `if`'s conditional — each is a few lines of recursive `evaluate`.

The shape:

```clojure
(defn evaluate [env expr]
  (cond
    (symbol? expr)  (lookup env expr)
    (seq? expr)     (evaluate-list env expr)   ; cond on (first expr)
    :else           expr))                      ; self-eval
```

The lessons from every prior kata show up:
- `cond` for dispatch (§3)
- destructuring of forms (§4)
- `reduce` over args (§6)
- `ex-info` for unbound symbols / unknown forms (§16)
- sequential `let` is a left fold over bindings (§15, §16)

> **By this point, you've learned enough to complete Kata 13 (interpreter).** This is the one where it all clicks. Write it; you'll know Clojure when you're done.

---

## Going further

- **[Community Clojure Style Guide](https://github.com/bbatsov/clojure-style-guide)** — skim it, then re-read after every 2–3 katas; you'll notice things you didn't before.
- **clojure.core** has ~600 functions. You don't need all of them, but skimming the [cheatsheet](https://clojure.org/api/cheatsheet) once is a high-ROI hour.
- **REPL-driven development**: get a REPL connected to your editor (CIDER for Emacs, Calva for VS Code, Cursive for IntelliJ). Send forms to the REPL as you write — don't reload files. This is the workflow that makes Clojure productive.
- **Joy of Clojure** (Fogus & Houser) or **Programming Clojure** (Halloway & Bedra) when you want a book. Skip "Clojure for the Brave and True" — fun, but you're past its level.

Functional programming will feel slow for two weeks and faster than anything you've used after a month. Stick with it through the awkward middle.
