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
 * TODO
 * 
 * @author Sebastian Niemann
 */
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

  /**
   * TODO
   * 
   * @param matrix TODO
   */
  public AbstractView(final AbstractMat matrix) {
    _matrix = matrix;
    _data = matrix._data;
  }

  /**
   * Resets the iterator
   */
  protected void iteratorReset() {
    _iterator = 0;
  }

  /**
   * Returns the next iterator
   * 
   * @return TODO
   */
  protected int iteratorNext() {
    return _iterator++;
  }

  /**
   * Returns true if iterator has yet to reach the last position.
   * 
   * @return TODO
   */
  protected final boolean iteratorHasNext() {
    return _iterator < n_elem;
  }

  /**
   * TODO
   * 
   * @param value TODO
   */
  protected void fill(double value) {
    iteratorReset();
    while (iteratorHasNext()) {
      _data[iteratorNext()] = value;
    }
  }

  /**
   * TODO
   * 
   * @param binary_operator TODO
   * @param rightHandOperand TODO
   * 
   * @throws UnsupportedOperationException TODO
   */
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
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] *= rightHandOperand;
        }
        break;
      case DIVIDE:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] /= rightHandOperand;
        }
        break;
      default:
        throw new UnsupportedOperationException("Unexpected operator (" + binary_operator + ").");
    }
  }

  /**
   * TODO
   * 
   * @param binary_operator TODO
   * @param rightHandOperand TODO
   * 
   * @throws UnsupportedOperationException TODO
   */
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

  /**
   * TODO
   * 
   * @param binary_operator TODO
   * @param rightHandOperand TODO
   * 
   * @throws UnsupportedOperationException TODO
   */
  protected void inPlace(final Op binary_operator, final AbstractView rightHandOperand) throws UnsupportedOperationException {
    switch (binary_operator) {
      case EQUAL:
        iteratorReset();
        rightHandOperand.iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] = rightHandOperand._data[rightHandOperand.iteratorNext()];
        }
        break;
      default:
        throw new UnsupportedOperationException("Unexpected operator (" + binary_operator + ").");
    }
  }
}
