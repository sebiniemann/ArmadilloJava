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

import static arma.TestUtil.assertMatEquals;

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
public class TestArmaDecomposition {

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
   * Test method for {@link arma.Arma#chol(arma.AbstractMat)}.
   */
  @Test
  public void testCholAbstractMat() {
    if (_filename.startsWith("hilbert.")) {
      return;
    }

    if (!_testMatrix.is_symmetric() || !_testMatrix.is_positive_definite()) {
      return;
    }

    Mat R = Arma.chol(_testMatrix);

    assertMatEquals(_filename, _testMatrix, R.t().times(R));
  }

  /**
   * Test method for {@link arma.Arma#chol(arma.Mat, arma.AbstractMat)}.
   */
  @Test
  public void testCholMatAbstractMat() {
    if (_filename.startsWith("hilbert.")) {
      return;
    }

    if (!_testMatrix.is_symmetric() || !_testMatrix.is_positive_definite()) {
      return;
    }

    Mat R = new Mat();
    Arma.chol(R, _testMatrix);

    assertMatEquals(_filename, _testMatrix, R.t().times(R));
  }

  /**
   * Test method for {@link arma.Arma#eig_sym(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testEig_symAbstractMat() throws IOException {
    if (!_testMatrix.is_square() || !_testMatrix.is_symmetric()) {
      return;
    }

    Mat expectedEigenValues = new Mat();
    expectedEigenValues.load("./test/data/expected/TestArmaDecomposition/testEig_sym.eigenValues." + _filename);

    assertMatEquals(_filename, expectedEigenValues, Arma.eig_sym(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#eig_sym(arma.Mat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testEig_symMatAbstractMat() throws IOException {
    if (!_testMatrix.is_square() || !_testMatrix.is_symmetric()) {
      return;
    }

    Mat expectedEigenValues = new Mat();
    expectedEigenValues.load("./test/data/expected/TestArmaDecomposition/testEig_sym.eigenValues." + _filename);

    Mat eigenValues;

    eigenValues = new Mat(1, _testMatrix.n_cols);
    Arma.eig_sym(eigenValues, _testMatrix);

    assertMatEquals(_filename, expectedEigenValues, eigenValues);

    eigenValues = new Mat(_testMatrix.n_rows, 1);
    Arma.eig_sym(eigenValues, _testMatrix);

    assertMatEquals(_filename, expectedEigenValues, eigenValues);
  }

  /**
   * Test method for {@link arma.Arma#eig_sym(arma.Mat, arma.Mat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testEig_symMatMatAbstractMat() throws IOException {
    if (!_testMatrix.is_square() || !_testMatrix.is_symmetric()) {
      return;
    }

    Mat expectedEigenValues = new Mat();
    expectedEigenValues.load("./test/data/expected/TestArmaDecomposition/testEig_sym.eigenValues." + _filename);

    Mat eigenValues;
    Mat eigenVectors;

    eigenValues = new Mat(1, _testMatrix.n_cols);
    eigenVectors = new Mat();
    Arma.eig_sym(eigenValues, eigenVectors, _testMatrix);

    assertMatEquals(_filename, expectedEigenValues, eigenValues);
    for (int n = 0; n < eigenValues.n_elem; n++) {
      double eigenValue = eigenValues.at(n);
      assertMatEquals(_filename + ", eigen value " + eigenValue, Arma.zeros(_testMatrix.n_rows, 1), (_testMatrix.minus(Arma.eye(_testMatrix.n_rows, _testMatrix.n_cols).times(eigenValue))).times(eigenVectors.col(n)));
    }

    eigenValues = new Mat(_testMatrix.n_rows, 1);
    eigenVectors = new Mat();
    Arma.eig_sym(eigenValues, eigenVectors, _testMatrix);

    assertMatEquals(_filename, expectedEigenValues, eigenValues);
    for (int n = 0; n < eigenValues.n_elem; n++) {
      double eigenValue = eigenValues.at(n);
      assertMatEquals(_filename + ", eigen value " + eigenValue, Arma.zeros(_testMatrix.n_rows, 1), (_testMatrix.minus(Arma.eye(_testMatrix.n_rows, _testMatrix.n_cols).times(eigenValue))).times(eigenVectors.col(n)));
    }
  }

  /**
   * Test method for {@link arma.Arma#inv(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testInvAbstractMat() throws IOException {
    if (!_testMatrix.is_invertable()) {
      return;
    }

    Mat expectedInverse = new Mat();
    expectedInverse.load("./test/data/expected/TestArmaDecomposition/testInv." + _filename);

    assertMatEquals(_filename, expectedInverse, Arma.inv(_testMatrix), 1e-9);
  }

  /**
   * Test method for {@link arma.Arma#inv(arma.Mat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testInvMatAbstractMat() throws IOException {
    if (!_testMatrix.is_invertable()) {
      return;
    }

    Mat expectedInverse = new Mat();
    expectedInverse.load("./test/data/expected/TestArmaDecomposition/testInv." + _filename);

    Mat inverse = new Mat();
    Arma.inv(inverse, _testMatrix);

    assertMatEquals(_filename, expectedInverse, inverse, 1e-9);
  }

  /**
   * Test method for {@link arma.Arma#lu(arma.Mat, arma.Mat, arma.AbstractMat)}.
   */
  @Test
  public void testLuMatMatAbstractMat() {
    if (_filename.startsWith("hilbert.") || _filename.startsWith("hankel.")) {
      return;
    }

    if (!_testMatrix.is_square()) {
      return;
    }

    Mat L = new Mat();
    Mat U = new Mat();

    Arma.lu(L, U, _testMatrix);

    assertMatEquals(_filename, _testMatrix, L.times(U));
  }

  /**
   * Test method for {@link arma.Arma#lu(arma.Mat, arma.Mat, arma.Mat, arma.AbstractMat)}.
   */
  @Test
  public void testLuMatMatMatAbstractMat() {
    if (!_testMatrix.is_square()) {
      return;
    }

    Mat L = new Mat();
    Mat U = new Mat();
    Mat P = new Mat();

    Arma.lu(L, U, P, _testMatrix);

    assertMatEquals(_filename + ", P.t() = P.i()", P.t(), P.i());
    assertMatEquals(_filename + ", Arma.trimatu(L) = L", Arma.trimatl(L), L);
    assertMatEquals(_filename + ", Arma.trimatu(U) = U", Arma.trimatu(U), U);
    assertMatEquals(_filename + ", X = P.t().times(L).times(U)", _testMatrix, P.t().times(L).times(U));
  }

  /**
   * Test method for {@link arma.Arma#pinv(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testPinvAbstractMat() throws IOException {
    if (_filename.startsWith("zeros.")) {
      return;
    }

    Mat expectedPseudoInverse = new Mat();
    expectedPseudoInverse.load("./test/data/expected/TestArmaDecomposition/testPinv." + _filename);

    assertMatEquals(_filename, expectedPseudoInverse, Arma.pinv(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#pinv(arma.AbstractMat, double)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testPinvAbstractMatDouble() throws IOException {
    if (_filename.startsWith("zeros.")) {
      return;
    }

    Mat expectedPseudoInverse = new Mat();
    expectedPseudoInverse.load("./test/data/expected/TestArmaDecomposition/testPinv." + _filename);

    assertMatEquals(_filename, expectedPseudoInverse, Arma.pinv(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#pinv(arma.Mat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testPinvMatAbstractMat() throws IOException {
    if (_filename.startsWith("zeros.")) {
      return;
    }

    Mat expectedPseudoInverse = new Mat();
    expectedPseudoInverse.load("./test/data/expected/TestArmaDecomposition/testPinv." + _filename);

    Mat pseudoInverse = new Mat();
    Arma.pinv(pseudoInverse, _testMatrix);

    assertMatEquals(_filename, expectedPseudoInverse, pseudoInverse);
  }

  /**
   * Test method for {@link arma.Arma#pinv(arma.Mat, arma.AbstractMat, double)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testPinvAbstractMatMatDouble() throws IOException {
    if (_filename.startsWith("zeros.")) {
      return;
    }

    Mat expectedPseudoInverse = new Mat();
    expectedPseudoInverse.load("./test/data/expected/TestArmaDecomposition/testPinv." + _filename);

    Mat pseudoInverse = new Mat();
    Arma.pinv(pseudoInverse, _testMatrix);

    assertMatEquals(_filename, expectedPseudoInverse, pseudoInverse);
  }

  /**
   * Test method for {@link arma.Arma#qr(arma.Mat, arma.Mat, arma.AbstractMat)}.
   */
  @Test
  public void testQr() {
    if (!_testMatrix.is_square()) {
      return;
    }

    Mat Q = new Mat();
    Mat R = new Mat();

    Arma.qr(Q, R, _testMatrix);

    assertMatEquals(_filename + ", Q.t() = Q.i()", Q.t(), Q.i());
    assertMatEquals(_filename + ", Arma.trimatu(R) = R", Arma.trimatu(R), R);
    assertMatEquals(_filename + ", X = Q.times(R)", _testMatrix, Q.times(R));
  }

  /**
   * Test method for {@link arma.Arma#solve(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSolveAbstractMatAbstractMat() throws IOException {
    if (_filename.startsWith("hilbert.5x5.")) {
      return;
    }

    if (!_testMatrix.is_square()) {
      return;
    }

    for (Object[] object : getTestMatrices()) {
      Mat B = (Mat) object[1];

      if (_testMatrix.n_rows == B.n_rows && _testMatrix.is_invertable()) {
        assertMatEquals(_filename, _testMatrix.i().times(B), Arma.solve(_testMatrix, B));
      }
    }
  }

  /**
   * Test method for {@link arma.Arma#solve(arma.Mat, arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSolveMatAbstractMatAbstractMat() throws IOException {
    if (_filename.startsWith("hilbert.5x5.")) {
      return;
    }

    if (!_testMatrix.is_square()) {
      return;
    }

    for (Object[] object : getTestMatrices()) {
      Mat B = (Mat) object[1];

      if (_testMatrix.n_rows == B.n_rows && _testMatrix.is_invertable()) {
        Mat X = new Mat();
        Arma.solve(X, _testMatrix, B);

        assertMatEquals(_filename, _testMatrix.i().times(B), X);
      }
    }
  }

  /**
   * Test method for {@link arma.Arma#svd(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSvdAbstractMat() throws IOException {
    Mat expectedS = new Mat();
    expectedS.load("./test/data/expected/TestArmaDecomposition/testSvd." + _filename);

    assertMatEquals(_filename + ", expectedS", expectedS, Arma.svd(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#svd(arma.Mat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSvdMatAbstractMat() throws IOException {
    Mat expectedS = new Mat();
    expectedS.load("./test/data/expected/TestArmaDecomposition/testSvd." + _filename);

    Mat S = new Mat();
    Arma.svd(S, _testMatrix);

    assertMatEquals(_filename + ", expectedS", expectedS, S);
  }

  /**
   * Test method for {@link arma.Arma#svd(arma.Mat, arma.Mat, arma.Mat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSvdMatMatMatAbstractMat() throws IOException {
    Mat expectedS = new Mat();
    expectedS.load("./test/data/expected/TestArmaDecomposition/testSvd." + _filename);

    Mat U = new Mat();
    Mat S = new Mat();
    Mat V = new Mat();

    Arma.svd(U, S, V, _testMatrix);

    assertMatEquals(_filename + ", U.t(), U.i()", U.t(), U.i());
    assertMatEquals(_filename + ", expectedS", expectedS, S);
    assertMatEquals(_filename + ", V.t(), V.i()", V.t(), V.i());

    if (_testMatrix.is_square()) {
      assertMatEquals(_filename + ", X = U.times(Arma.diagmat(S)).times(V.t())", _testMatrix, U.times(Arma.diagmat(S)).times(V.t()));
    }
  }

}
