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
 * @author Daniel Kiechle <kiechle@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic {

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

    input.load("./test/data/input/logic.mat");
    testMatrices.add(new Object[]{new Mat(input)});

    return testMatrices;
  }

  /**
   * The test matrix
   */
  @Parameter
  public Mat _testMatrix;

  /**
   * Test method for {@link arma.Arma#any(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testAnyMat() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertTrue(j + "th column", (expected.at(j) == 1) == Arma.any(_testMatrix.col(j)));
    }
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertTrue(i + "th row", (expected.at(i) == 1) == Arma.any(_testMatrix.row(i)));
    }
  }

  /**
   * Test method for {@link arma.Arma#anyMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testAnyMatMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.anyMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#anyMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testAnyMatMatInt() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.anyMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.anyMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#all(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testAllMat() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertTrue(j + "th column", (expected.at(j) == 1) == Arma.all(_testMatrix.col(j)));
    }
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertTrue(i + "th row", (expected.at(i) == 1) == Arma.all(_testMatrix.row(i)));
    }
  }

  /**
   * Test method for {@link arma.Arma#allMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testAllMatMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.allMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#allMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testAllMatMatInt() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.allMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.allMat(_testMatrix, 1));
  }

}
