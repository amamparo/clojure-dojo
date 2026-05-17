## 0. Mental model

Three ideas to absorb first; the rest is mechanical.

**Code is data.** A Clojure program is a tree of lists, vectors, and maps; the compiler reads and evaluates that tree. There is no separate AST — the source *is* the tree. Hence macros are tractable: a macro is a function from tree to tree.

**Values are immutable.** A vector, map, or set, once constructed, cannot change. `assoc`, `conj`, etc. return *new* collections that structurally share most of their innards with the old (cheap — O(log32 n) for the persistent data structures). This feels limiting at first; within a week it has removed a whole class of bug.

**Functions are the abstraction.** Where other languages reach for a class, an object, or a loop, Clojure reaches for a function. State, when genuinely needed, lives in explicit reference types (atoms, refs, agents) you `deref` to read.

You're on a host with a deep library ecosystem; interop is one character of syntax away.



## 1. REPL-driven development

In most languages a REPL is an afterthought — a scratch console you rarely open. In Clojure it is first-class. It is *how you work*: you keep one running and build the program inside it.

So: start a REPL, connect your editor to it, and leave it running. As you write code, send each form to it — a function, an expression to try that function, a test. It takes effect immediately in the running program, and you see the result at once. Change it, send it again. Definitions and state accumulate; you rarely restart or reload a whole file.

The REPL is also where you look things up — `(doc f)`, `(source f)`, `(apropos "...")` — and where you keep throwaway experiments, in a `(comment ...)` block that is ignored on load.

This is the Clojure workflow, not a nicety. If it is new to you, getting fluent in it is as much the point of this repo as the katas.



## 2. Syntax — the whole language in 5 minutes

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

**Truthiness:** `nil` and `false` are falsy. Everything else is truthy — including `0`, `""`, and empty collections. This is one of the most common bug sources for newcomers.



## 3. The built-in data types

| Type | Literal | Notes |
|---|---|---|
| Long | `42` | 64-bit integer by default |
| Double | `1.5` | double-precision float |
| Ratio | `1/3` | exact rationals, just work |
| BigInt | `42N` | arbitrary precision |
| String | `"hi"` | native immutable strings |
| Character | `\a` | a single character |
| Boolean | `true`, `false` | |
| Nil | `nil` | absence of a value |
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



## 4. Control flow

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



## 5. `let` and destructuring

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



## 6. Functions in depth

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

Three rules, in order:

1. Prefer the bare function when it works — `(filter even? xs)`, not `(filter #(even? %) xs)`.
2. Reach for `#(…)` for short single-form bodies that actually do something — `#(* % %)`.
3. Reach for `fn` for anything multi-form, or where naming the parameter helps the reader.

Functions are values: pass them, return them, store them. This will feel like the anonymous functions you've used before, but used vastly more. The standard library is built around higher-order functions; you'll use them constantly.



## 7. Sequences — `map`, `filter`, `reduce`

The seq abstraction is Clojure's iterator protocol. Almost every collection can be viewed as a seq, and the core sequence functions work on all of them uniformly.

```clojure
(map inc [1 2 3])              ; => (2 3 4)
(filter even? (range 10))      ; => (0 2 4 6 8)
(reduce + 0 [1 2 3 4])         ; => 10
(reduce + [1 2 3 4])           ; => 10 (uses first elem as init)
```

`range` produces a (lazy) seq of integers. `(range 5)` → `(0 1 2 3 4)`. `(range 1 6)` → `(1 2 3 4 5)`.

`for` is a list comprehension, not a loop:

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



> ## Complete kata 1 ("FizzBuzz") before continuing



## 8. Threading macros

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

Rule of thumb: data-shaped operations (`assoc`, `update`, host method calls) go in `->`; sequence operations go in `->>`. Most Clojure pipelines you'll write are `->>`.

`as->` lets you name the intermediate value when neither position fits.



## 9. Working with maps

Maps are the workhorse data structure. Learn these cold:

