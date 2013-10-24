/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package arma;

import java.io.PrintWriter;
import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.RandomMatrices;

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
 * <b>Note:</b> Armadillo stores values by <a
 * href="https://en.wikipedia.org/wiki/Column_major#Column-major_order">column-major-ordering</a>, while EJML stores
 * them by <a href="http://en.wikipedia.org/wiki/Row-major_order">row-major ordering</a>. In order to be similar to
 * Armadillo, all interfaces work as if column-major-ordering is used while converting to row-major ordering internally.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient Java Matrix Library</a>
 */
public class Mat {

  /**
   * The internal data representation of the matrix
   */
  DenseMatrix64F _matrix;

  /**
   * The number of rows
   */
  public int     n_rows;

  /**
   * The number of columns
   */
  public int     n_cols;

  /**
   * The number of elements (same as {@link #n_rows} * {@link #n_cols}) .
   */
  public int     n_elem;

  /**
   * Creates a matrix by copying to the provided one.
   * 
   * @param matrix The matrix
   */
  Mat(DenseMatrix64F matrix) {
    _matrix = new DenseMatrix64F(matrix);
    updateAttributes();
  }

  /**
   * Creates an empty matrix with zero elements.
   */
  public Mat() {
    this(new DenseMatrix64F());
  }

  /**
   * Creates a column vector with the same number of elements as the provided one-dimensional array.
   * 
   * @param matrix The array
   */
  public Mat(double[] matrix) {
    if (matrix.length > 0) {
      _matrix = new DenseMatrix64F(matrix.length, 1);

      for (int n = 0; n < _matrix.numRows; n++) {
        _matrix.set(n, matrix[n]);
      }
    } else {
      _matrix = new DenseMatrix64F();
    }

    updateAttributes();
  }

  /**
   * Creates a matrix with the same size as the provided two-dimensional array.
   * <p>
   * The array is assumed to have a structure like {@code array[rows][columns]}.
   * 
   * @param matrix The array
   * 
   * @throws IllegalArgumentException All columns must have the same length.
   */
  public Mat(double[][] matrix) throws IllegalArgumentException {
    if (matrix.length > 0 && matrix[0].length > 0) {
      int numberOfRows = matrix.length;
      int numberOfColumns = matrix[0].length;

      _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);

      for (int i = 0; i < _matrix.numRows; i++) {
        for (int j = 0; j < _matrix.numCols; j++) {
          if (matrix[0].length != _matrix.numCols) {
            throw new IllegalArgumentException("All columns must have the same length.");
          }

          _matrix.set(i, j, matrix[i][j]);
        }
      }
    } else {
      _matrix = new DenseMatrix64F();
    }

