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
 * Provides shallow column vector sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class ViewSubCol extends AbstractView {

  /**
   * The first position of the sub view within the underlying matrix
   */
  protected final int _firstPosition;

  /**
   * Creates a shallow copy of the specified matrix and restrict its access to a sub view.
   * 
   * @param matrix The matrix
   * @param col_number The column position
   * @param first_row The first row position
   * @param n_rows The number of rows
   */
  protected ViewSubCol(final AbstractMat matrix, final int col_number, final int first_row, final int n_rows) {
    super(matrix);

    this.n_rows = n_rows;
    this.n_cols = 1;
    this.n_elem = this.n_rows;

    _firstPosition = first_row + col_number * matrix.n_cols;
  }

  /**
   * Creates a shallow copy of the specified matrix and restrict the access to a sub view.
   * 
   * @param matrix The matrix
   * @param col_number The column position
   */
  protected ViewSubCol(AbstractMat matrix, int col_number) {
    super(matrix);

    n_rows = matrix.n_rows;
    n_cols = 1;
    n_elem = n_rows;

    _firstPosition = col_number * n_rows;
  }

  @Override
  protected void iteratorReset() {
    _iterator = _firstPosition - 1;
  }

  @Override
  protected int iteratorNext() {
    return ++_iterator;
  }
}
