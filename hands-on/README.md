# Hands-On Java Practice

Solutions to a set of Java practice questions (methods, strings, dates,
files, regex, conditionals, plus a handful of standalone exercises), scanned
from a worksheet. 105 exercises across 7 folders, each a standalone `.java`
file with a `main()` that reads its inputs from the console via `Scanner`.

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

## Running one

Every exercise prompts for its own input on stdin. Compile and run
interactively:

```bash
cd hands-on/java-methods
javac Q13TriangleArea.java
java Q13TriangleArea
# Input Side-1: 10
# Input Side-2: 15
# Input Side-3: 20
# The area of the triangle is 72.61843774138907
```

Or pipe input in non-interactively (handy for scripting or re-checking
against the worksheet's test data):

```bash
printf "10\n15\n20\n" | java Q13TriangleArea
```

The `files/` and `misc-exercises/` exercises prompt for a **file path** —
point them at their own `sample-data/` folder (paths are relative to wherever
you run `java` from, so `cd` into the exercise's own directory first):

```bash
cd hands-on/files
javac *.java
echo "sample-data/sample.txt" | java Q12ReadPlainTextFile
```

`files/Q08ReadFromConsole` reads free-text console input directly, not a path.

## What each exercise asks for

Most prompts mirror the worksheet's own "Input the first number: " style
wording. A few exercises take more than one value (e.g. three sides of a
triangle, two strings to compare, N followed by that many rows) — the
program's own prompts make the order clear as you run it.

**Six exercises are exceptions** and were deliberately left without a Scanner,
because they query the current system clock rather than anything a user could
meaningfully supply as input — "what time is it right now" has no sensible
prompt:

- `java-methods/Q15CurrentDateTime`
- `date-time/Q02DisplayCalendarInfo`, `Q03MaxCalendarFields`,
  `Q04MinCalendarFields`, `Q05CurrentTimeInNewYork`, `Q06CurrentFullDateTime`

Every other Date/Time exercise that was originally phrased around "the
current month" or "today" was generalized to take a year/month/date from the
user instead (e.g. `Q07LastDayOfCurrentMonth` now asks for a year and month
and returns the last day of *that* month, not necessarily this one).

## Verification

Every convertible exercise (99 of 105) was compiled and run with its
original worksheet test data piped in via stdin, and the output checked
against the worksheet's expected value — same results as before the input
conversion, now sourced from `Scanner` instead of hardcoded. This included:

- The first 50 pentagonal numbers (`java-methods/Q07`) and the 5-year monthly
  compound-interest table (`Q08`), matched digit-for-digit against the scan.
- The quadratic-equation roots (`conditional-logic/Q02`) to full double
  precision.
- Heron's-formula triangle area and the regular-pentagon area
  (`java-methods/Q13`, `Q14`).

All 7 sections were also compiled together in one final sweep with zero
errors.

## Notes on the source material

The worksheet is a scanned/OCR'd document, and a handful of spots were
garbled or self-contradictory (e.g. one exercise's title says "check every
digit is odd" while its own worked example uses an all-even number and
expects `true`). Where that happened, the code follows the worked example
over the prose, and each affected file has a comment explaining the call.
