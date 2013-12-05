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

/**
 * Provides predefined constants to identify arithmetic or relational operators.
 * <p>
 * Unary arithmetic operators: <br>
 * {@link #INCREMENT} and {@link #DECREMENT}
 * <p>
 * Binary arithmetic operators: <br>
 * {@link #PLUS}, {@link #MINUS}, {@link #TIMES}, {@link #ELEMTIMES} and {@link #ELEMDIVIDE}
 * <p>
 * Relational operators: <br>
 * {@link #EQUAL}, {@link #NOT_EQUAL}, {@link #STRICT_LESS}, {@link #LESS}, {@link #STRICT_GREATER} and {@link #GREATER}
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public enum Op {
  /**
   * Pre-increment (unary arithmetic operator)
   */
  INCREMENT,
  /**
   * Pre-decrement (unary arithmetic operator)
   */
  DECREMENT,
  /**
   * Summation (binary arithmetic operator)
   */
  PLUS,
  /**
   * Subtraction (binary arithmetic operator)
   */
  MINUS,
  /**
   * Multiplication (binary arithmetic operator)
   */
  TIMES,
  /**
   * Element-wise multiplication (binary arithmetic operator)
   */
  ELEMTIMES,
  /**
   * Element-wise division (binary arithmetic operator)
   */
  ELEMDIVIDE,
  /**
   * Equality (relational operator)
   */
  EQUAL,
  /**
   * Inequality (relational operator)
   */
  NOT_EQUAL,
  /**
   * Strict less than (relational operator)
   */
  STRICT_LESS,
  /**
   * Non-strict less than (relational operator)
   */
  LESS,
  /**
   * Strict greater than (relational operator)
   */
  STRICT_GREATER,
  /**
   * Non-strict greater than (relational operator)
   */
  GREATER;

  /**
   * <b>Non-canonical:</b> Creates a column vector containing a 1 for each element for which the evaluation of
   * {@code a operator b} holds true and 0 otherwise.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param b The right-hand side operand
   * @return The vector
   * 
   * @throws IllegalArgumentException NaN is not a valid operand value.
   * @throws IllegalArgumentException Both provided matrices must have the same number of elements, but had
   *           {@code a.n_elem} and {@code b.n_elem} elements.
   * @throws UnsupportedOperationException Only relational operators are supported.
   */
  public static Mat evaluate(Mat a, Op operator, Mat b) throws IllegalArgumentException, UnsupportedOperationException {
    AbstractMat.isNonEqualNumberOfElementsDetection(a.n_elem, b.n_elem);

    Mat result = new Mat(a.n_elem, 1, Fill.ZEROS);
    for (int n = 0; n < a.n_elem; n++) {
      if (evaluate(a.at(n), operator, b.at(n))) {
        result._matrix[n] = 1;
      }
    }

    return result;
  }

  /**
   * <b>Non-canonical:</b> Creates a column vector containing a 1 for each element for which the evaluation of
   * {@code a operator b} holds true and 0 otherwise.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param b The right-hand side operand
   * @return The vector
   * 
   * @throws IllegalArgumentException NaN is not a valid operand value.
   * @throws UnsupportedOperationException Only relational operators are supported.
   */
  public static Mat evaluate(Mat a, Op operator, double b) throws IllegalArgumentException, UnsupportedOperationException {
    Mat result = new Mat(a.n_elem, 1);
    for (int n = 0; n < a.n_elem; n++) {
      if (evaluate(a.at(n), operator, b)) {
        result._matrix[n] = 1;
      }
    }

    return result;
  }

  /**
   * <b>Non-canonical:</b> Creates a column vector containing a 1 for each element for which the evaluation of
   * {@code a operator b} holds true and 0 otherwise.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param b The right-hand side operand
   * @return The vector
   * 
   * @throws IllegalArgumentException NaN is not a valid operand value.
   * @throws UnsupportedOperationException Only relational operators are supported.
   */
  public static Mat evaluate(double a, Op operator, Mat b) throws IllegalArgumentException, UnsupportedOperationException {
    switch (operator) {
      case STRICT_LESS:
        return evaluate(b, Op.STRICT_GREATER, a);
      case LESS:
        return evaluate(b, Op.GREATER, a);
      case GREATER:
        return evaluate(b, Op.LESS, a);
      case STRICT_GREATER:
        return evaluate(b, Op.STRICT_LESS, a);
      case EQUAL:
      case NOT_EQUAL:
        return evaluate(b, operator, a);
      default:
        throw new UnsupportedOperationException("Only relational operators are supported.");
    }
  }

  /**
   * <b>Non-canonical:</b> Returns true if the evaluation of {@code a operator b} holds true and false otherwise.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param b The right-hand side operand
   * @return Whether the evaluation holds true
   * 
   * @throws IllegalArgumentException NaN is not a valid operand value.
   * @throws UnsupportedOperationException Only relational operators are supported.
   */
  private static boolean evaluate(double a, Op operator, double b) throws IllegalArgumentException, UnsupportedOperationException {
    if (Double.isNaN(a) || Double.isNaN(b)) {
      throw new IllegalArgumentException("NaN is not a valid operand value.");
    }

    switch (operator) {
      case STRICT_LESS:
        return (a < b);
      case LESS:
        return (a <= b);
      case EQUAL:
        return (a == b);
      case NOT_EQUAL:
        return (a != b);
      case GREATER:
        return (a >= b);
      case STRICT_GREATER:
        return (a > b);
      default:
        throw new UnsupportedOperationException("Only relational operators are supported.");
    }
  }
}