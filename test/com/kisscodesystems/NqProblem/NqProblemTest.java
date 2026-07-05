package com.kisscodesystems.NqProblem;

import static com.kisscodesystems.NqProblem.Const.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Regression test for the N Queens solver counters. It pins the shared static state to the default
 * queen / ordered / single-thread configuration (the one used by the {@code o 14} and {@code i 14}
 * command lines) and verifies that both the original backtracking solution and the improved solution
 * count exactly 365596 solutions for the 14x14 board.
 *
 * <p>This also guards against the {@code int} overflow that used to make the original mode wrap to a
 * negative "all attempts" total: the solution count and the dead-placings count are now {@code long}
 * (see {@link State#found} and {@link State#deads}).
 */
public final class NqProblemTest {

  /** The board dimension under test. */
  private static final int DIMENSION = 14;

  /** The known number of solutions of the 14 queens problem. */
  private static final long EXPECTED = 365596L;

  /**
   * Resets the shared static solution state and pins every parameter to the default queen / regular
   * / ordered / single-thread / no-uniques / no-log configuration before each test, so the drivers
   * dispatch exactly like the {@code o 14} and {@code i 14} command lines.
   */
  @Before
  public void setUp() {
    State.found = 0;
    State.deads = 0;
    State.dimension = DIMENSION;
    State.pieces = QUEEN;
    State.kinds = REGULAR;
    State.hits = ORDERED;
    State.threads = 1;
    State.uniques = NO;
    State.log = NO;
    State.placings = defaultPlacings;
  }

  /** The original backtracking solution ({@code o 14}) must find 365596 solutions. */
  @Test
  public void originalModeFinds365596() {
    Modes.doOriginal();
    assertEquals(EXPECTED, State.found);
  }

  /** The improved solution ({@code i 14}) must find 365596 solutions. */
  @Test
  public void improvedModeFinds365596() {
    Modes.doImproved(0L);
    assertEquals(EXPECTED, State.found);
  }

  /** The original and improved solutions must agree, and both must equal the expected count. */
  @Test
  public void originalAndImprovedAgree() {
    Modes.doOriginal();
    long original = State.found;
    setUp();
    Modes.doImproved(0L);
    long improved = State.found;
    assertEquals("o 14 and i 14 must find the same number of solutions", original, improved);
    assertEquals(EXPECTED, improved);
  }
}
