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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
public class Mat implements Iterable<Double> {

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
  public Mat(int numberOfElements, Fill fillType) throws IllegalArgumentException {
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
  public void elem(Mat selection, Op operator) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnsupportedOperationException {
    if (!selection.is_vec()) {
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The rowSelection must be a vector, but was a (" + rowSelection.n_rows + ", " + rowSelection.n_cols + ")-matrix.");
    }

    if (!columnSelection.is_vec()) {
      throw new IllegalArgumentException("The columnSelection must be a vector, but was a (" + columnSelection.n_rows + ", " + columnSelection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The rowSelection must be a vector, but was a (" + rowSelection.n_rows + ", " + rowSelection.n_cols + ")-matrix.");
    }

    if (!columnSelection.is_vec()) {
      throw new IllegalArgumentException("The columnSelection must be a vector, but was a (" + columnSelection.n_rows + ", " + columnSelection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The rowSelection must be a vector, but was a (" + rowSelection.n_rows + ", " + rowSelection.n_cols + ")-matrix.");
    }

    if (!columnSelection.is_vec()) {
      throw new IllegalArgumentException("The columnSelection must be a vector, but was a (" + columnSelection.n_rows + ", " + columnSelection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The rowSelection must be a vector, but was a (" + rowSelection.n_rows + ", " + rowSelection.n_cols + ")-matrix.");
    }

    if (!columnSelection.is_vec()) {
      throw new IllegalArgumentException("The columnSelection must be a vector, but was a (" + columnSelection.n_rows + ", " + columnSelection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
      throw new IllegalArgumentException("The selection must be a vector, but was a (" + selection.n_rows + ", " + selection.n_cols + ")-matrix.");
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
  public Mat times(Mat operand) throws IllegalArgumentException {
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
   * Expands the matrix by inserting a matrix at column position {@code j}.
   * 
   * @param j The column position
   * @param matrix The matrix
   * 
   * 
   * @throws IllegalArgumentException The number of rows of this matrix must match with the matrix to be inserted, but
   *           was {@link #n_rows} and {@code matrix.n_rows}.
   * @throws ArrayIndexOutOfBoundsException The column position is out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column position was {@code j}.
   */
  public void insert_cols(int j, Mat matrix) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    if (matrix.n_rows != n_rows) {
      throw new IllegalArgumentException("The number of rows of this matrix must match with the matrix to be inserted, but was " + n_rows + " and " + matrix.n_rows + ".");
    }

    if (!in_range(Span.all(), new Span(j))) {
      throw new ArrayIndexOutOfBoundsException("The column position is out of bound. The matrix contains " + n_cols + " columns, but the column position was  " + j + ".");
    }

    Mat temp = new Mat(_matrix);
    set_size(n_rows, n_cols + matrix.n_cols);
    // Note: n_cols and n_rows were updated by .set_size()
    cols(0, j - 1, Op.EQUAL, temp.cols(0, j - 1));
    cols(j, matrix.n_cols - 1, Op.EQUAL, matrix);
    cols(matrix.n_cols, n_cols - 1, Op.EQUAL, temp.cols(j, n_cols - matrix.n_cols - 1));
  }

  /**
   * Expands the matrix by inserting a zero matrix from the {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   * 
   * @throws ArrayIndexOutOfBoundsException The column positions are out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column positions are from {@code a} to {@code b}.
   */
  public void insert_cols(int a, int b) throws ArrayIndexOutOfBoundsException {
    insert_cols(a, b, true);
  }

  /**
   * Expands the matrix by inserting a matrix from the {@code a}th to {@code b}th column.
   * <p>
   * The inserted matrix will be filled with 0 ({@code fillWithZeros} = true) or uninitialised otherwise.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param fillWithZeros Whether to be filled with 0
   * 
   * @throws ArrayIndexOutOfBoundsException The column positions are out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column positions are from {@code a} to {@code b}.
   */
  public void insert_cols(int a, int b, boolean fillWithZeros) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column positions are out of bound. The matrix contains " + n_cols + " columns, but the column positions are from  " + a + " to " + b + ".");
    }

    int spanLength = b - a + 1;
    Mat fillMatrix;
    if (fillWithZeros) {
      fillMatrix = new Mat(n_rows, spanLength, Fill.ZEROS);
    } else {
      fillMatrix = new Mat(n_rows, spanLength);
    }

    Mat temp = new Mat(_matrix);
    set_size(n_rows, n_cols + spanLength);
    // Note: n_cols and n_rows were updated by .set_size()
    cols(0, a - 1, Op.EQUAL, temp.cols(0, a - 1));
    cols(a, b, Op.EQUAL, fillMatrix);
    cols(b + 1, n_cols - 1, Op.EQUAL, temp.cols(a, n_cols - spanLength - 1));
  }

  /**
   * Expands the matrix by inserting a matrix at row position {@code j}.
   * 
   * @param i The row position
   * @param matrix The matrix
   * 
   * @throws IllegalArgumentException The number of columns of this matrix must match with the matrix to be inserted,
   *           but was {@link #n_cols} and {@code matrix.n_cols}.
   * @throws ArrayIndexOutOfBoundsException The row position is out of bound. The matrix contains {@link #n_rows} rows,
   *           but the column position was {@code i}.
   */
  public void insert_rows(int i, Mat matrix) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    if (matrix.n_cols != n_cols) {
      throw new IllegalArgumentException("The number of columns of this matrix must match with the matrix to be inserted, but was " + n_cols + " and " + matrix.n_cols + ".");
    }

    if (!in_range(new Span(i), Span.all())) {
      throw new ArrayIndexOutOfBoundsException("The row position is out of bound. The matrix contains " + n_rows + " rows, but the row position was  " + i + ".");
    }

    Mat temp = new Mat(_matrix);
    set_size(n_rows + matrix.n_rows, n_cols);
    // Note: n_cols and n_rows were updated by .set_size()
    rows(0, i - 1, Op.EQUAL, temp.rows(0, i - 1));
    rows(i, matrix.n_rows - 1, Op.EQUAL, matrix);
    rows(matrix.n_rows, n_rows - 1, Op.EQUAL, temp.rows(i, n_rows - matrix.n_rows - 1));
  }

  /**
   * Expands the matrix by inserting a zero matrix from the {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * 
   * @throws ArrayIndexOutOfBoundsException The row positions are out of bound. The matrix contains {@link #n_rows}
   *           rows, but the row positions are from {@code a} to {@code b}.
   */
  public void insert_rows(int a, int b) throws ArrayIndexOutOfBoundsException {
    insert_rows(a, b, true);
  }

  /**
   * Expands the matrix by inserting a matrix from the {@code a}th to {@code b}th row.
   * <p>
   * The inserted matrix will be filled with 0 ({@code fillWithZeros} = true) or uninitialised otherwise.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param fillWithZeros Whether to be filled with 0
   * 
   * @throws ArrayIndexOutOfBoundsException The row positions are out of bound. The matrix contains {@link #n_rows}
   *           rows, but the row positions are from {@code a} to {@code b}.
   */
  public void insert_rows(int a, int b, boolean fillWithZeros) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(a, b), Span.all())) {
      throw new IllegalArgumentException("The row positions are out of bound. The matrix contains " + n_rows + " rows, but the row positions are from " + a + " to " + b + ".");
    }

    int spanLength = b - a + 1;
    Mat fillMatrix;
    if (fillWithZeros) {
      fillMatrix = new Mat(spanLength, n_cols, Fill.ZEROS);
    } else {
      fillMatrix = new Mat(spanLength, n_cols);
    }

    Mat temp = new Mat(_matrix);
    set_size(n_rows + spanLength, n_cols);
    // Note: n_cols and n_rows were updated by .set_size()
    rows(0, a - 1, Op.EQUAL, temp.rows(0, a - 1));
    rows(a, b, Op.EQUAL, fillMatrix);
    rows(b + 1, n_rows - 1, Op.EQUAL, temp.rows(a, n_rows - spanLength - 1));
  }

  /**
   * Shrinks the matrix by removing the {@code j}th column.
   * 
   * @param j The column position
   * 
   * @throws ArrayIndexOutOfBoundsException The column positions are out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column positions are from {@code j} to {@code j}.
   */
  public void shed_col(int j) throws ArrayIndexOutOfBoundsException {
    shed_cols(j, j);
  }

  /**
   * Shrinks the matrix by removing the {@code a}th to {@code b}th columns.
   * 
   * @param a The first column position
   * @param b The last column position
   * 
   * @throws ArrayIndexOutOfBoundsException The column positions are out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column positions are from {@code a} to {@code b}.
   */
  public void shed_cols(int a, int b) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(), new Span(a, b))) {
      throw new ArrayIndexOutOfBoundsException("The column positions are out of bound. The matrix contains " + n_cols + " columns, but the column positions are from  " + a + " to " + b + ".");
    }

