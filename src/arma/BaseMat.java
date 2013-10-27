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
 * Used as a base class for {@link Mat} and its submatrices.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
abstract class BaseMat implements Iterable<Double> {

  /**
   * The internal data representation of the matrix
   */
  protected double[] _matrix;

  /**
   * The number of rows
   */
  public int         n_rows;

  /**
   * The number of columns
   */
  public int         n_cols;

  /**
   * The number of elements (same as {@link #n_rows} * {@link #n_cols}) .
   */
  public int         n_elem;

  /**
   * Returns true if the matrix is a vector and false otherwise.
   * 
   * @return Whether the matrix is vector
   */
  public boolean is_vec() {
    return is_colvec() || is_rowvec();
  }

  /**
   * Returns true if the matrix is a column vector and false otherwise.
   * 
   * @return Whether the matrix is column vector
   */
  public boolean is_colvec() {
    return (n_cols == 1);
  }

  /**
   * Returns true if the matrix is a row vector and false otherwise.
   * 
   * @return Whether the matrix is row vector
   */
  public boolean is_rowvec() {
    return (n_rows == 1);
  }

  /**
   * Performs a element-wise unary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  protected void inPlace(Op operator) throws IllegalArgumentException, UnsupportedOperationException {
    for (int n = 0; n < n_elem; n++) {
      inPlace(n, operator);
    }
  }

  /**
   * Performs a element-wise unary right-hand side inplace operation on the {@code n}th element.
   * 
   * @param n The position
   * @param operator The operator
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  protected void inPlace(int n, Op operator) throws IllegalArgumentException, UnsupportedOperationException {
    int index = getElementPosition(n);
    double operand = _matrix[index];

    if (Double.isNaN(operand)) {
      throw new IllegalArgumentException("NaN is not a valid operand.");
    }

    double result;
    switch (operator) {
      case INCREMENT:
        result = _matrix[index]++;
        break;
      case DECREMENT:
        result = _matrix[index]--;
        break;
      default:
        throw new UnsupportedOperationException("Only unary arithmetic operators are supported.");
    }

    isUnderflowOverflowDetection(operand, operator, result);
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inPlace(Op operator, BaseMat operand) throws IllegalArgumentException, UnsupportedOperationException {
    isNonEqualNumberOfElementsDetection(n_elem, operand.n_elem);

    for (int n = 0; n < n_elem; n++) {
      inPlace(n, operator, operand._matrix[n]);
    }
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inPlace(Op operator, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    for (int n = 0; n < n_elem; n++) {
      inPlace(n, operator, operand);
    }
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on the {@code n}th element.
   * 
   * @param n The position
   * @param operator The operator
   * @param rightHandSideOperand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws ArithmeticException Division by zero.
   * @throws ArithmeticException Division by infinity.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inPlace(int n, Op operator, double rightHandSideOperand) throws IllegalArgumentException, UnsupportedOperationException {
    int index = getElementPosition(n);
    double leftHandSideOperand = _matrix[index];

    if (Double.isNaN(leftHandSideOperand) || Double.isNaN(rightHandSideOperand)) {
      throw new IllegalArgumentException("NaN is not a valid operand.");
    }

    double result;
    switch (operator) {
      case EQUAL:
        result = _matrix[index] = rightHandSideOperand;
        break;
      case PLUS:
        result = _matrix[index] += rightHandSideOperand;
        break;
      case MINUS:
        result = _matrix[index] -= rightHandSideOperand;
        break;
      case TIMES:
      case ELEMTIMES:
        result = _matrix[index] *= rightHandSideOperand;
        break;
      case ELEMDIVIDE:
        if (rightHandSideOperand == 0) {
          throw new ArithmeticException("Division by zero.");
        }

        if (Double.isInfinite(rightHandSideOperand)) {
          throw new ArithmeticException("Division by infinity.");
        }

        result = _matrix[index] /= rightHandSideOperand;
        break;
      default:
        throw new UnsupportedOperationException("Only binary arithmetic operators and equality are supported.");
    }

    isUnderflowOverflowDetection(leftHandSideOperand, operator, rightHandSideOperand, result);
  }

  /**
   * Updates the attributes {@link #n_rows}, {@link #n_cols} and {@link #n_elem}.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  protected void updateAttributes(int numberOfRows, int numberOfColumns) {
    n_rows = numberOfRows;
    n_cols = numberOfColumns;
    n_elem = n_rows * n_cols;
  }

  /**
   * Detects if the position is negative or one of NaN or +/-infinity and throws an exception if so.
   * 
   * @param n The position
   * 
   * @throws IllegalArgumentException The position must be non-negative.
   * @throws IllegalArgumentException NaN and +/-infinity are not valid positions value.
   */
  protected static void isInvalidPositionDetection(int n) throws IllegalArgumentException {
    if (n < 0) {
      throw new IllegalArgumentException("The position must be non-negative.");
    }

    if (Double.isInfinite(n) || Double.isNaN(n)) {
      throw new IllegalArgumentException("NaN and +/-infinity are not valid positions value.");
    }
  }

