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
 * Provides shallow column vectors sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann
 */
class ViewSubCols extends AbstractView {

  /**
   * The first position of the sub view within the underlying matrix.
   */
  protected final int _first_position;

  /**
   * Current position of the sub view within the underlying matrix
   */
  protected int       _current_position;

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

    _first_position = first_col * this.n_rows;
  }

  @Override
  protected void iteratorReset() {
    super.iteratorReset();

    _current_position = _first_position - 1;
  }

  @Override
  protected int iteratorNext() {
    super.iteratorNext();

    return ++_current_position;
  }
}
