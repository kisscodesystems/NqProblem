package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.ConsoleIo.*;
import static com.kisscodesystems.NqProblem.Const.*;
import static com.kisscodesystems.NqProblem.Filters.*;
import static com.kisscodesystems.NqProblem.Placer.*;
import static com.kisscodesystems.NqProblem.PreCalc.*;
import static com.kisscodesystems.NqProblem.Print.*;
import static com.kisscodesystems.NqProblem.State.*;
import static com.kisscodesystems.NqProblem.Utils.*;

/**
 * The mode drivers: the original solution, the numbered improved cases (single- and multi-threaded)
 * and the testing benchmark.
 */
final class Modes {

  /**
   * Runs the default (standard) solution. This is quicker than the default backtrack solution
   * because it uses the precalculated attacking map instead of computing the hit state from every
   * already placed queen by coordinates.
   */
  static void doOriginal() {
    preCalcStuffs(dimension);
    putPiecesOriginal(0);
  }

  /**
   * Improved case 01: debug, first solution, single thread, no preplacings, no uniques. Uses the
   * bishop or order-optimized first-solution debug method depending on the pieces.
   */
  static void doImproved01() {
    outprintln("\n ( case : improved 01 )");
    if (BISHOP.equals(pieces)) {
      putPiecesFirstDebug(0, 0, dimensionD);
    } else {
      putPiecesFirstOrderoptimizedDebug(0, 0, dimensionD);
    }
  }

  /**
   * Improved case 02: info, first solution, single thread, no preplacings, no uniques. Places the
   * first piece to every candidate and prints per-piece progress.
   *
   * @param startDate the start timestamp in milliseconds used to display the elapsed time
   */
  static void doImproved02(long startDate) {
    outprintln("\n ( case : improved 02 )");
    long foundToThisPiece = 0;
    int count;
    if (BISHOP.equals(pieces)) {
      for (int i = 0; i < dimensionD && found == 0; i++) {
        currPath[0] = workingArray[i];
        currIndToWrite = dimensionD;
        count = applyFilters(currPath[0], 0, dimensionD);
        putPiecesFirst(1, currIndToWrite - count, currIndToWrite);
        outprintln(
            " ("
                + i
                + ") found: "
                + found
                + "  c"
                + (found - foundToThisPiece)
                + " | "
                + calcElapsed(System.currentTimeMillis() - startDate));
        foundToThisPiece = found;
      }
    } else {
      for (int i = 0; i < dimension && found == 0; i++) {
        currPath[0] = workingArray[i];
        currIndToWrite = dimensionD;
        count = applyFilters(currPath[0], 0, dimensionD);
        putPiecesFirstOrderoptimized(1, currIndToWrite - count, currIndToWrite);
        outprintln(
            " ("
                + i
                + ") found: "
                + found
                + "  c"
                + (found - foundToThisPiece)
                + " | "
                + calcElapsed(System.currentTimeMillis() - startDate));
        foundToThisPiece = found;
      }
    }
    if (found != 0) {
      printPs();
    }
  }

  /**
   * Improved case 03: no log, first solution, single thread, no preplacings, no uniques. Uses the
   * bishop or order-optimized first-solution method depending on the pieces.
   */
  static void doImproved03() {
    outprintln("\n ( case : improved 03 )");
    if (BISHOP.equals(pieces)) {
      putPiecesFirst(0, 0, dimensionD);
    } else {
      putPiecesFirstOrderoptimized(0, 0, dimensionD);
    }
    if (found != 0) {
      printPs();
    }
  }

  /**
   * Improved case 04: debug, ordered solutions, single thread, no preplacings, no uniques. Uses the
   * bishop or order-optimized all-solutions debug method depending on the pieces.
   */
  static void doImproved04() {
    outprintln("\n ( case : improved 04 )");
    if (BISHOP.equals(pieces)) {
      putPiecesAllDebug(0, 0, dimensionD);
    } else {
      putPiecesOrderoptimizedDebug(0, 0, dimensionD);
    }
  }

