/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package arma;

import java.util.Iterator;

/**
 * Provides an iterator class for {@link Mat} and its submatrices.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class MatIterator implements Iterator<Double> {

  /**
   * The matrix
   */
  private BaseMat _matrix;
  
  /**
   * The current position
   */
  private int _currentPosition;

  /**
   * Initialises the iterator.
   * 
   * @param matrix The matrix
   */
  public MatIterator(BaseMat matrix) {
    _matrix = matrix;
    _currentPosition = 0;
  }

  @Override
  public boolean hasNext() {
    return (_currentPosition < _matrix.n_elem);
  }

  @Override
  public Double next() {
    return _matrix._matrix[_matrix.getElementPosition(_currentPosition++)];
  }

  @Override
  public void remove() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Removal is not supported.");
  }
}
