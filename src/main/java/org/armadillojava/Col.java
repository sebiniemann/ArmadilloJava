package org.armadillojava;

import java.util.Arrays;

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
   * @param n_elem
   */
  public Col(int n_elem) {
    set_size(n_elem);
  }

  /**
   * Creates a column vector with the specified number of elements that is filled according to {@code fillType}.
   * 
   * @param n_elem The number of elements
   * @param fill_type The fill type
   * 
   * @throws RuntimeException The fill type ({@code fill_type}) is not supported for column vectors.
   * 
   * @see Fill
   */
  public Col(int n_elem, Fill fill_type) throws RuntimeException {
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
   * Creates a deep copy of a column vector.
   * 
   * @param col The column vector
   */
  public Col(AbstractMat vec) {
    copy_size(vec);
    System.arraycopy(vec._data, 0, _data, 0, vec.n_elem);
  }

  /**
   * Creates a deep copy of a matrix with exactly one column.
   * 
   * @param mat The matrix
   * 
   * @throws RuntimeException The provided matrix must have exactly one column.
   */
  public Col(Mat mat) throws RuntimeException {
    if (mat.n_cols > 1) {
      throw new RuntimeException("The provided matrix must have exactly one column.");
    }

    set_size(mat.n_elem);
    System.arraycopy(mat._data, 0, _data, 0, mat.n_elem);
  }

  /**
   * Creates a column vector with the same number of elements and values as the provided array.
   * 
   * @param array The array
   */
  public Col(double[] array) {
    set_size(array.length);
    System.arraycopy(array, 0, _data, 0, array.length);
  }

  protected Col(AbstractView view) {
    copy_size(view);

    view.iteratorReset();
    for (int n = 1; n < n_elem; n++) {
      _data[n] = view._data[view.iteratorNext()];
    }
  }

  /**
   * Sets the size to be the same as {@code A}.
   * 
   * @param vec The column vector
   * 
   * @throws RuntimeException The specified matrix must be empty or have exactly one column.
   */
  public void copy_size(AbstractMat vec) throws RuntimeException {
    if (vec.n_cols > 1) {
      throw new RuntimeException("The specified matrix must be empty or have excatly one column.");
    }

    set_size(vec.n_elem);
  }

  /**
   * Inserts the rows from {@code X} at row position {@code row_number}.
   * 
   * @param row_number The row position
   * @param X The column vector
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public void insert_rows(int row_number, Col X) throws IndexOutOfBoundsException {
    if (row_number < 0 || row_number > n_elem) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    if (X.is_empty()) {
      return; // Nothing to do here.
    }
    else if (is_empty()) {
      set_size(X.n_elem);
      System.arraycopy(X._data, 0, _data, 0, X.n_elem);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_elem + X.n_elem);

      System.arraycopy(temp, 0, _data, 0, row_number);
      System.arraycopy(X._data, row_number, _data, 0, X.n_elem);
      System.arraycopy(temp, X.n_elem + row_number, _data, 0, n_elem - row_number);
    }
  }

  /**
   * Inserts {@code number_of_rows} uninitialised rows at row position {@code row_number}.
   * 
   * @param row_number The row position
   * @param number_of_rows The number of rows
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public void insert_rows(int row_number, int number_of_rows) throws IndexOutOfBoundsException {
    if (row_number < 0 || row_number > n_elem) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    if (number_of_rows == 0) {
      return; // Nothing to do here.
    } else if (is_empty()) {
      set_size(number_of_rows);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_elem + number_of_rows);

      System.arraycopy(temp, 0, _data, 0, row_number);
      System.arraycopy(temp, number_of_rows + row_number, _data, 0, n_elem - row_number);
    }
  }

  /**
   * Inserts {@code number_of_rows} rows at row position {@code row_number}.
   * <p>
   * The will be set to 0 ({@code set_to_zero} = true) or left uninitialised.
   * 
   * @param row_number The row position
   * @param number_of_rows The number of rows
   * @param set_to_zero Whether the inserted elements are to be set to 0
   */
  public void insert_rows(int row_number, int number_of_rows, boolean set_to_zero) {
    /*
     * All entries of an array are already set to 0 during creation.
     * Therefore, set_to_zero will be ignored.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    insert_rows(row_number, number_of_rows);
  }

  /**
   * Returns the out-of-place addition with the specified right-hand side addend.
   * 
   * @param X The addend
   */
  public Col plus(double X) {
    Col result = new Col(_data);
    result.inPlacePlus(X);
    return result;
  }

  /**
   * Returns the out-of-place addition with the specified right-hand side addend.
   * 
   * @param X The addend
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col plus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(_data);
    result.inPlacePlus(X);
    return result;
  }

  /**
   * Returns the out-of-place subtraction with the specified right-hand side subtrahend.
   * 
   * @param X The subtrahend
   */
  public Col minus(double X) {
    Col result = new Col(_data);
    result.inPlaceMinus(X);
    return result;
  }

  /**
   * Returns the out-of-place subtraction with the specified right-hand side subtrahend.
   * 
   * @param X The subtrahend
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col minus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(_data);
    result.inPlaceMinus(X);
    return result;
  }

  /**
   * Returns the out-of-place element-wise division with the specified right-hand side divisor.
   * 
   * @param operand The divisor
   */
  public Col elemDivide(double X) {
    Col result = new Col(_data);
    result.inPlaceElemDivide(X);
    return result;
  }

  /**
   * Returns the out-of-place element-wise division with the specified right-hand side divisor.
   * 
   * @param operand The divisor
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col elemDivide(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(_data);
    result.inPlaceElemDivide(X);
    return result;
  }

  public Col times(double X) {
    return elemTimes(X);
  }

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The numbers of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  public Col times(Col X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (1, 1)-column vectors can be right-hand side multiplied to column vectors.
     */
    return elemTimes(X._data[0]);
  }

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   */
  public Mat times(Row X) {
    /*
     * Row vectors can always be right-hand side multiplied to column vectors.
     * 
     * Each element of the resulting matrix can be computed directly from one multiplication between two elements of the
     * provided vectors.
     * 
     * Since the resulting matrix is column-major ordered, the outer loops iterates over the rows, while the inner loop
     * iterates over the columns.
     */
    Mat result = new Mat(n_rows, X.n_cols);
    int n = 0;
    for (int i = 0; i < n_rows; i++) {
      double rowValue = X._data[i];
      for (int j = 0; j < n_rows; j++) {
        result._data[n++] = rowValue * _data[j];
      }
    }

    return result;
  }

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The numbers of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  public Mat times(Mat X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (1, m)-matrices can be right-hand side multiplied to column vectors. Therefore, it can be handled exactly
     * the same as the right-hand side multiplication of row vectors.
     * 
     * See times(Row X)
     */
    Mat result = new Mat(n_rows, X.n_cols);
    int n = 0;
    for (int i = 0; i < n_rows; i++) {
      double rowValue = X._data[i];
      for (int j = 0; j < n_rows; j++) {
        result._data[n++] = rowValue * _data[j];
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise multiplication with the specified right-hand side multiplier.
   * 
   * @param operand The multiplier
   */
  public Col elemTimes(double X) {
    Col result = new Col(_data);
    result.inPlaceElemTimes(X);
    return result;
  }

  /**
   * Returns the out-of-place element-wise multiplication with the specified right-hand side multiplier.
   * 
   * @param operand The multiplier
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col elemTimes(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(_data);
    result.inPlaceElemTimes(X);
    return result;
  }

  /**
   * Returns the out-of-place element-wise equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding values are equal and 0 otherwise.
   * 
   * @param operand The operand
   */
  public Col equal(double X) {
    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] == X) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding values are equal and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col equal(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] == X._data[n]) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise non-equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding values are non-equal and 0 otherwise.
   * 
   * @param operand The operand
   */
  public Col nonEqual(double X) {
    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] != X) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise non-equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding values are non-equal and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col nonEqual(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] != X._data[n]) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise <em>greater than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding left-hand side value is greater than
   * the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   */
  public Col greaterThan(double X) {
    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] >= X) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise <em>greater than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding left-hand side value is greater than
   * the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col greaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] >= X._data[n]) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise <em>less than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding left-hand side value is less than the
   * right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   */
  public Col lessThan(double X) {
    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] <= X) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise <em>less than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding left-hand side value is less than the
   * right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col lessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] <= X._data[n]) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise <em>strict greater than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding left-hand side value is strict
   * greater than the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   */
  public Col strictGreaterThan(double X) {
    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] > X) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise <em>strict greater than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding left-hand side value is strict
   * greater than the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col strictGreaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] > X._data[n]) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise <em>strict less than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding left-hand side value is strict less
   * than the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   */
  public Col strictLessThan(double X) {
    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] < X) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Returns the out-of-place element-wise <em>strict less than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned vector will be set to 1 at positions where the two corresponding left-hand side value is strict less
   * than the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  public Col strictLessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);

    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (_data[n] < X._data[n]) {
        result._data[n] = 1;
      }
    }

    return result;
  }

  /**
   * Resizes the vector to the specified number of elements.
   * <p>
   * If the requested size is equal to the current size, the existing memory is reused. Otherwise, new memory will be
   * allocated and left uninitialised.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public void set_size(int n_elem) throws NegativeArraySizeException {
    if (n_elem < 0) {
      throw new NegativeArraySizeException("The specified number of elements (" + n_elem + ") must be positive.");
    }

    if (n_elem != this.n_elem) {
      _data = new double[n_elem];
      this.n_rows = n_elem;
      this.n_cols = 1;
      this.n_elem = n_elem;
    }
  }

  /**
   * Removes the specified row.
   * 
   * @param row_number The row position
   * 
   * @throws RuntimeException The row position ({@code row_number}) is out of bound.
   */
  public void shed_row(int row_number) {
    if (!in_range(row_number)) {
      throw new RuntimeException("The row position (" + row_number + ") is out of bound.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - 1);

    System.arraycopy(temp, 0, _data, 0, row_number);
    System.arraycopy(temp, row_number + 1, _data, row_number, n_elem - row_number);
  }

  /**
   * Removes all rows from {@code first_row} to {@code last_row}.
   * 
   * @param first_row The first row position
   * @param last_row The last row position
   * 
   * @throws RuntimeException The first row position ({@code first_row}) is out of bound.
   * @throws RuntimeException The last row position ({@code last_row}) is out of bound.
   * @throws RuntimeException The first row must be less than or equal the last row.
   */
  public void shed_rows(int first_row, int last_row) throws RuntimeException {
    if (!in_range(first_row)) {
      throw new RuntimeException("The first row position (" + first_row + ") is out of bound.");
    }

    if (!in_range(last_row)) {
      throw new RuntimeException("The last row position (" + last_row + ") is out of bound.");
    }

    if (first_row > last_row) {
      throw new RuntimeException("The first row must be less than or equal the last row.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - (last_row - first_row + 1));

    System.arraycopy(temp, 0, _data, 0, first_row);
    System.arraycopy(temp, last_row + 1, _data, first_row, n_elem - first_row);
  }

  /**
   * Returns a deep copy of the {@code col_number}th column.
   * 
   * @param col_number The column position
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   */
  public Col col(int col_number) throws IndexOutOfBoundsException {
    if (!in_range(col_number)) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    /**
     * The first and only column is the same as the whole column vector.
     */
    return new Col(_data);
  }

  /**
   * Returns a deep copy of the {@code row_number}th row.
   * 
   * @param col_number The column position
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public Col row(int row_number) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    /**
     * There is only one element per row.
     */
    return new Col(new double[]{_data[row_number]});
  }

  /**
   * Returns a deep copy of the {@code first_col}th to {@code last_col} column.
   * 
   * @param first_col The first column position
   * @param last_col The last column position
   * 
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  public Col cols(int first_col, int last_col) throws IndexOutOfBoundsException {
    if (!in_range(first_col)) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (!in_range(last_col)) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }

    /**
     * The first and only column is the same as the whole column vector.
     */
    return new Col(_data);
  }

  /**
   * Returns a deep copy of the {@code first_row}th to {@code last_row} row.
   * 
   * @param first_row The first row position
   * @param last_row The last row position
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   */
  public Col rows(int first_row, int last_row) throws IndexOutOfBoundsException {
    if (!in_range(first_row)) {
      throw new IndexOutOfBoundsException("The first row position (" + first_row + ") is out of bounds.");
    }

    if (!in_range(last_row)) {
      throw new IndexOutOfBoundsException("The last row position (" + last_row + ") is out of bounds.");
    }

    /**
     * There is only one element per row.
     */
    return new Col(Arrays.copyOfRange(_data, first_row, last_row));
  }

  /**
   * Returns a deep copy of the {@code span._first}th to {@code span._last} row of the {@code col_number}th column.
   * 
   * @param span The span
   * @param col_number The column position
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code span._last}) is out of bounds.
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   */
  public Col col(Span span, int col_number) throws IndexOutOfBoundsException {
    if (!in_range(col_number)) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    if (span._isEntireRange) {
      /**
       * The first and only column is the same as the whole column vector.
       */
      return new Col(_data);
    } else {
      /**
       * There is only one element per row.
       */
      return rows(span._first, span._last);
    }
  }

  /**
   * Returns a deep copy of the {@code row_number}th row of the {@code span._first}th to {@code span._last} column.
   * 
   * @param row_number The row position
   * @param span The span
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code span._last}) is out of bounds.
   */
  public Col row(int row_number, Span span) throws IndexOutOfBoundsException {
    if (!in_range(span._first)) {
      throw new IndexOutOfBoundsException("The first column position (" + span._first + ") is out of bounds.");
    }

    if (!in_range(span._last)) {
      throw new IndexOutOfBoundsException("The last column position (" + span._last + ") is out of bounds.");
    }

    /**
     * There is only one element per row.
     */
    return row(row_number);
  }

  /**
   * Returns a deep copy of the {@code first_row}th to {@code last_row} row of the {@code first_col}th to
   * {@code last_col} column.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param last_row The last row position
   * @param last_col The last column position
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  public Col submat(int first_row, int first_col, int last_row, int last_col) throws IndexOutOfBoundsException {
    if (!in_range(first_col)) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (!in_range(last_col)) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }

    /**
     * The first and only column is the same as the whole column vector.
     */
    return rows(first_row, last_row);
  }

  /**
   * Returns a deep copy of the {@code row_span._first}th to {@code row_span._last} row of the {@code col_span._first}th
   * to {@code col_span._last} column.
   * 
   * @param row_span The row span
   * @param col_span The column span
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code row_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code row_span._last}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code col_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code col_span._last}) is out of bounds.
   */
  public Col submat(Span row_span, Span col_span) throws IndexOutOfBoundsException {
    return submat(row_span._first, col_span._first, row_span._last, col_span._last);
  }

  /**
   * Returns a deep copy of the {@code first_index}th to {@code last_index} element.
   * 
   * @param first_index The first position
   * @param last_index The last position
   * 
   * @throws IndexOutOfBoundsException The first position ({@code first_index}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code last_index}) is out of bounds.
   */
  public Col subvec(int first_index, int last_index) throws IndexOutOfBoundsException {
    return rows(first_index, last_index);
  }

  /**
   * Returns a deep copy of the {@code span._first}th to {@code span._last} element.
   * 
   * @param span The span
   * 
   * @throws IndexOutOfBoundsException The first position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last position ({@code span._last}) is out of bounds.
   */
  public Col subvec(Span span) throws IndexOutOfBoundsException {
    return subvec(span._first, span._last);
  }

  /**
   * Returns a deep copy starting at position ({@code first_row}, {@code first_col}) of {@code size.n_rows} rows and
   * {@code size.n_cols} columns.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param size The size
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code first_row + size.n_rows - 1}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code first_col + size.n_cols - 1}) is out of bounds.
   */
  public Col submat(int first_row, int first_col, Size size) throws IndexOutOfBoundsException {
    return submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1);
  }

  /**
   * Returns a deep copy of the specified elements.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_indices The positions
   */
  public Col elem(AbstractMat vector_of_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemMat(this, vector_of_indices));
  }

  /**
   * Returns a deep copy of the specified columns.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_column_indices The column positions
   */
  public Col cols(AbstractMat vector_of_column_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemCols(this, vector_of_column_indices));
  }

  /**
   * Returns a deep copy of the specified rows.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   */
  public Col rows(AbstractMat vector_of_row_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemCols(this, vector_of_row_indices));
  }

  /**
   * Returns a deep copy of the specified rows of the specified columns.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param vector_of_column_indices The column positions
   */
  public Col submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemSubMat(this, vector_of_row_indices, vector_of_column_indices));
  }

  /**
   * Swaps the content of this column vector with another one.
   * 
   * @param X The column vector
   */
  public void swap(AbstractMat X) {
    int temp_n_rows = n_rows;
    int temp_n_cols = n_cols;
    double[] temp = Arrays.copyOf(_data, n_elem);

    copy_size(X);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    /*
     * There is no set_size for both vectors and matrices. The previous copy_size(X) should ensure compatibility in both
     * directions.
     */
    X.n_rows = temp_n_rows;
    X.n_cols = temp_n_cols;
    X.n_elem = temp.length;
    X._data = new double[X.n_elem];
    System.arraycopy(temp, 0, X._data, 0, X.n_elem);
  }

  /**
   * Returns the transpose.
   */
  public Row t() {
    return new Row(_data);
  }
}
