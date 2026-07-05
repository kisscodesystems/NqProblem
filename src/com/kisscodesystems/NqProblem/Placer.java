package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.ConsoleIo.*;
import static com.kisscodesystems.NqProblem.Filters.*;
import static com.kisscodesystems.NqProblem.Print.*;
import static com.kisscodesystems.NqProblem.Rotations.*;
import static com.kisscodesystems.NqProblem.State.*;
import static com.kisscodesystems.NqProblem.Utils.*;

/**
 * The core placing methods: the standard backtracking solution and the family of improved,
 * micro-optimized variants (all/ordered/first, rotated and preplaced flavors). The variants are
 * intentionally kept as separate near-duplicate methods so no per-call mode decision slows the
 * recursion down.
 */
final class Placer {

  /**
   * The main placing method used while searching for all solutions. It recursively places the next
   * piece to every usable position and filters the remaining candidates. Some code is duplicated
   * across the placing variants for the performance reason mentioned in {@link
   * Filters#applyFilters(int, int, int)}.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesAll(int pieceToPlace, int from, int to) {
    // The piece to place is maximum dimension - 1 means: there are pieces to put.
    if (pieceToPlace < dimension) {
      // Are there enough places to put all of the pieces?
      if (to - from >= dimension - pieceToPlace) {
        // Important, the number of places to use from the input sequence.
        int count;
        // We want to loop on the working array between the given bounds.
        for (int i = from; i < to; i++) {
          // If we are here, the next position is always valid so let's use this.
          currPath[pieceToPlace] = workingArray[i];
          // This position always has to be set into this:
          // we want to write the next sequence into the end of this current sequence.
          currIndToWrite = to;
          // This filtering process has to be done here, and the count has to be saved.
          // ( The number of usable positions to use from the input. )
          count = applyFilters(currPath[pieceToPlace], from, to);
          // Let it invoke itself: this will be the next piece put to this current set of places.
          putPiecesAll(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
        }
      } else {
        // Deads 1: there are more pieces to put than the available free places.
        deads++;
      }
    } else {
      // All of the pieces have been put so this is a valid solution.
      found++;
    }
  }

  /**
   * Debug variant of {@link #putPiecesAll(int, int, int)}: also prints the array of pieces
   * currently put, the produced solutions and the per-case counts.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesAllDebug(int pieceToPlace, int from, int to) {
    // We will print the array of pieces currently put.
    outprint("  [ ");
    for (int i = 0; i < pieceToPlace; i++) {
      outprint(padTo("" + currPath[i], 4));
    }
    outprintln("]");
    // We also want to display the number of found solution to this case.
    long c = found;
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        int count;
        for (int i = from; i < to; i++) {
          currPath[pieceToPlace] = workingArray[i];
          currIndToWrite = to;
          count = applyFiltersDebug(currPath[pieceToPlace], from, to);
          putPiecesAllDebug(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
        }
      } else {
        deads++;
      }
    } else {
      found++;
      // Prints the pieces, this is a valid solution!
      printPs();
    }
    // Calculation and print of the count belongs to this case.
    c = found - c;
    outprintln(padTo("", pieceToPlace * 4) + c);
  }

  /**
   * All-solutions placing variant that only counts a completed placing when it is not a rotated
   * version of an earlier solution, storing the accepted ones into {@link State#foundPathes}.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesAllRotated(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        int count;
        for (int i = from; i < to; i++) {
          currPath[pieceToPlace] = workingArray[i];
          currIndToWrite = to;
          count = applyFilters(currPath[pieceToPlace], from, to);
          putPiecesAllRotated(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
        }
      } else {
        deads++;
      }
    } else {
      // This will be a valid solution only if this is not a rotated as the founds before.
      if (!thisIsRotatedVersion()) {
        // This solution has to be saved.
        for (int i = 0; i < dimension; i++) {
          foundPathes[(int) found][i] = currPath[i];
        }
        found++;
      } else {
        // Deads 2: this is also a dead solution.
        deads++;
      }
    }
  }

  /**
   * Debug variant of {@link #putPiecesAllRotated(int, int, int)}: also prints the placing progress,
   * the accepted solutions and the per-case counts.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesAllRotatedDebug(int pieceToPlace, int from, int to) {
    outprint("  [ ");
    for (int i = 0; i < pieceToPlace; i++) {
      outprint(padTo("" + currPath[i], 4));
    }
    outprintln("]");
    long c = found;
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        int count;
        for (int i = from; i < to; i++) {
          currPath[pieceToPlace] = workingArray[i];
          currIndToWrite = to;
          count = applyFiltersDebug(currPath[pieceToPlace], from, to);
          putPiecesAllRotatedDebug(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
        }
      } else {
        deads++;
      }
    } else {
      if (!thisIsRotatedVersion()) {
        for (int i = 0; i < dimension; i++) {
          foundPathes[(int) found][i] = currPath[i];
        }
        found++;
        printPs();
      } else {
        deads++;
      }
    }
    c = found - c;
    outprintln(padTo("", pieceToPlace * 4) + c);
  }

  /**
   * Order-optimized placing variant (usable for queen and rook, not bishop): a piece may only be
   * placed into the row that equals its level, which prunes many invalid positions.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesOrderoptimized(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        // The rules of the chess will be applied to queen and rook.
        // If we have N queens and NxN chessboard, we have to put one into all of the
        // rows ( or cols ). If not, there will be the situation when 2 or more queens
        // have to be put into the same column ( or row ), and that solutions won't
        // count as valid.
        // This is just an optimization step and the core logic is not built
        // using this. The precalculated attacking map ensures that only the ordered
        // solutions will be found.
        // But we find that too many invalid positions will be tested if the below rule
        // is not applied. So we do this optimization for queen and rook.
        // This won't work using bishops.
        if (rowOfPiece[workingArray[from]] == pieceToPlace) {
          int count;
          // The first run happens separately to avoid the first duplicated row check.
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
          // Deads 3: the put piece is not in the expected row.
          deads++;
        }
      } else {
        deads++;
      }
    } else {
      found++;
    }
  }

  /**
   * Debug variant of {@link #putPiecesOrderoptimized(int, int, int)}: also prints the placing
   * progress, the produced solutions and the per-case counts.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesOrderoptimizedDebug(int pieceToPlace, int from, int to) {
    outprint("  [ ");
    for (int i = 0; i < pieceToPlace; i++) {
      outprint(padTo("" + currPath[i], 4));
    }
    outprintln("]");
    long c = found;
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        if (rowOfPiece[workingArray[from]] == pieceToPlace) {
          int count;
          currPath[pieceToPlace] = workingArray[from];
          currIndToWrite = to;
          count = applyFiltersDebug(currPath[pieceToPlace], from, to);
          putPiecesOrderoptimizedDebug(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          for (int i = from + 1; i < to && rowOfPiece[workingArray[i]] == pieceToPlace; i++) {
            currPath[pieceToPlace] = workingArray[i];
            currIndToWrite = to;
            count = applyFiltersDebug(currPath[pieceToPlace], from, to);
            putPiecesOrderoptimizedDebug(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          }
        } else {
          deads++;
        }
      } else {
        deads++;
      }
    } else {
      found++;
      printPs();
    }
    c = found - c;
    outprintln(padTo("", pieceToPlace * 4) + c);
  }

  /**
   * Order-optimized placing variant that only counts a completed placing when it is not a rotated
   * version of an earlier solution, storing the accepted ones into {@link State#foundPathes}.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesOrderoptimizedRotated(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        if (rowOfPiece[workingArray[from]] == pieceToPlace) {
          int count;
          currPath[pieceToPlace] = workingArray[from];
          currIndToWrite = to;
          count = applyFilters(currPath[pieceToPlace], from, to);
          putPiecesOrderoptimizedRotated(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          for (int i = from + 1; i < to && rowOfPiece[workingArray[i]] == pieceToPlace; i++) {
            currPath[pieceToPlace] = workingArray[i];
            currIndToWrite = to;
            count = applyFilters(currPath[pieceToPlace], from, to);
            putPiecesOrderoptimizedRotated(
                pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          }
        } else {
          deads++;
        }
      } else {
        deads++;
      }
    } else {
      if (!thisIsRotatedVersion()) {
        for (int i = 0; i < dimension; i++) {
          foundPathes[(int) found][i] = currPath[i];
        }
        found++;
      } else {
        deads++;
      }
    }
  }

  /**
   * Debug variant of {@link #putPiecesOrderoptimizedRotated(int, int, int)}: also prints the
   * placing progress, the accepted solutions and the per-case counts.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesOrderoptimizedRotatedDebug(int pieceToPlace, int from, int to) {
    outprint("  [ ");
    for (int i = 0; i < pieceToPlace; i++) {
      outprint(padTo("" + currPath[i], 4));
    }
    outprintln("]");
    long c = found;
    if (pieceToPlace < dimension) {
      if (to - from >= dimension - pieceToPlace) {
        if (rowOfPiece[workingArray[from]] == pieceToPlace) {
          int count;
          currPath[pieceToPlace] = workingArray[from];
          currIndToWrite = to;
          count = applyFiltersDebug(currPath[pieceToPlace], from, to);
          putPiecesOrderoptimizedRotatedDebug(
              pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          for (int i = from + 1; i < to && rowOfPiece[workingArray[i]] == pieceToPlace; i++) {
            currPath[pieceToPlace] = workingArray[i];
            currIndToWrite = to;
            count = applyFiltersDebug(currPath[pieceToPlace], from, to);
            putPiecesOrderoptimizedRotatedDebug(
                pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          }
        } else {
          deads++;
        }
      } else {
        deads++;
      }
    } else {
      if (!thisIsRotatedVersion()) {
        for (int i = 0; i < dimension; i++) {
          foundPathes[(int) found][i] = currPath[i];
        }
        found++;
        printPs();
      } else {
        deads++;
      }
    }
    c = found - c;
    outprintln(padTo("", pieceToPlace * 4) + c);
  }

  /**
   * Placing variant that stops at the first valid full placing. All loops on the different levels
   * are stopped immediately once the first solution is found.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesFirst(int pieceToPlace, int from, int to) {
    // We will check after the final found solution in the middle of the loop.
    if (to - from >= dimension - pieceToPlace) {
      int count;
      // The found == 0 has to be here because all loops being on different levels
      // have to be stopped immediately if the first solution is found.
      for (int i = from; i < to && found == 0; i++) {
        currPath[pieceToPlace] = workingArray[i];
        if (pieceToPlace == dimension - 1) {
          // When all of the pieces have been placed then we found the first valid places!
          found++;
          // Don't have to continue of course because we know the places all of the pieces!
          break;
        }
        currIndToWrite = to;
        count = applyFilters(currPath[pieceToPlace], from, to);
        putPiecesFirst(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
      }
    } else {
      deads++;
    }
  }

  /**
   * Debug variant of {@link #putPiecesFirst(int, int, int)}: also prints the placing progress, the
   * solution and the per-case counts.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesFirstDebug(int pieceToPlace, int from, int to) {
    outprint("  [ ");
    for (int i = 0; i < pieceToPlace; i++) {
      outprint(padTo("" + currPath[i], 4));
    }
    outprintln("]");
    long c = found;
    if (to - from >= dimension - pieceToPlace) {
      int count;
      for (int i = from; i < to && found == 0; i++) {
        currPath[pieceToPlace] = workingArray[i];
        if (pieceToPlace == dimension - 1) {
          found++;
          printPs();
          break;
        }
        currIndToWrite = to;
        count = applyFiltersDebug(currPath[pieceToPlace], from, to);
        putPiecesFirstDebug(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
      }
    } else {
      deads++;
    }
    c = found - c;
    outprintln(padTo("", pieceToPlace * 4) + c);
  }

  /**
   * Order-optimized first-solution placing variant (usable for queen and rook): a piece may only be
   * placed into the row that equals its level, and the search stops at the first full placing.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesFirstOrderoptimized(int pieceToPlace, int from, int to) {
    if (to - from >= dimension - pieceToPlace) {
      if (rowOfPiece[workingArray[from]] == pieceToPlace) {
        currPath[pieceToPlace] = workingArray[from];
        if (pieceToPlace == dimension - 1) {
          found++;
        } else {
          int count;
          currIndToWrite = to;
          count = applyFilters(currPath[pieceToPlace], from, to);
          putPiecesFirstOrderoptimized(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          for (int i = from + 1;
              i < to && found == 0 && rowOfPiece[workingArray[i]] == pieceToPlace;
              i++) {
            currPath[pieceToPlace] = workingArray[i];
            if (pieceToPlace == dimension - 1) {
              found++;
              break;
            }
            currIndToWrite = to;
            count = applyFilters(currPath[pieceToPlace], from, to);
            putPiecesFirstOrderoptimized(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          }
        }
      } else {
        deads++;
      }
    } else {
      deads++;
    }
  }

  /**
   * Debug variant of {@link #putPiecesFirstOrderoptimized(int, int, int)}: also prints the placing
   * progress, the solution and the per-case counts.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesFirstOrderoptimizedDebug(int pieceToPlace, int from, int to) {
    outprint("  [ ");
    for (int i = 0; i < pieceToPlace; i++) {
      outprint(padTo("" + currPath[i], 4));
    }
    outprintln("]");
    long c = found;
    if (to - from >= dimension - pieceToPlace) {
      if (rowOfPiece[workingArray[from]] == pieceToPlace) {
        currPath[pieceToPlace] = workingArray[from];
        if (pieceToPlace == dimension - 1) {
          found++;
        } else {
          int count;
          currIndToWrite = to;
          count = applyFiltersDebug(currPath[pieceToPlace], from, to);
          putPiecesFirstOrderoptimizedDebug(
              pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          for (int i = from + 1;
              i < to && found == 0 && rowOfPiece[workingArray[i]] == pieceToPlace;
              i++) {
            currPath[pieceToPlace] = workingArray[i];
            if (pieceToPlace == dimension - 1) {
              found++;
              break;
            }
            currIndToWrite = to;
            count = applyFiltersDebug(currPath[pieceToPlace], from, to);
            putPiecesFirstOrderoptimizedDebug(
                pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          }
        }
      } else {
        deads++;
      }
    } else {
      deads++;
    }
    c = found - c;
    outprintln(padTo("", pieceToPlace * 4) + c);
  }

  /**
   * First-solution placing variant for initial placings. When there are preplaced pieces, this
   * checks whether the current level already holds a placing before running the core search, and it
   * accounts for the still-remaining preplaced items when deciding whether enough places are left.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesFirstP(int pieceToPlace, int from, int to) {
    // The ( to - from ) difference could be less than the actual so the count of the
    // remaining items (not placed yet) has to be added to it.
    // This is an element of an array because this number is different by the current
    // piece to place!
    if (to - from + placingsRemainings[pieceToPlace] >= dimension - pieceToPlace) {
      // Do the core only if there is no already placed piece.
      if (placingsArray[pieceToPlace] == -1) {
        int count;
        for (int i = from; i < to && found == 0; i++) {
          currPath[pieceToPlace] = workingArray[i];
          if (pieceToPlace == dimension - 1) {
            found++;
            break;
          }
          currIndToWrite = to;
          count = applyFilters(currPath[pieceToPlace], from, to);
          putPiecesFirstP(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
        }
      } else {
        // Placing the item to the currPath located in the placings!
        currPath[pieceToPlace] = placingsArray[pieceToPlace];
        if (pieceToPlace == dimension - 1) {
          found++;
        } else {
          // Jump to the next piece placing immediately and using the same input sequence.
          putPiecesFirstP(pieceToPlace + 1, from, to);
        }
      }
    } else {
      deads++;
    }
  }

  /**
   * Order-optimized first-solution placing variant for initial placings. Combines the row-per-level
   * optimization with respecting the preplaced pieces.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesFirstOrderoptimizedP(int pieceToPlace, int from, int to) {
    if (to - from + placingsRemainings[pieceToPlace] >= dimension - pieceToPlace) {
      if (placingsArray[pieceToPlace] == -1) {
        if (rowOfPiece[workingArray[from]] == pieceToPlace) {
          currPath[pieceToPlace] = workingArray[from];
          if (pieceToPlace == dimension - 1) {
            found++;
          } else {
            int count;
            currIndToWrite = to;
            count = applyFilters(currPath[pieceToPlace], from, to);
            putPiecesFirstOrderoptimizedP(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
            for (int i = from + 1;
                i < to && found == 0 && rowOfPiece[workingArray[i]] == pieceToPlace;
                i++) {
              currPath[pieceToPlace] = workingArray[i];
              if (pieceToPlace == dimension - 1) {
                found++;
                break;
              }
              currIndToWrite = to;
              count = applyFilters(currPath[pieceToPlace], from, to);
              putPiecesFirstOrderoptimizedP(
                  pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
            }
          }
        } else {
          deads++;
        }
      } else {
        currPath[pieceToPlace] = placingsArray[pieceToPlace];
        if (pieceToPlace == dimension - 1) {
          found++;
        } else {
          putPiecesFirstOrderoptimizedP(pieceToPlace + 1, from, to);
        }
      }
    } else {
      deads++;
    }
  }

  /**
   * All-solutions placing variant for initial placings. Respects the preplaced pieces at each level
   * and accounts for the still-remaining preplaced items in the enough-places check.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesAllP(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (placingsArray[pieceToPlace] == -1) {
        if (to - from + placingsRemainings[pieceToPlace] >= dimension - pieceToPlace) {
          int count;
          for (int i = from; i < to; i++) {
            currPath[pieceToPlace] = workingArray[i];
            currIndToWrite = to;
            count = applyFilters(currPath[pieceToPlace], from, to);
            putPiecesAllP(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          }
        } else {
          deads++;
        }
      } else {
        currPath[pieceToPlace] = placingsArray[pieceToPlace];
        putPiecesAllP(pieceToPlace + 1, from, to);
      }
    } else {
      found++;
    }
  }

  /**
   * Order-optimized all-solutions placing variant for initial placings. Combines the row-per-level
   * optimization with respecting the preplaced pieces.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesOrderoptimizedP(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (placingsArray[pieceToPlace] == -1) {
        if (to - from + placingsRemainings[pieceToPlace] >= dimension - pieceToPlace) {
          if (rowOfPiece[workingArray[from]] == pieceToPlace) {
            int count;
            currPath[pieceToPlace] = workingArray[from];
            currIndToWrite = to;
            count = applyFilters(currPath[pieceToPlace], from, to);
            putPiecesOrderoptimizedP(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
            for (int i = from + 1; i < to && rowOfPiece[workingArray[i]] == pieceToPlace; i++) {
              currPath[pieceToPlace] = workingArray[i];
              currIndToWrite = to;
              count = applyFilters(currPath[pieceToPlace], from, to);
              putPiecesOrderoptimizedP(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
            }
          } else {
            deads++;
          }
        } else {
          deads++;
        }
      } else {
        currPath[pieceToPlace] = placingsArray[pieceToPlace];
        putPiecesOrderoptimizedP(pieceToPlace + 1, from, to);
      }
    } else {
      found++;
    }
  }

  /**
   * All-solutions placing variant for initial placings that only counts non-rotated completed
   * placings, storing the accepted ones into {@link State#foundPathes}.
   *
   * @param pieceToPlace the index of the next chess piece to place
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesAllRotatedP(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (placingsArray[pieceToPlace] == -1) {
        if (to - from + placingsRemainings[pieceToPlace] >= dimension - pieceToPlace) {
          int count;
          for (int i = from; i < to; i++) {
            currPath[pieceToPlace] = workingArray[i];
            currIndToWrite = to;
            count = applyFilters(currPath[pieceToPlace], from, to);
            putPiecesAllRotatedP(pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
          }
        } else {
          deads++;
        }
      } else {
        currPath[pieceToPlace] = placingsArray[pieceToPlace];
        putPiecesAllRotatedP(pieceToPlace + 1, from, to);
      }
    } else {
      if (!thisIsRotatedVersion()) {
        for (int i = 0; i < dimension; i++) {
          foundPathes[(int) found][i] = currPath[i];
        }
        found++;
      } else {
        deads++;
      }
    }
  }

  /**
   * Order-optimized all-solutions placing variant for initial placings that only counts non-rotated
   * completed placings, storing the accepted ones into {@link State#foundPathes}.
   *
   * @param pieceToPlace the index of the next chess piece to place (also the expected row)
   * @param from the start index (inclusive) in the working array of the usable positions
   * @param to the end index (exclusive) in the working array of the usable positions
   */
  static void putPiecesOrderoptimizedRotatedP(int pieceToPlace, int from, int to) {
    if (pieceToPlace < dimension) {
      if (placingsArray[pieceToPlace] == -1) {
        if (to - from + placingsRemainings[pieceToPlace] >= dimension - pieceToPlace) {
          if (rowOfPiece[workingArray[from]] == pieceToPlace) {
            int count;
            currPath[pieceToPlace] = workingArray[from];
            currIndToWrite = to;
            count = applyFilters(currPath[pieceToPlace], from, to);
            putPiecesOrderoptimizedRotatedP(
                pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
            for (int i = from + 1; i < to && rowOfPiece[workingArray[i]] == pieceToPlace; i++) {
              currPath[pieceToPlace] = workingArray[i];
              currIndToWrite = to;
              count = applyFilters(currPath[pieceToPlace], from, to);
              putPiecesOrderoptimizedRotatedP(
                  pieceToPlace + 1, currIndToWrite - count, currIndToWrite);
            }
          } else {
            deads++;
          }
        } else {
          deads++;
        }
      } else {
        currPath[pieceToPlace] = placingsArray[pieceToPlace];
        putPiecesOrderoptimizedRotatedP(pieceToPlace + 1, from, to);
      }
    } else {
      if (!thisIsRotatedVersion()) {
        for (int i = 0; i < dimension; i++) {
          foundPathes[(int) found][i] = currPath[i];
        }
        found++;
      } else {
        deads++;
      }
    }
  }

  /**
   * The standard backtracking placing method. It tries every field of the row belonging to the
   * current piece and recurses into the next piece when the field is not attacked by any already
   * placed piece.
   *
   * @param pieceToPlace the index of the next chess piece (and its row) to place
   */
  static void putPiecesOriginal(int pieceToPlace) {
    if (pieceToPlace < dimension) {
      for (int i = pieceToPlace * dimension; i < (pieceToPlace + 1) * dimension; i++) {
        if (notFilteredOriginal(i, pieceToPlace)) {
          currPath[pieceToPlace] = i;
          putPiecesOriginal(pieceToPlace + 1);
        }
      }
    } else {
      found++;
    }
  }

  /**
   * Decides whether a candidate field is free from the attacks of the already placed pieces in the
   * standard solution.
   *
   * @param element the candidate field to test
   * @param pieceToPlace the number of pieces already placed (levels to check against)
   * @return {@code true} if no already placed piece attacks the candidate field
   */
  static boolean notFilteredOriginal(int element, int pieceToPlace) {
    for (int i = 0; i < pieceToPlace; i++) {
      if (isFiltered[currPath[i]][element]) {
        deads++;
        return false;
      }
    }
    return true;
  }
}
