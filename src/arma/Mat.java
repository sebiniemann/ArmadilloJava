package arma;

import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.RandomMatrices;

/**
 * Mat is a dense matrix with interfaces similar to the Armadillo C++ Algebra Library by Conrad Sanderson et al., based
 * on DenseMatrix64F from Peter Abeles’ Efficient Java Matrix Library (EJML) Version 0.23 - 21.06.2013.
 * <p>
 * <b>Note:</b> Armadillo's Mat is stored by <a
 * href="https://en.wikipedia.org/wiki/Column_major#Column-major_order">column-major-ordering</a>, while EJML's
 * DenseMatrix64F is stored by <a href="http://en.wikipedia.org/wiki/Row-major_order">row-major ordering</a>. In order
 * to be similar to Armadillo, all interfaces work as if column-major-ordering were used and internally convert to
 * row-major ordering.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * @version 0.9
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient Java Matrix Library</a>
 */
public class Mat {

  /**
   * The internal data representation of the matrix.
   */
  private DenseMatrix64F _matrix;

  /**
   * The number of rows in the matrix.
   */
  public int             n_rows;

  /**
   * The number of columns in the matrix.
   */
  public int             n_cols;

  /**
   * The number of elements (same as {@link #n_rows} * {@link #n_cols}) in the matrix.
   */
  public int             n_elem;

  /**
   * Creates a new matrix by copying the provided one.
   * 
   * @param matrix The matrix to be copied.
   */
  Mat(DenseMatrix64F matrix) {
    _matrix = new DenseMatrix64F(matrix);

    updateAttributes();
  }

  /**
   * Creates a new matrix with {@link #n_elem} = 0.
   */
  public Mat() {
    this(new DenseMatrix64F());
  }

  /**
   * Creates a new matrix with the same dimensions and assignments as the provided two-dimensional array. The array is
   * assumed to have a structure of <code>array[rows][columns]</code>.
   * 
   * @param matrix The two-dimensional array to be converted into a matrix.
   */
  public Mat(double[][] matrix) {
    // Error-checking should be done in DenseMatrix64F
    this(new DenseMatrix64F(matrix));
  }

  /**
   * Creates a new matrix with {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code>.
   * 
   * @param numberOfRows The number of rows of the matrix to be created.
   * @param numberOfColumns The number of columns of the matrix to be created.
   */
  public Mat(int numberOfRows, int numberOfColumns) {
    // Error-checking should be done in DenseMatrix64F
    this(new DenseMatrix64F(numberOfRows, numberOfColumns));
  }

  /**
   * Creates a new matrix by copying the provided one.
   * 
   * @param matrix The matrix to be copied.
   */
  public Mat(Mat matrix) {
    this(new DenseMatrix64F(matrix.memptr()));
  }

  /**
   * Returns the value of the <i>n</i>th element of a column-major-ordered one-dimensional view of the matrix. <b>Note:</b>
   * {@link #at(int, int) at(i, j)} = {@link #at(int, int) at(i + j * n_cols)}.
   * 
   * @param n The position of the element.
   * @return The value of the <i>n</i>th element.
   * 
   * @see #at(int, int)
   */
  public double at(int n) {
    return _matrix.get(convertMajorOrdering(n));
  }

  /**
   * Returns the value of the element at the <i>n</i>th row and <i>j</i>th column.
   * 
   * @param i The row of the element.
   * @param j The column of the element.
   * @return The value of the element at the <i>n</i>th row and <i>j</i>th column.
   * 
   * @see #at(int)
   */
  public double at(int i, int j) {
    return _matrix.get(i, j);
  }

  /**
   * Performs a right-hand side operation on the value of the <i>n</i>th element of a column-major-ordered
   * one-dimensional view of the matrix. The value of the requested element will be overwritten by the result of the
   * operation. <b>Note:</b> {@link #at(int, int) at(i, j)} = {@link #at(int, int) at(i + j * n_cols)}.
   * 
   * @param n The position of the left-hand side operand.
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @see #at(int, int, Op, double)
   * @see #getResult(double, Op, double)
   */
  public void at(int n, Op operation, double operand) {
    _matrix.set(n, getResult(at(n), operation, operand));
  }

  /**
   * Performs a right-hand side operation on the value of the element at the <i>n</i>th row and <i>j</i>th column. The
   * value of the requested element will be overwritten by the result of the operation.
   * 
   * @param i The row of the left-hand side operand.
   * @param j The column of the left-hand side operand.
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @see #at(int, Op, double)
   * @see #getResult(double, Op, double)
   */
  public void at(int i, int j, Op operation, double operand) {
    _matrix.set(i, j, getResult(at(i, j), operation, operand));
  }

