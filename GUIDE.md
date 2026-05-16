# Clojure for the Experienced Imperative Programmer

A from-scratch tour of Clojure syntax and concepts, ordered so that each
section gives you exactly what you need to clear the next kata. It assumes
you are fluent in Python and Java but have never done functional
programming. Where it helps, concepts are anchored to their Python/Java
analogues — and to where those analogues *break down*.

Work it top to bottom. When you hit a **"you can now do Kata N"** marker,
stop reading and go solve that kata. The katas are the point; this is
scaffolding.

---

## 0. The five things that will trip you up

Before any syntax, internalise these. Every confusion you'll have in the
first week traces back to one of them.

1. **Code is data.** `(f a b)` is a *list* whose first element is the
   function and whose rest are arguments. The language is written in its
   own primary data structure. This is why parentheses go *outside* the
   function name: `(+ 1 2)`, never `+(1, 2)`. There are no commas between
   arguments (commas are whitespace in Clojure).

2. **Everything is an expression.** There are no statements. `if`
   returns a value. A function body returns the value of its last
   expression — there is no `return` keyword. Think Java's `cond ? a : b`
   everywhere, never `if (x) { ... }`.

3. **Data is immutable.** `conj`-ing onto a vector doesn't change the
   vector; it returns a *new* vector that structurally shares memory with
   the old one. There is no `x.append(y)`. This is not a performance
   compromise you tolerate — it is the feature that makes the rest of the
   language tractable. Stop looking for the mutating version. There isn't
   one (until §9, where mutation is explicit and rare).

4. **You transform data, you don't manage objects.** The Java instinct
   is "what class models this?" The Clojure instinct is "this is a map /
   vector / set; what sequence of pure functions turns the input into the
   output?" Most of your "domain model" will be plain maps and vectors.

5. **The REPL is the unit of work.** You do not write a file and run it.
   You evaluate one form at a time in a live REPL, building the answer
   incrementally. `just test-kata N` runs the tests, but you should be
   evaluating expressions long before the tests pass.

---

## 1. Atoms of syntax, functions, conditionals, arithmetic

### Literals

```clojure
42            ; long
3.14          ; double
"hello"       ; string (always double quotes; single quote means
              ;          something else entirely — see §12)
\a            ; the character a
true false    ; booleans
nil           ; null / None. Distinct from false.
:status       ; a keyword — an interned, self-evaluating symbol.
              ; Think Java enum constant or Python string-used-as-key,
              ; but cheaper to compare and idiomatic as a map key.
```

### Calling functions

A call is a list: `(operator arg1 arg2 ...)`. The operator comes first —
*including arithmetic*, which is just a function:

```clojure
(+ 1 2 3)        ; => 6        ; + takes any number of args
(< 1 2 3)        ; => true     ; chained comparison, like 1 < 2 < 3
(mod 10 3)       ; => 1        ; modulo
(rem -1 3)       ; => -1       ; remainder (differs from mod on negatives)
(quot 7 2)       ; => 3        ; integer division
(inc 4) (dec 4)  ; => 5, 3     ; prefer these over (+ x 1)
(zero? 0) (pos? 3) (neg? -1)   ; => true true true ; prefer over (= x 0)
```

### Defining a function

```clojure
(defn add
  "Optional docstring."
  [a b]
  (+ a b))
```

`[a b]` is the parameter vector. The body is `(+ a b)`; its value is
returned. No `return`.

`defn` is for named top-level functions. An anonymous function is `fn`:

```clojure
(fn [x] (* x x))
#(* % %)          ; shorthand: % is the single arg, %1 %2 for more.
                  ; Don't nest these and don't use them for multi-form
                  ; bodies — use fn for that.
```

### Conditionals

```clojure
(if (pos? x) "positive" "non-positive")   ; ternary. else-branch optional
                                          ; (defaults to nil).

(when (pos? x)        ; single-branch if; body is an implicit "do"
  (println "pos")
  :ok)

(cond                 ; like a Java if/else-if chain or Python elif
  (zero? x) "zero"
  (pos? x)  "positive"
  :else     "negative")     ; catch-all key is :else by convention
```

`cond` evaluates test/expression pairs top to bottom, returns the
expression for the first truthy test. **Truthiness is simple:** only
`false` and `nil` are falsey. `0`, `""`, `[]` are all truthy (unlike
Python).

