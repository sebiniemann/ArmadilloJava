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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Daniel Kiechle <kiechle@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestMatrixValuedElementWiseFunctionsMiscellaneous {
	
	/**
	   * Test data for miscellaneous functions
	   * @return TestData
	   */
	  @Parameters
	  public static Collection<Object[]> getTestData (){
		  Collection<Object[]> matrices = new ArrayList<Object[]>();
		  
		  double[][] testdata = new double[][] {{
			   0, 1, 2, Datum.eps, Datum.inf, Datum.e, 12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789, 0.9,
			   0.89, 0.789, 0.6789, 0.56789, 0.456789, 0.3456789, 0.23456789, 0.123456789
		  },{
			  -0, -1, -2, -Datum.eps, -Datum.inf, -Datum.e, -12, -123, -1234, -12345, -123456, -1234567, -12345678, -123456789, -0.9,
			   -0.89, -0.789, -0.6789, -0.56789, -0.456789, -0.3456789, -0.23456789, -0.123456789
		  }};
		  
		  Object[] mat = new Object[]{new Mat(testdata)};
		  matrices.add(mat);
		  return matrices;
	  }
	  
	  @Parameter
	  public Mat _testData;

	  /**
	   * Test method for {@link arma.Arma#abs(AbstractMat)}.
	   */
	  @Test
	  public void testAbs() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#eps(AbstractMat)}.
	   */
	  @Test
	  public void testEps() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#exp(AbstractMat)}.
	   */
	  @Test
	  public void testExp() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#exp2(AbstractMat)}.
	   */
	  @Test
	  public void testExp2() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#exp10(AbstractMat)}.
	   */
	  @Test
	  public void testExp10() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#trunc_exp(AbstractMat)}.
	   */
	  @Test
	  public void testTrunc_exp() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#log(AbstractMat)}.
	   */
	  @Test
	  public void testLog() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#log2(AbstractMat)}.
	   */
	  @Test
	  public void testLog2() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#log10(AbstractMat)}.
	   */
	  @Test
	  public void testLog10() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#trunc_log(AbstractMat)}.
	   */
	  @Test
	  public void testTrunc_log() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#pow(AbstractMat, int)}.
	   */
	  @Test
	  public void testPow() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#sqrt(AbstractMat)}.
	   */
	  @Test
	  public void testSqrt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#square(AbstractMat)}.
	   */
	  @Test
	  public void testSquare() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#floor(AbstractMat)}.
	   */
	  @Test
	  public void testFloor() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#ceil(AbstractMat)}.
	   */
	  @Test
	  public void testCeil() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#round(AbstractMat)}.
	   */
	  @Test
	  public void testRound() {
	    fail("Not yet implemented"); // TODO
	  }
}
