/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.armadillojava;

import static org.armadillojava.TestUtil.assertMatElementWiseEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic {

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

    input.load("./test/data/input/statistics.mat");
    testMatrices.add(new Object[]{new Mat(input)});

    return testMatrices;
  }

  /**
   * The test matrix
   */
  @Parameter
  public Mat _testMatrix;

  /**
   * Test method for {@link arma.Arma#cor(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCorAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCor.n0.mat");
    assertMatElementWiseEquals("", expected, Arma.cor(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#cor(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCorAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCor.n0.mat");
    assertMatElementWiseEquals("", expected, Arma.cor(_testMatrix, 0));
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCor.n1.mat");
    assertMatElementWiseEquals("", expected, Arma.cor(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#cor(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCorAbstractMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCor.n0.mat");
    assertMatElementWiseEquals("", expected, Arma.cor(_testMatrix, _testMatrix));

    for (int j = 0; j < _testMatrix.n_cols; j++) {
      for (int jj = 0; jj < _testMatrix.n_cols; jj++) {
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j), _testMatrix.col(jj)));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j), _testMatrix.col(jj).t()));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j).t(), _testMatrix.col(jj)));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j).t(), _testMatrix.col(jj).t()));
      }
    }
  }

  /**
   * Test method for {@link arma.Arma#cor(arma.AbstractMat, arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCorAbstractMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCor.n0.mat");
    assertMatElementWiseEquals("", expected, Arma.cor(_testMatrix, _testMatrix, 0));

    for (int j = 0; j < _testMatrix.n_cols; j++) {
      for (int jj = 0; jj < _testMatrix.n_cols; jj++) {
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j), _testMatrix.col(jj), 0));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j), _testMatrix.col(jj).t(), 0));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j).t(), _testMatrix.col(jj), 0));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j).t(), _testMatrix.col(jj).t(), 0));
      }
    }

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCor.n1.mat");
    assertMatElementWiseEquals("", expected, Arma.cor(_testMatrix, _testMatrix, 1));

    for (int j = 0; j < _testMatrix.n_cols; j++) {
      for (int jj = 0; jj < _testMatrix.n_cols; jj++) {
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j), _testMatrix.col(jj), 1));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j), _testMatrix.col(jj).t(), 1));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j).t(), _testMatrix.col(jj), 1));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cor(_testMatrix.col(j).t(), _testMatrix.col(jj).t(), 1));
      }
    }
  }

  /**
   * Test method for {@link arma.Arma#cov(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCovAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCov.n0.mat");
    assertMatElementWiseEquals("", expected, Arma.cov(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#cov(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCovAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCov.n0.mat");
    assertMatElementWiseEquals("", expected, Arma.cov(_testMatrix, 0));
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCov.n1.mat");
    assertMatElementWiseEquals("", expected, Arma.cov(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#cov(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCovAbstractMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCov.n0.mat");
    assertMatElementWiseEquals("", expected, Arma.cov(_testMatrix, _testMatrix));

    for (int j = 0; j < _testMatrix.n_cols; j++) {
      for (int jj = 0; jj < _testMatrix.n_cols; jj++) {
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j), _testMatrix.col(jj)));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j), _testMatrix.col(jj).t()));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j).t(), _testMatrix.col(jj)));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j).t(), _testMatrix.col(jj).t()));
      }
    }
  }

  /**
   * Test method for {@link arma.Arma#cov(arma.AbstractMat, arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCovAbstractMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCov.n0.mat");
    assertMatElementWiseEquals("", expected, Arma.cov(_testMatrix, _testMatrix, 0));

    for (int j = 0; j < _testMatrix.n_cols; j++) {
      for (int jj = 0; jj < _testMatrix.n_cols; jj++) {
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j), _testMatrix.col(jj), 0));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j), _testMatrix.col(jj).t(), 0));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j).t(), _testMatrix.col(jj), 0));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j).t(), _testMatrix.col(jj).t(), 0));
      }
    }

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesStatistic/testCov.n1.mat");
    assertMatElementWiseEquals("", expected, Arma.cov(_testMatrix, _testMatrix, 1));

    for (int j = 0; j < _testMatrix.n_cols; j++) {
      for (int jj = 0; jj < _testMatrix.n_cols; jj++) {
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j), _testMatrix.col(jj), 1));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j), _testMatrix.col(jj).t(), 1));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j).t(), _testMatrix.col(jj), 1));
        assertMatElementWiseEquals("", new Mat(new double[]{expected.at(j, jj)}), Arma.cov(_testMatrix.col(j).t(), _testMatrix.col(jj).t(), 1));
      }
    }
  }
}
