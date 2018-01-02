/*
** NqProblemExtended application.
**
** Description     : This program was born to play around the N Queens Problem.
**                   We give a solution which  considers the problem from a whole
**                   different  aspect and splits the actual calculation from the
**                   real rules of the chess game. In this way, the calculations
**                   of rooks and bishops are also available as well as their
**                   super or awesome versions.
**                  (super means that the pieces can also attack as a knight and)
**                  (awesome means the above but till the edge of the chessboard)
**                   Preplaced chess pieces are also available to solve N Queens
**                   Completion problem.
**
** Published       : 01.01.2018
**
** Current version : 1.0
**
** Developed by    : Jozsef Kiss
**                   KissCode Systems Kft
**                   <https://openso.kisscodesystems.com>
**
** Change log       : 1.0 - 01.01.2018
**                   Initial release.
**
** See NqProblemExtended txt or pdf for mode information.
*/
package com . kisscodesystems . NqProblemExtended ;
import java . lang . System ;
/*
** This is the only class except the inner one.
*/
public final class NqMain
{
/*
** Constants.
*/
/*
** Version and release data informations.
*/
  private static final String VERSION = "1.0" ;
  private static final String RELEASED = "01-01-2018" ;
/*
** The options usable as parameters.
*/
  private static final String HELP1 = "?" ;
  private static final String HELP2 = "h" ;
  private static final String HELP3 = "help" ;
  private static final String ORIGINAL = "o" ;
  private static final String IMPROVED = "i" ;
  private static final String TESTING = "t" ;
  private static final String QUEEN = "q" ;
  private static final String ROOK = "r" ;
  private static final String BISHOP = "b" ;
  private static final String REGULAR = "r" ;
  private static final String SUPER = "s" ;
  private static final String AWESOME = "a" ;
  private static final String ORDERED = "o" ;
  private static final String ALL = "a" ;
  private static final String FIRST = "f" ;
  private static final String YES = "y" ;
  private static final String NO = "n" ;
  private static final String DEBUG = "d" ;
  private static final String INFO = "i" ;
/*
** If the given value is not correct, these are the default values to use.
*/
  private static final String defaultMode = HELP1 ;
  private static final int defaultDimension = 15 ;
  private static final String defaultPieces = QUEEN ;
  private static final String defaultKinds = REGULAR ;
  private static final String defaultHits = ORDERED ;
  private static final int defaultThreads = 1 ;
  private static final String defaultUniques = NO ;
  private static final String defaultLog = NO ;
  private static final String defaultPlacings = "" ;
/*
** Others.
*/
/*
** The properties to use initialized by the default values.
*/
  private static String mode = defaultMode ;
  private static int dimension = defaultDimension ;
  private static String pieces = defaultPieces ;
  private static String kinds = defaultKinds ;
  private static String hits = defaultHits ;
  private static int threads = defaultThreads ;
  private static String uniques = defaultUniques ;
  private static String log = defaultLog ;
  private static String placings = defaultPlacings ;
/*
** The number of found solutions,
** and the piece placings that cannot be finished or don't count
**   - in the improved versions: there are fewer place than the number of piece to put
**   - in the orderoptimized situation: the next piece cannot be placed into the next row,
**   - in the uniques situation: the piece placing found as a previously but
**     rotated solution also will be a dead solution
**   - in the original solution: whenever a hit check fails
*/
  private static int found = 0 ;
  private static int deads = 0 ;
/*
** Values depend on the actual dimension to cache these values.
*/
  private static int dimensionD = 0 ;
  private static int dimensionP1 = 0 ;
  private static int dimensionM1 = 0 ;
/*
** This is the store of the actual piece placings.
*/
  private static int [ ] currPath = null ;
/*
** This is a shadow of the above: to store the initial placings.
*/
  private static int [ ] placingsArray = null ;
/*
** We will precompute the rows and the cols of the pieces to cache these values.
** For example we have a 4x4 chessboard.
** The chessboard will be represented as:  0  1  2  3
**                                         4  5  6  7
**                                         8  9  10 11
**                                         12 13 14 15
** So the row of the 11th element is 2 and the col of this is 3.
*/
  private static int [ ] rowOfPiece = null ;
  private static int [ ] colOfPiece = null ;
/*
** During the filtering procedure, we won't return new arrays containing
** the not filtered values (not filtered means valid positions for further use).
** We will use one single array to work instead. We will use the begin and the
** end index where we have to use the workingArray working array.
** It will have an initial size which will probably be enough.
*/
  private static int currIndToWrite = 0 ;
  private static int [ ] workingArray = null ;
  private static int workingArrayLength = 100000 ;
/*
** It the found solution have to be searched as already put but rotated
** versions, the found pathes have to be saved.
*/
  private static int [ ] [ ] foundPathes = null ;
/*
** The rotated indexes also have to be precomputed.
** Here are the stores of these.
*/
  private static int [ ] rotatedIndexes1 = null ;
  private static int [ ] rotatedIndexes2 = null ;
  private static int [ ] rotatedIndexes3 = null ;
  private static int [ ] rotatedIndexes4 = null ;
  private static int [ ] rotatedIndexes5 = null ;
  private static int [ ] rotatedIndexes6 = null ;
  private static int [ ] rotatedIndexes7 = null ;
/*
** This is the part of the core logic.
** We will precompute all of the attackings depending on the input parameters.
** In this way, an attacking map will be contained in isFiltered array
** which can show what piece can attack other pieces and what are these other
** chess pieces.
** For example (4x4, queen): the 6th element will attack 9th element, so
** isFiltered [ 6 ] [ 9 ] = true
** and 7th element won't attack 8th element, so
** isFiltered [ 7 ] [ 8 ] = false
*/
  private static boolean [ ] [ ] isFiltered = null ;
/*
** The numbers of piece to place remaining.
** This value will be the count of the elements of placings.
** According to the preplaced elements, these will be integers and will be
** different by the level we currently place in.
*/
  private static int [ ] placingsRemainings = null ;
/*
** End of variables, main method. Basically, it will determine the values of the
** parameters to initialize and where to go from here.
*/
  public static void main ( String [ ] args )
  {
// The first and second parameter is essential so let's get these values.
    try
    {
      mode = args [ 0 ] ;
      if ( ! HELP1 . equals ( mode ) && ! HELP2 . equals ( mode ) && ! HELP3 . equals ( mode ) && ! ORIGINAL . equals ( mode ) && ! IMPROVED . equals ( mode ) && ! TESTING . equals ( mode ) )
      {
        mode = defaultMode ;
      }
    }
    catch ( Exception e )
    {
      mode = defaultMode ;
    }
    try
    {
      dimension = Integer . parseInt ( args [ 1 ] ) ;
      if ( dimension > 0 )
      {
        if ( TESTING . equals ( mode ) && ( dimension < 13 || dimension > 18 ) )
        {
          dimension = defaultDimension ;
        }
      }
      else
      {
        dimension = defaultDimension ;
      }
    }
    catch ( Exception e )
    {
      dimension = defaultDimension ;
    }
// Only in improved mode, there are additional parameters which can be used...
    if ( IMPROVED . equals ( mode ) )
    {
      try
      {
        pieces = args [ 2 ] ;
        if ( ! QUEEN . equals ( pieces ) && ! ROOK . equals ( pieces ) && ! BISHOP . equals ( pieces ) )
        {
          pieces = defaultPieces ;
        }
      }
      catch ( Exception e )
      {
        pieces = defaultPieces ;
      }
      try
      {
        kinds = args [ 3 ] ;
        if ( ! REGULAR . equals ( kinds ) && ! SUPER . equals ( kinds ) && ! AWESOME . equals ( kinds ) )
        {
          kinds = defaultKinds ;
        }
      }
      catch ( Exception e )
      {
        kinds = defaultKinds ;
      }
      try
      {
        hits = args [ 4 ] ;
        if ( ! ORDERED . equals ( hits ) && ! ALL . equals ( hits ) && ! FIRST . equals ( hits ) )
        {
          hits = defaultHits ;
        }
      }
      catch ( Exception e )
      {
        hits = defaultHits ;
      }
      try
      {
        threads = Integer . parseInt ( args [ 5 ] ) ;
        if ( threads < 1 )
        {
          threads = defaultThreads ;
        }
      }
      catch ( Exception e )
      {
        threads = defaultThreads ;
      }
      try
      {
        uniques = args [ 6 ] ;
        if ( ! YES . equals ( uniques ) && ! NO . equals ( uniques ) )
        {
          uniques = defaultUniques ;
        }
      }
      catch ( Exception e )
      {
        uniques = defaultUniques ;
      }
      try
      {
        log = args [ 7 ] ;
        if ( ! DEBUG . equals ( log ) && ! INFO . equals ( log ) && ! NO . equals ( log ) )
        {
          log = defaultLog ;
        }
      }
      catch ( Exception e )
      {
        log = defaultLog ;
      }
      try
      {
        placings = args [ 8 ] ;
      }
      catch ( Exception e )
      {
        placings = defaultPlacings ;
      }
    }
// .. otherwise, these default values will be used no matter what are the values given by the user.
    else
    {
      pieces = defaultPieces ;
      kinds = defaultKinds ;
      hits = defaultHits ;
      threads = defaultThreads ;
      uniques = defaultUniques ;
      log = defaultLog ;
      placings = defaultPlacings ;
      if ( args . length > 2 )
      {
        outprintln ( "Warning: original and testing modes accept dimension only!" ) ;
      }
    }
// Sometimes the correction of the aboves is necessary. So, let's do it and warn the user about this.
    if ( ( threads > 1 && FIRST . equals ( hits ) ) || ( threads > 1 && YES . equals ( uniques ) ) || ( threads > 1 && ! placings . equals ( defaultPlacings ) ) )
    {
      threads = 1 ;
      outprintln ( "Warning: 1 thread  is possible  to use when we will" ) ;
      outprintln ( "         search  for the  first  valid  position or" ) ;
      outprintln ( "         the rotated versions wont'be differents or" ) ;
      outprintln ( "         preplaced  chess  pieces   are  specified." ) ;
    }
    if ( YES . equals ( uniques ) && FIRST . equals ( hits ) )
    {
      uniques = NO ;
      outprintln ( "Warning: uniques version is not possible when the" ) ;
      outprintln ( "         first  valid  position  is searched for." ) ;
    }
    if ( threads > 1 && DEBUG . equals ( log ) )
    {
      log = INFO ;
      outprintln ( "Warning: debug  is not possible when" ) ;
      outprintln ( "         multithreaded is requested." ) ;
    }
    if ( ! NO . equals ( log ) && ! placings . equals ( defaultPlacings ) )
    {
      log = NO ;
      outprintln ( "Warning: logging is not possible when default placings are used." ) ;
    }
// OK, we can start now because we have every information to calculate.
// Informing the user about the parameter to use.
    outprintln ( "" ) ;
    outprintln ( "Let's  calculate the positions of the chess" ) ;
    outprintln ( "pieces  when they don't  attack each other!" ) ;
    outprintln ( "" ) ;
    outprintln ( "  0 mode      (original,improved,testing) : " + mode ) ;
    outprintln ( "  1 dimension (a positive integer)        : " + dimension ) ;
    outprintln ( "  2 pieces    (queen,rook,bishop)         : " + pieces ) ;
    outprintln ( "  3 kinds     (regular,super,awesome)     : " + kinds ) ;
    outprintln ( "  4 hits      (ordered,all,first)         : " + hits ) ;
    outprintln ( "  5 threads   (a positive integer)        : " + threads ) ;
    outprintln ( "  6 uniques   (no,yes)                    : " + uniques ) ;
    outprintln ( "  7 log       (no,info,debug)             : " + log ) ;
    outprintln ( "  8 placings  (ints separated by ; char)  : " + placings ) ;
    outprintln ( "" ) ;
// The starter timestamp has to be stored to display the calculation time
// elapsed at the end.
    long startDate = System . currentTimeMillis ( ) ;
    outprintln ( "Started : " + new java . util . Date ( ) ) ;
// Call of the original, improved or testing version.
// If none of these is possible then the usage will be printed.
    if ( ORIGINAL . equals ( mode ) )
    {
      doOriginal ( ) ;
    }
    else if ( IMPROVED . equals ( mode ) )
    {
      doImproved ( startDate ) ;
    }
    else if ( TESTING . equals ( mode ) )
    {
      doTesting ( ) ;
    }
    else
    {
      printUsage ( ) ;
    }
// In case of original and improved modes, a footer will be printed containing
// for example the found solutions, the dead cases or the elapsed time.
    if ( ORIGINAL . equals ( mode ) || IMPROVED . equals ( mode ) )
    {
      long endDate = System . currentTimeMillis ( ) ;
      outprintln ( "" ) ;
      outprintln ( "  " + found ) ;
      outprintln ( "  " + ( found == 1 ? "position has been found," : "positions have been found" ) ) ;
      if ( deads != 0 )
      {
        outprintln ( "  all attempts: " + ( found + deads ) ) ;
        outprintln ( "              ( " + ( ( double ) ( ( int ) ( ( double ) found / ( found + deads ) * 10000 ) ) / 100 ) + "% success )" ) ;
      }
      outprintln ( "  in " + calcElapsed ( endDate - startDate ) ) ;
      outprintln ( "   ( " + ( endDate - startDate ) + " )" ) ;
    }
  }
/*
** To increment the found solutions from the worker threads.
*/
  private static synchronized void foundInc ( int f )
  {
    found += f ;
  }
/*
** To increment the deads from the worker threads.
*/
  private static synchronized void deadsInc ( int d )
  {
    deads += d ;
  }
/*
** Precalculations.
*/
  private static void preCalcStuffs ( int d )
  {
// These two have to be 0 at the begin.
    found = 0 ;
    deads = 0 ;
// This is obvious but needed for the testing mode where we can
// initialize the whole process in multiple times and in different dimensions.
    dimension = d ;
// To cache these values.
    dimensionD = dimension * dimension ;
    dimensionP1 = dimension + 1 ;
    dimensionM1 = dimension - 1 ;
// It will contain the preplaced chess pieces. -1 means: there is no piece to place by initial.
    placingsArray = new int [ dimension ] ;
    for ( int i = 0 ; i < dimension ; i ++ )
    {
      placingsArray [ i ] = - 1 ;
    }
// The container of the found solutions is empty by default.
    currPath = new int [ dimension ] ;
// The working array has to contain the original representation of the chessboard.
    workingArray = new int [ workingArrayLength ] ;
    for ( int i = 0 ; i < dimensionD ; i ++ )
    {
      workingArray [ i ] = i ;
    }
// By default, we want to write the next not filtered position into dimensionD,
// but this will be redefined in each run of the putPieces method.
    currIndToWrite = dimensionD ;
// The row and col of the pieces will also be precalculated and cached.
    rowOfPiece = new int [ dimensionD ] ;
    colOfPiece = new int [ dimensionD ] ;
    for ( int i = 0 ; i < dimension ; i ++ )
    {
      for ( int j = 0 ; j < dimension ; j ++ )
      {
        rowOfPiece [ i * dimension + j ] = i ;
        colOfPiece [ i * dimension + j ] = j ;
      }
    }
// The attacking map will be precalculated this time.
    preCalcAttackints ( ) ;
  }
/*
** Mainly: we will calculate a map that contains the attacking
** value (true or false) to every single fields by every other
** single fields. We don't want to calculate this value between
** the same fields millions of time.
** We will calculate only in the direction lower to higher pieces because
** the back way depends on what kind of hits we search for.
** Ordered: hit forward regulary and hit backward everything.
** Others: hit forward and backward regulary.
** For example:
**   In 4x4 and queen situation: the 2th place hits the 10th place.
**   But, if we would like to get the ordered solutions, then the 10th place
**   has to hit not just the 2th place but all of the lower places!
**   Hitting backward is not possible while searching for ordered solutions.
**   In every other situation ( all solutions, first solution ) if the 2th place
**   hits the 10th place then the 10th place hist the 2th place.
**   ( There are more places to hit but remember:
**     this attacking map calculates 1-1 relation at a time. )
*/
  private static void preCalcAttackints ( )
  {
// The place of the precalculated attackins will be the isFiltered array.
    isFiltered = new boolean [ dimensionD ] [ dimensionD ] ;
// We will calculate the attackings in forward direction from every piece to
// every other and higher pieces.
// ( Calculations from pieces to every other lower pieces will be below. )
// **
    for ( int i = 0 ; i < dimensionD ; i ++ )
    {
// The queen and the bishop can attack:
//   - - - - X - - - -
//   - - - x - - - - -
//   - - x - - - - - -
//   - x - - - - - - -
//   x - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
      if ( QUEEN . equals ( pieces ) || BISHOP . equals ( pieces ) )
      {
        for ( int j = i + dimensionM1 ; j < dimensionD ; j += dimensionM1 )
        {
          if ( colOfPiece [ j ] < colOfPiece [ i ] )
          {
            isFiltered [ i ] [ j ] = true ;
          }
          else
          {
            break ;
          }
        }
      }
// The queen and the rook can attack:
//   - - - - X - - - -
//   - - - - x - - - -
//   - - - - x - - - -
//   - - - - x - - - -
//   - - - - x - - - -
//   - - - - x - - - -
//   - - - - x - - - -
//   - - - - x - - - -
//   - - - - x - - - -
      if ( QUEEN . equals ( pieces ) || ROOK . equals ( pieces ) )
      {
        for ( int j = i + dimension ; j < dimensionD ; j += dimension )
        {
          isFiltered [ i ] [ j ] = true ;
        }
      }
// The queen and the bishop can attack:
//   - - - - X - - - -
//   - - - - - x - - -
//   - - - - - - x - -
//   - - - - - - - x -
//   - - - - - - - - x
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
      if ( QUEEN . equals ( pieces ) || BISHOP . equals ( pieces ) )
      {
        for ( int j = i + dimensionP1 ; j < dimensionD ; j += dimensionP1 )
        {
          if ( colOfPiece [ j ] > colOfPiece [ i ] )
          {
            isFiltered [ i ] [ j ] = true ;
          }
          else
          {
            break ;
          }
        }
      }
// The queen and the rook can attack:
//   x x x x X x x x x
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
      if ( QUEEN . equals ( pieces ) || ROOK . equals ( pieces ) )
      {
        for ( int j = rowOfPiece [ i ] * dimension ; j < rowOfPiece [ i ] * dimension + dimension ; j ++ )
        {
          isFiltered [ i ] [ j ] = true ;
        }
      }
// The bishops can attack only themselves vertically:
//   - - - - X - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
      if ( BISHOP . equals ( pieces ) )
      {
        isFiltered [ i ] [ i ] = true ;
      }
// Super peaces can attack as a knight:
//   - - - - X - - - -
//   - - x - - - x - -
//   - - - x - x - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
      if ( SUPER . equals ( kinds ) )
      {
        if ( colOfPiece [ i ] > 1 && rowOfPiece [ i ] < dimension - 1 )
        {
          isFiltered [ i ] [ i + dimension - 2 ] = true ;
        }
        if ( colOfPiece [ i ] > 0 && rowOfPiece [ i ] < dimension - 2 )
        {
          isFiltered [ i ] [ i + 2 * dimension - 1 ] = true ;
        }
        if ( colOfPiece [ i ] < dimension - 1 && rowOfPiece [ i ] < dimension - 2 )
        {
          isFiltered [ i ] [ i + 2 * dimension + 1 ] = true ;
        }
        if ( colOfPiece [ i ] < dimension - 2 && rowOfPiece [ i ] < dimension - 1 )
        {
          isFiltered [ i ] [ i + dimension + 2 ] = true ;
        }
      }
// Awesome peaces can attack as a knight but till the edge of the chessboard:
//   - - - - X - - - -
//   - - x - - - x - -
//   x - - x - x - - x
//   - - - - - - - - -
//   - - x - - - x - -
//   - - - - - - - - -
//   - x - - - - - x -
//   - - - - - - - - -
//   x - - - - - - - x
      else if ( AWESOME . equals ( kinds ) )
      {
        if ( colOfPiece [ i ] > 1 && rowOfPiece [ i ] < dimension - 1 )
        {
          for ( int j = i + dimension - 2 ; j < dimensionD ; j += ( dimension - 2 ) )
          {
            if ( colOfPiece [ j ] < colOfPiece [ i ] )
            {
              isFiltered [ i ] [ j ] = true ;
            }
            else
            {
              break ;
            }
          }
        }
        if ( colOfPiece [ i ] > 0 && rowOfPiece [ i ] < dimension - 2 )
        {
          for ( int j = i + 2 * dimension - 1 ; j < dimensionD ; j += ( 2 * dimension - 1 ) )
          {
            if ( colOfPiece [ j ] < colOfPiece [ i ] )
            {
              isFiltered [ i ] [ j ] = true ;
            }
            else
            {
              break ;
            }
          }
        }
        if ( colOfPiece [ i ] < dimension - 1 && rowOfPiece [ i ] < dimension - 2 )
        {
          for ( int j = i + 2 * dimension + 1 ; j < dimensionD ; j += ( 2 * dimension + 1 ) )
          {
            if ( colOfPiece [ j ] > colOfPiece [ i ] )
            {
              isFiltered [ i ] [ j ] = true ;
            }
            else
            {
              break ;
            }
          }
        }
        if ( colOfPiece [ i ] < dimension - 2 && rowOfPiece [ i ] < dimension - 1 )
        {
          for ( int j = i + dimension + 2 ; j < dimensionD ; j += ( dimension + 2 ) )
          {
            if ( colOfPiece [ j ] > colOfPiece [ i ] )
            {
              isFiltered [ i ] [ j ] = true ;
            }
            else
            {
              break ;
            }
          }
        }
      }
    }
// ***
// ordered solutions means: there are no backward possibilities to place!
// If there are preplaced piceces then the else case will be done and the
// makeHitsOrderoptimized will be called later.
//   x x x x x x x x x
//   x x x x x x x x x
//   x x x x x x x x x
//   x x x x x x x x x
//   x x x x x X - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
//   - - - - - - - - -
    if ( ORDERED . equals ( hits ) && placings . equals ( defaultPlacings ) )
    {
      makeHitsOrderoptimized ( ) ;
    }
// In case of all or first solutions, the same as between ** and *** but in
// backward direction. ( [ j ] [ i ] instead of [ i ] [ j ] )
    else
    {
      for ( int i = 0 ; i < dimensionD ; i ++ )
      {
        if ( QUEEN . equals ( pieces ) || BISHOP . equals ( pieces ) )
        {
          for ( int j = i + dimensionM1 ; j < dimensionD ; j += dimensionM1 )
          {
            if ( colOfPiece [ j ] < colOfPiece [ i ] )
            {
              isFiltered [ j ] [ i ] = true ;
            }
            else
            {
              break ;
            }
          }
        }
        if ( QUEEN . equals ( pieces ) || ROOK . equals ( pieces ) )
        {
          for ( int j = i + dimension ; j < dimensionD ; j += dimension )
          {
            isFiltered [ j ] [ i ] = true ;
          }
        }
        if ( QUEEN . equals ( pieces ) || BISHOP . equals ( pieces ) )
        {
          for ( int j = i + dimensionP1 ; j < dimensionD ; j += dimensionP1 )
          {
            if ( colOfPiece [ j ] > colOfPiece [ i ] )
            {
              isFiltered [ j ] [ i ] = true ;
            }
            else
            {
              break ;
            }
          }
        }
        if ( QUEEN . equals ( pieces ) || ROOK . equals ( pieces ) )
        {
          for ( int j = rowOfPiece [ i ] * dimension ; j < rowOfPiece [ i ] * dimension + dimension ; j ++ )
          {
            isFiltered [ j ] [ i ] = true ;
          }
        }
        if ( SUPER . equals ( kinds ) )
        {
          if ( colOfPiece [ i ] > 1 && rowOfPiece [ i ] < dimension - 1 )
          {
            isFiltered [ i + dimension - 2 ] [ i ] = true ;
          }
          if ( colOfPiece [ i ] > 0 && rowOfPiece [ i ] < dimension - 2 )
          {
            isFiltered [ i + 2 * dimension - 1 ] [ i ] = true ;
          }
          if ( colOfPiece [ i ] < dimension - 1 && rowOfPiece [ i ] < dimension - 2 )
          {
            isFiltered [ i + 2 * dimension + 1 ] [ i ] = true ;
          }
          if ( colOfPiece [ i ] < dimension - 2 && rowOfPiece [ i ] < dimension - 1 )
          {
            isFiltered [ i + dimension + 2 ] [ i ] = true ;
          }
        }
        else if ( AWESOME . equals ( kinds ) )
        {
          if ( colOfPiece [ i ] > 1 && rowOfPiece [ i ] < dimension - 1 )
          {
            for ( int j = i + dimension - 2 ; j < dimensionD ; j += ( dimension - 2 ) )
            {
              if ( colOfPiece [ j ] < colOfPiece [ i ] )
              {
                isFiltered [ j ] [ i ] = true ;
              }
              else
              {
                break ;
              }
            }
          }
          if ( colOfPiece [ i ] > 0 && rowOfPiece [ i ] < dimension - 2 )
          {
            for ( int j = i + 2 * dimension - 1 ; j < dimensionD ; j += ( 2 * dimension - 1 ) )
            {
              if ( colOfPiece [ j ] < colOfPiece [ i ] )
              {
                isFiltered [ j ] [ i ] = true ;
              }
              else
              {
                break ;
              }
            }
          }
          if ( colOfPiece [ i ] < dimension - 1 && rowOfPiece [ i ] < dimension - 2 )
          {
            for ( int j = i + 2 * dimension + 1 ; j < dimensionD ; j += ( 2 * dimension + 1 ) )
            {
              if ( colOfPiece [ j ] > colOfPiece [ i ] )
              {
                isFiltered [ j ] [ i ] = true ;
              }
              else
              {
                break ;
              }
            }
          }
          if ( colOfPiece [ i ] < dimension - 2 && rowOfPiece [ i ] < dimension - 1 )
          {
            for ( int j = i + dimension + 2 ; j < dimensionD ; j += ( dimension + 2 ) )
            {
              if ( colOfPiece [ j ] > colOfPiece [ i ] )
              {
                isFiltered [ j ] [ i ] = true ;
              }
              else
              {
                break ;
              }
            }
          }
        }
      }
    }
  }
/*
** This has to be here. There is no backward attacking.
*/
  private static void makeHitsOrderoptimized ( )
  {
    for ( int i = 0 ; i < dimensionD ; i ++ )
    {
      for ( int j = i ; j < dimensionD ; j ++ )
      {
        isFiltered [ j ] [ i ] = true ;
      }
    }
  }
/*
** And then: all of the rotated versions of the original sequence
** will also be calculated to be able to search for already found
** but rotated versions. The original positions (for 3x3):  0 1 2
**                                                          3 4 5
**                                                          6 7 8
*/
  private static void preCalcRotatings ( )
  {
// rot 1.
//  2 1 0
//  5 4 3
//  8 7 6
    rotatedIndexes1 = new int [ dimensionD ] ;
    int counter = 0 ;
    for ( int i = dimension - 1 ; i < dimensionD ; i += dimension )
    {
      for ( int j = i ; j > i - dimension ; j -- )
      {
        rotatedIndexes1 [ counter ] = j ;
        counter ++ ;
      }
    }
// rot 2.
//  6 7 8
//  3 4 5
//  0 1 2
    rotatedIndexes2 = new int [ dimensionD ] ;
    counter = 0 ;
    for ( int i = dimensionD - dimension ; i >= 0 ; i -= dimension )
    {
      for ( int j = i ; j < i + dimension ; j ++ )
      {
        rotatedIndexes2 [ counter ] = j ;
        counter ++ ;
      }
    }
// rot 3.
//  8 7 6
//  5 4 3
//  2 1 0
    rotatedIndexes3 = new int [ dimensionD ] ;
    counter = 0 ;
    for ( int i = dimensionD - 1 ; i >= 0 ; i -- )
    {
      rotatedIndexes3 [ counter ] = i ;
      counter ++ ;
    }
// rot 4.
//  0 3 6
//  1 4 7
//  2 5 8
    rotatedIndexes4 = new int [ dimensionD ] ;
    counter = 0 ;
    for ( int i = 0 ; i < dimension ; i ++ )
    {
      for ( int j = i ; j < dimensionD ; j += dimension )
      {
        rotatedIndexes4 [ counter ] = j ;
        counter ++ ;
      }
    }
// rot 5.
//  8 5 2
//  7 4 1
//  6 3 0
    rotatedIndexes5 = new int [ dimensionD ] ;
    counter = 0 ;
    for ( int i = dimensionD - 1 ; i >= dimensionD - dimension ; i -- )
    {
      for ( int j = i ; j >= 0 ; j -= dimension )
      {
        rotatedIndexes5 [ counter ] = j ;
        counter ++ ;
      }
    }
// rot 6.
//  6 3 0
//  7 4 1
//  8 5 2
    rotatedIndexes6 = new int [ dimensionD ] ;
    counter = 0 ;
    for ( int i = dimensionD - dimension ; i < dimensionD ; i ++ )
    {
      for ( int j = i ; j >= 0 ; j -= dimension )
      {
        rotatedIndexes6 [ counter ] = j ;
        counter ++ ;
      }
    }
// rot 7.
//  2 5 8
//  1 4 7
//  0 3 6
    rotatedIndexes7 = new int [ dimensionD ] ;
    counter = 0 ;
    for ( int i = dimension - 1 ; i >= 0 ; i -- )
    {
      for ( int j = i ; j < dimensionD ; j += dimension )
      {
        rotatedIndexes7 [ counter ] = j ;
        counter ++ ;
      }
    }
// We will initialize the container of the already found pathes.
// This will contain the solutions which cannot be transformed into each other.
    foundPathes = new int [ workingArrayLength ] [ dimension ] ;
  }
/*
** Precalculation of the placings.
** When initially placed pieces are specified, then the filtering procedure
** of them will be done. The further placings of the other chess pieces can be
** started after determining the free places depending on the initial placings.
*/
  private static int preCalcFilterings ( )
  {
// This int will be used later: when the count of remaining items will be stored by level
// ( by currPiecePos values ). It will be increased when a valid preplaced position is found.
    int remaining = 0 ;
// Do if this is not empty. ( Failsafe, this case shouldn't be used if this is empty. )
    if ( ! placings . equals ( defaultPlacings ) )
    {
// The pieces to be stored.
      String [ ] array = placings . split ( ";" ) ;
      for ( int i = 0 ; i < array . length ; i ++ )
      {
        try
        {
// This tries to be the next placing.
          int intPlacing = Integer . parseInt ( array [ i ] ) ;
          if ( intPlacing > - 1 && intPlacing < dimension * dimension )
          {
// This is a valid integer position.
// We are gonna check it: this piece can hit any previously defined chess piece.
            for ( int j = 0 ; j < dimension ; j ++ )
            {
              if ( placingsArray [ j ] != - 1 )
              {
                if ( isFiltered [ placingsArray [ j ] ] [ intPlacing ] )
                {
// If yes: then a message will be apprared to user because 0 valid positions will be possible!
// Any of the combination will be invalid because we have found 2 pieces: they always will hit
// each other so 0 valid position would be found, we won't search at all at this time.
                  outprintln ( "\nError while placing the given pieces." ) ;
                  outprintln ( "0 position has been found because " + intPlacing + " hits " + placingsArray [ j ] + "." ) ;
                  System . exit ( 1 ) ;
                }
              }
            }
// If we are here then the current position ( intPlacing ) can be stored as the next preplaced piece on the board.
            if ( BISHOP . equals ( pieces ) )
            {
              placingsArray [ remaining ] = intPlacing ;
            }
            else
            {
              placingsArray [ rowOfPiece [ intPlacing ] ] = intPlacing ;
            }
// The final value of remaining items can be increased now bcause this is a valid position to preplace above.
            remaining ++ ;
          }
          else
          {
// If the specified value can be parsed into integer bu this is too small or too big, then
// it will be ignored and a message goes to the user.
            outprintln ( "\nGot an integer but this is too small or too big. (index: " + i + ")" ) ;
          }
        }
        catch ( Exception e )
        {
// If an invalid integer is specified then it will be ignored and a message goes to the user.
          outprintln ( "\nException during parsing an integer value, this value won't be used. (index: " + i + ")" ) ;
        }
      }
    }
// The array of the remaining items by levels (currPiecePos).
    placingsRemainings = new int [ dimension ] ;
    if ( remaining > 0 )
    {
      outprint ( "\n Preplaced chess pieces are:" ) ;
// If there are any preplaced pieces then it calculates the count of remaining items by levels.
      for ( int i = 0 ; i < dimension ; i ++ )
      {
        if ( placingsArray [ i ] != - 1 )
        {
          outprint ( " " + placingsArray [ i ] ) ;
          placingsRemainings [ i ] = remaining -- ;
        }
        else
        {
          placingsRemainings [ i ] = remaining ;
        }
      }
      outprintln ( "" ) ;
    }
// From and to, the indexes of the working array to use between.
    int from = 0 ;
    int to = dimensionD ;
// Now the initial filtering by the preplaced chess pieces!
    for ( int i = 0 ; i < dimension ; i ++ )
    {
      if ( placingsArray [ i ] != - 1 )
      {
        int count = applyFilters ( placingsArray [ i ] , from , to ) ;
        from = to ;
        to += count ;
      }
    }
// Important! If the ordered solution is specified then it has to be applied now!
    if ( ORDERED . equals ( hits ) )
    {
      makeHitsOrderoptimized ( ) ;
    }
// To initialize the writing position into the workingArray.
    currIndToWrite = to ;
// It is the beginning of the working array to be read from.
    return from ;
  }
/*
** This will be used in the putPiecesXXX where the rotated versions won't be
** different. It will search the actual solution as a rotated.
*/
  private static boolean thisIsRotatedVersion ( )
  {
    boolean rotatedFound = false ;
    for ( int i = 0 ; i < found ; i ++ )
    {
      if ( thisIsRotatedVersionOf ( i , rotatedIndexes1 ) )
      {
        rotatedFound = true ;
        break ;
      }
      if ( thisIsRotatedVersionOf ( i , rotatedIndexes2 ) )
      {
        rotatedFound = true ;
        break ;
      }
      if ( thisIsRotatedVersionOf ( i , rotatedIndexes3 ) )
      {
        rotatedFound = true ;
        break ;
      }
      if ( thisIsRotatedVersionOf ( i , rotatedIndexes4 ) )
      {
        rotatedFound = true ;
        break ;
      }
      if ( thisIsRotatedVersionOf ( i , rotatedIndexes5 ) )
      {
        rotatedFound = true ;
        break ;
      }
      if ( thisIsRotatedVersionOf ( i , rotatedIndexes6 ) )
      {
        rotatedFound = true ;
        break ;
      }
      if ( thisIsRotatedVersionOf ( i , rotatedIndexes7 ) )
      {
        rotatedFound = true ;
        break ;
      }
    }
    return rotatedFound ;
  }
/*
** This is the actual searcher method between solutions.
** Every piece should be compared with every other pieces in the store
** because the sequences may be not ordered!
** Sadly, this is an expensive searching of rotated version.
*/
  private static boolean thisIsRotatedVersionOf ( int i , int [ ] indexes )
  {
    boolean matched = false ;
    for ( int j = 0 ; j < dimension ; j ++ )
    {
// Doesn't match by default.
      matched = false ;
      for ( int k = 0 ; k < dimension ; k ++ )
      {
        if ( foundPathes [ i ] [ k ] == indexes [ currPath [ j ] ] )
        {
// The first element which presents in the currently searched solution.
          matched = true ;
// The others doesn't need to be compared of course.
          break ;
        }
      }
// If this is not matched here then returning false immediately.
      if ( ! matched )
      {
        return false ;
      }
    }
// This is a rotated version because every element has a pair in this stored solution.
    return true ;
  }
/*
** Here is the implementation of our ideas.
** First: the filter methods to determine whether a position will be
** filtered by the actual position.
** We will duplicate the code because we don't want to make the
** decision of the operating mode every time, that slows us down.
*/
  private static int applyFilters ( int currPiecePos , int from , int to )
  {
// This is a reference to the array we will use.
    boolean [ ] a = isFiltered [ currPiecePos ] ;
// We want to go thru the working array between the given bounds.
    for ( int i = from ; i < to ; i ++ )
    {
// Every elements should be considered: the currPiecePos element filters
// This actual element or not.
// The result is false means:
// that actual element can be used further as a valid position to use.
      if ( ! a [ workingArray [ i ] ] )
      {
        workingArray [ currIndToWrite ] = workingArray [ i ] ;
        currIndToWrite ++ ;
      }
    }
// Returns the number of usable positions.
    return currIndToWrite - to ;
  }
  private static int applyFiltersDebug ( int currPiecePos , int from , int to )
  {
    boolean [ ] a = isFiltered [ currPiecePos ] ;
    for ( int i = from ; i < to ; i ++ )
    {
      if ( ! a [ workingArray [ i ] ] )
      {
        workingArray [ currIndToWrite ] = workingArray [ i ] ;
        currIndToWrite ++ ;
      }
    }
// Same as the above but the pieces will be traced.
    tracePs ( currPiecePos , from , to ) ;
    return currIndToWrite - to ;
  }
/*
** The main methods to calculate.
** Using just one method while searching for the positions and some
** of the code will be duplicated because of the same reason mentioned above.
*/
  private static void putPiecesAll ( int pieceToPlace , int from , int to )
  {
// The piece to place is maximum dimension - 1 means: there are pieces to put.
    if ( pieceToPlace < dimension )
    {
// Are there enough places to put all of the pieces?
      if ( to - from >= dimension - pieceToPlace )
      {
// Important, the number of places to use from the input sequence.
        int count ;
// We wanted to loop on the working array between the given bounds.
        for ( int i = from ; i < to ; i ++ )
        {
// If we are here, the next position is always valid so let's use this.
          currPath [ pieceToPlace ] = workingArray [ i ] ;
// This position always has to be set into this:
// we want to write the next sequence into the end of this current sequence.
          currIndToWrite = to ;
// This filtering process has to be done here, and the count has to be saved.
// ( The number of usable positions to use from the input. )
          count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
// Let it invoke itself: this will be the next piece put to this currently set of places.
          putPiecesAll ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
        }
      }
      else
      {
// Deads 1: there are more pieces to put than the available free places.
        deads ++ ;
      }
    }
    else
    {
// All of the pieces have been put so this is a valid solution.
      found ++ ;
    }
  }
  private static void putPiecesAllDebug ( int pieceToPlace , int from , int to )
  {
// We will print the array of pieces currently put.
    outprint ( "  [ " ) ;
    for ( int i = 0 ; i < pieceToPlace ; i ++ )
    {
      outprint ( padTo ( "" + currPath [ i ] , 4 ) ) ;
    }
    outprintln ( "]" ) ;
// We also want to display the number of found solution to this case.
    int c = found ;
    if ( pieceToPlace < dimension )
    {
      if ( to - from >= dimension - pieceToPlace )
      {
        int count ;
        for ( int i = from ; i < to ; i ++ )
        {
          currPath [ pieceToPlace ] = workingArray [ i ] ;
          currIndToWrite = to ;
          count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesAllDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
      found ++ ;
// Prints the pieces, this is a valid solution!
      printPs ( ) ;
    }
// Calculation and print of the count belongs to this case.
    c = found - c ;
    outprintln ( padTo ( "" , pieceToPlace * 4 ) + c ) ;
  }
  private static void putPiecesAllRotated ( int pieceToPlace , int from , int to )
  {
    if ( pieceToPlace < dimension )
    {
      if ( to - from >= dimension - pieceToPlace )
      {
        int count ;
        for ( int i = from ; i < to ; i ++ )
        {
          currPath [ pieceToPlace ] = workingArray [ i ] ;
          currIndToWrite = to ;
          count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesAllRotated ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
// This will be a valid solution only if this is not a rotated as the founds before.
      if ( ! thisIsRotatedVersion ( ) )
      {
// This solution has to be saved.
        for ( int i = 0 ; i < dimension ; i ++ )
        {
          foundPathes [ found ] [ i ] = currPath [ i ] ;
        }
        found ++ ;
      }
      else
      {
// Deads 2: this is also a dead solution.
        deads ++ ;
      }
    }
  }
  private static void putPiecesAllRotatedDebug ( int pieceToPlace , int from , int to )
  {
    outprint ( "  [ " ) ;
    for ( int i = 0 ; i < pieceToPlace ; i ++ )
    {
      outprint ( padTo ( "" + currPath [ i ] , 4 ) ) ;
    }
    outprintln ( "]" ) ;
    int c = found ;
    if ( pieceToPlace < dimension )
    {
      if ( to - from >= dimension - pieceToPlace )
      {
        int count ;
        for ( int i = from ; i < to ; i ++ )
        {
          currPath [ pieceToPlace ] = workingArray [ i ] ;
          currIndToWrite = to ;
          count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesAllRotatedDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
      if ( ! thisIsRotatedVersion ( ) )
      {
        for ( int i = 0 ; i < dimension ; i ++ )
        {
          foundPathes [ found ] [ i ] = currPath [ i ] ;
        }
        found ++ ;
        printPs ( ) ;
      }
      else
      {
        deads ++ ;
      }
    }
    c = found - c ;
    outprintln ( padTo ( "" , pieceToPlace * 4 ) + c ) ;
  }
  private static void putPiecesOrderoptimized ( int pieceToPlace , int from , int to )
  {
    if ( pieceToPlace < dimension )
    {
      if ( to - from >= dimension - pieceToPlace )
      {
// The rules of the chess will be applied to queen and rook.
// If we have N queens and NxN chessboard, we have to put one into all of the
// rows ( or cols ). If not, there will be the situation when 2 or more queens
// have to be put into the same column ( or row ), and that solutions won't
// count as valid.
// This is just an optimalization step and the core logic is not been built
// using this. The precalculated attacking map provides that only the ordered
// solutions will be found.
// But we find that too many invalid positions will be tested if the below rule
// doesn't applyed. So we do this optimalization to queen and rook.
// This won't work using bishops.
        if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
        {
          int count ;
// The first run happens separately to avoid the first duplicated row check.
          currPath [ pieceToPlace ] = workingArray [ from ] ;
          currIndToWrite = to ;
          count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesOrderoptimized ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          for ( int i = from + 1 ; i < to && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
          {
            currPath [ pieceToPlace ] = workingArray [ i ] ;
            currIndToWrite = to ;
            count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesOrderoptimized ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          }
        }
        else
        {
// Deads 3: the put piece is not in the expected row.
          deads ++ ;
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
      found ++ ;
    }
  }
  private static void putPiecesOrderoptimizedDebug ( int pieceToPlace , int from , int to )
  {
    outprint ( "  [ " ) ;
    for ( int i = 0 ; i < pieceToPlace ; i ++ )
    {
      outprint ( padTo ( "" + currPath [ i ] , 4 ) ) ;
    }
    outprintln ( "]" ) ;
    int c = found ;
    if ( pieceToPlace < dimension )
    {
      if ( to - from >= dimension - pieceToPlace )
      {
        if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
        {
          int count ;
          currPath [ pieceToPlace ] = workingArray [ from ] ;
          currIndToWrite = to ;
          count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesOrderoptimizedDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          for ( int i = from + 1 ; i < to && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
          {
            currPath [ pieceToPlace ] = workingArray [ i ] ;
            currIndToWrite = to ;
            count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesOrderoptimizedDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          }
        }
        else
        {
          deads ++ ;
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
      found ++ ;
      printPs ( ) ;
    }
    c = found - c ;
    outprintln ( padTo ( "" , pieceToPlace * 4 ) + c ) ;
  }
  private static void putPiecesOrderoptimizedRotated ( int pieceToPlace , int from , int to )
  {
    if ( pieceToPlace < dimension )
    {
      if ( to - from >= dimension - pieceToPlace )
      {
        if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
        {
          int count ;
          currPath [ pieceToPlace ] = workingArray [ from ] ;
          currIndToWrite = to ;
          count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesOrderoptimizedRotated ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          for ( int i = from + 1 ; i < to && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
          {
            currPath [ pieceToPlace ] = workingArray [ i ] ;
            currIndToWrite = to ;
            count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesOrderoptimizedRotated ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          }
        }
        else
        {
          deads ++ ;
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
      if ( ! thisIsRotatedVersion ( ) )
      {
        for ( int i = 0 ; i < dimension ; i ++ )
        {
          foundPathes [ found ] [ i ] = currPath [ i ] ;
        }
        found ++ ;
      }
      else
      {
        deads ++ ;
      }
    }
  }
  private static void putPiecesOrderoptimizedRotatedDebug ( int pieceToPlace , int from , int to )
  {
    outprint ( "  [ " ) ;
    for ( int i = 0 ; i < pieceToPlace ; i ++ )
    {
      outprint ( padTo ( "" + currPath [ i ] , 4 ) ) ;
    }
    outprintln ( "]" ) ;
    int c = found ;
    if ( pieceToPlace < dimension )
    {
      if ( to - from >= dimension - pieceToPlace )
      {
        if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
        {
          int count ;
          currPath [ pieceToPlace ] = workingArray [ from ] ;
          currIndToWrite = to ;
          count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesOrderoptimizedRotatedDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          for ( int i = from + 1 ; i < to && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
          {
            currPath [ pieceToPlace ] = workingArray [ i ] ;
            currIndToWrite = to ;
            count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesOrderoptimizedRotatedDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          }
        }
        else
        {
          deads ++ ;
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
      if ( ! thisIsRotatedVersion ( ) )
      {
        for ( int i = 0 ; i < dimension ; i ++ )
        {
          foundPathes [ found ] [ i ] = currPath [ i ] ;
        }
        found ++ ;
        printPs ( ) ;
      }
      else
      {
        deads ++ ;
      }
    }
    c = found - c ;
    outprintln ( padTo ( "" , pieceToPlace * 4 ) + c ) ;
  }
  private static void putPiecesFirst ( int pieceToPlace , int from , int to )
  {
// We will check after the final found solution in the middle of the loop.
    if ( to - from >= dimension - pieceToPlace )
    {
      int count ;
// The found == 0 has to be here because all loops being on different levels
// have to be stopped immediately if the first solution is found.
      for ( int i = from ; i < to && found == 0 ; i ++ )
      {
        currPath [ pieceToPlace ] = workingArray [ i ] ;
        if ( pieceToPlace == dimension - 1 )
        {
// When all of the pieces have been placed then we found the first valid places!
          found ++ ;
// Don't have to continue of course because we know the places all of the pieces!
          break ;
        }
        currIndToWrite = to ;
        count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
        putPiecesFirst ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
      }
    }
    else
    {
      deads ++ ;
    }
  }
  private static void putPiecesFirstDebug ( int pieceToPlace , int from , int to )
  {
    outprint ( "  [ " ) ;
    for ( int i = 0 ; i < pieceToPlace ; i ++ )
    {
      outprint ( padTo ( "" + currPath [ i ] , 4 ) ) ;
    }
    outprintln ( "]" ) ;
    int c = found ;
    if ( to - from >= dimension - pieceToPlace )
    {
      int count ;
      for ( int i = from ; i < to && found == 0 ; i ++ )
      {
        currPath [ pieceToPlace ] = workingArray [ i ] ;
        if ( pieceToPlace == dimension - 1 )
        {
          found ++ ;
          printPs ( ) ;
          break ;
        }
        currIndToWrite = to ;
        count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
        putPiecesFirstDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
      }
    }
    else
    {
      deads ++ ;
    }
    c = found - c ;
    outprintln ( padTo ( "" , pieceToPlace * 4 ) + c ) ;
  }
  private static void putPiecesFirstOrderoptimized ( int pieceToPlace , int from , int to )
  {
    if ( to - from >= dimension - pieceToPlace )
    {
      if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
      {
        currPath [ pieceToPlace ] = workingArray [ from ] ;
        if ( pieceToPlace == dimension - 1 )
        {
          found ++ ;
        }
        else
        {
          int count ;
          currIndToWrite = to ;
          count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesFirstOrderoptimized ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          for ( int i = from + 1 ; i < to && found == 0 && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
          {
            currPath [ pieceToPlace ] = workingArray [ i ] ;
            if ( pieceToPlace == dimension - 1 )
            {
              found ++ ;
              break ;
            }
            currIndToWrite = to ;
            count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesFirstOrderoptimized ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          }
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
      deads ++ ;
    }
  }
  private static void putPiecesFirstOrderoptimizedDebug ( int pieceToPlace , int from , int to )
  {
    outprint ( "  [ " ) ;
    for ( int i = 0 ; i < pieceToPlace ; i ++ )
    {
      outprint ( padTo ( "" + currPath [ i ] , 4 ) ) ;
    }
    outprintln ( "]" ) ;
    int c = found ;
    if ( to - from >= dimension - pieceToPlace )
    {
      if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
      {
        currPath [ pieceToPlace ] = workingArray [ from ] ;
        if ( pieceToPlace == dimension - 1 )
        {
          found ++ ;
        }
        else
        {
          int count ;
          currIndToWrite = to ;
          count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesFirstOrderoptimizedDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          for ( int i = from + 1 ; i < to && found == 0 && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
          {
            currPath [ pieceToPlace ] = workingArray [ i ] ;
            if ( pieceToPlace == dimension - 1 )
            {
              found ++ ;
              break ;
            }
            currIndToWrite = to ;
            count = applyFiltersDebug ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesFirstOrderoptimizedDebug ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          }
        }
      }
      else
      {
        deads ++ ;
      }
    }
    else
    {
      deads ++ ;
    }
    c = found - c ;
    outprintln ( padTo ( "" , pieceToPlace * 4 ) + c ) ;
  }
/*
** For initial placings. If we have pieces to place initially then the put
** methods have to check: are there any placings before.
*/
  private static void putPiecesFirstP ( int pieceToPlace , int from , int to )
  {
// The ( to - from ) difference could be less than the actual so the count of the
// remaining items (not placed yet) has to be added to it.
// This is an element of an array because this number is different by the current
// piece to place!
    if ( to - from + placingsRemainings [ pieceToPlace ] >= dimension - pieceToPlace )
    {
// Do the core only if there is no already placed piece.
      if ( placingsArray [ pieceToPlace ] == - 1 )
      {
        int count ;
        for ( int i = from ; i < to && found == 0 ; i ++ )
        {
          currPath [ pieceToPlace ] = workingArray [ i ] ;
          if ( pieceToPlace == dimension - 1 )
          {
            found ++ ;
            break ;
          }
          currIndToWrite = to ;
          count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
          putPiecesFirstP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
        }
      }
      else
      {
// Placing the item to the currPath  located in the pacings!
        currPath [ pieceToPlace ] = placingsArray [ pieceToPlace ] ;
        if ( pieceToPlace == dimension - 1 )
        {
          found ++ ;
        }
        else
        {
// Jump to the next piece placing immediately and using the same input sequence.
          putPiecesFirstP ( pieceToPlace + 1 , from , to ) ;
        }
      }
    }
    else
    {
      deads ++ ;
    }
  }
  private static void putPiecesFirstOrderoptimizedP ( int pieceToPlace , int from , int to )
  {
    if ( to - from + placingsRemainings [ pieceToPlace ] >= dimension - pieceToPlace )
    {
      if ( placingsArray [ pieceToPlace ] == - 1 )
      {
        if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
        {
          currPath [ pieceToPlace ] = workingArray [ from ] ;
          if ( pieceToPlace == dimension - 1 )
          {
            found ++ ;
          }
          else
          {
            int count ;
            currIndToWrite = to ;
            count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesFirstOrderoptimizedP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
            for ( int i = from + 1 ; i < to && found == 0 && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
            {
              currPath [ pieceToPlace ] = workingArray [ i ] ;
              if ( pieceToPlace == dimension - 1 )
              {
                found ++ ;
                break ;
              }
              currIndToWrite = to ;
              count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
              putPiecesFirstOrderoptimizedP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
            }
          }
        }
        else
        {
          deads ++ ;
        }
      }
      else
      {
        currPath [ pieceToPlace ] = placingsArray [ pieceToPlace ] ;
        if ( pieceToPlace == dimension - 1 )
        {
          found ++ ;
        }
        else
        {
          putPiecesFirstOrderoptimizedP ( pieceToPlace + 1 , from , to ) ;
        }
      }
    }
    else
    {
      deads ++ ;
    }
  }
  private static void putPiecesAllP ( int pieceToPlace , int from , int to )
  {
    if ( pieceToPlace < dimension )
    {
      if ( placingsArray [ pieceToPlace ] == - 1 )
      {
        if ( to - from + placingsRemainings [ pieceToPlace ] >= dimension - pieceToPlace )
        {
          int count ;
          for ( int i = from ; i < to ; i ++ )
          {
            currPath [ pieceToPlace ] = workingArray [ i ] ;
            currIndToWrite = to ;
            count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesAllP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          }
        }
        else
        {
          deads ++ ;
        }
      }
      else
      {
        currPath [ pieceToPlace ] = placingsArray [ pieceToPlace ] ;
        putPiecesAllP ( pieceToPlace + 1 , from , to ) ;
      }
    }
    else
    {
      found ++ ;
    }
  }
  private static void putPiecesOrderoptimizedP ( int pieceToPlace , int from , int to )
  {
    if ( pieceToPlace < dimension )
    {
      if ( placingsArray [ pieceToPlace ] == - 1 )
      {
        if ( to - from + placingsRemainings [ pieceToPlace ] >= dimension - pieceToPlace )
        {
          if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
          {
            int count ;
            currPath [ pieceToPlace ] = workingArray [ from ] ;
            currIndToWrite = to ;
            count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesOrderoptimizedP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
            for ( int i = from + 1 ; i < to && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
            {
              currPath [ pieceToPlace ] = workingArray [ i ] ;
              currIndToWrite = to ;
              count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
              putPiecesOrderoptimizedP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
            }
          }
          else
          {
            deads ++ ;
          }
        }
        else
        {
          deads ++ ;
        }
      }
      else
      {
        currPath [ pieceToPlace ] = placingsArray [ pieceToPlace ] ;
        putPiecesOrderoptimizedP ( pieceToPlace + 1 , from , to ) ;
      }
    }
    else
    {
      found ++ ;
    }
  }
  private static void putPiecesAllRotatedP ( int pieceToPlace , int from , int to )
  {
    if ( pieceToPlace < dimension )
    {
      if ( placingsArray [ pieceToPlace ] == - 1 )
      {
        if ( to - from + placingsRemainings [ pieceToPlace ] >= dimension - pieceToPlace )
        {
          int count ;
          for ( int i = from ; i < to ; i ++ )
          {
            currPath [ pieceToPlace ] = workingArray [ i ] ;
            currIndToWrite = to ;
            count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesAllRotatedP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
          }
        }
        else
        {
          deads ++ ;
        }
      }
      else
      {
        currPath [ pieceToPlace ] = placingsArray [ pieceToPlace ] ;
        putPiecesAllRotatedP ( pieceToPlace + 1 , from , to ) ;
      }
    }
    else
    {
      if ( ! thisIsRotatedVersion ( ) )
      {
        for ( int i = 0 ; i < dimension ; i ++ )
        {
          foundPathes [ found ] [ i ] = currPath [ i ] ;
        }
        found ++ ;
      }
      else
      {
        deads ++ ;
      }
    }
  }
  private static void putPiecesOrderoptimizedRotatedP ( int pieceToPlace , int from , int to )
  {
    if ( pieceToPlace < dimension )
    {
      if ( placingsArray [ pieceToPlace ] == - 1 )
      {
        if ( to - from + placingsRemainings [ pieceToPlace ] >= dimension - pieceToPlace )
        {
          if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
          {
            int count ;
            currPath [ pieceToPlace ] = workingArray [ from ] ;
            currIndToWrite = to ;
            count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
            putPiecesOrderoptimizedRotatedP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
            for ( int i = from + 1 ; i < to && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
            {
              currPath [ pieceToPlace ] = workingArray [ i ] ;
              currIndToWrite = to ;
              count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
              putPiecesOrderoptimizedRotatedP ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
            }
          }
          else
          {
            deads ++ ;
          }
        }
        else
        {
          deads ++ ;
        }
      }
      else
      {
        currPath [ pieceToPlace ] = placingsArray [ pieceToPlace ] ;
        putPiecesOrderoptimizedRotatedP ( pieceToPlace + 1 , from , to ) ;
      }
    }
    else
    {
      if ( ! thisIsRotatedVersion ( ) )
      {
        for ( int i = 0 ; i < dimension ; i ++ )
        {
          foundPathes [ found ] [ i ] = currPath [ i ] ;
        }
        found ++ ;
      }
      else
      {
        deads ++ ;
      }
    }
  }
/*
** This is the default solution we use as the standard.
** This one is quicker than the default backtrack solution because it uses our
** precalculated attacking map instead of calculating the hit state from every
** single already put queens by coordinates.
*/
  private static void doOriginal ( )
  {
    preCalcStuffs ( dimension ) ;
    putPiecesOriginal ( 0 ) ;
  }
  private static void putPiecesOriginal ( int pieceToPlace )
  {
    if ( pieceToPlace < dimension )
    {
      for ( int i = pieceToPlace * dimension ; i < ( pieceToPlace + 1 ) * dimension ; i ++ )
      {
        if ( notFilteredOriginal ( i , pieceToPlace ) )
        {
          currPath [ pieceToPlace ] = i ;
          putPiecesOriginal ( pieceToPlace + 1 ) ;
        }
      }
    }
    else
    {
      found ++ ;
    }
  }
  private static boolean notFilteredOriginal ( int element , int pieceToPlace )
  {
    for ( int i = 0 ; i < pieceToPlace ; i ++ )
    {
      if ( isFiltered [ currPath [ i ] ] [ element ] )
      {
        deads ++ ;
        return false ;
      }
    }
    return true ;
  }
/*
** End of the standard solution.
*/
/*
** The improved version of chess pieces searching
** which is able to find positions of queens, rooks and bishops.
** There will be several decisions depending on bishops.
** When possible, the orderoptimized logic will run but this is not possible
** in case of using bishops.
*/
  private static void doImproved01 ( )
  {
    outprintln ( "\n ( case : improved 01 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesFirstDebug ( 0 , 0 , dimensionD ) ;
    }
    else
    {
      putPiecesFirstOrderoptimizedDebug ( 0 , 0 , dimensionD ) ;
    }
  }
  private static void doImproved02 ( long startDate )
  {
    outprintln ( "\n ( case : improved 02 )" ) ;
    int foundToThisPiece = 0 ;
    int count ;
    if ( BISHOP . equals ( pieces ) )
    {
      for ( int i = 0 ; i < dimensionD && found == 0 ; i ++ )
      {
        currPath [ 0 ] = workingArray [ i ] ;
        currIndToWrite = dimensionD ;
        count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
        putPiecesFirst ( 1 , currIndToWrite - count , currIndToWrite ) ;
        outprintln ( " (" + i + ") found: " + found + "  c" + ( found - foundToThisPiece ) + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
        foundToThisPiece = found ;
      }
    }
    else
    {
      for ( int i = 0 ; i < dimension && found == 0 ; i ++ )
      {
        currPath [ 0 ] = workingArray [ i ] ;
        currIndToWrite = dimensionD ;
        count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
        putPiecesFirstOrderoptimized ( 1 , currIndToWrite - count , currIndToWrite ) ;
        outprintln ( " (" + i + ") found: " + found + "  c" + ( found - foundToThisPiece ) + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
        foundToThisPiece = found ;
      }
    }
    if ( found != 0 )
    {
      printPs ( ) ;
    }
  }
  private static void doImproved03 ( )
  {
    outprintln ( "\n ( case : improved 03 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesFirst ( 0 , 0 , dimensionD ) ;
    }
    else
    {
      putPiecesFirstOrderoptimized ( 0 , 0 , dimensionD ) ;
    }
    if ( found != 0 )
    {
      printPs ( ) ;
    }
  }
  private static void doImproved04 ( )
  {
    outprintln ( "\n ( case : improved 04 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesAllDebug ( 0 , 0 , dimensionD ) ;
    }
    else
    {
      putPiecesOrderoptimizedDebug ( 0 , 0 , dimensionD ) ;
    }
  }
  private static void doImproved05 ( long startDate )
  {
    outprintln ( "\n ( case : improved 05 )" ) ;
    int foundToThisPiece = 0 ;
    int count ;
    if ( BISHOP . equals ( pieces ) )
    {
      for ( int i = 0 ; i < dimensionD ; i ++ )
      {
        currPath [ 0 ] = workingArray [ i ] ;
        currIndToWrite = dimensionD ;
        count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
        putPiecesAll ( 1 , currIndToWrite - count , currIndToWrite ) ;
        outprintln ( " (" + i + ") found: " + found + "  c" + ( found - foundToThisPiece ) + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
        foundToThisPiece = found ;
      }
    }
    else
    {
      for ( int i = 0 ; i < dimension ; i ++ )
      {
        currPath [ 0 ] = workingArray [ i ] ;
        currIndToWrite = dimensionD ;
        count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
        putPiecesOrderoptimized ( 1 , currIndToWrite - count , currIndToWrite ) ;
        outprintln ( " (" + i + ") found: " + found + "  c" + ( found - foundToThisPiece ) + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
        foundToThisPiece = found ;
      }
    }
  }
  private static void doImproved06 ( )
  {
    outprintln ( "\n ( case : improved 06 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesAll ( 0 , 0 , dimensionD ) ;
    }
    else
    {
      putPiecesOrderoptimized ( 0 , 0 , dimensionD ) ;
    }
  }
  private static void doImproved07 ( )
  {
    outprintln ( "\n ( case : improved 07 )" ) ;
    putPiecesAllDebug ( 0 , 0 , dimensionD ) ;
  }
  private static void doImproved08 ( long startDate )
  {
    outprintln ( "\n ( case : improved 08 )" ) ;
    int foundToThisPiece = 0 ;
    int count ;
    for ( int i = 0 ; i < dimensionD ; i ++ )
    {
      currPath [ 0 ] = workingArray [ i ] ;
      currIndToWrite = dimensionD ;
      count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
      putPiecesAll ( 1 , currIndToWrite - count , currIndToWrite ) ;
      outprintln ( " (" + i + ") found: " + found + "  c" + ( found - foundToThisPiece ) + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
      foundToThisPiece = found ;
    }
  }
  private static void doImproved09 ( )
  {
    outprintln ( "\n ( case : improved 09 )" ) ;
    putPiecesAll ( 0 , 0 , dimensionD ) ;
  }
  private static void doImproved10 ( )
  {
    outprintln ( "\n ( case : improved 10 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesAllRotatedDebug ( 0 , 0 , dimensionD ) ;
    }
    else
    {
      putPiecesOrderoptimizedRotatedDebug ( 0 , 0 , dimensionD ) ;
    }
  }
  private static void doImproved11 ( long startDate )
  {
    outprintln ( "\n ( case : improved 11 )" ) ;
    int foundToThisPiece = 0 ;
    int count ;
    if ( BISHOP . equals ( pieces ) )
    {
      for ( int i = 0 ; i < dimensionD ; i ++ )
      {
        currPath [ 0 ] = workingArray [ i ] ;
        currIndToWrite = dimensionD ;
        count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
        putPiecesAllRotated ( 1 , currIndToWrite - count , currIndToWrite ) ;
        outprintln ( " (" + i + ") found: " + found + "  c" + ( found - foundToThisPiece ) + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
        foundToThisPiece = found ;
      }
    }
    else
    {
      for ( int i = 0 ; i < dimension ; i ++ )
      {
        currPath [ 0 ] = workingArray [ i ] ;
        currIndToWrite = dimensionD ;
        count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
        putPiecesOrderoptimizedRotated ( 1 , currIndToWrite - count , currIndToWrite ) ;
        outprintln ( " (" + i + ") found: " + found + "  c" + ( found - foundToThisPiece ) + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
        foundToThisPiece = found ;
      }
    }
  }
  private static void doImproved12 ( )
  {
    outprintln ( "\n ( case : improved 12 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesAllRotated ( 0 , 0 , dimensionD ) ;
    }
    else
    {
      putPiecesOrderoptimizedRotated ( 0 , 0 , dimensionD ) ;
    }
  }
  private static void doImproved13 ( )
  {
    outprintln ( "\n ( case : improved 13 )" ) ;
    putPiecesAllRotatedDebug ( 0 , 0 , dimensionD ) ;
  }
  private static void doImproved14 ( long startDate )
  {
    outprintln ( "\n ( case : improved 14 )" ) ;
    int foundToThisPiece = 0 ;
    int count ;
    for ( int i = 0 ; i < dimensionD ; i ++ )
    {
      currPath [ 0 ] = workingArray [ i ] ;
      currIndToWrite = dimensionD ;
      count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
      putPiecesAllRotated ( 1 , currIndToWrite - count , currIndToWrite ) ;
      outprintln ( " (" + i + ") found: " + found + "  c" + ( found - foundToThisPiece ) + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
      foundToThisPiece = found ;
    }
  }
  private static void doImproved15 ( )
  {
    outprintln ( "\n ( case : improved 15 )" ) ;
    putPiecesAllRotated ( 0 , 0 , dimensionD ) ;
  }
  private static void doImproved16 ( int from )
  {
    outprintln ( "\n ( case : improved 16 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesFirstP ( 0 , from , currIndToWrite ) ;
    }
    else
    {
      putPiecesFirstOrderoptimizedP ( 0 , from , currIndToWrite ) ;
    }
    if ( found != 0 )
    {
      printPs ( ) ;
    }
  }
  private static void doImproved17 ( int from )
  {
    outprintln ( "\n ( case : improved 17 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesAllP ( 0 , from , currIndToWrite ) ;
    }
    else
    {
      putPiecesOrderoptimizedP ( 0 , from , currIndToWrite ) ;
    }
  }
  private static void doImproved18 ( int from )
  {
    outprintln ( "\n ( case : improved 18 )" ) ;
    putPiecesAllP ( 0 , from , currIndToWrite ) ;
  }
  private static void doImproved19 ( int from )
  {
    outprintln ( "\n ( case : improved 19 )" ) ;
    if ( BISHOP . equals ( pieces ) )
    {
      putPiecesAllRotatedP ( 0 , from , currIndToWrite ) ;
    }
    else
    {
      putPiecesOrderoptimizedRotatedP ( 0 , from , currIndToWrite ) ;
    }
  }
  private static void doImproved20 ( int from )
  {
    outprintln ( "\n ( case : improved 20 )" ) ;
    putPiecesAllRotatedP ( 0 , from , currIndToWrite ) ;
  }
/*
** The improved solution to be called.
** All of the above doImprovedXX functions will be used here depending on the
** parameters of this program invoke.
*/
  private static void doImproved ( long startDate )
  {
// All of the precalculation will be done.
    preCalcStuffs ( dimension ) ;
// The attacking map will be printed if first solution is requested.
    if ( FIRST . equals ( hits ) )
    {
      printIsFiltered ( ) ;
    }
// Then, let's call the proper doImprovedXX method.
    if ( threads == 1 )
    {
      if ( placings . equals ( defaultPlacings ) )
      {
        if ( NO . equals ( uniques ) )
        {
          if ( FIRST . equals ( hits ) )
          {
            if ( DEBUG . equals ( log ) )
            {
              doImproved01 ( ) ;
            }
            else if ( INFO . equals ( log ) )
            {
              doImproved02 ( startDate ) ;
            }
            else if ( NO . equals ( log ) )
            {
              doImproved03 ( ) ;
            }
          }
          else if ( ORDERED . equals ( hits ) )
          {
            if ( DEBUG . equals ( log ) )
            {
              doImproved04 ( ) ;
            }
            else if ( INFO . equals ( log ) )
            {
              doImproved05 ( startDate ) ;
            }
            else if ( NO . equals ( log ) )
            {
              doImproved06 ( ) ;
            }
          }
          else if ( ALL . equals ( hits ) )
          {
            if ( DEBUG . equals ( log ) )
            {
              doImproved07 ( ) ;
            }
            else if ( INFO . equals ( log ) )
            {
              doImproved08 ( startDate ) ;
            }
            else if ( NO . equals ( log ) )
            {
              doImproved09 ( ) ;
            }
          }
        }
        else if ( YES . equals ( uniques ) )
        {
          preCalcRotatings ( ) ;
          if ( ORDERED . equals ( hits ) )
          {
            if ( DEBUG . equals ( log ) )
            {
              doImproved10 ( ) ;
            }
            else if ( INFO . equals ( log ) )
            {
              doImproved11 ( startDate ) ;
            }
            else if ( NO . equals ( log ) )
            {
              doImproved12 ( ) ;
            }
          }
          else if ( ALL . equals ( hits ) )
          {
            if ( DEBUG . equals ( log ) )
            {
              doImproved13 ( ) ;
            }
            else if ( INFO . equals ( log ) )
            {
              doImproved14 ( startDate ) ;
            }
            else if ( NO . equals ( log ) )
            {
              doImproved15 ( ) ;
            }
          }
        }
      }
      else
      {
        int from = preCalcFilterings ( ) ;
        if ( NO . equals ( uniques ) )
        {
          if ( FIRST . equals ( hits ) )
          {
            doImproved16 ( from ) ;
          }
          else if ( ORDERED . equals ( hits ) )
          {
            doImproved17 ( from ) ;
          }
          else if ( ALL . equals ( hits ) )
          {
            doImproved18 ( from ) ;
          }
        }
        else if ( YES . equals ( uniques ) )
        {
          preCalcRotatings ( ) ;
          if ( ORDERED . equals ( hits ) )
          {
            doImproved19 ( from ) ;
          }
          else if ( ALL . equals ( hits ) )
          {
            doImproved20 ( from ) ;
          }
        }
      }
    }
    else if ( threads > 1 )
    {
// If multiple threads is requested then it has to use this little inner class.
      final class NqWork
      implements Runnable
      {
// This is the only variable that doesn't exist in the parent.
// It stores the position of the first line put piece.
        private int pos = 0 ;
// These are familiar and hide the parent variables because these need to be
// unique in every single worker thread.
// Everything else will be used from the parent class.
        private int found = 0 ;
        private int deads = 0 ;
        private int [ ] currPath = new int [ dimension ] ;
        private int currIndToWrite = 0 ;
        private int [ ] workingArray = new int [ workingArrayLength ] ;
// Getting the position of the first piece.
        public NqWork ( int p )
        {
          pos = p ;
        }
// Let's start to work.
        @Override
        public void run ( )
        {
// The initialization of this thread.
          for ( int i = 0 ; i < dimensionD ; i ++ )
          {
            workingArray [ i ] = i ;
          }
          currIndToWrite = dimensionD ;
// We put the first piece.
          currPath [ 0 ] = workingArray [ pos ] ;
// The starter timestamp.
          long startDate = System . currentTimeMillis ( ) ;
// OK, we can do the followings:
// - info and none log levels ( no debug )
// - ordered and all solutions ( no first )
// - dimension, pieces, kind: all is possible
          if ( ORDERED . equals ( hits ) )
          {
            int count ;
            if ( INFO . equals ( log ) )
            {
              count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
              if ( BISHOP . equals ( pieces ) )
              {
                putPiecesAll ( 1 , currIndToWrite - count , currIndToWrite ) ;
              }
              else
              {
                putPiecesOrderoptimized ( 1 , currIndToWrite - count , currIndToWrite ) ;
              }
              outprintln ( " (" + pos + ") found: " + found + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
            }
            else if ( NO . equals ( log ) )
            {
              count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
              if ( BISHOP . equals ( pieces ) )
              {
                putPiecesAll ( 1 , currIndToWrite - count , currIndToWrite ) ;
              }
              else
              {
                putPiecesOrderoptimized ( 1 , currIndToWrite - count , currIndToWrite ) ;
              }
            }
          }
          else if ( ALL . equals ( hits ) )
          {
            int count ;
            if ( INFO . equals ( log ) )
            {
              count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
              putPiecesAll ( 1 , currIndToWrite - count , currIndToWrite ) ;
              outprintln ( " (" + pos + ") found: " + found + " | " + calcElapsed ( System . currentTimeMillis ( ) - startDate ) ) ;
            }
            else if ( NO . equals ( log ) )
            {
              count = applyFilters ( currPath [ 0 ] , 0 , dimensionD ) ;
              putPiecesAll ( 1 , currIndToWrite - count , currIndToWrite ) ;
            }
          }
// We have to increase these values by the found ints found on this thread.
          foundInc ( found ) ;
          deadsInc ( deads ) ;
        }
// The methods below are exactly the same as having in the parent class.
        private int applyFilters ( int currPiecePos , int from , int to )
        {
          boolean [ ] a = isFiltered [ currPiecePos ] ;
          for ( int i = from ; i < to ; i ++ )
          {
            if ( ! a [ workingArray [ i ] ] )
            {
              workingArray [ currIndToWrite ] = workingArray [ i ] ;
              currIndToWrite ++ ;
            }
          }
          return currIndToWrite - to ;
        }
        private void putPiecesAll ( int pieceToPlace , int from , int to )
        {
          if ( pieceToPlace < dimension )
          {
            if ( to - from >= dimension - pieceToPlace )
            {
              int count ;
              for ( int i = from ; i < to ; i ++ )
              {
                currPath [ pieceToPlace ] = workingArray [ i ] ;
                currIndToWrite = to ;
                count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
                putPiecesAll ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
              }
            }
            else
            {
              deads ++ ;
            }
          }
          else
          {
            found ++ ;
          }
        }
        private void putPiecesOrderoptimized ( int pieceToPlace , int from , int to )
        {
          if ( pieceToPlace < dimension )
          {
            if ( to - from >= dimension - pieceToPlace )
            {
              if ( rowOfPiece [ workingArray [ from ] ] == pieceToPlace )
              {
                int count ;
                currPath [ pieceToPlace ] = workingArray [ from ] ;
                currIndToWrite = to ;
                count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
                putPiecesOrderoptimized ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
                for ( int i = from + 1 ; i < to && rowOfPiece [ workingArray [ i ] ] == pieceToPlace ; i ++ )
                {
                  currPath [ pieceToPlace ] = workingArray [ i ] ;
                  currIndToWrite = to ;
                  count = applyFilters ( currPath [ pieceToPlace ] , from , to ) ;
                  putPiecesOrderoptimized ( pieceToPlace + 1 , currIndToWrite - count , currIndToWrite ) ;
                }
              }
              else
              {
                deads ++ ;
              }
            }
            else
            {
              deads ++ ;
            }
          }
          else
          {
            found ++ ;
          }
        }
      }
// The calculation on multiple threads will happen by using thread pool.
      java . util . concurrent . ExecutorService executorService = java . util . concurrent . Executors . newFixedThreadPool ( threads ) ;
// Ordered solutions: there is difference between bishops and others:
// The loop has to end on dimensionD in case of bishops while the others can
// stop at dimension to apply the optimalization based on the rules of chess.
      if ( ORDERED . equals ( hits ) )
      {
        if ( BISHOP . equals ( pieces ) )
        {
          for ( int i = 0 ; i < dimensionD ; i ++ )
          {
            NqWork nqWork = new NqWork ( i ) ;
            executorService . execute ( nqWork ) ;
          }
        }
        else
        {
          for ( int i = 0 ; i < dimension ; i ++ )
          {
            NqWork nqWork = new NqWork ( i ) ;
            executorService . execute ( nqWork ) ;
          }
        }
      }
      else if ( ALL . equals ( hits ) )
      {
        for ( int i = 0 ; i < dimensionD ; i ++ )
        {
          NqWork nqWork = new NqWork ( i ) ;
          executorService . execute ( nqWork ) ;
        }
      }
// We will wait for every single thread to be finished.
      executorService . shutdown ( ) ;
      while ( ! executorService . isTerminated ( ) ) { }
    }
  }
/*
** The testing mode.
** This will compare the running times of the original and the improved solution.
** Testing will do the improved algorithm on 1 thread to let the comparing be correct.
** Important!
** The running times of the original solution are in the code below!
** You have to actualize it by run them on your computer and place your running
** times into the code below and recompile this application.
** This is because of the huge differences may be in different computers.
** The below elapsed times are from a laptop, having
** - Windows 10 x64 OS
** - Intel Celeron N2840 ( 2 x 2.16GHz )
** - 8GB 1333MHz DDR3
** - 1 thread both
** More clearly: nobody wants to wait a half of a day to see the difference,
**               so the default solution won't be run, you have to do it
**               separately and update-recompile this code. (Sorry.)
*/
  private static void doTesting ( )
  {
// The dimension now is a limit: we will run every dimensions until this bound.
    int maxDimension = dimension ;
// Starting times, there will be some.
    long sDate = 0 ;
// The results of running times and result strings.
// The integer ones are to calculate rates between runnings.
// The resultTimes integers and the 3rd columns of the proper result string
// have to be equal and depend on your own runnings.
    int [ ] [ ] resultTimes = new int [ 19 ] [ 2 ] ;
    String [ ] resultStrins = new String [ 19 ] ;
// You have to actualize these lines according your own running times
// using java -jar NqProblemExtended.jar i 13 ..18 commands!
// The times have to be updated till <<EOR
    resultStrins [ 13 ] = "13  c73712     2377     ms ( 2s 377ms )          -> " ;
    resultStrins [ 14 ] = "14  c365596    15116    ms ( 15s 116ms )         -> " ;
    resultStrins [ 15 ] = "15  c2279184   104357   ms ( 1m 44s 357ms )      -> " ;
    resultStrins [ 16 ] = "16  c14772512  766915   ms ( 12m 46s 915ms )     -> " ;
    resultStrins [ 17 ] = "17  c95815104  6040290  ms ( 1h 40m 40s 290ms )  -> " ;
    resultStrins [ 18 ] = "18  c666090624 48165728 ms ( 13h 22m 45s 728ms ) -> " ;
    resultTimes [ 13 ] [ 0 ] = 2377 ;
    resultTimes [ 14 ] [ 0 ] = 15116 ;
    resultTimes [ 15 ] [ 0 ] = 104357 ;
    resultTimes [ 16 ] [ 0 ] = 766915 ;
    resultTimes [ 17 ] [ 0 ] = 6040290 ;
    resultTimes [ 18 ] [ 0 ] = 48165728 ;
// << EOR
// Prints some information to the user, this contains the info to consider the table.
    outprintln ( "" ) ;
    outprintln ( "Let's see the difference of using mode o and i." ) ;
    outprintln ( "mode o -> mode i (improved version versus original solution)" ) ;
    outprintln ( "mode o won't be run," ) ;
    outprintln ( "  the times have to be written manually to the source" ) ;
    outprintln ( "rate1: i elapsed / o elapsed" ) ;
    outprintln ( "rate2: i (k) elapsed / i (k-1) elapsed" ) ;
    outprintln ( "" ) ;
    outprintln ( "n   count      elapsed      elapsed2             -> elapsed        elapsed2           count        rate1   rate2" ) ;
// Let's do all of it to the max dimension.
    for ( int i = 13 ; i <= maxDimension ; i ++ )
    {
      sDate = System . currentTimeMillis ( ) ;
      preCalcStuffs ( i ) ;
      putPiecesOrderoptimized ( 0 , 0 , dimensionD ) ;
      resultTimes [ i ] [ 1 ] = ( int ) ( System . currentTimeMillis ( ) - sDate ) ;
      resultStrins [ i ] += padTo ( "" + resultTimes [ i ] [ 1 ] , 10 ) + "ms ( " + padTo ( calcElapsed ( resultTimes [ i ] [ 1 ] ) + " )" , 18 ) + " " + padTo ( "c" + found , 10 ) + " ( " + padTo ( "" + ( ( double ) ( ( int ) ( ( double ) resultTimes [ i ] [ 1 ] / resultTimes [ i ] [ 0 ] * 1000 ) ) / 1000 ) , 5 ) + ( i == 13 ? " |       )" : ( " | " + padTo ( "" + ( ( double ) ( ( int ) ( ( double ) resultTimes [ i ] [ 1 ] / resultTimes [ i - 1 ] [ 1 ] * 1000 ) ) / 1000 ) , 5 ) + " )" ) ) ;
      outprintln ( resultStrins [ i ] ) ;
    }
// If there are unprinted lines, let's print them now.
    for ( int i = maxDimension + 1 ; i <= 18 ; i ++ )
    {
      outprintln ( resultStrins [ i ] ) ;
    }
  }
/*
** Debug or info to the output.
*/
  private static void tracePs ( int currPiecePos , int from , int to )
  {
    outprintln ( "                              | ----------+----------------------------------------------------------------------------" ) ;
    int counter = 0 ;
    if ( from < to )
    {
      outprint ( "                              | to filter : [ " ) ;
      for ( int i = from ; i < to ; i ++ )
      {
        outprint ( "" + workingArray [ i ] ) ;
        counter ++ ;
        if ( counter % 10 == 0 )
        {
          outprint ( "\n                              |            " ) ;
        }
        if ( i != to - 1 )
        {
          outprint ( " , " ) ;
        }
      }
      outprintln ( " ]" ) ;
    }
    else
    {
      outprintln ( "                              | to filter : [ ]" ) ;
    }
    outprintln ( "                              | Piece     : " + currPiecePos ) ;
    if ( to < currIndToWrite )
    {
      counter = 0 ;
      outprint ( "                              | filtered  : [ " ) ;
      for ( int i = to ; i < currIndToWrite ; i ++ )
      {
        outprint ( "" + workingArray [ i ] ) ;
        counter ++ ;
        if ( counter % 10 == 0 )
        {
          outprint ( "\n                              |            " ) ;
        }
        if ( i != currIndToWrite - 1 )
        {
          outprint ( " , " ) ;
        }
      }
      outprintln ( " ]" ) ;
    }
    else
    {
      outprintln ( "                              | filtered  : [ ]" ) ;
    }
  }
  private static void printPs ( )
  {
    outprintln ( "" ) ;
    outprint ( " | " ) ;
    for ( int j = 0 ; j < dimension ; j ++ )
    {
      outprint ( currPath [ j ] + " | " ) ;
    }
    boolean elementFound = false ;
    for ( int i = 0 ; i < dimensionD ; i ++ )
    {
      if ( i % dimension == 0 )
      {
        outprintln ( "" ) ;
      }
      elementFound = false ;
      for ( int j = 0 ; j < dimension ; j ++ )
      {
        if ( currPath [ j ] == i )
        {
          elementFound = true ;
          break ;
        }
      }
      if ( elementFound )
      {
        boolean found2 = false ;
        for ( int j = 0 ; j < dimension ; j ++ )
        {
          if ( placingsArray [ j ] == i )
          {
            found2 = true ;
            break ;
          }
        }
        outprint ( " " + ( found2 ? pieces . toUpperCase ( ) : pieces ) ) ;
      }
      else
      {
        outprint ( " *" ) ;
      }
    }
    outprintln ( "" ) ;
  }
/*
** Prints the precalculated map of attackings.
** It happens till dimension 6.
*/
  private static void printIsFiltered ( )
  {
    outprintln ( "" ) ;
    if ( dimension < 7 )
    {
      outprintln ( "The currently used attacking map is:" ) ;
      outprint ( "   " ) ;
      for ( int j = 0 ; j < dimensionD ; j ++ )
      {
        outprint ( padTo ( "" + j , 3 ) ) ;
      }
      outprintln ( "" ) ;
      for ( int i = 0 ; i < dimensionD ; i ++ )
      {
        outprint ( padTo ( "" + i , 3 ) ) ;
        for ( int j = 0 ; j < dimensionD ; j ++ )
        {
          outprint ( padTo ( "" + ( isFiltered [ i ] [ j ] ? "+" : " " ) , 3 ) ) ;
        }
        outprintln ( "" ) ;
      }
    }
    else
    {
      outprintln ( "Attacking map  is too  large so it" ) ;
      outprintln ( "could be printed under dimension 7" ) ;
    }
    int [ ] attacks = new int [ dimensionD ] ;
    for ( int i = 0 ; i < dimensionD ; i ++ )
    {
      for ( int j = 0 ; j < dimensionD ; j ++ )
      {
        if ( isFiltered [ i ] [ j ] )
        {
          attacks [ i ] += 1 ;
        }
      }
    }
  }
/*
** The usage to be printed if requested or the first parameter is not one of the expecteds.
*/
  private static void printUsage ( )
  {
    outprintln ( "" ) ;
    outprintln ( "This program was born to play around the N Queens Problem." ) ;
    outprintln ( "Version: " + VERSION + ", released: " + RELEASED ) ;
    outprintln ( "" ) ;
    outprintln ( "  We  give  a  solution  which   considers  the  problem  from  a  whole" ) ;
    outprintln ( "  different   aspect  and  splits the  actual  calculation from the real" ) ;
    outprintln ( "  rules  of the chess  game. In this way, the calculations  of rooks and" ) ;
    outprintln ( "  bishops are also available as well as their super or awesome versions." ) ;
    outprintln ( " (super means that the pieces can also attack as a knight and)" ) ;
    outprintln ( " (awesome means the above but till the edge of the chessborad)" ) ;
    outprintln ( "  Preplaced chess pieces are also available to solve N Queens" ) ;
    outprintln ( "  Completion problem." ) ;
    outprintln ( "  Note: the board in size N is an ordered sequence between 0 and N^2 - 1" ) ;
    outprintln ( "  Read more: https://openso.kisscodesystems.com" ) ;
    outprintln ( "" ) ;
    outprintln ( "Parameters:" ) ;
    outprintln ( "                                                                 default" ) ;
    outprintln ( "  0 mode      [ o           i          t              ?,h,help ]       ?" ) ;
    outprintln ( "  1 dimension [ 1->n int    1->n int   13->18 int              ]      15" ) ;
    outprintln ( "  2 pieces    [ q           q,r,b      q                       ]       q" ) ;
    outprintln ( "  3 kinds     [ r           r,s,a      r                       ]       r" ) ;
    outprintln ( "  4 hits      [ o           o,a,f      o                       ]       o" ) ;
    outprintln ( "  5 threads   [ 1           1->n int   1                       ]       1" ) ;
    outprintln ( "  6 uniques   [ n           n,y        n                       ]       n" ) ;
    outprintln ( "  7 log       [ n           d,i,n      n                       ]       n" ) ;
    outprintln ( "  8 placings  [ empty       int;int... empty                   ]   empty" ) ;
    outprintln ( "" ) ;
    outprintln ( "  mode        o : original  , i : improved  , t : testing  , x : default" ) ;
    outprintln ( "  pieces      q : queen     , r : rook      , b : bishop   , x : default" ) ;
    outprintln ( "  kinds       r : regular   , s : super     , a : awesome  , x : default" ) ;
    outprintln ( "  hits        o : ordered   , a : all       , f : first    , x : default" ) ;
    outprintln ( "  uniques     n : no        , y : yes                      , x : default" ) ;
    outprintln ( "  log         n : no        , i : info      , d : debug    , x : default" ) ;
    outprintln ( "  placings    1 or more integer values separated by ; char , or    empty" ) ;
    outprintln ( "" ) ;
    outprintln ( "Example usages:" ) ;
    outprintln ( "" ) ;
    outprintln ( "  java -jar NqProblemExtended.jar" ) ;
    outprintln ( "    prints  help" ) ;
    outprintln ( "" ) ;
    outprintln ( "  java -jar NqProblemExtended.jar o" ) ;
    outprintln ( "    runs the  original solution  using the" ) ;
    outprintln ( "    default values of the other parameters" ) ;
    outprintln ( "" ) ;
    outprintln ( "  java -jar NqProblemExtended.jar i 16 b x a" ) ;
    outprintln ( "    runs  the improved  version (i) of  the  calculation and uses" ) ;
    outprintln ( "    16x16  chess  board to  place 16 (16) bishops (b)  onto it in" ) ;
    outprintln ( "    the default kind of chess pieces (x) to get all solutions (a)" ) ;
    outprintln ( "" ) ;
    outprintln ( "  java -jar NqProblemExtended.jar i 13 q s f 1 n n 166;2;18" ) ;
    outprintln ( "    runs the improved version  (i) of  the  calculation and uses" ) ;
    outprintln ( "    13x13  chess  board to  place 13 (13) queens (q)  onto it in" ) ;
    outprintln ( "    the super kind of chess pieces (s) to get first solution (f)" ) ;
    outprintln ( "    using 1 (1) thread to calculate and the found versions won't" ) ;
    outprintln ( "    be different (n)  and not uses  any logging (n)  to find the" ) ;
    outprintln ( "    solution  for  the  initially  placed  pieces  (to 166;2;18)" ) ;
    outprintln ( "" ) ;
  }
/*
** Utility functions to use.
*/
/*
** These two can print the output to the console.
** synchronized because worker threads can use these.
*/
  private static synchronized void outprintln ( String s )
  {
    System . out . println ( s ) ;
  }
  private static synchronized void outprint ( String s )
  {
    System . out . print ( s ) ;
  }
/*
** Pads the given string into the given length using spaces.
*/
  private static String padTo ( String s , int n )
  {
    String back = s ;
    while ( back . length ( ) < n )
    {
      back += " " ;
    }
    return back ;
  }
/*
** This one can calculate the difference of two timestamps:
** displays the elapsed days, hours, minutes, seconds and milliseconds.
*/
  private static String calcElapsed ( long elapsedMs )
  {
    int days = 0 ;
    int hours = 0 ;
    int minutes = 0 ;
    int seconds = 0 ;
    int milliseconds = 0 ;
    boolean zeroIsNeeded = false ;
    String totalElapsedTime = "" ;
    if ( elapsedMs >= 24 * 3600 * 1000 )
    {
      days = ( int ) ( elapsedMs / ( 24 * 3600 * 1000 ) ) ;
      elapsedMs -= days * ( 24 * 3600 * 1000 ) ;
    }
    if ( elapsedMs >= 3600 * 1000 )
    {
      hours = ( int ) ( elapsedMs / ( 3600 * 1000 ) ) ;
      elapsedMs -= hours * ( 3600 * 1000 ) ;
    }
    if ( elapsedMs >= 60 * 1000 )
    {
      minutes = ( int ) ( elapsedMs / ( 60 * 1000 ) ) ;
      elapsedMs -= minutes * ( 60 * 1000 ) ;
    }
    if ( elapsedMs >= 1000 )
    {
      seconds = ( int ) ( elapsedMs / 1000 ) ;
      elapsedMs -= seconds * ( 1000 ) ;
    }
    milliseconds = ( int ) elapsedMs ;
    if ( days > 0 )
    {
      zeroIsNeeded = true ;
      totalElapsedTime += days + "d " ;
    }
    if ( hours > 0 )
    {
      zeroIsNeeded = true ;
      totalElapsedTime += hours + "h " ;
    }
    if ( minutes > 0 || zeroIsNeeded )
    {
      zeroIsNeeded = true ;
      totalElapsedTime += minutes + "m " ;
    }
    if ( seconds > 0 || zeroIsNeeded )
    {
      zeroIsNeeded = true ;
      totalElapsedTime += seconds + "s " ;
    }
    if ( milliseconds > 0 || zeroIsNeeded )
    {
      totalElapsedTime += milliseconds + "ms" ;
    }
    if ( days == 0 && hours == 0 && minutes == 0 && seconds == 0 && milliseconds == 0 )
    {
      totalElapsedTime = "0ms" ;
    }
    milliseconds = 0 ;
    seconds = 0 ;
    minutes = 0 ;
    hours = 0 ;
    days = 0 ;
    zeroIsNeeded = false ;
    return totalElapsedTime ;
  }
}