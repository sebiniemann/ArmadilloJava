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
   * The original number of rows
   */
  boolean _orig_is_vec;
  /**
   * The original number of rows
   */
  int     _orig_n_rows;
  /**
   * The first element position
   */
  int     _a;

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
    _orig_n_rows = vector.n_rows;
    _orig_is_vec = vector.is_vec();

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
    _orig_n_rows = matrix.n_rows;
    _orig_is_vec = matrix.is_vec();

    n_rows = bi - ai + 1;
    n_cols = bj - aj + 1;
    n_elem = n_rows * n_cols;

    _a = matrix.getElementPosition(ai, aj);
  }

  @Override
  protected int getElementPosition(int n) {
    if (_orig_is_vec) {
      return n;
    } else {
      int j = (n / n_rows);
      return _a + (n - j * n_rows) + j * _orig_n_rows;
    }
  }
}
