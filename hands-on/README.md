# Hands-On Java Practice

Solutions to a set of Java practice questions (methods, strings, dates,
files, regex, conditionals, plus a handful of standalone exercises), scanned
from a worksheet. 105 exercises across 7 folders, each a standalone `.java`
file with a `main()` that demonstrates the solution.

This is unrelated to the microservices exercise in the rest of this
repository — it lives here purely as a separate, self-contained practice set.

## Layout

```
hands-on/
├── java-methods/        23 exercises  (Q01-Q23)
├── java-strings/          6 exercises (Q01-Q06)
├── date-time/            18 exercises (Q01-Q18)
├── files/                18 exercises (Q01-Q18) + sample-data/
├── regex/                 9 exercises (Q01-Q09)
├── conditional-logic/    20 exercises (Q01-Q20)
└── misc-exercises/       11 exercises (Q01-Q11) + sample-data/
```

Every file is self-contained — no shared classpath, no build tool. Compile
and run any one directly:

```bash
cd hands-on/java-methods
javac Q13TriangleArea.java
java Q13TriangleArea
```

Or compile and run a whole section at once:

```bash
cd hands-on/java-methods
javac *.java
for f in Q*.class; do java "${f%.class}"; echo; done
```

The `files/` and `misc-exercises/` folders read from their own `sample-data/`
subfolder, so run those from inside the folder (as shown above) rather than
from `hands-on/` directly.

## Verification

Every exercise with a concrete input/output pair given on the worksheet
(Java Methods, Java Strings, Regex, Conditional Logic, and the 11 standalone
exercises — about 70 of the 105) was compiled and run, and its output checked
against the worksheet's expected value. All matched exactly, including:

- The first 50 pentagonal numbers (java-methods/Q07) and the 5-year monthly
  compound-interest table (Q08), matched digit-for-digit against the scan.
- The quadratic-equation roots (conditional-logic/Q02) to full double
  precision.
- Heron's-formula triangle area and the regular-pentagon area
  (java-methods/Q13, Q14).

Two sections — **Date/Time** and **Files** — are inherently open-ended
("write a program to get the current time", "read a file line by line")
rather than fixed input/output pairs, so those were verified by running each
one and confirming it produces sensible, correct output, not by matching a
static expected value.

## Notes on the source material

The worksheet is a scanned/OCR'd document, and a handful of spots were
garbled or self-contradictory (e.g. one exercise's title says "check every
digit is odd" while its own worked example uses an all-even number and
expects `true`). Where that happened, the code follows the worked example
over the prose, and each affected file has a comment explaining the call.
A few Date/Time and Files items had no expected output at all (open-ended
"write a program to..." tasks) — those got a correct, working implementation
with a demonstration `main()`, without a fixed value to assert against.
