package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.Const.*;

/**
 * Mutable runtime state shared by the modules: the parsed parameters, the solution and dead
 * counters, the cached dimension values, the working arrays, the precalculated row/column, rotation
 * and attacking maps, plus the synchronized counter updates used by the worker threads.
 */
final class State {

  /** The mode to run, initialized to its default value. */
  static String mode = defaultMode;

  /** The dimension of the chessboard, initialized to its default value. */
  static int dimension = defaultDimension;

  /** The kind of chess pieces to place, initialized to its default value. */
  static String pieces = defaultPieces;

  /** The movement kind of the pieces, initialized to its default value. */
  static String kinds = defaultKinds;

  /** The kind of hits (ordered/all/first) to search for, initialized to its default value. */
  static String hits = defaultHits;

  /** The number of worker threads to use, initialized to its default value. */
  static int threads = defaultThreads;

  /** Whether only unique (not rotated) solutions count, initialized to its default value. */
  static String uniques = defaultUniques;

  /** The log level, initialized to its default value. */
  static String log = defaultLog;

  /** The preplaced pieces, initialized to its default value. */
  static String placings = defaultPlacings;

  /**
   * The number of found solutions.
   *
   * <p>Its companion {@link #deads} counts the piece placings that cannot be finished or don't
   * count: in the improved versions there are fewer places than the number of pieces to put, in the
   * orderoptimized situation the next piece cannot be placed into the next row, in the uniques
   * situation a placing found as a previously found but rotated solution also becomes a dead
   * solution, and in the original solution whenever a hit check fails.
   */
  static long found = 0;

  /** The number of dead (unusable) piece placings. See {@link #found} for the details. */
  static long deads = 0;

  /** Cached square of the dimension (dimension * dimension). */
  static int dimensionD = 0;

  /** Cached dimension + 1 value. */
  static int dimensionP1 = 0;

  /** Cached dimension - 1 value. */
  static int dimensionM1 = 0;

  /** The store of the actual piece placings (the current path). */
  static int[] currPath = null;

  /** A shadow of {@link #currPath}: stores the initial (preplaced) placings. */
  static int[] placingsArray = null;

  /**
   * The precomputed row of every piece position, cached to avoid recomputing it.
   *
   * <p>For example on a 4x4 chessboard the fields are numbered 0..15 row by row, so the row of the
   * 11th element is 2 and the column of it is 3.
   */
  static int[] rowOfPiece = null;

  /** The precomputed column of every piece position, cached to avoid recomputing it. */
  static int[] colOfPiece = null;

  /**
   * During the filtering procedure we don't return new arrays containing the not-filtered values
   * (not filtered means valid positions for further use). Instead a single working array is reused,
   * and this is the next index to write into it.
   */
  static int currIndToWrite = 0;

  /** The single reused working array holding the ordered board and the filtered sub-sequences. */
  static int[] workingArray = null;

  /** The initial (and maximum) size of {@link #workingArray}, expected to be enough. */
  static int workingArrayLength = 100000;

  /**
   * When the found solution has to be searched as an already put but rotated version, the found
   * paths have to be saved here.
   */
  static int[][] foundPathes = null;

  /** Precomputed rotation index map, variant 1. */
  static int[] rotatedIndexes1 = null;

  /** Precomputed rotation index map, variant 2. */
  static int[] rotatedIndexes2 = null;

  /** Precomputed rotation index map, variant 3. */
  static int[] rotatedIndexes3 = null;

  /** Precomputed rotation index map, variant 4. */
  static int[] rotatedIndexes4 = null;

  /** Precomputed rotation index map, variant 5. */
  static int[] rotatedIndexes5 = null;

  /** Precomputed rotation index map, variant 6. */
  static int[] rotatedIndexes6 = null;

  /** Precomputed rotation index map, variant 7. */
  static int[] rotatedIndexes7 = null;

  /**
   * The precomputed attacking map. This is part of the core logic: all attackings are precomputed
   * depending on the input parameters, so {@code isFiltered[a][b]} shows whether the piece on field
   * a can attack the piece on field b. For example (4x4, queen) the 6th field attacks the 9th
   * field, so {@code isFiltered[6][9] == true}, while the 7th field does not attack the 8th field,
   * so {@code isFiltered[7][8] == false}.
   */
  static boolean[][] isFiltered = null;

  /**
   * The count of pieces still to place, by level. This equals the count of the elements of
   * placings; according to the preplaced elements these are integers that differ by the level we
   * currently place in.
   */
  static int[] placingsRemainings = null;

  /**
   * Increments the found-solutions counter from the worker threads. Synchronized because several
   * worker threads may call it concurrently.
   *
   * @param f the number of found solutions to add
   */
  static synchronized void foundInc(long f) {
    found += f;
  }

  /**
   * Increments the dead-placings counter from the worker threads. Synchronized because several
   * worker threads may call it concurrently.
   *
   * @param d the number of dead placings to add
   */
  static synchronized void deadsInc(long d) {
    deads += d;
  }
}
