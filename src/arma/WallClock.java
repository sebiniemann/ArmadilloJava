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
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class WallClock {
  
  /**
   * 
   */
  private static long _tic;

  /**
   * 
   */
  public static void tic() {
    _tic = System.currentTimeMillis();
  }

  /**
   * @return
   */
  public static long toc() {
    return TimeUnit.SECONDS.convert(System.currentTimeMillis() - _tic, TimeUnit.MILLISECONDS) ;
  }
}
