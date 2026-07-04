package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.ConsoleIo.*;
import static com.kisscodesystems.NqProblem.Const.*;
import static com.kisscodesystems.NqProblem.State.*;
import static com.kisscodesystems.NqProblem.Utils.*;

/** Human-readable output: solution boards, filtering traces, the attacking map and the usage. */
final class Print {

  /**
   * Prints the debug or info trace of a filtering step: the positions to filter, the current piece
   * and the resulting filtered positions.
   *
   * @param currPiecePos the board position of the piece whose filtering is being traced
   * @param from the start index (inclusive) in the working array of the positions to filter
   * @param to the end index (exclusive) in the working array of the positions to filter
   */
  static void tracePs(int currPiecePos, int from, int to) {
    outprintln(
        "                              |"
            + " ----------+----------------------------------------------------------------------------");
    int counter = 0;
    if (from < to) {
      outprint("                              | to filter : [ ");
      for (int i = from; i < to; i++) {
        outprint("" + workingArray[i]);
        counter++;
        if (counter % 10 == 0) {
          outprint("\n                              |            ");
        }
        if (i != to - 1) {
          outprint(" , ");
        }
      }
      outprintln(" ]");
    } else {
      outprintln("                              | to filter : [ ]");
    }
    outprintln("                              | Piece     : " + currPiecePos);
    if (to < currIndToWrite) {
      counter = 0;
      outprint("                              | filtered  : [ ");
      for (int i = to; i < currIndToWrite; i++) {
        outprint("" + workingArray[i]);
        counter++;
        if (counter % 10 == 0) {
          outprint("\n                              |            ");
        }
        if (i != currIndToWrite - 1) {
          outprint(" , ");
        }
      }
      outprintln(" ]");
    } else {
      outprintln("                              | filtered  : [ ]");
    }
  }

  /**
   * Prints the current solution ({@link #currPath}): first the raw path, then the board where the
   * pieces are shown (uppercase for preplaced pieces) and empty fields are shown with an asterisk.
   */
  static void printPs() {
    outprintln("");
    outprint(" | ");
    for (int j = 0; j < dimension; j++) {
      outprint(currPath[j] + " | ");
    }
    boolean elementFound = false;
    for (int i = 0; i < dimensionD; i++) {
      if (i % dimension == 0) {
        outprintln("");
      }
      elementFound = false;
      for (int j = 0; j < dimension; j++) {
        if (currPath[j] == i) {
          elementFound = true;
          break;
        }
      }
      if (elementFound) {
        boolean found2 = false;
        for (int j = 0; j < dimension; j++) {
          if (placingsArray[j] == i) {
            found2 = true;
            break;
          }
        }
        outprint(" " + (found2 ? pieces.toUpperCase() : pieces));
      } else {
        outprint(" *");
      }
    }
    outprintln("");
  }

  /**
   * Prints the precalculated attacking map. The full map is printed only up to dimension 6; above
   * that only a note is printed.
   */
  static void printIsFiltered() {
    outprintln("");
    if (dimension < 7) {
      outprintln("The currently used attacking map is:");
      outprint("   ");
      for (int j = 0; j < dimensionD; j++) {
        outprint(padTo("" + j, 3));
      }
      outprintln("");
      for (int i = 0; i < dimensionD; i++) {
        outprint(padTo("" + i, 3));
        for (int j = 0; j < dimensionD; j++) {
          outprint(padTo("" + (isFiltered[i][j] ? "+" : " "), 3));
        }
        outprintln("");
      }
    } else {
      outprintln("Attacking map  is too  large so it");
      outprintln("could be printed under dimension 7");
    }
    int[] attacks = new int[dimensionD];
    for (int i = 0; i < dimensionD; i++) {
      for (int j = 0; j < dimensionD; j++) {
        if (isFiltered[i][j]) {
          attacks[i] += 1;
        }
      }
    }
  }

  /**
   * Prints the usage/help. Called when requested or when the first parameter is not one of the
   * expected values.
   */
  static void printUsage() {
    outprintln("");
    outprintln("This program was born to play around the N Queens Problem.");
    outprintln("Version: " + VERSION + ", released: " + RELEASED);
    outprintln("");
    outprintln("  We  give  a  solution  which   considers  the  problem  from  a  whole");
    outprintln("  different   aspect  and  splits the  actual  calculation from the real");
    outprintln("  rules  of the chess  game. In this way, the calculations  of rooks and");
    outprintln("  bishops are also available as well as their super or awesome versions.");
    outprintln(" (super means that the pieces can also attack as a knight and)");
    outprintln(" (awesome means the above but till the edge of the chessborad)");
    outprintln("  Preplaced chess pieces are also available to solve N Queens");
    outprintln("  Completion problem.");
    outprintln("  Note: the board in size N is an ordered sequence between 0 and N^2 - 1");
    outprintln("  Read more: https://kcsops.kisscodesystems.com");
    outprintln("");
    outprintln("Parameters:");
    outprintln("                                                                 default");
    outprintln("  0 mode      [ o           i          t              ?,h,help ]       ?");
    outprintln("  1 dimension [ 1->n int    1->n int   13->18 int              ]      15");
    outprintln("  2 pieces    [ q           q,r,b      q                       ]       q");
    outprintln("  3 kinds     [ r           r,s,a      r                       ]       r");
    outprintln("  4 hits      [ o           o,a,f      o                       ]       o");
    outprintln("  5 threads   [ 1           1->n int   1                       ]       1");
    outprintln("  6 uniques   [ n           n,y        n                       ]       n");
    outprintln("  7 log       [ n           d,i,n      n                       ]       n");
    outprintln("  8 placings  [ empty       int,int... empty                   ]   empty");
    outprintln("");
    outprintln("  mode        o : original  , i : improved  , t : testing  , x : default");
    outprintln("  pieces      q : queen     , r : rook      , b : bishop   , x : default");
    outprintln("  kinds       r : regular   , s : super     , a : awesome  , x : default");
    outprintln("  hits        o : ordered   , a : all       , f : first    , x : default");
    outprintln("  uniques     n : no        , y : yes                      , x : default");
    outprintln("  log         n : no        , i : info      , d : debug    , x : default");
    outprintln("  placings    1 or more integer values separated by , char , or    empty");
    outprintln("");
    outprintln("Example usages:");
    outprintln("");
    outprintln("  java -jar NqProblem.jar");
    outprintln("    prints  help");
    outprintln("");
    outprintln("  java -jar NqProblem.jar o");
    outprintln("    runs the  original solution  using the");
    outprintln("    default values of the other parameters");
    outprintln("");
    outprintln("  java -jar NqProblem.jar i 16 b x a");
    outprintln("    runs  the improved  version (i) of  the  calculation and uses");
    outprintln("    16x16  chess  board to  place 16 (16) bishops (b)  onto it in");
    outprintln("    the default kind of chess pieces (x) to get all solutions (a)");
    outprintln("");
    outprintln("  java -jar NqProblem.jar i 13 q s f 1 n n 166,2,18");
    outprintln("    runs the improved version  (i) of  the  calculation and uses");
    outprintln("    13x13  chess  board to  place 13 (13) queens (q)  onto it in");
    outprintln("    the super kind of chess pieces (s) to get first solution (f)");
    outprintln("    using 1 (1) thread to calculate and the found versions won't");
    outprintln("    be different (n)  and not uses  any logging (n)  to find the");
    outprintln("    solution  for  the  initially  placed  pieces  (to 166,2,18)");
    outprintln("");
  }
}
