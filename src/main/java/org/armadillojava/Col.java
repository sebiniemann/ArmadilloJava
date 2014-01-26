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
   * Creates a deep copy of a vector.
   * 
   * @param vec The vector
   */
  public Col(Col vec) throws RuntimeException {
    set_size(vec.n_elem);
    System.arraycopy(vec._data, 0, _data, 0, vec.n_elem);
  }

  /**
   * Creates a deep copy of a matrix with exactly one column.
   * 
   * @param mat The matrix
   * 
   * @throws RuntimeException The provided matrix must have exactly one column.
   */
  public Col(AbstractMat mat) throws RuntimeException {
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

  @Override
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
    } else if (is_empty()) {
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

  @Override
  public Col plus(double X) {
    Col result = new Col(n_elem);
    result.inPlacePlus(this, X);
    return result;
  }

  @Override
  public Col plus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlacePlus(this, X);
    return result;
  }

  @Override
  public Col minus(double X) {
    Col result = new Col(n_elem);
    result.inPlaceMinus(this, X);
    return result;
  }

  @Override
  public Col minus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceMinus(this, X);
    return result;
  }

  @Override
  public Col elemDivide(double X) {
    Col result = new Col(n_elem);
    result.inPlaceElemDivide(this, X);
    return result;
  }

  @Override
  public Col elemDivide(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceElemDivide(this, X);
    return result;
  }

  @Override
  public Col times(double X) {
    return elemTimes(X);
  }

  @Override
  public Col times(Col X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (1, 1)-column vectors can be right-hand side multiplied to column vectors.
     */
    return elemTimes(X._data[0]);
  }

  @Override
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
    BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
    return result;
  }

  @Override
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
    BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
    return result;
  }

  @Override
  public Col elemTimes(double X) {
    Col result = new Col(n_elem);
    result.inPlaceElemTimes(this, X);
    return result;
  }

  @Override
  public Col elemTimes(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceElemTimes(this, X);
    return result;
  }

  @Override
  public Col equal(double X) {
    Col result = new Col(n_elem);
    result.inPlaceEqual(this, X);
    return result;
  }

  @Override
  public Col equal(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceEqual(this, X);
    return result;
  }

  @Override
  public Col nonEqual(double X) {
    Col result = new Col(n_elem);
    result.inPlaceNonEqual(this, X);
    return result;
  }

  @Override
  public Col nonEqual(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceNonEqual(this, X);
    return result;
  }

  @Override
  public Col greaterThan(double X) {
    Col result = new Col(n_elem);
    result.inPlaceGreaterThan(this, X);
    return result;
  }

  @Override
  public Col greaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceGreaterThan(this, X);
    return result;
  }

  @Override
  public Col lessThan(double X) {
    Col result = new Col(n_elem);
    result.inPlaceLessThan(this, X);
    return result;
  }

  @Override
  public Col lessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceLessThan(this, X);
    return result;
  }

  @Override
  public Col strictGreaterThan(double X) {
    Col result = new Col(n_elem);
    result.inPlaceStrictGreaterThan(this, X);
    return result;
  }

  @Override
  public Col strictGreaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceStrictGreaterThan(this, X);
    return result;
  }

  @Override
  public Col strictLessThan(double X) {
    Col result = new Col(n_elem);
    result.inPlaceStrictLessThan(this, X);
    return result;
  }

  @Override
  public Col strictLessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Col result = new Col(n_elem);
    result.inPlaceStrictLessThan(this, X);
    return result;
  }

  @Override
  public void set_size(int n_elem) throws NegativeArraySizeException {
    if (n_elem < 0) {
      throw new NegativeArraySizeException("The specified number of elements (" + n_elem + ") must be positive.");
    }

    if (n_elem != this.n_elem) {
      this.n_rows = n_elem;
      this.n_cols = 1;
      this.n_elem = n_elem;

      _data = new double[this.n_elem];
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

  @Override
  public Col col(int col_number) throws IndexOutOfBoundsException {
    if (!in_range(col_number)) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    /**
     * The first and only column is the same as the whole column vector.
     */
    return new Col(_data);
  }

  @Override
  public Col row(int row_number) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    /**
     * There is only one element per row.
     */
    return new Col(new double[]{_data[row_number]});
  }

  @Override
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

  @Override
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
    return new Col(Arrays.copyOfRange(_data, first_row, last_row + 1));
  }

  @Override
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

  @Override
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

  @Override
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

  @Override
  public Col submat(Span row_span, Span col_span) throws IndexOutOfBoundsException {
    return submat(row_span._first, col_span._first, row_span._last, col_span._last);
  }

  @Override
  public Col subvec(int first_index, int last_index) throws IndexOutOfBoundsException {
    return rows(first_index, last_index);
  }

  @Override
  public Col subvec(Span span) throws IndexOutOfBoundsException {
    return subvec(span._first, span._last);
  }

  @Override
  public Col submat(int first_row, int first_col, Size size) throws IndexOutOfBoundsException {
    return submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1);
  }

  @Override
  public Col elem(AbstractMat vector_of_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemMat(this, vector_of_indices));
  }

  @Override
  public Col cols(AbstractMat vector_of_column_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemCols(this, vector_of_column_indices));
  }

  @Override
  public Col rows(AbstractMat vector_of_row_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemCols(this, vector_of_row_indices));
  }

  @Override
  public Col submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices) throws IndexOutOfBoundsException {
    return new Col(new ViewElemSubMat(this, vector_of_row_indices, vector_of_column_indices));
  }

  @Override
  public void swap(Mat X) {
    if (X.n_cols > 1) {
      throw new RuntimeException("The content of column vectors can only be swaped with matrices that have at most one column.");
    }

    Col temp = new Col(_data);
    
    set_size(X.n_elem);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_elem, 1);
    System.arraycopy(temp, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(Row X) {
    if (X.n_elem > 1 || n_elem > 1) {
      throw new RuntimeException("The content of column vectors can only be swaped with row vectors if both have at most one element.");
    }

    Col temp = new Col(_data);
    
    set_size(X.n_elem);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_elem);
    System.arraycopy(temp, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(Col X) {
    Col temp = new Col(_data);
    
    set_size(X.n_elem);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_elem);
    System.arraycopy(temp, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public Row t() {
    return new Row(_data);
  }
}
