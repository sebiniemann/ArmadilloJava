package arma;

import static org.junit.Assert.assertEquals;

/**
 * Provides methods for often repeated tasks within tests.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class TestUtil {

  /**
   * Asserts that the provides Matrices are element-wise equal to within a positive delta and have the same layout. The
   * assertion is based on org.junit.Assert.assertEquals.
   * 
   * @param message 
   * @param A The expected matrix.
   * @param B The matrix to check against expected.
   */
  public static void assertMatElementWiseEquals(String message, Mat A, Mat B) {
    assertMatEquals(message, A, B, 1e-11);
  }

  /**
   * Asserts that the provides Matrices are element-wise equal to within a positive delta and have the same layout. The
   * assertion is based on org.junit.Assert.assertEquals.
   * 
   * @param message 
   * @param A The expected matrix.
   * @param B The matrix to check against expected.
   * @param delta The maximum element-wise delta between expected and actual for which both matrices are still
   *          considered equal.
   */
  public static void assertMatElementWiseEquals(String message, Mat A, Mat B, double delta) {
    assertEquals(message + ". Number of rows", A.n_rows, B.n_rows);
    assertEquals(message + ". Number of columns", A.n_cols, B.n_cols);

    for (int n = 0; n < A.n_elem; n++) {
      double a = A.at(n);
      double b = B.at(n);

      assertEquals(message + " at position " + n, a, b, ((a == 0) ? 1 : Math.abs(a)) * delta);
    }
  }

  /**
   * Asserts that the provides Matrices are element-wise equal to within a positive delta and have the same layout. The
   * assertion is based on org.junit.Assert.assertEquals.
   * 
   * @param message 
   * @param A The expected matrix.
   * @param B The matrix to check against expected.
   */
  public static void assertMatEquals(String message, Mat A, Mat B) {
    assertMatEquals(message, A, B, 1e-11);
  }

  /**
   * Asserts that the provides Matrices are element-wise equal to within a positive delta and have the same layout. The
   * assertion is based on org.junit.Assert.assertEquals.
   * 
   * @param message 
   * @param A The expected matrix.
   * @param B The matrix to check against expected.
   * @param delta The maximum element-wise delta between expected and actual for which both matrices are still
   *          considered equal.
   */
  public static void assertMatEquals(String message, Mat A, Mat B, double delta) {
    assertEquals(message + ". Number of rows", A.n_rows, B.n_rows);
    assertEquals(message + ". Number of columns", A.n_cols, B.n_cols);

    double absoluteMax = 0;
    for (int n = 0; n < A.n_elem; n++) {
      double a = Math.abs(A.at(n));

      if(absoluteMax < a) {
        absoluteMax = a;
      }
    }
    
    if(absoluteMax == 0) {
      absoluteMax = 1;
    }
    
    for (int n = 0; n < A.n_elem; n++) {
      assertEquals(message + " at position " + n, A.at(n), B.at(n), absoluteMax * delta);
    }
  }
}
