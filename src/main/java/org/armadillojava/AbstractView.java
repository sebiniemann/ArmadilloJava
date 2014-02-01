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

  public AbstractView(AbstractMat matrix) {
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

  protected void inPlace(Op unary_operator) {
    switch (unary_operator) {
      case NEGATE:
        iteratorReset();
        while (iteratorHasNext()) {
          int n = iteratorNext();
          _data[n] = -_data[n];
        }
        break;
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
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  protected void inPlace(Op binary_operator, double rightHandOperand) {
    switch (binary_operator) {
      case EQUAL:
        iteratorReset();
        while (iteratorHasNext()) {
          _data[iteratorNext()] = rightHandOperand;
        }
        break;
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
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  protected void inPlace(Op binary_operator, AbstractMat rightHandOperand) {
    int n;
    switch (binary_operator) {
      case EQUAL:
        inPlaceEqual(rightHandOperand);
        break;
      case PLUS:
        iteratorReset();
        n = 0;
        while (iteratorHasNext()) {
          _data[iteratorNext()] += rightHandOperand._data[n++];
        }
        break;
      case MINUS:
        iteratorReset();
        n = 0;
        while (iteratorHasNext()) {
          _data[iteratorNext()] -= rightHandOperand._data[n++];
        }
        break;
      case ELEMTIMES:
        iteratorReset();
        n = 0;
        while (iteratorHasNext()) {
          _data[iteratorNext()] *= rightHandOperand._data[n++];
        }
        break;
      case ELEMDIVIDE:
        iteratorReset();
        n = 0;
        while (iteratorHasNext()) {
          _data[iteratorNext()] /= rightHandOperand._data[n++];
        }
        break;
      default:
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  protected void inPlace(Op binary_operator, AbstractView rightHandOperand) {
    switch (binary_operator) {
      case EQUAL:
        inPlaceEqual(rightHandOperand);
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
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  protected void inPlaceEqual(double rightHandOperand) {
    iteratorReset();
    while (iteratorHasNext()) {
      _data[iteratorNext()] = rightHandOperand;
    }
  }

  protected void inPlaceEqual(AbstractMat rightHandOperand) {
    iteratorReset();
    int n = 0;
    while (iteratorHasNext()) {
      _data[iteratorNext()] = rightHandOperand._data[n++];
    }
  }

  protected void inPlaceEqual(AbstractView rightHandOperand) {
    iteratorReset();
    rightHandOperand.iteratorReset();
    while (iteratorHasNext()) {
      _data[iteratorNext()] = rightHandOperand._data[rightHandOperand.iteratorNext()];
    }
  }
}
