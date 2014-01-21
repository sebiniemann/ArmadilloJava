/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package org.armadillojava;

/**
 * Used for shallow copies of diagonals of {@link Mat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class MatViewDiag extends AbstractMat {

  /**
   * The diagonal index
   */
  final int           _k;

  /**
   * The current index
   */
  protected int _n;

  /**
   * The difference between the current and the next index
   */
  final int           _step;

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
  protected MatViewDiag(AbstractMat matrix, int k) {
    matrix.isVectorDetection();

    int length;
    if (k > 0) {
      matrix.isColumnOutOfBoundsDetection(k);

      length = Math.min(matrix.n_rows, matrix.n_cols - k);
    } else {
      matrix.isRowOutOfBoundsDetection(-k);

      length = Math.min(matrix.n_rows + k, matrix.n_cols);
    }

    n_rows = length;
    n_cols = 1;
    n_elem = n_rows * n_cols;

    _originalMatrix = matrix;
    _matrix = _originalMatrix._matrix;

    _step = _originalMatrix.n_rows + 1;
    _k = k;
  }

  @Override
  public void iteratorReset() {
    if (_k > 0) {
      _n = _k * _originalMatrix.n_rows;
    } else {
      _n = -_k;
    }

    _n -= _step;
  }

  @Override
  public int iteratorNext() {
    super.iteratorNext();

    _n += _step;
    return _originalMatrix.getPosition(_n);
  }

  @Override
  public int getPosition(int i, int j) {
    return _originalMatrix.getPosition(i);
  }

  @Override
  public int getPosition(int n) {
    int nn;
    if (_k > 0) {
      nn = n * (n_rows + 1) + _k * n_rows;
    } else {
      nn = n * (n_rows + 1) - _k;
    }

    return _originalMatrix.getPosition(nn);
  }

  @Override
  public double at(int i, int j) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double at(int n) {
    // TODO Auto-generated method stub
    return 0;
  }
}