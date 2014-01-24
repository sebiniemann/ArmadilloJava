package org.armadillojava;

import com.github.fommil.netlib.BLAS;

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
      case ZEROS:
        zeros(n_rows, n_cols);
        break;
      case ONES:
        ones(n_rows, n_cols);
        break;
      case RANDU:
        randu(n_rows, n_cols);
        break;
      case RANDN:
        randn(n_rows, n_cols);
      default:
        throw new RuntimeException("The fill type (" + fill_type + ") is not supported for column vectors.");
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

  protected Mat(AbstractView view) {
    copy_size(view);

    view.iteratorReset();
    for (int n = 1; n < n_elem; n++) {
      _data[n] = view._data[view.iteratorNext()];
    }
  }

  public Mat diag() {
    return new Mat(new ViewDiag(this, 0));
  }

  public void diag(Op unary_operator) {
    new Mat(new ViewDiag(this, 0)).inPlace(unary_operator);
  }

  public void diag(Op binary_operator, double operand) {
    new Mat(new ViewDiag(this, 0)).inPlace(binary_operator, operand);
  }

  public void diag(Op binary_operator, AbstractMat operand) {
    new Mat(new ViewDiag(this, 0)).inPlace(binary_operator, operand);
  }

  public Mat diag(int k) {
    if (k > 0 && k >= n_cols) {
      throw new IndexOutOfBoundsException("The diagonal index (" + k + ") is out of bounds.");
    }
    
    if (k < 0 && -k <= n_rows) {
      throw new IndexOutOfBoundsException("The diagonal index (" + k + ") is out of bounds.");
    }

    return new Mat(new ViewDiag(this, k));
  }

  public void diag(int k, Op unary_operator) {
    new Mat(new ViewDiag(this, k)).inPlace(unary_operator);
  }

  public void diag(int k, Op binary_operator, double operand) {
    new Mat(new ViewDiag(this, k)).inPlace(binary_operator, operand);
  }

  public void diag(int k, Op binary_operator, AbstractMat operand) {
    new Mat(new ViewDiag(this, k)).inPlace(binary_operator, operand);
  }

  public void each_col(Op unary_operator) {

  }

  public void each_col(Op binary_operator, double operand) {

  }

  public void each_col(Op binary_operator, AbstractMat operand) {

  }

  public void each_col(AbstractMat vector_of_indices, Op unary_operator) {

  }

  public void each_col(AbstractMat vector_of_indices, Op binary_operator, double operand) {

  }

  public void each_col(AbstractMat vector_of_indices, Op binary_operator, AbstractMat operand) {

  }

  public void each_row(Op unary_operator) {

  }

  public void each_row(Op binary_operator, double operand) {

  }

  public void each_row(Op binary_operator, AbstractMat operand) {

  }

  public void each_row(AbstractMat vector_of_indices, Op unary_operator) {

  }

  public void each_row(AbstractMat vector_of_indices, Op binary_operator, double operand) {

  }

  public void each_row(AbstractMat vector_of_indices, Op binary_operator, AbstractMat operand) {

  }

  public double at(int i, int j) {
    return _data[i + j * n_rows];
  }

  public void eye() {

  }

  public void eye(int n_rows, int n_cols) {

  }

  public Mat i() {

  }

  public boolean is_square() {
    return (n_rows == n_cols);
  }

  public boolean is_vec() {
    return (is_colvec() || is_rowvec());
  }

  public boolean is_colvec() {
    return (n_cols == 1);
  }

  public boolean is_rowvec() {
    return (n_rows == 1);
  }

  public void insert_rows(int row_number, AbstractMat X) {

  }

  public void insert_rows(int row_number, int number_of_rows) {

  }

  public void insert_rows(int row_number, int number_of_rows, boolean set_to_zero) {

  }

  public void insert_cols(int row_number, AbstractMat X) {

  }

  public void insert_cols(int row_number, int number_of_rows) {

  }

  public void insert_cols(int row_number, int number_of_rows, boolean set_to_zero) {

  }

  public double min(int[] row_of_min_val, int[] col_of_min_val) {

  }

  public double max(int[] row_of_max_val, int[] col_of_max_val) {

  }

  public void ones(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
    fill(1);
  }

  public void randu(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextDouble();
    }
  }
  
  public void randn(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextGaussian();
    }
  }

  public void zeros(int n_rows, int n_cols) {
    set_size(n_rows, n_cols);
  }

  public void reshape(int n_rows, int n_cols) {

  }

  public void resize(int n_rows, int n_cols) {

  }

  public void set_size(int n_rows, int n_cols) {

  }

  public void shed_row(int row_number) {

  }

  public void shed_rows(int first_row, int last_row) {

  }

  public void shed_col(int column_number) {

  }

  public void shed_cols(int first_column, int last_column) {

  }

  @Override
  public Col col(int col_number) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Row row(int row_number) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat cols(int first_col, int last_col) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat rows(int first_row, int last_row) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Col col(Span span, int col_number) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Row row(int row_number, Span span) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat submat(int first_row, int first_col, int last_row, int last_col) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat submat(Span row_span, Span col_span) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat submat(int first_row, int first_col, Size size) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Col elem(AbstractMat vector_of_indices) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat cols(AbstractMat vector_of_column_indices) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat rows(AbstractMat vector_of_row_indices) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mat submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void swap(Mat X) {
    // TODO Auto-generated method stub

  }

  @Override
  public void swap(Col X) {
    // TODO Auto-generated method stub

  }

  @Override
  public void swap(Row X) {
    // TODO Auto-generated method stub

  }

  @Override
  public AbstractMat t() {
    // TODO Auto-generated method stub
    return null;
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
