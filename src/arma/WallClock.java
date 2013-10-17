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
 * Measures the elapsed time in seconds.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class WallClock {
  
  /**
   * The starting time.
   */
  private static long _tic;

  /**
   * Sets the starting time to the current time.
   */
  public static void tic() {
    _tic = System.nanoTime();
  }

  /**
   * Returns the elapsed time between the current time and the stating time.
   * 
   * @return The elapsed time.
   */
  public static long toc() {
    return TimeUnit.SECONDS.convert(System.nanoTime() - _tic, TimeUnit.MILLISECONDS) ;
  }
}
