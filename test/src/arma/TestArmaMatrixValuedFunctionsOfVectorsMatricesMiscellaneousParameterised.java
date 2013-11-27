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
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised {

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
    String filename;

    int dimensions[] = {1, 2, 3, 4, 5, 10, 100};
    String matrices[] = {"numbered"};

    for (int numberOfRows : dimensions) {
      for (int numberOfColumns : dimensions) {
        for (String matrix : matrices) {
          filename = matrix + "." + numberOfRows + "x" + numberOfColumns + ".mat";
          input.load("./test/data/input/" + filename);
          testMatrices.add(new Object[]{filename, new Mat(input)});
        }
      }
    }

    return testMatrices;
  }

  /**
   * The filename of the test matrix
   */
  @Parameter(0)
  public String _filename;

  /**
   * The test matrix
   */
  @Parameter(1)
  public Mat    _testMatrix;

  /**
   * Test method for {@link arma.Arma#trans(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testTrans() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testTrans." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.trans(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#diagvec(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testDiagvecAbstractMat() throws IOException {
    if (!_testMatrix.is_vec()) {
      return;
    }

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testDiagvec.k0." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.diagvec(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#diagvec(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testDiagvecAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    for (int k = -_testMatrix.n_rows + 1; k < _testMatrix.n_cols; k++) {
      expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testDiagvec.k" + k + "." + _filename);
      assertMatElementWiseEquals(_filename + ", k = " + k, expected, Arma.diagvec(_testMatrix, k));
    }
  }

  /**
   * Test method for {@link arma.Arma#vectorise(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testVectoriseAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testVectorise.d0." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.vectorise(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#vectorise(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testVectoriseAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testVectorise.d0." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.vectorise(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousParameterised/testVectorise.d1." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.vectorise(_testMatrix, 1));
  }
}
