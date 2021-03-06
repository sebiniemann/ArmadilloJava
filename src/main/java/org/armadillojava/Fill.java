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
 * Sebastian Niemann - Lead developer
 * Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

/**
 * Provides predefined constants to identify fill types.
 * 
 * @author Sebastian Niemann
 */
public enum Fill {
  /**
   * Do nothing
   */
  NONE,
  /**
   * Set all elements to 0.
   */
  ZEROS,
  /**
   * Set all elements to 1.
   */
  ONES,
  /**
   * Set all elements along the main diagonal to 1 and the other elements to 0.
   */
  EYE,
  /**
   * Set each element to a pseudorandom value drawn from the standard uniform distribution on the left-closed and
   * right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   */
  RANDU,
  /**
   * Set each element to a pseudorandom value drawn from the standard normal distribution with mean 0.0 and standard
   * deviation 1.0.
   */
  RANDN
}