  /**
   * Detects if any position is invalid or the first position is not less than equal the last one and throws an
   * exception if so.
   * 
   * @param a The first position
   * @param b The last position
   * 
   * @throws IllegalArgumentException The first position needs to be less than equal the last, but were {@code a} and
   *           {@code b}.
   */
  protected static void isInvalidRangeDetection(int a, int b) throws IllegalArgumentException {
    if (a < b) {
      throw new IllegalArgumentException("The first position needs to be less than equal the last, but were " + a + " and " + b + ".");
    }

    isInvalidPositionDetection(a);
    isInvalidPositionDetection(b);
  }

  /**
   * Detects if the element position is invalid or out of bound and throws an exception if so.
   * 
   * @param n The element position
   * 
   * @throws ArrayIndexOutOfBoundsException The element position range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the element position range was ({@code n}, {@code n}).
   */
  protected void isElementOutOfBoundsDetection(int n) throws ArrayIndexOutOfBoundsException {
    isElementRangeOutOfBoundsDetection(n, n);
  }

  /**
   * Detects if the element position range is invalid or out of bound and throws an exception if so.
   * 
   * @param a The first element position
   * @param b The last element position
   * 
   * @throws ArrayIndexOutOfBoundsException The element position range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the element position range was ({@code a}, {@code b}).
   */
  protected void isElementRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b > n_rows) {
      throw new ArrayIndexOutOfBoundsException("The element position range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the element position range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if the result of an unary operation was affected by an overflow and throws an exception if so.
   * <p>
   * Note: The supported unary operators cannot result in an underflow.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param result The calculated result
   * 
   * @throws ArithmeticException Overflow detected. {@code a operator} resulted in {@code result}.
   */
  protected static void isUnderflowOverflowDetection(double a, Op operator, double result) throws ArithmeticException {
    if (!Double.isInfinite(a) && Double.isInfinite(result)) {
      throw new ArithmeticException("Overflow detected. " + a + " " + operator + " resulted in " + result + ".");
    }
  }

  /**
   * Detects if the result of an binary operation was affected by an underflow or overflow and throws an exception if
   * so.
   * <p>
   * The overflow detection is only active if the operator is one of {@link Op#TIMES}, {@link Op#ELEMTIMES} or
   * {@link Op#ELEMDIVIDE}.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param b The right-hand side operand
   * @param result The calculated result
   * 
   * @throws ArithmeticException Underflow detected. {@code a operator b} resulted in {@code result}.
   * @throws ArithmeticException Overflow detected. {@code a operator b} resulted in {@code result}.
   */
  protected static void isUnderflowOverflowDetection(double a, Op operator, double b, double result) throws ArithmeticException {
    if (operator.equals(Op.TIMES) || operator.equals(Op.ELEMTIMES) || operator.equals(Op.ELEMDIVIDE)) {
      if (a != 0 && b != 0 && result == 0) {
        throw new ArithmeticException("Underflow detected. " + a + " " + operator + " " + b + " resulted in " + result + ".");
      }
    }

    if (!Double.isInfinite(a) && !Double.isInfinite(b) && Double.isInfinite(result)) {
      throw new ArithmeticException("Overflow detected. " + a + " " + operator + " " + b + " resulted in " + result + ".");
    }
  }

  /**
   * Detects if the matrix is not a vector and throws an exception if so.
   * 
   * @param first The first amount
   * @param second The second amount
   * 
   * @throws IllegalArgumentException The numbers of elements must match, but were {@code first} and {@code second}.
   */
  protected static void isNonEqualNumberOfElementsDetection(int first, int second) throws IllegalArgumentException {
    if (first != second) {
      throw new IllegalArgumentException("The numbers of elements must match, but were " + first + " and " + second + ".");
    }
  }

  /**
   * Detects if the matrix is not a vector and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be a vector, but was a ({@link #n_rows}, {@link #n_cols}
   *           )-matrix.
   */
  protected void isNonVectorDetection() throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("The matrix must be a vector, but was a (" + n_rows + ", " + n_cols + ")-matrix.");
    }
  }

  /**
   * Detects if the matrix is a vector and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be a non-vector, but was a ({@link #n_rows}, {@link #n_cols}
   *           )-vector.
   */
  protected void isVectorDetection() throws UnsupportedOperationException {
    if (is_vec()) {
      throw new UnsupportedOperationException("The matrix must be a non-vector, but was a (" + n_rows + ", " + n_cols + ")-vector.");
    }
  }
  
  /**
   * Returns the element position based on the actual submatrix view.
   * 
   * @param n The element position
   * @return The actual element position
   */
  abstract protected int getElementPosition(int n);

  @Override
  public Iterator<Double> iterator() {
    return new MatIterator(this);
  }
}
