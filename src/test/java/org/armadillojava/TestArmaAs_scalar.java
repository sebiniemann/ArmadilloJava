/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.armadillojava;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class TestArmaAs_scalar {

  /**
   * Test method for {@link arma.Arma#as_scalar(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occured
   */
  @Test
  public void testAs_scalar() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/miscellaneous.mat");

    Mat testMatrix;

    for (int n = 0; n < input.n_elem; n++) {
      double testValue = input.at(n);
      testMatrix = new Mat(new double[]{testValue});
      assertEquals(testValue, Arma.as_scalar(testMatrix), 0);
    }
  }
}
