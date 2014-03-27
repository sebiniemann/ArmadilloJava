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
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

/**
 * Provides shallow matrix non-contiguous sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class ViewElemRows extends AbstractView {

  /**
   * Vector of specified indices.
   */
  protected final double[] _vector_of_row_indices;

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
   * @param vector_of_row_indices The rows
   */
  protected ViewElemRows(final AbstractMat matrix, final double[] vector_of_row_indices) {
    super(matrix);

    n_rows = vector_of_row_indices.length;
    n_cols = matrix.n_cols;
    n_elem = n_rows * n_cols;

    _vector_of_row_indices = vector_of_row_indices;
  }

  @Override
  protected void iteratorReset() {
    super.iteratorReset();

    _row_number = 0;
    _col_number = 0;

    _current_position = (int) _vector_of_row_indices[0] - n_rows;
  }

  @Override
  protected int iteratorNext() {
    super.iteratorNext();

    if (_col_number >= n_cols) {
      _col_number = 1;
      _current_position = (int) _vector_of_row_indices[++_row_number];
      return _current_position;
    } else {
      ++_col_number;

      _current_position += n_rows;
      return _current_position;
    }
  }

}
