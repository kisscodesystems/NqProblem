/*
 ** NqProblem application.
 **
 ** Description : This program was born to play around the N Queens Problem. We give a solution which
 ** considers the problem from a whole different aspect and splits the actual calculation from the
 ** real rules of the chess game. In this way, the calculations of rooks and bishops are also
 ** available as well as their super or awesome versions. (super means that the pieces can also
 ** attack as a knight and) (awesome means the above but till the edge of the chessboard) Preplaced
 ** chess pieces are also available to solve N Queens Completion problem.
 **
 ** Published : 01.01.2018
 **
 ** Current version : 2.1
 **
 ** Developed by : Jozsef Kiss KissCode Systems Kft <https://kcsops.kisscodesystems.com>
 **
 ** Change log : 1.0 - 01.01.2018 Initial release.
 **              1.1 - 31-12-2018 Small updates.
 **              2.0 - 07.04.2026 Restructure the code.
 **              2.1 - 07.04.2026 Small fixes.
 **
 ** See NqProblem txt or pdf for more information.
 */
package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.ConsoleIo.*;
import static com.kisscodesystems.NqProblem.Const.*;
import static com.kisscodesystems.NqProblem.Modes.*;
import static com.kisscodesystems.NqProblem.Print.*;
import static com.kisscodesystems.NqProblem.State.*;
import static com.kisscodesystems.NqProblem.Utils.*;

/**
 * Application entry point: parses and validates the command-line parameters, prints the chosen
 * configuration, dispatches to the original, improved or testing mode, and prints the footer.
 */
public final class NqProblemMain {

