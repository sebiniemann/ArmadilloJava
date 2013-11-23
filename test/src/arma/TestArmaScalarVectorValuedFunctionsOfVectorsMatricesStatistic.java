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
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
public class TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic {

  /**
   * Returns the matrices to be tested.
   * 
   * @return The test matrices 
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
   * Test method for {@link arma.Arma#min(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMin() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.min(_testMatrix.col(j)), 1e-15);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.min(_testMatrix.row(i)), 1e-15);
    }
  }

  /**
   * Test method for {@link arma.Arma#minMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMinMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.minMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#minMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMinMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.minMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.minMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#max(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMax() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.max(_testMatrix.col(j)), 1e-15);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.max(_testMatrix.row(i)), 1e-15);
    }
  }

  /**
   * Test method for {@link arma.Arma#maxMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMaxMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.maxMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#maxMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMaxMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.maxMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.maxMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#mean(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMean() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.mean(_testMatrix.col(j)), 1e-15);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.mean(_testMatrix.row(i)), 1e-15);
    }
  }

  /**
   * Test method for {@link arma.Arma#meanMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMeanMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.meanMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#meanMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMeanMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.meanMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.meanMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#median(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMedian() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.median(_testMatrix.col(j)), 1e-15);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.median(_testMatrix.row(i)), 1e-15);
    }
  }

  /**
   * Test method for {@link arma.Arma#medianMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMedianMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.medianMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#medianMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testMedianMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.medianMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.medianMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#stddev(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testStddevAbstractMat() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.stddev(_testMatrix.col(j)), 1e-9);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.stddev(_testMatrix.row(i)), 1e-9);
    }
  }

  /**
   * Test method for {@link arma.Arma#stddev(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testStddevAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.stddev(_testMatrix.col(j), 0), 1e-9);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.stddev(_testMatrix.col(j), 1), 1e-9);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.stddev(_testMatrix.row(i), 0), 1e-9);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.stddev(_testMatrix.row(i), 1), 1e-9);
    }
  }

  /**
   * Test method for {@link arma.Arma#stddevMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testStddevMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#stddevMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testStddevMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#stddevMat(arma.AbstractMat, int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testStddevMatAbstractMatIntInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 0, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 1, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 0, 1));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 1, 1));
  }

  /**
   * Test method for {@link arma.Arma#var(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testVarAbstractMat() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.var(_testMatrix.col(j)), 1e-9);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.var(_testMatrix.row(i)), 1e-9);
    }
  }

  /**
   * Test method for {@link arma.Arma#var(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testVarAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.var(_testMatrix.col(j), 0), 1e-9);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.var(_testMatrix.col(j), 1), 1e-9);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.var(_testMatrix.row(i), 0), 1e-9);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.var(_testMatrix.row(i), 1), 1e-9);
    }
  }

  /**
   * Test method for {@link arma.Arma#varMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testVarMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#varMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testVarMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#varMat(arma.AbstractMat, int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testVarMatAbstractMatIntInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 0, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 1, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 0, 1));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 1, 1));
  }

}
