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

}
