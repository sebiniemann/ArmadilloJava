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
 * Provides shallow row vector sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class ViewSubRow extends AbstractView {

  /**
   * The first position of the sub view within the underlying matrix
   */
  protected int _firstPosition;

  /**
   * The rows to skip within the underlying matrix to move one position to another within the sub view.
   */
  protected int _n_rows_skip;

  /**
   * Creates a shallow copy of the specified matrix and restrict its access to a sub view.
   * 
   * @param matrix The matrix
   * @param row_number The row position
   * @param first_col The first column position
   * @param n_cols The number of columns
   */
  protected ViewSubRow(AbstractMat matrix, int row_number, int first_col, int n_cols) {
    super(matrix);

    this.n_rows = 1;
    this.n_cols = n_cols;
    this.n_elem = this.n_cols;

    _firstPosition = row_number + first_col * matrix.n_cols;
    _n_rows_skip = matrix.n_rows;
  }

  /**
   * Creates a shallow copy of the specified matrix and restrict its access to a sub view.
   * 
   * @param matrix The matrix
   * @param row_number The row position
   */
  protected ViewSubRow(AbstractMat matrix, int row_number) {
    super(matrix);

    n_rows = 1;
    n_cols = matrix.n_cols;
    n_elem = n_cols;

    _firstPosition = row_number;
    _n_rows_skip = matrix.n_rows;
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
