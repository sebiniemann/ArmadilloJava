package org.armadillojava;

import java.util.Arrays;

abstract class AbstractVector extends AbstractMat {

  /**
   * Resizes the vector to the specified number of elements and sets each element to a pseudo-random value drawn from
   * the standard uniform distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void randu(int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */
    
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
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void randn(int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */
    
    set_size(n_elem);
    
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextGaussian();
    }
  }

  /**
   * Resizes the vector to the specified number of elements and sets all elements to 1.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void ones(int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */
    
    set_size(n_elem);
    fill(1);
  }

  /**
   * Resizes the vector to the specified number of elements and sets all elements to 0.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void zeros(int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */
    
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
   * Resizes the vector to the specified number of elements.
   * <p>
   * Reuses the values of the elements and their positions.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void resize(int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */
    
    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem);
    System.arraycopy(temp, 0, _data, 0, temp.length);
  }

  /**
   * Resizes the vector to the specified number of elements.
   * <p>
   * If the requested size is equal to the current size, the existing memory is reused. Otherwise, new memory will be
   * allocated and left uninitialised.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  abstract public void set_size(int n_elem) throws NegativeArraySizeException;

  /**
   * Performs a in-place unary operation on the {@code first_index}th to {@code last_index} element.
   * 
   * @param first_index The first position
   * @param last_index The last position
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The first position ({@code first_index}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code last_index}) is out of bounds.
   */
  abstract public void subvec(int first_index, int last_index, Op unary_operator) throws IndexOutOfBoundsException {
    /*
     * The parameter "first_index", "last_index" and "unary_operator" are validated within set_size(int).
     */
    
    rows(first_index, last_index, unary_operator);
  }

  /**
   * Performs a in-place binary operation on the {@code first_index}th to {@code last_index} element with the specified
   * right-hand side operand.
   * 
   * @param first_index The first position
   * @param last_index The last position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first position ({@code first_index}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code last_index}) is out of bounds.
   */
  abstract public void subvec(int first_index, int last_index, Op binary_operator, double operand) {
    rows(first_index, last_index, binary_operator, operand);
  }

  /**
   * Performs a in-place binary operation on the {@code first_index}th to {@code last_index} element with the specified
   * right-hand side operand.
   * 
   * @param first_index The first position
   * @param last_index The last position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first position ({@code first_index}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code last_index}) is out of bounds.
   */
  abstract public void subvec(int first_index, int last_index, Op binary_operator, AbstractMat operand) {
    rows(first_index, last_index, binary_operator, operand);
  }

  /**
   * Performs a in-place unary operation on the {@code span._first}th to {@code span._last} element.
   * 
   * @param span The span
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The first position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code span._last}) is out of bounds.
   */
  public void subvec(Span span, Op unary_operator) {
    subvec(span._first, span._last, unary_operator);
  }

  /**
   * Performs a in-place binary operation on the {@code span._first}th to {@code span._last} element with the specified
   * right-hand side operand.
   * 
   * @param span The span
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code span._last}) is out of bounds.
   */
  public void subvec(Span span, Op binary_operator, double operand) {
    subvec(span._first, span._last, binary_operator, operand);
  }

  /**
   * Performs a in-place binary operation on the {@code span._first}th to {@code span._last} element with the specified
   * right-hand side operand.
   * 
   * @param span The span
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code span._last}) is out of bounds.
   */
  public void subvec(Span span, Op binary_operator, AbstractMat operand) {
    subvec(span._first, span._last, binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code first_index}th to {@code last_index} element.
   * 
   * @param first_index The first position
   * @param last_index The last position
   * 
   * @throws IndexOutOfBoundsException The first position ({@code first_index}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code last_index}) is out of bounds.
   */
  abstract public AbstractVector subvec(int first_index, int last_index) throws IndexOutOfBoundsException;

  /**
   * Returns a deep copy of the {@code span._first}th to {@code span._last} element.
   * 
   * @param span The span
   * 
   * @throws IndexOutOfBoundsException The first position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code span._last}) is out of bounds.
   */
  abstract public AbstractVector subvec(Span span) throws IndexOutOfBoundsException;
}
