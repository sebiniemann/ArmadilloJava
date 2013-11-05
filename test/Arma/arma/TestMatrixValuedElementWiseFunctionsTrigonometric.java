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

import java.util.*;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

/**
 * @author Daniel Kiechle <kiechle@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestMatrixValuedElementWiseFunctionsTrigonometric {
	
  /**
   * Test data for trigonometric functions
   * @return TestData
   */
  @Parameters
  public static Collection<Object[]> getTestData (){
	  Collection<Object[]> matrices = new ArrayList<Object[]>();
	  
	  double[][] testdata = new double[][] {{
		   0, 1, 2, Datum.eps, Datum.inf, 1/12*Datum.pi, 1/10*Datum.pi, 1/8*Datum.pi, 1/6*Datum.pi, 1/4*Datum.pi, 1/2*Datum.pi,
		  Datum.pi, 3/2*Datum.pi, 2*Datum.pi, 3*Datum.pi, 4*Datum.pi, 5*Datum.pi, 10*Datum.pi, 100*Datum.pi
	  },{
		  -0, -1, -2, -Datum.eps, -Datum.inf, -1/12*Datum.pi, -1/10*Datum.pi, -1/8*Datum.pi, -1/6*Datum.pi, -1/4*Datum.pi, -1/2*Datum.pi,
		  -Datum.pi, -3/2*Datum.pi, -2*Datum.pi, -3*Datum.pi, -4*Datum.pi, -5*Datum.pi, -10*Datum.pi, -100*Datum.pi
	  }};
	  
	  Object[] mat = new Object[]{new Mat(testdata)};
	  matrices.add(mat);
	  return matrices;
  }
  
  @Parameter
  public Mat _testData;
  
  /**
   * Test method for {@link arma.Arma#sign(AbstractMat)}.
   */
  @Test
  public void testSign() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#sin(AbstractMat)}.
   */
  @Test
  public void testSin() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#asin(AbstractMat)}.
   */
  @Test
  public void testAsin() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#sinh(AbstractMat)}.
   */
  @Test
  public void testSinh() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#asinh(AbstractMat)}.
   */
  @Test
  public void testAsinh() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#cos(AbstractMat)}.
   */
  @Test
  public void testCos() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#acos(AbstractMat)}.
   */
  @Test
  public void testAcos() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#cosh(AbstractMat)}.
   */
  @Test
  public void testCosh() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#acosh(AbstractMat)}.
   */
  @Test
  public void testAcosh() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#tan(AbstractMat)}.
   */
  @Test
  public void testTan() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#atan(AbstractMat)}.
   */
  @Test
  public void testAtan() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#tanh(AbstractMat)}.
   */
  @Test
  public void testTanh() {
    fail("Not yet implemented"); // TODO
  }

  /**
   * Test method for {@link arma.Arma#atanh(AbstractMat)}.
   */
  @Test
  public void testAtanh() {
    fail("Not yet implemented"); // TODO
  }
}
