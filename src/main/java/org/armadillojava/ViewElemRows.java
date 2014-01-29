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
 * Provides shallow matrix non-contiguous sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class ViewElemRows extends AbstractView {

  /**
   * The vector of specified indices
   */
  protected AbstractMat _vector_of_row_indices;

  /**
   * The current row number within the sub view
   */
  protected int         _row_number;

  /**
   * The number of elements to shift to focus on the current column
   */
  protected int         _col_shift;

  /**
   * Creates a shallow copy of the specified matrix and restrict the access to a sub view.
   * 
   * @param matrix The matrix
   * @param first_row The first row position
   * @param last_row The last row position
   * @param first_col The first column position
   * @param last_col The last column position
   */
  protected ViewElemRows(AbstractMat matrix, AbstractMat vector_of_row_indices) {
    super(matrix);

    n_rows = vector_of_row_indices.n_elem;
    n_cols = matrix.n_cols;
    n_elem = n_rows * n_cols;

    _vector_of_row_indices = vector_of_row_indices;
  }

  @Override
  protected void iteratorReset() {
    _row_number = -1;
    _col_shift = 0;
  }

  @Override
  protected int iteratorNext() {
    if (_row_number >= n_rows) {
      _row_number = 0;
      _col_shift += n_rows;
    }

    _iterator = (int) _vector_of_row_indices._data[_row_number++] + _col_shift;

    return _iterator;
  }
}