  /**
   * Improved case 05: info, ordered solutions, single thread, no preplacings, no uniques. Places
   * the first piece to every candidate and prints per-piece progress.
   *
   * @param startDate the start timestamp in milliseconds used to display the elapsed time
   */
  static void doImproved05(long startDate) {
    outprintln("\n ( case : improved 05 )");
    long foundToThisPiece = 0;
    int count;
    if (BISHOP.equals(pieces)) {
      for (int i = 0; i < dimensionD; i++) {
        currPath[0] = workingArray[i];
        currIndToWrite = dimensionD;
        count = applyFilters(currPath[0], 0, dimensionD);
        putPiecesAll(1, currIndToWrite - count, currIndToWrite);
        outprintln(
            " ("
                + i
                + ") found: "
                + found
                + "  c"
                + (found - foundToThisPiece)
                + " | "
                + calcElapsed(System.currentTimeMillis() - startDate));
        foundToThisPiece = found;
      }
    } else {
      for (int i = 0; i < dimension; i++) {
        currPath[0] = workingArray[i];
        currIndToWrite = dimensionD;
        count = applyFilters(currPath[0], 0, dimensionD);
        putPiecesOrderoptimized(1, currIndToWrite - count, currIndToWrite);
        outprintln(
            " ("
                + i
                + ") found: "
                + found
                + "  c"
                + (found - foundToThisPiece)
                + " | "
                + calcElapsed(System.currentTimeMillis() - startDate));
        foundToThisPiece = found;
      }
    }
  }

  /**
   * Improved case 06: no log, ordered solutions, single thread, no preplacings, no uniques. Uses
   * the bishop or order-optimized all-solutions method depending on the pieces.
   */
  static void doImproved06() {
    outprintln("\n ( case : improved 06 )");
    if (BISHOP.equals(pieces)) {
      putPiecesAll(0, 0, dimensionD);
    } else {
      putPiecesOrderoptimized(0, 0, dimensionD);
    }
  }

  /** Improved case 07: debug, all solutions, single thread, no preplacings, no uniques. */
  static void doImproved07() {
    outprintln("\n ( case : improved 07 )");
    putPiecesAllDebug(0, 0, dimensionD);
  }

  /**
   * Improved case 08: info, all solutions, single thread, no preplacings, no uniques. Places the
   * first piece to every candidate and prints per-piece progress.
   *
   * @param startDate the start timestamp in milliseconds used to display the elapsed time
   */
  static void doImproved08(long startDate) {
    outprintln("\n ( case : improved 08 )");
    long foundToThisPiece = 0;
    int count;
    for (int i = 0; i < dimensionD; i++) {
      currPath[0] = workingArray[i];
      currIndToWrite = dimensionD;
      count = applyFilters(currPath[0], 0, dimensionD);
      putPiecesAll(1, currIndToWrite - count, currIndToWrite);
      outprintln(
          " ("
              + i
              + ") found: "
              + found
              + "  c"
              + (found - foundToThisPiece)
              + " | "
              + calcElapsed(System.currentTimeMillis() - startDate));
      foundToThisPiece = found;
    }
  }

  /** Improved case 09: no log, all solutions, single thread, no preplacings, no uniques. */
  static void doImproved09() {
    outprintln("\n ( case : improved 09 )");
    putPiecesAll(0, 0, dimensionD);
  }

  /**
   * Improved case 10: debug, ordered solutions, single thread, no preplacings, uniques. Uses the
   * bishop or order-optimized rotated all-solutions debug method depending on the pieces.
   */
  static void doImproved10() {
    outprintln("\n ( case : improved 10 )");
    if (BISHOP.equals(pieces)) {
      putPiecesAllRotatedDebug(0, 0, dimensionD);
    } else {
      putPiecesOrderoptimizedRotatedDebug(0, 0, dimensionD);
    }
  }

  /**
   * Improved case 11: info, ordered solutions, single thread, no preplacings, uniques. Places the
   * first piece to every candidate and prints per-piece progress.
   *
   * @param startDate the start timestamp in milliseconds used to display the elapsed time
   */
  static void doImproved11(long startDate) {
    outprintln("\n ( case : improved 11 )");
    long foundToThisPiece = 0;
    int count;
    if (BISHOP.equals(pieces)) {
      for (int i = 0; i < dimensionD; i++) {
        currPath[0] = workingArray[i];
        currIndToWrite = dimensionD;
        count = applyFilters(currPath[0], 0, dimensionD);
        putPiecesAllRotated(1, currIndToWrite - count, currIndToWrite);
        outprintln(
            " ("
                + i
                + ") found: "
                + found
                + "  c"
                + (found - foundToThisPiece)
                + " | "
                + calcElapsed(System.currentTimeMillis() - startDate));
        foundToThisPiece = found;
      }
    } else {
      for (int i = 0; i < dimension; i++) {
        currPath[0] = workingArray[i];
        currIndToWrite = dimensionD;
        count = applyFilters(currPath[0], 0, dimensionD);
        putPiecesOrderoptimizedRotated(1, currIndToWrite - count, currIndToWrite);
        outprintln(
            " ("
                + i
                + ") found: "
                + found
                + "  c"
                + (found - foundToThisPiece)
                + " | "
                + calcElapsed(System.currentTimeMillis() - startDate));
        foundToThisPiece = found;
      }
    }
  }

