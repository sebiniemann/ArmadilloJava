/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

import java.util.concurrent.TimeUnit;

/**
 * Measures the elapsed time between the last invoke {@link #tic()} and {@link #toc()} in seconds.
 * <p>
 * <b>Non-canonical:</b> The precision is bounded by {@code System.nanoTime()}
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class WallClock {
  /**
   * The start instant of a time interval.
   */
  private static long _tic;

  /**
   * Sets the start instant of a time interval to the current instant.
   */
  public static void tic() {
    _tic = System.nanoTime();
  }

  /**
   * Returns the elapsed time between the last invoke {@link #tic()} and the current instant.
   * 
   * @return The elapsed time.
   */
  public static double toc() {
    /*
     * Conversions from finer to coarser granularities via TimeUnit.X.convert(int, TimeUnit) truncates information.
     * Therefore, a direct conversion to seconds would result in a complete information loss of the milliseconds.
     */
    return TimeUnit.MILLISECONDS.convert(System.nanoTime() - _tic, TimeUnit.NANOSECONDS) / 1000.0;
  }
}
