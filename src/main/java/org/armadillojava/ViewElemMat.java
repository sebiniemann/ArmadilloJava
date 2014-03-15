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
class ViewElemMat extends AbstractView {

  /**
   * Vector of specified indices
   */
  protected final AbstractMat _vector_of_indices;

  /**
   * Current element number within the sub view
   */
  protected int               _elem_number;

  /**
   * Creates a shallow copy of the specified matrix and restrict the access to a sub view.
   * 
   * @param matrix The matrix
   * @param vector_of_indices The elements
   */
  protected ViewElemMat(final AbstractMat matrix, final AbstractMat vector_of_indices) {
    super(matrix);

    n_rows = vector_of_indices.n_elem;
    n_cols = 1;
    n_elem = n_rows;

    _vector_of_indices = vector_of_indices;
  }

  @Override
  protected void iteratorReset() {
    super.iteratorReset();
    
    _elem_number = -1;
  }

  @Override
  protected int iteratorNext() {
    super.iteratorNext();
    
    return (int) _vector_of_indices._data[++_elem_number];
  }
  
}
