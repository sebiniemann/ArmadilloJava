package arma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Contains all test cases for parametrised interface tests of non-element-wise operations in {@link Arma}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrix {

  /**
   * Returns all test matrices used for this test.
   * 
   * @return The test matrices.
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() {
    return TestData.getTestMatrices();
  }

  /**
   * A matrix to be used for this test.
   */
  @Parameter
  public Mat      _testMatrix;

  /**
   * The ground-truth determinant of {@link #_testMatrix}.
   */
  @Parameter(value = 1)
  public double   _determinant;

  /**
   * The ground-truth sum of all values of {@link #_testMatrix}.
   */
  @Parameter(value = 2)
  public double   _accu;

  /**
   * The ground-truth norms (Induced matrix 1-norm and Induced matrix 2-norm) of {@link #_testMatrix}.
   */
  @Parameter(value = 3)
  public double[] _norms;

  /**
   * Tests {@link Arma#det(Mat)} by comparison with {@link #_determinant}. Only symmetric matrices are considered.
   */
  @Test
  public void testDet() {
    assumeTrue(_testMatrix.n_cols == _testMatrix.n_rows);
    assertEquals(_determinant, Arma.det(_testMatrix), Math.abs(_determinant) * TestData.NUMERIC_TOLERANCE);
  }

  /**
   * Tests {@link Arma#accu(Mat)} by comparison with {@link #_determinant}.
   */
  @Test
  public void testAccu() {
    assertEquals(_accu, Arma.accu(_testMatrix), TestData.NUMERIC_TOLERANCE);
  }

  /**
   * Tests {@link Arma#qr(Mat, Mat, Mat)} by assertion that <code>Q^t = Q^{-1}</code>, <code>i > j => r_{i,j} = 0</code>
   * and <code>Q*R = X</test> holds.
   */
  @Test
  public void testQr() {
    Mat Q = new Mat();
    Mat R = new Mat();

    Arma.qr(Q, R, _testMatrix);

    Mat inverseQ = Q.i();
    Mat tranposeQ = Q.t();

    for (int n = 0; n < inverseQ.n_elem; n++) {
      assertEquals(tranposeQ.at(n), inverseQ.at(n), TestData.NUMERIC_TOLERANCE);
    }

    for (int i = 1; i < R.n_rows; i++) {
      for (int j = 0; j < i; j++) {
        assertEquals(0, R.at(i, j), 0);
      }
    }

    Mat X = Q.times(R);
    for (int n = 0; n < X.n_elem; n++) {
      assertEquals(_testMatrix.at(n), X.at(n), TestData.NUMERIC_TOLERANCE);
    }
  }

  /**
   * Tests {@link Arma#norm(Mat, int)} and {@link Arma#norm(Mat, String)} for non-vectors by comparison with
   * {@link #_norms}.
   */
  @Test
  public void testNorm() {
    assertEquals(_norms[0], Arma.norm(_testMatrix, 1), TestData.NUMERIC_TOLERANCE);
    assertEquals(_norms[1], Arma.norm(_testMatrix, 2), TestData.NUMERIC_TOLERANCE);
    assertEquals(_norms[2], Arma.norm(_testMatrix, "inf"), TestData.NUMERIC_TOLERANCE);
    assertEquals(_norms[3], Arma.norm(_testMatrix, "fro"), TestData.NUMERIC_TOLERANCE);
  }
}
