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
class ViewElemMat extends AbstractView {

  /**
   * The vector of specified indices
   */
  protected AbstractMat _vector_of_indices;

  /**
   * Creates a shallow copy of the specified matrix and restrict the access to a sub view.
   * 
   * @param matrix The matrix
   * @param first_row The first row position
   * @param last_row The last row position
   * @param first_col The first column position
   * @param last_col The last column position
   */
  protected ViewElemMat(AbstractMat matrix, AbstractMat vector_of_indices) {
    super(matrix);

    n_rows = 1;
    n_cols = vector_of_indices.n_elem;
    n_elem = n_cols;

    _vector_of_indices = vector_of_indices;
  }

  @Override
  protected void iteratorReset() {
    _iterator = -1;
  }

  @Override
  protected int iteratorNext() {
    return (int) _vector_of_indices._data[++_iterator];
  }
}