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
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm {

  /**
   * Returns the matrices to be tested.
   * 
   * @return The test matrices
   * 
   * @throws IOException An I/O error occurred
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    Mat input = new Mat();

    input.load("./test/data/input/series.mat");
    testMatrices.add(new Object[]{new Mat(input)});

    return testMatrices;
  }

  /**
   * The test matrix
   */
  @Parameter
  public Mat _testMatrix;

  /**
   * The number of bins
   */
  public int _numberOfBins[] = {1, 2, 3, 4, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

  /**
   * The number of bins
   */
  public Mat _centers = new Mat(new double[]{-90, -45, -22.5, -9, -4.5, -1.8, -0.9, -0.27, -0.009, 0, 0.01, 0.3, 1, 2, 5, 10, 25, 50, 100});

  /**
   * Test method for {@link arma.Arma#hist(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testHistAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.n10.mat");
    assertMatElementWiseEquals("", expected, Arma.hist(Arma.vectorise(_testMatrix)));
  }

  /**
   * Test method for {@link arma.Arma#hist(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testHistAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    Mat testMatrix = Arma.vectorise(_testMatrix);
    
    for (int bins : _numberOfBins) {
      expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.n" + bins + ".mat");
      assertMatElementWiseEquals("", expected, Arma.hist(testMatrix, bins));
    }
  }

  /**
   * Test method for {@link arma.Arma#hist(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testHistAbstractMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.centers.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertMatElementWiseEquals("", expected.col(j), Arma.hist(_testMatrix.col(j), _centers));
    }
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.centers.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertMatElementWiseEquals("", expected.row(i), Arma.hist(_testMatrix.row(i), _centers));
    }
  }

  /**
   * Test method for {@link arma.Arma#histMat(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testHistMatAbstractMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.centers.d0.mat");

    assertMatElementWiseEquals("", expected, Arma.histMat(_testMatrix, _centers));
  }

  /**
   * Test method for {@link arma.Arma#histMat(arma.AbstractMat, arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testHistMatAbstractMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.centers.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.histMat(_testMatrix, _centers, 0));
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHist.centers.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.histMat(_testMatrix, _centers, 1));
  }

  /**
   * Test method for {@link arma.Arma#histc(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testHistc() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHistc.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertMatElementWiseEquals("", expected.col(j), Arma.histc(_testMatrix.col(j), _centers));
    }
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHistc.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertMatElementWiseEquals("", expected.row(i), Arma.histc(_testMatrix.row(i), _centers));
    }
  }

  /**
   * Test method for {@link arma.Arma#histcMat(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testHistcMatAbstractMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHistc.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.histcMat(_testMatrix, _centers));
  }

  /**
   * Test method for {@link arma.Arma#histcMat(arma.AbstractMat, arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testHistcMatAbstractMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHistc.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.histcMat(_testMatrix, _centers, 0));
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesHistogramm/testHistc.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.histcMat(_testMatrix, _centers, 1));
  }

}
