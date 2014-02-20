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

import static arma.TestUtil.assertMatElementWiseEquals;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Daniel Kiechle <kiechle@sra.uni-hannover.de>
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixValuedElementWiseFunctionsMiscellaneous {

  /**
   * Returns the matrices to be tested.
   * 
   * @return The test matrices
   * 
   * @throws IOException n I/O error occurred
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    Mat without = new Mat();
    Mat logPow = new Mat();
    Mat sqrt = new Mat();
    Mat sign = new Mat();
    
    without.load("./test/data/input/miscellaneous.mat");
    logPow.load("./test/data/input/miscellaneous.log.pow.mat");
    sqrt.load("./test/data/input/miscellaneous.sqrt.mat");
    sign.load("./test/data/input/miscellaneous.sign.mat");
    
    testMatrices.add(new Object[]{new Mat(without),new Mat(logPow),new Mat(sqrt),new Mat(sign)});

    return testMatrices;
  }

  /**
   * The test matrices
   */
  @Parameter(value = 0)
  public Mat    _testMatrix;
  
  @Parameter(value = 1)
  public Mat	_testLogPow;
  
  @Parameter(value = 2)
  public Mat	_testSqrt;
  
  @Parameter(value = 3)
  public Mat	_testSign;
 
  /**
   * The powers
   */
  public double _powers[] = {-10, -5, -4, -3, -2, -1, -1 / 2.0, -1 / 3.0, -1 / 4.0, -1 / 5.0, -1 / 10.0, 0, 1 / 10.0, 1 / 5.0, 1 / 4.0, 1 / 3.0, 1 / 2.0, 1, 2, 3, 4, 5, 10};

  /**
   * Test method for {@link arma.Arma#abs(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testAbs() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testAbs.mat");
    assertMatElementWiseEquals("", expected, Arma.abs(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#eps(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testEps() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testEps.mat");
    assertMatElementWiseEquals("", expected, Arma.eps(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#exp(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testExp() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testExp.mat");
    assertMatElementWiseEquals("", expected, Arma.exp(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#exp2(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testExp2() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testExp2.mat");
    assertMatElementWiseEquals("", expected, Arma.exp2(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#exp10(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testExp10() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testExp10.mat");
    assertMatElementWiseEquals("", expected, Arma.exp10(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#trunc_exp(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testTrunc_exp() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testTrunc_exp.mat");
    assertMatElementWiseEquals("", expected, Arma.trunc_exp(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#log(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testLog() throws IOException {
	Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testLog.mat");
    assertMatElementWiseEquals("", expected, Arma.log(_testLogPow));
  }

  /**
   * Test method for {@link arma.Arma#log2(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testLog2() throws IOException {
	Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testLog2.mat");
    assertMatElementWiseEquals("", expected, Arma.log2(_testLogPow));
  }

  /**
   * Test method for {@link arma.Arma#log10(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testLog10() throws IOException {
	Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testLog10.mat");
    assertMatElementWiseEquals("", expected, Arma.log10(_testLogPow));
  }

  /**
   * Test method for {@link arma.Arma#trunc_log(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testTrunc_log() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testTrunc_log.mat");
    assertMatElementWiseEquals("", expected, Arma.trunc_log(_testLogPow));
  }

  /**
   * Test method for {@link arma.Arma#pow(arma.AbstractMat, double)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testPow() throws IOException {
	for (double power : _powers) {
      Mat expected = new Mat();
      expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testPow.p" + String.format(Locale.ENGLISH, "%1$1.6f", power) + ".mat");
      assertMatElementWiseEquals("", expected, Arma.pow(_testLogPow, power));
    }
  }

  /**
   * Test method for {@link arma.Arma#sqrt(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSqrt() throws IOException {
	Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSqrt.mat");
    assertMatElementWiseEquals("", expected, Arma.sqrt(_testSqrt));
  }

  /**
   * Test method for {@link arma.Arma#square(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSquare() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSquare.mat");
    assertMatElementWiseEquals("", expected, Arma.square(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#floor(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testFloor() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testFloor.mat");
    assertMatElementWiseEquals("", expected, Arma.floor(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#ceil(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCeil() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testCeil.mat");
    assertMatElementWiseEquals("", expected, Arma.ceil(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#round(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testRound() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testRound.mat");
    assertMatElementWiseEquals("", expected, Arma.round(_testMatrix));
  }
  
  /**
   * Test method for {@link arma.Arma#sign(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSign() throws IOException {
	Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedElementWiseFunctionsMiscellaneous/testSign.mat");
    assertMatElementWiseEquals("", expected, Arma.sign(_testSign));
  }
}
