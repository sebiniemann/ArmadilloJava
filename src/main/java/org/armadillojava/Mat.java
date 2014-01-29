package org.armadillojava;

import java.util.Arrays;

import org.netlib.util.intW;

import com.github.fommil.netlib.BLAS;
import com.github.fommil.netlib.LAPACK;

/**
 * Provides a real-valued dense matrix with interfaces similar to the Armadillo C++ Algebra Library (Armadillo) by
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
public class Mat extends AbstractMat {

  /**
   * Creates an empty matrix.
   */
  public Mat() {
    set_size(0, 0);
  }

  /**
   * Creates an uninitialised matrix with the specified number of rows and columns.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   */
  public Mat(int n_rows, int n_cols) {
    // n_rows and n_cols are validated in set_size
    set_size(n_rows, n_cols);
  }

  /**
   * Creates a matrix with the specified number of rows and columns that is filled according to {@code fillType}.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * @param fill_type The fill type
   * 
   * @throws RuntimeException The fill type ({@code fill_type}) is not supported for column vectors.
   * 
   * @see Fill
   */
  public Mat(int n_rows, int n_cols, Fill fill_type) {
    switch (fill_type) {
      case NONE:
        // n_rows and n_cols are validated in set_size
        set_size(n_rows, n_cols);
        break;
      case ZEROS:
        // n_rows and n_cols are validated in zeros
        zeros(n_rows, n_cols);
        break;
      case ONES:
        // n_rows and n_cols are validated in ones
        ones(n_rows, n_cols);
        break;
      case EYE:
        // n_rows and n_cols are validated in ones
        eye(n_rows, n_cols);
        break;
      case RANDU:
        // n_rows and n_cols are validated in randu
        randu(n_rows, n_cols);
        break;
      case RANDN:
        // n_rows and n_cols are validated in randn
        randn(n_rows, n_cols);
    }
  }

  /**
   * Creates a deep copy of a matrix.
   * 
   * @param mat The matrix
   */
  public Mat(AbstractMat mat) {
    set_size(mat.n_rows, mat.n_cols);
    System.arraycopy(mat._data, 0, _data, 0, mat.n_elem);
  }

  /**
   * Creates a matrix in the shape of a column vector with the same number of elements and values as the provided array.
   * 
   * @param array The array
   */
  public Mat(double[] array) {
    set_size(array.length, 1);
    System.arraycopy(array, 0, _data, 0, array.length);
  }
  
  /**
   * Creates a deep copy of a matrix sub view.
   * 
   * @param mat The sub view
   */
  protected Mat(AbstractView view) {
    copy_size(view);

    view.iteratorReset();
    for (int n = 1; n < n_elem; n++) {
      _data[n] = view._data[view.iteratorNext()];
    }
  }
  
  /**
   * Returns a deep copy of the main diagonal.
   */
  public Mat diag() {
    return new Mat(new ViewDiag(this, 0));
  }
  
  /**
   * Performs an in-place unary operation on the main diagonal.
   * 
   * @param unary_operator The unary operator
   */
  public void diag(Op unary_operator) {
    new Mat(new ViewDiag(this, 0)).inPlace(unary_operator);
  }
  
  /**
   * Performs an in-place binary operation on the main diagonal with the specified right-hand side operand.
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void diag(Op binary_operator, double operand) {
    new Mat(new ViewDiag(this, 0)).inPlace(binary_operator, operand);
  }
  
  /**
   * Performs an in-place binary operation on the main diagonal with the specified right-hand side operand.
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void diag(Op binary_operator, AbstractMat operand) {
    new Mat(new ViewDiag(this, 0)).inPlace(binary_operator, operand);
  }
  
  /**
   * Returns a deep copy of the {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, its the main diagonal.
   * <li>For {@code k} > 0, its the {@code k}th super-diagonal.
   * <li>For {@code k} < 0, its the {@code k}th sub-diagonal.
   * </ul>
   */
  public Mat diag(int k) {
    if (k > 0 && k >= n_cols) {
      throw new IndexOutOfBoundsException("The diagonal index (" + k + ") is out of bounds.");
    }

    if (k < 0 && -k <= n_rows) {
      throw new IndexOutOfBoundsException("The diagonal index (" + k + ") is out of bounds.");
    }

    return new Mat(new ViewDiag(this, k));
  }
  
  /**
   * Performs an in-place unary operation on the {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, its the main diagonal.
   * <li>For {@code k} > 0, its the {@code k}th super-diagonal.
   * <li>For {@code k} < 0, its the {@code k}th sub-diagonal.
   * </ul>
   * 
   * @param unary_operator The unary operator
   */
  public void diag(int k, Op unary_operator) {
    if (k > 0 && k >= n_cols) {
      throw new IndexOutOfBoundsException("The diagonal index (" + k + ") is out of bounds.");
    }

    if (k < 0 && -k <= n_rows) {
      throw new IndexOutOfBoundsException("The diagonal index (" + k + ") is out of bounds.");
    }
    
    new Mat(new ViewDiag(this, k)).inPlace(unary_operator);
  }
  
  /**
   * Performs an in-place binary operation on the {@code k}th diagonal with the specified right-hand side operand.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, its the main diagonal.
   * <li>For {@code k} > 0, its the {@code k}th super-diagonal.
   * <li>For {@code k} < 0, its the {@code k}th sub-diagonal.
   * </ul>
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void diag(int k, Op binary_operator, double operand) {
    if (k > 0 && k >= n_cols) {
      throw new IndexOutOfBoundsException("The diagonal index (" + k + ") is out of bounds.");
    }

    if (k < 0 && -k <= n_rows) {
      throw new IndexOutOfBoundsException("The diagonal index (" + k + ") is out of bounds.");
    }
    
    new Mat(new ViewDiag(this, k)).inPlace(binary_operator, operand);
  }
  
  /**
   * Performs an in-place binary operation on the {@code k}th diagonal with the specified right-hand side operand.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, its the main diagonal.
   * <li>For {@code k} > 0, its the {@code k}th super-diagonal.
   * <li>For {@code k} < 0, its the {@code k}th sub-diagonal.
   * </ul>
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void diag(int k, Op binary_operator, AbstractMat operand) {
    new Mat(new ViewDiag(this, k)).inPlace(binary_operator, operand);
  }
  
  /**
   * Performs an in-place unary operation on each column of the matrix individually.
   * 
   * @param unary_operator The unary operator
   */
  public void each_col(Op unary_operator) {
    inPlace(unary_operator);
  }
  
  /**
   * Performs an in-place binary operation on each column of the matrix individually with the specified right-hand side operand.
   *
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void each_col(Op binary_operator, double operand) {
    inPlace(binary_operator, operand);
  }
  
  /**
   * Performs an in-place binary operation on each column of the matrix individually with the specified right-hand side operand.
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void each_col(Op binary_operator, AbstractMat operand) {
    for (int j = 0; j < n_cols; j++) {
      col(j, binary_operator, operand);
    }
  }
  
  /**
   * Performs an in-place unary operation on each specified column of the matrix individually.
   * 
   * @param vector_of_indices The column positions
   * @param unary_operator The unary operator
   */
  public void each_col(AbstractMat vector_of_indices, Op unary_operator) {
    cols(vector_of_indices, unary_operator);
  }
  
  /**
   * Performs an in-place binary operation on each specified column of the matrix individually with the specified right-hand side operand.
   * 
   * @param vector_of_indices The column positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void each_col(AbstractMat vector_of_indices, Op binary_operator, double operand) {
    cols(vector_of_indices, binary_operator, operand);
  }
  
  /**
   * Performs an in-place binary operation on each specified column of the matrix individually with the specified right-hand side operand.
   * 
   * @param vector_of_indices The column positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void each_col(AbstractMat vector_of_indices, Op binary_operator, AbstractMat operand) {
    for (int n = 0; n < vector_of_indices.n_elem; n++) {
      col((int) vector_of_indices._data[n], binary_operator, operand);
    }
  }
  
  /**
   * Performs an in-place unary operation on each row of the matrix individually.
   * 
   * @param unary_operator The unary operator
   */
  public void each_row(Op unary_operator) {
    inPlace(unary_operator);
  }
  
  /**
   * Performs an in-place binary operation on each row of the matrix individually with the specified right-hand side operand.
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void each_row(Op binary_operator, double operand) {
    inPlace(binary_operator, operand);
  }
  
  /**
   * Performs an in-place binary operation on each row of the matrix individually with the specified right-hand side operand.
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void each_row(Op binary_operator, AbstractMat operand) {
    for (int i = 0; i < n_rows; i++) {
      row(i, binary_operator, operand);
    }
  }
  
  /**
   * Performs an in-place unary operation on each specified row of the matrix individually.
   * 
   * @param vector_of_indices The row positions
   * @param unary_operator The unary operator
   */
  public void each_row(AbstractMat vector_of_indices, Op unary_operator) {
    rows(vector_of_indices, unary_operator);
  }
  
  /**
   * Performs an in-place binary operation on each specified row of the matrix individually with the specified right-hand side operand.
   * 
   * @param vector_of_indices The row positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void each_row(AbstractMat vector_of_indices, Op binary_operator, double operand) {
    rows(vector_of_indices, binary_operator, operand);
  }
  
  /**
   * Performs an in-place binary operation on each specified row of the matrix individually with the specified right-hand side operand.
   * 
   * @param vector_of_indices The row positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void each_row(AbstractMat vector_of_indices, Op binary_operator, AbstractMat operand) {
    for (int n = 0; n < vector_of_indices.n_elem; n++) {
      row((int) vector_of_indices._data[n], binary_operator, operand);
    }
  }
  
  /**
   * Returns the value of the element at the {@code i}th row and {@code j}th column.
   * 
   * @param i The row position
   * @param j The column position
   */
  public double at(int i, int j) {
    return _data[i + j * n_rows];
  }
  
  /**
   * Sets all elements along the main diagonal to 1 and all others to 0.
   */
  public void eye() {
    _data = new double[n_elem];
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */

    int length = Math.min(n_rows, n_cols);
    for (int n = 0; n < length; n++) {
      _data[n + n * n_rows] = 1;
    }
  }
  
  /**
   * Resizes the matrix to the specified number of rows and columns and sets all elements along the main diagonal to 1 and all others to 0.
   */
  public void eye(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */

    int length = Math.min(n_rows, n_cols);
    for (int n = 0; n < length; n++) {
      _data[n + n * n_rows] = 1;
    }
  }

  /**
   * Returns the inverse.
   * 
   * @throws RuntimeException Only square matrices can be inverted.
   * @throws RuntimeException The matrix appears to be singular.
   */
  public Mat i() throws RuntimeException {
    if(!is_square()) {
      throw new RuntimeException("Only square matrices can be inverted.");
    }
    
    Mat inverse = new Mat(this);
    int[] pivotIndices = new int[n_rows * n_cols];
    intW info = new intW(0);
    
    LAPACK.getInstance().dgetrf(n_rows, n_cols, inverse._data, n_rows, pivotIndices, info);
    if(info.val != 0) {
      throw new RuntimeException("The matrix appears to be singular.");
    }
    
    double[] temp = new double[n_cols];
    LAPACK.getInstance().dgetri(n_rows, inverse._data, n_rows, pivotIndices, temp, n_cols, info);
    if(info.val != 0) {
      throw new RuntimeException("The matrix appears to be singular.");
    }
    
    return inverse;
  }

  /**
   * Returns true if the number of rows equals the number of columns.
   */
  public boolean is_square() {
    return (n_rows == n_cols);
  }

  /**
   * Returns true if the matrix has only one row or column.
   */
  public boolean is_vec() {
    return (is_colvec() || is_rowvec());
  }

  /**
   * Returns true if the matrix has only one column.
   */
  public boolean is_colvec() {
    return (n_cols == 1);
  }

  /**
   * Returns true if the matrix has only one row.
   */
  public boolean is_rowvec() {
    return (n_rows == 1);
  }
  
  /**
   * Inserts the rows from {@code X} at row position {@code row_number}.
   * 
   * @param row_number The row position
   * @param X The column vector
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   * @throws RuntimeException Both matrices must have the same number of columns.
   */
  public void insert_rows(int row_number, AbstractMat X) throws IndexOutOfBoundsException, RuntimeException {
    if (row_number < 0 || row_number > n_elem) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    if (n_cols != X.n_cols) {
      throw new RuntimeException("Both matrices must have the same number of columns.");
    }

    if (X.is_empty()) {
      return; // Nothing to do here.
    } else if (is_empty()) {
      set_size(X.n_rows, X.n_cols);
      System.arraycopy(X._data, 0, _data, 0, X.n_elem);
    } else {
      Mat temp = new Mat(this);
      set_size(n_rows + X.n_rows, n_cols);

      rows(0, row_number - 1, Op.EQUAL, temp.rows(0, row_number - 1));
      rows(row_number, row_number + X.n_rows - 1, Op.EQUAL, X);
      // n_rows has been updated by set_size
      rows(row_number + X.n_rows, n_rows - 1, Op.EQUAL, rows(row_number, n_rows - 1));
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
      set_size(number_of_rows, 1);
    } else {
      Mat temp = new Mat(this);
      set_size(n_rows + number_of_rows, n_cols);

      rows(0, row_number - 1, Op.EQUAL, temp.rows(0, row_number - 1));
      rows(row_number + number_of_rows, n_rows - 1, Op.EQUAL, rows(row_number, n_rows - 1));
    }
  }
  
  /**
   * Inserts {@code number_of_rows} rows at row position {@code row_number}.
   * <p>
   * All elements will be set to 0 ({@code set_to_zero} = true) or left uninitialised.
   * 
   * @param row_number The row position
   * @param number_of_rows The number of rows
   * @param set_to_zero Whether the inserted elements are to be set to 0
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public void insert_rows(int row_number, int number_of_rows, boolean set_to_zero) throws IndexOutOfBoundsException {
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
   * Inserts the columns from {@code X} at column position {@code col_number}.
   * 
   * @param col_number The column position
   * @param X The column vector
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   * @throws RuntimeException Both matrices must have the same number of columns.
   */
  public void insert_cols(int col_number, AbstractMat X) throws IndexOutOfBoundsException, RuntimeException {
    if (col_number < 0 || col_number > n_elem) {
      throw new IndexOutOfBoundsException("The row position (" + col_number + ") is out of bounds.");
    }

    if (n_rows != X.n_rows) {
      throw new RuntimeException("Both matrices must have the same number of rows.");
    }

    if (X.is_empty()) {
      return; // Nothing to do here.
    } else if (is_empty()) {
      set_size(n_rows, X.n_cols);
      System.arraycopy(X._data, 0, _data, 0, X.n_elem);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_rows, n_cols + X.n_cols);

      System.arraycopy(temp, 0, _data, 0, col_number * n_rows);
      System.arraycopy(X._data, 0, _data, col_number * n_rows, X.n_elem);
      System.arraycopy(temp, (X.n_cols + col_number) * n_rows, _data, 0, n_elem - col_number * n_rows);
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
      throw new IndexOutOfBoundsException("The row position (" + col_number + ") is out of bounds.");
    }

    if (number_of_cols == 0) {
      return; // Nothing to do here.
    } else if (is_empty()) {
      set_size(1, number_of_cols);
    } else {
      double[] temp = Arrays.copyOf(_data, n_elem);
      set_size(n_rows, n_cols + number_of_cols);

      System.arraycopy(temp, 0, _data, 0, col_number * n_rows);
      System.arraycopy(temp, (number_of_cols + col_number) * n_rows, _data, 0, n_elem - col_number * n_rows);
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
  public void insert_cols(int col_number, int number_of_cols, boolean set_to_zero) throws IndexOutOfBoundsException {
    /*
     * All entries of an array are already set to 0 during creation.
     * Therefore, set_to_zero will be ignored.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    insert_cols(col_number, number_of_cols);
  }
  
  /**
   * Returns the smallest value within the matrix and stores its row position in {@code row_of_min_val} and column position in {@code col_of_min_val}.
   * <p>
   * <b>Note:</b> Unfortunately, the position variable must be of the mutable type int[].
   * 
   * @param index_of_min_val The row position storage
   * @param index_of_min_val The column position storage
   * 
   * @throws RuntimeException The matrix must have at least one element.
   */
  public double min(int[] row_of_min_val, int[] col_of_min_val) throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The matrix must have at least one element.");
    }

    double minimum = _data[0];
    row_of_min_val[0] = 0;
    col_of_min_val[0] = 0;

    int n = 0;
    for (int j = 1; j < n_cols; j++) {
      for (int i = 1; i < n_rows; i++) {
        double value = _data[n++];

        if (value < minimum) {
          minimum = value;
          row_of_min_val[0] = i;
          col_of_min_val[0] = j;
        }
      }
    }

    return minimum;
  }
  
  /**
   * Returns the largest value within the matrix and stores its row position in {@code row_of_min_val} and column position in {@code col_of_min_val}.
   * <p>
   * <b>Note:</b> Unfortunately, the position variable must be of the mutable type int[].
   * 
   * @param index_of_min_val The row position storage
   * @param index_of_min_val The column position storage
   * 
   * @throws RuntimeException The matrix must have at least one element.
   */
  public double max(int[] row_of_max_val, int[] col_of_max_val) throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The matrix must have at least one element.");
    }

    double maximum = _data[0];
    row_of_max_val[0] = 0;
    col_of_max_val[0] = 0;

    int n = 0;
    for (int j = 1; j < n_cols; j++) {
      for (int i = 1; i < n_rows; i++) {
        double value = _data[n++];

        if (value > maximum) {
          maximum = value;
          row_of_max_val[0] = i;
          col_of_max_val[0] = j;
        }
      }
    }

    return maximum;
  }
  
  /**
   * Resizes the matrix to the specified number of rows and columns and sets all elements to 1.
   */
  public void ones(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
    fill(1);
  }
  
  /**
   * Resizes the matrix to the specified number of rows and columns  and sets each element to a pseudo-random value drawn from the standard uniform distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   */
  public void randu(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextDouble();
    }
  }
  
  /**
   * Resizes the matrix to the specified number of rows and columns and sets each element to a pseudo-random value drawn from the standard normal distribution with mean 0.0 and standard deviation 1.0.
   */
  public void randn(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextGaussian();
    }
  }
  
  /**
   * Resizes the matrix to the specified number of rows and columns and sets all elements to 0.
   */
  public void zeros(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
  }
  
  /**
   * Resizes the matrix to the specified number of rows and columns and reuses existing values in a column-wise manner.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   */
  public void reshape(int n_rows, int n_cols) {
    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_rows, n_cols);
    System.arraycopy(temp, 0, _data, 0, temp.length);
  }

  /**
   * Resizes the matrix to the specified number of rows and columns and preserves existing values at the same position.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   */
  public void resize(int n_rows, int n_cols) {
    Mat temp = new Mat(this);
    set_size(n_rows, n_cols);
    
    int min_n_rows = Math.min(n_rows, temp.n_rows);
    int min_n_cols = Math.min(n_cols, temp.n_cols);
    
    for(int j = 0; j < min_n_cols; j++) {
      for(int i = 0; i < min_n_rows; i++) {
        _data[i + j * n_rows] = temp._data[i + j * temp.n_rows];
      }
    }
  }

  public void set_size(int n_rows, int n_cols) {
    if (n_rows < 0) {
      throw new NegativeArraySizeException("The specified number of rows (" + n_rows + ") must be positive.");
    }

    if (n_cols < 0) {
      throw new NegativeArraySizeException("The specified number of columns (" + n_cols + ") must be positive.");
    }

    if (n_rows != this.n_rows || n_cols != this.n_cols) {
      this.n_rows = n_rows;
      this.n_cols = n_cols;
      this.n_elem = n_rows * n_cols;

      _data = new double[this.n_elem];
    }
  }

  public void shed_row(int row_number) {
    Mat temp = new Mat(this);
    
    set_size(n_rows - 1, n_cols);
    rows(0, row_number - 1, Op.EQUAL, temp.rows(0, row_number - 1));
    // n_rows has been updated by set_size
    rows(row_number, n_rows - 1, Op.EQUAL, temp.rows(row_number + 1, temp.n_rows - 1));
  }

  public void shed_rows(int first_row, int last_row) {
    Mat temp = new Mat(this);
    
    set_size(n_rows - (last_row - first_row + 1), n_cols);
    rows(0, first_row - 1, Op.EQUAL, temp.rows(0, first_row - 1));
    // n_rows has been updated by set_size
    rows(first_row, n_rows - 1, Op.EQUAL, temp.rows(last_row + 1, temp.n_rows - 1));
  }

  public void shed_col(int column_number) {
    if (column_number < 0 || column_number > n_elem) {
      throw new IndexOutOfBoundsException("The row position (" + column_number + ") is out of bounds.");
    }

    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_rows, n_cols - 1);

    System.arraycopy(temp, 0, _data, 0, column_number * n_rows);
    System.arraycopy(temp, (column_number + 1) * n_rows, _data, 0, n_elem - (column_number + 1) * n_rows);
  }

  public void shed_cols(int first_column, int last_column) {
    double[] temp = Arrays.copyOf(_data, n_elem);
    set_size(n_rows, n_cols - (last_column - first_column + 1));

    System.arraycopy(temp, 0, _data, 0, first_column * n_rows);
    System.arraycopy(temp, (last_column + 1) * n_rows, _data, 0, n_elem - (last_column + 1) * n_rows);
  }

  @Override
  public Col col(int col_number) {
    int n = col_number * n_rows;
    return new Col(Arrays.copyOfRange(_data, n, n + n_rows));
  }

  @Override
  public Row row(int row_number) {
    return new Row(new ViewSubRow(this, row_number));
  }

  @Override
  public Mat cols(int first_col, int last_col) {
    Mat cols = new Mat(n_rows, last_col - first_col + 1);
    System.arraycopy(_data, first_col * n_rows, cols._data, 0, cols.n_elem);
    return cols;
  }

  @Override
  public Mat rows(int first_row, int last_row) {
    return new Mat(new ViewSubRows(this, first_row, last_row));
  }

  @Override
  public Col col(Span span, int col_number) {
    int n = col_number * n_rows;
    return new Col(Arrays.copyOfRange(_data, n, n + n_rows));
  }

  @Override
  public Row row(int row_number, Span span) {
    return new Row(new ViewSubRow(this, row_number, span._first, span._last));
  }

  @Override
  public Mat submat(int first_row, int first_col, int last_row, int last_col) {
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
  public Mat submat(Span row_span, Span col_span) {
    return new Mat(new ViewSubMat(this, row_span._first, row_span._last, col_span._first, col_span._last));
  }

  @Override
  public Mat submat(int first_row, int first_col, Size size) {
    return new Mat(new ViewSubMat(this, first_row, first_row + size.n_rows - 1, first_col, first_col + size.n_cols - 1));
  }

  @Override
  public Col elem(AbstractMat vector_of_indices) {
    return new Col(new ViewElemMat(this, vector_of_indices));
  }

  @Override
  public Mat cols(AbstractMat vector_of_column_indices) {
    return new Mat(new ViewElemCols(this, vector_of_column_indices));
  }

  @Override
  public Mat rows(AbstractMat vector_of_row_indices) {
    return new Mat(new ViewElemRows(this, vector_of_row_indices));
  }

  @Override
  public Mat submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices) {
    return new Mat(new ViewElemSubMat(this, vector_of_row_indices, vector_of_column_indices));
  }

  @Override
  public void swap(Mat X) {
    Mat temp = new Mat(this);

    set_size(X.n_rows, X.n_cols);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_rows, temp.n_cols);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(Col X) {
    if (n_cols > 1) {
      throw new RuntimeException("The content of column vectors can only be swaped with matrices that have at most one column.");
    }

    Mat temp = new Mat(this);

    set_size(X.n_elem, 1);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_elem);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public void swap(Row X) {
    if (n_rows > 1) {
      throw new RuntimeException("The content of row vectors can only be swaped with matrices that have at most one row.");
    }

    Mat temp = new Mat(this);

    set_size(X.n_elem, 1);
    System.arraycopy(X._data, 0, _data, 0, X.n_elem);

    X.set_size(temp.n_elem);
    System.arraycopy(temp._data, 0, X._data, 0, temp.n_elem);
  }

  @Override
  public Mat t() {
    Mat transpose = new Mat(n_cols, n_rows);

    int n = 0;
    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        transpose._data[i + j * n_rows] = _data[n++];
      }
    }

    return transpose;
  }

  @Override
  public void copy_size(AbstractMat A) {
    set_size(A.n_rows, A.n_cols);
  }

  @Override
  public Mat plus(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlacePlus(this, X);
    return result;
  }

  @Override
  public Mat plus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlacePlus(this, X);
    return result;
  }

  @Override
  public Mat minus(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceMinus(this, X);
    return result;
  }

  @Override
  public Mat minus(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceMinus(this, X);
    return result;
  }

  @Override
  public Mat elemDivide(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceElemDivide(this, X);
    return result;
  }

  @Override
  public Mat elemDivide(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceElemDivide(this, X);
    return result;
  }

  @Override
  public Mat times(double X) {
    return elemTimes(X);
  }

  @Override
  public Mat times(Col X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (1, m)-matrices can be left-hand side multiplied to column vectors.
     */
    return new Mat(new double[]{BLAS.getInstance().ddot(n_cols, _data, 1, X._data, 1)});
  }

  @Override
  public Mat times(Row X) throws RuntimeException {
    if (n_cols != X.n_rows) {
      throw new RuntimeException("The numbers of columns (" + n_cols + ") must be equal to the number of rows (" + X.n_rows + ") in the specified multiplier.");
    }

    /*
     * Only (n, 1)-matrices can be left-hand side multiplied to row vectors.
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

    Mat result = new Mat(n_rows, X.n_cols);
    BLAS.getInstance().dgemm("N", "N", n_rows, X.n_cols, n_cols, 1, _data, n_rows, X._data, X.n_rows, 0, result._data, n_rows);
    return result;
  }

  @Override
  public Mat elemTimes(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceElemTimes(this, X);
    return result;
  }

  @Override
  public Mat elemTimes(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceElemTimes(this, X);
    return result;
  }

  @Override
  public Mat equal(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceEqual(this, X);
    return result;
  }

  @Override
  public Mat equal(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceEqual(this, X);
    return result;
  }

  @Override
  public Mat nonEqual(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceNonEqual(this, X);
    return result;
  }

  @Override
  public Mat nonEqual(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceNonEqual(this, X);
    return result;
  }

  @Override
  public Mat greaterThan(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceGreaterThan(this, X);
    return result;
  }

  @Override
  public Mat greaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceGreaterThan(this, X);
    return result;
  }

  @Override
  public Mat lessThan(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceLessThan(this, X);
    return result;
  }

  @Override
  public Mat lessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceLessThan(this, X);
    return result;
  }

  @Override
  public Mat strictGreaterThan(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceStrictGreaterThan(this, X);
    return result;
  }

  @Override
  public Mat strictGreaterThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceStrictGreaterThan(this, X);
    return result;
  }

  @Override
  public Mat strictLessThan(double X) {
    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceStrictLessThan(this, X);
    return result;
  }

  @Override
  public Mat strictLessThan(AbstractMat X) throws RuntimeException {
    if (n_rows != X.n_rows || n_cols != X.n_cols) {
      throw new RuntimeException("Both operands must have the same size.");
    }

    Mat result = new Mat(n_rows, n_cols);
    result.inPlaceStrictLessThan(this, X);
    return result;
  }

}
