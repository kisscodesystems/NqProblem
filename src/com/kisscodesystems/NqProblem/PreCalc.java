package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.ConsoleIo.*;
import static com.kisscodesystems.NqProblem.Const.*;
import static com.kisscodesystems.NqProblem.Filters.*;
import static com.kisscodesystems.NqProblem.State.*;

/**
 * Precalculations: the derived dimension values, the attacking map, the rotation maps and the
 * filterings of the preplaced pieces.
 */
final class PreCalc {

  /**
   * Performs the precalculations for a given dimension: resets the counters, caches the derived
   * dimension values, allocates and initializes the placings, current path, working array and the
   * row/column caches, then precalculates the attacking map.
   *
   * @param d the dimension of the chessboard to prepare for
   */
  static void preCalcStuffs(int d) {
    // These two have to be 0 at the begin.
    found = 0;
    deads = 0;
    // This is obvious but needed for the testing mode where we can
    // initialize the whole process multiple times and in different dimensions.
    dimension = d;
    // To cache these values.
    dimensionD = dimension * dimension;
    dimensionP1 = dimension + 1;
    dimensionM1 = dimension - 1;
    // It will contain the preplaced chess pieces. -1 means: there is no piece to place initially.
    placingsArray = new int[dimension];
    for (int i = 0; i < dimension; i++) {
      placingsArray[i] = -1;
    }
    // The container of the found solutions is empty by default.
    currPath = new int[dimension];
    // The working array has to contain the original representation of the chessboard.
    workingArray = new int[workingArrayLength];
    for (int i = 0; i < dimensionD; i++) {
      workingArray[i] = i;
    }
    // By default, we want to write the next not filtered position into dimensionD,
    // but this will be redefined in each run of the putPieces method.
    currIndToWrite = dimensionD;
    // The row and col of the pieces will also be precalculated and cached.
    rowOfPiece = new int[dimensionD];
    colOfPiece = new int[dimensionD];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        rowOfPiece[i * dimension + j] = i;
        colOfPiece[i * dimension + j] = j;
      }
    }
    // The attacking map will be precalculated this time.
    preCalcAttackints();
  }

  /**
   * Precalculates the attacking map into {@link #isFiltered}: a map holding, for every field, the
   * true/false attacking value against every other field.
   *
   * <p>These values are computed only once instead of millions of times. The forward direction
   * (lower to higher fields) is calculated first because the backward direction depends on what
   * kind of hits we search for. For ordered solutions we hit forward regularly and backward
   * everything; for the others (all/first) we hit forward and backward regularly. For example (4x4,
   * queen) the 2nd field hits the 10th field: for ordered solutions the 10th field then hits all
   * lower fields, while for the other cases the relation is symmetric between the two fields. (This
   * map stores a one-to-one relation at a time.)
   */
  static void preCalcAttackints() {
    // The place of the precalculated attackings will be the isFiltered array.
    isFiltered = new boolean[dimensionD][dimensionD];
    // We will calculate the attackings in forward direction from every piece to
    // every other and higher pieces.
    // ( Calculations from pieces to every other lower pieces will be below. )
    // **
    for (int i = 0; i < dimensionD; i++) {
      // The queen and the bishop can attack:
      // - - - - X - - - -
      // - - - x - - - - -
      // - - x - - - - - -
      // - x - - - - - - -
      // x - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      if (QUEEN.equals(pieces) || BISHOP.equals(pieces)) {
        for (int j = i + dimensionM1; j < dimensionD; j += dimensionM1) {
          if (colOfPiece[j] < colOfPiece[i]) {
            isFiltered[i][j] = true;
          } else {
            break;
          }
        }
      }
      // The queen and the rook can attack:
      // - - - - X - - - -
      // - - - - x - - - -
      // - - - - x - - - -
      // - - - - x - - - -
      // - - - - x - - - -
      // - - - - x - - - -
      // - - - - x - - - -
      // - - - - x - - - -
      // - - - - x - - - -
      if (QUEEN.equals(pieces) || ROOK.equals(pieces)) {
        for (int j = i + dimension; j < dimensionD; j += dimension) {
          isFiltered[i][j] = true;
        }
      }
      // The queen and the bishop can attack:
      // - - - - X - - - -
      // - - - - - x - - -
      // - - - - - - x - -
      // - - - - - - - x -
      // - - - - - - - - x
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      if (QUEEN.equals(pieces) || BISHOP.equals(pieces)) {
        for (int j = i + dimensionP1; j < dimensionD; j += dimensionP1) {
          if (colOfPiece[j] > colOfPiece[i]) {
            isFiltered[i][j] = true;
          } else {
            break;
          }
        }
      }
      // The queen and the rook can attack:
      // x x x x X x x x x
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      if (QUEEN.equals(pieces) || ROOK.equals(pieces)) {
        for (int j = rowOfPiece[i] * dimension; j < rowOfPiece[i] * dimension + dimension; j++) {
          isFiltered[i][j] = true;
        }
      }
      // The bishops can attack only themselves vertically:
      // - - - - X - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      if (BISHOP.equals(pieces)) {
        isFiltered[i][i] = true;
      }
      // Super pieces can attack as a knight:
      // - - - - X - - - -
      // - - x - - - x - -
      // - - - x - x - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      // - - - - - - - - -
      if (SUPER.equals(kinds)) {
        if (colOfPiece[i] > 1 && rowOfPiece[i] < dimension - 1) {
          isFiltered[i][i + dimension - 2] = true;
        }
        if (colOfPiece[i] > 0 && rowOfPiece[i] < dimension - 2) {
          isFiltered[i][i + 2 * dimension - 1] = true;
        }
        if (colOfPiece[i] < dimension - 1 && rowOfPiece[i] < dimension - 2) {
          isFiltered[i][i + 2 * dimension + 1] = true;
        }
        if (colOfPiece[i] < dimension - 2 && rowOfPiece[i] < dimension - 1) {
          isFiltered[i][i + dimension + 2] = true;
        }
      }
      // Awesome pieces can attack as a knight but till the edge of the chessboard:
      // - - - - X - - - -
      // - - x - - - x - -
      // x - - x - x - - x
      // - - - - - - - - -
      // - - x - - - x - -
      // - - - - - - - - -
      // - x - - - - - x -
      // - - - - - - - - -
      // x - - - - - - - x
      else if (AWESOME.equals(kinds)) {
        if (colOfPiece[i] > 1 && rowOfPiece[i] < dimension - 1) {
          for (int j = i + dimension - 2; j < dimensionD; j += (dimension - 2)) {
            if (colOfPiece[j] < colOfPiece[i]) {
              isFiltered[i][j] = true;
            } else {
              break;
            }
          }
        }
        if (colOfPiece[i] > 0 && rowOfPiece[i] < dimension - 2) {
          for (int j = i + 2 * dimension - 1; j < dimensionD; j += (2 * dimension - 1)) {
            if (colOfPiece[j] < colOfPiece[i]) {
              isFiltered[i][j] = true;
            } else {
              break;
            }
          }
        }
        if (colOfPiece[i] < dimension - 1 && rowOfPiece[i] < dimension - 2) {
          for (int j = i + 2 * dimension + 1; j < dimensionD; j += (2 * dimension + 1)) {
            if (colOfPiece[j] > colOfPiece[i]) {
              isFiltered[i][j] = true;
            } else {
              break;
            }
          }
        }
        if (colOfPiece[i] < dimension - 2 && rowOfPiece[i] < dimension - 1) {
          for (int j = i + dimension + 2; j < dimensionD; j += (dimension + 2)) {
            if (colOfPiece[j] > colOfPiece[i]) {
              isFiltered[i][j] = true;
            } else {
              break;
            }
          }
        }
      }
    }
    // ***
    // ordered solutions means: there are no backward possibilities to place!
    // If there are preplaced pieces then the else case will be done and the
    // makeHitsOrderoptimized will be called later.
    // x x x x x x x x x
    // x x x x x x x x x
    // x x x x x x x x x
    // x x x x x x x x x
    // x x x x x X - - -
    // - - - - - - - - -
    // - - - - - - - - -
    // - - - - - - - - -
    // - - - - - - - - -
    if (ORDERED.equals(hits) && placings.equals(defaultPlacings)) {
      makeHitsOrderoptimized();
    }
    // In case of all or first solutions, the same as between ** and *** but in
    // backward direction. ( [ j ] [ i ] instead of [ i ] [ j ] )
    else {
      for (int i = 0; i < dimensionD; i++) {
        if (QUEEN.equals(pieces) || BISHOP.equals(pieces)) {
          for (int j = i + dimensionM1; j < dimensionD; j += dimensionM1) {
            if (colOfPiece[j] < colOfPiece[i]) {
              isFiltered[j][i] = true;
            } else {
              break;
            }
          }
        }
        if (QUEEN.equals(pieces) || ROOK.equals(pieces)) {
          for (int j = i + dimension; j < dimensionD; j += dimension) {
            isFiltered[j][i] = true;
          }
        }
        if (QUEEN.equals(pieces) || BISHOP.equals(pieces)) {
          for (int j = i + dimensionP1; j < dimensionD; j += dimensionP1) {
            if (colOfPiece[j] > colOfPiece[i]) {
              isFiltered[j][i] = true;
            } else {
              break;
            }
          }
        }
        if (QUEEN.equals(pieces) || ROOK.equals(pieces)) {
          for (int j = rowOfPiece[i] * dimension; j < rowOfPiece[i] * dimension + dimension; j++) {
            isFiltered[j][i] = true;
          }
        }
        if (SUPER.equals(kinds)) {
          if (colOfPiece[i] > 1 && rowOfPiece[i] < dimension - 1) {
            isFiltered[i + dimension - 2][i] = true;
          }
          if (colOfPiece[i] > 0 && rowOfPiece[i] < dimension - 2) {
            isFiltered[i + 2 * dimension - 1][i] = true;
          }
          if (colOfPiece[i] < dimension - 1 && rowOfPiece[i] < dimension - 2) {
            isFiltered[i + 2 * dimension + 1][i] = true;
          }
          if (colOfPiece[i] < dimension - 2 && rowOfPiece[i] < dimension - 1) {
            isFiltered[i + dimension + 2][i] = true;
          }
        } else if (AWESOME.equals(kinds)) {
          if (colOfPiece[i] > 1 && rowOfPiece[i] < dimension - 1) {
            for (int j = i + dimension - 2; j < dimensionD; j += (dimension - 2)) {
              if (colOfPiece[j] < colOfPiece[i]) {
                isFiltered[j][i] = true;
              } else {
                break;
              }
            }
          }
          if (colOfPiece[i] > 0 && rowOfPiece[i] < dimension - 2) {
            for (int j = i + 2 * dimension - 1; j < dimensionD; j += (2 * dimension - 1)) {
              if (colOfPiece[j] < colOfPiece[i]) {
                isFiltered[j][i] = true;
              } else {
                break;
              }
            }
          }
          if (colOfPiece[i] < dimension - 1 && rowOfPiece[i] < dimension - 2) {
            for (int j = i + 2 * dimension + 1; j < dimensionD; j += (2 * dimension + 1)) {
              if (colOfPiece[j] > colOfPiece[i]) {
                isFiltered[j][i] = true;
              } else {
                break;
              }
            }
          }
          if (colOfPiece[i] < dimension - 2 && rowOfPiece[i] < dimension - 1) {
            for (int j = i + dimension + 2; j < dimensionD; j += (dimension + 2)) {
              if (colOfPiece[j] > colOfPiece[i]) {
                isFiltered[j][i] = true;
              } else {
                break;
              }
            }
          }
        }
      }
    }
  }

  /**
   * Marks the ordered-optimized backward attackings in {@link #isFiltered}. This has to be here
   * because in the ordered case there is no real backward attacking: every field is considered to
   * attack backward toward all fields with a lower or equal index.
   */
  static void makeHitsOrderoptimized() {
    for (int i = 0; i < dimensionD; i++) {
      for (int j = i; j < dimensionD; j++) {
        isFiltered[j][i] = true;
      }
    }
  }

  /**
   * Precalculates all the rotated versions of the original sequence so that already found but
   * rotated versions can be searched for. For a 3x3 board the original positions are 0 1 2 / 3 4 5
   * / 6 7 8, and the seven rotated/mirrored index maps are computed from that. Also initializes
   * {@link State#foundPathes}.
   */
  static void preCalcRotatings() {
    // rot 1.
    // 2 1 0
    // 5 4 3
    // 8 7 6
    rotatedIndexes1 = new int[dimensionD];
    int counter = 0;
    for (int i = dimension - 1; i < dimensionD; i += dimension) {
      for (int j = i; j > i - dimension; j--) {
        rotatedIndexes1[counter] = j;
        counter++;
      }
    }
    // rot 2.
    // 6 7 8
    // 3 4 5
    // 0 1 2
    rotatedIndexes2 = new int[dimensionD];
    counter = 0;
    for (int i = dimensionD - dimension; i >= 0; i -= dimension) {
      for (int j = i; j < i + dimension; j++) {
        rotatedIndexes2[counter] = j;
        counter++;
      }
    }
    // rot 3.
    // 8 7 6
    // 5 4 3
    // 2 1 0
    rotatedIndexes3 = new int[dimensionD];
    counter = 0;
    for (int i = dimensionD - 1; i >= 0; i--) {
      rotatedIndexes3[counter] = i;
      counter++;
    }
    // rot 4.
    // 0 3 6
    // 1 4 7
    // 2 5 8
    rotatedIndexes4 = new int[dimensionD];
    counter = 0;
    for (int i = 0; i < dimension; i++) {
      for (int j = i; j < dimensionD; j += dimension) {
        rotatedIndexes4[counter] = j;
        counter++;
      }
    }
    // rot 5.
    // 8 5 2
    // 7 4 1
    // 6 3 0
    rotatedIndexes5 = new int[dimensionD];
    counter = 0;
    for (int i = dimensionD - 1; i >= dimensionD - dimension; i--) {
      for (int j = i; j >= 0; j -= dimension) {
        rotatedIndexes5[counter] = j;
        counter++;
      }
    }
    // rot 6.
    // 6 3 0
    // 7 4 1
    // 8 5 2
    rotatedIndexes6 = new int[dimensionD];
    counter = 0;
    for (int i = dimensionD - dimension; i < dimensionD; i++) {
      for (int j = i; j >= 0; j -= dimension) {
        rotatedIndexes6[counter] = j;
        counter++;
      }
    }
    // rot 7.
    // 2 5 8
    // 1 4 7
    // 0 3 6
    rotatedIndexes7 = new int[dimensionD];
    counter = 0;
    for (int i = dimension - 1; i >= 0; i--) {
      for (int j = i; j < dimensionD; j += dimension) {
        rotatedIndexes7[counter] = j;
        counter++;
      }
    }
    // We will initialize the container of the already found paths.
    // This will contain the solutions which cannot be transformed into each other.
    foundPathes = new int[workingArrayLength][dimension];
  }

  /**
   * Precalculates the filterings for the preplaced pieces. When initially placed pieces are
   * specified, they are filtered here: after determining the free places depending on the initial
   * placings, the further placings of the other chess pieces can be started. If two preplaced
   * pieces attack each other, prints an error and terminates the program.
   *
   * @return the beginning index of the working array to be read from by the placing methods
   */
  static int preCalcFilterings() {
    // This int will be used later: when the count of remaining items will be stored by level
    // ( by currPiecePos values ). It will be increased when a valid preplaced position is found.
    int remaining = 0;
    // Do if this is not empty. ( Failsafe, this case shouldn't be used if this is empty. )
    if (!placings.equals(defaultPlacings)) {
      // The pieces to be stored.
      String[] array = placings.split(",");
      for (int i = 0; i < array.length; i++) {
        try {
          // This tries to be the next placing.
          int intPlacing = Integer.parseInt(array[i]);
          if (intPlacing > -1 && intPlacing < dimension * dimension) {
            // This is a valid integer position.
            // We are going to check it: this piece can hit any previously defined chess piece.
            for (int j = 0; j < dimension; j++) {
              if (placingsArray[j] != -1) {
                if (isFiltered[placingsArray[j]][intPlacing]) {
                  // If yes: then a message will appear to the user because 0 valid positions will
                  // be possible!
                  // Any of the combinations will be invalid because we have found 2 pieces: they
                  // always will hit
                  // each other so 0 valid position would be found, we won't search at all at this
                  // time.
                  outprintln("\nError while placing the given pieces.");
                  outprintln(
                      "0 position has been found because "
                          + intPlacing
                          + " hits "
                          + placingsArray[j]
                          + ".");
                  System.exit(1);
                }
              }
            }
            // If we are here then the current position ( intPlacing ) can be stored as the next
            // preplaced piece on the board.
            if (BISHOP.equals(pieces)) {
              placingsArray[remaining] = intPlacing;
            } else {
              placingsArray[rowOfPiece[intPlacing]] = intPlacing;
            }
            // The final value of remaining items can be increased now because this is a valid
            // position to preplace above.
            remaining++;
          } else {
            // If the specified value can be parsed into an integer but this is too small or too
            // big,
            // then
            // it will be ignored and a message goes to the user.
            outprintln("\nGot an integer but this is too small or too big. (index: " + i + ")");
          }
        } catch (Exception e) {
          // If an invalid integer is specified then it will be ignored and a message goes to the
          // user.
          outprintln(
              "\nException during parsing an integer value, this value won't be used. (index: "
                  + i
                  + ")");
        }
      }
    }
    // The array of the remaining items by levels (currPiecePos).
    placingsRemainings = new int[dimension];
    if (remaining > 0) {
      outprint("\n Preplaced chess pieces are:");
      // If there are any preplaced pieces then it calculates the count of remaining items by
      // levels.
      for (int i = 0; i < dimension; i++) {
        if (placingsArray[i] != -1) {
          outprint(" " + placingsArray[i]);
          placingsRemainings[i] = remaining--;
        } else {
          placingsRemainings[i] = remaining;
        }
      }
      outprintln("");
    }
    // From and to, the indexes of the working array to use between.
    int from = 0;
    int to = dimensionD;
    // Now the initial filtering by the preplaced chess pieces!
    for (int i = 0; i < dimension; i++) {
      if (placingsArray[i] != -1) {
        int count = applyFilters(placingsArray[i], from, to);
        from = to;
        to += count;
      }
    }
    // Important! If the ordered solution is specified then it has to be applied now!
    if (ORDERED.equals(hits)) {
      makeHitsOrderoptimized();
    }
    // To initialize the writing position into the workingArray.
    currIndToWrite = to;
    // It is the beginning of the working array to be read from.
    return from;
  }
}
