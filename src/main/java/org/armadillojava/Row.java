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
  public Row(Row vec) {
    set_size(vec.n_elem);
    System.arraycopy(vec._data, 0, _data, 0, vec.n_elem);
  }

  /**
   * Creates a deep copy of a matrix with exactly one row.
   * 
   * @param mat The matrix
   * 
   * @throws RuntimeException The provided matrix must have exactly one row.
   */
  public Row(AbstractMat mat) throws RuntimeException {
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

  /**
   * Creates a deep copy of a matrix sub view.
   * <p>
   * <b>Note:</b> Error checking in pure internal methods is only covered by unit tests. No checks are applied during
   * run time.
   * 
   * @param mat The sub view
   */
  protected Row(AbstractView view) {
    copy_size(view);

    view.iteratorReset();
    for (int n = 1; n < n_elem; n++) {
      _data[n] = view._data[view.iteratorNext()];
    }
  }

  public void copy_size(AbstractView vec) {
    set_size(vec.n_elem);
  }

  @Override
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
    } else if (is_empty()) {
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
  public void insert_cols(int col_number, int number_of_cols) throws IndexOutOfBoundsException {
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
   * All elements will be set to 0 ({@code set_to_zero} = true) or left uninitialised.
   * 
   * @param col_number The column position
   * @param number_of_cols The number of columns
   * @param set_to_zero Whether the inserted elements are to be set to 0
   */
  public void insert_cols(int col_number, int number_of_cols, boolean set_to_zero) {
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
  public Row plus(double X) {
    Row result = new Row(n_elem);
    result.outOfPlacePlus(this, X);
    return result;
  }

  @Override
  public Row plus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlacePlus(this, X);
    return result;
  }

  @Override
  public Row minus(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceMinus(this, X);
    return result;
  }

  @Override
  public Row minus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceMinus(this, X);
    return result;
  }

  @Override
  public Row elemDivide(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceElemDivide(this, X);
    return result;
  }

  @Override
  public Row elemDivide(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceElemDivide(this, X);
    return result;
  }

  @Override
  public Row times(double X) {
    return elemTimes(X);
  }

  @Override
  public Row times(Row X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (1, 1)-row vectors can be left-hand side multiplied to row vectors.
     */
    return elemTimes(X._data[0]);
  }

  @Override
  public Mat times(Mat X) {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (n, 1)-matrices can be right-hand side multiplied to row vectors.
     */
    return new Mat(new double[]{BLAS.getInstance().ddot(n_cols, _data, 1, X._data, 1)});
  }

  @Override
  public Mat times(Col X) {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    return new Mat(new double[]{BLAS.getInstance().ddot(n_cols, _data, 1, X._data, 1)});
  }

  @Override
  public Row elemTimes(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceElemTimes(this, X);
    return result;
  }

  @Override
  public Row elemTimes(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceElemTimes(this, X);
    return result;
  }

  @Override
  public Row equal(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceEqual(this, X);
    return result;
  }

  @Override
  public Row equal(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceEqual(this, X);
    return result;
  }

  @Override
  public Row nonEqual(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceNonEqual(this, X);
    return result;
  }

  @Override
  public Row nonEqual(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceNonEqual(this, X);
    return result;
  }

  @Override
  public Row greaterThan(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceGreaterThan(this, X);
    return result;
  }

  @Override
  public Row greaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceGreaterThan(this, X);
    return result;
  }

  @Override
  public Row lessThan(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceLessThan(this, X);
    return result;
  }

  @Override
  public Row lessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceLessThan(this, X);
    return result;
  }

  @Override
  public Row strictGreaterThan(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceStrictGreaterThan(this, X);
    return result;
  }

  @Override
  public Row strictGreaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceStrictGreaterThan(this, X);
    return result;
  }

  @Override
  public Row strictLessThan(double X) {
    Row result = new Row(n_elem);
    result.outOfPlaceStrictLessThan(this, X);
    return result;
  }

  @Override
  public Row strictLessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Row result = new Row(n_elem);
    result.outOfPlaceStrictLessThan(this, X);
    return result;
  }

  @Override
  public void set_size(int n_elem) throws NegativeArraySizeException {
    if (n_elem < 0) {
      throw new NegativeArraySizeException("The specified number of elements (" + n_elem + ") must be positive.");
    }

    if (n_elem != this.n_elem) {
      this.n_rows = 1;
      this.n_cols = n_elem;
      this.n_elem = n_elem;

      _data = new double[this.n_elem];
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

  @Override
  public Row col(int col_number) throws IndexOutOfBoundsException {
    if (!in_range(col_number)) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    /**
     * There is only one element per column.
     */
    return new Row(new double[]{_data[col_number]});
  }

  @Override
  public Row row(int row_number) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    /**
     * The first and only row is the same as the whole row vector.
     */
    return new Row(_data);
  }

  @Override
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

  @Override
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

  @Override
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

  @Override
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

  @Override
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

  @Override
  public Row submat(Span row_span, Span col_span) throws IndexOutOfBoundsException {
    return submat(row_span._first, col_span._first, row_span._last, col_span._last);
  }

  @Override
  public Row subvec(int first_index, int last_index) throws IndexOutOfBoundsException {
    return rows(first_index, last_index);
  }

  @Override
  public Row subvec(Span span) throws IndexOutOfBoundsException {
    return subvec(span._first, span._last);
  }

  @Override
  public Row submat(int first_row, int first_col, Size size) throws IndexOutOfBoundsException {
    return submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1);
  }

  @Override
  public Col elem(AbstractMat vector_of_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemMat(this, vector_of_indices));
  }

  @Override
  public Row cols(AbstractMat vector_of_column_indices) throws IndexOutOfBoundsException {
    return new Row(new ViewElemCols(this, vector_of_column_indices));
  }

  @Override
  public Row rows(AbstractMat vector_of_row_indices) throws IndexOutOfBoundsException {
    return new Row(new ViewElemCols(this, vector_of_row_indices));
  }

  @Override
  public Row submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices) throws IndexOutOfBoundsException {
    return new Row(new ViewElemSubMat(this, vector_of_row_indices, vector_of_column_indices));
  }

  @Override
  public void swap(Mat X) {
    if (X.n_rows > 1) {
      throw new RuntimeException("The content of row vectors can only be swaped with matrices that have at most one row.");
    }

    Row temp = new Row(_data);

    set_size(X.n_elem);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_elem, 1);
    System.arraycopy(temp, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(Col X) {
    if (X.n_elem > 1 || n_elem > 1) {
      throw new RuntimeException("The content of row vectors can only be swaped with column vectors if both have at most one element.");
    }

    Row temp = new Row(_data);

    set_size(X.n_elem);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_elem);
    System.arraycopy(temp, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(Row X) {
    Row temp = new Row(_data);

    set_size(X.n_elem);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_elem);
    System.arraycopy(temp, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public Col t() {
    return new Col(_data);
  }
  
}
