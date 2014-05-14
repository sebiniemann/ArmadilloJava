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
 * Provides shallow row vector sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class ViewSubRow extends AbstractView {

  /**
   * The first position of the sub view within the underlying matrix.
   */
  protected final int _first_position;

  /**
   * Current position of the sub view within the underlying matrix
   */
  protected int       _current_position;

  /**
   * Creates a shallow copy of the specified matrix and restrict its access to a sub view.
   * 
   * @param matrix The matrix
   * @param row_number The row position
   * @param first_col The first column position
   * @param n_cols The number of columns
   */
  protected ViewSubRow(final AbstractMat matrix, final int row_number, final int first_col, final int n_cols) {
    super(matrix);

    this.n_rows = 1;
    this.n_cols = n_cols;
    this.n_elem = this.n_cols;

    _first_position = row_number + first_col * matrix.n_rows;
  }

  /**
   * Creates a shallow copy of the specified matrix and restrict its access to a sub view.
   * 
   * @param matrix The matrix
   * @param row_number The row position
   */
  protected ViewSubRow(final AbstractMat matrix, final int row_number) {
    super(matrix);

    n_rows = 1;
    n_cols = matrix.n_cols;
    n_elem = n_cols;

    _first_position = row_number;
  }

  @Override
  protected void iteratorReset() {
    super.iteratorReset();

    _current_position = _first_position - _matrix.n_rows;
  }

  @Override
  protected int iteratorNext() {
    super.iteratorNext();

    _current_position += _matrix.n_rows;
    return _current_position;
  }
}
