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
class SelectionMat extends BaseMat {
  /**
   * The element positions
   */
  Mat _elementSelection;
  /**
   * The row positions
   */
  Mat _rowSelection;
  /**
   * The column positions
   */
  Mat _columnSelection;

  /**
   * Creates a shallow copy of a matrix and restrict its access to the elements specified in the selection.
   * 
   * @param matrix The matrix
   * @param selection The element positions
   */
  SelectionMat(Mat matrix, Mat selection) {
    selection.isNonVectorDetection();
    matrix.isInvalidElementSelectionDetection(selection);

    _matrix = matrix._matrix;

    n_rows = selection.n_elem;
    n_cols = 1;
    n_elem = n_rows * n_cols;

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
  SelectionMat(Mat matrix, Mat rowSelection, Mat columnSelection) throws IllegalArgumentException {
    _matrix = matrix._matrix;

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

    _elementSelection = null;
    _rowSelection = rowSelection;
    _columnSelection = columnSelection;
  }

  @Override
  protected int getElementPosition(int n) {
    if (_elementSelection != null) {
      return (int) _elementSelection._matrix[n];
    } else {
      int j = n / n_rows;
      int i = n - j * n_rows;

      if (_rowSelection == null) {
        return i + ((int) _rowSelection._matrix[j]) * n_rows;
      } else if (_columnSelection == null) {
        return ((int) _rowSelection._matrix[i]) + j * n_rows;
      } else {
        return ((int) _rowSelection._matrix[i]) + ((int) _rowSelection._matrix[j]) * n_rows;
      }
    }
  }
}
