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

/**
 * Used for shallow copies of subviews of {@link Mat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class SubviewMat extends BaseMat {
  /**
   * The first element position
   */
  int _a;

  /**
   * Creates a shallow copy of a matrix and restrict its access to a subvector from row {@code a} to {@code b}.
   * 
   * @param vector The vector
   * @param a The first element position
   * @param b The last element position
   */
  SubviewMat(Mat vector, int a, int b) {
    vector.isNonVectorDetection();
    vector.isElementRangeOutOfBoundsDetection(a, b);

    _matrix = vector._matrix;

    if (is_colvec()) {
      n_rows = 1;
      n_cols = b - a + 1;
    } else {
      n_rows = b - a + 1;
      n_cols = 1;
    }
    n_elem = n_rows * n_cols;

    _a = a;
  }

  /**
   * Creates a shallow copy of a matrix and restrict its access to a submatrix from row {@code ai} to {@code bi} and
   * column {@code aj} to {@code bj}.
   * 
   * @param matrix The matrix
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   */
  SubviewMat(Mat matrix, int ai, int bi, int aj, int bj) {
    matrix.isRowRangeOutOfBoundsDetection(ai, bi);
    matrix.isColumnRangeOutOfBoundsDetection(aj, bj);

    _matrix = matrix._matrix;

    n_rows = bi - ai + 1;
    n_cols = bj - aj + 1;
    n_elem = n_rows * n_cols;

    _a = ai + aj * n_rows;
  }

  @Override
  protected int getElementPosition(int n) {
    if (is_vec()) {
      return n;
    } else {
      int jTimesN_Rows = (n / n_rows) * n_rows; // Note: jTimesN_Rows = Math.floor(n / n_rows) * n_rows != n
      return _a + (n - jTimesN_Rows) + jTimesN_Rows;
    }
  }
}
