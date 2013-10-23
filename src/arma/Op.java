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
    if (a.n_elem != b.n_elem) {
      throw new IllegalArgumentException("Both provided matrices must have the same number of elements, but had " + a.n_elem + " and " + b.n_elem + " elements.");
    }

    Mat result = new Mat(a.n_elem, 1);
    for (int n = 0; n < a.n_elem; n++) {
      if (evaluate(a._matrix.get(n), operator, b._matrix.get(n))) {
        result._matrix.set(n, 1);
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
      if (evaluate(a._matrix.get(n), operator, b)) {
        result._matrix.set(n, 1);
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
   * <b>Non-canonical:</b> Evaluates an unary arithmetic operation {@code a operator}.
   * 
   * @param a The left-hand side operand
   * @param operator The operator (only unary arithmetic operators)
   * @return The result
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  static double getResult(double a, Op operator) throws IllegalArgumentException, UnsupportedOperationException {
    double result = 0;

    if (Double.isNaN(a)) {
      throw new IllegalArgumentException("NaN is not a valid operand.");
    }

    switch (operator) {
      case INCREMENT:
        result = a++;
        break;
      case DECREMENT:
        result = a--;
        break;
      default:
        throw new UnsupportedOperationException("Only unary arithmetic operators are supported.");
    }
    
    underflowOverflowDetection(a, operator, result);
    return result;
  }

  /**
   * <b>Non-canonical:</b> Evaluates an binary arithmetic operation {@code a operator b} or returns {@code b} if {@code operation} = {@link Op#EQUAL}.
   * 
   * @param a The left-hand side operand
   * @param operator The operator (only binary arithmetic operators)
   * @param b The right-hand side operand
   * @return The result
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws ArithmeticException Division by zero.
   * @throws ArithmeticException Division by infinity.
   * @throws UnsupportedOperationException Only arithmetic operators and equality are supported.
   */
  static double getResult(double a, Op operator, double b) throws IllegalArgumentException, ArithmeticException, UnsupportedOperationException {
    double result = 0;

    if (Double.isNaN(a) || Double.isNaN(b)) {
      throw new IllegalArgumentException("NaN is not a valid operand.");
    }

    switch (operator) {
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

    underflowOverflowDetection(a, operator, b, result);
    return result;
  }
  
  /**
   * Detects if the result of an unary operation was affected by an overflow.
   * <p>
   * Note: The supported unary operators cannot result in an underflow.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param result The calculated result
   * 
   * @throws ArithmeticException Overflow detected. {@code a operator} resulted in {@code result}.
   */
  static void underflowOverflowDetection(double a, Op operator, double result) throws ArithmeticException {
    if (!Double.isInfinite(a) && Double.isInfinite(result)) {
      throw new ArithmeticException("Overflow detected. " + a + " " + operator + " resulted in " + result + ".");
    }
  }
  
  /**
   * Detects if the result of an binary operation was affected by an underflow or overflow.
   * <p>
   * The overflow detection is only active if the operator is one of {@link #TIMES}, {@link #ELEMTIMES} or {@link #ELEMDIVIDE}.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param b The right-hand side operand
   * @param result The calculated result
   * 
   * @throws ArithmeticException Underflow detected. {@code a operator b} resulted in {@code result}.
   * @throws ArithmeticException Overflow detected. {@code a operator b} resulted in {@code result}.
   */
  static void underflowOverflowDetection(double a, Op operator, double b, double result) throws ArithmeticException {
    if (operator.equals(TIMES) || operator.equals(ELEMTIMES) || operator.equals(ELEMDIVIDE)) {
      if (a != 0 && b != 0 && result == 0) {
        throw new ArithmeticException("Underflow detected. " + a + " " + operator + " " + b + " resulted in " + result + ".");
      }
    }

    if (!Double.isInfinite(a) && !Double.isInfinite(b) && Double.isInfinite(result)) {
      throw new ArithmeticException("Overflow detected. " + a + " " + operator + " " + b + " resulted in " + result + ".");
    }
  }
}