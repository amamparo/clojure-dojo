# Goals

- Replace **all** existing katas with a freshly designed progression.
- Write **DENSHO.md**, which **replaces** SENSEI.md (SENSEI.md is deleted;
  README.md and CLAUDE.md are updated to match — see Constraints).
- Audience ("the Seito"): a strong generalist software engineer, ~10
  years' experience almost entirely in imperative programming, zero
  functional/Lisp background. Assume data structures, recursion, and
  closures are understood *as concepts*; do **not** assume immutability,
  the seq abstraction, REPL-driven workflow, or macro intuition.
  - This concrete persona is the **internal design target and the
    Seito's role-play brief only**. Learner-facing copy (DENSHO.md,
    README) keeps a single spare, generic audience line and **no
    over-specified persona / no `## Audience` section** — a concrete
    persona in published docs was previously rejected as too concrete.
- North star: on finishing DENSHO.md the Seito can **contribute
  productively to an existing Clojure codebase** — read an unfamiliar
  namespace, run/extend it, use the REPL and tests as instruments, and
  write idiomatic changes. This is a strict superset of "can solve the
  katas"; the curriculum is designed backward from it.

# Challenge Sources

Agents may consult these **at authoring time only** to design,
calibrate, and synthesize katas:

- Any past problem from <https://adventofcode.com/>.
- Problems from <https://4clojure.oxal.org/>.
- Synthesized originals, or other internet problems, where they fit the
  gradient better.

**Offline contract:** the shipped repo — DENSHO.md narrative, kata
files, tests — must be fully self-contained with **zero web references
in learner-facing material**. A single optional "Going further"
appendix in DENSHO.md may carry external links (as SENSEI.md does); the
core narrative and katas cite nothing external.

# Sub-Agents

I (the orchestrator) spawn and coordinate these. The Shihan and Sensei
collaborate and revise together each round; the Seito is independent,
adversarial QA, freshly spawned with no memory of prior rounds.

## The Shihan — kata selection & difficulty curve

- Scans the Challenge Sources and selects/synthesizes a kata set whose
  difficulty rises **gradually and evenly**, so the Seito feels steady
  momentum — no cliffs, no plateaus.
- **Sizes the set itself** (no fixed count), guided only by the
  north-star goal and the gradient criterion. The concept coverage is
  **discovered through iteration** with the Sensei and the Seito's gap
  reports — it is deliberately *not* prescribed in this brief.
- Owns the difficulty curve and the source material; co-owns, with the
  Sensei, exactly where each kata sits relative to the prose.
- Default kata shape is stub → implement (`;; TODO` body, tests written
  against the intended solution). The Shihan **may** also include
  "extend / refactor / fix this existing code" katas (shipping partial
  working code instead of a bare stub) — this directly serves the
  contribute-to-an-existing-codebase north star and is a sanctioned
  deviation from the stub-only convention (CLAUDE.md is updated to
  document the variant).

## The Sensei — DENSHO.md

- Collaborates with the Shihan to write DENSHO.md so it:
  - teaches whatever concepts the north-star goal demands — the Shihan
    and Sensei converge on that set through iteration and Seito gap
    feedback, not from a prescribed list — in an order where each
    concept is earned before it is needed;
  - places **kata breaks** (`> 🥋 Complete kata N …`) exactly where the
    Seito is equipped to attempt that kata using only prose *above* the
    break;
  - spreads katas evenly through the narrative — the Seito always learns
    something between consecutive katas.
- **Voice (locked):** spare, dense, technical — SENSEI.md's existing
  register is the reference. Thematic voice is limited to (a) the
  existing dojo role terminology and (b) a brief section opener
  (≤ 1 sentence, at most a single light aphorism). **No** character
  grammar or aphorism inside explanatory prose. Be spare, not cryptic;
  never sacrifice accuracy, usefulness, or sufficiency for voice.
- **Audience in copy:** at most one spare, generic line naming the
  reader (new to Clojure and to FP); no over-specified persona, no
  labeled audience section. In-text comparisons stay generic ("other
  languages"), never naming a specific background language.

## The Seito — adversarial learner / QA

- Freshly spawned **each round** (no memory of prior rounds; no access
  to intended solutions or to the Shihan/Sensei rationale).
- Per kata, sees only: DENSHO.md **up to that kata's break**, the kata
  file, and its tests. Tests are the oracle (`just test N`).
- **Hard constraint:** uses only Clojure constructs introduced at or
  before the current break. If it needs to reach past that, it records
  a **gap** ("DENSHO did not equip me here") instead of using outside
  knowledge — that gap is the signal, not a failure to route around.
- Reports each round on: (a) per-kata pass/fail and where pre-break
  material was insufficient or misleading; (b) difficulty gradient
  (jumps too large/small, momentum, sense of progress); (c)
  DENSHO ↔ kata harmony (right concepts, right order, on-voice).

# Build Loop & Definition of Done

1. Shihan + Sensei produce / revise the kata set + DENSHO.md (round 1
   from scratch; later rounds from the prior Seito's feedback).
2. A fresh Seito attempts the whole progression under its constraints.
3. Seito feedback → back to step 1.

- **Converged** when a fresh Seito completes **every** kata with green
  tests using only pre-break material **and** raises no blocking
  pedagogical defect.
- **Hard cap: 10 rounds.** If not converged by the last round, stop and report
  state + remaining gaps; the user may re-trigger.
- A one-paragraph summary per round is appended to a working
  `dojo-build-log.md` (build scaffolding, not part of the shipped
  history) so progress is visible without a mid-run gate.

# Constraints & Conventions (retained from CLAUDE.md)

- Tooling unchanged: `just` / eftest / zprint / clj-kondo;
  `.zprint.edn` and `.clj-kondo/config.edn` as-is. New kata namespaces
  must stay under the `katas.*` group so a fresh clone still **lints
  clean and is formatted**, with **tests red by design**.
- File conventions: `src/katas/kata_NN_<slug>.clj` +
  `test/katas/kata_NN_<slug>_test.clj`, matching `ns` forms; tests
  `:refer`/`:as` the vars under test. Renumber-on-insert rule applies.
- DENSHO.md replaces SENSEI.md: **delete SENSEI.md**; update README.md
  (SENSEI → DENSHO everywhere, kata list) and CLAUDE.md (authoritative
  for Claude — SENSEI references, checkpoint model, kata count/list,
  and the new extend-existing-code kata variant).
- I do not commit or push any of this unless explicitly asked.

# Deliverables

- `DENSHO.md`; the new kata set (`src` + `test`); SENSEI.md removed;
  README.md and CLAUDE.md updated. Fresh clone: lint clean, formatted,
  tests red by design. Final report: convergence state + any residual
  gaps.