  /**
   * The main entry point. Determines the values of the parameters to initialize from and decides
   * where to go from here (original, improved, testing or usage), then prints a footer for the
   * original and improved modes.
   *
   * @param args the command line arguments: mode, dimension and (in improved mode) pieces, kinds,
   *     hits, threads, uniques, log and placings
   */
  public static void main(String[] args) {
    // The first and second parameters are essential so let's get these values.
    try {
      mode = args[0];
      if (!HELP1.equals(mode)
          && !HELP2.equals(mode)
          && !HELP3.equals(mode)
          && !ORIGINAL.equals(mode)
          && !IMPROVED.equals(mode)
          && !TESTING.equals(mode)) {
        mode = defaultMode;
      }
    } catch (Exception e) {
      mode = defaultMode;
    }
    try {
      dimension = Integer.parseInt(args[1]);
      if (dimension > 0) {
        if (TESTING.equals(mode) && (dimension < 13 || dimension > 18)) {
          dimension = defaultDimension;
        }
      } else {
        dimension = defaultDimension;
      }
    } catch (Exception e) {
      dimension = defaultDimension;
    }
    // Only in improved mode, there are additional parameters which can be used...
    if (IMPROVED.equals(mode)) {
      try {
        pieces = args[2];
        if (!QUEEN.equals(pieces) && !ROOK.equals(pieces) && !BISHOP.equals(pieces)) {
          pieces = defaultPieces;
        }
      } catch (Exception e) {
        pieces = defaultPieces;
      }
      try {
        kinds = args[3];
        if (!REGULAR.equals(kinds) && !SUPER.equals(kinds) && !AWESOME.equals(kinds)) {
          kinds = defaultKinds;
        }
      } catch (Exception e) {
        kinds = defaultKinds;
      }
      try {
        hits = args[4];
        if (!ORDERED.equals(hits) && !ALL.equals(hits) && !FIRST.equals(hits)) {
          hits = defaultHits;
        }
      } catch (Exception e) {
        hits = defaultHits;
      }
      try {
        threads = Integer.parseInt(args[5]);
        if (threads < 1) {
          threads = defaultThreads;
        }
      } catch (Exception e) {
        threads = defaultThreads;
      }
      try {
        uniques = args[6];
        if (!YES.equals(uniques) && !NO.equals(uniques)) {
          uniques = defaultUniques;
        }
      } catch (Exception e) {
        uniques = defaultUniques;
      }
      try {
        log = args[7];
        if (!DEBUG.equals(log) && !INFO.equals(log) && !NO.equals(log)) {
          log = defaultLog;
        }
      } catch (Exception e) {
        log = defaultLog;
      }
      try {
        placings = args[8];
      } catch (Exception e) {
        placings = defaultPlacings;
      }
    }
    // .. otherwise, these default values will be used no matter what values are given by the
    // user.
    else {
      pieces = defaultPieces;
      kinds = defaultKinds;
      hits = defaultHits;
      threads = defaultThreads;
      uniques = defaultUniques;
      log = defaultLog;
      placings = defaultPlacings;
      if (args.length > 2) {
        outprintln("Warning: original and testing modes accept dimension only!");
      }
    }
    // Sometimes correcting the above values is necessary. So, let's do it and warn the user about
    // this.
    if ((threads > 1 && FIRST.equals(hits))
        || (threads > 1 && YES.equals(uniques))
        || (threads > 1 && !placings.equals(defaultPlacings))) {
      threads = 1;
      outprintln("Warning: 1 thread  is possible  to use when we will");
      outprintln("         search  for the  first  valid  position or");
      outprintln("         the rotated versions wont'be differents or");
      outprintln("         preplaced  chess  pieces   are  specified.");
    }
    if (YES.equals(uniques) && FIRST.equals(hits)) {
      uniques = NO;
      outprintln("Warning: uniques version is not possible when the");
      outprintln("         first  valid  position  is searched for.");
    }
    if (threads > 1 && DEBUG.equals(log)) {
      log = INFO;
      outprintln("Warning: debug  is not possible when");
      outprintln("         multithreaded is requested.");
    }
    if (!NO.equals(log) && !placings.equals(defaultPlacings)) {
      log = NO;
      outprintln("Warning: logging is not possible when default placings are used.");
    }
    // OK, we can start now because we have every information to calculate.
    // Informing the user about the parameters to use.
    outprintln("");
    outprintln("Let's  calculate the positions of the chess");
    outprintln("pieces  when they don't  attack each other!");
    outprintln("");
    outprintln("  0 mode      (original,improved,testing) : " + mode);
    outprintln("  1 dimension (a positive integer)        : " + dimension);
    outprintln("  2 pieces    (queen,rook,bishop)         : " + pieces);
    outprintln("  3 kinds     (regular,super,awesome)     : " + kinds);
    outprintln("  4 hits      (ordered,all,first)         : " + hits);
    outprintln("  5 threads   (a positive integer)        : " + threads);
    outprintln("  6 uniques   (no,yes)                    : " + uniques);
    outprintln("  7 log       (no,info,debug)             : " + log);
    outprintln("  8 placings  (ints separated by , char)  : " + placings);
    outprintln("");
    // The starter timestamp has to be stored to display the calculation time
    // elapsed at the end.
    long startDate = System.currentTimeMillis();
    outprintln("Started : " + new java.util.Date());
    // Call of the original, improved or testing version.
    // If none of these is possible then the usage will be printed.
    if (ORIGINAL.equals(mode)) {
      doOriginal();
    } else if (IMPROVED.equals(mode)) {
      doImproved(startDate);
    } else if (TESTING.equals(mode)) {
      doTesting();
    } else {
      printUsage();
    }
    // In case of original and improved modes, a footer will be printed containing
    // for example the found solutions, the dead cases or the elapsed time.
    if (ORIGINAL.equals(mode) || IMPROVED.equals(mode)) {
      long endDate = System.currentTimeMillis();
      outprintln("");
      outprintln("  " + found);
      outprintln("  " + (found == 1 ? "position has been found," : "positions have been found"));
      if (deads != 0) {
        outprintln("  all attempts: " + (found + deads));
        outprintln(
            "              ( "
                + ((double) ((int) ((double) found / (found + deads) * 10000)) / 100)
                + "% success )");
      }
      outprintln("  in " + calcElapsed(endDate - startDate));
      outprintln("   ( " + (endDate - startDate) + " )");
    }
  }
}