```clojure
(get m :k)            ; lookup, nil if missing
(get m :k :default)   ; with default
(:k m)                ; same as (get m :k); idiomatic for keyword keys
(m :k)                ; works too, but errors if m is nil — avoid

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



## 10. Namespaces and `clojure.string`

A namespace is a unit of code. The first form in every file declares it and any imports:

```clojure
(ns katas.kata-03-anagrams
  (:require [clojure.string :as str]))
```

You then call `str/lower-case`, `str/split`, etc. The conventional aliases are worth memorising: `str`, `set`, `io`, `json`, `time`. Mismatched aliases across a codebase is a small but real friction.

`:require :refer [foo bar]` pulls specific names in directly. Use it sparingly — explicit aliasing tells the reader where a function comes from.

Use `clojure.string/...` for string ops, not host interop, unless interop is clearly more idiomatic. E.g. `str/lower-case` over `.toLowerCase`.



## 11. Sorting

```clojure
(sort [3 1 2])                       ; => (1 2 3)
(sort > [3 1 2])                     ; => (3 2 1)
(sort-by :age users)                 ; key fn
(sort-by (juxt :last :first) users)  ; multi-key
```

Custom comparators: a 2-arg fn returning negative/zero/positive, or a 2-arg predicate (`<`, `>`).

**Multi-key sorts.** `juxt` packs N key functions into one that returns a vector, and Clojure compares vectors element-wise — so `(sort-by (juxt :last :first) users)` sorts by last name, breaking ties by first name. The same shape works on any data with more than one sort key.

**Mixed directions.** Comparators don't support per-key direction natively, but you can negate a numeric key to flip its order:

```clojure
(sort-by (juxt #(- (:score %)) :name) results)
;; score descending, name ascending
```

**Map entries** are key/value pairs you can destructure or call `key`/`val` on:

```clojure
(sort-by val (frequencies "hello"))   ; sort entries by their count
(sort-by key (frequencies "hello"))   ; sort entries by their letter
```



> ## Complete kata 2 ("word frequencies") before continuing



## 12. `group-by`

```clojure
(group-by odd? [1 2 3 4 5])
;; => {true [1 3 5], false [2 4]}

(group-by :role users)
;; => {:admin [...], :user [...]}
```

The pattern: pick a *canonical form* (a key function), let `group-by` bucket things, then post-process. For anagrams, the canonical form of a word is its sorted lowercased letters.



> ## Complete kata 3 ("anagrams") before continuing



## 13. More sequence operations

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



> ## Complete kata 4 ("RLE") before continuing



## 14. Laziness in earnest

You've been using lazy seqs (everything `map`/`filter`/`range` produces). Now you'll *build* one.

`lazy-seq` wraps a body so it's not evaluated until something pulls on it. The simplest recursive shape:

```clojure
(defn naturals-from [n]
  (lazy-seq (cons n (naturals-from (inc n)))))

(take 5 (naturals-from 1))  ; => (1 2 3 4 5)
```

That recursion never blows the stack because `lazy-seq` defers the recursive call until needed. Each element materialises on demand.

A more interesting case is when each step needs the *previous* values. Fibonacci passes its state along as function arguments:

```clojure
(defn fibs
  ([] (fibs 0 1))
  ([a b] (lazy-seq (cons a (fibs b (+ a b))))))

(take 8 (fibs))  ; => (0 1 1 2 3 5 8 13)
```

The state — whatever the recursion needs to produce the next element — rides along as args. This pattern generalises: pass an index, an accumulator, a predicate, a previous result, whatever the next step requires.

A `def` can hold a lazy seq directly — handy for "the seq of all X":

```clojure
(def all-evens (filter even? (range)))
(take 5 all-evens)   ; => (0 2 4 6 8)
```

Two caveats with lazy seqs:
1. Side effects inside laziness happen *when realised*, not when defined. Don't rely on ordering or "did it run."
2. Holding the head of a long lazy seq while walking it can pin the whole thing in memory ("head retention"). Usually not a problem; just be aware.



> ## Complete kata 5 ("primes") before continuing



## 15. Sets — both predicate and data

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



> ## Complete kata 6 ("Game of Life") before continuing



## 16. Recursion — `loop`/`recur`

The host doesn't optimise tail calls automatically. Clojure exposes them via the explicit `recur` form, which jumps back to an enclosing `loop` or function with new bindings:

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



> ## Complete kata 7 ("Roman numerals") before continuing



## 17. Recursion — state machines

Some loops aren't a fold: the next move depends on what was just consumed — a *state machine*. The defining property is **variable lookahead** — depending on what you see, you consume one item, or two, or three.

Example. You're walking a stream of commands. Most commands stand alone, but `:goto` takes the *next* item in the stream as its argument:

```clojure
(defn run-commands [cmds]
  (loop [cmds cmds
         pos 0
         path [0]]
    (let [[c & rest] cmds]
      (cond
        (nil? c)        path
        (= c :forward)  (recur rest (inc pos) (conj path (inc pos)))
        (= c :back)     (recur rest (dec pos) (conj path (dec pos)))
        (= c :goto)     (let [[target & rest2] rest]
                          (recur rest2 target (conj path target)))))))

(run-commands [:forward :forward :back :goto 10 :back])
;; => [0 1 2 1 10 9]
```

Three things to notice:

- **Variable consumption.** Most branches `recur` with `rest` — one item consumed. The `:goto` branch destructures one more value off the front and `recur`s with `rest2` — two items consumed. A `reduce` over `cmds` can't express that.
- **State as loop bindings.** `pos` and `path` ride along explicitly; nothing mutates.
- **Termination on empty.** `(nil? c)` catches the end of the stream and returns the accumulator.

The same shape shows up whenever the parsing rule depends on the current token — escape sequences in strings, opcodes with immediate operands, scoring systems where some events grant lookahead.



> ## Complete kata 8 ("bowling") before continuing



## 18. Identity vs value — atoms, `ex-info`

Until now everything has been pure; real systems hold state. Clojure separates the two: a *value* is immutable; an *identity* — a thing that holds different values over time — is an explicit reference you `deref`.

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
;; WRONG — read and write are separate steps
(reset! cache (assoc @cache user-id user-data))

;; RIGHT — the read+update happens atomically inside swap!
(swap! cache assoc user-id user-data)
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

A note on the test files. Most of the suite uses `(is (= expected actual))` — plain equality, the workhorse. A few tests also use `(is (match? ...))` and `(is (thrown-match? ex-class data-shape ...))`. Those come from the **matcher-combinators** library; `match?` on a map matches *partially* — extra keys in the actual value are fine. That's useful for asserting on `ex-data` (which often carries more than the test needs) and for the history vector (which may grow new fields over time without breaking the test). Plain `=` requires exact equality. Use whichever expresses what you actually mean.



> ## Complete katas 9 ("bank account"), 10 ("assignments"), and 11 ("my-memoize") before continuing



## 19. Dispatch — maps of functions

Before reaching for the heavy polymorphism tools, notice that a map of functions is often all you need:

```clojure
(def conversions
  {:c->f  #(+ 32 (* % 9/5))
   :f->c  #(* (- % 32) 5/9)
   :m->ft #(* % 3.28084)
   :ft->m #(/ % 3.28084)})

((conversions :c->f) 100)   ; => 212.0
```

That's a dispatch table — a map whose values are the functions that handle each case. To wire it into something larger, look up and call:

```clojure
(defn convert [conv-key value]
  (if-let [f (conversions conv-key)]
    (f value)
    (throw (ex-info "Unknown conversion" {:key conv-key}))))
```

It composes naturally with `reduce` when you want to thread a value through a sequence of operations chosen at runtime:

```clojure
(reduce (fn [v k] ((conversions k) v))
        0
        [:c->f :f->c :c->f])
```

When the inputs are strings but your dispatch table uses symbols (or vice versa), normalise with `symbol` or `name`:

```clojure
(symbol "+")    ; => +
(name '+)       ; => "+"
```



> ## Complete kata 12 ("RPN") before continuing



## 20. Real polymorphism — protocols, records, multimethods

Two complementary tools. Both are open in different axes.

**Protocols** group methods by *type*. Closed set of methods, open set of types implementing them — like an interface, but you can add implementations to types you don't own.

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
- All implementations cluster around a *type*? Protocol. (Faster, easier to navigate.)
- All implementations cluster around a *behaviour* and the dispatch key isn't a host type? Multimethod.
- Hierarchical dispatch (`derive`/`isa?`)? Only multimethods.

**One important caveat** — most Clojure code, most of the time, uses plain maps and never reaches for `defrecord`. Maps compose with the whole standard library, can grow new fields without breaking callers, survive REPL reloads, and don't need a positional constructor. Reach for a record when you actually need (a) protocol dispatch tied to a named type, or (b) measured performance from typed field access. Kata 13 introduces records because protocols are records' canonical implementation type and you should have seen the tool. You will not reach for it often.



> ## Complete kata 13 ("shapes") before continuing



## 21. Macros — code as data, finally cashed in

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

**Recursive expansion.** Macros can call themselves; each recursion happens at compile time, leaving a flat nested form. A real `and` takes any number of arguments:

```clojure
(defmacro my-and
  ([] true)
  ([x] x)
  ([x & more]
   `(if ~x (my-and ~@more) ~x)))
```

When a macro takes a *structured* input — a bindings vector, a pair-list, a nested form — you destructure that input the same way you'd destructure data, and recurse on the rest. The mechanism is identical; only the shape of the input differs.

**When to write a macro.** Not often. If a function works, use a function — macros don't compose, can't be `apply`d, and obscure stack traces. Write a macro when you need to control *evaluation* (don't evaluate the second arg unless...) or *binding* (introduce a name visible in the body).



> ## Complete kata 14 ("when-let*") before continuing



## 22. The capstone — writing an interpreter

By the time you sit down with Kata 15, you have everything. A tree-walking interpreter for a Lisp-y language is a perfect closer because:

- Source code is already a Clojure data structure — no parser needed.
- Self-evaluating values are obvious — numbers, strings, keywords return themselves.
- Symbol lookup is a map lookup.
- Composite forms dispatch on their first element — the pattern you've seen with `cond`, multimethods, and dispatch tables.
- The special forms you need (`if`, `let`, `do`, `def`, `fn`) are each a few lines of recursive evaluation.

One thing worth stating up front, because it's a definition not a hint: in `let`, bindings extend the environment that the body sees. Each binding is visible to every subsequent binding *and* to the body. The shadow doesn't leak — the extended env exists only inside the `let` form.

A second one: `def` needs to extend the env beyond a single form. The simplest way to model this is to let the top-level env be an atom (and plain maps for nested scopes inside `let`/`fn` bodies), so `def` can `swap!` into it. Closures then capture the env reference, which means a function defined via `def` will see future `def`s — that's how recursion through `def` works.

Everything else falls out of the techniques from the prior katas. Trust them.



> ## Complete kata 15 ("the interpreter") before continuing



## Going further

- **[Clojure cheatsheet](https://clojure.org/api/cheatsheet)** — every core function on one page, grouped by what they operate on. (You won't need most of them.) **[clojuredocs.org](https://clojuredocs.org)** has community-contributed examples for each.
- **Joy of Clojure** (Fogus & Houser) or **Programming Clojure** (Miller, Halloway & Bedra) when you want a book.
- Rich Hickey's talks — "Simple Made Easy", "The Value of Values", "Hammock Driven Development" — for the reasons the language is shaped the way it is.
- **[4ever-clojure](https://4clojure.oxal.org/)** when you want more problems.

It will feel slow for the first few weeks. That's normal; fluency comes before speed.
