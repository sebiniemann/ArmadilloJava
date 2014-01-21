package org.armadillojava;

import java.io.IOException;
import java.io.InputStream;

abstract class AbstractView extends AbstractMat {

  /**
   * The underlying matrix
   */
  protected AbstractMat _matrix;
  
  /**
   * The current iterator
   */
  protected int _iterator;

  public AbstractView(AbstractMat matrix) {
    _matrix = matrix;
    _data = matrix._data;
  }

  /**
   * Resets the iterator
   */
  abstract protected void iteratorReset();

  /**
   * Returns true if iterator has yet to reach the last position.
   */
  protected final boolean iteratorHasNext() {
    return (_iterator < n_elem - 1);
  }

  /**
   * Returns the next iterator
   */
  abstract protected int iteratorNext();
  
  @Override
  public boolean load(InputStream stream, FileType file_type) throws AssertionError, IllegalArgumentException, IOException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public void reset() {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }
  
}
