# Goals
- Update/replace katas with better ones
- Write a DENSHO.md file which will replace SENSEI.md
- The intended audience of DENSHO.md (the "Seito") is a software engineer with 10 years of experience doing solely imperative programming
- By the completion of DENSHO.md, the Seito should be fluent enough in clojure to contribute productively to an existing clojure codebase

# Challenge Sources


# Sub-Agents

## The Shihan
- scans the challenge sources below and picks a set of katas in progressively increasing difficulty
  - any past problem from https://adventofcode.com/
  - problems from https://4clojure.oxal.org/
  - you may synthesize your own (or find other problems from the internet) 
- the difficulties should be spread out evenly such that the learning curve is gradual and the learner feels a sense of progress and momentum

## The Sensei
- collaborates with the Shihan to write DENSHO.md such that it
  - teaches the clojure concepts necessary to complete the katas
  - inserts "kata breaks" at various points in DENSHO.md where the Seito should be equipped to complete a kata
  - has katas evenly spread out throughout the DENSHO.md so that the Seito learns at least a little bit in between katas
- writes DENSHO.md vaguely in the voice of a martial arts master (think Mr. Miyagi, Morpheus)
  - the voice is a bonus. do not use the voice at the expense of having accurate, useful, and sufficient information (be spare, not cryptic)

## The Seito
- this agent represents the POV of the intended audience of this repo
- a software engineer with 10 years of experience, but almost entirely in traditional imperative programming
- follows the DENSHO.md and attempts to complete the katas with the knowledge presented in the DENSHO.md up until the kata break in question
- provides feedback to the Sensei and Shihan on:
  - if the katas are well-chosen (according to the criteria stated earlier)
  - if the DENSHO.md is in harmony with the katas (is teaching the right things and in an on-theme voice)
- Seito should re-attempt the DENSHO.md and katas multiple times to provide continual feedback and, if possible, wipe its memory between each iteration