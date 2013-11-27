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

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
public class TestArmaMiscellaneous {

  /**
   * Test method for {@link arma.Arma#is_finite(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testIs_finiteAbstractMat() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/elementwise.miscellaneous.mat");

    Mat testMatrix;

    for (double testValue : input) {
      testMatrix = new Mat(new double[]{testValue});
      assertEquals(!Double.isInfinite(testValue), Arma.is_finite(Arma.as_scalar(testMatrix)));
    }
  }

  /**
   * Test method for {@link arma.Arma#is_finite(double)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testIs_finiteDouble() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/elementwise.miscellaneous.mat");

    for (double testValue : input) {
      assertEquals(!Double.isInfinite(testValue), Arma.is_finite(testValue));
    }
  }

}
