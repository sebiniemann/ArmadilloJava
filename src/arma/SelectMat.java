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
 * Used for shallow copies of selections of {@link Mat}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
class SelectMat extends AbstractMat {

  /**
   * The current row iterator position
   */
  private int _i;

  /**
   * The current column iterator position
   */
  private int _j;

  /**
   * The element positions
   */
  AbstractMat _elementSelection;

  /**
   * The row positions
   */
  AbstractMat _rowSelection;

  /**
   * The column positions
   */
  AbstractMat _columnSelection;

  /**
   * Creates a shallow copy of a matrix and restrict its access to the elements specified in the selection.
   * 
   * @param matrix The matrix
   * @param selection The element positions
   */
  protected SelectMat(AbstractMat matrix, AbstractMat selection) {
    selection.isNonVectorDetection();
    matrix.isInvalidElementSelectionDetection(selection);

    n_rows = selection.n_elem;
    n_cols = 1;
    n_elem = n_rows * n_cols;

    _underlyingMatrix = matrix;
    _matrix = _underlyingMatrix._matrix;

    _elementSelection = selection;
    _columnSelection = null;
    _rowSelection = null;
  }

  /**
   * Creates a shallow copy of a matrix and restrict its access to the elements specified in the selection.
   * 
   * @param matrix The matrix
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * 
   * @throws IllegalArgumentException At least one selection must not be null.
   */
  protected SelectMat(AbstractMat matrix, AbstractMat rowSelection, AbstractMat columnSelection) throws IllegalArgumentException {
    if (rowSelection == null && columnSelection == null) {
      throw new IllegalArgumentException("At least one selection must not be null.");
    } else if (rowSelection == null) {
      columnSelection.isNonVectorDetection();
      matrix.isInvalidElementSelectionDetection(columnSelection);

      n_rows = matrix.n_rows;
      n_cols = columnSelection.n_elem;
    } else if (columnSelection == null) {
      rowSelection.isNonVectorDetection();
      matrix.isInvalidElementSelectionDetection(rowSelection);

      n_rows = rowSelection.n_elem;
      n_cols = matrix.n_cols;
    } else {
      rowSelection.isNonVectorDetection();
      columnSelection.isNonVectorDetection();
      matrix.isInvalidElementSelectionDetection(rowSelection);
      matrix.isInvalidElementSelectionDetection(columnSelection);

      n_rows = rowSelection.n_elem;
      n_cols = columnSelection.n_elem;
    }

    n_elem = n_rows * n_cols;

    _underlyingMatrix = matrix;
    _matrix = _underlyingMatrix._matrix;

    _elementSelection = null;
    _rowSelection = rowSelection;
    _columnSelection = columnSelection;
  }

  @Override
  protected void iteratorReset() {
    _i = 0;
    _j = 0;
  }

  @Override
  protected int iteratorNext() {
    super.iteratorNext();
    return _underlyingMatrix.getElementIndex(getElementIndex(_i++, _j++));
  }

  @Override
  protected int getElementIndex(int i, int j) {
    int n;
    if (_elementSelection != null) {
      n = (int) _elementSelection._matrix[_elementSelection.getElementIndex(i)];
    } else if (_rowSelection == null) {
      n = i + ((int) _columnSelection._matrix[_columnSelection.getElementIndex(j)]) * n_rows;
    } else if (_columnSelection == null) {
      n = ((int) _rowSelection._matrix[_rowSelection.getElementIndex(i)]) + j * n_rows;
    } else {
      n = ((int) _rowSelection._matrix[_rowSelection.getElementIndex(i)]) + ((int) _columnSelection._matrix[_columnSelection.getElementIndex(j)]) * n_rows;
    }

    return _underlyingMatrix.getElementIndex(n);
  }

  @Override
  protected int getElementIndex(int n) {
    int nn;
    if (_elementSelection != null) {
      nn = (int) _elementSelection._matrix[_elementSelection.getElementIndex(n)];
    } else {
      int j = n / n_rows;
      int i = n - j * n_rows;

      if (_rowSelection == null) {
        nn = i + ((int) _columnSelection._matrix[_columnSelection.getElementIndex(j)]) * n_rows;
      } else if (_columnSelection == null) {
        nn = ((int) _rowSelection._matrix[_rowSelection.getElementIndex(i)]) + j * n_rows;
      } else {
        nn = ((int) _rowSelection._matrix[_rowSelection.getElementIndex(i)]) + ((int) _columnSelection._matrix[_columnSelection.getElementIndex(j)]) * n_rows;
      }
    }

    return _underlyingMatrix.getElementIndex(nn);
  }
}
