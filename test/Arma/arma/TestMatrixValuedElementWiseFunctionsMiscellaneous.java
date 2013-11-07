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
	 * 
	 * @return TestData
	 */
	@Parameters
	public static Collection<Object[]> getTestData() {
		Collection<Object[]> matrices = new ArrayList<Object[]>();

		double[][] testdata = new double[][] {{ 
				0, 1, 2, Datum.eps, 0/* Datum.inf */, Datum.e, 12, 123, 1234, 12345, 123456, 1234567,
				12345678, 123456789, 0.9, 0.89, 0.789, 0.6789, 0.56789, 0.456789, 0.3456789, 0.23456789, 0.123456789
			},{
				-0, -1, -2, -Datum.eps, 0/*-Datum.inf*/, -Datum.e, -12, -123, -1234, -12345, -123456, -1234567, -12345678,
				-123456789, -0.9, -0.89, -0.789, -0.6789, -0.56789, -0.456789, -0.3456789, -0.23456789, -0.123456789 
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
	 * Test method for {@link arma.Arma#abs(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testAbs() throws IOException {
	  Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.abs.mat");

		Mat actual = Arma.abs(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#eps(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testEps() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.eps.mat");

		Mat actual = Arma.eps(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#exp(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testExp() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.exp.mat");

		Mat actual = Arma.exp(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#exp2(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testExp2() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.exp2.mat");

		Mat actual = Arma.exp2(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#exp10(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testExp10() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.exp10.mat");

		Mat actual = Arma.exp10(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#trunc_exp(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testTrunc_exp() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.trunc_exp.mat");

		Mat actual = Arma.trunc_exp(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#log(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testLog() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log.mat");

		Mat actual = Arma.log(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#log2(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testLog2() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log2.mat");

		Mat actual = Arma.log2(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#log10(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testLog10() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.log10.mat");

		Mat actual = Arma.log10(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#trunc_log(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testTrunc_log() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.trunc_log.mat");

		Mat actual = Arma.trunc_log(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#pow(AbstractMat, int)}.
	 * @throws IOException 
	 */
	@Test
	public void testPow() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.pow.mat");

		Mat actual = Arma.pow(_testData, 100);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#sqrt(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testSqrt() throws IOException{
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.sqrt.mat");

		Mat actual = Arma.sqrt(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#square(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testSquare() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.square.mat");

		Mat actual = Arma.square(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#floor(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testFloor() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.floor.mat");

		Mat actual = Arma.floor(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#ceil(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testCeil() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.ceil.mat");

		Mat actual = Arma.ceil(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}

	/**
	 * Test method for {@link arma.Arma#round(AbstractMat)}.
	 * @throws IOException 
	 */
	@Test
	public void testRound() throws IOException {
    Mat expected = new Mat();
		expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Miscellaneous.round.mat");

		Mat actual = Arma.round(_testData);
		for (int n = 0; n < _testData.n_elem; n++) {
			assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
		}
	}
}
