package com.kisscodesystems.NqProblem;

/** Small pure helpers: string padding and elapsed-time formatting. */
final class Utils {

  /**
   * Pads the given string to the given length using trailing spaces. If the string is already at
   * least that long it is returned unchanged.
   *
   * @param s the string to pad
   * @param n the desired minimum length
   * @return the padded string
   */
  static String padTo(String s, int n) {
    String back = s;
    while (back.length() < n) {
      back += " ";
    }
    return back;
  }

  /**
   * Formats an elapsed duration into a human readable string of days, hours, minutes, seconds and
   * milliseconds (only the leading zero units are omitted).
   *
   * @param elapsedMs the elapsed time in milliseconds
   * @return the formatted elapsed time (for example {@code "1m 44s 357ms"} or {@code "0ms"})
   */
  static String calcElapsed(long elapsedMs) {
    int days = 0;
    int hours = 0;
    int minutes = 0;
    int seconds = 0;
    int milliseconds = 0;
    boolean zeroIsNeeded = false;
    String totalElapsedTime = "";
    if (elapsedMs >= 24 * 3600 * 1000) {
      days = (int) (elapsedMs / (24 * 3600 * 1000));
      elapsedMs -= days * (24 * 3600 * 1000);
    }
    if (elapsedMs >= 3600 * 1000) {
      hours = (int) (elapsedMs / (3600 * 1000));
      elapsedMs -= hours * (3600 * 1000);
    }
    if (elapsedMs >= 60 * 1000) {
      minutes = (int) (elapsedMs / (60 * 1000));
      elapsedMs -= minutes * (60 * 1000);
    }
    if (elapsedMs >= 1000) {
      seconds = (int) (elapsedMs / 1000);
      elapsedMs -= seconds * (1000);
    }
    milliseconds = (int) elapsedMs;
    if (days > 0) {
      zeroIsNeeded = true;
      totalElapsedTime += days + "d ";
    }
    if (hours > 0) {
      zeroIsNeeded = true;
      totalElapsedTime += hours + "h ";
    }
    if (minutes > 0 || zeroIsNeeded) {
      zeroIsNeeded = true;
      totalElapsedTime += minutes + "m ";
    }
    if (seconds > 0 || zeroIsNeeded) {
      zeroIsNeeded = true;
      totalElapsedTime += seconds + "s ";
    }
    if (milliseconds > 0 || zeroIsNeeded) {
      totalElapsedTime += milliseconds + "ms";
    }
    if (days == 0 && hours == 0 && minutes == 0 && seconds == 0 && milliseconds == 0) {
      totalElapsedTime = "0ms";
    }
    milliseconds = 0;
    seconds = 0;
    minutes = 0;
    hours = 0;
    days = 0;
    zeroIsNeeded = false;
    return totalElapsedTime;
  }
}
