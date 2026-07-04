package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.State.*;

/** Detection of already found but rotated solutions. */
final class Rotations {

  /**
   * Decides whether the current path ({@link #currPath}) is a rotated version of any of the already
   * found solutions. Used by the putPieces variants where the rotated versions must not be counted
   * as different.
   *
   * @return {@code true} if the current path matches an earlier solution under any rotation
   */
  static boolean thisIsRotatedVersion() {
    boolean rotatedFound = false;
    for (int i = 0; i < found; i++) {
      if (thisIsRotatedVersionOf(i, rotatedIndexes1)) {
        rotatedFound = true;
        break;
      }
      if (thisIsRotatedVersionOf(i, rotatedIndexes2)) {
        rotatedFound = true;
        break;
      }
      if (thisIsRotatedVersionOf(i, rotatedIndexes3)) {
        rotatedFound = true;
        break;
      }
      if (thisIsRotatedVersionOf(i, rotatedIndexes4)) {
        rotatedFound = true;
        break;
      }
      if (thisIsRotatedVersionOf(i, rotatedIndexes5)) {
        rotatedFound = true;
        break;
      }
      if (thisIsRotatedVersionOf(i, rotatedIndexes6)) {
        rotatedFound = true;
        break;
      }
      if (thisIsRotatedVersionOf(i, rotatedIndexes7)) {
        rotatedFound = true;
        break;
      }
    }
    return rotatedFound;
  }

  /**
   * The actual searcher between solutions. Every piece is compared with every other piece in the
   * stored solution because the sequences may not be ordered. This is an expensive search of a
   * rotated version.
   *
   * @param i the index of the stored solution in {@link State#foundPathes} to compare against
   * @param indexes the rotation index map to apply to the current path
   * @return {@code true} if every element of the current path has a pair in the stored solution
   *     under the given rotation
   */
  static boolean thisIsRotatedVersionOf(int i, int[] indexes) {
    boolean matched = false;
    for (int j = 0; j < dimension; j++) {
      // Doesn't match by default.
      matched = false;
      for (int k = 0; k < dimension; k++) {
        if (foundPathes[i][k] == indexes[currPath[j]]) {
          // The first element which presents in the currently searched solution.
          matched = true;
          // The others don't need to be compared of course.
          break;
        }
      }
      // If this is not matched here then returning false immediately.
      if (!matched) {
        return false;
      }
    }
    // This is a rotated version because every element has a pair in this stored solution.
    return true;
  }
}
