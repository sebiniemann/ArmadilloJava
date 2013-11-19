/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package arma;

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
  public static long toc() {
    return TimeUnit.SECONDS.convert(System.nanoTime() - _tic, TimeUnit.NANOSECONDS);
  }
}
