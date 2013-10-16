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

import static org.junit.Assert.assertEquals;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Contains all test cases for parametrised interface tests of element-wise operations in {@link Arma}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaElementWise {
  
  /**
   * Returns all test matrices used for this test.
   * 
   * @return The test matrices.
   */
  @Parameters
  public static Collection<Object[]> getTestElementWise() {
    return TestData.getTestElementWise();
  }

  /**
   * A matrix composed of elements to be used for this test.
   */
  @Parameter
  public Mat _testElements;
  
  /**
   * Tests {@link Arma#abs(Mat)} by comparing it per-element with <code>Math.abs(double)</code>.
   */
  @Test
  public void testAbs() {
    Mat actual = Arma.abs(_testElements);
    
    for(int n = 0; n < _testElements.n_elem; n++) {
      double expected = Math.abs(_testElements.at(n));
      assertEquals(expected, actual.at(n), TestData.NUMERIC_TOLERANCE);
    }
  }

  /**
   * Tests {@link Arma#abs(Mat)} by comparing it per-element with <code>Math.floor(double)</code>.
   */
  @Test
  public void testFloor() {
    Mat actual = Arma.floor(_testElements);
    
    for(int n = 0; n < _testElements.n_elem; n++) {
      double expected = Math.floor(_testElements.at(n));
      assertEquals(expected, actual.at(n), TestData.NUMERIC_TOLERANCE);
    }
  }

  /**
   * Tests {@link Arma#abs(Mat)} by comparing it per-element with <code>Math.pow(double, 2)</code>.
   */
  @Test
  public void testSquare() {
    Mat actual = Arma.square(_testElements);
    
    for(int n = 0; n < _testElements.n_elem; n++) {
      double expected = Math.pow(_testElements.at(n), 2);
      assertEquals(expected, actual.at(n), TestData.NUMERIC_TOLERANCE);
    }
  }
}
