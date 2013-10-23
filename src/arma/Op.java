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

import org.ejml.data.DenseMatrix64F;

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
   * Increment (unary arithmetic operator)
   */
  INCREMENT,
  /**
   * Decrement (unary arithmetic operator)
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
   * <b>Non-canonical:</b> Creates a column vector containing a {@code 1} for each element for which an element-wise
   * evaluation of {@code a operator b} holds true and a {@code 0} otherwise.
   * 
   * @param a The left-hand side operand.
   * @param operator The operator to be used.
   * @param b The right-hand side operand.
   * @return The created column vector.
   * 
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operator besides relational operators
   *           is provided.
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if both arguments are matrices but have an unequal
   *           number of elements.
   */
  public static Mat evaluate(Mat a, Op operator, Mat b) throws UnsupportedOperationException, IllegalArgumentException {
    if (a.n_elem != b.n_elem) {
      throw new IllegalArgumentException("The provided matrices must have the same number of elements, but their amount were " + a.n_elem + " and " + b.n_elem + ".");
    }

    DenseMatrix64F result = new DenseMatrix64F(a.n_elem, 1);
    DenseMatrix64F memptrA = a.memptr();
    DenseMatrix64F memptrB = b.memptr();

    for (int i = 0; i < a.n_elem; i++) {
      if (evaluate(memptrA.get(i), operator, memptrB.get(i))) {
        result.set(a.convertToRowMajorOrdering(i), 1);
      }
    }

    return new Mat(result);
  }

  /**
   * <b>Non-canonical:</b> Creates a column vector containing a {@code 1} for each element for which an element-wise
   * evaluation of {@code a operator b} holds true and a {@code 0} otherwise.
   * 
   * @param a The left-hand side operand.
   * @param operator The operator to be used.
   * @param b The right-hand side operand.
   * @return The created column vector.
   * 
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operator besides relational operators
   *           is provided.
   * 
   * @see #evaluate(Mat, Op, Mat)
   */
  public static Mat evaluate(Mat a, Op operator, double b) throws UnsupportedOperationException {
    DenseMatrix64F result = new DenseMatrix64F(a.n_elem, 1);
    DenseMatrix64F memptrA = a.memptr();

    for (int i = 0; i < a.n_elem; i++) {
      if (evaluate(memptrA.get(i), operator, b)) {
        result.set(a.convertToRowMajorOrdering(i), 1);
      }
    }

    return new Mat(result);
  }

  /**
   * <b>Non-canonical:</b> Creates a column vector containing a {@code 1} for each element for which an element-wise
   * evaluation of {@code a operator b} holds true and a {@code 0} otherwise.
   * 
   * @param a The left-hand side operand.
   * @param operator The operator to be used.
   * @param b The right-hand side operand.
   * @return The created column vector.
   * 
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operator besides relational operators
   *           is provided.
   * 
   * @see #evaluate(Mat, Op, Mat)
   */
  public static Mat evaluate(double a, Op operator, Mat b) throws UnsupportedOperationException {
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
   * Returns true if the evaluation of {@code a operator b} holds true.
   * 
   * @param a The left-hand side operand.
   * @param operator The operator to be used.
   * @param b The right-hand side operand.
   * @return The boolean result.
   * 
   * @throws IllegalArgumentException Thrown if any of the operands is not a number (NaN).
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operator besides relational operators
   *           is provided.
   */
  private static boolean evaluate(double a, Op operator, double b) throws IllegalArgumentException, UnsupportedOperationException {
    if (Double.isNaN(a) || Double.isNaN(b)) {
      throw new IllegalArgumentException("Relational operations on NaN are not supported.");
    }

    switch (operator) {
      case STRICT_LESS:
        if (a < b) {
          return true;
        }
        break;
      case LESS:
        if (a <= b) {
          return true;
        }
        break;
      case EQUAL:
        if (a == b) {
          return true;
        }
        break;
      case NOT_EQUAL:
        if (a != b) {
          return true;
        }
        break;
      case GREATER:
        if (a >= b) {
          return true;
        }
        break;
      case STRICT_GREATER:
        if (a > b) {
          return true;
        }
        break;
      default:
        throw new UnsupportedOperationException("Only relational operators are supported.");
    }

    return false;
  }

  /**
   * <b>Non-canonical:</b> Performs the requested unary arithmetic operation {@code a operator}.
   * <p>
   * Prints a warning to Sysout.out if an overflow is detected.
   * 
   * @param a The left-hand side operand.
   * @param operation The operation to be performed. Only unary arithmetic operators.
   * @return The result.
   * 
   * @throws IllegalArgumentException Thrown if the operand is not a number (NaN).
   * @throws UnsupportedOperationException Thrown if another operation besides unary arithmetic operators is requested.
   */
  static double getResult(double a, Op operation) throws IllegalArgumentException, UnsupportedOperationException {
    double result = 0;

    if (Double.isNaN(a)) {
      throw new IllegalArgumentException("Arithmetic operations on NaN are not supported.");
    }

    switch (operation) {
      case INCREMENT:
        result = a++;
        break;
      case DECREMENT:
        result = a--;
        break;
      default:
        throw new UnsupportedOperationException("Only unary arithmetic operators are supported.");
    }

    if (!Double.isInfinite(a) && Double.isInfinite(result)) {
      System.out.println("WARNING: Overflow detected. Requested " + a + " " + operation);
      Thread.dumpStack();
    }

    return result;
  }

  /**
   * <b>Non-canonical:</b> Performs the requested binary arithmetic operation {@code a operator b} or sets {@code a} to
   * the value of {@code b} if {@code operation = }{@link Op#EQUAL}.
   * <p>
   * Prints a warning to Sysout.out if an overflow or underflow is detected.
   * 
   * @param a The left-hand side operand.
   * @param operation The operation to be performed. Only binary arithmetic operators and equality are supported.
   * @param b The right-hand side operand.
   * @return The result.
   * 
   * @throws IllegalArgumentException Thrown if any of the operands is not a number (NaN).
   * @throws ArithmeticException Thrown if division by zero or infinity is requested.
   * @throws UnsupportedOperationException Thrown if another operation besides binary arithmetic operators or equality
   *           is requested.
   */
  static double getResult(double a, Op operation, double b) throws IllegalArgumentException, ArithmeticException, UnsupportedOperationException {
    double result = 0;

    if (Double.isNaN(a) || Double.isNaN(b)) {
      throw new IllegalArgumentException("Arithmetic operations on NaN are not supported.");
    }

    switch (operation) {
      case EQUAL:
        result = b;
        break;
      case PLUS:
        result = a + b;
        break;
      case MINUS:
        result = a - b;
        break;
      case TIMES:
      case ELEMTIMES:
        result = a * b;
        break;
      case ELEMDIVIDE:
        if (b == 0) {
          throw new ArithmeticException("Division by zero.");
        }

        if (Double.isInfinite(b)) {
          throw new ArithmeticException("Division by infinity.");
        }

        result = a / b;
        break;
      default:
        throw new UnsupportedOperationException("Only arithmetic operators and equality are supported.");
    }

    if (operation.equals(TIMES) || operation.equals(ELEMTIMES) || operation.equals(ELEMDIVIDE)) {
      if (a != 0 && b != 0 && result == 0) {
        System.out.println("WARNING: Underflow detected. Requested " + a + " " + operation + " " + b);
        Thread.dumpStack();
      }
    }

    if (!Double.isInfinite(a) && !Double.isInfinite(b) && Double.isInfinite(result)) {
      System.out.println("WARNING: Overflow detected. Requested " + a + " " + operation + " " + b);
      Thread.dumpStack();
    }

    return result;
  }
}