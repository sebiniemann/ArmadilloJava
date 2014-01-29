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
 * Provides shallow matrix diagonal views of {@link Mat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class ViewDiag extends AbstractView {

  /**
   * The first position of the sub view within the underlying matrix
   */
  protected int _firstPosition;

  /**
   * The rows to skip within the underlying matrix to move one position to another within the sub view.
   */
  protected int _n_rows_skip;
  
  /**
   * Creates a shallow copy of a matrix and restrict the access to its {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, its the main diagonal.
   * <li>For {@code k} > 0, its the {@code k}th super-diagonal.
   * <li>For {@code k} < 0, its the {@code k}th sub-diagonal.
   * </ul>
   * 
   * @param matrix The matrix
   * @param k The diagonal position
   */
  public ViewDiag(AbstractMat matrix, int k) {
    super(matrix);
    
    if (k > 0) {
      n_rows = Math.min(matrix.n_rows, matrix.n_cols - k);
      _firstPosition = k * matrix.n_rows;
    } else {
      n_rows = Math.min(matrix.n_rows + k, matrix.n_cols);
      _firstPosition = -k;
    }
    
    n_cols = 1;
    n_elem = n_rows * n_cols;

    _n_rows_skip = matrix.n_rows + 1;
  }

  @Override
  protected void iteratorReset() {
    _iterator = _firstPosition - _n_rows_skip;
  }

  @Override
  protected int iteratorNext() {
    _iterator += _n_rows_skip;
    return _iterator;
  }

}
