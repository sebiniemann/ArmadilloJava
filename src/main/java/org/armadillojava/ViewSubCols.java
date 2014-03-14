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
 * Provides shallow column vectors sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class ViewSubCols extends AbstractView {

  /**
   * The first position of the sub view within the underlying matrix
   */
  protected final int _firstPosition;

  /**
   * Creates a shallow copy of the specified matrix and restrict the access to a sub view.
   * 
   * @param matrix The matrix
   * @param first_col The first column position
   * @param n_cols The number of columns
   */
  protected ViewSubCols(final AbstractMat matrix, final int first_col, final int n_cols) {
    super(matrix);

    this.n_rows = matrix.n_rows;
    this.n_cols = n_cols;
    this.n_elem = this.n_rows * this.n_cols;

    _firstPosition = first_col * this.n_rows;
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
