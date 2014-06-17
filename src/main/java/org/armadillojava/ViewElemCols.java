/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 * Sebastian Niemann - Lead developer
 * Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

/**
 * Provides shallow matrix non-contiguous sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann
 */
class ViewElemCols extends AbstractView {

  /**
   * Vector of specified indices
   */
  protected final double[] _vector_of_column_indices;

  /**
   * Current position of the sub view within the underlying matrix
   */
  protected int            _current_position;

  /**
   * Current row number within the sub view
   */
  protected int            _row_number;

  /**
   * Current column number within the sub view
   */
  protected int            _col_number;

  /**
   * Creates a shallow copy of the specified matrix and restrict the access to a sub view.
   * 
   * @param matrix The matrix
   * @param vector_of_column_indices The columns
   */
  protected ViewElemCols(final AbstractMat matrix, final double[] vector_of_column_indices) {
    super(matrix);

    n_rows = matrix.n_rows;
    n_cols = vector_of_column_indices.length;
    n_elem = n_rows * n_cols;

    _vector_of_column_indices = vector_of_column_indices;
  }

  @Override
  protected void iteratorReset() {
    super.iteratorReset();

    _row_number = 0;
    _col_number = 0;

    _current_position = (int) _vector_of_column_indices[0] * n_rows - 1;
  }

  @Override
  protected int iteratorNext() {
    super.iteratorNext();

    if (_row_number >= n_rows) {
      _row_number = 1;
      _current_position = (int) _vector_of_column_indices[++_col_number] * n_rows;
      return _current_position;
    } else {
      ++_row_number;
      return ++_current_position;
    }
  }

}
