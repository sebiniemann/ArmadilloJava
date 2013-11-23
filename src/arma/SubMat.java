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
class SubMat extends AbstractMat {

  /**
   * The current index
   */
  private int _n;

  /**
   * The first corresponding index
   */
  int         _a;

  /**
   * The remaining rows
   */
  private int _remainingRows;

  /**
   * The difference between the last index of a row and the next index of the next row
   */
  int         _stepRow;

  /**
   * Creates a shallow copy of a matrix and restrict its access to a subvector from row {@code a} to {@code b}.
   * 
   * @param vector The vector
   * @param a The first element position
   * @param b The last element position
   */
  protected SubMat(AbstractMat vector, int a, int b) {
    vector.isNonVectorDetection();
    vector.isElementRangeOutOfBoundsDetection(a, b);

    if (is_colvec()) {
      n_rows = 1;
      n_cols = b - a + 1;
    } else {
      n_rows = b - a + 1;
      n_cols = 1;
    }
    n_elem = n_rows * n_cols;

    _underlyingMatrix = vector;
    _matrix = _underlyingMatrix._matrix;

    _stepRow = vector.n_rows - n_rows;
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
  protected SubMat(AbstractMat matrix, int ai, int bi, int aj, int bj) {
    matrix.isRowRangeOutOfBoundsDetection(ai, bi);
    matrix.isColumnRangeOutOfBoundsDetection(aj, bj);

    n_rows = bi - ai + 1;
    n_cols = bj - aj + 1;
    n_elem = n_rows * n_cols;

    _underlyingMatrix = matrix;
    _matrix = _underlyingMatrix._matrix;

    _stepRow = matrix.n_rows - n_rows;
    _a = ai + aj * n_rows;
  }

  @Override
  protected void iteratorReset() {
    _n = _a;
    _remainingRows = n_rows;
  }

  @Override
  protected int iteratorNext() {
    super.iteratorNext();

    if (_underlyingMatrix.is_vec()) {
      return _n++;
    } else {
      if (_remainingRows < 1) {
        _remainingRows = n_rows;
        _n += _stepRow;
      }

      _remainingRows--;

      return _n++;
    }

  }

  @Override
  protected int getElementIndex(int i, int j) {
    int n;
    if (_underlyingMatrix.is_vec()) {
      n = i;
    } else {
      n = i + j * n_rows;
    }

    return _underlyingMatrix.getElementIndex(n);
  }

  @Override
  protected int getElementIndex(int n) {
    int nn;
    if (_underlyingMatrix.is_vec()) {
      nn = n;
    } else {
      int j = (n / n_rows);
      nn = _a + n + j * (_stepRow + 1);
    }

    return _underlyingMatrix.getElementIndex(nn);
  }
}