  /**
   * Return a copy of the <i>i</i>th row as a (1, {@link #n_cols}) matrix.
   * 
   * @param i The row to be copied.
   * @return The copied row.
   */
  public Mat row(int i) {
    DenseMatrix64F result = new DenseMatrix64F(1, n_cols);

    // n_cols = result.getNumElements()
    for (int n = 0; n < n_cols; n++) {
      result.set(n, at(i, n));
    }

    return new Mat(result);
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the <i>i</i>th row.
   * 
   * @param i The row of the left-hand side operands.
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException Thrown if the number of columns of the left-hand side does not match with the
   *           provided right-hand side operand.
   * 
   * @see #getResult(double, Op, double)
   */
  public void row(int i, Op operation, Mat operand) throws IllegalArgumentException {
    if (operand.n_cols != n_cols) {
      throw new IllegalArgumentException("The number of columns of the left-hand side operand (n_cols = " + n_cols
                                         + ") does not match with the provided right-hand side operand (n_cols = " + operand.n_cols + ").");
    }

    for (int n = 0; n < n_cols; n++) {
      at(i, n, operation, operand.at(n));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the <i>i</i>th row. The single provided
   * right-hand side operand is used for all operations.
   * 
   * @param i The row of the left-hand side operands.
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @see #getResult(double, Op, double)
   */
  public void row(int i, Op operation, double operand) {
    for (int n = 0; n < n_cols; n++) {
      at(i, n, operation, operand);
    }
  }

  /**
   * Return a copy of the <i>j</i>th column as a ({@link #n_rows}, 1) matrix.
   * 
   * @param j The column to be copied.
   * @return The copied column.
   */
  public Mat col(int j) {
    DenseMatrix64F result = new DenseMatrix64F(n_rows, 1);

    // n_rows = result.getNumElements()
    for (int n = 0; n < n_rows; n++) {
      result.set(n, _matrix.get(n, j));
    }

    return new Mat(result);
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the <i>j</i>th column.
   * 
   * @param j The column of the left-hand side operands.
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException Thrown if the number of rows of the left-hand side does not match with the
   *           provided right-hand side operand.
   * 
   * @see #getResult(double, Op, double)
   */
  public void col(int j, Op operation, Mat operand) throws IllegalArgumentException {
    if (operand.n_rows != n_rows) {
      throw new IllegalArgumentException("The number of rows of the left-hand side operand (n_rows = " + n_rows
                                         + ") does not match with the provided right-hand side operand (n_rows = " + operand.n_rows + ").");
    }

    for (int i = 0; i < n_rows; i++) {
      at(i, j, operation, operand.at(i));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the <i>j</i>th column. The single provided
   * right-hand side operand is used for all operations.
   * 
   * @param j The column of the left-hand side operands.
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @see #getResult(double, Op, double)
   */
  public void col(int j, Op operation, double operand) {
    for (int n = 0; n < n_rows; n++) {
      at(n, j, operation, operand);
    }
  }

  /**
   * Returns a copy of the main diagonal as a (<code>Math.min</code>({@link #n_rows}, {@link #n_cols}), 1) matrix.
   * 
   * @return The copy of the main diagonal.
   */
  public Mat diag() {
    DenseMatrix64F result = new DenseMatrix64F(Math.min(n_rows, n_cols), 1);
    CommonOps.extractDiag(_matrix, result);
    return new Mat(result);
  }

  /**
   * Performs a right-hand side element-wise operation on all elements on the main diagonal.
   * 
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException Thrown if the number of elements on main diagonal does not match with the provided
   *           right-hand side operand.
   */
  public void diag(Op operation, Mat operand) throws IllegalArgumentException {
    int length = Math.min(n_cols, n_rows);
    if (operand.n_elem != length) {
      throw new IllegalArgumentException("The number of elements on main diagonal (n_elem = " + length
                                         + ") does not match with the provided right-hand side operand (n_elem = " + operand.n_elem + ").");
    }

    for (int i = 0; i < length; i++) {
      at(i, i, operation, operand.at(i));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements on the main diagonal. The single provided
   * right-hand side operand is used for all operations.
   * 
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   */
  public void diag(Op operation, double operand) {
    int length = Math.min(n_cols, n_rows);
    for (int i = 0; i < length; i++) {
      at(i, i, operation, operand);
    }
  }

  /**
   * Performs the provided right-hand side element-wise operation on each column.
   * 
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @see #col(int, Op, Mat)
   */
  public void each_col(Op operation, Mat operand) {
    // Error-checking should be done in col(int, Op, Mat)
    for (int j = 0; j < n_cols; j++) {
      col(j, operation, operand);
    }
  }

  /**
   * Performs the provided right-hand side element-wise operation on each column. The single provided right-hand side
   * operand is used for all operations.
   * 
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @see #col(int, Op, double)
   */
  public void each_col(Op operation, double operand) {
    // Error-checking should be done in col(int, Op, double)
    for (int j = 0; j < n_cols; j++) {
      col(j, operation, operand);
    }
  }

  /**
   * Performs the provided right-hand side element-wise operation on each row.
   * 
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @see #row(int, Op, Mat)
   */
  public void each_row(Op operation, Mat operand) {
    // Error-checking should be done in row(int, Op, Mat)
    for (int i = 0; i < n_rows; i++) {
      row(i, operation, operand);
    }
  }

  /**
   * Performs the provided right-hand side element-wise operation on each row. The single provided right-hand side
   * operand is used for all operations.
   * 
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   * 
   * @see #row(int, Op, double)
   */
  public void each_row(Op operation, double operand) {
    // Error-checking should be done in row(int, Op, double)
    for (int i = 0; i < n_rows; i++) {
      row(i, operation, operand);
    }
  }

  /**
   * Returns a copy of all elements for which selection.at(n) > 0 holds as a ({@link Arma#accu(Mat) Arma.accu}(
   * {@link Arma#find(Mat, Op, double) Arma.find(selection, Op.STRICT_GREATER, 0)}), 1) matrix.
   * 
   * @param selection The selection to be used.
   * @return The copy of the selected elements.
   * 
   * @throws IllegalArgumentException Thrown if the number of elements of the matrix does not match with the provided
   *           selection.
   */
  public Mat elem(Mat selection) throws IllegalArgumentException {
    if (selection.n_elem != n_elem) {
      throw new IllegalArgumentException("The number of elements of the matrix (n_elem = " + n_elem
                                         + ") does not match with the provided selection (n_elem = " + selection.n_elem + ").");
    }

    DenseMatrix64F result = new DenseMatrix64F(n_elem, 1);
    int length = 0;
    for (int i = 0; i < n_elem; i++) {
      if (selection.at(i) > 0) {
        result.set(length, _matrix.get(i));
        length++;
      }
    }

    // Saves current values of the elements because length <= n_elem
    result.reshape(length, 1);
    return new Mat(result);
  }

  /**
   * Performs the provided right-hand side element-wise operation on all elements for which selection.at(n) > 0 holds.
   * 
   * @param selection The selection to be used.
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   */
  public void elem(Mat selection, Op operation, Mat operand) {
    int index = 0;
    for (int i = 0; i < selection.n_elem; i++) {
      if (selection.at(i) > 0) {
        at(i, operation, operand.at(index));
        index++;
      }
    }
  }

  /**
   * Performs the provided right-hand side element-wise operation on all elements for which selection.at(n) > 0 holds.
   * The single provided right-hand side value is used for all operations.
   * 
   * @param selection The selection to be used.
   * @param operation The operation to be performed. See {@link #getResult(double, Op, double)} for more details.
   * @param operand The right-hand side operand.
   */
  public void elem(Mat selection, Op operation, double operand) {
    for (int i = 0; i < selection.n_elem; i++) {
      if (selection.at(i) > 0) {
        at(i, operation, operand);
      }
    }
  }

  /**
   * Replaces the value of all elements with the provided one.
   * 
   * @param value The new value of all elements.
   */
  public void fill(double value) {
    CommonOps.fill(_matrix, value);
  }

  /**
   * Returns a reference to the internal data representation of the matrix.
   * 
   * @return The reference to the internal data representation of the matrix.
   */
  public DenseMatrix64F memptr() {
    return _matrix;
  }

  /**
   * Returns the transpose of the matrix.
   * 
   * @return The transpose.
   */
  public Mat t() {
    DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
    CommonOps.transpose(_matrix, result);
    return new Mat(result);
  }

  /**
   * Returns the inverse matrix. Fails if the provided matrix is not invertible.
   * 
   * @return The inverse.
   */
  public Mat i() {
    DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
    // Error-checking should be done in CommonOps.invert(DenseMatrix64F, DenseMatrix64F)
    CommonOps.invert(_matrix, result);
    return new Mat(result);
  }

  /**
   * Changes the dimension of the matrix to {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code> and neither guarantees to reuse the values of the elements, nor their positions.
   * 
   * @param numberOfRows The new number of rows of the matrix.
   * @param numberOfColumns The new number of columns of the matrix.
   * 
   * @see #reshape(int, int, boolean)
   * @see #resize
   */
  public void set_size(int numberOfRows, int numberOfColumns) {
    _matrix.reshape(numberOfRows, numberOfColumns);
    updateAttributes();
  }

  /**
   * Changes the dimension of the matrix to {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code> and guarantees to reuses the values of the elements, but not their positions. The
   * elements of the current matrix are either accessed row-wise (<code>rowWise = true</code>) or column-wise (
   * <code>rowWise = false</code>), while the new matrix is always filled column-wise.
   * 
   * @param numberOfRows The new number of rows of the matrix.
   * @param numberOfColumns The new number of columns of the matrix.
   * @param rowWise The access of the elements of the current matrix .
   * 
   * @see #set_size(int, int)
   * @see #resize(int, int)
   */
  public void reshape(int numberOfRows, int numberOfColumns, boolean rowWise) {
    DenseMatrix64F temp = new DenseMatrix64F(n_rows, n_cols);

    if (rowWise) {
      // reshape fills the new matrix row-wise and not column-wise. Filling a transposed matrix row-wise and transposing
      // it again afterwards will result in a column-wise filled matrix.
      _matrix.reshape(numberOfColumns, numberOfRows, true);
      CommonOps.transpose(_matrix, temp);
      _matrix = temp;
    } else {
      // same as above with a transpose of the current matrix in order to access it column-wise as a result.
      CommonOps.transpose(_matrix, temp);
      temp.reshape(numberOfColumns, numberOfRows, true);
      CommonOps.transpose(temp, _matrix);
    }

    updateAttributes();
  }

  /**
   * Equivalent to {@link #reshape(int, int, boolean) reshape(numberOfRows, numberOfColumns, true)}
   * 
   * @param numberOfRows The new number of rows of the matrix.
   * @param numberOfColumns The new number of columns of the matrix.
   */
  public void reshape(int numberOfRows, int numberOfColumns) {
    reshape(numberOfRows, numberOfColumns, false);
  }

  /**
   * Changes the dimension of the matrix to {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code> and guarantees to reuses the values of the elements and their positions.
   * 
   * @param numberOfRows The new number of rows of the matrix.
   * @param numberOfColumns The new number of columns of the matrix.
   * 
   * @see #set_size(int, int)
   * @see #reshape(int, int, boolean)
   */
  public void resize(int numberOfRows, int numberOfColumns) {
    if (numberOfRows <= n_rows && numberOfColumns <= n_cols) {
      // No additional memory needs to be allocated.
      _matrix.reshape(numberOfColumns, numberOfRows);
    } else {
      DenseMatrix64F newMatrix = new DenseMatrix64F(numberOfRows, numberOfColumns);
      for (int i = 0; i < n_rows; i++) {
        for (int j = 0; j < n_cols; j++) {
          newMatrix.set(i, j, _matrix.get(i, j));
        }
      }
      _matrix = newMatrix;
    }

    updateAttributes();
  }

  @Override
  public String toString() {
    return _matrix.toString();
  }

  /**
   * Creates a new zero matrix with {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code>.
   * 
   * @param numberOfRows The number of rows of the matrix to be created.
   * @param numberOfColumns The number of columns of the matrix to be created.
   * @return The created matrix.
   */
  public static Mat zeros(int numberOfRows, int numberOfColumns) {
    return new Mat(new DenseMatrix64F(numberOfRows, numberOfColumns));
  }

  /**
   * Creates a new matrix of ones with {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code>.
   * 
   * @param numberOfRows The number of rows of the matrix to be created.
   * @param numberOfColumns The number of columns of the matrix to be created.
   * @return The created matrix.
   */
  public static Mat ones(int numberOfRows, int numberOfColumns) {
    DenseMatrix64F result = new DenseMatrix64F(numberOfRows, numberOfColumns);
    CommonOps.fill(result, 1);
    return new Mat(result);
  }

  /**
   * Creates a new identity matrix with {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code>.
   * 
   * @param numberOfRows The number of rows of the matrix to be created.
   * @param numberOfColumns The number of columns of the matrix to be created.
   * @return The created matrix.
   */
  public static Mat eye(int numberOfRows, int numberOfColumns) {
    return new Mat(CommonOps.identity(numberOfRows, numberOfColumns));
  }

  /**
   * Creates a new matrix with {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code> and normally distributed pseudorandom values.
   * 
   * @param numberOfRows The number of rows of the matrix to be created.
   * @param numberOfColumns The number of columns of the matrix to be created.
   * @param rng The pseudorandom generator to be used.
   * @return The created matrix.
   */
  public static Mat randn(int numberOfRows, int numberOfColumns, Random rng) {
    DenseMatrix64F result = new DenseMatrix64F(numberOfRows, numberOfColumns);

    int numberOfElements = result.getNumElements();
    for (int i = 0; i < numberOfElements; i++) {
      result.set(i, rng.nextGaussian());
    }

    return new Mat(result);
  }

  /**
   * Creates a new matrix with {@link #n_rows} = <code>numberOfRows</code> and {@link #n_cols} =
   * <code>numberOfColumns</code> and uniformly distributed pseudorandom values.
   * 
   * @param numberOfRows The number of rows of the matrix to be created.
   * @param numberOfColumns The number of columns of the matrix to be created.
   * @param rng The pseudorandom generator to be used.
   * @return The created matrix.
   */
  public static Mat randu(int numberOfRows, int numberOfColumns, Random rng) {
    return new Mat(RandomMatrices.createRandom(numberOfRows, numberOfColumns, rng));
  }

  /**
   * Creates a new matrix being the sum of a right-hand side (always element-wise) summation with the provided operand.
   * The single provided right-hand side value is used for all operations.
   * 
   * @param operand The right-hand side operand.
   * @return The created matrix.
   */
  public Mat plus(double operand) {
    DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
    CommonOps.add(_matrix, operand, result);
    return new Mat(result);
  }

  /**
   * Creates a new matrix being the sum of a right-hand side (always element-wise) summation with the provided
   * right-hand side addend.
   * 
   * @param operand The right-hand side addend.
   * @return The created matrix.
   */
  public Mat plus(Mat operand) {
    DenseMatrix64F result = new DenseMatrix64F(operand.n_rows, operand.n_cols);
    CommonOps.add(_matrix, operand.memptr(), result);
    return new Mat(result);
  }

  /**
   * Creates a new matrix being the difference of a right-hand side (always element-wise) subtraction with the provided
   * subtrahend. The single provided right-hand side value is used for all operations.
   * 
   * @param operand The subtrahend.
   * @return The created matrix.
   */
  public Mat minus(double operand) {
    DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
    // Applies subtraction by addition with the additive inverse value.
    CommonOps.add(_matrix, -operand, result);
    return new Mat(result);
  }

  /**
   * Creates a new matrix being the difference of a right-hand side (always element-wise) subtraction with the provided
   * subtrahend.
   * 
   * @param operand The subtrahend.
   * @return The created matrix.
   */
  public Mat minus(Mat operand) {
    DenseMatrix64F result = new DenseMatrix64F(operand.n_rows, operand.n_cols);
    CommonOps.sub(_matrix, operand.memptr(), result);
    return new Mat(result);
  }

  /**
   * Creates a new matrix being the product of a right-hand side (element-wise) multiplication with the provided
   * multiplier. The single provided right-hand side value is used for all operations.
   * 
   * @param operand The multiplier.
   * @return The created matrix.
   * 
   * @see #elemTimes(double)
   */
  public Mat times(double operand) {
    return elemTimes(operand);
  }

  /**
   * Creates a new matrix being the product of a right-hand side matrix multiplication with the provided multiplier.
   * 
   * @param operand The multiplier.
   * @return The created matrix.
   */
  public Mat times(Mat operand) {
    DenseMatrix64F result = new DenseMatrix64F(operand.n_rows, operand.n_cols);
    CommonOps.mult(_matrix, operand.memptr(), result);
    return new Mat(result);
  }

  /**
   * Creates a new matrix being the product of a right-hand side (element-wise) multiplication with the provided
   * multiplier. The single provided right-hand side value is used for all operations.
   * 
   * @param operand The multiplier.
   * @return The created matrix.
   * 
   * @see #times(double)
   */
  public Mat elemTimes(double operand) {
    DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
    CommonOps.scale(operand, _matrix, result);
    return new Mat(result);
  }

  /**
   * Creates a new matrix being the product of a right-hand side (element-wise) multiplication with the provided
   * multiplier.
   * 
   * @param operand The multiplier.
   * @return The created matrix.
   */
  public Mat elemTimes(Mat operand) {
    DenseMatrix64F result = new DenseMatrix64F(operand.n_rows, operand.n_cols);
    CommonOps.elementMult(_matrix, operand.memptr(), result);
    return new Mat(result);
  }

  /**
   * Creates a new matrix being the quotient of a right-hand side (element-wise) division with the provided divisor. The
   * single provided right-hand side value is used for all operations.
   * 
   * @param operand The divisor.
   * @return The created matrix.
   * 
   * @see #elemDivide(double)
   */
  public Mat divide(double operand) {
    return elemDivide(operand);
  }

  /**
   * Creates a new matrix being the quotient of a right-hand side matrix division with the provided divisor. Fails if
   * the provided divisor is not invertible.
   * 
   * @param operand The divisor.
   * @return The created matrix.
   */
  public Mat divide(Mat operand) {
    DenseMatrix64F result = new DenseMatrix64F(operand.n_rows, operand.n_cols);
    DenseMatrix64F inverse = new DenseMatrix64F(operand.n_rows, operand.n_cols);

    // Error-checking should be done in CommonOps.invert(DenseMatrix64F, DenseMatrix64F)
    CommonOps.invert(operand.memptr(), inverse);
    CommonOps.mult(_matrix, inverse, result);

    return new Mat(result);
  }

  /**
   * Creates a new matrix being the quotient of a right-hand side (element-wise) division with the provided divisor. The
   * single provided right-hand side value is used for all operations.
   * 
   * @param operand The divisor.
   * @return The created matrix.
   * 
   * @see #divide(double)
   */
  public Mat elemDivide(double operand) {
    DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
    CommonOps.divide(operand, _matrix, result);
    return new Mat(result);
  }

  /**
   * Creates a new matrix being the quotient of a right-hand side (element-wise) division with the provided divisor.
   * 
   * @param operand The divisor.
   * @return The created matrix.
   */
  public Mat elemDivide(Mat operand) {
    DenseMatrix64F result = new DenseMatrix64F(operand.n_rows, operand.n_cols);
    CommonOps.elementDiv(_matrix, operand.memptr(), result);
    return new Mat(result);
  }

  /**
   * Performs the requested binary arithmetic operation on <code>a</code> and <code>b</code> or sets <code>a</code> to
   * the value of <code>b</code> if <code>operation = {@link Op#EQUAL}</code>.
   * 
   * @param a The left-hand side operand.
   * @param operation The operation to be performed. Only arithmetic operators and equality are supported.
   * @param b The right-hand side operand.
   * @return The result.
   * 
   * @throws UnsupportedOperationException Thrown if another operation besides arithmetic operators or equality is
   *           requested.
   *           
   * @see Op
   */
  private static double getResult(double a, Op operation, double b) throws UnsupportedOperationException {
    double result = 0;

    switch (operation) {
      case EQUAL:
        result = b;
        break;
      case PLUS:
        result = a + b;
        break;
      case MINUS:
        result = a - b;
        break;
      case TIMES:
      case ELEMTIMES:
        result = a * b;
        break;
      case DIVIDE:
      case ELEMDIVIDE:
        result = a / b;
        break;
      default:
        throw new UnsupportedOperationException("Only arithmetic operators and equality are supported.");
    }

    return result;
  }

  /**
   * Updates the attributes {@link #n_rows}, {@link #n_cols} and {@link #n_elem} of the matrix. Should be called right
   * after the shape of the matrix is changed.
   */
  private void updateAttributes() {
    n_rows = _matrix.numRows;
    n_cols = _matrix.numCols;
    n_elem = n_rows * n_cols;
  }

  /**
   * Converts the position of an element based on a column-major-ordered one-dimensional view of the matrix into a
   * row-major-ordered based one, in order to provide interfaces similar to Armadillo's Mat while using EJML's
   * DenseMatrix64F.
   * 
   * @param n The position based on column-major-ordering.
   * @return The position based on row-major-ordering.
   * 
   * @throws IllegalArgumentException Thrown if a negative value is provided for the position.
   */
  private int convertMajorOrdering(int n) throws IllegalArgumentException {
    if (n < 0) {
      throw new IllegalArgumentException("The value of the provided position must be non-negative.");
    }

    /*
     * n = i + j * n_cols, i < n_cols
     * 
     * j = (n - i) / n_cols j = Math.floor(n / n_cols)
     * 
     * i = n - j * n_cols i = n % n_cols
     */
    return (n / n_cols) * n_rows + n % n_cols;
  }
}