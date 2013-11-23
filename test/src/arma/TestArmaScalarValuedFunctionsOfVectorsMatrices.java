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
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaScalarValuedFunctionsOfVectorsMatrices {

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
    String filename;

    int dimensions[] = {1, 2, 3, 4, 5, 10, 100};
    String matrices[] = {"zeros", "ones", "eye", "hankel", "hilbert"};

    for (int numberOfRows : dimensions) {
      for (int numberOfColumns : dimensions) {
        for (String matrix : matrices) {
          if (!matrix.equals("hilbert") || (numberOfRows < 10 && numberOfColumns < 10)) {
            filename = matrix + "." + numberOfRows + "x" + numberOfColumns + ".mat";
            input.load("./test/data/input/" + filename);
            testMatrices.add(new Object[]{filename, new Mat(input)});
          }
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
   * Test method for {@link arma.Arma#accu(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testAccu() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testAccu." + _filename);
    assertEquals(_filename, expected.at(0), Arma.accu(_testMatrix), 1e-11);
  }

  /**
   * Test method for {@link arma.Arma#det(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testDet() throws IOException {
    if (!_testMatrix.is_square()) {
      return;
    }

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testDet." + _filename);
    assertEquals(_filename, expected.at(0), Arma.det(_testMatrix), 1e-11);
  }

  /**
   * Test method for {@link arma.Arma#log_det(double[], int[], arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testLog_det() throws IOException {
    if (!_testMatrix.is_square()) {
      return;
    }

    double[] value = {0};
    int[] sign = {0};
    Arma.log_det(value, sign, _testMatrix);

    assertEquals(_filename, Arma.det(_testMatrix), sign[0] * Math.exp(value[0]), 1e-15);
  }

  /**
   * Test method for {@link arma.Arma#norm(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testNormAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.p1." + _filename);
    assertEquals(_filename, expected.at(0), Arma.norm(_testMatrix, 1), 1e-11);
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.p2." + _filename);
    assertEquals(_filename, expected.at(0), Arma.norm(_testMatrix, 2), 1e-8);
  }

  /**
   * Test method for {@link arma.Arma#norm(arma.AbstractMat, String)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testNormAbstractMatString() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.pinf." + _filename);
    assertEquals(_filename, expected.at(0), Arma.norm(_testMatrix, "inf"), 1e-11);
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.pfro." + _filename);
    assertEquals(_filename, expected.at(0), Arma.norm(_testMatrix, "fro"), 1e-8);

    if (_testMatrix.is_vec()) {
      expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm.p-inf." + _filename);
      assertEquals(_filename, expected.at(0), Arma.norm(_testMatrix, "-inf"), 1e-11);
    }
  }

  /**
   * Test method for {@link arma.Arma#rank(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testRankAbstractMat() throws IOException {
    if (!_testMatrix.is_square()) {
      return;
    }

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testRank." + _filename);
    assertEquals(_filename, expected.at(0), Arma.rank(_testMatrix), 1e-11);
  }

  /**
   * Test method for {@link arma.Arma#rank(arma.AbstractMat, double)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testRankAbstractMatDouble() throws IOException {
    if (!_testMatrix.is_square()) {
      return;
    }

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testRank." + _filename);
    assertEquals(_filename, expected.at(0), Arma.rank(_testMatrix), 1e-11);
  }

  /**
   * Test method for {@link arma.Arma#trace(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testTrace() throws IOException {
    if (!_testMatrix.is_square()) {
      return;
    }

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testTrace." + _filename);
    assertEquals(_filename, expected.at(0), Arma.trace(_testMatrix), 1e-11);
  }

}