  /**
   * Improved case 12: no log, ordered solutions, single thread, no preplacings, uniques. Uses the
   * bishop or order-optimized rotated all-solutions method depending on the pieces.
   */
  static void doImproved12() {
    outprintln("\n ( case : improved 12 )");
    if (BISHOP.equals(pieces)) {
      putPiecesAllRotated(0, 0, dimensionD);
    } else {
      putPiecesOrderoptimizedRotated(0, 0, dimensionD);
    }
  }

  /** Improved case 13: debug, all solutions, single thread, no preplacings, uniques. */
  static void doImproved13() {
    outprintln("\n ( case : improved 13 )");
    putPiecesAllRotatedDebug(0, 0, dimensionD);
  }

  /**
   * Improved case 14: info, all solutions, single thread, no preplacings, uniques. Places the first
   * piece to every candidate and prints per-piece progress.
   *
   * @param startDate the start timestamp in milliseconds used to display the elapsed time
   */
  static void doImproved14(long startDate) {
    outprintln("\n ( case : improved 14 )");
    long foundToThisPiece = 0;
    int count;
    for (int i = 0; i < dimensionD; i++) {
      currPath[0] = workingArray[i];
      currIndToWrite = dimensionD;
      count = applyFilters(currPath[0], 0, dimensionD);
      putPiecesAllRotated(1, currIndToWrite - count, currIndToWrite);
      outprintln(
          " ("
              + i
              + ") found: "
              + found
              + "  c"
              + (found - foundToThisPiece)
              + " | "
              + calcElapsed(System.currentTimeMillis() - startDate));
      foundToThisPiece = found;
    }
  }

  /** Improved case 15: no log, all solutions, single thread, no preplacings, uniques. */
  static void doImproved15() {
    outprintln("\n ( case : improved 15 )");
    putPiecesAllRotated(0, 0, dimensionD);
  }

  /**
   * Improved case 16: first solution, single thread, with preplacings, no uniques. Uses the bishop
   * or order-optimized preplacings first-solution method depending on the pieces.
   *
   * @param from the start index in the working array returned by {@link
   *     PreCalc#preCalcFilterings()}
   */
  static void doImproved16(int from) {
    outprintln("\n ( case : improved 16 )");
    if (BISHOP.equals(pieces)) {
      putPiecesFirstP(0, from, currIndToWrite);
    } else {
      putPiecesFirstOrderoptimizedP(0, from, currIndToWrite);
    }
    if (found != 0) {
      printPs();
    }
  }

  /**
   * Improved case 17: ordered solutions, single thread, with preplacings, no uniques. Uses the
   * bishop or order-optimized preplacings all-solutions method depending on the pieces.
   *
   * @param from the start index in the working array returned by {@link
   *     PreCalc#preCalcFilterings()}
   */
  static void doImproved17(int from) {
    outprintln("\n ( case : improved 17 )");
    if (BISHOP.equals(pieces)) {
      putPiecesAllP(0, from, currIndToWrite);
    } else {
      putPiecesOrderoptimizedP(0, from, currIndToWrite);
    }
  }

  /**
   * Improved case 18: all solutions, single thread, with preplacings, no uniques.
   *
   * @param from the start index in the working array returned by {@link
   *     PreCalc#preCalcFilterings()}
   */
  static void doImproved18(int from) {
    outprintln("\n ( case : improved 18 )");
    putPiecesAllP(0, from, currIndToWrite);
  }

