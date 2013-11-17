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
public class TestScalarVectorValuedFunctionsOfVectorsMatricesLogic {

	/**
	 * Test data for logic functions
	 * 
	 * @return TestData
	 */
	@Parameters
	public static Collection<Object[]> getTestData() {
		Collection<Object[]> matrices = new ArrayList<Object[]>();

		double[][] testdata = new double[][] {{ 
			0, 1, 0, 123, -1, 0, -123, 0, 0
		},{
			0, 1, 1, 234, -1, -1, -234, Datum.eps, Datum.inf 
		},{
			0, 1, 0, 345, -1, 0, -345, 0, 0 
		},{
			0, 1, 1, 456, -1, -1, -456, -Datum.eps, -Datum.inf 
		},{
			0, 1, 0, 567, -1, 0, -567, 0, 0
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
	   * Test method for {@link arma.Arma#any(AbstractMat)}.
	   */
	  @Test
	  public void testAnyMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#anyMat(AbstractMat)}.
	   */
	  @Test
	  public void testAnyMatMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#anyMat(AbstractMat, int)}.
	   */
	  @Test
	  public void testAnyMatMatInt() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#all(AbstractMat)}.
	   */
	  @Test
	  public void testAllMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#allMat(AbstractMat)}.
	   */
	  @Test
	  public void testAllMatMat() {
	    fail("Not yet implemented"); // TODO
	  }

	  /**
	   * Test method for {@link arma.Arma#allMat(AbstractMat, int)}.
	   */
	  @Test
	  public void testAllMatMatInt() {
	    fail("Not yet implemented"); // TODO
	  }


}
