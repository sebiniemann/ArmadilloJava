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
 * Contains all test cases for parametrised interface tests of element-wise operations in {@link Mat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestMatElementWise {

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

  @Test
  public void testPlusDouble() {
    fail("Not yet implemented");
  }

  @Test
  public void testPlusMat() {
    fail("Not yet implemented");
  }

  @Test
  public void testMinusDouble() {
    fail("Not yet implemented");
  }

  @Test
  public void testMinusMat() {
    fail("Not yet implemented");
  }

  @Test
  public void testTimesDouble() {
    fail("Not yet implemented");
  }

  @Test
  public void testElemTimesDouble() {
    fail("Not yet implemented");
  }

  @Test
  public void testElemTimesMat() {
    fail("Not yet implemented");
  }

  @Test
  public void testDivideDouble() {
    fail("Not yet implemented");
  }

  @Test
  public void testElemDivideDouble() {
    fail("Not yet implemented");
  }

  @Test
  public void testElemDivideMat() {
    fail("Not yet implemented");
  }

}
