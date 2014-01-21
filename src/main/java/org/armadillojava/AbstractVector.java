package org.armadillojava;

import java.util.Arrays;

abstract class AbstractVector extends AbstractMat {

  /**
   * Returns the value of the {@code n}th element.
   * 
   * @param n The position
   */
  public double at(int n) {
    return _data[n];
  }

  /**
   * Resizes the vector to the specified number of elements and sets each element to a pseudo-random value drawn from
   * the standard uniform distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param n_elem The number of elements
   */
  public void randu(int n_elem) {
    set_size(n_elem);
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextDouble();
    }
  }

  /**
   * Resizes the vector to the specified number of elements and sets each element to a pseudo-random value drawn from
   * the standard normal distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param n_elem The number of elements
   */
  public void randn(int n_elem) {
    set_size(n_elem);
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextGaussian();
    }
  }

  /**
   * Resizes the vector to the specified number of elements and sets all elements to 1.
   * 
   * @param n_elem The number of elements
   */
  public void ones(int n_elem) throws NegativeArraySizeException {
    set_size(n_elem);
    fill(1);
  }

  /**
   * Resizes the vector to the specified number of elements and sets all elements to 0.
   * 
   * @param n_elem The number of elements
   */
  public void zeros(int n_elem) {
    set_size(n_elem);
    /*
     * All entries of an array are already set to 0 during creation.
     * Therefore, fill(0) will be skipped.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
  }

  /**
   * Causes the column vector to be empty.
   */
  public void reset() {
    set_size(0);
  }

  /**
   * Resizes the vector to the specified number of elements.
   * <p>
   * Reuses the values of the elements and their positions.
   * 
   * @param n_elem The number of elements
   */
  public void resize(int n_elem) {
    _data = Arrays.copyOf(_data, n_elem);
  }

  abstract public void set_size(int n_elem);

  public void subvec(int first_index, int last_index, Op unary_operator) {
    rows(first_index, last_index, unary_operator);
  }

  public void subvec(int first_index, int last_index, Op binary_operator, double operand) {
    rows(first_index, last_index, binary_operator, operand);
  }

  public void subvec(int first_index, int last_index, Op binary_operator, AbstractMat operand) {
    rows(first_index, last_index, binary_operator, operand);
  }

  public void subvec(Span span, Op unary_operator) {
    subvec(span._first, span._last, unary_operator);
  }

  public void subvec(Span span, Op binary_operator, double operand) {
    subvec(span._first, span._last, binary_operator, operand);
  }

  public void subvec(Span span, Op binary_operator, AbstractMat operand) {
    subvec(span._first, span._last, binary_operator, operand);
  }
}
