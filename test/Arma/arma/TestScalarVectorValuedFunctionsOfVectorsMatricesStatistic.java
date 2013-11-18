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
	   * @throws IOException 
	   */
	  @Test
	  public void testMinMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.min.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.min(_testData.col(j)), TestUtil.NUMERIC_TOLERANCE);
		  }
	  }

	  /**
	   * Test method for {@link arma.Arma#minMat(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMinMatMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.min.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.minMat(_testData), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#minMat(AbstractMat, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMinMatMatInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.min.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.minMat(_testData,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.min.d1.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.minMat(_testData,1), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#max(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMaxMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.max.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.max(_testData.col(j)), TestUtil.NUMERIC_TOLERANCE);
		  }
	  }

	  /**
	   * Test method for {@link arma.Arma#maxMat(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMaxMatMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.max.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.maxMat(_testData), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#maxMat(AbstractMat, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMaxMatMatInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.max.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.maxMat(_testData,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.max.d1.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.maxMat(_testData,1), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#mean(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMeanMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.mean.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.mean(_testData.col(j)), TestUtil.NUMERIC_TOLERANCE);
		  }
	  }

	  /**
	   * Test method for {@link arma.Arma#meanMat(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMeanMatMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.mean.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.meanMat(_testData), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#meanMat(AbstractMat, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMeanMatMatInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.mean.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.meanMat(_testData,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.mean.d1.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.meanMat(_testData,1), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#median(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMedianMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.median.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.median(_testData.col(j)), TestUtil.NUMERIC_TOLERANCE);
		  }
	  }

	  /**
	   * Test method for {@link arma.Arma#medianMat(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMedianMatMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.median.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.medianMat(_testData), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#medianMat(AbstractMat, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testMedianMatMatInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.median.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.medianMat(_testData,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.median.d1.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.medianMat(_testData,1), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#stddev(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testStddevMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n0.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.stddev(_testData.col(j)), TestUtil.NUMERIC_TOLERANCE);
		  }
	  }

	  /**
	   * Test method for {@link arma.Arma#stddev(AbstractMat, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testStddevMatInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n0.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.stddev(_testData.col(j),0), TestUtil.NUMERIC_TOLERANCE);
		  }
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n1.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.stddev(_testData.col(j),1), TestUtil.NUMERIC_TOLERANCE);
		  }
	  }

	  /**
	   * Test method for {@link arma.Arma#stddevMat(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testStddevMatMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n0.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.stddevMat(_testData), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#stddevMat(AbstractMat, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testStddevMatMatInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n0.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.stddevMat(_testData,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n1.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.stddevMat(_testData,1), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#stddevMat(AbstractMat, int, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testStddevMatMatIntInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n0.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.stddevMat(_testData,0,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n1.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.stddevMat(_testData,1,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n0.d1.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.stddevMat(_testData,0,1), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.stddev.n1.d1.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.stddevMat(_testData,1,1), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#var(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testVarMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n0.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.var(_testData.col(j)), TestUtil.NUMERIC_TOLERANCE);
		  }
	  }

	  /**
	   * Test method for {@link arma.Arma#var(AbstractMat, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testVarMatInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n0.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.var(_testData.col(j),0), TestUtil.NUMERIC_TOLERANCE);
		  }
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n1.d0.mat");

		  for(int j = 0; j < _testData.n_cols; j++) {
		    assertEquals(expected.at(j), Arma.var(_testData.col(j),1), TestUtil.NUMERIC_TOLERANCE);
		  }
	  }

	  /**
	   * Test method for {@link arma.Arma#varMat(AbstractMat)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testVarMatMat() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n0.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.varMat(_testData), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#varMat(AbstractMat, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testVarMatMatInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n0.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.varMat(_testData,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n1.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.varMat(_testData,1), TestUtil.NUMERIC_TOLERANCE);
	  }

	  /**
	   * Test method for {@link arma.Arma#varMat(AbstractMat, int, int)}.
	   * @throws IOException 
	   */
	  @Test
	  public void testVarMatMatIntInt() throws IOException {
		  Mat expected = new Mat();
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n0.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.varMat(_testData,0,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n1.d0.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.varMat(_testData,1,0), TestUtil.NUMERIC_TOLERANCE);
		  
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n0.d1.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.varMat(_testData,0,1), TestUtil.NUMERIC_TOLERANCE);
		  
		  expected.load("test/data/Arma/TestScalarVectorValuedFunctionsOfVectorsMatrices/Statistic.var.n1.d1.mat");
		  
		  TestUtil.assertMatEquals(expected,Arma.varMat(_testData,1,1), TestUtil.NUMERIC_TOLERANCE);
	  }

}
