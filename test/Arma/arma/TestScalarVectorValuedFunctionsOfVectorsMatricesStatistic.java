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
public class TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic {

	/**
	 * Test data for Statistic functions
	 * 
	 * @return TestData
	 */
	@Parameters
	public static Collection<Object[]> getTestData() {
		Collection<Object[]> matrices = new ArrayList<Object[]>();

		double[][] testdata = new double[][] {{ 
			0, 1, 2, 10, 5, -1, -2, -10, -5, 0
		},{
			0, 1, 2, 50, 55, -1, -2, -50, -55, -Datum.inf
		},{
			0, 1, 2, 20, 15, -1, -2, -20, -15, Datum.inf 
		},{
			0, 1, 2, 40, 45, -1, -2, -40, -45, Datum.eps 
		},{
			0, 1, 2, 30, 30, -1, -2, -30, -30, -Datum.eps
		}};

		Object[] mat = new Object[] { new Mat(testdata) };
		matrices.add(mat);
		return matrices;
	}
	
	 /**
	 * 
	 */
	 @Parameter
	 public Mat _testData;


	  /**
	   * Test method for {@link arma.Arma#min(AbstractMat)}.
	   */
	  @Test
	  public void testMinMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#minMat(AbstractMat)}.
	   */
	  @Test
	  public void testMinMatMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#minMat(AbstractMat, int)}.
	   */
	  @Test
	  public void testMinMatMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#max(AbstractMat)}.
	   */
	  @Test
	  public void testMaxMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#maxMat(AbstractMat)}.
	   */
	  @Test
	  public void testMaxMatMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#maxMat(AbstractMat, int)}.
	   */
	  @Test
	  public void testMaxMatMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#mean(AbstractMat)}.
	   */
	  @Test
	  public void testMeanMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#meanMat(AbstractMat)}.
	   */
	  @Test
	  public void testMeanMatMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#meanMat(AbstractMat, int)}.
	   */
	  @Test
	  public void testMeanMatMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#median(AbstractMat)}.
	   */
	  @Test
	  public void testMedianMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#medianMat(AbstractMat)}.
	   */
	  @Test
	  public void testMedianMatMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#medianMat(AbstractMat, int)}.
	   */
	  @Test
	  public void testMedianMatMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#stddev(AbstractMat)}.
	   */
	  @Test
	  public void testStddevMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#stddev(AbstractMat, int)}.
	   */
	  @Test
	  public void testStddevMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#stddevMat(AbstractMat)}.
	   */
	  @Test
	  public void testStddevMatMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#stddevMat(AbstractMat, int)}.
	   */
	  @Test
	  public void testStddevMatMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#stddevMat(AbstractMat, int, int)}.
	   */
	  @Test
	  public void testStddevMatMatIntInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#var(AbstractMat)}.
	   */
	  @Test
	  public void testVarMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#var(AbstractMat, int)}.
	   */
	  @Test
	  public void testVarMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#varMat(AbstractMat)}.
	   */
	  @Test
	  public void testVarMatMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#varMat(AbstractMat, int)}.
	   */
	  @Test
	  public void testVarMatMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#varMat(AbstractMat, int, int)}.
	   */
	  @Test
	  public void testVarMatMatIntInt() {
	    fail("Not yet implemented"); // TODO
	  }

}