    int spanLength = b - a + 1;

    Mat temp = new Mat(_matrix);
    set_size(n_rows, n_cols - spanLength);
    // Note: n_cols and n_rows were updated by .set_size()
    cols(0, a - 1, Op.EQUAL, temp.cols(0, a - 1));
    cols(a, n_cols - 1, Op.EQUAL, temp.cols(b + 1, n_cols + spanLength - 1));
  }

  /**
   * Shrinks the matrix by removing the {@code j}th row.
   * 
   * @param i The row position
   * 
   * @throws ArrayIndexOutOfBoundsException The row positions are out of bound. The matrix contains {@link #n_rows}
   *           rows, but the row positions are from {@code i} to {@code i}.
   */
  public void shed_row(int i) throws ArrayIndexOutOfBoundsException {
    shed_rows(i, i);
  }

  /**
   * Shrinks the matrix by removing the {@code a}th to {@code b}th rows.
   * 
   * @param a The first row position
   * @param b The last row position
   * 
   * @throws ArrayIndexOutOfBoundsException The row positions are out of bound. The matrix contains {@link #n_rows}
   *           rows, but the row positions are from {@code a} to {@code b}.
   */
  public void shed_rows(int a, int b) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(a, b), Span.all())) {
      throw new IllegalArgumentException("The row positions are out of bound. The matrix contains " + n_rows + " rows, but the row positions are from " + a + " to " + b + ".");
    }

    int spanLength = b - a + 1;

    Mat temp = new Mat(_matrix);
    set_size(n_rows - spanLength, n_cols);
    // Note: n_cols and n_rows were updated by .set_size()
    rows(0, a - 1, Op.EQUAL, temp.rows(0, a - 1));
    rows(a, n_rows - 1, Op.EQUAL, temp.rows(b + 1, n_rows + spanLength - 1));
  }

  /**
   * Swaps the elements of this matrix with another one.
   * 
   * @param matrix The matrix
   * 
   * @throws IllegalArgumentException The size of this matrix must match with the one to be swaped with, but this is a (
   *           {@link #n_rows}, {@link #n_cols})-matrix while the other one was a ({@code matrix.n_rows},
   *           {@code matrix.n_cols})-matrix.
   */
  public void swap(Mat matrix) throws IllegalArgumentException {
    if (n_rows != matrix.n_rows || n_cols != matrix.n_cols) {
      throw new IllegalArgumentException("The size of this matrix must match with the one to be swaped with, but this is a (" + n_rows + ", " + n_cols + ")-matrix while the other one was a (" + matrix.n_rows + ", " + matrix.n_rows + ")-matrix");
    }

    submat(Op.EQUAL, matrix);
  }

  /**
   * Swaps the {@code j1}th column with the {@code j2}th.
   * 
   * @param j1 The first column
   * @param j2 The second column
   * 
   * @throws ArrayIndexOutOfBoundsException The column position is out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column position was {@code j1}.
   * @throws ArrayIndexOutOfBoundsException The column position is out of bound. The matrix contains {@link #n_cols}
   *           columns, but the column position was {@code j2}.
   */
  public void swap_cols(int j1, int j2) throws ArrayIndexOutOfBoundsException {
    if (!in_range(Span.all(), new Span(j1))) {
      throw new ArrayIndexOutOfBoundsException("The column position is out of bound. The matrix contains " + n_cols + " columns, but the column position was  " + j1 + ".");
    }

    if (!in_range(Span.all(), new Span(j2))) {
      throw new ArrayIndexOutOfBoundsException("The column position is out of bound. The matrix contains " + n_cols + " columns, but the column position was  " + j2 + ".");
    }

    Mat temp = col(j1);
    col(j2, Op.EQUAL, j1);
    col(j1, Op.EQUAL, temp);
  }

  /**
   * Swaps the {@code i1}th row with the {@code i2}th.
   * 
   * @param i1 The first row
   * @param i2 The second row
   * 
   * @throws ArrayIndexOutOfBoundsException The row position is out of bound. The matrix contains {@link #n_rows} rows,
   *           but the row position was {@code i1}.
   * @throws ArrayIndexOutOfBoundsException The row position is out of bound. The matrix contains {@link #n_rows} rows,
   *           but the row position was {@code i2}.
   */
  public void swap_rows(int i1, int i2) throws ArrayIndexOutOfBoundsException {
    if (!in_range(new Span(i1), Span.all())) {
      throw new ArrayIndexOutOfBoundsException("The row position is out of bound. The matrix contains " + n_rows + " rows, but the row position was  " + i1 + ".");
    }

    if (!in_range(new Span(i2), Span.all())) {
      throw new ArrayIndexOutOfBoundsException("The row position is out of bound. The matrix contains " + n_rows + " rows, but the row position was  " + i2 + ".");
    }

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
    for (int n = 0; n < n_elem; n++) {
      double element = _matrix.get(n);

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
  boolean is_number() {
    for (int n = 0; n < n_elem; n++) {
      double element = _matrix.get(n);

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
    return (n_rows == 1 || n_cols == 1);
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
   * Returns true if the element position is within the boundary
   * 
   * @param n The element position
   * @return Whether the element position is within the boundary
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
   * Returns true if the row position and column position are within the boundary
   * 
   * @param i The row position
   * @param j The column position
   * @return Whether the row position and column position are within the boundary
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
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  public double min() throws UnsupportedOperationException {
    if (n_elem < 1) {
      throw new UnsupportedOperationException("The matrix must have at least one element.");
    }

    double minimum = _matrix.get(0);
    for (int n = 1; n < n_elem; n++) {
      minimum = Math.min(minimum, _matrix.get(n));
    }

    return minimum;
  }

  /**
   * Returns the smallest value of all elements and stores its element position in {@code n}.
   * <p>
   * Note: The position must be of the mutable type int[].
   * 
   * @param n The element position
   * @return The minimum
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  public double min(int[] n) throws UnsupportedOperationException {
    if (n_elem < 1) {
      throw new UnsupportedOperationException("The matrix must have at least one element.");
    }

    double minimum = _matrix.get(0);
    for (int elementN = 1; elementN < n_elem; elementN++) {
      double element = _matrix.get(elementN);

      if (element < minimum) {
        minimum = element;
        n[0] = elementN;
      }
    }

    return minimum;
  }

  /**
   * Returns the smallest value of all elements and stores its row position in {@code j} and column position in
   * {@code j}.
   * <p>
   * Note: The positions must be of the mutable type int[].
   * 
   * @param i The row position
   * @param j The column position
   * @return The minimum
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  public double min(int[] i, int[] j) throws UnsupportedOperationException {
    if (n_elem < 1) {
      throw new UnsupportedOperationException("The matrix must have at least one element.");
    }

    double minimum = _matrix.get(0);
    for (int n = 1; n < n_elem; n++) {
      double element = _matrix.get(n);

      if (element < minimum) {
        minimum = element;
        i[0] = n / n_cols;
        j[0] = n - (i[0] * n_cols);
      }
    }

    return minimum;
  }

  /**
   * Returns the largest value of all elements.
   * 
   * @return The maximum
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  public double max() throws UnsupportedOperationException {
    if (n_elem < 1) {
      throw new UnsupportedOperationException("The matrix must have at least one element.");
    }

    double maximum = _matrix.get(0);
    for (int n = 1; n < n_elem; n++) {
      maximum = Math.max(maximum, _matrix.get(n));
    }

    return maximum;
  }

  /**
   * Returns the largest value of all elements and stores its element position in {@code n}.
   * <p>
   * Note: The position must be of the mutable type int[].
   * 
   * @param n The element position
   * @return The maximum
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  public double max(int[] n) throws UnsupportedOperationException {
    if (n_elem < 1) {
      throw new UnsupportedOperationException("The matrix must have at least one element.");
    }

    double maximum = _matrix.get(0);
    for (int elementN = 1; elementN < n_elem; elementN++) {
      double element = _matrix.get(elementN);

      if (element > maximum) {
        maximum = element;
        n[0] = elementN;
      }
    }

    return maximum;
  }

  /**
   * Returns the largest value of all elements and stores its row position in {@code j} and column position in {@code j}
   * .
   * <p>
   * Note: The positions must be of the mutable type int[].
   * 
   * @param i The row position
   * @param j The column position
   * @return The maximum
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  public double max(int[] i, int[] j) throws UnsupportedOperationException {
    if (n_elem < 1) {
      throw new UnsupportedOperationException("The matrix must have at least one element.");
    }

    double maximum = _matrix.get(0);
    for (int n = 1; n < n_elem; n++) {
      double element = _matrix.get(n);

      if (element > maximum) {
        maximum = element;
        i[0] = n / n_cols;
        j[0] = n - (i[0] * n_cols);
      }
    }

    return maximum;
  }

  /**
   * Sets the size of the vector to contain {@code numberOfElements} elements.
   * <p>
   * Neither guarantees to reuse the values of the elements nor their positions, except the number of elements is the
   * same as before.
   * <p>
   * In this case, the values of the elements are reused and only the size changes.
   * 
   * @param numberOfElements The number of elements
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   */
  public void set_size(int numberOfElements) throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    if (numberOfElements == n_elem) {
      return;
    } else if (is_colvec()) {
      set_size(numberOfElements, 1);
    } else {
      set_size(1, numberOfElements);
    }
  }

  /**
   * Sets the size of the contain {@link #n_rows} rows and {@link #n_cols} columns.
   * <p>
   * Neither guarantees to reuse the values of the elements nor their positions, except the number of elements is the
   * same as before.
   * <p>
   * In this case, the values of the elements are reused and only the size changes.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void set_size(int numberOfRows, int numberOfColumns) {
    _matrix.reshape(numberOfRows, numberOfColumns);
    updateAttributes();
  }

  /**
   * Sets the size of the contain {@link #n_rows} rows and {@link #n_cols} columns.
   * <p>
   * Elements are accessed column-wise, while the matrix is also filled column-wise.
   * <p>
   * Guarantees to reuse the values of the elements but not their positions.
   * 
   * @param numberOfRows The new number of rows
   * @param numberOfColumns The new number of columns
   */
  public void reshape(int numberOfRows, int numberOfColumns) {
    reshape(numberOfRows, numberOfColumns, 0);
  }

  /**
   * Sets the size of the contain {@link #n_rows} rows and {@link #n_cols} columns.
   * <p>
   * Elements can be accessed either column-wise ({@code dimension} = 0) or row-wise ({@code dimension} = 1), while the
   * matrix is always filled column-wise.
   * <p>
   * Guarantees to reuse the values of the elements but not their positions.
   * 
   * @param numberOfRows The new number of rows
   * @param numberOfColumns The new number of columns
   * @param dimension The dimension
   * 
   * @throws IllegalArgumentException The dimension must be on of 0 or 1, but was {@code dimension}.
   */
  public void reshape(int numberOfRows, int numberOfColumns, int dimension) throws IllegalArgumentException {
    if (dimension < 0 || dimension > 1) {
      throw new IllegalArgumentException("The dimension must be on of 0 or 1, but was " + dimension + ".");
    }

    DenseMatrix64F temp = new DenseMatrix64F(numberOfColumns, numberOfRows);
    if (dimension == 1) {
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
   * Sets the size of the vector to contain {@code numberOfElements} elements.
   * <p>
   * Guarantees to reuse the values of the elements and their positions.
   * 
   * @param numberOfElements The number of elements
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   */
  public void resize(int numberOfElements) throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("Must only be invoked for vectors.");
    }

    if (is_colvec()) {
      resize(numberOfElements, 1);
    } else {
      resize(1, numberOfElements);
    }
  }

  /**
   * Sets the size of the contain {@link #n_rows} rows and {@link #n_cols} columns.
   * <p>
   * Guarantees to reuse the values of the elements and their positions.
   * 
   * @param numberOfRows The new number of rows
   * @param numberOfColumns The new number of columns
   */
  public void resize(int numberOfRows, int numberOfColumns) {
    if (numberOfRows <= n_rows && numberOfColumns <= n_cols) {
      // No additional memory needs to be allocated
      // Shrinks n_rows and n_cols only
      _matrix.reshape(numberOfColumns, numberOfRows, false);
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
    writer.println(toString());
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
   * @throws IllegalArgumentException Only ascii and auto_detect is supported, but was {@code filetype}.
   */
  public boolean save(String filename, String filetype) throws FileNotFoundException, IllegalArgumentException {
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
        double value = _matrix.get(i, j);
        if(Double.isInfinite(value)) {
          String sign = "";
          if(value < 0) {
            sign = "-";
          }
          writer.format("%24s", sign + "Inf");
        } else {
          writer.format(Locale.ENGLISH, "%1$ 24.16e", _matrix.get(i, j));
        }
      }

      writer.println();
    }

    writer.close();

    return true;

  }

  /**
   * Fills the matrix with data from a file of filetype {@code ascii} and returns true on succes.
   * <p>
   * Resets the matrix if an error occured.
   * 
   * @param filename The filename
   * @return Whether the process was succesfully.
   * 
   * @throws IOException An I/O error occured
   */
  public boolean load(String filename) throws IOException {
    return load(filename, "auto_detect");
  }

  /**
   * Fills the matrix with data from a file of filetype {@code filetype} and returns true on succes.
   * <p>
   * Resets the matrix if an error occured.
   * 
   * @param filename The filename
   * @param filetype The filetype
   * @return Whether the process was succesfully.
   * 
   * @throws IllegalArgumentException Only ascii and auto_detect is supported, but was {@code filetype}.
   * @throws IOException An I/O error occured
   */
  public boolean load(String filename, String filetype) throws IllegalArgumentException, IOException {
    return load(new FileInputStream(filename), filetype);
  }

  /**
   * Fills the matrix with data from a stream of filetype {@code ascii} and returns true on succes.
   * <p>
   * Resets the matrix if an error occured.
   * 
   * @param stream The stream
   * @return Whether the process was succesfully.
   * 
   * @throws IOException An I/O error occured
   */
  public boolean load(InputStream stream) throws IOException {
    return load(stream, "auto_detect");
  }

  /**
   * Fills the matrix with data from a stream of filetype {@code filetype} and returns true on succes.
   * <p>
   * Resets the matrix if an error occured.
   * 
   * @param stream The stream
   * @param filetype The filetype
   * @return Whether the process was succesfully.
   * 
   * @throws IllegalArgumentException Only ascii and auto_detect is supported, but was {@code filetype}.
   * @throws IOException An I/O error occured
   * @throws IllegalArgumentException All columns must have the same length.
   */
  public boolean load(InputStream stream, String filetype) throws IllegalArgumentException, IOException {
    if (!filetype.equals("ascii") && !filetype.equals("auto_detect")) {
      throw new IllegalArgumentException("Only ascii and auto_detect is supported, but was " + filetype + ".");
    }

    BufferedReader reader;
    try {
      reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 is not supported. How could this happen?");
    }

    String line = reader.readLine();
    if(line == null) {
      reset();
      return false;
    }
    
    int numberOfColumns = line.trim().split("\\s+").length;
    List<double[]> matrix = new ArrayList<double[]>();
    do {
      String rowString[] = line.trim().split("\\s+");
      
      if(rowString.length != numberOfColumns) {
        reset();
        throw new IllegalArgumentException("All columns must have the same length.");
      }
      
      double[] rowDouble = new double[numberOfColumns];
      for(int j = 0; j < numberOfColumns; j++) {
        if(rowString[j].equals("Inf")) {
          rowDouble[j] = Double.POSITIVE_INFINITY;
        } else if(rowString[j].equals("-Inf")) {
          rowDouble[j] = Double.NEGATIVE_INFINITY;
        } else {
          rowDouble[j] = Double.valueOf(rowString[j]);
        }
      }
      matrix.add(rowDouble);
      
    } while ((line = reader.readLine()) != null);
    
    _matrix = new DenseMatrix64F(matrix.toArray(new double[0][0]));
    updateAttributes();
    
    reader.close();
      
    return true;
  }

  /**
   * Resets the matrix to an emtpy one.
   */
  public void clear() {
    reset();
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
   * @throws UnsupportedOperationException Must only be invoked for square matrices.
   * @throws UnsupportedOperationException The matrix is not invertible.
   */
  public Mat i() throws UnsupportedOperationException {
    if (!is_square()) {
      throw new UnsupportedOperationException("Must only be invoked for square matrices.");
    }

    DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
    if (!CommonOps.invert(_matrix, result)) {
      throw new UnsupportedOperationException("The matrix is not invertible.");
    }

    return new Mat(result);
  }

  /**
   * Returns the transpose of the matrix.
   * 
   * @return The transpose
   */
  public Mat t() {
    DenseMatrix64F result = new DenseMatrix64F(n_cols, n_rows);
    CommonOps.transpose(_matrix, result);
    return new Mat(result);
  }

  /**
   * Resets the matrix to an emtpy one.
   */
  public void reset() {
    _matrix = new DenseMatrix64F();
    updateAttributes();
  }

  /**
   * Sets the size of to be the same as {@code matrix}.
   * <p>
   * Neither guarantees to reuse the values of the elements nor their positions.
   * 
   * @param matrix The matrix
   */
  public void copy_size(Mat matrix) {
    set_size(matrix.n_rows, matrix.n_cols);
  }

  @Override
  public String toString() {
    return _matrix.toString();
  }

  /**
   * Updates the attributes {@link #n_rows}, {@link #n_cols} and {@link #n_elem}.
   * <p>
   * Should always be called right after the size of the matrix is changed.
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
   * @throws ArrayIndexOutOfBoundsException The position is out of bound. The matrix contains {@link #n_elem} elements,
   *           but the position was {@code n}.
   */
  int convertToRowMajorOrdering(int n) throws ArrayIndexOutOfBoundsException {
    if (!in_range(n)) {
      throw new ArrayIndexOutOfBoundsException("The  position is out of bound. The matrix contains " + n_elem + " elements, but the position was  " + n + ".");
    }

    if (is_vec()) {
      return n;
    } else {
      /*
       * n = i + j * n_rows, i < n_rows
       * 
       * i = n - j * n_rows
       * i = n % n_rows
       * 
       * j = (n - i) / n_rows
       * j = Math.floor(n / n_rows)
       */
      int j = n / n_rows;
      return j + (n - j * n_rows) * n_cols;
    }
  }

  @Override
  public Iterator<Double> iterator() {
    class MatIterator implements Iterator<Double> {

      private Mat _matrix;
      private int _currentPosition;
      
      public MatIterator(Mat matrix) {
        _matrix = matrix;
        _currentPosition = 0;
      }
      
      @Override
      public boolean hasNext() {
       return (_currentPosition < _matrix.n_elem);
      }

      @Override
      public Double next() {
       return _matrix.at(_currentPosition++);
      }

      @Override
      public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removal is not supported.");
      }
     }
    
    return new MatIterator(this);
  }
}