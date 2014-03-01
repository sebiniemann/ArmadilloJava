/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package org.armadillojava;

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
}