  /**
   * Improved case 19: ordered solutions, single thread, with preplacings, uniques. Uses the bishop
   * or order-optimized preplacings rotated all-solutions method depending on the pieces.
   *
   * @param from the start index in the working array returned by {@link
   *     PreCalc#preCalcFilterings()}
   */
  static void doImproved19(int from) {
    outprintln("\n ( case : improved 19 )");
    if (BISHOP.equals(pieces)) {
      putPiecesAllRotatedP(0, from, currIndToWrite);
    } else {
      putPiecesOrderoptimizedRotatedP(0, from, currIndToWrite);
    }
  }

  /**
   * Improved case 20: all solutions, single thread, with preplacings, uniques.
   *
   * @param from the start index in the working array returned by {@link
   *     PreCalc#preCalcFilterings()}
   */
  static void doImproved20(int from) {
    outprintln("\n ( case : improved 20 )");
    putPiecesAllRotatedP(0, from, currIndToWrite);
  }

  /**
   * The improved solution entry point. Performs all the precalculations, optionally prints the
   * attacking map, then dispatches to the proper doImprovedXX method depending on the parameters
   * (threads, placings, uniques, hits and log). For more than one thread it fans the work out onto
   * a fixed thread pool using an inner worker.
   *
   * @param startDate the start timestamp in milliseconds used to display the elapsed time
   */
  static void doImproved(long startDate) {
    // All of the precalculation will be done.
    preCalcStuffs(dimension);
    // The attacking map will be printed if first solution is requested.
    if (FIRST.equals(hits)) {
      printIsFiltered();
    }
    // Then, let's call the proper doImprovedXX method.
    if (threads == 1) {
      if (placings.equals(defaultPlacings)) {
        if (NO.equals(uniques)) {
          if (FIRST.equals(hits)) {
            if (DEBUG.equals(log)) {
              doImproved01();
            } else if (INFO.equals(log)) {
              doImproved02(startDate);
            } else if (NO.equals(log)) {
              doImproved03();
            }
          } else if (ORDERED.equals(hits)) {
            if (DEBUG.equals(log)) {
              doImproved04();
            } else if (INFO.equals(log)) {
              doImproved05(startDate);
            } else if (NO.equals(log)) {
              doImproved06();
            }
          } else if (ALL.equals(hits)) {
            if (DEBUG.equals(log)) {
              doImproved07();
            } else if (INFO.equals(log)) {
              doImproved08(startDate);
            } else if (NO.equals(log)) {
              doImproved09();
            }
          }
        } else if (YES.equals(uniques)) {
          preCalcRotatings();
          if (ORDERED.equals(hits)) {
            if (DEBUG.equals(log)) {
              doImproved10();
            } else if (INFO.equals(log)) {
              doImproved11(startDate);
            } else if (NO.equals(log)) {
              doImproved12();
            }
          } else if (ALL.equals(hits)) {
            if (DEBUG.equals(log)) {
              doImproved13();
            } else if (INFO.equals(log)) {
              doImproved14(startDate);
            } else if (NO.equals(log)) {
              doImproved15();
            }
          }
        }
      } else {
        int from = preCalcFilterings();
        if (NO.equals(uniques)) {
          if (FIRST.equals(hits)) {
            doImproved16(from);
          } else if (ORDERED.equals(hits)) {
            doImproved17(from);
          } else if (ALL.equals(hits)) {
            doImproved18(from);
          }
        } else if (YES.equals(uniques)) {
          preCalcRotatings();
          if (ORDERED.equals(hits)) {
            doImproved19(from);
          } else if (ALL.equals(hits)) {
            doImproved20(from);
          }
        }
      }
    } else if (threads > 1) {
      // If multiple threads is requested then it has to use this little inner class.

      // The calculation on multiple threads will happen by using thread pool.
      java.util.concurrent.ExecutorService executorService =
          java.util.concurrent.Executors.newFixedThreadPool(threads);
      // Ordered solutions: there is a difference between bishops and others:
      // The loop has to end on dimensionD in case of bishops while the others can
      // stop at dimension to apply the optimization based on the rules of chess.
      if (ORDERED.equals(hits)) {
        if (BISHOP.equals(pieces)) {
          for (int i = 0; i < dimensionD; i++) {
            NqProblemWork nqWork = new NqProblemWork(i);
            executorService.execute(nqWork);
          }
        } else {
          for (int i = 0; i < dimension; i++) {
            NqProblemWork nqWork = new NqProblemWork(i);
            executorService.execute(nqWork);
          }
        }
      } else if (ALL.equals(hits)) {
        for (int i = 0; i < dimensionD; i++) {
          NqProblemWork nqWork = new NqProblemWork(i);
          executorService.execute(nqWork);
        }
      }
      // We will wait for every single thread to be finished.
      executorService.shutdown();
      while (!executorService.isTerminated()) {}
    }
  }

