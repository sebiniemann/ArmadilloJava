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

import java.util.Arrays;

import com.github.fommil.netlib.BLAS;

/**
 * Provides a real-valued dense row vector with interfaces similar to the Armadillo C++ Algebra Library (Armadillo) by
 * Conrad Sanderson et al..
 * <p>
 * If not stated otherwise (marked as non-canonical), the provided interfaces are identical to Armadillo (e.g. same
 * ordering of arguments, accepted values, ...). However, numeric results may slightly differ from the Armadillo C++
 * Algebra Library.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 */
public class Row extends AbstractVector {

  /**
   * Creates an empty row vector.
   */
  public Row() {
    set_size(0);
  }

  /**
   * Creates an uninitialised row vector with the specified number of elements.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public Row(final int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */
    set_size(n_elem);
  }

  /**
   * Creates a row vector with the specified number of elements that is filled according to {@code fillType}.
   * 
   * @param n_elem The number of elements
   * @param fill_type The fill type
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   * @throws RuntimeException The fill type ({@code fill_type}) is not supported for row vectors.
   * 
   * @see Fill
   */
  public Row(final int n_elem, final Fill fill_type) throws NegativeArraySizeException, RuntimeException {
    /*
     * The parameter "n_elem" is validated within zeros(int), ones(int), randu(int) and randn(int).
     */

    switch (fill_type) {
      case NONE:
      case ZEROS:
        zeros(n_elem);
        break;
      case ONES:
        ones(n_elem);
        break;
      case RANDU:
        randu(n_elem);
        break;
      case RANDN:
        randn(n_elem);
      default:
        throw new RuntimeException("The fill type (" + fill_type + ") is not supported for row vectors.");
    }
  }

  /**
   * Creates a deep copy of a row vector.
   * 
   * @param col The column vector
   */
  public Row(final Row vec) {
    copy_size(vec);
    System.arraycopy(vec._data, 0, _data, 0, vec.n_elem);
  }

  /**
   * Creates a deep copy of a matrix with exactly one row.
   * 
   * @param mat The matrix
   * 
   * @throws RuntimeException The provided ({@code mat.n_rows}, {@code mat.n_cols})-matrix must be equivalent in shape
   *           to a row vector.
   */
  public Row(final AbstractMat mat) throws RuntimeException {
    if (!mat.is_rowvec()) {
      throw new RuntimeException("The provided (" + mat.n_rows + ", " + mat.n_cols + ")-matrix must be equivalent in shape to a row vector.");
    }

    copy_size(mat);
    System.arraycopy(mat._data, 0, _data, 0, mat.n_elem);
  }

  /**
   * Creates a row vector with the same number of elements and values as the provided array.
   * 
   * @param array The array
   */
  public Row(final double[] array) {
    set_size(array.length);
    System.arraycopy(array, 0, _data, 0, array.length);
  }

  /**
   * Creates a deep copy of a matrix sub view.
   * 
   * @param view The sub view
   */
  protected Row(final AbstractView view) {
    copy_size(view);

    view.iteratorReset();
    for (int n = 0; n < n_elem; n++) {
      _data[n] = view._data[view.iteratorNext()];
    }
  }

  protected void copy_size(final AbstractView vec) {
    set_size(vec.n_elem);
  }

  @Override
  public void copy_size(final AbstractMat vec) throws RuntimeException {
    if (!vec.is_rowvec()) {
      throw new RuntimeException("Row vectors can only copy the size of matrices equivalent in shape to a row vector.");
    }

    set_size(vec.n_elem);
  }

  /**
   * Returns a deep copy of the {@code col_number}th column.
   * 
   * @param col_number The column
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   */
  public Col col(final int col_number) throws IndexOutOfBoundsException {
    if (col_number < 0 || col_number > n_cols - 1) {
      throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
    }

    int n = col_number * n_rows;
    return new Col(Arrays.copyOfRange(_data, n, n + n_rows));
  }

  /**
   * Performs an in-place binary operation on the {@code col_number}th column with the specified right-hand side
   * operand.
   * 
   * @param col_number The column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void col(final int col_number, final Op binary_operator, final double operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (col_number < 0 || col_number > n_cols - 1) {
      throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
    }

    new ViewSubCol(this, col_number).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code col_number}th column with the specified right-hand side
   * operand.
   * 
   * @param col_number The column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   * @throws RuntimeException The provided ({@code operand.n_rows}, {@code operand.n_cols})-matrix must be equally in
   *           shape to a ({@code n_rows}, 1)-matrix.
   */
  public void col(final int col_number, final Op binary_operator, final AbstractMat operand) throws IndexOutOfBoundsException, UnsupportedOperationException, RuntimeException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (col_number < 0 || col_number > n_cols - 1) {
      throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
    }

    if (!operand.is_colvec() || operand.n_elem != n_rows) {
      throw new RuntimeException("The provided (" + operand.n_rows + ", " + operand.n_cols + ")-matrix must be equally in shape to a (" + n_rows + ", 1)-matrix.");
    }

    new ViewSubCol(this, col_number).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code first_col}th to {@code last_col} column.
   * 
   * @param first_col The first column
   * @param last_col The last column
   * 
   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
   *           specified column ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_row}) is out of bounds.
   */
  public Mat cols(final int first_col, final int last_col) throws RuntimeException, IndexOutOfBoundsException {
    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_col + ") is out of bounds.");
    }

    return new Mat(new ViewSubCols(this, first_col, last_col - first_col + 1));
  }

  /**
   * Performs an in-place binary operation on the {@code first_col}th to {@code last_col} column with the specified
   * right-hand side operand.
   * 
   * @param first_col The first column
   * @param last_col The last column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
   *           row ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_row}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void cols(final int first_col, final int last_col, final Op binary_operator, final double operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_col + ") is out of bounds.");
    }

    new ViewSubCols(this, first_col, last_col - first_col + 1).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code first_col}th to {@code last_col} column with the specified
   * right-hand side operand.
   * 
   * @param first_col The first column
   * @param last_col The last column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
   *           row ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_row}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   * @throws RuntimeException The provided ({@code operand.n_rows}, {@code operand.n_cols})-matrix must be equally in
   *           shape to a ({@code last_row} - {@code first_row} + 1, {@code n_cols})-matrix.
   */
  public void cols(final int last_col, final int first_col, final Op binary_operator, final AbstractMat operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException, RuntimeException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_col + ") is out of bounds.");
    }

    if (operand.n_rows != last_col - first_col + 1 || operand.n_cols != n_cols) {
      throw new RuntimeException("The provided (" + operand.n_rows + ", " + operand.n_cols + ")-matrix must be equally in shape to a (" + (n_rows - first_col + 1) + ", " + n_cols + ")-matrix.");
    }

    new ViewSubCols(this, first_col, last_col - first_col + 1).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified columns.
   * <p>
   * <b>Note:</b> No explicit bounds checking handling. However, the JVM should throw IndexOutOfBoundsException
   * exceptions upon errors.
   * 
   * @param vector_of_column_indices The columns
   */
  public Row cols(final AbstractVector vector_of_column_indices) {
    return new Row(new ViewElemMat(this, vector_of_column_indices._data));
  }

  /**
   * Performs an in-place binary operation on the specified columns with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit bounds checking handling. However, the JVM should throw IndexOutOfBoundsException
   * exceptions upon errors.
   * 
   * @param vector_of_column_indices The columns
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void cols(final AbstractVector vector_of_column_indices, final Op binary_operator, final double operand) throws UnsupportedOperationException {
    new ViewElemMat(this, vector_of_column_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified columns with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit bounds checking handling. However, the JVM should throw IndexOutOfBoundsException
   * exceptions upon errors.
   * 
   * @param vector_of_column_indices The columns
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   * @throws RuntimeException The provided ({@code operand.n_rows}, {@code operand.n_cols})-matrix must be equally in
   *           shape to a ({@code n_rows}, {@code vector_of_column_indices.n_elem})-matrix.
   */
  public void cols(final AbstractVector vector_of_column_indices, final Op binary_operator, final AbstractMat operand) throws UnsupportedOperationException, RuntimeException {
    if (operand.n_rows != n_rows || operand.n_cols != vector_of_column_indices.n_elem) {
      throw new RuntimeException("The provided (" + operand.n_rows + ", " + operand.n_cols + ")-matrix must be equally in shape to a (" + n_rows + ", " + vector_of_column_indices.n_elem + ")-matrix.");
    }
    
    new ViewElemMat(this, vector_of_column_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Inserts the columns from {@code X} between column {@code col_number} - 1 and {@code col_number}.
   * 
   * @param col_number The column
   * @param X The column vector
   * 
   * @throws IndexOutOfBoundsException The row ({@code row_number}) is out of bounds.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape to a
   *           row vector.
   */
  public void insert_cols(final int col_number, final Row X) throws IndexOutOfBoundsException, RuntimeException {
    if (col_number < 0 || col_number > n_elem) {
      throw new IndexOutOfBoundsException("The column (" + col_number + ") is out of bounds.");
    }

    if (!X.is_rowvec()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must be equivalent in shape to a row vector.");
    }

    if (X.is_empty()) {
      return; // Nothing to do here.
    } else if (is_empty()) {
      copy_size(X);
      System.arraycopy(X._data, 0, _data, 0, X.n_elem);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_elem + X.n_elem);

      System.arraycopy(temp, 0, _data, 0, col_number);
      System.arraycopy(X._data, col_number, _data, 0, X.n_elem);
      System.arraycopy(temp, X.n_elem + col_number, _data, 0, n_elem - col_number);
    }
  }

  /**
   * Inserts {@code number_of_cols} uninitialised columns between column {@code col_number} - 1 and {@code col_number}.
   * 
   * @param col_number The column
   * @param number_of_cols The number of columns
   * 
   * @throws IndexOutOfBoundsException The row ({@code row_number}) is out of bounds.
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void insert_cols(final int col_number, final int number_of_cols) throws IndexOutOfBoundsException, NegativeArraySizeException {
    /*
     * The parameter "number_of_cols" is validated within set_size(int).
     */

    if (col_number < 0 || col_number > n_elem) {
      throw new IndexOutOfBoundsException("The column (" + col_number + ") is out of bounds.");
    }

    if (number_of_cols == 0) {
      return; // Nothing to do here.
    } else if (is_empty()) {
      set_size(number_of_cols);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_elem + number_of_cols);

      System.arraycopy(temp, 0, _data, 0, col_number);
      System.arraycopy(temp, number_of_cols + col_number, _data, 0, n_elem - col_number);
    }
  }

  /**
   * Inserts {@code number_of_cols} columns between column {@code col_number} - 1 and {@code col_number}.
   * <p>
   * All elements will be set to 0 ({@code set_to_zero} = true) or left uninitialised.
   * 
   * @param col_number The column
   * @param number_of_cols The number of columns
   * @param set_to_zero Whether the inserted elements are to be set to 0
   * 
   * @throws IndexOutOfBoundsException The row ({@code row_number}) is out of bounds.
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void insert_cols(final int col_number, final int number_of_cols, final boolean set_to_zero) throws IndexOutOfBoundsException, NegativeArraySizeException {
    /*
     * The parameter "number_of_cols" is validated within set_size(int).
     */

    /*
     * All entries of an array are already set to 0 during creation.
     * Therefore, set_to_zero will be ignored.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    insert_cols(col_number, number_of_cols);
  }

  @Override
  public Row plus(final double X) {
    Row result = new Row(n_elem);
    plus(result._data, _data, X);
    return result;
  }

  @Override
  public Row plus(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    plus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row plus(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    plus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row plus(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    plus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row minus(final double X) {
    Row result = new Row(n_elem);
    minus(result._data, _data, X);
    return result;
  }

  @Override
  public Row minus(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    minus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row minus(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    minus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row minus(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    minus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row divide(final double X) {
    Row result = new Row(n_elem);
    divide(result._data, _data, X);
    return result;
  }

  @Override
  public Row elemDivide(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    elemDivide(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row elemDivide(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    elemDivide(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row elemDivide(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    elemDivide(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row times(final double X) {
    Row result = new Row(n_elem);
    times(result._data, _data, X);
    return result;
  }

  @Override
  protected AbstractMat times(final AbstractMat X) {
    if (X.n_elem == 1) {
      return times(X._data[0]);
    } else if (X.is_colvec()) {
      if (n_cols != X.n_rows) {
        throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
      }

      /*
       * Only (1, 1)-row vectors can be left-hand side multiplied to row vectors.
       */
      return times(X._data[0]);
    } else if (X.is_rowvec()) {
      if (n_cols != X.n_rows) {
        throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
      }

      /*
       * Only (n, 1)-matrices can be right-hand side multiplied to row vectors.
       */
      return new Mat(new double[]{BLAS.getInstance().ddot(n_elem, _data, 1, X._data, 1)});
    } else {
      if (n_cols != X.n_rows) {
        throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
      }

      return new Mat(new double[]{BLAS.getInstance().ddot(n_elem, _data, 1, X._data, 1)});
    }
  }


  /**
   * Return the out-of-place matrix multiplication with the provided right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The number of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  public Mat times(final Mat X) {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    Mat result = new Mat(n_rows, X.n_cols);
    BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
    return result;
  }

  @Override
  public Row elemTimes(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    elemTimes(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row elemTimes(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    elemTimes(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row elemTimes(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    elemTimes(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row equals(final double X) {
    Row result = new Row(n_elem);
    equals(result._data, _data, X);
    return result;
  }

  @Override
  public Row equals(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    equals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row equals(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    equals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row equals(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    equals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row nonEquals(final double X) {
    Row result = new Row(n_elem);
    nonEquals(result._data, _data, X);
    return result;
  }

  @Override
  public Row nonEquals(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    nonEquals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row nonEquals(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    nonEquals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row nonEquals(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    nonEquals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row greaterThan(final double X) {
    Row result = new Row(n_elem);
    greaterThan(result._data, _data, X);
    return result;
  }

  @Override
  public Row greaterThan(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    greaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row greaterThan(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    greaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row greaterThan(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    greaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row lessThan(final double X) {
    Row result = new Row(n_elem);
    lessThan(result._data, _data, X);
    return result;
  }

  @Override
  public Row lessThan(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    lessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row lessThan(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    lessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row lessThan(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    lessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row strictGreaterThan(final double X) {
    Row result = new Row(n_elem);
    strictGreaterThan(result._data, _data, X);
    return result;
  }

  @Override
  public Row strictGreaterThan(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    strictGreaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row strictGreaterThan(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    strictGreaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row strictGreaterThan(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    strictGreaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row strictLessThan(final double X) {
    Row result = new Row(n_elem);
    strictLessThan(result._data, _data, X);
    return result;
  }

  @Override
  public Row strictLessThan(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    strictLessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row strictLessThan(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    strictLessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Row strictLessThan(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Row result = new Row(n_elem);
    strictLessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public void set_size(final int n_elem) throws NegativeArraySizeException {
    if (n_elem < 0) {
      throw new NegativeArraySizeException("The specified number of elements (" + n_elem + ") must be positive.");
    }

    if (n_elem != this.n_elem) {
      if (n_elem > 0) {
        this.n_rows = 1;
        this.n_cols = n_elem;
      } else {
        this.n_rows = 0;
        this.n_cols = 0;
      }

      this.n_elem = this.n_cols;
      _data = new double[this.n_elem];
    }
  }

  /**
   * Removes the {@code col_number}th column.
   * 
   * @param col_number The column
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code row_number}) is out of bound.
   */
  public void shed_col(final int col_number) throws IndexOutOfBoundsException {
    if (!in_range(col_number)) {
      throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bound.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - 1);

    System.arraycopy(temp, 0, _data, 0, col_number);
    System.arraycopy(temp, col_number + 1, _data, col_number, n_elem - col_number);
  }

  /**
   * Removes all columns from the {@code first_col} to the {@code last_col} (inclusive).
   * 
   * @param first_col The first column
   * @param last_col The last column
   * 
   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
   *           specified column ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bound.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bound.
   */
  public void shed_cols(final int first_col, int last_col) throws RuntimeException, IndexOutOfBoundsException {
    if (first_col > last_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ") .");
    }

    if (!in_range(first_col)) {
      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bound.");
    }

    if (!in_range(last_col)) {
      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bound.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - (last_col - first_col + 1));

    System.arraycopy(temp, 0, _data, 0, first_col);
    System.arraycopy(temp, last_col + 1, _data, first_col, n_elem - first_col);
  }

  @Override
  public Row subvec(final int first_index, final int last_index) throws RuntimeException, IndexOutOfBoundsException {
    if (last_index < first_index) {
      throw new RuntimeException("The first specified column (" + first_index + ") must be less than or equal the last specified column (" + last_index + ").");
    }

    if (first_index < 0) {
      throw new IndexOutOfBoundsException("The first specified column (" + first_index + ") is out of bounds.");
    }

    if (last_index > n_cols - 1) {
      throw new IndexOutOfBoundsException("The last specified column (" + last_index + ") is out of bounds.");
    }

    Row row = new Row(last_index - first_index + 1);
    System.arraycopy(_data, first_index, row._data, 0, last_index - first_index + 1);
    return row;
  }

  @Override
  public Row subvec(final Span span) throws IndexOutOfBoundsException {
    /*
     * The parameter "span" is already validated during its instantiation and further validated in the context of this
     * vector in subvec(int, int).
     */
    return subvec(span._first, span._last);
  }

  @Override
  public void swap(final Mat X) throws RuntimeException {
    if (!X.is_rowvec()) {
      throw new RuntimeException("The content of row vectors can only be swapped with matrices that are equivalent in shape to a row vector.");
    }

    Row temp = new Row(_data);

    copy_size(X);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    copy_size(temp);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(final Col X) throws RuntimeException {
    if (!X.is_rowvec()) {
      throw new RuntimeException("The content of row vectors can only be swapped with matrices that are equivalent in shape to a row vector.");
    }

    Row temp = new Row(_data);

    copy_size(X);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    copy_size(temp);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(final Row X) {
    Row temp = new Row(_data);

    copy_size(X);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    copy_size(temp);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public Col t() {
    return new Col(_data);
  }

}
