# NqProblem

NqProblem is a command-line solver for the **N Queens Problem** — placing N
chess pieces on an N×N board so that none of them attacks another. It approaches
the problem from a whole different aspect: it splits the actual calculation from
the rules of the chess game, which lets it solve not only queens but also rooks
and bishops, plus their "super" and "awesome" movement variants, and it can
complete boards that already have some pieces placed (the N Queens Completion
problem).

- **Developed by:** Jozsef Kiss — KissCode Systems Kft
- **License:** GNU General Public License, version 3
- **Current version:** 2.1

## Concept

- The board of size N is represented as an **ordered sequence of the integers
  0 … N²−1** (row by row). For a 4×4 board, field 11 is at row 2, column 3.
- Instead of re-deriving hits from coordinates during the search, NqProblem
  **precomputes an attacking map** once: for every field it records which other
  fields it attacks. The backtracking search then only consults this map, which
  is what makes it fast.
- Because the hit rules live entirely in the precomputed map, the same engine
  handles different **pieces** (queen / rook / bishop) and **kinds** of movement:
  - `regular` — the normal chess movement of the piece.
  - `super` — the piece can *also* attack as a knight.
  - `awesome` — like super, but the knight-style attacks continue **to the edge**
    of the board.
- **Hits** selects what you are searching for:
  - `ordered` — the order-optimized search (one piece per row); the natural mode
    for counting all valid arrangements efficiently.
  - `all` — all solutions.
  - `first` — stop at the first valid arrangement.
- **uniques** (`yes`) discards solutions that are rotations/mirrors of one already
  found, so only genuinely distinct boards are counted.
- **placings** lets you pre-place pieces (comma-separated field indices); the
  solver then completes the board around them (N Queens Completion). If two
  preplaced pieces attack each other it reports it and stops.
- **threads** parallelizes the search across the possible positions of the first
  piece using a fixed thread pool.
- **log** controls tracing: `no`, `info` (per-first-piece progress) or `debug`
  (full step-by-step trace; only practical on small boards).

## Modes

| Mode | Meaning                                                                                     |
| ---- | ------------------------------------------------------------------------------------------- |
| `o`  | **Original** — the standard backtracking solution (using the precomputed attacking map).    |
| `i`  | **Improved** — the optimized engine with all the options above (pieces, kinds, hits, uniques, threads, placings, log). |
| `t`  | **Testing** — a benchmark that runs the improved algorithm on one thread and compares it against baseline times of the original solution. |
| `?` `h` `help` | Print the usage.                                                                |

> **Testing mode note:** the baseline running times of the *original* solution
> are hard-coded in the source (they were measured on one specific machine). To
> get a meaningful comparison on your own hardware you have to run the original
> solution yourself and update those numbers in `Modes.doTesting`, then rebuild.
> Testing mode only accepts dimensions 13–18.

## Parameters

The application is invoked as `java -jar NqProblem.jar <arguments>`, positionally:

```
  #  name        original     improved      testing          help        default
  0  mode        o            i             t                ?,h,help          ?
  1  dimension   1..n int     1..n int      13..18 int                        15
  2  pieces      q            q,r,b         q                                  q
  3  kinds       r            r,s,a         r                                  r
  4  hits        o            o,a,f         o                                  o
  5  threads     1            1..n int      1                                  1
  6  uniques     n            n,y           n                                  n
  7  log         n            d,i,n         n                                  n
  8  placings    empty        int,int...    empty                          empty
```

- `mode`    → `o`:original, `i`:improved, `t`:testing (anything else → help)
- `pieces`  → `q`:queen, `r`:rook, `b`:bishop
- `kinds`   → `r`:regular, `s`:super, `a`:awesome
- `hits`    → `o`:ordered, `a`:all, `f`:first
- `uniques` → `n`:no, `y`:yes
- `log`     → `n`:no, `i`:info, `d`:debug
- `placings`→ one or more integer field indices separated by `,` (improved only)

Any value that is missing or not recognized falls back to its default. In
**original** and **testing** modes only the dimension is used; the extra improved
parameters are ignored.

Some combinations are automatically corrected (with a warning): more than one
thread is only used together with `ordered`/`all` hits, `uniques = no` and no
preplacings; `uniques` is disabled when searching for the `first` solution;
`debug` is downgraded to `info` when multithreaded; and logging is disabled when
preplacings are given.

## Architecture

NqProblem is a single Java package built as a set of small, flat, static modules
(the same style as the sibling MyPwStock / MyDbConns applications), wired together
with `import static`:

| Module              | Responsibility                                                                       |
| ------------------- | ------------------------------------------------------------------------------------ |
| `NqProblemMain`     | Entry point: parse/validate parameters, print the configuration, dispatch, footer.   |
| `Const`             | Option keywords and default values.                                                  |
| `State`             | All mutable runtime state and the synchronized worker counters.                      |
| `ConsoleIo`         | Synchronized console output.                                                         |
| `Utils`             | Small pure helpers (padding, elapsed-time formatting).                               |
| `PreCalc`           | Precalculations: attacking map, rotation maps, preplacing filters.                   |
| `Filters`           | Filtering the working array by a placed piece's attacks.                             |
| `Rotations`         | Detecting already-found-but-rotated solutions.                                       |
| `Placer`            | The core placing methods — the standard solver plus the family of micro-optimized improved variants. |
| `Modes`             | The mode drivers (`doOriginal`, the numbered improved cases, `doTesting`).           |
| `Print`             | Human-readable output: boards, traces, attacking map, usage.                         |
| `NqProblemWork`     | The worker used for multithreaded improved runs.                                     |

## Build

NqProblem is a single-package, dependency-free Java program. Building it is just a
`javac` compile plus packaging the runnable jar (run from the repository root):

```bash
./NqProblem_build.sh
```

This produces `NqProblem.jar` in the repository root. A JDK 8 or newer is
sufficient; no external libraries are required to build or run it.

## Run

```bash
java -jar NqProblem.jar                       # print help
java -jar NqProblem.jar o 8                    # original solution, 8x8 queens
java -jar NqProblem.jar i 8 q r a              # improved: all 8-queens solutions
java -jar NqProblem.jar i 8 q r o 4            # improved, ordered, on 4 threads
java -jar NqProblem.jar i 8 q r a 1 y n        # only rotation-unique solutions
java -jar NqProblem.jar i 6 b r a              # 6 non-attacking bishops, all
java -jar NqProblem.jar i 8 q s f              # first "super queen" arrangement
java -jar NqProblem.jar i 8 q r f 1 n n 0,9,18 # complete the board around preplaced pieces
```

For original and improved modes the footer prints how many arrangements were
found, the total number of attempts, the success rate and the elapsed time.
