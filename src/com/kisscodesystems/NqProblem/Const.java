package com.kisscodesystems.NqProblem;

/**
 * Application-wide constants: the command-line option keywords and the default values used when an
 * argument is missing or invalid.
 */
final class Const {
  /** Version of the application. */
  static final String VERSION = "2.0";

  /** Release date of the current version. */
  static final String RELEASED = "07-04-2026";

  /*
   ** The options usable as parameters.
   */
  /** Mode option: print the help/usage (variant '?'). */
  static final String HELP1 = "?";

  /** Mode option: print the help/usage (variant 'h'). */
  static final String HELP2 = "h";

  /** Mode option: print the help/usage (variant 'help'). */
  static final String HELP3 = "help";

  /** Mode option: run the original (standard) solution. */
  static final String ORIGINAL = "o";

  /** Mode option: run the improved solution. */
  static final String IMPROVED = "i";

  /** Mode option: run the testing (benchmark) mode. */
  static final String TESTING = "t";

  /** Pieces option: queen. */
  static final String QUEEN = "q";

  /** Pieces option: rook. */
  static final String ROOK = "r";

  /** Pieces option: bishop. */
  static final String BISHOP = "b";

  /** Kinds option: regular movement. */
  static final String REGULAR = "r";

  /** Kinds option: super movement (can also attack as a knight). */
  static final String SUPER = "s";

  /** Kinds option: awesome movement (knight-like but till the edge of the chessboard). */
  static final String AWESOME = "a";

  /** Hits option: search for the ordered solutions. */
  static final String ORDERED = "o";

  /** Hits option: search for all solutions. */
  static final String ALL = "a";

  /** Hits option: search for the first solution only. */
  static final String FIRST = "f";

  /** Boolean-like option value: yes. */
  static final String YES = "y";

  /** Boolean-like option value: no. */
  static final String NO = "n";

  /** Log option: debug level. */
  static final String DEBUG = "d";

  /** Log option: info level. */
  static final String INFO = "i";

  /*
   ** If the given value is not correct, these are the default values to use.
   */
  /** Default mode when the given value is not valid. */
  static final String defaultMode = HELP1;

  /** Default dimension when the given value is not valid. */
  static final int defaultDimension = 15;

  /** Default pieces when the given value is not valid. */
  static final String defaultPieces = QUEEN;

  /** Default kinds when the given value is not valid. */
  static final String defaultKinds = REGULAR;

  /** Default hits when the given value is not valid. */
  static final String defaultHits = ORDERED;

  /** Default thread count when the given value is not valid. */
  static final int defaultThreads = 1;

  /** Default uniques when the given value is not valid. */
  static final String defaultUniques = NO;

  /** Default log level when the given value is not valid. */
  static final String defaultLog = NO;

  /** Default placings (none) when the given value is not valid. */
  static final String defaultPlacings = "";
}