### Producing a sequence

Two ways to turn a range of numbers into a sequence of results:

```clojure
(range 5)              ; => (0 1 2 3 4)   ; half-open, like Python range
(range 1 4)            ; => (1 2 3)

(map inc (range 5))    ; => (1 2 3 4 5)   ; like Python map / list comp
(map (fn [i] (* i i))
     (range 5))        ; => (0 1 4 9 16)

(for [i (range 5)]     ; list comprehension. Returns a lazy seq.
  (* i i))             ; => (0 1 4 9 16)
```

`map` applies a function across a collection. `for` is a comprehension
(it is *not* a loop — it builds a sequence). Wrap a sequence in `vec` to
get a vector when a vector is what's required:

```clojure
(vec (map inc (range 3)))   ; => [1 2 3]
```

> **You can now do Kata 1 (FizzBuzz).** You have `defn`, `cond`, `mod`,
> `range`, `map`/`for`, and `vec`. That is the whole kata. Go.

---

## 2. Collections, `reduce`, threading, recursion

### The four collections

```clojure
[1 2 3]              ; vector  — indexed, grows at the end. Your default.
'(1 2 3)             ; list    — linked list, grows at the front.
{:a 1 :b 2}          ; map     — like Python dict / Java Map.
#{1 2 3}             ; set     — like Python set.
```

All immutable. All are *functions of their contents* in useful ways
(more later). Build them up with `conj` ("conjoin"):

```clojure
(conj [1 2] 3)       ; => [1 2 3]    ; vector: adds at end
(conj '(1 2) 3)      ; => (3 1 2)    ; list: adds at front
(conj #{1 2} 2)      ; => #{1 2}     ; set: idempotent
(assoc {:a 1} :b 2)  ; => {:a 1 :b 2}; map: add/replace a key
(get {:a 1} :a)      ; => 1
(:a {:a 1})          ; => 1          ; keyword acts as its own getter.
                                     ; This is the idiomatic form.
```

None of these mutate. `(conj v 3)` returns a new vector; `v` is
unchanged.

### `reduce` — the fold

`reduce` is the workhorse you'll reach for constantly. It threads an
accumulator through a collection:

```clojure
(reduce + 0 [1 2 3 4])        ; => 10
;; equivalent to ((((0 + 1) + 2) + 3) + 4)

(reduce (fn [acc x] (conj acc (* x x)))
        []
        [1 2 3])              ; => [1 4 9]
```

The reducing function takes `[accumulator next-element]` and returns the
next accumulator. This single pattern subsumes most loops you'd write in
Java. (`map`, `filter`, `frequencies`, etc. are all expressible as
`reduce`; often a more specific function is clearer, so reach for the
specific one when it exists.)

### Threading macros

Nested calls read inside-out, which is painful. Threading macros undo
the nesting:

```clojure
(-> 5 inc (* 2) (- 1))
;; thread-first: result becomes the FIRST arg of the next form
;; => (- (* (inc 5) 2) 1) => 11

(->> (range 10) (filter even?) (map inc) (reduce +))
;; thread-last: result becomes the LAST arg of the next form
;; => 25
```

`->` for "operate on a thing" (maps, single values). `->>` for "operate
on a sequence" (`map`/`filter`/`reduce` take the collection last). The
round-trip property in Kata 2 is literally written
`(-> n int->roman roman->int)`.

### Recursion and `loop`/`recur`

Clojure has no mutable loop counter. You recurse. For self-recursion
that doesn't grow the stack, use `recur`:

```clojure
(defn countdown [n]
  (when (pos? n)
    (println n)
    (recur (dec n))))         ; tail call; reuses the stack frame

(defn sum-to [n]
  (loop [i n, acc 0]          ; loop establishes a recur target with
    (if (zero? i)             ;   initial bindings
      acc
      (recur (dec i) (+ acc i)))))   ; recur rebinds i and acc
```

`loop`/`recur` is the explicit accumulator pattern: the Clojure
equivalent of a `for` loop with mutable locals, but the "mutation" is
just rebinding on the next iteration. `recur` must be in tail position.

### Lookup tables as data

A "table" is just a vector of pairs or a map — data, not a `switch`:

```clojure
(def roman-table
  [[1000 "M"] [900 "CM"] [500 "D"] [400 "CD"]
   [100 "C"] [90 "XC"] [50 "L"] [40 "XL"]
   [10 "X"] [9 "IX"] [5 "V"] [4 "IV"] [1 "I"]])
```

`def` binds a name to a value at the top level (think `static final`).
Iterating this table with `reduce` while subtracting from `n` is the
spine of the int→roman direction.

> **You can now do Kata 2 (Roman numerals).** Lookup table + `reduce`
> (or `loop`/`recur`) + threading for the round-trip. Both directions
> are folds over a table.

---

## 3. Maps as your primary data structure; sorting

### Maps are the domain model

In Java you'd write a `WordCount` class. In Clojure the answer is a map
`{word count}`. Building one:

```clojure
(reduce (fn [m word] (update m word (fnil inc 0) ))
        {}
        ["a" "b" "a"])           ; => {"a" 2, "b" 1}
```

`update` applies a function to the value at a key: `(update m k f)` is
`(assoc m k (f (get m k)))`. `fnil` supplies a default for the first
call when the key is absent (`(fnil inc 0)` means "inc, but treat nil as
0"). This exact pattern *is* a word-frequency counter.

And because it is so common, it's built in:

```clojure
(frequencies ["a" "b" "a"])      ; => {"a" 2, "b" 1}
```

### String wrangling

```clojure
(require '[clojure.string :as str])    ; in real code this goes in the
                                       ; ns form; see §5

(str/lower-case "Hello")               ; => "hello"
(str/split "a b  c" #"\s+")            ; => ["a" "b" "c"]  (#"..." is a
                                       ;   regex literal, java.util.regex)
(str/trim "  hi ")                     ; => "hi"
(str/replace "a.b" #"[.,]" "")         ; strip punctuation
```

`#"\s+"` is a regex literal. Splitting, lower-casing, and trimming
edge punctuation is the normalisation step of Kata 3.

### Sorting with comparators

```clojure
(sort [3 1 2])                         ; => (1 2 3)
(sort-by count ["bbb" "a" "cc"])       ; => ("a" "cc" "bbb")
(sort-by (juxt second first)           ; sort by 2nd elem, then 1st
         [["a" 1] ["b" 1] ["c" 2]])

;; Custom comparator: negative/zero/positive, like Java Comparator.
(sort (fn [x y] (compare y x)) [1 2 3]) ; => (3 2 1)  (descending)
```

`compare` is the universal three-way comparator (works on strings,
numbers, vectors lexicographically). `juxt` builds a function that
returns a vector of several functions applied to the same input —
perfect for "sort by count descending, then word ascending". Take the
first `n` with `(take n sorted)`.

> **You can now do Kata 3 (Word frequencies).** Normalise with
> `clojure.string`, count with `reduce`/`frequencies`, rank with
> `sort-by` + a custom comparator + `take`.

---

## 4. Grouping, canonical forms, sequence order

```clojure
(group-by odd? (range 6))
;; => {false [0 2 4], true [1 3 5]}

(group-by count ["a" "bb" "cc" "d"])
;; => {1 ["a" "d"], 2 ["bb" "cc"]}
```

`group-by` applies a key function to each element and collects elements
into a map of `key -> vector-of-elements`, **preserving input order
within each group**. The Java equivalent is
`Collectors.groupingBy`.

The trick for anagrams: pick a **canonical form** — a value that is
equal for inputs you consider "the same". Sorted, lower-cased letters
work: `"listen"` and `"silent"` both canonicalise to `(\e \i \l \n \s
\t)`.

```clojure
(sort (str/lower-case "Silent"))       ; => (\e \i \l \n \s \t)
(= (sort (str/lower-case "Listen"))
   (sort (str/lower-case "Silent")))   ; => true  → they're anagrams
```

`=` is deep structural equality (like Python `==`, *not* Java `==`):
`(= [1 2] [1 2])` is `true`. Two sequences with the same elements in the
same order are equal.

**Order caveat:** maps are unordered. If a kata requires groups "in the
order of their first member", don't read them off the `group-by` map's
key order — `group-by` *does* preserve input order in its values, but to
order the *groups* themselves you typically reduce over the input
yourself, or use the fact that `group-by` keeps first-seen order for
many inputs and then re-sort by first occurrence. The safe mental model:
**maps don't promise key order; sequences do.**

> **You can now do Kata 4 (Anagrams).** Canonical form via sorted
> lower-cased chars; `=` for the predicate; `group-by` for grouping;
> mind the ordering requirement.

---

## 5. The seq abstraction; sequence-shaping functions; the `ns` form

### Everything is a "seq"

Strings, vectors, lists, lazy ranges, even maps (as pairs) can all be
viewed through one interface: the **seq**. Write one function over seqs
and it works on all of them. This is why Kata 5's `encode` must work on
a string *and* a vector *and* a lazy seq with no special-casing.

```clojure
(seq "abc")          ; => (\a \b \c)
(seq [1 2])          ; => (1 2)
(first "abc")        ; => \a
(rest "abc")         ; => (\b \c)
(seq "")             ; => nil    ; empty seq is nil — this is "nil-punning"
(when (seq xs) ...)  ; idiomatic "if not empty"
```

### Shaping functions you need

```clojure
(partition-by identity "aaabbc")
;; => ((\a \a \a) (\b \b) (\c))   ; splits into runs where key fn changes

(map (fn [run] [(count run) (first run)])
     (partition-by identity "aaabbc"))
;; => ([3 \a] [2 \b] [1 \c])      ; that's run-length ENCODE, basically

(repeat 3 \a)        ; => (\a \a \a)       ; n copies
(mapcat (fn [[n x]] (repeat n x))
        [[3 \a] [1 \b]])
;; => (\a \a \a \b)  ; mapcat = map then concat; that's DECODE
```

`partition-by` cuts a seq into maximal runs where a key function's value
is constant — tailor-made for run-length encoding. `mapcat` maps a
function that returns sequences and concatenates the results (a flat-map)
— tailor-made for decoding. `repeat` produces n copies.

### Pure functions

Every function so far takes data and returns data, touching nothing
else: no I/O, no mutation, same input → same output. That is a **pure
function**. Kata 5 asks for the round-trip property
`(= (vec xs) (decode (encode xs)))`; purity is what makes such
properties hold and testable.

### The `ns` form (do this for real now)

The `(require ...)` calls above were REPL shorthand. In a source file,
declare dependencies in the namespace form at the top — Kata 4's file
already shows the shape:

```clojure
(ns katas.kata-05-run-length-encoding
  (:require [clojure.string :as str]))
```

`:require ... :as` is preferred over `:refer`. Aliases are conventional
(`clojure.string` → `str`, `clojure.set` → `set`).

> **You can now do Kata 5 (Run-length encoding).** `partition-by` to
> encode, `mapcat` + `repeat` to decode, the seq abstraction so one
> implementation covers strings/vectors/seqs, purity for the round-trip.

---

## 6. Laziness and infinite sequences

Clojure sequences are **lazy**: elements are computed only when
demanded. This is why `(range)` with no argument — an *infinite*
sequence — doesn't hang:

```clojure
(take 10 (range))            ; => (0 1 2 3 4 5 6 7 8 9)
(take 5 (map #(* % %) (range))) ; => (0 1 4 9 16)
(take-while #(< % 20) (range))  ; => (0 1 2 ... 19)
(nth (range) 24)                ; => 24
```

You build your own infinite seq with `lazy-seq`, which wraps a body so
it isn't evaluated until forced:

```clojure
(defn nats-from [n]
  (lazy-seq (cons n (nats-from (inc n)))))

(take 3 (nats-from 7))       ; => (7 8 9)
```

`cons` prepends an element to a seq. `(lazy-seq (cons head (recurse)))`
is the canonical shape of an infinite generator: produce one element,
defer the rest. For primes, generate candidates lazily and keep the ones
not divisible by any smaller prime — trial division against
already-produced primes.

### `def` holding a seq vs. a function returning one

```clojure
(defn primes-fn [] (lazy-seq ...))   ; call it: (take 5 (primes-fn))
(def  primes     (lazy-seq ...))     ; it IS the seq: (take 5 primes)
```

Kata 6 wants `primes` to be a `def` — a value that *is* the lazy
sequence (and is realised once and cached as you walk it), not a
function you call. This distinction (identity of a value vs. a procedure
that makes one) is worth pausing on; it recurs in §9.

> **You can now do Kata 6 (Lazy primes).** `prime?` is a predicate;
> `primes` is a `lazy-seq`-built infinite `def`; consume with `take` /
> `take-while` / `nth`.

---

## 7. Sets as data; set operations

Some problems are clearest when the data structure *is* a set. Conway's
Game of Life on an infinite grid: represent the world as the **set of
live coordinates** `#{[0 0] [1 0] [2 0]}`. Dead cells aren't stored;
the grid is implicitly infinite because you never allocated a grid.

```clojure
(require '[clojure.set :as set])

(contains? #{[0 0] [1 0]} [1 0])     ; => true
(#{[0 0] [1 0]} [1 0])               ; => [1 0]   ; set is a fn of membership
(set/union #{1 2} #{2 3})            ; => #{1 2 3}
(set [1 1 2])                        ; => #{1 2}   ; build a set from a seq
```

The algorithm reuses tools you already have. For each live cell, expand
to its 8 neighbours; `mapcat` that over all live cells to get every
candidate (with multiplicity); `frequencies` turns multiplicity into a
neighbour-count map; then keep cells satisfying survive/birth rules.

```clojure
(frequencies (mapcat neighbours alive))
;; => {[x y] count, ...}  ; how many live cells touch each coordinate
```

That `frequencies`-over-`mapcat`-then-filter shape is the entire `step`
function.

> **You can now do Kata 7 (Game of Life).** Set-as-world; `mapcat`
> neighbours; `frequencies` to count; filter by the rules; return a new
> set.

---

## 8. Destructuring; recursion as a state machine

### Destructuring

Pull values out of collections positionally or by key, in any binding
position (`let`, function params, `loop`):

```clojure
(let [[a b & more] [1 2 3 4]]      ; a=1, b=2, more=(3 4)
  [a b more])

(let [{:keys [x y]} {:x 1 :y 2}]   ; x=1, y=2  (by map key)
  (+ x y))

(defn dist [[x1 y1] [x2 y2]]       ; destructure args directly
  (Math/sqrt (+ (Math/pow (- x2 x1) 2)
                (Math/pow (- y2 y1) 2))))
```

`& more` is rest-args, like Python `*args`. `{:keys [x y]}` is the
keyword-key form. `(Math/sqrt x)` is Java interop — calling a static
method; `Math/PI` is a static field. (Interop is sugar:
`(.method obj args)`, `(Class. ctor-args)`.)

### Recursion as a state machine

Bowling scoring is a finite state machine: strike / spare / open frame,
ten frames, plus bonus rolls. Model it as recursion over the rolls with
explicit accumulators — the same `loop`/`recur` (or self-recursive
helper) pattern from §2, but now the "state" is "which frame am I in,
what's the score so far, what rolls remain":

```clojure
(defn score [rolls]
  (loop [rolls rolls, frame 1, total 0]
    (cond
      (> frame 10)        total
      (= 10 (first rolls))                 ; strike: 1 slot + 2-roll bonus
      (recur (rest rolls) (inc frame)
             (+ total 10 (first (rest rolls)) (second (rest rolls))))
      ;; ... spare and open-frame clauses ...
      )))
```

Each `recur` is a state transition. Destructuring the first few rolls
(`(let [[a b c] rolls] ...)`) keeps each clause readable.

> **You can now do Kata 8 (Bowling).** Destructuring + accumulator
> recursion + `cond` clauses encoding the strike/spare/open state
> machine.

---

## 9. Identity, state, atoms, and structured errors

So far: pure functions, immutable values, no mutation. Real programs
sometimes need *state that changes over time* — a bank balance. Clojure
separates two ideas Java conflates:

- a **value** is immutable (`{:balance 100}`);
- an **identity** is a stable reference whose value changes over time.

An **atom** is a managed, thread-safe identity:

```clojure
(def acct (atom {:balance 0 :history []}))

@acct                       ; deref: => {:balance 0 :history []}
(deref acct)                ; same thing

(swap! acct update :balance + 100)
;; applies (update old-value :balance + 100) atomically, stores & returns
;; the new value. The trailing ! marks a side-effecting fn.

(reset! acct {:balance 0 :history []})   ; replace unconditionally
```

`swap!` takes the atom and a function (plus extra args) and atomically
applies `(f current-value & args)`, retrying if another thread changed
it in between. **This is the whole concurrency story for Kata 9:** do
the read-and-update *inside* the `swap!` function. If you instead
`@acct` to read, compute, then `reset!`, you reintroduce the lost-update
race you came here to avoid.

### Structured errors

```clojure
(throw (ex-info "Insufficient funds"
                {:type :insufficient-funds :requested 50 :balance 10}))

;; catching:
(try
  (withdraw! acct 999)
  (catch clojure.lang.ExceptionInfo e
    (:type (ex-data e))))     ; => :insufficient-funds
```

`ex-info` creates an exception carrying a **data map** (retrievable with
`ex-data`) — far better than Java exception subclasses for the kata's
`{:type :invalid-amount}` style error contract. You'll validate the
amount and `throw` an `ex-info` before touching the atom.

> **You can now do Kata 9 (Bank account).** Account = `atom` over a map;
> `deref`/`@` to read; `swap!` with an in-function read+update for
> safety; `ex-info` for the error contract.

---

## 10. Data-driven dispatch

Kata 10 (RPN calculator) is a fold over a stack. The stack is a vector;
each token either pushes a number or transforms the stack.

```clojure
(reduce step [] tokens)      ; accumulator is the stack
```

The interesting part is dispatching on the token. The imperative
instinct is a giant `cond`/`case`. The Clojure idiom is **a map from
token to the function that implements it** — data, not control flow:

```clojure
(def ops
  {'+ (fn [stack] (let [[b a & r] (reverse stack)] ...))   ; sketch
   'dup  (fn [[& xs :as s]] (conj s (peek s)))
   'drop pop
   'swap (fn [s] ...)})

(get ops token)              ; the implementing fn, or nil if unknown
```

`peek`/`pop` are the vector-as-stack operations (top is the last
element). Look the token up in `ops`; if present, apply it to the stack;
if it's a number, push it; otherwise `throw` an `ex-info`
`{:type :unknown-token}`. Underflow (operator wants more operands than
the stack holds) is another `ex-info`.

This "map of functions" *is* polymorphism — open for extension by
`assoc`-ing new entries, no conditional to edit. (`case` is still the
right tool when branches are fixed compile-time constants; here the set
of operators is naturally a data table.)

> **You can now do Kata 10 (RPN).** `reduce` over a stack vector;
> dispatch via a map of token→fn; `peek`/`pop`; `ex-info` for
> underflow/unknown-token.

---

## 11. Polymorphism: protocols/records and multimethods

Two first-class polymorphism mechanisms, deliberately contrasted in
Kata 11.

### Protocols + records (dispatch on type)

A **protocol** is a named set of function signatures (like a Java
interface). A **record** is a map-like type with named fields that can
implement protocols (like a final class / data class):

```clojure
(defprotocol Shape
  (area [shape])
  (perimeter [shape]))

(defrecord Circle [r]
  Shape
  (area      [_] (* Math/PI r r))
  (perimeter [_] (* 2 Math/PI r)))

(defrecord Rectangle [w h]
  Shape
  (area      [_] (* w h))
  (perimeter [_] (* 2 (+ w h))))

(area (->Circle 1))          ; => 3.14159...
(perimeter (->Rectangle 3 4)); => 14
```

`(->Circle 1)` is the positional constructor `defrecord` generates. `_`
is the conventional name for an ignored parameter (here, `this` —
fields are in scope directly). Dispatch is on the **concrete type**:
closed set of methods, open set of types.

### Multimethods (dispatch on anything)

A **multimethod** dispatches on the value returned by an arbitrary
function of the arguments:

```clojure
(defmulti describe :kind)        ; dispatch fn = (:kind arg)

(defmethod describe :circle    [{:keys [r]}] (str "circle with radius " r))
(defmethod describe :rectangle [{:keys [w h]}] (str "rectangle " w " by " h))
(defmethod describe :default   [s] (str "unknown shape: " (:kind s)))

(describe {:kind :circle :r 2})  ; => "circle with radius 2"
```

Dispatch is on `(:kind shape)` — an open set of methods *and* types,
extensible by anyone, anywhere, by adding a `defmethod`. `:default` is
the fallback.

The trade-off, made concrete by doing the same domain twice: a protocol
groups all behaviours of one *type*; a multimethod groups all
implementations of one *behaviour* and can dispatch on arbitrary logic.
Extend along whichever axis you expect to grow.

> **You can now do Kata 11 (Shapes).** `defprotocol` + `defrecord` for
> Part A; `defmulti`/`defmethod` keyed on `:kind` for Part B.

---

## 12. Macros, quoting, and code-as-data realised

Recall §0.1: code *is* data. A **macro** is a function that runs at
*compile time*, taking unevaluated code (as data) and returning code to
be compiled in its place. This is how `when`, `cond`, `->`, `and` are
all defined — they couldn't be plain functions because their arguments
must *not* be evaluated eagerly (think Kata 12's requirement that later
binding forms are never evaluated after a `nil`).

### Quoting

```clojure
'(+ 1 2)             ; => (+ 1 2)   ; quote: the LIST, not 3.
'x                   ; => x         ; the symbol x, not its value
`(+ 1 ~x)            ; syntax-quote: like quote but auto-namespaces
                     ;   symbols and allows ~ (unquote) to splice values
`(list ~@xs)         ; ~@ unquote-splicing: inline a sequence's elements
```

- `'` (quote): "this code, as data, untouched."
- `` ` `` (syntax-quote): a template for building code.
- `~` (unquote): inside a syntax-quote, "evaluate this and drop the
  value in."
- `~@` (unquote-splicing): same, but splice a collection's elements in.

### Writing a macro

```clojure
(defmacro unless [test then]
  `(if (not ~test) ~then nil))

(macroexpand-1 '(unless false :ok))   ; => (if (not false) :ok nil)
```

`macroexpand-1` shows the code your macro produces — your primary
debugging tool. **Hygiene:** if your macro introduces a local binding,
generate a unique symbol so it can't collide with the user's names:

```clojure
(defmacro my-or [a b]
  `(let [t# ~a]              ; foo# auto-generates a unique gensym
     (if t# t# ~b)))
```

The `name#` suffix inside a syntax-quote auto-`gensym`s — the hygienic
way to introduce temporaries.

Kata 12 (`when-let*`) builds nested `when-let`s. You'll destructure the
binding vector pairwise, recurse to nest them, validate the vector has
even length (throwing `IllegalArgumentException` at macroexpansion
time — i.e. in the macro body itself, not in emitted code), and stay
hygienic. `macroexpand-1` on your examples is how you'll know it's
right.

> **You can now do Kata 12 (when-let\*).** Syntax-quote to template the
> nested `when-let`s, `~`/`~@` to splice bindings/body, gensym for
> hygiene, `macroexpand-1` to verify, a compile-time arity check.

---

## 13. Capstone: the tiny interpreter

Kata 13 introduces no new syntax. It asks you to *write `eval`* — which
is fitting, because every section above was secretly about this:

- code is data (§0, §12), so an expression is just a vector/list/symbol;
- dispatch on the first element of a composite form — a `case` or a map
  of fns (§10), one clause per special form (`do`, `let`, `if`, `+`…);
- `let` does **sequential** bindings where each RHS sees prior names —
  exactly the accumulator-over-bindings recursion from §8, threading an
  environment map (§3) instead of a score;
- self-evaluating literals fall straight through;
- unbound symbols and unknown forms `throw` `ex-info` with a `:type`
  (§9);
- the environment is an immutable map extended with `assoc` as you bind
  (§2, §3) — no mutation, even for a stateful-looking language.

The interpreter is the proof that the whole toolkit composes: pure
functions over immutable data, dispatched on shape, recursing with an
accumulator. If §0–§12 landed, Kata 13 is assembly, not invention.

> **You can now do Kata 13 (Interpreter).** Everything. That's the
> point of a capstone.

---

## Appendix: working effectively

- **Live in the REPL.** Evaluate every form as you write it. Don't write
  ten lines then run tests; build the answer expression by expression.
- **`(doc fn)`**, **`(source fn)`**, **`(dir clojure.string)`** at the
  REPL are your stdlib browser.
- **Read the kata stub's "You are learning:" line.** It names the exact
  concepts; this guide's section for that kata covers them.
- **`just test-kata N`** runs one kata's tests; **`just review N`**
  reviews it. The kata file is the source of truth — examples in the
  comment are runnable expectations.
- **When stuck, shrink the input.** `(encode "aa")` before
  `(encode "aaabbc")`. Pure functions make this trivial.
- **Style:** 2-space indent, no commas, gather trailing parens, prefer
  `(:k m)` over `(get m :k)`, `seq` for emptiness checks, threading over
  nesting. See [STYLEGUIDE.md](STYLEGUIDE.md).
