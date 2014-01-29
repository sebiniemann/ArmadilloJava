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
class ViewElemCols extends AbstractView {

  /**
   * The vector of specified indices
   */
  protected AbstractMat _vector_of_column_indices;

  /**
   * The current row number within the sub view
   */
  protected int         _row_number;

  /**
   * The current column number within the sub view
   */
  protected int         _col_number;

  /**
   * Creates a shallow copy of the specified matrix and restrict the access to a sub view.
   * 
   * @param matrix The matrix
   * @param first_row The first row position
   * @param last_row The last row position
   * @param first_col The first column position
   * @param last_col The last column position
   */
  protected ViewElemCols(AbstractMat matrix, AbstractMat vector_of_column_indices) {
    super(matrix);

    n_rows = matrix.n_rows;
    n_cols = vector_of_column_indices.n_elem;
    n_elem = n_rows * n_cols;

    _vector_of_column_indices = vector_of_column_indices;
  }

  @Override
  protected void iteratorReset() {
    _row_number = n_rows;
    _col_number = -1;
  }

  @Override
  protected int iteratorNext() {
    if (_row_number >= n_rows) {
      _iterator = (int) _vector_of_column_indices._data[++_col_number] * n_rows;
      /**
       * The iterator will already point to the next element
       */
      return _iterator;
    } else {
      return ++_iterator;
    }
  }
}