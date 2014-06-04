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
 * Provides shallow column vector sub views of {@link AbstractMat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class ViewSubVec extends AbstractView {

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
   * @param first_elem The first element position
   * @param n_elem The number of elements
   */
  protected ViewSubVec(final AbstractMat matrix, final int first_elem, final int n_elem) {
    super(matrix);

    if(matrix.is_colvec()) {
      this.n_rows = n_elem;
      this.n_cols = 1;
    } else {
      this.n_rows = 1;
      this.n_cols = n_elem;
    }
    this.n_elem = n_elem;

    _first_position = first_elem;
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
