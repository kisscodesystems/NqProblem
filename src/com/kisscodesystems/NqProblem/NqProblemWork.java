package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.ConsoleIo.*;
import static com.kisscodesystems.NqProblem.Const.*;
import static com.kisscodesystems.NqProblem.State.*;
import static com.kisscodesystems.NqProblem.Utils.*;

/**
 * The worker used for multithreaded improved runs. Each instance handles one starting position of
 * the first piece; everything else it needs is read from the enclosing class. It keeps its own
 * copies of the mutable search state so the workers do not interfere with each other, and only the
 * found/dead counts are merged back into the parent via {@link State#foundInc(int)} and {@link
 * State#deadsInc(int)}.
 */
final class NqProblemWork implements Runnable {
  /** The position of the first-line put piece handled by this worker. */
  private int pos = 0;

  /** Per-thread copy of the found counter. */
  private int found = 0;

  /** Per-thread copy of the dead counter. */
  private int deads = 0;

  /** Per-thread copy of the current path. */
  private int[] currPath = new int[dimension];

  /** Per-thread copy of the write index into {@link #workingArray}. */
  private int currIndToWrite = 0;

  /** Per-thread copy of the working array. */
  private int[] workingArray = new int[workingArrayLength];

  /**
   * Creates a worker for the given first-piece position.
   *
   * @param p the position of the first piece this worker starts from
   */
  public NqProblemWork(int p) {
    pos = p;
  }

  /**
   * Runs this worker: initializes the local state, places the first piece to {@link #pos}, runs the
   * ordered or all-solutions search (info or no log, never debug or first) and merges its counters
   * back into the parent.
   */
  @Override
  public void run() {
    // The initialization of this thread.
    for (int i = 0; i < dimensionD; i++) {
      workingArray[i] = i;
    }
    currIndToWrite = dimensionD;
    // We put the first piece.
    currPath[0] = workingArray[pos];
    // The starter timestamp.
    long startDate = System.currentTimeMillis();
    // OK, we can do the followings:
    // - info and none log levels ( no debug )
    // - ordered and all solutions ( no first )
    // - dimension, pieces, kind: all is possible
    if (ORDERED.equals(hits)) {
      int count;
      if (INFO.equals(log)) {
        count = applyFilters(currPath[0], 0, dimensionD);
        if (BISHOP.equals(pieces)) {
          putPiecesAll(1, currIndToWrite - count, currIndToWrite);
        } else {
          putPiecesOrderoptimized(1, currIndToWrite - count, currIndToWrite);
        }
        outprintln(
            " ("
                + pos
                + ") found: "
                + found
                + " | "
                + calcElapsed(System.currentTimeMillis() - startDate));
      } else if (NO.equals(log)) {
        count = applyFilters(currPath[0], 0, dimensionD);
        if (BISHOP.equals(pieces)) {
          putPiecesAll(1, currIndToWrite - count, currIndToWrite);
        } else {
          putPiecesOrderoptimized(1, currIndToWrite - count, currIndToWrite);
        }
      }
    } else if (ALL.equals(hits)) {
      int count;
      if (INFO.equals(log)) {
        count = applyFilters(currPath[0], 0, dimensionD);
        putPiecesAll(1, currIndToWrite - count, currIndToWrite);
        outprintln(
            " ("
                + pos
                + ") found: "
                + found
                + " | "
                + calcElapsed(System.currentTimeMillis() - startDate));
      } else if (NO.equals(log)) {
        count = applyFilters(currPath[0], 0, dimensionD);
        putPiecesAll(1, currIndToWrite - count, currIndToWrite);
      }
    }
    // We have to increase these values by the found ints found on this thread.
    foundInc(found);
    deadsInc(deads);
  }

  /**
   * Per-thread copy of {@link Filters#applyFilters(int, int, int)}. It works on this worker's own
   * working array and write index.
   *
   * @param currPiecePos the board position of the piece whose attacks are applied as a filter
   * @param from the start index (inclusive) in the working array of the positions to filter
   * @param to the end index (exclusive) in the working array of the positions to filter
   * @return the number of usable (not filtered) positions written
   */
  private int applyFilters(int currPiecePos, int from, int to) {
    boolean[] a = isFiltered[currPiecePos];
    for (int i = from; i < to; i++) {
      if (!a[workingArray[i]]) {
        workingArray[currIndToWrite] = workingArray[i];
        currIndToWrite++;
      }
    }
    return currIndToWrite - to;
  }

  /**
   * Per-thread copy of {@link Placer#putPiecesAll(int, int, int)}.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  private void putPiecesAll(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        int count;
        for (int i = from; i < to; i++) {
          currPath[pieceToPlace] = workingArray[i];
          currIndToWrite = to;
          count = applyFilters(currPath[pieceToPlace], from, to);
          putPiecesAll(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
        }
      } else {
        deads++;
      }
    } else {
      found++;
    }
  }

  /**
   * Per-thread copy of {@link Placer#putPiecesOrderoptimized(int, int, int)}.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  private void putPiecesOrderoptimized(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        if (rowOfPiece[workingArray[from]] == pieceToPlace) {
          int count;
          currPath[pieceToPlace] = workingArray[from];
          currIndToWrite = to;
          count = applyFilters(currPath[pieceToPlace], from, to);
          putPiecesOrderoptimized(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          for (int i = from + 1; i < to && rowOfPiece[workingArray[i]] == pieceToPlace; i++) {
            currPath[pieceToPlace] = workingArray[i];
            currIndToWrite = to;
            count = applyFilters(currPath[pieceToPlace], from, to);
            putPiecesOrderoptimized(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          }
        } else {
          deads++;
        }
      } else {
        deads++;
      }
    } else {
      found++;
    }
  }
}
