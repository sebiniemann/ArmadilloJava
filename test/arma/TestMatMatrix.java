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

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Contains all test cases for parametrised interface tests of non-element-wise operations in {@link Mat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestMatMatrix {

  /**
   * Returns all test matrices used for this test.
   * 
   * @return The test matrices.
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() {
    return TestData.getTestMatrices();
  }

  /**
   * A matrix to be used for this test.
   */
  @Parameter
  public Mat      _testMatrix;

  /**
   * The ground-truth determinant of {@link #_testMatrix}.
   */
  @Parameter(value = 1)
  public double   _determinant;

  /**
   * The ground-truth sum of all values of {@link #_testMatrix}.
   */
  @Parameter(value = 2)
  public double   _accu;

  /**
   * The ground-truth norms (Induced matrix 1-norm and Induced matrix 2-norm) of {@link #_testMatrix}.
   */
  @Parameter(value = 3)
  public double[] _norms;

  @Test
  public void testT() {
    fail("Not yet implemented");
  }

  @Test
  public void testI() {
    fail("Not yet implemented");
  }

}
