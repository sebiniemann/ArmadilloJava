package org.armadillojava;

import java.util.Arrays;

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
   * @param n_elem
   */
  public Row(int n_elem) {
    set_size(n_elem);
  }

  /**
   * Creates a row vector with the specified number of elements that is filled according to {@code fillType}.
   * 
   * @param n_elem The number of elements
   * @param fill_type The fill type
   * 
   * @throws RuntimeException The fill type ({@code fill_type}) is not supported for row vectors.
   * 
   * @see Fill
   */
  public Row(int n_elem, Fill fill_type) throws RuntimeException {
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
  public Row(AbstractMat vec) {
    copy_size(vec);
    System.arraycopy(vec._data, 0, _data, 0, vec.n_elem);
  }

  /**
   * Creates a deep copy of a matrix with exactly one row.
   * 
   * @param mat The matrix
   * 
   * @throws RuntimeException The provided matrix must have exactly one row.
   */
  public Row(Mat mat) throws RuntimeException {
    if (mat.n_rows > 1) {
      throw new RuntimeException("The provided matrix must have exactly one row.");
    }

    set_size(mat.n_elem);
    System.arraycopy(mat._data, 0, _data, 0, mat.n_elem);
  }

  /**
   * Creates a row vector with the same number of elements and values as the provided array.
   * 
   * @param array The array
   */
  public Row(double[] array) {
    set_size(array.length);
    System.arraycopy(array, 0, _data, 0, array.length);
  }

  protected Row(AbstractView view) {
    copy_size(view);

    view.iteratorReset();
    for (int n = 1; n < n_elem; n++) {
      _data[n] = view._data[view.iteratorNext()];
    }
  }

  /**
   * Sets the size to be the same as {@code A}.
   * 
   * @param vec The row vector
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
   * Inserts the columns from {@code X} at column position {@code col_number}.
   * 
   * @param col_number The column position
   * @param X The column vector
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public void insert_cols(int col_number, Row X) throws IndexOutOfBoundsException {
    if (col_number < 0 || col_number > n_elem) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
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

      System.arraycopy(temp, 0, _data, 0, col_number);
      System.arraycopy(X._data, col_number, _data, 0, X.n_elem);
      System.arraycopy(temp, X.n_elem + col_number, _data, 0, n_elem - col_number);
    }
  }

  /**
   * Inserts {@code number_of_cols} uninitialised columns at column position {@code col_number}.
   * 
   * @param col_number The column position
   * @param number_of_cols The number of columns
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public void insert_rows(int col_number, int number_of_cols) throws IndexOutOfBoundsException {
    if (col_number < 0 || col_number > n_elem) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
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
   * Inserts {@code number_of_cols} columns at column position {@code col_number}.
   * <p>
   * The will be set to 0 ({@code set_to_zero} = true) or left uninitialised.
   * 
   * @param col_number The column position
   * @param number_of_cols The number of columns
   * @param set_to_zero Whether the inserted elements are to be set to 0
   */
  public void insert_rows(int col_number, int number_of_cols, boolean set_to_zero) {
    /*
     * All entries of an array are already set to 0 during creation.
     * Therefore, set_to_zero will be ignored.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    insert_rows(col_number, number_of_cols);
  }

  /**
   * Returns the out-of-place addition with the specified right-hand side addend.
   * 
   * @param X The addend
   */
  public Row plus(double X) {
    Row result = new Row(_data);
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
  public Row plus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(_data);
    result.inPlacePlus(X);
    return result;
  }

  /**
   * Returns the out-of-place subtraction with the specified right-hand side subtrahend.
   * 
   * @param X The subtrahend
   */
  public Row minus(double X) {
    Row result = new Row(_data);
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
  public Row minus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(_data);
    result.inPlaceMinus(X);
    return result;
  }

  /**
   * Returns the out-of-place element-wise division with the specified right-hand side divisor.
   * 
   * @param operand The divisor
   */
  public Row elemDivide(double X) {
    Row result = new Row(_data);
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
  public Row elemDivide(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(_data);
    result.inPlaceElemDivide(X);
    return result;
  }

  public Row times(double X) {
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
  public Row times(Row X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (1, 1)-row vectors can be **left**-hand side multiplied to row vectors.
     */
    return elemTimes(X._data[0]);
  }

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * <p>
   * Handles the multiplication for matrices of type {@code Col} as well as {@code Mat}.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The numbers of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  public Mat times(AbstractMat X) {
    /*
     * Only (n, 1)-matrices can be right-hand side multiplied to column vectors. Therefore, it can be handled exactly
     * the same as the right-hand side multiplication of column vectors.
     */
    
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * The right-hand side multiplication with a column vector will result in a single scalar value.
     */
    double result = 0;
    for (int n = 0; n < n_elem; n++) {
      result += _data[n] * X._data[n];
    }

    return new Mat(new double[]{result});
  }

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The numbers of columns ({@code n_cols}) must be equal to the number of row (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  public Mat times(Mat X) throws RuntimeException {

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
  public Row elemTimes(double X) {
    Row result = new Row(this);
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
  public Row elemTimes(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(this);
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
  public Row equal(double X) {
    Row result = new Row(n_elem);

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
  public Row equal(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);

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
  public Row nonEqual(double X) {
    Row result = new Row(n_elem);

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
  public Row nonEqual(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);

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
  public Row greaterThan(double X) {
    Row result = new Row(n_elem);

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
  public Row greaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);

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
  public Row lessThan(double X) {
    Row result = new Row(n_elem);

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
  public Row lessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);

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
  public Row strictGreaterThan(double X) {
    Row result = new Row(n_elem);

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
  public Row strictGreaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);

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
  public Row strictLessThan(double X) {
    Row result = new Row(n_elem);

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
  public Row strictLessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);

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
      this.n_rows = 1;
      this.n_cols = n_elem;
      this.n_elem = n_elem;
    }
  }

  /**
   * Removes the specified column.
   * 
   * @param col_number The column position
   * 
   * @throws RuntimeException The column position ({@code row_number}) is out of bound.
   */
  public void shed_col(int col_number) {
    if (!in_range(col_number)) {
      throw new RuntimeException("The column position (" + col_number + ") is out of bound.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - 1);

    System.arraycopy(temp, 0, _data, 0, col_number);
    System.arraycopy(temp, col_number + 1, _data, col_number, n_elem - col_number);
  }

  /**
   * Removes all columns from {@code first_col} to {@code last_col}.
   * 
   * @param first_col The first column position
   * @param last_col The last column position
   * 
   * @throws RuntimeException The first column position ({@code first_col}) is out of bound.
   * @throws RuntimeException The last column position ({@code last_col}) is out of bound.
   * @throws RuntimeException The first column must be less than or equal the last column.
   */
  public void shed_cols(int first_col, int last_col) throws RuntimeException {
    if (!in_range(first_col)) {
      throw new RuntimeException("The first column position (" + first_col + ") is out of bound.");
    }

    if (!in_range(last_col)) {
      throw new RuntimeException("The last column position (" + last_col + ") is out of bound.");
    }

    if (first_col > last_col) {
      throw new RuntimeException("The first column must be less than or equal the last column.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - (last_col - first_col + 1));

    System.arraycopy(temp, 0, _data, 0, first_col);
    System.arraycopy(temp, last_col + 1, _data, first_col, n_elem - first_col);
  }

  /**
   * Returns a deep copy of the {@code col_number}th column.
   * 
   * @param col_number The column position
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   */
  public Row col(int col_number) throws IndexOutOfBoundsException {
    if (!in_range(col_number)) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    /**
     * There is only one element per column.
     */
    return new Row(new double[]{_data[col_number]});
  }

  /**
   * Returns a deep copy of the {@code row_number}th row.
   * 
   * @param col_number The column position
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public Row row(int row_number) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    /**
     * The first and only row is the same as the whole row vector.
     */
    return new Row(_data);
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
  public Row cols(int first_col, int last_col) throws IndexOutOfBoundsException {
    if (!in_range(first_col)) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (!in_range(last_col)) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }

    /**
     * There is only one element per column.
     */
    return new Row(Arrays.copyOfRange(_data, first_col, last_col));
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
  public Row rows(int first_row, int last_row) throws IndexOutOfBoundsException {
    if (!in_range(first_row)) {
      throw new IndexOutOfBoundsException("The first row position (" + first_row + ") is out of bounds.");
    }

    if (!in_range(last_row)) {
      throw new IndexOutOfBoundsException("The last row position (" + last_row + ") is out of bounds.");
    }

    /**
     * The first and only row is the same as the whole row vector.
     */
    return new Row(_data);
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
  public Row col(Span span, int col_number) throws IndexOutOfBoundsException {
    if (!in_range(span._first)) {
      throw new IndexOutOfBoundsException("The first row position (" + span._first + ") is out of bounds.");
    }

    if (!in_range(span._last)) {
      throw new IndexOutOfBoundsException("The last row position (" + span._last + ") is out of bounds.");
    }

    /**
     * There is only one element per column.
     */
    return col(col_number);
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
  public Row row(int row_number, Span span) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    if (span._isEntireRange) {
      /**
     * The first and only row is the same as the whole row vector.
       */
      return new Row(this);
    } else {
      /**
     * There is only one element per column.
       */
      return cols(span._first, span._last);
    }
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
  public Row submat(int first_row, int first_col, int last_row, int last_col) throws IndexOutOfBoundsException {
    if (!in_range(first_row)) {
      throw new IndexOutOfBoundsException("The first row position (" + first_row + ") is out of bounds.");
    }

    if (!in_range(last_row)) {
      throw new IndexOutOfBoundsException("The last row position (" + last_row + ") is out of bounds.");
    }

    /**
     * The first and only row is the same as the whole row vector.
     */
    return cols(first_col, last_col);
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
  public Row submat(Span row_span, Span col_span) throws IndexOutOfBoundsException {
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
  public Row subvec(int first_index, int last_index) throws IndexOutOfBoundsException {
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
  public Row subvec(Span span) throws IndexOutOfBoundsException {
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
  public Row submat(int first_row, int first_col, Size size) throws IndexOutOfBoundsException {
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
  public Row cols(AbstractMat vector_of_column_indices) throws IndexOutOfBoundsException {
    return new Row(new ViewElemCols(this, vector_of_column_indices));
  }

  /**
   * Returns a deep copy of the specified rows.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   */
  public Row rows(AbstractMat vector_of_row_indices) throws IndexOutOfBoundsException {
    return new Row(new ViewElemCols(this, vector_of_row_indices));
  }

  /**
   * Returns a deep copy of the specified rows of the specified columns.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param vector_of_column_indices The column positions
   */
  public Row submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices) throws IndexOutOfBoundsException {
    return new Row(new ViewElemSubMat(this, vector_of_row_indices, vector_of_column_indices));
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
  public Col t() {
    return new Col(_data);
  }
}