    updateAttributes();
  }

  /**
   * Creates a matrix by copying the provided one.
   * 
   * @param matrix The matrix
   */
  public Mat(Mat matrix) {
    this(new DenseMatrix64F(matrix.memptr()));
  }

  /**
   * Creates a uninitialised column vector with {@code numberOfElements} elements.
   * 
   * @param numberOfElements The number of elements
   */
  public Mat(int numberOfElements) {
    this(numberOfElements, 1, Fill.NONE);
  }

  /**
   * Creates an uninitialised matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public Mat(int numberOfRows, int numberOfColumns) {
    this(numberOfRows, numberOfColumns, Fill.NONE);
  }

  /**
   * Creates a column vector with {@code numberOfElements} elements that is filled according to {@code fillType}.
   * 
   * @param numberOfElements The number of elements
   * @param fillType The fill type
   * 
   * @throws IllegalArgumentException Does not support {@link Fill#RANDU} or {@link Fill#RANDN}. Use
   *           {@link #Mat(int, Fill, Random)} instead.
   */
  public Mat(int numberOfElements, Fill fillType) {
    this(numberOfElements, 1, fillType);
  }

  /**
   * Creates a matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns that is filled according to
   * {@code fillType}.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param fillType The fill type
   * 
   * @throws IllegalArgumentException Does not support {@link Fill#RANDU} or {@link Fill#RANDN}. Use
   *           {@link #Mat(int, int, Fill, Random)} instead.
   */
  public Mat(int numberOfRows, int numberOfColumns, Fill fillType) throws IllegalArgumentException {
    switch (fillType) {
      case NONE:
      case ZEROS:
        _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);
        break;
      case ONES:
        _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);
        CommonOps.fill(_matrix, 1);
        break;
      case EYE:
        _matrix = CommonOps.identity(numberOfRows, numberOfColumns);
        break;
      case RANDU:
      case RANDN:
        throw new IllegalArgumentException("Does not support Fill.RANDU or FILL.RANDN. Use Mat(int, int, Fill, Random) instead.");
    }

    updateAttributes();
  }

  /**
   * Creates a column vector with {@code numberOfElements} elements that is filled according to {@code fillType}.
   * <p>
   * Works also with {@link Fill#RANDU} and {@link Fill#RANDN}.
   * 
   * @param numberOfElements The number of elements
   * @param fillType The fill type
   * @param rng The pseudorandom generator
   */
  public Mat(int numberOfElements, Fill fillType, Random rng) {
    this(numberOfElements, 1, fillType, rng);
  }

  /**
   * Creates a matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns that is filled according to
   * {@code fillType}.
   * <p>
   * Works also with {@link Fill#RANDU} and {@link Fill#RANDN}.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param fillType The fill type
   * @param rng The pseudorandom generator
   */
  public Mat(int numberOfRows, int numberOfColumns, Fill fillType, Random rng) {
    switch (fillType) {
      case NONE:
      case ZEROS:
        _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);
        break;
      case ONES:
        _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);
        CommonOps.fill(_matrix, 1);
        break;
      case EYE:
        _matrix = CommonOps.identity(numberOfRows, numberOfColumns);
        break;
      case RANDU:
        _matrix = RandomMatrices.createRandom(numberOfRows, numberOfColumns, rng);
        break;
      case RANDN:
        _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);

        int numberOfElements = _matrix.getNumElements();
        for (int i = 0; i < numberOfElements; i++) {
          _matrix.set(i, rng.nextGaussian());
        }
        break;
    }

    updateAttributes();
  }

  /**
   * Returns the value of the element at the {@code i}th row and {@code j}th column.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param i The row
   * @param j The column
   * @return The value
   * 
   * @throws ArrayIndexOutOfBoundsException The requested position is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the position was ({@code i}, {@code j}).
   * @throws IllegalArgumentException All position must be non-negative.
   * @throws IllegalArgumentException NaN and infinity are not valid element positions.
   */
  public double at(int i, int j) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
    if (!in_range(i, j)) {
      throw new ArrayIndexOutOfBoundsException("The position is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the position was (" + i + ", " + j + ").");
    }

    return _matrix.get(i, j);
  }

  /**
   * Performs a unary operation on the value of the element at the {@code i}th row and {@code j}th column and overwrites
   * it with the result.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param i The row
   * @param j The column
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The requested position is out of bound. The matrix is of size (
   *           {@link #n_rows} , {@link #n_cols}) but the position was ({@code i}, {@code j}).
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void at(int i, int j, Op operator) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, UnsupportedOperationException {
    if (!in_range(i, j)) {
      throw new ArrayIndexOutOfBoundsException("The position is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the position was (" + i + ", " + j + ").");
    }

    _matrix.set(i, j, Op.getResult(at(i, j), operator));
  }

  /**
   * Performs a right-hand side operation on the value of the element at the {@code i}th row and {@code j}th column and
   * overwrites it with the result.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param i The row
   * @param j The column
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix is of size ({@link #n_rows} ,
   *           {@link #n_cols}) but the position was ({@code i}, {@code j}).
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void at(int i, int j, Op operator, double operand) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, UnsupportedOperationException {
    if (!in_range(i, j)) {
      throw new ArrayIndexOutOfBoundsException("The position is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the position was (" + i + ", " + j + ").");
    }

    _matrix.set(i, j, Op.getResult(at(i, j), operator, operand));
  }

  /**
   * Returns the value of the {@code n}th element of a column-major-ordered one-dimensional view of the matrix.
   * <p>
   * <b>Note:</b> {@link #at(int, int) at(i, j)}{@code = at(i + j * n_rows)}.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param n The position
   * @return The value
   * 
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   */
  public double at(int n) throws ArrayIndexOutOfBoundsException {
    if (!in_range(n)) {
      throw new ArrayIndexOutOfBoundsException("The  position is out of bound. The matrix contains " + n_elem + " elements, but the position was  " + n + ".");
    }

    return _matrix.get(convertToRowMajorOrdering(n));
  }

  /**
   * Performs a unary operation on the value of the {@code n}th element of a column-major-ordered one-dimensional view
   * of the matrix and overwrites it with the result.
   * <p>
   * <b>Note:</b> {@link #at(int, int) at(i, j)}{@code = at(i + j * n_rows)}.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param n The position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void at(int n, Op operator) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, UnsupportedOperationException {
    if (!in_range(n)) {
      throw new ArrayIndexOutOfBoundsException("The position is out of bound. The matrix contains " + n_elem + " elements, but the position was  " + n + ".");
    }

    n = convertToRowMajorOrdering(n);
    _matrix.set(n, Op.getResult(_matrix.get(n), operator));
  }

  /**
   * Performs a right-hand side operation on the value of the {@code n}th element of a column-major-ordered
   * one-dimensional view of the matrix and overwrites it with the result.
   * <p>
   * <b>Note:</b> {@link #at(int, int) at(i, j)}{@code = at(i + j * n_rows)}.
   * <p>
   * <b>Non-canonical:</b> Performs boundary checks. <b>Note:</b> There is no element access provided without boundary
   * checks.
   * 
   * @param n The position
   * @param operation The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void at(int n, Op operation, double operand) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, UnsupportedOperationException {
    if (!in_range(n)) {
      throw new ArrayIndexOutOfBoundsException("The position is out of bound. The matrix contains " + n_elem + " elements, but the position was  " + n + ".");
    }

    n = convertToRowMajorOrdering(n);
    _matrix.set(n, Op.getResult(_matrix.get(n), operation, operand));
  }

  /**
   * Returns the {@code j}th column.
   * 
   * @param j The column position
   * @return The column
   * 
   * @throws ArrayIndexOutOfBoundsException The column position is out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column position was {@code j}.
   */
  public Mat col(int j) throws ArrayIndexOutOfBoundsException {
    if (!in_range(Span.all(), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column position is out of bound. The matrix contains " + n_cols + " columns, but the column position was  " + j + ".");
    }

    Mat result = new Mat(n_rows, 1);
    for (int n = 0; n < n_rows; n++) {
      result._matrix.set(n, _matrix.get(n, j));
    }

    return result;
  }

  /**
   * Performs a unary operation on all elements of the {@code j}th column and overwrites each element with the result.
   * 
   * @param j The column position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The column position is out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column position was {@code j}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void col(int j, Op operator) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(Span.all(), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column position is out of bound. The matrix contains " + n_cols + " columns, but the column position was  " + j + ".");
    }

    for (int i = 0; i < n_rows; i++) {
      at(i, j, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code j}th column and overwrites each
   * element with the result.
   * 
   * @param j The column position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@code n_rows} and {@link #n_elem operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The column position is out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column position was {@code j}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void col(int j, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (operand.n_elem != n_rows) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + n_rows + " and " + operand.n_elem + ".");
    }

    if (!in_range(Span.all(), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column position is out of bound. The matrix contains " + n_cols + " columns, but the column position was  " + j + ".");
    }

    for (int i = 0; i < n_rows; i++) {
      at(i, j, operator, operand.at(i));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code j}th column and overwrites each
   * element with the result.
   * 
   * @param j The column position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The column position is out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column position was {@code j}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void col(int j, Op operator, double operand) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(Span.all(), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column position is out of bound. The matrix contains " + n_cols + " columns, but the column position was  " + j + ".");
    }

    for (int i = 0; i < n_rows; i++) {
      at(i, j, operator, operand);
    }
  }

  /**
   * Returns all elements in the {@code j}th column and {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @return The submatrix
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from column {@code j} and row {@code a} to
   *           {@code b}.
   */
  public Mat col(int a, int b, int j) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(a, b), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from column " + j + " and row " + a + " to " + b + ".");
    }

    Mat result = new Mat(b - a + 1, 1);
    int resultN = 0;
    for (int i = a; i <= b; i++) {
      result._matrix.set(resultN++, _matrix.get(i, j));
    }

    return result;
  }

  /**
   * Performs a unary operation on all elements of the {@code j}th column and {@code a}th to {@code b}th row and
   * overwrites each element with the result.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from column {@code j} and row {@code a} to
   *           {@code b}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void col(int a, int b, int j, Op operator) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(a, b), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the position were  " + a + ", " + b + " and " + j + ".");
    }

    for (int i = a; i <= b; i++) {
      at(i, j, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code j}th column and {@code a}th to
   * {@code b}th row and overwrites each element with the result.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @param operation The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@code b} - {@code a} + 1 and {@link #n_elem operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from column {@code j} and row {@code a} to
   *           {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void col(int a, int b, int j, Op operation, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (operand.n_elem != n_rows) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + (b - a + 1) + " and " + operand.n_elem + ".");
    }

    if (!in_range(new Span(a, b), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from column " + j + " and row " + a + " to " + b + ".");
    }

    int operandN = 0;
    for (int i = a; i <= b; i++) {
      at(i, j, operation, operand.at(operandN++));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code j}th column and {@code a}th to
   * {@code b}th row and overwrites each element with the result.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @param operation The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from column {@code j} and row {@code a} to
   *           {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void col(int a, int b, int j, Op operation, double operand) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(a, b), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from column " + j + " and row " + a + " to " + b + ".");
    }

    for (int i = a; i <= b; i++) {
      at(i, j, operation, operand);
    }
  }

  /**
   * Returns the {@code i}th row.
   * 
   * @param i The row position
   * @return The row
   * 
   * @throws ArrayIndexOutOfBoundsException The row position is out of bound. The matrix contains {@link #n_rows} rows,
   *           but the row position was {@code i}.
   */
  public Mat row(int i) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(i), Span.all())) {
      throw new ArrayIndexOutOfBoundsException("The row position is out of bound. The matrix contains " + n_rows + " rows, but the row position was  " + i + ".");
    }

    Mat result = new Mat(1, n_cols);
    for (int n = 0; n < n_cols; n++) {
      result._matrix.set(n, _matrix.get(i, n));
    }

    return result;
  }

  /**
   * Performs a unary operation on all elements of the {@code i}th row and overwrites each element with the result.
   * 
   * @param i The row position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The row position is out of bound. The matrix contains {@link #n_rows} rows,
   *           but the row position was {@code i}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void row(int i, Op operator) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(i), Span.all())) {
      throw new ArrayIndexOutOfBoundsException("The row position is out of bound. The matrix contains " + n_rows + " rows, but the row position was  " + i + ".");
    }

    for (int j = 0; j < n_cols; j++) {
      at(i, j, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code i}th row and overwrites each
   * element with the result.
   * 
   * @param i The row position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but was {@code n_cols} and {@link #n_elem operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The row position is out of bound. The matrix contains {@link #n_rows} rows,
   *           but the row position was {@code i}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void row(int i, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (operand.n_elem != n_cols) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + n_cols + " and " + operand.n_elem + ".");
    }

    if (!in_range(new Span(i), Span.all())) {
      throw new ArrayIndexOutOfBoundsException("The row position is out of bound. The matrix contains " + n_rows + " rows, but the row position was  " + i + ".");
    }

    for (int j = 0; j < n_cols; j++) {
      at(i, j, operator, operand.at(j));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code i}th row and overwrites each
   * element with the result.
   * 
   * @param i The row position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The row position is out of bound. The matrix contains {@link #n_rows} rows,
   *           but the row position was {@code i}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void row(int i, Op operator, double operand) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(i), Span.all())) {
      throw new ArrayIndexOutOfBoundsException("The row position is out of bound. The matrix contains " + n_rows + " rows, but the row position was  " + i + ".");
    }
    for (int j = 0; j < n_cols; j++) {
      at(i, j, operator, operand);
    }
  }

  /**
   * Returns all elements in the {@code i}th row and {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param i The row position
   * @return The submatrix
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from row {@code i} and column {@code a} to
   *           {@code b}.
   */
  public Mat row(int a, int b, int i) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(i), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from row " + i + " and column " + a + " to " + b + ".");
    }

    Mat result = new Mat(1, b - a + 1);
    int resultN = 0;
    for (int j = a; j <= b; j++) {
      result._matrix.set(resultN++, _matrix.get(i, j));
    }

    return result;
  }

  /**
   * Performs a unary operation on all elements of the {@code j}th row and {@code a}th to {@code b}th column and
   * overwrites each element with the result.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param i The row position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from row {@code i} and column {@code a} to
   *           {@code b}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void row(int a, int b, int i, Op operator) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(i), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from row " + i + " and column " + a + " to " + b + ".");
    }

    for (int j = a; j <= b; j++) {
      at(i, j, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code j}th row and {@code a}th to
   * {@code b}th column and overwrites each element with the result.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param i The row position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@code b} - {@code a} + 1 and {@link #n_elem operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from row {@code i} and column {@code a} to
   *           {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void row(int a, int b, int i, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (operand.n_elem != b - a + 1) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right hand side operand, but were " + (b - a + 1) + " and " + operand.n_elem + ".");
    }

    if (!in_range(new Span(i), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from row " + i + " and column " + a + " to " + b + ".");
    }

    int operandN = 0;
    for (int j = a; j <= b; j++) {
      at(i, j, operator, operand.at(operandN++));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code j}th row and {@code a}th to
   * {@code b}th column and overwrites each element with the result.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param i The row position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from row {@code i} and column {@code a} to
   *           {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void row(int a, int b, int i, Op operator, double operand) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(i), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from row " + i + " and column " + a + " to " + b + ".");
    }

    for (int j = a; j <= b; j++) {
      at(i, j, operator, operand);
    }
  }

  /**
   * Returns the {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @return The columns
   * 
   * @throws ArrayIndexOutOfBoundsException The column positions are out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column positions are from {@code a} to {@code b}.
   */
  public Mat cols(int a, int b) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column positions are out of bound. The matrix contains " + n_cols + " columns, but the column positions are from  " + a + " to " + b + ".");
    }

    Mat result = new Mat(n_rows, b - a + 1);
    int resultN = 0;
    for (int i = 0; i < n_rows; i++) {
      for (int j = a; j <= b; j++) {
        result._matrix.set(resultN, _matrix.get(i, j));
      }
    }

    return result;
  }

  /**
   * Performs a unary operation on all elements of the {@code a}th to {@code b}th column and overwrites each element
   * with the result.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The column positions are out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void cols(int a, int b, Op operator) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column positions are out of bound. The matrix contains " + n_cols + " columns, but the column positions are from  " + a + " to " + b + ".");
    }

    for (int i = 0; i < n_rows; i++) {
      for (int j = a; j <= b; j++) {
        at(i, j, operator);
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code a}th to {@code b}th column and
   * overwrites each element with the result.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were ({@code b} - {@code a} + 1) * {@link #n_rows} and {@link #n_elem
   *           operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The column positions are out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void cols(int a, int b, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (operand.n_elem != (b - a + 1) * n_rows) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + ((b - a + 1) * n_rows) + " and " + operand.n_elem + ".");
    }

    if (!in_range(new Span(), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column positions are out of bound. The matrix contains " + n_cols + " columns, but the column positions are from  " + a + " to " + b + ".");
    }

    int operandN = 0;
    for (int i = 0; i < n_rows; i++) {
      for (int j = a; j <= b; j++) {
        at(i, j, operator, operand.at(operandN++));
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code a}th to {@code b}th column and
   * overwrites each element with the result.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The column positions are out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void cols(int a, int b, Op operator, double operand) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column positions are out of bound. The matrix contains " + n_cols + " columns, but the column positions are from  " + a + " to " + b + ".");
    }

    for (int i = 0; i < n_rows; i++) {
      for (int j = a; j <= b; j++) {
        at(i, j, operator, operand);
      }
    }
  }

  /**
   * Returns the {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @return The rows
   * 
   * @throws ArrayIndexOutOfBoundsException The row positions are out of bound. The matrix contains {@link #n_rows}
   *           rows, but the row positions are from {@code a} to {@code b}.
   */
  public Mat rows(int a, int b) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(a, b), Span.all())) {
      throw new IllegalArgumentException("The row positions are out of bound. The matrix contains " + n_rows + " rows, but the row positions are from " + a + " to " + b + ".");
    }

    Mat result = new Mat(b - a + 1, n_cols);
    int resultN = 0;
    for (int i = a; i <= b; i++) {
      for (int j = 0; j < n_cols; j++) {
        result._matrix.set(resultN, _matrix.get(i, j));
      }
    }

    return result;
  }

  /**
   * Performs a unary operation on all elements of the {@code a}th to {@code b}th row and overwrites each element with
   * the result.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The row positions are out of bound. The matrix contains {@link #n_rows}
   *           rows, but the row positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void rows(int a, int b, Op operator) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(a, b), Span.all())) {
      throw new IllegalArgumentException("The row positions are out of bound. The matrix contains " + n_rows + " rows, but the row positions are from " + a + " to " + b + ".");
    }

    for (int i = a; i <= b; i++) {
      for (int j = 0; j < n_cols; j++) {
        at(i, j, operator);
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code a}th to {@code b}th row and
   * overwrites each element with the result.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were ({@code b} - {@code a} + 1) * {@link #n_cols} and {@link #n_elem
   *           operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The row positions are out of bound. The matrix contains {@link #n_rows}
   *           rows, but the row positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void rows(int a, int b, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (operand.n_elem != (b - a + 1) * n_cols) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + ((b - a + 1) * n_cols) + " and " + operand.n_elem + ".");
    }

    if (!in_range(new Span(a, b), Span.all())) {
      throw new IllegalArgumentException("The row positions are out of bound. The matrix contains " + n_rows + " rows, but the row positions are from " + a + " to " + b + ".");
    }

    int operandN = 0;
    for (int i = a; i <= b; i++) {
      for (int j = 0; j < n_cols; j++) {
        at(i, j, operator, operand.at(operandN++));
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code a}th to {@code b}th row and
   * overwrites each element with the result.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The row positions are out of bound. The matrix contains {@link #n_rows}
   *           rows, but the row positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void rows(int a, int b, Op operator, double operand) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(a, b), Span.all())) {
      throw new IllegalArgumentException("The row positions are out of bound. The matrix contains " + n_rows + " rows, but the row positions are from " + a + " to " + b + ".");
    }

    for (int i = a; i <= b; i++) {
      for (int j = 0; j < n_cols; j++) {
        at(i, j, operator, operand);
      }
    }
  }

  /**
   * Performs a unary operation on all elements the matrix and overwrites each element with the result.
   * 
   * @param operator The operator
   * 
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void submat(Op operator) throws UnsupportedOperationException {
    for (int n = 0; n < n_elem; n++) {
      _matrix.set(n, Op.getResult(_matrix.get(n), operator));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements the matrix and overwrites each element with the
   * result.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem} and {@code operand.n_elem}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void submat(Op operator, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    if (operand.n_elem != n_elem) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right hand side operand, but were " + n_elem + " and " + operand.n_elem + ".");
    }

    for (int n = 0; n < n_elem; n++) {
      _matrix.set(n, Op.getResult(_matrix.get(n), operator, operand._matrix.get(n)));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements the matrix and overwrites each element with the
   * result.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void submat(Op operator, double operand) throws UnsupportedOperationException {
    for (int n = 0; n < n_elem; n++) {
      _matrix.set(n, Op.getResult(_matrix.get(n), operator, operand));
    }
  }

  /**
   * Returns all elements in the {@code ai}th to {@code bi}th row and {@code aj}th and {@code bj}th column.
   * 
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   * @return The submatrix
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from row {@code ai} to {@code bi} and column
   *           {@code aj} to {@code bj}.
   */
  public Mat submat(int ai, int bi, int aj, int bj) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(ai, bi), new Span(aj, bj))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from row " + ai + " to " + bi + " and column " + aj + " to " + bj + ".");
    }

    Mat result = new Mat(bi - ai + 1, bj - aj + 1);
    int resultN = 0;
    for (int i = ai; i <= bi; i++) {
      for (int j = aj; j <= bj; j++) {
        result._matrix.set(resultN++, _matrix.get(i, j));
      }
    }

    return result;
  }

  /**
   * Performs a unary operation on all elements in the {@code ai}th to {@code bi}th row and {@code aj}th and {@code bj}
   * th column and overwrites each element with the result.
   * 
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from row {@code ai} to {@code bi} and column
   *           {@code aj} to {@code bj}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(ai, bi), new Span(aj, bj))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from row " + ai + " to " + bi + " and column " + aj + " to " + bj + ".");
    }

    for (int i = ai; i <= bi; i++) {
      for (int j = aj; j <= bj; j++) {
        at(i, j, operator);
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements in the {@code ai}th to {@code bi}th row and
   * {@code aj}th and {@code bj}th column and overwrites each element with the result.
   * 
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were ({@code bi} - {@code ai} + 1) * ({@code bj} - {@code aj} + 1) and
   *           {@link #n_elem operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from row {@code ai} to {@code bi} and column
   *           {@code aj} to {@code bj}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (operand.n_elem != (bi - ai + 1) * (bj - aj + 1)) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + ((bi - ai + 1) * (bj - aj + 1)) + " and " + operand.n_elem + ".");
    }

    if (!in_range(new Span(ai, bi), new Span(aj, bj))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from row " + ai + " to " + bi + " and column " + aj + " to " + bj + ".");
    }

    int operandN = 0;
    for (int i = ai; i <= bi; i++) {
      for (int j = aj; j <= bj; j++) {
        at(i, j, operator, operand.at(operandN++));
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements in the {@code ai}th to {@code bi}th row and
   * {@code aj}th and {@code bj}th column and overwrites each element with the result.
   * 
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws ArrayIndexOutOfBoundsException The row and column positions are out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}), but the position were from row {@code ai} to {@code bi} and column
   *           {@code aj} to {@code bj}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator, double operand) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!in_range(new Span(ai, bi), new Span(aj, bj))) {
      throw new ArrayIndexOutOfBoundsException("The column und row positions are out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the positions are from row " + ai + " to " + bi + " and column " + aj + " to " + bj + ".");
    }

    for (int i = ai; i <= bi; i++) {
      for (int j = aj; j <= bj; j++) {
        at(i, j, operator, operand);
      }
    }
  }

  /**
   * Returns the {@code a}th to {@code b}th element.
   * <p>
   * <b>Non-canonical:</b> Returns a row/column vector if this is invoked for a row/column vector.
   * 
   * @param a The first element position
   * @param b The last element position
   * @return The elements
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   * @throws ArrayIndexOutOfBoundsException The positions are out of bound. The vector contains {@link #n_elem}
   *           elements, but the positions are from {@code a} to {@code b}.
   */
  public Mat subvec(int a, int b) throws UnsupportedOperationException, ArrayIndexOutOfBoundsException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    if (!in_range(new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The positions are out of bound. The vector contains " + n_elem + " elements, but the positions are from " + a + " to " + b + ".");
    }

    Mat result;
    if (is_colvec()) {
      result = new Mat(b - a + 1, 1);
    } else {
      result = new Mat(1, b - a + 1);
    }

    int resultN = 0;
    for (int n = a; n <= b; n++) {
      result._matrix.set(resultN++, _matrix.get(n));
    }

    return result;
  }

  /**
   * Performs a unary operation on the {@code a}th to {@code b}th element and overwrites each element with the result.
   * 
   * @param a The first element position
   * @param b The last element position
   * @param operator The operator
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   * @throws ArrayIndexOutOfBoundsException The positions are out of bound. The vector contains {@link #n_elem}
   *           elements, but the positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void subvec(int a, int b, Op operator) throws UnsupportedOperationException, ArrayIndexOutOfBoundsException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    if (!in_range(new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The positions are out of bound. The vector contains " + n_elem + " elements, but the positions are from " + a + " to " + b + ".");
    }

    for (int n = a; n <= b; n++) {
      at(n, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on the {@code a}th to {@code b}th element and overwrites each
   * element with the result.
   * 
   * @param a The first element position
   * @param b The last element position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@code b} - {@code a} + 1 and {@link #n_elem operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The positions are out of bound. The vector contains {@link #n_elem}
   *           elements, but the positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void subvec(int a, int b, Op operator, Mat operand) throws UnsupportedOperationException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    if (operand.n_elem != b - a + 1) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + (b - a + 1) + " and " + operand.n_elem + ".");
    }

    if (!in_range(new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The positions are out of bound. The vector contains " + n_elem + " elements, but the positions are from " + a + " to " + b + ".");
    }

    int operandN = 0;
    for (int n = a; n <= b; n++) {
      at(n, operator, at(operandN++));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on the {@code a}th to {@code b}th element and overwrites each
   * element with the result.
   * 
   * @param a The first element position
   * @param b The last element position
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   * @throws ArrayIndexOutOfBoundsException The positions are out of bound. The vector contains {@link #n_elem}
   *           elements, but the positions are from {@code a} to {@code b}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void subvec(int a, int b, Op operator, double operand) throws UnsupportedOperationException, ArrayIndexOutOfBoundsException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    if (!in_range(new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The positions are out of bound. The vector contains " + n_elem + " elements, but the positions are from " + a + " to " + b + ".");
    }

    for (int n = a; n <= b; n++) {
      at(n, operator, operand);
    }
  }

  /**
   * Returns all elements specified in the selection – a vector of positions – as a column vector.
   * 
   * @param selection The element positions
   * @return The elements
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   */
  public Mat elem(Mat selection) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    if (selection.n_elem > 0) {
      Mat result = new Mat(selection.n_elem, 1);

      for (int n = 0; n < selection.n_elem; n++) {
        double elementDouble = selection._matrix.get(n);
        int elementInt = (int) elementDouble;

        // Will also fail if the value is negative, which are also not allowed.
        if (elementDouble != elementInt) {
          throw new IllegalArgumentException("Each position must be a integer value, but one was " + elementDouble + ".");
        }

        result._matrix.set(n, at(elementInt));
      }

      return result;
    } else {
      return new Mat();
    }
  }

  /**
   * Performs a unary operation on all elements specified in the selection – a vector of positions – and overwrites each
   * element with the result.
   * 
   * @param selection The element positions
   * @param operator The operator
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void elem(Mat selection, Op operator) {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < selection.n_elem; n++) {
      double elementDouble = selection._matrix.get(n);
      int elementInt = (int) elementDouble;

      // Will also fail if the value is negative, which are also not allowed.
      if (elementDouble != elementInt) {
        throw new IllegalArgumentException("Each position must be a integer values, but one was " + elementDouble + ".");
      }

      at(elementInt, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements specified in the selection – a vector of
   * positions – and overwrites each element with the result.
   * 
   * @param selection The element positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem selection.n_elem} and {@code operand.n_elem}.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@code n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void elem(Mat selection, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    if (operand.n_elem != selection.n_elem) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + selection.n_elem + " and " + operand.n_elem + ".");
    }

    int indexN = 0;
    for (int n = 0; n < selection.n_elem; n++) {
      double elementDouble = selection._matrix.get(n);
      int elementInt = (int) elementDouble;

      // Will also fail if the value is negative, which are also not allowed.
      if (elementDouble != elementInt) {
        throw new IllegalArgumentException("Each position must be a integer values, but one was " + elementDouble + ".");
      }

      at(elementInt, operator, operand.at(indexN++));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements specified in the selection – a vector of
   * positions – and overwrites each element with the result.
   * 
   * @param selection The element positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void elem(Mat selection, Op operator, double operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < selection.n_elem; n++) {
      double elementDouble = selection._matrix.get(n);
      int elementInt = (int) elementDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (elementDouble != elementInt) {
        throw new IllegalArgumentException("Each position must be a integer values, but one was " + elementDouble + ".");
      }

      at(elementInt, operator, operand);
    }
  }

  /**
   * Returns all columns specified in the selection – a vector of column positions.
   * 
   * @param selection The column positions
   * @return The elements
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   */
  public Mat cols(Mat selection) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    if (selection.n_elem > 0) {
      Mat result = new Mat(n_rows, selection.n_elem);

      int resultN = 0;
      for (int n = 0; n < selection.n_elem; n++) {
        double jDouble = selection._matrix.get(n);
        int jInt = (int) jDouble;

        // Will also fail if the value is negative, which is also not allowed.
        if (jDouble != jInt) {
          throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
        }

        for (int i = 0; i < n_rows; i++) {
          result._matrix.set(resultN++, _matrix.get(i, jInt));
        }
      }

      return result;
    } else {
      return new Mat();
    }
  }

  /**
   * Performs a unary operation on all columns specified in the selection – a vector of column positions – and
   * overwrites each element with the result.
   * 
   * @param selection The column positions
   * @param operator The operator
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void cols(Mat selection, Op operator) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < selection.n_elem; n++) {
      double jDouble = selection._matrix.get(n);
      int jInt = (int) jDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (jDouble != jInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
      }

      col(jInt, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all columns specified in the selection – a vector of
   * column positions – and overwrites each element with the result.
   * 
   * @param selection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem selection.n_elem} and {@code operand.n_elem}.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@code n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void cols(Mat selection, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    if (operand.n_elem != selection.n_elem) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + selection.n_elem + " and " + operand.n_elem + ".");
    }

    for (int n = 0; n < selection.n_elem; n++) {
      double jDouble = selection._matrix.get(n);
      int jInt = (int) jDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (jDouble != jInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
      }

      col(jInt, operator, operand);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all columns specified in the selection – a vector of
   * column positions – and overwrites each element with the result.
   * 
   * @param selection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@code n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void cols(Mat selection, Op operator, double operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < selection.n_elem; n++) {
      double jDouble = selection._matrix.get(n);
      int jInt = (int) jDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (jDouble != jInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
      }

      col(jInt, operator, operand);
    }
  }

  /**
   * Returns all rows specified in the selection – a vector of row positions.
   * 
   * @param selection The row positions
   * @return The elements
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   */
  public Mat rows(Mat selection) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    if (selection.n_elem > 0) {
      Mat result = new Mat(selection.n_elem, n_cols);

      int resultN = 0;
      for (int n = 0; n < selection.n_elem; n++) {
        double iDouble = selection._matrix.get(n);
        int iInt = (int) iDouble;

        // Will also fail if the value is negative, which is also not allowed.
        if (iDouble != iInt) {
          throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
        }

        for (int j = 0; j < n_cols; j++) {
          result._matrix.set(resultN++, _matrix.get(iInt, j));
        }
      }

      return result;
    } else {
      return new Mat();
    }
  }

  /**
   * Performs a unary operation on all rows specified in the selection – a vector of row positions – and overwrites each
   * element with the result.
   * 
   * @param selection The row positions
   * @param operator The operator
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void rows(Mat selection, Op operator) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < selection.n_elem; n++) {
      double iDouble = selection._matrix.get(n);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      row(iInt, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all rows specified in the selection – a vector of row
   * positions – and overwrites each element with the result.
   * 
   * @param selection The row positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem selection.n_elem} and {@code operand.n_elem}.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@code n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void rows(Mat selection, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    if (operand.n_elem != selection.n_elem) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + selection.n_elem + " and " + operand.n_elem + ".");
    }

    for (int n = 0; n < selection.n_elem; n++) {
      double iDouble = selection._matrix.get(n);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      row(iInt, operator, operand);
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all rows specified in the selection – a vector of row
   * positions – and overwrites each element with the result.
   * 
   * @param selection The row positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@code n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void rows(Mat selection, Op operator, double operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < selection.n_elem; n++) {
      double iDouble = selection._matrix.get(n);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      row(iInt, operator, operand);
    }
  }

  /**
   * Returns all elements in the specified selection – a cartesian product of a vector of row positions and column
   * positions.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @return The elements
   * 
   * @throws IllegalArgumentException The rowSelection must be a vector, but was a ({@link #n_rows rowSelection.n_rows},
   *           {@link #n_cols rowSelection.n_cols})-matrix.
   * @throws IllegalArgumentException The columnSelection must be a vector, but was a ({@code columnSelection.n_rows},
   *           {@code columnSelection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   */
  public Mat submat(Mat rowSelection, Mat columnSelection) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    if (!rowSelection.is_vec()) {
      throw new UnsupportedOperationException("The rowSelection must be a vector, but was a (" + rowSelection.n_rows + ", " + rowSelection.n_cols + ")-matrix.");
    }

    if (!columnSelection.is_vec()) {
      throw new UnsupportedOperationException("The columnSelection must be a vector, but was a (" + columnSelection.n_rows + ", " + columnSelection.n_cols + ")-matrix.");
    }

    if (rowSelection.n_elem > 0 && columnSelection.n_elem > 0) {
      Mat result = new Mat(rowSelection.n_elem, columnSelection.n_elem);

      int resultN = 0;
      for (int i = 0; i < rowSelection.n_elem; i++) {
        double iDouble = rowSelection._matrix.get(i);
        int iInt = (int) iDouble;

        // Will also fail if the value is negative, which is also not allowed.
        if (iDouble != iInt) {
          throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
        }

        for (int j = 0; j < columnSelection.n_elem; j++) {
          double jDouble = columnSelection._matrix.get(j);
          int jInt = (int) jDouble;

          // Will also fail if the value is negative, which is also not allowed.
          if (jDouble != jInt) {
            throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
          }

          result._matrix.set(resultN++, _matrix.get(iInt, jInt));
        }
      }

      return result;
    } else {
      return new Mat();
    }
  }

  /**
   * Performs a unary operation on all elements in the specified selection – a cartesian product of a vector of row
   * positions and column positions – and overwrites each element with the result.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @param operator The operator
   * 
   * @throws IllegalArgumentException The rowSelection must be a vector, but was a ({@link #n_rows rowSelection.n_rows},
   *           {@link #n_cols rowSelection.n_cols})-matrix.
   * @throws IllegalArgumentException The columnSelection must be a vector, but was a ({@code columnSelection.n_rows},
   *           {@code columnSelection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void submat(Mat rowSelection, Mat columnSelection, Op operator) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!rowSelection.is_vec()) {
      throw new UnsupportedOperationException("The rowSelection must be a vector, but was a (" + rowSelection.n_rows + ", " + rowSelection.n_cols + ")-matrix.");
    }

    if (!columnSelection.is_vec()) {
      throw new UnsupportedOperationException("The columnSelection must be a vector, but was a (" + columnSelection.n_rows + ", " + columnSelection.n_cols + ")-matrix.");
    }

    for (int i = 0; i < rowSelection.n_elem; i++) {
      double iDouble = rowSelection._matrix.get(i);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      for (int j = 0; j < columnSelection.n_elem; j++) {
        double jDouble = columnSelection._matrix.get(j);
        int jInt = (int) jDouble;

        // Will also fail if the value is negative, which is also not allowed.
        if (jDouble != jInt) {
          throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
        }

        at(iInt, jInt, operator);
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements in the specified selection – a cartesian product
   * of a vector of row positions and column positions – and overwrites each element with the result.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The rowSelection must be a vector, but was a ({@link #n_rows rowSelection.n_rows},
   *           {@link #n_cols rowSelection.n_cols})-matrix.
   * @throws IllegalArgumentException The columnSelection must be a vector, but was a ({@code columnSelection.n_rows},
   *           {@code columnSelection.n_cols})-matrix.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem rowSelection.n_elem} * {@code columnSelection.n_elem} and
   *           {@code operand.n_elem}.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void submat(Mat rowSelection, Mat columnSelection, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!rowSelection.is_vec()) {
      throw new UnsupportedOperationException("The rowSelection must be a vector, but was a (" + rowSelection.n_rows + ", " + rowSelection.n_cols + ")-matrix.");
    }

    if (!columnSelection.is_vec()) {
      throw new UnsupportedOperationException("The columnSelection must be a vector, but was a (" + columnSelection.n_rows + ", " + columnSelection.n_cols + ")-matrix.");
    }

    if (operand.n_elem != rowSelection.n_elem * columnSelection.n_elem) {
      throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + rowSelection.n_elem * columnSelection.n_elem + " and " + operand.n_elem + ".");
    }

    for (int i = 0; i < rowSelection.n_elem; i++) {
      double iDouble = rowSelection._matrix.get(i);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      for (int j = 0; j < columnSelection.n_elem; j++) {
        double jDouble = columnSelection._matrix.get(j);
        int jInt = (int) jDouble;

        // Will also fail if the value is negative, which is also not allowed.
        if (jDouble != jInt) {
          throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
        }

        at(iInt, jInt, operator);
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements in the specified selection – a cartesian product
   * of a vector of row positions and column positions – and overwrites each element with the result.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The rowSelection must be a vector, but was a ({@link #n_rows rowSelection.n_rows},
   *           {@link #n_cols rowSelection.n_cols})-matrix.
   * @throws IllegalArgumentException The columnSelection must be a vector, but was a ({@code columnSelection.n_rows},
   *           {@code columnSelection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void submat(Mat rowSelection, Mat columnSelection, Op operator, double operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!rowSelection.is_vec()) {
      throw new UnsupportedOperationException("The rowSelection must be a vector, but was a (" + rowSelection.n_rows + ", " + rowSelection.n_cols + ")-matrix.");
    }

    if (!columnSelection.is_vec()) {
      throw new UnsupportedOperationException("The columnSelection must be a vector, but was a (" + columnSelection.n_rows + ", " + columnSelection.n_cols + ")-matrix.");
    }

    for (int i = 0; i < rowSelection.n_elem; i++) {
      double iDouble = rowSelection._matrix.get(i);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      for (int j = 0; j < columnSelection.n_elem; j++) {
        double jDouble = columnSelection._matrix.get(j);
        int jInt = (int) jDouble;

        // Will also fail if the value is negative, which is also not allowed.
        if (jDouble != jInt) {
          throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
        }

        at(iInt, jInt, operator);
      }
    }
  }

  /**
   * Returns the main diagonal of the matrix as a column vector.
   * 
   * @return The diagonal
   */
  public Mat diag() {
    return diag(0);
  }

  /**
   * Performs a unary operation on all elements of the main diagonal of the matrix and overwrites each element with the
   * result.
   * 
   * @param operator The operator
   * 
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void diag(Op operator) throws UnsupportedOperationException {
    diag(0, operator);
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the main diagonal of the matrix and overwrites
   * each element with the result.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were min({@link #n_cols}, {@link #n_rows}) and {@link #n_elem operand.n_elem}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void diag(Op operator, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    diag(0, operator, operand);
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the main diagonal of the matrix and overwrites
   * each element with the result.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void diag(Op operator, double operand) throws UnsupportedOperationException {
    diag(0, operator, operand);
  }

  /**
   * Returns the {@code k}th diagonal of the matrix as a column vector.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @return The diagonal
   * 
   * @throws ArrayIndexOutOfBoundsException The diagonal is out of bound. The matrix is of size ({@link #n_rows},
   *           {@link #n_cols}), but the diagonal position was {@code k}.
   */
  public Mat diag(int k) throws ArrayIndexOutOfBoundsException {
    if (k <= n_rows || k >= n_cols) {
      throw new ArrayIndexOutOfBoundsException("The diagonal is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the diagonal position was " + k + ".");
    }

    Mat result;
    if (k > 0) {
      int length = Math.min(n_rows, n_cols - k);

      result = new Mat(length, 1);
      for (int n = 0; n < length; n++) {
        result._matrix.set(n, _matrix.get(n, n + k));
      }
    } else {
      int length = Math.min(n_rows + k, n_cols);

      result = new Mat(length, 1);
      for (int n = 0; n < length; n++) {
        result._matrix.set(n, _matrix.get(n - k, n));
      }
    }

    return result;
  }

  /**
   * Performs a unary operation on all elements of the {@code k}th diagonal of the matrix and overwrites each element
   * with the result.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @param operator The operator
   * 
   * @throws ArrayIndexOutOfBoundsException The diagonal is out of bound. The matrix is of size ({@link #n_rows},
   *           {@link #n_cols}), but the diagonal position was {@code k}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void diag(int k, Op operator) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (k <= n_rows || k >= n_cols) {
      throw new ArrayIndexOutOfBoundsException("The diagonal is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the diagonal position was " + k + ".");
    }

    if (k > 0) {
      int length = Math.min(n_rows, n_cols - k);

      for (int n = 0; n < length; n++) {
        at(n, n + k, operator);
      }
    } else {
      int length = Math.min(n_rows + k, n_cols);

      for (int n = 0; n < length; n++) {
        at(n - k, n, operator);
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code k}th diagonal of the matrix and
   * overwrites each element with the result.
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
   * 
   * @throws ArrayIndexOutOfBoundsException The diagonal is out of bound. The matrix is of size ({@link #n_rows},
   *           {@link #n_cols}), but the diagonal position was {@code k}.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were min({@link #n_cols}, {@link #n_rows}) and {@link #n_elem operand.n_elem}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void diag(int k, Op operator, Mat operand) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, UnsupportedOperationException {
    if (k <= n_rows || k >= n_cols) {
      throw new ArrayIndexOutOfBoundsException("The diagonal is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the diagonal position was " + k + ".");
    }

    if (k > 0) {
      int length = Math.min(n_rows, n_cols - k);

      if (operand.n_elem != length) {
        throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + length + " and " + operand.n_elem + ".");
      }

      for (int n = 0; n < length; n++) {
        at(n, n + k, operator, operand.at(n));
      }
    } else {
      int length = Math.min(n_rows + k, n_cols);

      if (operand.n_elem != length) {
        throw new IllegalArgumentException("The number of elements of the left-hand side operand must match with the right-hand side operand, but were " + length + " and " + operand.n_elem + ".");
      }

      for (int n = 0; n < length; n++) {
        at(n - k, n, operator, operand.at(n));
      }
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the {@code k}th diagonal of the matrix and
   * overwrites each element with the result.
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
   * 
   * @throws ArrayIndexOutOfBoundsException The diagonal is out of bound. The matrix is of size ({@link #n_rows},
   *           {@link #n_cols}), but the diagonal position was {@code k}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void diag(int k, Op operator, double operand) throws ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (k <= n_rows || k >= n_cols) {
      throw new ArrayIndexOutOfBoundsException("The diagonal is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + "), but the diagonal position was " + k + ".");
    }

    if (k > 0) {
      int length = Math.min(n_rows, n_cols - k);

      for (int n = 0; n < length; n++) {
        at(n, n + k, operator, operand);
      }
    } else {
      int length = Math.min(n_rows + k, n_cols);

      for (int n = 0; n < length; n++) {
        at(n - k, n, operator, operand);
      }
    }
  }

  /**
   * Performs a unary operation per column and overwrites each column with the result.
   * 
   * @param operator The operator
   * 
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void each_col(Op operator) throws UnsupportedOperationException {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation per column and overwrites each column with the result.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@code n_rows} and {@link #n_elem operand.n_elem}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void each_col(Op operator, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator, operand);
    }
  }

  /**
   * Performs a right-hand side element-wise operation per column and overwrites each column with the result.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void each_col(Op operator, double operand) throws UnsupportedOperationException {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator, operand);
    }
  }

  /**
   * Performs a unary operation per column specified in the selection – a vector of column positions – and overwrites
   * each column with the result.
   * 
   * @param selection The column positions
   * @param operator The operator
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void each_col(Mat selection, Op operator) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < n_cols; n++) {
      double jDouble = selection._matrix.get(n);
      int jInt = (int) jDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (jDouble != jInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
      }

      col(jInt, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation per column specified in the selection – a vector of column
   * positions – and overwrites each column with the result.
   * 
   * @param selection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@code n_rows} and {@link #n_elem operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void each_col(Mat selection, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < n_cols; n++) {
      double jDouble = selection._matrix.get(n);
      int jInt = (int) jDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (jDouble != jInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
      }

      col(jInt, operator, operand);
    }
  }

  /**
   * Performs a right-hand side element-wise operation per column specified in the selection – a vector of column
   * positions – and overwrites each column with the result.
   * 
   * @param selection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void each_col(Mat selection, Op operator, double operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < n_cols; n++) {
      double jDouble = selection._matrix.get(n);
      int jInt = (int) jDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (jDouble != jInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + jDouble + ".");
      }

      col(jInt, operator, operand);
    }
  }

  /**
   * Performs a unary operation per row and overwrites each row with the result.
   * 
   * @param operator The operator
   * 
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void each_row(Op operator) throws UnsupportedOperationException {
    for (int i = 0; i < n_cols; i++) {
      row(i, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation per row and overwrites each row with the result.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@code n_rows} and {@link #n_elem operand.n_elem}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void each_row(Op operator, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    for (int i = 0; i < n_rows; i++) {
      row(i, operator, operand);
    }
  }

  /**
   * Performs a right-hand side element-wise operation per row and overwrites each row with the result.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void each_row(Op operator, double operand) throws UnsupportedOperationException {
    for (int i = 0; i < n_rows; i++) {
      row(i, operator, operand);
    }
  }

  /**
   * Performs a unary operation per row specified in the selection – a vector of row positions – and overwrites each row
   * with the result.
   * 
   * @param selection The row positions
   * @param operator The operator
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  public void each_row(Mat selection, Op operator) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < n_cols; n++) {
      double iDouble = selection._matrix.get(n);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      row(iInt, operator);
    }
  }

  /**
   * Performs a right-hand side element-wise operation per row specified in the selection – a vector of row positions –
   * and overwrites each row with the result.
   * 
   * @param selection The row positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@code n_rows} and {@link #n_elem operand.n_elem}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void each_row(Mat selection, Op operator, Mat operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < n_cols; n++) {
      double iDouble = selection._matrix.get(n);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      row(iInt, operator, operand);
    }
  }

  /**
   * Performs a right-hand side element-wise operation per row specified in the selection – a vector of row positions –
   * and overwrites each row with the result.
   * 
   * @param selection The row positions
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException The selection must be a vector, but was a ({@link #n_rows selection.n_rows},
   *           {@link #n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException Each position must be a integer value, but one was {@code selection.at(n)}.
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  public void each_row(Mat selection, Op operator, double operand) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new UnsupportedOperationException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
    }

    for (int n = 0; n < n_cols; n++) {
      double iDouble = selection._matrix.get(n);
      int iInt = (int) iDouble;

      // Will also fail if the value is negative, which is also not allowed.
      if (iDouble != iInt) {
        throw new IllegalArgumentException("Each position must be a integer value, but one was " + iDouble + ".");
      }

      row(iInt, operator, operand);
    }
  }

  /**
   * Creates a matrix being the addition of this matrix with a right-hand side addend
   * 
   * @param operand The addend
   * @return The matrix
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem} and {@code operand.n_elem}.
   */
  public Mat plus(Mat operand) throws IllegalArgumentException {
    Mat result = new Mat(_matrix);
    result.submat(Op.PLUS, operand);
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
    result.submat(Op.PLUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the subtration of this matrix with a right-hand side subtrahend
   * 
   * @param operand The subtrahend
   * @return The matrix
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem} and {@code operand.n_elem}.
   */
  public Mat minus(Mat operand) throws IllegalArgumentException {
    Mat result = new Mat(_matrix);
    result.submat(Op.MINUS, operand);
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
    result.submat(Op.MINUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   * 
   * @throws IllegalArgumentException The number of columns of the left-hand side operand must match with the number of
   *           rows of the right hand side operand, but were {@link #n_cols} and {@link #n_rows operand.n_rows}.
   */
  public Mat times(Mat operand) {
    if (n_cols != operand.n_rows) {
      throw new IllegalArgumentException("The number of columns of the left-hand side operand must match with the number of rows of the right hand side operand, but were " + n_cols + " and " + operand.n_rows + ".");
    }

    // Performed by EJML, since non-element-wise multiplication is needed.
    Mat result = new Mat(operand.n_rows, operand.n_cols);
    CommonOps.mult(_matrix, operand._matrix, result._matrix);
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
    result.submat(Op.TIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem} and {@code operand.n_elem}.
   */
  public Mat elemTimes(Mat operand) throws IllegalArgumentException {
    Mat result = new Mat(_matrix);
    result.submat(Op.ELEMTIMES, operand);
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
    result.submat(Op.ELEMTIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise division of this matrix with a right-hand side divisor
   * 
   * @param operand The multiplier
   * @return The matrix
   * 
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link #n_elem} and {@code operand.n_elem}.
   */
  public Mat elemDivide(Mat operand) throws IllegalArgumentException {
    Mat result = new Mat(_matrix);
    result.submat(Op.ELEMDIVIDE, operand);
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
    result.submat(Op.ELEMDIVIDE, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise negation of this matrix
   * 
   * @return The matrix
   */
  public Mat negate() {
    Mat result = new Mat(_matrix);

    for (int n = 0; n < n_elem; n++) {
      result._matrix.set(n, _matrix.get(n));
    }

    return result;
  }

  /**
   * Returns a reference to the internal data representation of the matrix.
   * 
   * @return The reference
   */
  public DenseMatrix64F memptr() {
    return _matrix;
  }

  /**
   * Sets all elements to 0.
   */
  public void zeros() {
    fill(0);
  }

  /**
   * Resizes the vector and sets all elements to 0.
   * <p>
   * <b>Non-canonical:</b> Remains a row/column vector if this is invoked for a row/column vector.
   * 
   * @param numberOfElements The number of elements
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   */
  public void zeros(int numberOfElements) throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    set_size(numberOfElements);
    zeros();
  }

  /**
   * Resizes the matrix and sets all elements to 0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void zeros(int numberOfRows, int numberOfColumns) {
    set_size(numberOfRows, numberOfColumns);
    zeros();
  }

  /**
   * Sets all elements to 1.
   */
  public void ones() {
    fill(1);
  }

  /**
   * Resizes the vector and sets all elements to 1.
   * <p>
   * <b>Non-canonical:</b> Remains a row/column vector if this is invoked for a row/column vector.
   * 
   * @param numberOfElements The number of elements
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   */
  public void ones(int numberOfElements) throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    set_size(numberOfElements);
    ones();
  }

  /**
   * Resizes the matrix and sets all elements to 1.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void ones(int numberOfRows, int numberOfColumns) {
    set_size(numberOfRows, numberOfColumns);
    ones();
  }

  /**
   * Resizes the matrix and sets all elements along the main diagonal to 1 and the other elements to 0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void eye(int numberOfRows, int numberOfColumns) {
    _matrix = CommonOps.identity(numberOfRows, numberOfColumns);
    updateAttributes();
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
    _matrix = RandomMatrices.createRandom(n_rows, n_cols, rng);
    updateAttributes();
  }

  /**
   * Resizes the matrix and sets each element to a pseudorandom value drawn from the standard uniform distribution on
   * the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>Drawn from [0,1) instead of the closed interval [0,1].
   * <li>Remains a row/column vector if this is invoked for a row/column vector.
   * </ul>
   * 
   * @param numberOfElements The number of elements
   * @param rng The pseudorandom generator
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   */
  public void randu(int numberOfElements, Random rng) throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    if (is_colvec()) {
      _matrix = RandomMatrices.createRandom(numberOfElements, 1, rng);
    } else {
      _matrix = RandomMatrices.createRandom(1, numberOfElements, rng);
    }

    updateAttributes();
  }

  /**
   * Resizes the matrix and sets each element to a pseudorandom value drawn from the standard uniform distribution on
   * the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param rng The pseudorandom generator
   */
  public void randu(int numberOfRows, int numberOfColumns, Random rng) {
    _matrix = RandomMatrices.createRandom(numberOfRows, numberOfColumns, rng);
    updateAttributes();
  }

  /**
   * Sets each element to a pseudorandom value drawn from the standard normal distribution with mean 0.0 and standard
   * deviation 1.0.
   * 
   * @param rng The pseudorandom generator
   */
  public void randn(Random rng) {
    for (int i = 0; i < n_elem; i++) {
      _matrix.set(i, rng.nextGaussian());
    }
  }

  /**
   * Resizes the matrix and sets each element to a pseudorandom value drawn from the standard normal distribution with
   * mean 0.0 and standard deviation 1.0.
   * <p>
   * <b>Non-canonical:</b> Remains a row/column vector if this is invoked for a row/column vector.
   * 
   * @param numberOfElements The number of elements
   * @param rng The pseudorandom generator
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   */
  public void randn(int numberOfElements, Random rng) throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    if (is_colvec()) {
      _matrix = new DenseMatrix64F(numberOfElements, 1);
    } else {
      _matrix = new DenseMatrix64F(1, numberOfElements);
    }
    updateAttributes();
    
    randn(rng);
  }

  /**
   * Resizes the matrix and sets each element to a pseudorandom value drawn from the standard normal distribution with
   * mean 0.0 and standard deviation 1.0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param rng The pseudorandom generator
   */
  public void randn(int numberOfRows, int numberOfColumns, Random rng) {
    _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);
    updateAttributes();
    
    randn(rng);
  }

  /**
   * Replaces the value of each element with the provided one.
   * 
   * @param value The value
   */
  public void fill(double value) {
    CommonOps.fill(_matrix, value);
  }

  /**
   * @param j
   * @param B
   */
  public void insert_cols(int j, Mat B) {

  }

  /**
   * @param aj
   * @param bj
   */
  public void insert_cols(int aj, int bj) {

  }

  /**
   * @param aj
   * @param bj
   * @param zero
   */
  public void insert_cols(int aj, int bj, boolean zero) {

  }

  /**
   * @param j
   * @param B
   */
  public void insert_rows(int j, Mat B) {

  }

  /**
   * @param aj
   * @param bj
   */
  public void insert_rows(int aj, int bj) {

  }

  /**
   * @param aj
   * @param bj
   * @param zero
   */
  public void insert_rows(int aj, int bj, boolean zero) {

  }

  /**
   * @param a
   */
  public void shed_col(int a) {

  }

  /**
   * @param aj
   * @param bj
   */
  public void shed_cols(int aj, int bj) {

  }

  /**
   * @param a
   */
  public void shed_row(int a) {

  }

  /**
   * @param aj
   * @param bj
   */
  public void shed_rows(int aj, int bj) {

  }

  /**
   * @param B
   */
  public void swap(Mat B) {

  }

  /**
   * @param a
   * @param b
   */
  public void swap_cols(int a, int b) {

  }

  /**
   * @param a
   * @param b
   */
  public void swap_rows(int a, int b) {

  }

  /**
   * @return
   */
  public boolean is_empty() {
    return false;
  }

  /**
   * @return
   */
  public boolean is_finite() {
    return false;
  }

  /**
   * @return
   */
  boolean is_number() {
    return false;
  }

  /**
   * Returns true if the matrix is square and false otherwise.
   * 
   * @return The boolean value.
   */
  public boolean is_square() {
    if (n_rows != n_cols) {
      return false;
    }
    return true;
  }

  /**
   * Returns true if the matrix is a vector and false otherwise.
   * 
   * @return The boolean value.
   */
  public boolean is_vec() {
    if (n_rows == 1 || n_cols == 1) {
      return true;
    }
    return false;
  }

  /**
   * Returns true if the matrix is a column vector and false otherwise.
   * 
   * @return The boolean value.
   */
  public boolean is_colvec() {
    if (n_cols == 1) {
      return true;
    }
    return false;
  }

  /**
   * Returns true if the matrix is a row-vector and false otherwise.
   * 
   * @return The boolean value.
   */
  public boolean is_rowvec() {
    if (n_rows == 1) {
      return true;
    }
    return false;
  }

  /**
   * @param n
   * @return
   */
  public boolean in_range(int n) {
    if (n > -1 && n < n_elem) {
      return true;
    }

    return false;
  }

  /**
   * @param span
   * @return
   */
  public boolean in_range(Span span) {
    if (span.isEntireRange() || (span.getFirst() > -1 && span.getLast() < n_elem)) {
      return true;
    }

    return true;
  }

  /**
   * @param i
   * @param j
   * @return
   */
  public boolean in_range(int i, int j) {
    if (i > -1 && j > -1 && i < n_rows && j < n_cols) {
      return true;
    }

    return true;
  }

  /**
   * @param spanI
   * @param spanJ
   * @return
   */
  public boolean in_range(Span spanI, Span spanJ) {
    if ((spanI.isEntireRange() || (spanI.getFirst() > -1 && spanI.getLast() < n_rows)) && (spanJ.isEntireRange() || (spanJ.getFirst() > -1 && spanJ.getLast() < n_cols))) {
      return true;
    }

    return false;
  }

  /**
   * @return
   */
  public double min() {
    return 0.0;
  }

  /**
   * @param n
   * @return
   */
  public double min(int n) {
    return 0.0;
  }

  /**
   * @param i
   * @param j
   * @return
   */
  public double min(int i, int j) {
    return 0.0;
  }

  /**
   * @return
   */
  public double max() {
    return 0.0;
  }

  /**
   * @param n
   * @return
   */
  public double max(int n) {
    return 0.0;
  }

  /**
   * @param i
   * @param j
   * @return
   */
  public double max(int i, int j) {
    return 0.0;
  }

  /**
   * @param numberOfElements
   */
  public void set_size(int numberOfElements) {

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
    // EJML fails if DenseMatrix64F was not properly initialised.
    if (n_rows == 0 && n_cols == 0) {
      _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);
    } else {
      _matrix.reshape(numberOfRows, numberOfColumns);
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
   * @param numberOfElements
   */
  public void resize(int numberOfElements) {

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

  /**
   * @return
   */
  public String print() {
    return null;
  }

  /**
   * @param header
   * @return
   */
  public String print(String header) {
    return null;
  }

  /**
   * @param stream
   * @return
   */
  public String print(PrintWriter stream) {
    return null;
  }

  /**
   * @param stream
   * @param header
   * @return
   */
  public String print(PrintWriter stream, String header) {
    return null;
  }

  /**
   * @return
   */
  public String raw_print() {
    return null;
  }

  /**
   * @param header
   * @return
   */
  public String raw_print(String header) {
    return null;
  }

  /**
   * @param stream
   * @return
   */
  public String raw_print(PrintWriter stream) {
    return null;
  }

  /**
   * @param stream
   * @param header
   * @return
   */
  public String raw_print(PrintWriter stream, String header) {
    return null;
  }

  /**
   * @param n
   * @return
   */
  public String save(String n) {
    return null;
  }

  /**
   * @param n
   * @param t
   * @return
   */
  public String save(String n, String t) {
    return null;
  }

  /**
   * @param stream
   * @return
   */
  public String save(PrintWriter stream) {
    return null;
  }

  /**
   * @param stream
   * @param t
   * @return
   */
  public String save(PrintWriter stream, String t) {
    return null;
  }

  /**
   * @param n
   * @return
   */
  public String load(String n) {
    return null;
  }

  /**
   * @param n
   * @param t
   * @return
   */
  public String load(String n, String t) {
    return null;
  }

  /**
   * @param stream
   * @return
   */
  public String load(PrintWriter stream) {
    return null;
  }

  /**
   * @param stream
   * @param t
   * @return
   */
  public String load(PrintWriter stream, String t) {
    return null;
  }

  /**
   * 
   */
  public void clear() {

  }

  /**
   * @return
   */
  public boolean empty() {
    return false;

  }

  /**
   * @return
   */
  public int size() {
    return 0;
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
   * Returns the transpose of the matrix.
   * 
   * @return The transpose.
   */
  public Mat t() {
    DenseMatrix64F result = new DenseMatrix64F(n_cols, n_rows);
    CommonOps.transpose(_matrix, result);
    return new Mat(result);
  }

  /**
   * 
   */
  public void reset() {

  }

  /**
   * @param B
   * 
   */
  public void copy_size(Mat B) {

  }

  @Override
  public String toString() {
    return _matrix.toString();
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
   * @throws IllegalArgumentException Thrown if the provided position is out of bound.
   */
  int convertToRowMajorOrdering(int n) throws IllegalArgumentException {
    if (!in_range(n)) {
      throw new IllegalArgumentException("The provided position is out of bound.");
    }

    if (is_vec()) {
      return n;
    } else {
      /*
       * n = i + j * n_rows, i < n_rows
       * 
       * i = n - j * n_rows
       * i = Math.floor(n / n_rows)
       * 
       * j = (n - i) / n_rows
       * j = n % n_rows
       */
      return (n / n_rows) + (n % n_rows) * n_cols;
    }
  }
}