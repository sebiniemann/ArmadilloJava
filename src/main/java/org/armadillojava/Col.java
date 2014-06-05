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
 * Provides a real-valued dense column vector with interfaces similar to the Armadillo C++ Algebra Library (Armadillo)
 * by Conrad Sanderson et al..
 * <p>
 * If not stated otherwise (marked as non-canonical), the provided interfaces are identical to Armadillo (e.g. same
 * ordering of arguments, accepted values, ...). However, numeric results may slightly differ from the Armadillo C++
 * Algebra Library.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 */
public class Col extends AbstractVector {

  /**
   * Creates an empty column vector.
   */
  public Col() {
    set_size(0);
  }

  /**
   * Creates an uninitialised column vector with the specified number of elements.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public Col(final int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */
    set_size(n_elem);
  }

  /**
   * Creates a column vector with the specified number of elements that is filled according to {@code fillType}.
   * 
   * @param n_elem The number of elements
   * @param fill_type The fill type
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   * @throws RuntimeException The fill type ({@code fill_type}) is not supported for column vectors.
   * 
   * @see Fill
   */
  public Col(final int n_elem, final Fill fill_type) throws NegativeArraySizeException, RuntimeException {
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
        throw new RuntimeException("The fill type (" + fill_type + ") is not supported for column vectors.");
    }
  }

  /**
   * Creates a deep copy of a vector.
   * 
   * @param vec The vector
   */
  public Col(final Col vec) {
    copy_size(vec);
    System.arraycopy(vec._data, 0, _data, 0, vec.n_elem);
  }

  /**
   * Creates a deep copy of a matrix with exactly one column.
   * 
   * @param mat The matrix
   * 
   * @throws RuntimeException The provided ({@code mat.n_rows}, {@code mat.n_cols})-matrix must be equivalent in shape
   *           to a column vector.
   */
  public Col(final AbstractMat mat) throws RuntimeException {
    if (!mat.is_colvec()) {
      throw new RuntimeException("The provided (" + mat.n_rows + ", " + mat.n_cols + ")-matrix must be equivalent in shape to a column vector.");
    }

    copy_size(mat);
    System.arraycopy(mat._data, 0, _data, 0, mat.n_elem);
  }

  /**
   * Creates a column vector with the same number of elements and values as the provided array.
   * 
   * @param array The array
   */
  public Col(final double[] array) {
    set_size(array.length);
    System.arraycopy(array, 0, _data, 0, array.length);
  }

  /**
   * Creates a deep copy of a matrix sub view.
   * 
   * @param view The sub view
   */
  protected Col(final AbstractView view) {
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
    if (!vec.is_colvec()) {
      throw new RuntimeException("Column vectors can only copy the size of matrices equivalent in shape to a column vector.");
    }

    set_size(vec.n_elem);
  }

  /**
   * Returns a deep copy of the {@code row_number}th row.
   * 
   * @param row_number The column
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   */
  public Row row(final int row_number) throws IndexOutOfBoundsException {
    if (row_number < 0 || row_number > n_rows - 1) {
      throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
    }

    return new Row(new ViewSubRow(this, row_number));
  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row with the specified right-hand side operand.
   * 
   * @param row_number The row
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void row(final int row_number, final Op binary_operator, final double operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (row_number < 0 || row_number > n_rows - 1) {
      throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
    }

    new ViewSubRow(this, row_number).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row with the specified right-hand side operand.
   * 
   * @param row_number The row
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   * @throws RuntimeException The provided ({@code operand.n_rows}, {@code operand.n_cols})-matrix must be equally in
   *           shape to a (1, {@code n_cols})-matrix.
   */
  public void row(final int row_number, final Op binary_operator, final AbstractMat operand) throws IndexOutOfBoundsException, UnsupportedOperationException, RuntimeException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (row_number < 0 || row_number > n_rows - 1) {
      throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
    }

    if (!operand.is_rowvec() || operand.n_elem != n_cols) {
      throw new RuntimeException("The provided (" + operand.n_rows + ", " + operand.n_cols + ")-matrix must be equally in shape to a (1, " + n_cols + ")-matrix.");
    }

    new ViewSubRow(this, row_number).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code first_row}th to {@code last_row} row.
   * 
   * @param first_row The first row
   * @param last_row The last row
   * 
   * @throws RuntimeException The first specified row ({@code first_row}) must be less than or equal the last specified
   *           row ({@code last_row}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_row}) is out of bounds.
   */
  public Mat rows(final int first_row, final int last_row) throws RuntimeException, IndexOutOfBoundsException {
    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    return new Mat(new ViewSubRows(this, first_row, last_row - first_row + 1));
  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row with the specified
   * right-hand side operand.
   * 
   * @param first_row The first row
   * @param last_row The last row
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified row ({@code first_row}) must be less than or equal the last specified
   *           row ({@code last_row}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_row}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void rows(final int first_row, final int last_row, final Op binary_operator, final double operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    new ViewSubRows(this, first_row, last_row - first_row + 1).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row with the specified
   * right-hand side operand.
   * 
   * @param first_row The first row
   * @param last_row The last row
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified row ({@code first_row}) must be less than or equal the last specified
   *           row ({@code last_row}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_row}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   * @throws RuntimeException The provided ({@code operand.n_rows}, {@code operand.n_cols})-matrix must be equally in
   *           shape to a ({@code last_row} - {@code first_row} + 1, {@code n_cols})-matrix.
   */
  public void rows(final int first_row, final int last_row, final Op binary_operator, final AbstractMat operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException, RuntimeException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    if (operand.n_rows != last_row - first_row + 1 || operand.n_cols != n_cols) {
      throw new RuntimeException("The provided (" + operand.n_rows + ", " + operand.n_cols + ")-matrix must be equally in shape to a (" + (n_rows - first_row + 1) + ", " + n_cols + ")-matrix.");
    }

    new ViewSubRows(this, first_row, last_row - first_row + 1).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified rows.
   * <p>
   * <b>Note:</b> No explicit bounds checking handling. However, the JVM should throw IndexOutOfBoundsException
   * exceptions upon errors.
   * 
   * @param vector_of_row_indices The rows
   */
  public Col rows(final AbstractVector vector_of_row_indices) {
    return new Col(new ViewElemVec(this, vector_of_row_indices._data));
  }

  /**
   * Performs an in-place binary operation on the specified rows with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit bounds checking handling. However, the JVM should throw IndexOutOfBoundsException
   * exceptions upon errors.
   * 
   * @param vector_of_row_indices The rows
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void rows(final AbstractVector vector_of_row_indices, final Op binary_operator, final double operand) throws UnsupportedOperationException {
    new ViewElemVec(this, vector_of_row_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified rows with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit bounds checking handling. However, the JVM should throw IndexOutOfBoundsException
   * exceptions upon errors.
   * 
   * @param vector_of_row_indices The rows
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   * @throws RuntimeException The provided ({@code operand.n_rows}, {@code operand.n_cols})-matrix must be equally in
   *           shape to a ({@code vector_of_row_indices.n_elem}, {@code n_cols})-matrix.
   */
  public void rows(final AbstractVector vector_of_row_indices, final Op binary_operator, final AbstractMat operand) throws UnsupportedOperationException, RuntimeException {
    if (operand.n_rows != vector_of_row_indices.n_elem || operand.n_cols != n_cols) {
      throw new RuntimeException("The provided (" + operand.n_rows + ", " + operand.n_cols + ")-matrix must be equally in shape to a (" + vector_of_row_indices.n_elem + ", " + n_rows + ")-matrix.");
    }
    
    new ViewElemVec(this, vector_of_row_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Inserts the rows from {@code X} between row {@code row_number} - 1 and {@code row_number}.
   * 
   * @param row_number The row
   * @param X The column vector
   * 
   * @throws IndexOutOfBoundsException The row ({@code row_number}) is out of bounds.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape to a
   *           column vector.
   */
  public void insert_rows(final int row_number, final AbstractMat X) throws IndexOutOfBoundsException, RuntimeException {
    if (row_number < 0 || row_number > n_elem) {
      throw new IndexOutOfBoundsException("The row (" + row_number + ") is out of bounds.");
    }

    if (!X.is_colvec()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    if (X.is_empty()) {
      return; // Nothing to do here.
    } else if (is_empty()) {
      copy_size(X);
      System.arraycopy(X._data, 0, _data, 0, X.n_elem);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_elem + X.n_elem);

      System.arraycopy(temp, 0, _data, 0, row_number);
      System.arraycopy(X._data, row_number, _data, 0, X.n_elem);
      System.arraycopy(temp, row_number, _data, row_number + X.n_elem, temp.length - row_number);
    }
  }

  /**
   * Inserts {@code number_of_rows} uninitialised rows between row {@code row_number} - 1 and {@code row_number}.
   * 
   * @param row_number The row
   * @param number_of_rows The number of rows
   * 
   * @throws IndexOutOfBoundsException The row ({@code row_number}) is out of bounds.
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void insert_rows(final int row_number, final int number_of_rows) throws IndexOutOfBoundsException, NegativeArraySizeException {
    /*
     * The parameter "number_of_rows" is validated within set_size(int).
     */

    if (row_number < 0 || row_number > n_elem) {
      throw new IndexOutOfBoundsException("The row (" + row_number + ") is out of bounds.");
    }

    if (number_of_rows == 0) {
      return; // Nothing to do here.
    } else if (is_empty()) {
      set_size(number_of_rows);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_elem + number_of_rows);

      System.arraycopy(temp, 0, _data, 0, row_number);
      System.arraycopy(temp, row_number, _data, row_number + number_of_rows, temp.length - row_number);
    }
  }

  /**
   * Inserts {@code number_of_rows} rows between row {@code row_number} - 1 and {@code row_number}.
   * <p>
   * All elements will be set to 0 ({@code set_to_zero} = true) or left uninitialised.
   * 
   * @param row_number The row
   * @param number_of_rows The number of rows
   * @param set_to_zero Whether the inserted elements are to be set to 0
   * 
   * @throws IndexOutOfBoundsException The row ({@code row_number}) is out of bounds.
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void insert_rows(final int row_number, final int number_of_rows, final boolean set_to_zero) throws IndexOutOfBoundsException, NegativeArraySizeException {
    /*
     * The parameter "number_of_rows" is validated within set_size(int).
     */

    /*
     * All entries of an array are already set to 0 during creation.
     * Therefore, set_to_zero will be ignored.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    insert_rows(row_number, number_of_rows);
  }

  @Override
  public Col plus(final double X) {
    Col result = new Col(n_elem);
    plus(result._data, _data, X);
    return result;
  }

  @Override
  public Col plus(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    plus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col plus(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    plus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col plus(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    plus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col minus(final double X) {
    Col result = new Col(n_elem);
    minus(result._data, _data, X);
    return result;
  }

  @Override
  public Col minus(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    minus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col minus(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    minus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col minus(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    minus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col divide(final double X) {
    Col result = new Col(n_elem);
    divide(result._data, _data, X);
    return result;
  }

  @Override
  public Col elemDivide(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    elemDivide(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col elemDivide(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    elemDivide(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col elemDivide(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    elemDivide(result._data, _data, X._data);
    return result;
  }

  @Override
  protected AbstractMat times(final AbstractMat X) {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }
    
    if (X.n_elem == 1) {
      return times(X._data[0]);
    } else if (X.is_colvec()) {
      /*
       * Only (1, 1)-column vectors can be right-hand side multiplied to column vectors.
       */
      return times(X._data[0]);
    } else if (X.is_rowvec()) {
      Mat result = new Mat(n_rows, X.n_cols);
      BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
      return result;
    } else {
      /*
       * Only (1, m)-matrices can be right-hand side multiplied to column vectors.
       */
      Mat result = new Mat(n_rows, X.n_cols);
      BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
      return result;
    }
  }

  @Override
  public Col times(final double X) {
    Col result = new Col(n_elem);
    times(result._data, _data, X);
    return result;
  }

  /**
   * Return the out-of-place matrix multiplication with the provided right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The number of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  public Mat times(final Row X) {
    Mat result = new Mat(n_rows, X.n_cols);
    BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
    return result;
  }

  /**
   * Return the out-of-place matrix multiplication with the provided right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The number of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  public Mat times(final Mat X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    Mat result = new Mat(n_rows, X.n_cols);
    BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
    return result;
  }

  @Override
  public Col elemTimes(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    elemTimes(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col elemTimes(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    elemTimes(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col elemTimes(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    elemTimes(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col equals(final double X) {
    Col result = new Col(n_elem);
    equals(result._data, _data, X);
    return result;
  }

  @Override
  public Col equals(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    equals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col equals(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    equals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col equals(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    equals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col nonEquals(final double X) {
    Col result = new Col(n_elem);
    nonEquals(result._data, _data, X);
    return result;
  }

  @Override
  public Col nonEquals(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    nonEquals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col nonEquals(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    nonEquals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col nonEquals(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    nonEquals(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col greaterThan(final double X) {
    Col result = new Col(n_elem);
    greaterThan(result._data, _data, X);
    return result;
  }

  @Override
  public Col greaterThan(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    greaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col greaterThan(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    greaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col greaterThan(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    greaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col lessThan(final double X) {
    Col result = new Col(n_elem);
    lessThan(result._data, _data, X);
    return result;
  }

  @Override
  public Col lessThan(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    lessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col lessThan(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    lessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col lessThan(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    lessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col strictGreaterThan(final double X) {
    Col result = new Col(n_elem);
    strictGreaterThan(result._data, _data, X);
    return result;
  }

  @Override
  public Col strictGreaterThan(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    strictGreaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col strictGreaterThan(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    strictGreaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col strictGreaterThan(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    strictGreaterThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col strictLessThan(final double X) {
    Col result = new Col(n_elem);
    strictLessThan(result._data, _data, X);
    return result;
  }

  @Override
  public Col strictLessThan(final Mat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    strictLessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col strictLessThan(final Col X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    strictLessThan(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col strictLessThan(final Row X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
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
        this.n_rows = n_elem;
        this.n_cols = 1;
      } else {
        this.n_rows = 0;
        this.n_cols = 0;
      }

      this.n_elem = this.n_rows;
      _data = new double[this.n_elem];
    }
  }

  /**
   * Removes the {@code row_number}th row.
   * 
   * @param row_number The row
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bound.
   */
  public void shed_row(final int row_number) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bound.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - 1);

    System.arraycopy(temp, 0, _data, 0, row_number);
    System.arraycopy(temp, row_number + 1, _data, row_number, n_elem - row_number);
  }

  /**
   * Removes all rows from the {@code first_row} to the {@code last_row} (inclusive).
   * 
   * @param first_row The first row
   * @param last_row The last row
   * 
   * @throws RuntimeException The first specified row ({@code first_row}) must be less than or equal the last specified
   *           row ({@code last_row}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_row}) is out of bound.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_row}) is out of bound.
   */
  public void shed_rows(final int first_row, final int last_row) throws RuntimeException, IndexOutOfBoundsException {
    if (first_row > last_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ") .");
    }

    if (!in_range(first_row)) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bound.");
    }

    if (!in_range(last_row)) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bound.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - (last_row - first_row + 1));

    System.arraycopy(temp, 0, _data, 0, first_row);
    System.arraycopy(temp, last_row + 1, _data, first_row, n_elem - first_row);
  }

  @Override
  public Col subvec(final int first_index, final int last_index) throws RuntimeException, IndexOutOfBoundsException {
    if (last_index < first_index) {
      throw new RuntimeException("The first specified row (" + first_index + ") must be less than or equal the last specified row (" + last_index + ").");
    }

    if (first_index < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_index + ") is out of bounds.");
    }

    if (last_index > n_elem - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_index + ") is out of bounds.");
    }

    Col col = new Col(last_index - first_index + 1);
    System.arraycopy(_data, first_index, col._data, 0, col.n_elem);
    return col;
  }

  @Override
  public Col subvec(final Span span) throws IndexOutOfBoundsException {
    /*
     * The parameter "span" is already validated during its instantiation and further validated in the context of this
     * vector in subvec(int, int).
     */
    return subvec(span._first, span._last);
  }

  @Override
  public void swap(final Mat X) throws RuntimeException {
    if (!X.is_colvec()) {
      throw new RuntimeException("The content of column vectors can only be swapped with matrices that are equivalent in shape to a column vector.");
    }

    Col temp = new Col(_data);

    copy_size(X);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.copy_size(temp);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(final Row X) throws RuntimeException {
    if (!X.is_colvec()) {
      throw new RuntimeException("The content of column vectors can only be swapped with matrices that are equivalent in shape to a column vector.");
    }
    
    if (!is_rowvec()) {
      throw new RuntimeException("The content of row vectors can only be swapped with matrices that are equivalent in shape to a row vector.");
    }
    
    Col temp = new Col(_data);

    copy_size(X);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.copy_size(temp);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(final Col X) {
    Col temp = new Col(_data);

    copy_size(X);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.copy_size(temp);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public Row t() {
    return new Row(_data);
  }

}
