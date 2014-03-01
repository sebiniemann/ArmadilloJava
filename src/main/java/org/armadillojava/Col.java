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
    _data = Arrays.copyOf(vec._data, vec.n_elem);
  }

  /**
   * Creates a deep copy of a matrix with exactly one column.
   * 
   * @param mat The matrix
   * 
   * @throws RuntimeException The provided ({@code mat.n_rows}, {@code mat.n_cols})-matrix must have exactly one column.
   */
  public Col(final AbstractMat mat) throws RuntimeException {
    if (mat.n_cols > 1) {
      throw new RuntimeException("The provided (" + mat.n_rows + ", " + mat.n_cols + ")-matrix must have exactly one column.");
    }

    copy_size(mat);
    _data = Arrays.copyOf(mat._data, mat.n_elem);
  }

  /**
   * Creates a column vector with the same number of elements and values as the provided array.
   * 
   * @param array The array
   */
  public Col(final double[] array) {
    set_size(array.length);
    _data = Arrays.copyOf(array, array.length);
  }

  /**
   * Creates a deep copy of a matrix sub view.
   * 
   * @param view The sub view
   */
  protected Col(final AbstractView view) {
    copy_size(view);

    view.iteratorReset();
    for (int n = 1; n < n_elem; n++) {
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
   * Inserts the rows from {@code X} between row {@code row_number} - 1 and {@code row_number}.
   * 
   * @param row_number The row
   * @param X The column vector
   * 
   * @throws IndexOutOfBoundsException The row ({@code row_number}) is out of bounds.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape to a
   *           vector.
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
      _data = Arrays.copyOf(X._data, X.n_elem);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_elem + X.n_elem);

      System.arraycopy(temp, 0, _data, 0, row_number);
      System.arraycopy(X._data, row_number, _data, 0, X.n_elem);
      System.arraycopy(temp, X.n_elem + row_number, _data, 0, n_elem - row_number);
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
      System.arraycopy(temp, number_of_rows + row_number, _data, 0, n_elem - row_number);
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
  public void insert_rows(final int row_number, final int number_of_rows, final boolean set_to_zero)  throws IndexOutOfBoundsException, NegativeArraySizeException {
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
  public Col plus(final AbstractMat X) throws RuntimeException {
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
  public Col minus(final AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    minus(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col elemDivide(final double X) {
    Col result = new Col(n_elem);
    elemDivide(result._data, _data, X);
    return result;
  }

  @Override
  public Col elemDivide(final AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    elemDivide(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col times(final double X) {
    return elemTimes(X);
  }

  @Override
  public Col times(final Col X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (1, 1)-column vectors can be right-hand side multiplied to column vectors.
     */
    return elemTimes(X._data[0]);
  }

  @Override
  public Mat times(final Row X) {
    Mat result = new Mat(n_rows, X.n_cols);
    BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
    return result;
  }

  @Override
  public Mat times(final Mat X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (1, m)-matrices can be right-hand side multiplied to column vectors.
     */
    Mat result = new Mat(n_rows, X.n_cols);
    BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
    return result;
  }

  @Override
  public Col elemTimes(final double X) {
    Col result = new Col(n_elem);
    elemTimes(result._data, _data, X);
    return result;
  }

  @Override
  public Col elemTimes(final AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    elemTimes(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col equal(final double X) {
    Col result = new Col(n_elem);
    equal(result._data, _data, X);
    return result;
  }

  @Override
  public Col equal(final AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    equal(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col nonEqual(final double X) {
    Col result = new Col(n_elem);
    nonEqual(result._data, _data, X);
    return result;
  }

  @Override
  public Col nonEqual(final AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + X.n_rows + ", " + X.n_cols + ") must have the same shape.");
    }

    Col result = new Col(n_elem);
    nonEqual(result._data, _data, X._data);
    return result;
  }

  @Override
  public Col greaterThan(final double X) {
    Col result = new Col(n_elem);
    greaterThan(result._data, _data, X);
    return result;
  }

  @Override
  public Col greaterThan(final AbstractMat X) throws RuntimeException {
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
  public Col lessThan(final AbstractMat X) throws RuntimeException {
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
  public Col strictGreaterThan(final AbstractMat X) throws RuntimeException {
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
  public Col strictLessThan(final AbstractMat X) throws RuntimeException {
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
      this.n_rows = n_elem;
      this.n_cols = 1;
      this.n_elem = n_elem;

      _data = new double[this.n_elem];
    }
  }

  /**
   * Removes the specified row.
   * 
   * @param row_number The row
   * 
   * @throws RuntimeException The row ({@code row_number}) is out of bound.
   */
  public void shed_row(final int row_number) throws RuntimeException {
    if (!in_range(row_number)) {
      throw new RuntimeException("The row (" + row_number + ") is out of bound.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_elem - 1);

    System.arraycopy(temp, 0, _data, 0, row_number);
    System.arraycopy(temp, row_number + 1, _data, row_number, n_elem - row_number);
  }

  /**
   * Removes all rows from {@code first_row} to {@code last_row}.
   * 
   * @param first_row The first row
   * @param last_row The last row
   * 
   * @throws RuntimeException The first row ({@code first_row}) is out of bound.
   * @throws RuntimeException The last row ({@code last_row}) is out of bound.
   * @throws RuntimeException The first row must be less than or equal the last row.
   */
  public void shed_rows(final int first_row, final int last_row) throws RuntimeException {
    if (!in_range(first_row)) {
      throw new RuntimeException("The first row (" + first_row + ") is out of bound.");
    }

    if (!in_range(last_row)) {
      throw new RuntimeException("The last row (" + last_row + ") is out of bound.");
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
  public Col subvec(final int first_index, final int last_index) throws RuntimeException, IndexOutOfBoundsException {
    if (last_index < first_index) {
      throw new RuntimeException("The first specified row (" + first_index + ") must be less than or equal the last specified row (" + last_index + ").");
    }

    if (first_index < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_index + ") is out of bounds.");
    }

    if (last_index > n_cols - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_index + ") is out of bounds.");
    }

    Col col = new Col(last_index - first_index + 1);
    col._data = Arrays.copyOfRange(_data, first_index, last_index + 1);
    return col;
  }

  @Override
  public Col subvec(final Span span) throws IndexOutOfBoundsException {
    /*
     * The parameter "span" is already validated during its instantiation.
     */
    return subvec(span._first, span._last);
  }

  @Override
  public void swap(final Mat X) throws RuntimeException {
    if (!X.is_colvec()) {
      throw new RuntimeException("The content of column vectors can only be swaped with matrices that have at most one column.");
    }

    Col temp = new Col(_data);

    copy_size(X);
    _data = Arrays.copyOf(X._data, X.n_elem);

    copy_size(temp);
    X._data = Arrays.copyOf(temp._data, temp.n_elem);
  }

  @Override
  public void swap(final Row X) throws RuntimeException {
    if (!X.is_colvec()) {
      throw new RuntimeException("The content of column vectors can only be swaped with row vectors if both have at most one element.");
    }

    Col temp = new Col(_data);

    copy_size(X);
    _data = Arrays.copyOf(X._data, X.n_elem);

    copy_size(temp);
    X._data = Arrays.copyOf(temp._data, temp.n_elem);
  }

  @Override
  public void swap(final Col X) {
    Col temp = new Col(_data);

    copy_size(X);
    _data = Arrays.copyOf(X._data, X.n_elem);

    copy_size(temp);
    X._data = Arrays.copyOf(temp._data, temp.n_elem);
  }

  @Override
  public Row t() {
    return new Row(_data);
  }

}
