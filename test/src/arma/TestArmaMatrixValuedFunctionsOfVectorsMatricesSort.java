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
import static org.junit.Assert.*;

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
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesSort {

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
    for (int n = 0; n < input.n_elem; n++) {
      double value = input.at(n);
      if (!Arma.is_finite(value)) {
        input.at(n, Op.EQUAL, 0);
      }
    }

    testMatrices.add(new Object[]{new Mat(input)});

    return testMatrices;
  }

  /**
   * The test matrix
   */
  @Parameter
  public Mat _testMatrix;

  /**
   * Test method for {@link arma.Arma#sort(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSortAbstractMat() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertMatElementWiseEquals(j + "th column", expected.col(j), Arma.sort(_testMatrix.col(j)));
    }

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertMatElementWiseEquals(i + "th row", expected.row(i), Arma.sort(_testMatrix.row(i)));
    }
  }

  /**
   * Test method for {@link arma.Arma#sort(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSortAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertMatElementWiseEquals(j + "th column", expected.col(j), Arma.sort(_testMatrix.col(j), 0));
    }

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t1.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertMatElementWiseEquals(j + "th column", expected.col(j), Arma.sort(_testMatrix.col(j), 1));
    }

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertMatElementWiseEquals(i + "th row", expected.row(i), Arma.sort(_testMatrix.row(i), 0));
    }

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t1.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertMatElementWiseEquals(i + "th row", expected.row(i), Arma.sort(_testMatrix.row(i), 1));
    }
  }

  /**
   * Test method for {@link arma.Arma#sortMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSortMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.sortMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#sortMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSortMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.sortMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.sortMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#sortMat(arma.AbstractMat, int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSortMatAbstractMatIntInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.sortMat(_testMatrix, 0, 0));

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.sortMat(_testMatrix, 1, 0));

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t0.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.sortMat(_testMatrix, 0, 1));

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testSort.t1.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.sortMat(_testMatrix, 1, 1));
  }

  /**
   * Test method for {@link arma.Arma#sort_index(arma.AbstractMat)}.
   */
  @Test
  public void testSort_indexAbstractMat() {
    Mat testMatrix = Arma.vectorise(_testMatrix);

    Mat indicies = Arma.stable_sort_index(testMatrix);
    for (int n = 0; n < indicies.n_elem - 1; n++) {
      assertTrue("At position " + n, testMatrix.at((int) indicies.at(n)) <= testMatrix.at((int) indicies.at(n + 1)));
    }
  }

  /**
   * Test method for {@link arma.Arma#sort_index(arma.AbstractMat, int)}.
   */
  @Test
  public void testSort_indexAbstractMatInt() {
    Mat testMatrix = Arma.vectorise(_testMatrix);

    Mat indicies;

    indicies = Arma.stable_sort_index(testMatrix, 0);
    for (int n = 0; n < indicies.n_elem - 1; n++) {
      assertTrue("At position " + n, testMatrix.at((int) indicies.at(n)) <= testMatrix.at((int) indicies.at(n + 1)));
    }

    indicies = Arma.stable_sort_index(testMatrix, 1);
    for (int n = 0; n < indicies.n_elem - 1; n++) {
      assertTrue("At position " + n, testMatrix.at((int) indicies.at(n)) >= testMatrix.at((int) indicies.at(n + 1)));
    }
  }

  /**
   * Test method for {@link arma.Arma#stable_sort_index(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testStable_sort_indexAbstractMat() throws IOException {
    Mat testMatrix = Arma.vectorise(_testMatrix);

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testStable_sort_index.t0.mat");
    assertMatElementWiseEquals("", expected, Arma.stable_sort_index(testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#stable_sort_index(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testStable_sort_indexAbstractMatInt() throws IOException {
    Mat testMatrix = Arma.vectorise(_testMatrix);

    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testStable_sort_index.t0.mat");
    assertMatElementWiseEquals("", expected, Arma.stable_sort_index(testMatrix, 0));

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSort/testStable_sort_index.t1.mat");
    assertMatElementWiseEquals("", expected, Arma.stable_sort_index(testMatrix, 1));
  }

}
