/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
/**
 * 
 */
package org.armadillojava;

import static org.armadillojava.TestUtil.assertMatElementWiseEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixGenerationLinspace {

  /**
   * Returns the matrices to be tested.
   * 
   * @return The test matrices
   * 
   * @throws IOException n I/O error occurred
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    int elements[] = {1, 2, 3, 4, 5, 10, 100, 1000, 10000};

    for (int numberOfElements : elements) {
      testMatrices.add(new Object[]{numberOfElements});
    }

    return testMatrices;
  }

  /**
   * The number of elements
   */
  @Parameter
  public int _numberOfElements;

  /**
   * Test method for {@link arma.Arma#linspace(double, double)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testLinspaceDoubleDouble() throws IOException {
    if (_numberOfElements != 100) {
      return;
    }

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixGenerationLinspace/testLinspace.100.mat");
    assertMatElementWiseEquals("", expected, Arma.linspace(-12345.6789, 9876.54321));
  }

  /**
   * Test method for {@link arma.Arma#linspace(double, double, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testLinspaceDoubleDoubleInt() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixGenerationLinspace/testLinspace." + _numberOfElements + ".mat");
    assertMatElementWiseEquals("Number of elements: " + _numberOfElements, expected, Arma.linspace(-12345.6789, 9876.54321, _numberOfElements));
  }

}
