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
public class TestMatrixValuedElementWiseFunctionsTrigonometric {
	
  /**
   * Test data for trigonometric functions
   * @return TestData
   */
  @Parameters
  public static Collection<Object[]> getTestData (){
	  Collection<Object[]> matrices = new ArrayList<Object[]>();
	  
	  double[][] testdata = new double[][] {{
		   0, 1, 2, Datum.eps, 0 /*Datum.inf*/, 1/12*Datum.pi, 1/10*Datum.pi, 1/8*Datum.pi, 1/6*Datum.pi, 1/4*Datum.pi, 1/2*Datum.pi,
		  Datum.pi, 3/2*Datum.pi, 2*Datum.pi, 3*Datum.pi, 4*Datum.pi, 5*Datum.pi, 10*Datum.pi, 100*Datum.pi
	  },{
		  -0, -1, -2, -Datum.eps, 0 /*Datum.inf*/, -1/12*Datum.pi, -1/10*Datum.pi, -1/8*Datum.pi, -1/6*Datum.pi, -1/4*Datum.pi, -1/2*Datum.pi,
		  -Datum.pi, -3/2*Datum.pi, -2*Datum.pi, -3*Datum.pi, -4*Datum.pi, -5*Datum.pi, -10*Datum.pi, -100*Datum.pi
	  }};
	  
	  Object[] mat = new Object[]{new Mat(testdata)};
	  matrices.add(mat);
	  return matrices;
  }
  
  /**
   * 
   */
  @Parameter
  public Mat _testData;
  
  /**
   * Test method for {@link arma.Arma#sign(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testSign() throws IOException {
    Mat expected = new Mat();
    expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.sign.mat");
    
    Mat actual = Arma.sign(_testData);
    for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
    }
  }

  /**
   * Test method for {@link arma.Arma#sin(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testSin() throws IOException {
    Mat expected = new Mat();
	 expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.sin.mat");
	    
	 Mat actual = Arma.sin(_testData);
	 for (int n = 0; n< _testData.n_elem; n++){
		 assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	 }
  }

  /**
   * Test method for {@link arma.Arma#asin(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testAsin() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.asin.mat");
	  
	  Mat actual = Arma.asin(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#sinh(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testSinh() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.sinh.mat");
	  
	  Mat actual = Arma.sinh(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#asinh(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testAsinh() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.asinh.mat");
	  
	  Mat actual = Arma.asinh(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#cos(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testCos() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.cos.mat");
	  
	  Mat actual = Arma.cos(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#acos(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testAcos() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.acos.mat");
	  
	  Mat actual = Arma.acos(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#cosh(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testCosh() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.cosh.mat");
	  
	  Mat actual = Arma.cosh(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#acosh(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testAcosh() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.acosh.mat");
	  
	  Mat actual = Arma.acosh(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#tan(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testTan() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.tan.mat");
	  
	  Mat actual = Arma.tan(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#atan(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testAtan() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.atan.mat");
	  
	  Mat actual = Arma.atan(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#tanh(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testTanh() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.tanh.mat");
	  
	  Mat actual = Arma.tanh(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }

  /**
   * Test method for {@link arma.Arma#atanh(AbstractMat)}.
   * @throws IOException 
   */
  @Test
  public void testAtanh() throws IOException {
    Mat expected = new Mat();
	  expected.load("test/data/Arma/TestMatrixValuedElementWiseFunctions/Trigonometric.atanh.mat");
	  
	  Mat actual = Arma.atanh(_testData);
	  for (int n = 0; n< _testData.n_elem; n++){
		assertEquals(expected.at(n), actual.at(n), TestUtil.NUMERIC_TOLERANCE);
	  }
  }
}
