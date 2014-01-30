package org.armadillojava;

abstract class AbstractView {

  /**
   * The underlying matrix
   */
  protected AbstractMat _matrix;

  /**
   * The internal data representation
   */
  protected double[] _data;

  /**
   * The number of rows
   */
  public int         n_rows;

  /**
   * The number of columns
   */
  public int         n_cols;

  /**
   * The number of elements (same as {@code n_rows * n_cols}) .
   */
  public int         n_elem;
  
  /**
   * The current iterator
   */
  protected int         _iterator;

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

  protected void inPlace(Op unary_operator) {
    // TODO Auto-generated method stub
  }
  
  protected void inPlace(Op binary_operator, double operand) {
    // TODO Auto-generated method stub
  }
  
  protected void inPlace(Op binary_operator, AbstractMat operand) {
    // TODO Auto-generated method stub
  }
}
