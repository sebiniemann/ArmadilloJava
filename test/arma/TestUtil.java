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

/**
 * Provides methods for often repeated tasks within tests.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class TestUtil {
  /**
   * Asserts that the provides Matrices are element-wise equal to within a positive delta and have the same layout. The assertion is based on org.junit.Assert.assertEquals.
   * 
   * @param A The expected matrix.
   * @param B The matrix to check against expected.
   * @param delta The maximum element-wise delta between expected and actual for which both matrices are still considered equal.
   */
  public static void assertMatEquals(Mat A, Mat B, double delta) {
    assertEquals(A.n_rows, B.n_rows);
    assertEquals(A.n_cols, B.n_cols);
    
    for(int n = 0; n < A.n_elem; n++) {
      assertEquals(A.at(n), B.at(n), 0);
    }
  }
}
