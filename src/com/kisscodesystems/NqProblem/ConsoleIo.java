package com.kisscodesystems.NqProblem;

/** Console output helpers. Synchronized so the worker threads do not interleave their lines. */
final class ConsoleIo {

  /**
   * Prints the given text to the console followed by a line break. Synchronized because the worker
   * threads may use it.
   *
   * @param s the text to print
   */
  static synchronized void outprintln(String s) {
    System.out.println(s);
  }

  /**
   * Prints the given text to the console without a line break. Synchronized because the worker
   * threads may use it.
   *
   * @param s the text to print
   */
  static synchronized void outprint(String s) {
    System.out.print(s);
  }
}
