/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 * Sebastian Niemann - Lead developer
 * Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

/**
 * Provides predefined constants to identify arithmetic operators.
 * <p>
 * Unary arithmetic operators: <br>
 * {@link #INCREMENT} and {@link #DECREMENT}
 * <p>
 * Binary arithmetic operators: <br>
 * {@link #PLUS}, {@link #MINUS}, {@link #TIMES}, {@link #ELEMTIMES} and {@link #ELEMDIVIDE}
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
   * Division (binary arithmetic operator)
   */
  DIVIDE,
  /**
   * Element-wise division (binary arithmetic operator)
   */
  ELEMDIVIDE,
  /**
   * Equality (binary arithmetic operator)
   */
  EQUAL,
  /**
   * Equality (relational operator)
   */
}
