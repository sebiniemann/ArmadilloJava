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
 * Used for shallow copies of diagonals of {@link Mat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class DiagMat extends BaseMat {
  /**
   * The diagonal position
   */
  int _k;

  /**
   * Creates a shallow copy of a matrix and restrict its access to its {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is used.
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is used.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is used.
   * </ul>
   * 
   * @param matrix The matrix
   * @param k The diagonal position
   */
  DiagMat(Mat matrix, int k) {
    matrix.isVectorDetection();

    int length;
    if (k > 0) {
      matrix.isColumnOutOfBoundsDetection(k);

      length = Math.min(matrix.n_rows, matrix.n_cols - k);
    } else {
      matrix.isRowOutOfBoundsDetection(-k);

      length = Math.min(matrix.n_rows + k, matrix.n_cols);
    }

    _matrix = matrix._matrix;

    n_rows = 1;
    n_cols = length;
    n_elem = n_rows * n_cols;

    _k = k;
  }

  @Override
  protected int getElementPosition(int n) {
    return n * n_rows + 1 - _k;
  }
}