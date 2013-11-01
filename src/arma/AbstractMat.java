package arma;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;

/**
 * Provides a real-valued dense matrix with double precision. Member functions as well as attributes are similar to the
 * Armadillo C++ Algebra Library (Armadillo) by Conrad Sanderson et al., based on DenseMatrix64F from Peter Abeles'
 * Efficient Java Matrix Library (EJML) Version 0.23 from 21.06.2013.
 * <p>
 * See also the <a href="https://github.com/SebastianNiemann/ArmadilloJava/blob/master/CONVERSION.md">syntax conversion
 * table</a>.
 * <p>
 * If not stated otherwise (marked as non-canonical in case), the provided interfaces is identical to Armadillo (e.g.
 * same ordering of arguments, accepted values, ...). However, this project is based on EJML to provide a pure Java
 * solution, which is why numeric results may slightly differ from the Armadillo C++ Algebra Library.
 * <p>
 * <b>Iteration:</b> The iteration should be reset at the beginning <code>
 * Subview A;
 * A.iteratorReset();
 * </code>
 * 
 * An example of useful loops: <b>While-loop:</b> <code>
 * while (iteratorHastNext()) { 
 * Something(_matrix[A.iteratorNext()]);
 * }
 * </code>
 * 
 * <b>For-loop over n_elem</b> <code>
 * for (int n = 0; n < A.n_elem; n++) {
 * Something(A._matrix[A.iteratorNext()]);
 * Something(A._matrix[A.getElementIndex(n)]); // The same
 * Something(A.at(n)); // The same
 * }
 * </code>
 * 
 * <b>For-loop over n_cols and n_rows</b> <code>
 * for (int j = 0; j < A.n_cols; j++) {
 *  for (int i = 0; i < A.n_rows; i++) {
 *    Something(A._matrix[A.iteratorNext()]);
 *    Something(A._matrix[A.getElementIndex(i, j)]); // Only the same if the order of both loops is unchanged
 *    Something(A.at(i, j)); // Only the same if the order of both loops is unchanged
 *  }
 * }
 * </code>
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
abstract class AbstractMat implements Iterable<Double> {

  /**
   * The underlying matrix
   */
  AbstractMat        _underlyingMatrix;

  /**
   * The internal data representation of the matrix
   */
  protected double[] _matrix;

  /**
   * The number of rows
   */
  public int         n_rows;

  /**
   * The number of columns
   */
  public int         n_cols;

  /**
   * The number of elements (same as {@link #n_rows} * {@link #n_cols}) .
   */
  public int         n_elem;

  /**
   * The counter
   */
  protected int      _count;

  /**
   * Resets the internal position counter
   */
  protected void iteratorReset() {
    _count = 0;
  }

  /**
   * Returns true if iterator position has yet to reach the last position.
   * 
   * @return Wheter the iterator position has reached its last position.
   */
  protected final boolean iteratorHasNext() {
    return (_count < n_elem);
  }

  /**
   * Returns the next iterator position.
   * 
   * @return The iterator position
   */
  protected int iteratorNext() {
    return _count++;
  }

  /**
   * Converts the pair of a row and column index that is relative to this subview, into the corresponding index for the
   * underlying matrix.
   * 
   * @param i The row index
   * @param j The column index
   * @return The corresponding element index
   */
  abstract protected int getElementIndex(int i, int j);

  /**
   * Converts an element index that is relative to this subview, into the corresponding index for the underlying matrix.
   * 
   * @param n The relative element index
   * @return The corresponding element index
   */
  abstract protected int getElementIndex(int n);

  /**
   * Returns the value of the element in the {@code i}th row and {@code j}th column.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param i The row
   * @param j The column
   * @return The value
   */
  public double at(int i, int j) {
    return at(getElementIndex(i, j));
  }

  /**
   * Performs a unary right-hand side inplace operation on the element in the {@code i}th row and {@code j}th column.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param i The row
   * @param j The column
   * @param operator The operator
   */
  public void at(int i, int j, Op operator) {
    at(getElementIndex(i, j), operator);
  }

  /**
   * Performs a binary right-hand side inplace operation on the element in the {@code i}th row and {@code j}th column.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param i The row
   * @param j The column
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void at(int i, int j, Op operator, double operand) {
    at(getElementIndex(i, j), operator, operand);
  }

  /**
   * Returns the value of the {@code n}th element.
   * <p>
   * <b>Note:</b> The one-dimensional view is based on column-major-ordering: {@code at(i, j) = at(i + j * n_rows)}.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param n The position
   * @return The value
   */
  public double at(int n) {
    isElementOutOfBoundsDetection(n);

    return _matrix[getElementIndex(n)];
  }

  /**
   * Performs a unary right-hand side inplace operation on the {@code n}th element.
   * <p>
   * <b>Note:</b> The one-dimensional view is based on column-major-ordering: {@link #at(int, int) at(i, j)}
   * {@code = at(i + j * n_rows)}.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param n The position
   * @param operator The operator
   */
  public void at(int n, Op operator) {
    isElementOutOfBoundsDetection(n);

    inplace(n, operator);
  }

  /**
   * Performs a binary right-hand side inplace operation on the {@code n}th element.
   * <p>
   * <b>Note:</b> The one-dimensional view is based on column-major-ordering: {@link #at(int, int) at(i, j)}
   * {@code = at(i + j * n_rows)}.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param n The position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void at(int n, Op operator, double operand) {
    isElementOutOfBoundsDetection(n);

    inplace(n, operator, operand);
  }

  /**
   * Returns a shallow copy of the {@code j}th column.
   * 
   * @param j The column index
   * @return The submatrix
   */
  protected SubMat colInternal(int j) {
    return new SubMat(this, 0, n_rows - 1, j, j);
  }

  /**
   * Returns a deep copy of the {@code j}th column.
   * 
   * @param j The column index
   * @return The submatrix
   */
  public Mat col(int j) {
    return new Mat(colInternal(j));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code j}th column.
   * 
   * @param j The column index
   * @param operator The operator
   */
  public void col(int j, Op operator) {
    colInternal(j).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column.
   * 
   * @param j The column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int j, Op operator, AbstractMat operand) {
    colInternal(j).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column.
   * 
   * @param j The column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int j, Op operator, double operand) {
    colInternal(j).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code j}th column and {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @return The submatrix
   */
  protected SubMat colInternal(int a, int b, int j) {
    return new SubMat(this, a, b, j, j);
  }

  /**
   * Returns a deep copy of all elements in the {@code j}th column and {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @return The submatrix
   */
  public Mat col(int a, int b, int j) {
    return new Mat(colInternal(a, b, j));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @param operator The operator
   */
  public void col(int a, int b, int j, Op operator) {
    colInternal(a, b, j).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int a, int b, int j, Op operator, AbstractMat operand) {
    colInternal(a, b, j).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int a, int b, int j, Op operator, double operand) {
    colInternal(a, b, j).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of the {@code i}th row.
   * 
   * @param i The row index
   * @return The submatrix
   */
  protected SubMat rowInternal(int i) {
    return new SubMat(this, i, i, 0, n_cols - 1);
  }

  /**
   * Returns a deep copy of the {@code i}th row.
   * 
   * @param i The row index
   * @return The submatrix
   */
  public Mat row(int i) {
    return new Mat(rowInternal(i));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row index
   * @param operator The operator
   */
  public void row(int i, Op operator) {
    rowInternal(i).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int i, Op operator, AbstractMat operand) {
    rowInternal(i).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int i, Op operator, double operand) {
    rowInternal(i).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code i}th row and {@code a}th to {@code b}th column.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param i The column index
   * @return The submatrix
   */
  protected SubMat rowInternal(int a, int b, int i) {
    return new SubMat(this, i, i, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code i}th row and {@code a}th to {@code b}th column.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param i The column index
   * @return The submatrix
   */
  public Mat row(int a, int b, int i) {
    return new Mat(rowInternal(a, b, i));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param i The row index
   * @param operator The operator
   */
  public void row(int a, int b, int i, Op operator) {
    rowInternal(a, b, i).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param i The row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int a, int b, int i, Op operator, AbstractMat operand) {
    rowInternal(a, b, i).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param i The row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int a, int b, int i, Op operator, double operand) {
    rowInternal(a, b, i).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @return The submatrix
   */
  protected SubMat colsInternal(int a, int b) {
    return new SubMat(this, 0, n_rows - 1, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @return The submatrix
   */
  public Mat cols(int a, int b) {
    return new Mat(colsInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param operator The operator
   */
  public void cols(int a, int b, Op operator) {
    colsInternal(a, b).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(int a, int b, Op operator, AbstractMat operand) {
    colsInternal(a, b).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(int a, int b, Op operator, double operand) {
    colsInternal(a, b).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @return The submatrix
   */
  protected SubMat rowsInternal(int a, int b) {
    return new SubMat(this, a, b, 0, n_cols - 1);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @return The submatrix
   */
  public Mat rows(int a, int b) {
    return new Mat(rowsInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param operator The operator
   */
  public void rows(int a, int b, Op operator) {
    rowsInternal(a, b).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(int a, int b, Op operator, AbstractMat operand) {
    rowsInternal(a, b).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(int a, int b, Op operator, double operand) {
    rowsInternal(a, b).inplace(operator, operand);
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   */
  public void submat(Op operator) {
    inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(Op operator, AbstractMat operand) {
    inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(Op operator, double operand) {
    inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code ai}th to {@code bi}th row and {@code aj}th to {@code bj}th
   * column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @return The submatrix
   */
  protected SubMat submatInternal(int ai, int bi, int aj, int bj) {
    return new SubMat(this, ai, bi, aj, bj);
  }

  /**
   * Returns a deep copy of all elements in the {@code ai}th to {@code bi}th row and {@code aj}th to {@code bj}th
   * column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @return The submatrix
   */
  public Mat submat(int ai, int bi, int aj, int bj) {
    return new Mat(submatInternal(ai, bi, aj, bj));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code ai}th to {@code bi}
   * th
   * row and {@code aj}th to {@code bj}th column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @param operator The operator
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator) {
    submatInternal(ai, bi, aj, bj).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code ai}th to {@code bi}
   * th row and {@code aj}th to {@code bj}th column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator, AbstractMat operand) {
    submatInternal(ai, bi, aj, bj).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code ai}th to {@code bi}
   * th row and {@code aj}th to {@code bj}th column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator, double operand) {
    submatInternal(ai, bi, aj, bj).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @return The submatrix
   */
  protected SubMat subvecInternal(int a, int b) {
    return new SubMat(this, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @return The submatrix
   */
  public Mat subvec(int a, int b) {
    return new Mat(subvecInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @param operator The operator
   */
  public void subvec(int a, int b, Op operator) {
    subvecInternal(a, b).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void subvec(int a, int b, Op operator, AbstractMat operand) {
    subvecInternal(a, b).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void subvec(int a, int b, Op operator, double operand) {
    subvecInternal(a, b).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements specified in the selection as a column vector.
   * 
   * @param selection The element indices
   * @return The elements
   */
  protected SelectMat elemInternal(AbstractMat selection) {
    return new SelectMat(this, selection);
  }

  /**
   * Returns a deep copy of all elements specified in the selection as a column vector.
   * 
   * @param selection The element indices
   * @return The elements
   */
  public Mat elem(AbstractMat selection) {
    return new Mat(elemInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element indices
   * @param operator The operator
   */
  public void elem(AbstractMat selection, Op operator) {
    elemInternal(selection).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void elem(AbstractMat selection, Op operator, AbstractMat operand) {
    elemInternal(selection).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void elem(AbstractMat selection, Op operator, double operand) {
    elemInternal(selection).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all columns specified in the selection.
   * 
   * @param selection The column indices
   * @return The elements
   */
  protected SelectMat colsInternal(AbstractMat selection) {
    return new SelectMat(this, null, selection);
  }

  /**
   * Returns a deep copy of all columns specified in the selection.
   * 
   * @param selection The column indices
   * @return The elements
   */
  public Mat cols(AbstractMat selection) {
    return new Mat(colsInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   */
  public void cols(AbstractMat selection, Op operator) {
    colsInternal(selection).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(AbstractMat selection, Op operator, AbstractMat operand) {
    colsInternal(selection).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(AbstractMat selection, Op operator, double operand) {
    colsInternal(selection).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all rows specified in the selection.
   * 
   * @param selection The row indices
   * @return The elements
   */
  protected SelectMat rowsInternal(AbstractMat selection) {
    return new SelectMat(this, selection, null);
  }

  /**
   * Returns a deep copy of all rows specified in the selection.
   * 
   * @param selection The row indices
   * @return The elements
   */
  public Mat rows(AbstractMat selection) {
    return new Mat(rowsInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   */
  public void rows(AbstractMat selection, Op operator) {
    rows(selection).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(AbstractMat selection, Op operator, AbstractMat operand) {
    rows(selection).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(AbstractMat selection, Op operator, double operand) {
    rows(selection).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @return The elements
   */
  protected SelectMat submatInternal(AbstractMat rowSelection, AbstractMat columnSelection) {
    return new SelectMat(this, rowSelection, columnSelection);
  }

  /**
   * Returns a deep copy of all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @return The elements
   */
  public Mat submat(AbstractMat rowSelection, AbstractMat columnSelection) {
    return new Mat(submatInternal(rowSelection, columnSelection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @param operator The operator
   */
  public void submat(AbstractMat rowSelection, AbstractMat columnSelection, Op operator) {
    submatInternal(rowSelection, columnSelection).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(AbstractMat rowSelection, AbstractMat columnSelection, Op operator, AbstractMat operand) {
    submatInternal(rowSelection, columnSelection).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(AbstractMat rowSelection, AbstractMat columnSelection, Op operator, double operand) {
    submatInternal(rowSelection, columnSelection).inplace(operator, operand);
  }

  /**
   * Returns a deep copy of all elements on the main diagonal of the matrix as a column vector.
   * 
   * @return The diagonal
   */
  public Mat diag() {
    return diag(0);
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements on the main diagonal.
   * 
   * @param operator The operator
   */
  public void diag(Op operator) {
    diag(0, operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements on the main diagonal.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void diag(Op operator, AbstractMat operand) {
    diag(0, operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements on the main diagonal.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void diag(Op operator, double operand) {
    diag(0, operator, operand);
  }

  /**
   * Returns a shallow copy of all elements on the {@code k}th diagonal of the matrix as a column vector.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @return The diagonal
   */
  protected DiagMat diagInternal(int k) {
    return new DiagMat(this, k);
  }

  /**
   * Returns a deep copy of all elements on the {@code k}th diagonal of the matrix as a column vector.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @return The diagonal
   */
  public Mat diag(int k) {
    return new Mat(diagInternal(k));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements on the {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @param operator The operator
   */
  public void diag(int k, Op operator) {
    diagInternal(k).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements on the {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void diag(int k, Op operator, AbstractMat operand) {
    diagInternal(k).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements on the {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void diag(int k, Op operator, double operand) {
    diagInternal(k).inplace(operator, operand);
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in each column.
   * 
   * @param operator The operator
   */
  public void each_col(Op operator) {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in each column.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(Op operator, AbstractMat operand) {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in each column.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(Op operator, double operand) {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator, operand);
    }
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on each column specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   */
  public void each_col(AbstractMat selection, Op operator) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      col((int) selection._matrix[selection.iteratorNext()], operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each column specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(AbstractMat selection, Op operator, AbstractMat operand) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      col((int) selection._matrix[selection.iteratorNext()], operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each column specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(AbstractMat selection, Op operator, double operand) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      col((int) selection._matrix[selection.iteratorNext()], operator, operand);
    }
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in each row.
   * 
   * @param operator The operator
   */
  public void each_row(Op operator) {
    for (int i = 0; i < n_cols; i++) {
      row(i, operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in each row.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(Op operator, Mat operand) {
    for (int i = 0; i < n_rows; i++) {
      row(i, operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in each row.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(Op operator, double operand) {
    for (int i = 0; i < n_rows; i++) {
      row(i, operator, operand);
    }
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on each row specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   */
  public void each_row(AbstractMat selection, Op operator) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      row((int) selection._matrix[selection.iteratorNext()], operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each row specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(AbstractMat selection, Op operator, AbstractMat operand) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      row((int) selection._matrix[selection.iteratorNext()], operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each row specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(AbstractMat selection, Op operator, double operand) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      row((int) selection._matrix[selection.iteratorNext()], operator, operand);
    }
  }

  /**
   * Creates a matrix being the addition of this matrix with a right-hand side addend
   * 
   * @param operand The addend
   * @return The matrix
   */
  public Mat plus(AbstractMat operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.PLUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the addition of this matrix with a right-hand side addend
   * 
   * @param operand The addend
   * @return The matrix
   */
  public Mat plus(double operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.PLUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the subtration of this matrix with a right-hand side subtrahend
   * 
   * @param operand The subtrahend
   * @return The matrix
   */
  public Mat minus(AbstractMat operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.MINUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the subtration of this matrix with a right-hand side subtrahend
   * 
   * @param operand The subtrahend
   * @return The matrix
   */
  public Mat minus(double operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.MINUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat times(AbstractMat operand) {
    isNonEqualNumberOfElementsDetection(n_cols, operand.n_rows);

    Mat result = new Mat(n_rows, operand.n_cols);

    int n = 0;
    for (int j = 0; j < operand.n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        result._matrix[n++] = Arma.dot(rowInternal(i), operand.colInternal(j));
      }
    }

    return result;
  }

  /**
   * Creates a matrix being the multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat times(double operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.TIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemTimes(AbstractMat operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.ELEMTIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemTimes(double operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.ELEMTIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise division of this matrix with a right-hand side divisor
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemDivide(AbstractMat operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.ELEMDIVIDE, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise division of this matrix with a right-hand side divisor
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemDivide(double operand) {
    Mat result = new Mat(_matrix);
    result.inplace(Op.ELEMDIVIDE, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise negation of this matrix
   * 
   * @return The matrix
   */
  public Mat negate() {
    Mat result = new Mat(n_rows, n_cols);

    iteratorReset();
    for (int n = 0; n < n_elem; n++) {
      result._matrix[n] = -_matrix[iteratorNext()];
    }

    return result;
  }

  /**
   * Returns a reference to the internal data representation of the matrix.
   * 
   * @return The reference
   */
  public double[] memptr() {
    return _matrix;
  }

  /**
   * Sets all elements to 0.
   */
  public void zeros() {
    fill(0);
  }

  /**
   * Sets all elements to 1.
   */
  public void ones() {
    fill(1);
  }

  /**
   * Sets all elements along the main diagonal to 1 and the other elements to 0.
   */
  public void eye() {
    int length = Math.min(n_rows, n_cols);
    for (int i = 0; i < length; i++) {
      _matrix[getElementIndex(i, i)] = 1;
    }
  }

  /**
   * Sets each element to a pseudorandom value drawn from the standard uniform distribution on the left-closed and
   * right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param rng The pseudorandom generator
   */
  public void randu(Random rng) {
    iteratorReset();
    while (iteratorHasNext()) {
      _matrix[iteratorNext()] = rng.nextDouble();
    }
  }

  /**
   * Sets each element to a pseudorandom value drawn from the standard normal distribution with mean 0.0 and standard
   * deviation 1.0.
   * 
   * @param rng The pseudorandom generator
   */
  public void randn(Random rng) {
    iteratorReset();
    while (iteratorHasNext()) {
      _matrix[iteratorNext()] = rng.nextGaussian();
    }
  }

  /**
   * Replaces the value of each element with the provided one.
   * 
   * @param value The value
   */
  public void fill(double value) {
    iteratorReset();
    while (iteratorHasNext()) {
      _matrix[iteratorNext()] = value;
    }
  }

  /**
   * Swaps the elements of this matrix with another one.
   * 
   * @param matrix The matrix
   */
  public void swap(AbstractMat matrix) {
    isNonEqualNumberOfElementsDetection(n_rows, matrix.n_rows);
    isNonEqualNumberOfElementsDetection(n_cols, matrix.n_cols);

    submat(Op.EQUAL, matrix);
  }

  /**
   * Swaps the {@code j1}th column with the {@code j2}th.
   * 
   * @param j1 The first column
   * @param j2 The second column
   */
  public void swap_cols(int j1, int j2) {
    isColumnOutOfBoundsDetection(j1);
    isColumnOutOfBoundsDetection(j2);

    Mat temp = col(j1);
    col(j2, Op.EQUAL, j1);
    col(j1, Op.EQUAL, temp);
  }

  /**
   * Swaps the {@code i1}th row with the {@code i2}th.
   * 
   * @param i1 The first row
   * @param i2 The second row
   */
  public void swap_rows(int i1, int i2) {
    isRowOutOfBoundsDetection(i1);
    isRowOutOfBoundsDetection(i2);

    Mat temp = row(i1);
    row(i2, Op.EQUAL, i1);
    row(i1, Op.EQUAL, temp);
  }

  /**
   * Returns true if the matrix is empty and false otherwise.
   * 
   * @return Whether the matrix is empty
   */
  public boolean is_empty() {
    return (n_elem == 0);
  }

  /**
   * Returns true if the matrix contains only finite values and false otherwise.
   * 
   * @return Whether the matrix is finite
   */
  public boolean is_finite() {

    iteratorReset();
    while (iteratorHasNext()) {
      double element = _matrix[iteratorNext()];
      if (Double.isInfinite(element) || Double.isNaN(element)) {
        return false;
      }
    }

    return true;
  }

  /**
   * <b>Non-canonical:</b> Returns true if the matrix contains only numbers and false otherwise.
   * <p>
   * Note: False will only be returned if the value NaN is found.
   * 
   * @return Whether the matrix contains only numbers
   */
  protected boolean is_number() {
    iteratorReset();
    while (iteratorHasNext()) {
      double element = _matrix[iteratorNext()];
      if (Double.isNaN(element)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns true if the matrix is square and false otherwise.
   * 
   * @return Whether the matrix is sqaure
   */
  public boolean is_square() {
    return (n_rows == n_cols);
  }

  /**
   * Returns true if the matrix is a vector and false otherwise.
   * 
   * @return Whether the matrix is vector
   */
  public boolean is_vec() {
    return is_colvec() || is_rowvec();
  }

  /**
   * Returns true if the matrix is a column vector and false otherwise.
   * 
   * @return Whether the matrix is column vector
   */
  public boolean is_colvec() {
    return (n_cols == 1);
  }

  /**
   * Returns true if the matrix is a row vector and false otherwise.
   * 
   * @return Whether the matrix is row vector
   */
  public boolean is_rowvec() {
    return (n_rows == 1);
  }

  /**
   * Returns true if the element index is within the boundary
   * 
   * @param n The element index
   * @return Whether the element index is within the boundary
   */
  public boolean in_range(int n) {
    return (n > -1 && n < n_elem);
  }

  /**
   * Returns true if the span is within the boundary
   * 
   * @param span The span
   * @return Whether the span is within the boundary
   */
  public boolean in_range(Span span) {
    return (span.isEntireRange() || (span.getFirst() > -1 && span.getLast() < n_elem));
  }

  /**
   * Returns true if the row index and column index are within the boundary
   * 
   * @param i The row index
   * @param j The column index
   * @return Whether the row index and column index are within the boundary
   */
  public boolean in_range(int i, int j) {
    return (i > -1 && j > -1 && i < n_rows && j < n_cols);
  }

  /**
   * Returns true if the row span and column span are within the boundary
   * 
   * @param spanI The row span
   * @param spanJ The column span
   * @return Whether the row span and column span are within the boundary
   */
  public boolean in_range(Span spanI, Span spanJ) {
    return ((spanI.isEntireRange() || (spanI.getFirst() > -1 && spanI.getLast() < n_rows)) && (spanJ.isEntireRange() || (spanJ.getFirst() > -1 && spanJ.getLast() < n_cols)));
  }

  /**
   * Returns the smallest value of all elements.
   * 
   * @return The minimum
   */
  public double min() {
    isEmptyDetection();

    iteratorReset();
    double minimum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      minimum = Math.min(minimum, _matrix[iteratorNext()]);
    }

    return minimum;
  }

  /**
   * Returns the smallest value of all elements and stores its element index in {@code n}.
   * <p>
   * Note: The position must be of the mutable type int[].
   * 
   * @param n The element index
   * @return The minimum
   */
  public double min(int[] n) {
    isEmptyDetection();

    iteratorReset();
    double minimum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      int nn = iteratorNext();
      double element = _matrix[nn];

      if (element < minimum) {
        minimum = element;
        n[0] = nn;
      }
    }

    return minimum;
  }

  /**
   * Returns the smallest value of all elements and stores its row index in {@code j} and column index in {@code j}.
   * <p>
   * Note: The positions must be of the mutable type int[].
   * 
   * @param i The row index
   * @param j The column index
   * @return The minimum
   */
  public double min(int[] i, int[] j) {
    isEmptyDetection();

    iteratorReset();
    double minimum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      int n = iteratorNext();
      double element = _matrix[n];

      if (element < minimum) {
        minimum = element;
        j[0] = n / n_rows;
        i[0] = n - (j[0] * n_rows);
      }
    }

    return minimum;
  }

  /**
   * Returns the largest value of all elements.
   * 
   * @return The maximum
   */
  public double max() {
    isEmptyDetection();

    iteratorReset();
    double maximum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      maximum = Math.max(maximum, _matrix[iteratorNext()]);
    }

    return maximum;
  }

  /**
   * Returns the largest value of all elements and stores its element index in {@code n}.
   * <p>
   * Note: The position must be of the mutable type int[].
   * 
   * @param n The element index
   * @return The maximum
   */
  public double max(int[] n) {
    isEmptyDetection();

    iteratorReset();
    double maximum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      int nn = iteratorNext();
      double element = _matrix[nn];

      if (element > maximum) {
        maximum = element;
        n[0] = nn;
      }
    }

    return maximum;
  }

  /**
   * Returns the largest value of all elements and stores its row index in {@code j} and column index in {@code j} .
   * <p>
   * Note: The positions must be of the mutable type int[].
   * 
   * @param i The row index
   * @param j The column index
   * @return The maximum
   */
  public double max(int[] i, int[] j) {
    isEmptyDetection();

    iteratorReset();
    double maximum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      int n = iteratorNext();
      double element = _matrix[n];

      if (element > maximum) {
        maximum = element;
        j[0] = n / n_rows;
        i[0] = n - (j[0] * n_rows);
      }
    }

    return maximum;
  }

  /**
   * Prints the matrix to System.out.
   */
  public void print() {
    print(System.out);
  }

  /**
   * Prints the matrix to System.out, with an additional header.
   * 
   * @param header The header
   */
  public void print(String header) {
    print(System.out, header);
  }

  /**
   * Prints the matrix to a Stream.
   * 
   * @param stream The stream
   */
  public void print(OutputStream stream) {
    print(stream);
  }

  /**
   * Prints the matrix to a Stream, with an additional header.
   * 
   * @param stream The stream
   * @param header The header
   */
  public void print(OutputStream stream, String header) {

    PrintWriter writer;
    try {
      writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stream, "UTF-8")));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 is not supported. How could this happen?");
    }

    if (!header.isEmpty()) {
      writer.println(header);
    }
    writer.println(this);
  }

  /**
   * Prints the matrix to System.out.
   */
  public void raw_print() {
    print();
  }

  /**
   * Prints the matrix to System.out, with an additional header.
   * 
   * @param header The header
   */
  public void raw_print(String header) {
    print(header);
  }

  /**
   * Prints the matrix to a Stream.
   * 
   * @param stream The stream
   */
  public void raw_print(OutputStream stream) {
    print(stream);
  }

  /**
   * Prints the matrix to a Stream, with an additional header.
   * 
   * @param stream The stream
   * @param header The header
   */
  public void raw_print(OutputStream stream, String header) {
    print(stream, header);
  }

  /**
   * Store the element into a file with filetype {@code ascii} and returns true on succes.
   * 
   * @param filename The filename
   * @return Whether the process was succesfully.
   * 
   * @throws FileNotFoundException File not found.
   */
  public boolean save(String filename) throws FileNotFoundException {
    return save(filename, "ascii");
  }

  /**
   * Store the element into a file with filetype {@code filetype} and returns true on succes.
   * 
   * @param filename The filename
   * @param filetype The filetype
   * @return Whether the process was succesfully.
   * 
   * @throws FileNotFoundException File not found.
   */
  public boolean save(String filename, String filetype) throws FileNotFoundException {
    return save(new FileOutputStream(filename, false), filetype);
  }

  /**
   * Store the element into a stream with filetype {@code ascii} and returns true on succes.
   * 
   * @param stream The stream
   * @return Whether the process was succesfully.
   */
  public boolean save(OutputStream stream) {
    return save(stream, "ascii");
  }

  /**
   * Store the element into a stream with filetype {@code filetype} and returns true on succes.
   * 
   * @param stream The stream
   * @param filetype The filetype
   * @return Whether the process was succesfully.
   * 
   * @throws IllegalArgumentException Only ascii is supported, but was {@code filetype}.
   */
  public boolean save(OutputStream stream, String filetype) throws IllegalArgumentException {
    if (!filetype.equals("ascii")) {
      throw new IllegalArgumentException("Only ascii is supported, but was " + filetype + ".");
    }

    PrintWriter writer;
    try {
      writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stream, "UTF-8")));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 is not supported. How could this happen?");
    }

    for (int i = 0; i < n_rows; i++)
    {
      writer.print(" ");

      for (int j = 0; j < n_cols; j++) {
        double value = _matrix[getElementIndex(i, j)];
        if (Double.isInfinite(value)) {
          String sign = "";
          if (value < 0) {
            sign = "-";
          }
          writer.format("%24s", sign + "Inf");
        } else {
          writer.format(Locale.ENGLISH, "%1$ 24.16e", value);
        }
      }

      writer.println();
    }

    writer.close();

    return true;
  }

  /**
   * Returns true if the matrix is empty and false otherwise.
   * 
   * @return Whether the matrix is empty
   */
  public boolean empty() {
    return is_empty();
  }

  /**
   * Returns the number of elements.
   * 
   * @return The number of elements
   */
  public int size() {
    return n_elem;
  }

  /**
   * Returns the inverse matrix.
   * <p>
   * Fails if the provided matrix is not invertible.
   * 
   * @return The inverse
   * 
   * @throws UnsupportedOperationException The matrix is not invertible.
   */
  public Mat i() throws UnsupportedOperationException {
    isNotSquareDetection();
    isIllConditionedDectetion();

    DenseMatrix64F inverse = new DenseMatrix64F(n_rows, n_cols);
    if (!CommonOps.invert(convertMatToEJMLMat(this), inverse)) {
      throw new UnsupportedOperationException("The matrix is not invertible.");
    }

    return convertEJMLToMat(inverse);
  }

  /**
   * Returns the transpose of the matrix.
   * 
   * @return The transpose
   */
  public Mat t() {
    Mat transpose = new Mat(n_cols, n_rows);
    int n = 0;
    for (int i = 0; i < n_rows; i++) {
      for (int j = 0; j < n_cols; j++) {
        transpose._matrix[n++] = _matrix[getElementIndex(i, j)];
      }
    }

    return transpose;
  }

  /**
   * Performs a element-wise unary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  protected void inplace(Op operator) throws IllegalArgumentException, UnsupportedOperationException {
    iteratorReset();
    while (iteratorHasNext()) {
      inplace(iteratorNext(), operator);
    }
  }

  /**
   * Performs a element-wise unary right-hand side inplace operation on the {@code n}th element.
   * 
   * @param n The position
   * @param operator The operator
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  protected void inplace(int n, Op operator) throws IllegalArgumentException, UnsupportedOperationException {
    double operand = _matrix[n];

    if (Double.isNaN(operand)) {
      throw new IllegalArgumentException("NaN is not a valid operand.");
    }

    double result;
    switch (operator) {
      case INCREMENT:
        result = _matrix[n]++;
        break;
      case DECREMENT:
        result = _matrix[n]--;
        break;
      default:
        throw new UnsupportedOperationException("Only unary arithmetic operators are supported.");
    }

    isUnderflowOverflowDetection(operand, operator, result);
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inplace(Op operator, AbstractMat operand) throws IllegalArgumentException, UnsupportedOperationException {
    isNonEqualNumberOfElementsDetection(n_elem, operand.n_elem);

    iteratorReset();
    operand.iteratorReset();
    while (iteratorHasNext()) {
      inplace(iteratorNext(), operator, operand._matrix[operand.iteratorNext()]);
    }
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inplace(Op operator, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    iteratorReset();
    while (iteratorHasNext()) {
      inplace(iteratorNext(), operator, operand);
    }
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on the {@code n}th element.
   * 
   * @param n The position
   * @param operator The operator
   * @param rightHandSideOperand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws ArithmeticException Division by zero.
   * @throws ArithmeticException Division by infinity.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inplace(int n, Op operator, double rightHandSideOperand) throws IllegalArgumentException, UnsupportedOperationException {
    double leftHandSideOperand = _matrix[n];

    if (Double.isNaN(leftHandSideOperand) || Double.isNaN(rightHandSideOperand)) {
      throw new IllegalArgumentException("NaN is not a valid operand.");
    }

    double result;
    switch (operator) {
      case EQUAL:
        result = _matrix[n] = rightHandSideOperand;
        break;
      case PLUS:
        result = _matrix[n] += rightHandSideOperand;
        break;
      case MINUS:
        result = _matrix[n] -= rightHandSideOperand;
        break;
      case TIMES:
      case ELEMTIMES:
        result = _matrix[n] *= rightHandSideOperand;
        break;
      case ELEMDIVIDE:
        if (rightHandSideOperand == 0) {
          throw new ArithmeticException("Division by zero.");
        }

        if (Double.isInfinite(rightHandSideOperand)) {
          throw new ArithmeticException("Division by infinity.");
        }

        result = _matrix[n] /= rightHandSideOperand;
        break;
      default:
        throw new UnsupportedOperationException("Only binary arithmetic operators and equality are supported.");
    }

    isUnderflowOverflowDetection(leftHandSideOperand, operator, rightHandSideOperand, result);
  }

  /**
   * Detects if the result of an unary operation was affected by an overflow and throws an exception if so.
   * <p>
   * Note: The supported unary operators cannot result in an underflow.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param result The calculated result
   * 
   * @throws ArithmeticException Overflow detected. {@code a operator} resulted in {@code result}.
   */
  protected static void isUnderflowOverflowDetection(double a, Op operator, double result) throws ArithmeticException {
    if (!Double.isInfinite(a) && Double.isInfinite(result)) {
      throw new ArithmeticException("Overflow detected. " + a + " " + operator + " resulted in " + result + ".");
    }
  }

  /**
   * Detects if the result of an binary operation was affected by an underflow or overflow and throws an exception if
   * so.
   * <p>
   * The overflow detection is only active if the operator is one of {@link Op#TIMES}, {@link Op#ELEMTIMES} or
   * {@link Op#ELEMDIVIDE}.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param b The right-hand side operand
   * @param result The calculated result
   * 
   * @throws ArithmeticException Underflow detected. {@code a operator b} resulted in {@code result}.
   * @throws ArithmeticException Overflow detected. {@code a operator b} resulted in {@code result}.
   */
  protected static void isUnderflowOverflowDetection(double a, Op operator, double b, double result) throws ArithmeticException {
    if (operator.equals(Op.TIMES) || operator.equals(Op.ELEMTIMES) || operator.equals(Op.ELEMDIVIDE)) {
      if (a != 0 && b != 0 && result == 0) {
        throw new ArithmeticException("Underflow detected. " + a + " " + operator + " " + b + " resulted in " + result + ".");
      }
    }

    if (!Double.isInfinite(a) && !Double.isInfinite(b) && Double.isInfinite(result)) {
      throw new ArithmeticException("Overflow detected. " + a + " " + operator + " " + b + " resulted in " + result + ".");
    }
  }

  /**
   * Detects if the matrix is not a vector and throws an exception if so.
   * 
   * @param first The first amount
   * @param second The second amount
   * 
   * @throws IllegalArgumentException The numbers of elements must match, but were {@code first} and {@code second}.
   */
  protected static void isNonEqualNumberOfElementsDetection(int first, int second) throws IllegalArgumentException {
    if (first != second) {
      throw new IllegalArgumentException("The numbers of elements must match, but were " + first + " and " + second + ".");
    }
  }

  /**
   * Detects if the matrix is empty and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  protected void isEmptyDetection() throws UnsupportedOperationException {
    if (n_elem < 1) {
      throw new UnsupportedOperationException("The matrix must have at least one element.");
    }
  }

  /**
   * Detects if the matrix is ill-conditioned and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix is ill-conditioned: {@code conditionNumber}.
   */
  protected void isIllConditionedDectetion() {
    Mat singularvalues = Arma.svd(this);
    double conditionNumber = singularvalues.at(0) / singularvalues.at(singularvalues.n_elem - 1);

    if (conditionNumber >= 1 / Math.max(n_cols, n_rows) * Datum.eps) {
      throw new ArithmeticException("The matrix is ill-conditioned: " + conditionNumber);
    }
  }

  /**
   * Detects if the matrix is not a vector and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be a vector, but was a ({@link #n_rows}, {@link #n_cols}
   *           )-matrix.
   */
  protected void isNonVectorDetection() throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("The matrix must be a vector, but was a (" + n_rows + ", " + n_cols + ")-matrix.");
    }
  }

  /**
   * Detects if the matrix is a vector and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be a non-vector, but was a ({@link #n_rows}, {@link #n_cols}
   *           )-vector.
   */
  protected void isVectorDetection() throws UnsupportedOperationException {
    if (is_vec()) {
      throw new UnsupportedOperationException("The matrix must be a non-vector, but was a (" + n_rows + ", " + n_cols + ")-vector.");
    }
  }

  /**
   * Detects if the position is negative or one of NaN or +/-infinity and throws an exception if so.
   * 
   * @param n The position
   * 
   * @throws IllegalArgumentException The position must be non-negative.
   * @throws IllegalArgumentException NaN and +/-infinity are not valid positions value.
   */
  protected static void isInvalidPositionDetection(int n) throws IllegalArgumentException {
    if (n < 0) {
      throw new IllegalArgumentException("The position must be non-negative.");
    }

    if (Double.isInfinite(n) || Double.isNaN(n)) {
      throw new IllegalArgumentException("NaN and +/-infinity are not valid positions value.");
    }
  }

  /**
   * Detects if any position is invalid or the first position is not less than equal the last one and throws an
   * exception if so.
   * 
   * @param a The first position
   * @param b The last position
   * 
   * @throws IllegalArgumentException The first position needs to be less than equal the last, but were {@code a} and
   *           {@code b}.
   */
  protected static void isInvalidRangeDetection(int a, int b) throws IllegalArgumentException {
    if (a > b) {
      throw new IllegalArgumentException("The first position needs to be less than equal the last, but were " + a + " and " + b + ".");
    }

    isInvalidPositionDetection(a);
    isInvalidPositionDetection(b);
  }

  /**
   * Detects if the element index is invalid or out of bound and throws an exception if so.
   * 
   * @param n The element index
   * 
   * @throws ArrayIndexOutOfBoundsException The element index range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the element index range was ({@code n}, {@code n}).
   */
  protected void isElementOutOfBoundsDetection(int n) throws ArrayIndexOutOfBoundsException {
    isElementRangeOutOfBoundsDetection(n, n);
  }

  /**
   * Detects if the element index range is invalid or out of bound and throws an exception if so.
   * 
   * @param a The first element index
   * @param b The last element index
   * 
   * @throws ArrayIndexOutOfBoundsException The element index range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the element index range was ({@code a}, {@code b}).
   */
  protected void isElementRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b > n_rows) {
      throw new ArrayIndexOutOfBoundsException("The element index range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the element index range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if a column index is invalid or out of bound and throws an exception if so.
   * 
   * @param j The column index
   * 
   * @throws ArrayIndexOutOfBoundsException The column index range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the column index range was ({@code j}, {@code j}).
   */
  protected void isColumnOutOfBoundsDetection(int j) throws ArrayIndexOutOfBoundsException {
    isColumnRangeOutOfBoundsDetection(j, j);
  }

  /**
   * Detects if a column index range is invalid or out of bound and throws an exception if so.
   * 
   * @param a The first row index
   * @param b The last row index
   * 
   * @throws ArrayIndexOutOfBoundsException The column index range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the column index range was ({@code a}, {@code b}).
   */
  protected void isColumnRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b >= n_rows) {
      throw new ArrayIndexOutOfBoundsException("The column index range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the column index range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if a row index is invalid or out of bound and throws an exception if so.
   * 
   * @param i The row index
   * 
   * @throws ArrayIndexOutOfBoundsException The row index range is out of bound. The matrix is of size ( {@link #n_rows}
   *           , {@link #n_cols}) but the row index range was ({@code i}, {@code i}).
   */
  protected void isRowOutOfBoundsDetection(int i) throws ArrayIndexOutOfBoundsException {
    isRowRangeOutOfBoundsDetection(i, i);
  }

  /**
   * Detects if a row index range is out of bound and throws an exception if so.
   * 
   * @param a The first row index
   * @param b The last row index
   * 
   * @throws ArrayIndexOutOfBoundsException The row index range is out of bound. The matrix is of size ( {@link #n_rows}
   *           , {@link #n_cols}) but the row index range was ({@code a}, {@code b}).
   */
  protected void isRowRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b >= n_rows) {
      throw new ArrayIndexOutOfBoundsException("The row index range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the row index range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if any element index is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The column indices
   */
  protected void isInvalidElementSelectionDetection(AbstractMat selection) {
    for (int n = 0; n < selection.n_elem; n++) {
      double index = selection.at(n);

      isNonIntergerPositionDetection(index);
      isElementOutOfBoundsDetection((int) index);
    }
  }

  /**
   * Detects if any column index is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The column indices
   */
  protected void isInvalidColumnSelectionDetection(AbstractMat selection) {
    for (int n = 0; n < selection.n_elem; n++) {
      double index = selection.at(n);

      isNonIntergerPositionDetection(index);
      isColumnOutOfBoundsDetection((int) index);
    }
  }

  /**
   * Detects if any row index is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The row indices
   */
  protected void isInvalidRowSelectionDetection(AbstractMat selection) {
    for (int n = 0; n < selection.n_elem; n++) {
      double index = selection.at(n);

      isNonIntergerPositionDetection(index);
      isRowOutOfBoundsDetection((int) index);
    }
  }

  /**
   * Detects if a position is not an integer and throws an exception if so.
   * 
   * @param position The position
   * 
   * @throws IllegalArgumentException The position must be an integer, but was not: {@code rowPositionDouble}.
   */
  protected static void isNonIntergerPositionDetection(double position) throws IllegalArgumentException {
    int positionInt = (int) position;

    // Will also fail if the value is negative, which is also not allowed.
    if (position != positionInt) {
      throw new IllegalArgumentException("Each position must be an integer, but at least one was not: " + position + ".");
    }
  }

  /**
   * Detects if the matrix is not square and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be square, but was a ({@link #n_rows}, {@link #n_cols}
   *           )-matrix.
   */
  protected void isNotSquareDetection() throws UnsupportedOperationException {
    if (!is_square()) {
      throw new IllegalArgumentException("The matrix must be square, but was a (" + n_rows + ", " + n_cols + ")-matrix.");
    }
  }

  /**
   * Detects if the matrix is not symmetric and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be symmetric.
   */
  protected void isNotSymmetricDetection() {
    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        if (i != j && at(i) != at(j)) {
          throw new IllegalArgumentException("The matrix must be symmetric.");
        }
      }
    }
  }

  /**
   * Detects if a parameter value is neither 0 nor 1 and throws an exception if so.
   * 
   * @param value The parameter value
   * 
   * @throws IllegalArgumentException The parameter value must be either 0 or 1, but was {@code value}.
   */
  protected static void isNonBinaryParameterDetection(int value) throws IllegalArgumentException {
    if (value < 0 || value > 1) {
      throw new IllegalArgumentException("The parameter value must be either 0 or 1, but was " + value + ".");
    }
  }

  /**
   * Converts a matrix from the Mat format into EJML's DenseMatrix64F format.
   * 
   * @param matrix The matrix in Mat format
   * @return The matrix in DenseMatrix64F format
   */
  protected static DenseMatrix64F convertMatToEJMLMat(AbstractMat matrix) {
    if (matrix.is_vec()) {
      return new DenseMatrix64F(matrix.n_rows, matrix.n_cols, true, matrix._matrix);
    } else {
      return new DenseMatrix64F(matrix.n_rows, matrix.n_cols, false, matrix._matrix);
    }
  }

  /**
   * Converts a matrix from EJML's DenseMatrix64F format into the Mat format.
   * 
   * @param ejmlMatrix The matrix in DenseMatrix64F format
   * @return The matrix in Mat format
   */
  protected static Mat convertEJMLToMat(DenseMatrix64F ejmlMatrix) {
    Mat matrix = new Mat(ejmlMatrix.numRows, ejmlMatrix.numCols);

    if (MatrixFeatures.isVector(ejmlMatrix)) {
      System.arraycopy(ejmlMatrix.data, 0, matrix._matrix, 0, matrix.n_elem);
    } else {
      int n = 0;
      for (int i = 0; i < matrix.n_rows; i++) {
        for (int j = 0; j < matrix.n_cols; j++) {
          matrix._matrix[matrix.getElementIndex(i, j)] = ejmlMatrix.data[n++];
        }
      }
    }

    return matrix;
  }

  @Override
  public String toString() {
    String output = "(" + n_rows + ", " + n_cols + ")-matrix: [\n";
    for (int i = 0; i < n_rows; i++)
    {
      output += " ";

      for (int j = 0; j < n_cols; j++) {
        double value = _matrix[getElementIndex(i, j)];
        if (Double.isInfinite(value)) {
          String sign = "";
          if (value < 0) {
            sign = "-";
          }
          output += String.format("%5s", sign + "Inf");
        } else {
          output += String.format(Locale.ENGLISH, "%1$ 7.0f", value);
        }
      }

      output += "\n";
    }
    output += "]";

    return output;
  }

  @Override
  public Iterator<Double> iterator() {
    class MatIterator implements Iterator<Double> {

      /**
       * The matrix
       */
      private AbstractMat _matrix;

      /**
       * Initialises the iterator.
       * 
       * @param matrix The matrix
       */
      public MatIterator(AbstractMat matrix) {
        _matrix = matrix;
        _matrix.iteratorReset();
      }

      @Override
      public boolean hasNext() {
        return _matrix.iteratorHasNext();
      }

      @Override
      public Double next() {
        return _matrix._matrix[_matrix.iteratorNext()];
      }

      @Override
      public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removal is not supported.");
      }
    }

    return new MatIterator(this);
  }
}
