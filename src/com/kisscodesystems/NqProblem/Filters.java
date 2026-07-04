package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.Print.*;
import static com.kisscodesystems.NqProblem.State.*;

/** Filtering of the working array by the attackings of a placed piece. */
final class Filters {

  /**
   * Filters the working array by the attackings of a placed piece. It walks through the working
   * array between the given bounds and appends every position that the placed piece does not attack
   * to the write position ({@link #currIndToWrite}). The code is duplicated between variants on
   * purpose, to avoid deciding the operating mode on every call which would slow it down.
   *
   * @param currPiecePos the board position of the piece whose attacks are applied as a filter
   * @param from the start index (inclusive) in the working array of the positions to filter
   * @param to the end index (exclusive) in the working array of the positions to filter
   * @return the number of usable (not filtered) positions written
   */
  static int applyFilters(int currPiecePos, int from, int to) {
    // This is a reference to the array we will use.
    boolean[] a = isFiltered[currPiecePos];
    // We want to go thru the working array between the given bounds.
    for (int i = from; i < to; i++) {
      // Every element should be considered: the currPiecePos element filters
      // this actual element or not.
      // The result is false means:
      // that actual element can be used further as a valid position to use.
      if (!a[workingArray[i]]) {
        workingArray[currIndToWrite] = workingArray[i];
        currIndToWrite++;
      }
    }
    // Returns the number of usable positions.
    return currIndToWrite - to;
  }

  /**
   * Same as {@link #applyFilters(int, int, int)} but also traces the pieces to the output.
   *
   * @param currPiecePos the board position of the piece whose attacks are applied as a filter
   * @param from the start index (inclusive) in the working array of the positions to filter
   * @param to the end index (exclusive) in the working array of the positions to filter
   * @return the number of usable (not filtered) positions written
   */
  static int applyFiltersDebug(int currPiecePos, int from, int to) {
    boolean[] a = isFiltered[currPiecePos];
    for (int i = from; i < to; i++) {
      if (!a[workingArray[i]]) {
        workingArray[currIndToWrite] = workingArray[i];
        currIndToWrite++;
      }
    }
    // Same as the above but the pieces will be traced.
    tracePs(currPiecePos, from, to);
    return currIndToWrite - to;
  }
}
