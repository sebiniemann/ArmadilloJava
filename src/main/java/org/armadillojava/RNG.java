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

import java.util.Random;

/**
 * TODO
 * 
 * @author Sebastian Niemann
 */
public class RNG {

  /**
   * TODO
   */
  protected static Random _rng = new Random();

  /**
   * TODO
   * 
   * @param seed TODO
   */
  public static void set_seed(long seed) {
    _rng.setSeed(seed);
  }

  /**
   * TODO
   */
  public static void set_seed_random() {
    _rng.setSeed(Double.doubleToLongBits(Math.random()));
  }

}
