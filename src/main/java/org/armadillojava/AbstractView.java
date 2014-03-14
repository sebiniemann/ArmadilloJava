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
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

abstract class AbstractView {

  /**
   * The underlying matrix
   */
  protected AbstractMat _matrix;

  /**
   * The internal data representation
   */
  protected double[]    _data;

  /**
   * The number of rows
   */
  public int            n_rows;

  /**
   * The number of columns
   */
  public int            n_cols;

  /**
   * The number of elements (same as {@code n_rows * n_cols}) .
   */
  public int            n_elem;

  /**
   * The current iterator
   */
  protected int         _iterator;

  public AbstractView(final AbstractMat matrix) {
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

  protected void fill(double value) {
    iteratorReset();
    while (iteratorHasNext()) {
      _data[iteratorNext()] = value;
    }
  }
  
  protected void inPlace(final Op unary_operator) throws UnsupportedOperationException {
    switch (unary_operator) {
      case INCREMENT:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()]++;
        }
        break;
      case DECREMENT:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()]--;
        }
        break;
      default:
        throw new UnsupportedOperationException("Unexpected operator (" + unary_operator + ").");
    }
  }

  protected void inPlace(final Op binary_operator, final double rightHandOperand) throws UnsupportedOperationException {
    switch (binary_operator) {
      case PLUS:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] += rightHandOperand;
        }
        break;
      case MINUS:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] -= rightHandOperand;
        }
        break;
      case TIMES:
      case ELEMTIMES:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] *= rightHandOperand;
        }
        break;
      case ELEMDIVIDE:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] /= rightHandOperand;
        }
        break;
      default:
        throw new UnsupportedOperationException("Unexpected operator (" + binary_operator + ").");
    }
  }

  protected void inPlace(final Op binary_operator, final AbstractMat rightHandOperand) throws UnsupportedOperationException {
    int n = 0;
    switch (binary_operator) {
      case EQUAL:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] = rightHandOperand._data[n++];
        }
        break;
      case PLUS:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] += rightHandOperand._data[n++];
        }
        break;
      case MINUS:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] -= rightHandOperand._data[n++];
        }
        break;
      case ELEMTIMES:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] *= rightHandOperand._data[n++];
        }
        break;
      case ELEMDIVIDE:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] /= rightHandOperand._data[n++];
        }
        break;
      default:
        throw new UnsupportedOperationException("Unexpected operator (" + binary_operator + ").");
    }
  }

  protected void inPlace(final Op binary_operator, final AbstractView rightHandOperand) throws UnsupportedOperationException {
    switch (binary_operator) {
      case EQUAL:
        iteratorReset();
        rightHandOperand.iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] = rightHandOperand._data[rightHandOperand.iteratorNext()];
        }
        break;
      case PLUS:
        iteratorReset();
        rightHandOperand.iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] += rightHandOperand._data[rightHandOperand.iteratorNext()];
        }
        break;
      case MINUS:
        iteratorReset();
        rightHandOperand.iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] -= rightHandOperand._data[rightHandOperand.iteratorNext()];
        }
        break;
      case ELEMTIMES:
        iteratorReset();
        rightHandOperand.iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] *= rightHandOperand._data[rightHandOperand.iteratorNext()];
        }
        break;
      case ELEMDIVIDE:
        iteratorReset();
        rightHandOperand.iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] /= rightHandOperand._data[rightHandOperand.iteratorNext()];
        }
        break;
      default:
        throw new UnsupportedOperationException("Unexpected operator (" + binary_operator + ").");
    }
  }
}