  /**
   * The testing mode. Compares the running times of the original and the improved solution, running
   * the improved algorithm on one thread so the comparison is fair.
   *
   * <p>Important: the running times of the original solution are hard-coded below and have to be
   * updated by running them on your own computer, because the differences between machines are
   * huge. The baked-in elapsed times were measured on a laptop with Windows 10 x64, an Intel
   * Celeron N2840 (2 x 2.16GHz), 8GB 1333MHz DDR3 and one thread. Nobody wants to wait half a day
   * to see the difference, so the default solution is not run here; you have to run it separately
   * and update and recompile this code.
   */
  static void doTesting() {
    // The dimension now is a limit: we will run every dimensions until this bound.
    int maxDimension = dimension;
    // Starting times, there will be some.
    long sDate = 0;
    // The results of running times and result strings.
    // The integer ones are to calculate rates between runnings.
    // The resultTimes integers and the 3rd columns of the proper result string
    // have to be equal and depend on your own runnings.
    int[][] resultTimes = new int[19][2];
    String[] resultStrins = new String[19];
    // You have to update these lines according to your own running times
    // using java -jar NqProblem.jar i 13 ..18 commands!
    // The times have to be updated till <<EOR
    resultStrins[13] = "13  c73712     2377     ms ( 2s 377ms )          -> ";
    resultStrins[14] = "14  c365596    15116    ms ( 15s 116ms )         -> ";
    resultStrins[15] = "15  c2279184   104357   ms ( 1m 44s 357ms )      -> ";
    resultStrins[16] = "16  c14772512  766915   ms ( 12m 46s 915ms )     -> ";
    resultStrins[17] = "17  c95815104  6040290  ms ( 1h 40m 40s 290ms )  -> ";
    resultStrins[18] = "18  c666090624 48165728 ms ( 13h 22m 45s 728ms ) -> ";
    resultTimes[13][0] = 2377;
    resultTimes[14][0] = 15116;
    resultTimes[15][0] = 104357;
    resultTimes[16][0] = 766915;
    resultTimes[17][0] = 6040290;
    resultTimes[18][0] = 48165728;
    // << EOR
    // Prints some information to the user, this contains the info to consider the table.
    outprintln("");
    outprintln("Let's see the difference of using mode o and i.");
    outprintln("mode o -> mode i (improved version versus original solution)");
    outprintln("mode o won't be run,");
    outprintln("  the times have to be written manually to the source");
    outprintln("rate1: i elapsed / o elapsed");
    outprintln("rate2: i (k) elapsed / i (k-1) elapsed");
    outprintln("");
    outprintln(
        "n   count      elapsed      elapsed2             -> elapsed        elapsed2          "
            + " count        rate1   rate2");
    // Let's do all of it to the max dimension.
    for (int i = 13; i <= maxDimension; i++) {
      sDate = System.currentTimeMillis();
      preCalcStuffs(i);
      putPiecesOrderoptimized(0, 0, dimensionD);
      resultTimes[i][1] = (int) (System.currentTimeMillis() - sDate);
      resultStrins[i] +=
          padTo("" + resultTimes[i][1], 10)
              + "ms ( "
              + padTo(calcElapsed(resultTimes[i][1]) + " )", 18)
              + " "
              + padTo("c" + found, 10)
              + " ( "
              + padTo(
                  ""
                      + ((double) ((int) ((double) resultTimes[i][1] / resultTimes[i][0] * 1000))
                          / 1000),
                  5)
              + (i == 13
                  ? " |       )"
                  : (" | "
                      + padTo(
                          ""
                              + ((double)
                                      ((int)
                                          ((double) resultTimes[i][1]
                                              / resultTimes[i - 1][1]
                                              * 1000))
                                  / 1000),
                          5)
                      + " )"));
      outprintln(resultStrins[i]);
    }
    // If there are unprinted lines, let's print them now.
    for (int i = maxDimension + 1; i <= 18; i++) {
      outprintln(resultStrins[i]);
    }
  }
}
