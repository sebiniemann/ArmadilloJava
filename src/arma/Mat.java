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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

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
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient Java Matrix Library</a>
 */
public class Mat extends BaseMat {

  /**
   * Creates an empty matrix with zero elements.
   */
  public Mat() {
    this(new double[]{});
  }

  /**
   * Creates a column vector with the same number of elements as the provided one-dimensional array.
   * 
   * @param matrix The array
   */
  public Mat(double[] matrix) {
    if (_matrix != null && _matrix.length > 0) {
      set_size(_matrix.length, 1);
      System.arraycopy(matrix, 0, _matrix, 0, matrix.length);
    } else {
      set_size(0, 0);
    }
  }

  /**
   * Creates a matrix with the same size as the provided two-dimensional array.
   * <p>
   * The array is assumed to have a structure like {@code array[rows][columns]}.
   * 
   * @param matrix The array
   * 
   * @throws IllegalArgumentException All rows must have the same length.
   */
  public Mat(double[][] matrix) throws IllegalArgumentException {
    if (_matrix != null && _matrix.length > 0 && matrix[0].length > 0) {
      int numberOfRows = matrix.length;
      int numberOfColumns = matrix[0].length;

      set_size(numberOfRows, numberOfColumns);
      for (int i = 0; i < n_rows; i++) {
        for (int j = 0; j < n_cols; j++) {
          if (matrix[i].length != n_cols) {
            throw new IllegalArgumentException("All rows must have the same length.");
          }

          _matrix[getElementPosition(i, j)] = matrix[i][j];
        }
      }
    } else {
      set_size(0, 0);
    }
  }

  /**
   * Creates a matrix by copying the provided one.
   * 
   * @param matrix The matrix
   */
  public Mat(Mat matrix) {
    this(matrix._matrix);
  }

  /**
   * Creates a matrix by copying the provided one.
   * 
   * @param submatrix The matrix
   */
  protected Mat(SubviewMat submatrix) {
    set_size(submatrix.n_rows, submatrix.n_cols);

    int n = 0;
    for (double element : submatrix) {
      _matrix[n++] = element;
    }
  }

  /**
   * Creates a matrix by copying the provided one.
   * 
   * @param submatrix The matrix
   */
  protected Mat(SelectionMat submatrix) {
    set_size(submatrix.n_rows, submatrix.n_cols);

    int n = 0;
    for (double element : submatrix) {
      _matrix[n++] = element;
    }
  }

  /**
   * Creates a matrix by copying the provided one.
   * 
   * @param submatrix The matrix
   */
  protected Mat(DiagMat submatrix) {
    set_size(submatrix.n_rows, submatrix.n_cols);

    int n = 0;
    for (double element : submatrix) {
      _matrix[n++] = element;
    }
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
        zeros(numberOfRows, numberOfColumns);
        break;
      case ONES:
        ones(numberOfRows, numberOfColumns);
        break;
      case EYE:
        eye(numberOfRows, numberOfColumns);
        break;
      case RANDU:
      case RANDN:
        throw new IllegalArgumentException("Does not support Fill.RANDU or FILL.RANDN. Use Mat(int, int, Fill, Random) instead.");
    }
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
        zeros(numberOfRows, numberOfColumns);
        break;
      case ONES:
        ones(numberOfRows, numberOfColumns);
        break;
      case EYE:
        eye(numberOfRows, numberOfColumns);
        break;
      case RANDU:
        randu(numberOfRows, numberOfColumns, rng);
        break;
      case RANDN:
        randn(numberOfRows, numberOfColumns, rng);
        break;
    }
  }

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
    return at(getElementPosition(i, j));
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
    at(getElementPosition(i, j), operator);
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
    at(getElementPosition(i, j), operator, operand);
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

    return _matrix[n];
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

    inPlace(n, operator);
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

    inPlace(n, operator, operand);
  }

  /**
   * Returns a shallow copy of the {@code j}th column.
   * 
   * @param j The column position
   * @return The submatrix
   */
  SubviewMat colInternal(int j) {
    return new SubviewMat(this, 0, n_rows - 1, j, j);
  }

  /**
   * Returns a deep copy of the {@code j}th column.
   * 
   * @param j The column position
   * @return The submatrix
   */
  public Mat col(int j) {
    return new Mat(colInternal(j));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code j}th column.
   * 
   * @param j The column position
   * @param operator The operator
   */
  public void col(int j, Op operator) {
    colInternal(j).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column.
   * 
   * @param j The column position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int j, Op operator, Mat operand) {
    colInternal(j).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column.
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
    colInternal(j).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code j}th column and {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @return The submatrix
   */
  SubviewMat colInternal(int a, int b, int j) {
    return new SubviewMat(this, a, b, j, j);
  }

  /**
   * Returns a deep copy of all elements in the {@code j}th column and {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @return The submatrix
   */
  public Mat col(int a, int b, int j) {
    return new Mat(colInternal(a, b, j));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @param operator The operator
   */
  public void col(int a, int b, int j, Op operator) {
    colInternal(a, b, j).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int a, int b, int j, Op operator, Mat operand) {
    colInternal(a, b, j).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param j The column position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int a, int b, int j, Op operator, double operand) {
    colInternal(a, b, j).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of the {@code i}th row.
   * 
   * @param i The row position
   * @return The submatrix
   */
  SubviewMat rowInternal(int i) {
    return new SubviewMat(this, i, i, 0, n_cols - 1);
  }

  /**
   * Returns a deep copy of the {@code i}th row.
   * 
   * @param i The row position
   * @return The submatrix
   */
  public Mat row(int i) {
    return new Mat(rowInternal(i));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row position
   * @param operator The operator
   */
  public void row(int i, Op operator) {
    rowInternal(i).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int i, Op operator, Mat operand) {
    rowInternal(i).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int i, Op operator, double operand) {
    rowInternal(i).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code i}th row and {@code a}th to {@code b}th column.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param i The column position
   * @return The submatrix
   */
  SubviewMat rowInternal(int a, int b, int i) {
    return new SubviewMat(this, i, i, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code i}th row and {@code a}th to {@code b}th column.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param i The column position
   * @return The submatrix
   */
  public Mat row(int a, int b, int i) {
    return new Mat(rowInternal(a, b, i));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param i The row position
   * @param operator The operator
   */
  public void row(int a, int b, int i, Op operator) {
    rowInternal(a, b, i).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param i The row position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int a, int b, int i, Op operator, Mat operand) {
    rowInternal(a, b, i).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param i The row position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int a, int b, int i, Op operator, double operand) {
    rowInternal(a, b, i).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @return The submatrix
   */
  SubviewMat colsInternal(int a, int b) {
    return new SubviewMat(this, 0, n_rows - 1, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @return The submatrix
   */
  public Mat cols(int a, int b) {
    return new Mat(colsInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param operator The operator
   */
  public void cols(int a, int b, Op operator) {
    colsInternal(a, b).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(int a, int b, Op operator, Mat operand) {
    colsInternal(a, b).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(int a, int b, Op operator, double operand) {
    colsInternal(a, b).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @return The submatrix
   */
  SubviewMat rowsInternal(int a, int b) {
    return new SubviewMat(this, a, b, 0, n_cols - 1);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @return The submatrix
   */
  public Mat rows(int a, int b) {
    return new Mat(rowsInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param operator The operator
   */
  public void rows(int a, int b, Op operator) {
    rowsInternal(a, b).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(int a, int b, Op operator, Mat operand) {
    rowsInternal(a, b).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(int a, int b, Op operator, double operand) {
    rowsInternal(a, b).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   */
  public void submat(Op operator) {
    inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(Op operator, Mat operand) {
    inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(Op operator, double operand) {
    inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code ai}th to {@code bi}th row and {@code aj}th to {@code bj}th
   * column.
   * 
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   * @return The submatrix
   */
  SubviewMat submatInternal(int ai, int bi, int aj, int bj) {
    return new SubviewMat(this, ai, bi, aj, bj);
  }

  /**
   * Returns a deep copy of all elements in the {@code ai}th to {@code bi}th row and {@code aj}th to {@code bj}th
   * column.
   * 
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
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
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   * @param operator The operator
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator) {
    submatInternal(ai, bi, aj, bj).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code ai}th to {@code bi}
   * th row and {@code aj}th to {@code bj}th column.
   * 
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator, Mat operand) {
    submatInternal(ai, bi, aj, bj).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code ai}th to {@code bi}
   * th row and {@code aj}th to {@code bj}th column.
   * 
   * @param ai The first row position
   * @param bi The last row position
   * @param aj The first column position
   * @param bj The last column position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator, double operand) {
    submatInternal(ai, bi, aj, bj).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th position.
   * 
   * @param a The first element position
   * @param b The last element position
   * @return The submatrix
   */
  SubviewMat subvecInternal(int a, int b) {
    return new SubviewMat(this, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th position.
   * 
   * @param a The first element position
   * @param b The last element position
   * @return The submatrix
   */
  public Mat subvec(int a, int b) {
    return new Mat(subvecInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element position
   * @param b The last element position
   * @param operator The operator
   */
  public void subvec(int a, int b, Op operator) {
    subvecInternal(a, b).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element position
   * @param b The last element position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void subvec(int a, int b, Op operator, Mat operand) {
    subvecInternal(a, b).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element position
   * @param b The last element position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void subvec(int a, int b, Op operator, double operand) {
    subvecInternal(a, b).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements specified in the selection as a column vector.
   * 
   * @param selection The element positions
   * @return The elements
   */
  SelectionMat elemInternal(Mat selection) {
    return new SelectionMat(this, selection);
  }

  /**
   * Returns a deep copy of all elements specified in the selection as a column vector.
   * 
   * @param selection The element positions
   * @return The elements
   */
  public Mat elem(Mat selection) {
    return new Mat(elemInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element positions
   * @param operator The operator
   */
  public void elem(Mat selection, Op operator) {
    elemInternal(selection).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void elem(Mat selection, Op operator, Mat operand) {
    elemInternal(selection).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void elem(Mat selection, Op operator, double operand) {
    elemInternal(selection).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all columns specified in the selection.
   * 
   * @param selection The column positions
   * @return The elements
   */
  SelectionMat colsInternal(Mat selection) {
    return new SelectionMat(this, null, selection);
  }

  /**
   * Returns a deep copy of all columns specified in the selection.
   * 
   * @param selection The column positions
   * @return The elements
   */
  public Mat cols(Mat selection) {
    return new Mat(colsInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column positions
   * @param operator The operator
   */
  public void cols(Mat selection, Op operator) {
    colsInternal(selection).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(Mat selection, Op operator, Mat operand) {
    colsInternal(selection).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(Mat selection, Op operator, double operand) {
    colsInternal(selection).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all rows specified in the selection.
   * 
   * @param selection The row positions
   * @return The elements
   */
  SelectionMat rowsInternal(Mat selection) {
    return new SelectionMat(this, selection, null);
  }

  /**
   * Returns a deep copy of all rows specified in the selection.
   * 
   * @param selection The row positions
   * @return The elements
   */
  public Mat rows(Mat selection) {
    return new Mat(rowsInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row positions
   * @param operator The operator
   */
  public void rows(Mat selection, Op operator) {
    rows(selection).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(Mat selection, Op operator, Mat operand) {
    rows(selection).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(Mat selection, Op operator, double operand) {
    rows(selection).inPlace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements specified in the selection.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @return The elements
   */
  SelectionMat submatInternal(Mat rowSelection, Mat columnSelection) {
    return new SelectionMat(this, rowSelection, columnSelection);
  }

  /**
   * Returns a deep copy of all elements specified in the selection.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @return The elements
   */
  public Mat submat(Mat rowSelection, Mat columnSelection) {
    return new Mat(submatInternal(rowSelection, columnSelection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @param operator The operator
   */
  public void submat(Mat rowSelection, Mat columnSelection, Op operator) {
    submatInternal(rowSelection, columnSelection).inPlace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(Mat rowSelection, Mat columnSelection, Op operator, Mat operand) {
    submatInternal(rowSelection, columnSelection).inPlace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row positions
   * @param columnSelection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(Mat rowSelection, Mat columnSelection, Op operator, double operand) {
    submatInternal(rowSelection, columnSelection).inPlace(operator, operand);
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
  public void diag(Op operator, Mat operand) {
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
  public DiagMat diagInternal(int k) {
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
    diagInternal(k).inPlace(operator);
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
  public void diag(int k, Op operator, Mat operand) {
    diagInternal(k).inPlace(operator, operand);
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
    diagInternal(k).inPlace(operator, operand);
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
  public void each_col(Op operator, Mat operand) {
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
   * @param selection The column positions
   * @param operator The operator
   */
  public void each_col(Mat selection, Op operator) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    for (double element : selection) {
      col((int) element, operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each column specified in the selection.
   * 
   * @param selection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(Mat selection, Op operator, Mat operand) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    for (double element : selection) {
      col((int) element, operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each column specified in the selection.
   * 
   * @param selection The column positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(Mat selection, Op operator, double operand) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    for (double element : selection) {
      col((int) element, operator, operand);
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
   * @param selection The row positions
   * @param operator The operator
   */
  public void each_row(Mat selection, Op operator) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    for (double element : selection) {
      row((int) element, operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each row specified in the selection.
   * 
   * @param selection The row positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(Mat selection, Op operator, Mat operand) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    for (double element : selection) {
      row((int) element, operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each row specified in the selection.
   * 
   * @param selection The row positions
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(Mat selection, Op operator, double operand) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    for (double element : selection) {
      row((int) element, operator, operand);
    }
  }

  /**
   * Creates a matrix being the addition of this matrix with a right-hand side addend
   * 
   * @param operand The addend
   * @return The matrix
   */
  public Mat plus(Mat operand) {
    Mat result = new Mat(_matrix);
    result.inPlace(Op.PLUS, operand);
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
    result.inPlace(Op.PLUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the subtration of this matrix with a right-hand side subtrahend
   * 
   * @param operand The subtrahend
   * @return The matrix
   */
  public Mat minus(Mat operand) {
    Mat result = new Mat(_matrix);
    result.inPlace(Op.MINUS, operand);
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
    result.inPlace(Op.MINUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat times(Mat operand) {
    isNonEqualNumberOfElementsDetection(n_cols, operand.n_rows);

    Mat result = new Mat(n_rows, operand.n_cols);

    int position = 0;
    for (int j = 0; j < operand.n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        result._matrix[position++] = Arma.dot(row(i), operand.col(j));
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
    result.inPlace(Op.TIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemTimes(Mat operand) {
    Mat result = new Mat(_matrix);
    result.inPlace(Op.ELEMTIMES, operand);
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
    result.inPlace(Op.ELEMTIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise division of this matrix with a right-hand side divisor
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemDivide(Mat operand) {
    Mat result = new Mat(_matrix);
    result.inPlace(Op.ELEMDIVIDE, operand);
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
    result.inPlace(Op.ELEMDIVIDE, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise negation of this matrix
   * 
   * @return The matrix
   */
  public Mat negate() {
    Mat result = new Mat(n_rows, n_cols);

    for (int n = 0; n < n_elem; n++) {
      result._matrix[n] = -_matrix[n];
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
   * Resizes the vector and sets all elements to 0.
   * <p>
   * <b>Non-canonical:</b> Remains a row/column vector if this is invoked for a row/column vector.
   * 
   * @param numberOfElements The number of elements
   */
  public void zeros(int numberOfElements) {
    isNonVectorDetection();

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
   */
  public void ones(int numberOfElements) {
    isNonVectorDetection();

    if (is_colvec()) {
      set_size(numberOfElements, 1);
    } else {
      set_size(1, numberOfElements);
    }
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
   * Sets all elements along the main diagonal to 1 and the other elements to 0.
   */
  public void eye() {
    int length = Math.min(n_rows, n_cols);
    for (int i = 0; i < length; i++) {
      _matrix[getElementPosition(i, i)] = 1;
    }
  }

  /**
   * Resizes the matrix and sets all elements along the main diagonal to 1 and the other elements to 0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void eye(int numberOfRows, int numberOfColumns) {
    zeros(numberOfRows, numberOfColumns);
    eye();
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
    for (int n = 0; n < n_elem; n++) {
      _matrix[n] = rng.nextDouble();
    }
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
   */
  public void randu(int numberOfElements, Random rng) {
    isNonVectorDetection();

    if (is_colvec()) {
      set_size(numberOfElements, 1);
    } else {
      set_size(1, numberOfElements);
    }

    randu(rng);
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
    set_size(numberOfRows, numberOfColumns);
    randu(rng);
  }

  /**
   * Sets each element to a pseudorandom value drawn from the standard normal distribution with mean 0.0 and standard
   * deviation 1.0.
   * 
   * @param rng The pseudorandom generator
   */
  public void randn(Random rng) {
    for (int n = 0; n < n_elem; n++) {
      _matrix[n] = rng.nextGaussian();
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
   */
  public void randn(int numberOfElements, Random rng) {
    isNonVectorDetection();

    if (is_colvec()) {
      set_size(numberOfElements, 1);
    } else {
      set_size(1, numberOfElements);
    }

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
    set_size(numberOfRows, numberOfColumns);
    randn(rng);
  }

  /**
   * Replaces the value of each element with the provided one.
   * 
   * @param value The value
   */
  public void fill(double value) {
    Arrays.fill(_matrix, value);
  }

  /**
   * Expands the matrix by inserting a matrix at column position {@code j}.
   * 
   * @param j The column position
   * @param matrix The matrix
   */
  public void insert_cols(int j, Mat matrix) {
    isNonEqualNumberOfElementsDetection(matrix.n_rows, n_rows);
    isColumnOutOfBoundsDetection(j);

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
   */
  public void insert_cols(int a, int b) {
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
   */
  public void insert_cols(int a, int b, boolean fillWithZeros) {
    isColumnRangeOutOfBoundsDetection(a, b);

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
   */
  public void insert_rows(int i, Mat matrix) {
    isNonEqualNumberOfElementsDetection(matrix.n_cols, n_cols);
    isColumnOutOfBoundsDetection(i);

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
   */
  public void insert_rows(int a, int b) {
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
   */
  public void insert_rows(int a, int b, boolean fillWithZeros) {
    isRowRangeOutOfBoundsDetection(a, b);

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
   */
  public void shed_col(int j) {
    shed_cols(j, j);
  }

  /**
   * Shrinks the matrix by removing the {@code a}th to {@code b}th columns.
   * 
   * @param a The first column position
   * @param b The last column position
   */
  public void shed_cols(int a, int b) {
    isColumnRangeOutOfBoundsDetection(a, b);

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
   */
  public void shed_row(int i) {
    shed_rows(i, i);
  }

  /**
   * Shrinks the matrix by removing the {@code a}th to {@code b}th rows.
   * 
   * @param a The first row position
   * @param b The last row position
   */
  public void shed_rows(int a, int b) {
    isRowRangeOutOfBoundsDetection(a, b);

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
   */
  public void swap(Mat matrix) {
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
    for (double element : this) {
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
    for (double element : this) {
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
   */
  public double min() {
    isEmptyDetection();

    double minimum = _matrix[0];
    for (int n = 1; n < n_elem; n++) {
      minimum = Math.min(minimum, _matrix[n]);
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
   */
  public double min(int[] n) {
    isEmptyDetection();

    double minimum = _matrix[0];
    for (int nn = 1; nn < n_elem; nn++) {
      double element = _matrix[nn];

      if (element < minimum) {
        minimum = element;
        n[0] = nn;
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
   */
  public double min(int[] i, int[] j) {
    isEmptyDetection();

    double minimum = _matrix[0];
    for (int n = 1; n < n_elem; n++) {
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

    double maximum = _matrix[0];
    for (int n = 1; n < n_elem; n++) {
      maximum = Math.max(maximum, _matrix[n]);
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
   */
  public double max(int[] n) {
    isEmptyDetection();

    double maximum = _matrix[0];
    for (int nn = 1; nn < n_elem; nn++) {
      double element = _matrix[nn];

      if (element > maximum) {
        maximum = element;
        n[0] = nn;
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
   */
  public double max(int[] i, int[] j) {
    isEmptyDetection();

    double maximum = _matrix[0];
    for (int n = 1; n < n_elem; n++) {
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
   * Sets the size of the vector to contain {@code numberOfElements} elements.
   * <p>
   * Neither guarantees to reuse the values of the elements nor their positions, except the number of elements is the
   * same as before.
   * <p>
   * In this case, the values of the elements are reused and only the size changes.
   * 
   * @param numberOfElements The number of elements
   */
  public void set_size(int numberOfElements) {
    isNonVectorDetection();
    isInvalidPositionDetection(numberOfElements);

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
    isInvalidPositionDetection(numberOfRows);
    isInvalidPositionDetection(numberOfColumns);

    if (n_rows != numberOfRows || n_cols != numberOfColumns) {
      _matrix = new double[numberOfRows * numberOfColumns];
    }
    updateAttributes(numberOfRows, numberOfColumns);
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
   */
  public void reshape(int numberOfRows, int numberOfColumns, int dimension) {
    isNotInSetDetection(dimension, 0, 1);

    Mat temp = new Mat(_matrix);
    set_size(numberOfColumns, numberOfRows);

    if (dimension == 0) {
      System.arraycopy(temp, 0, _matrix, 0, temp.n_elem);
    } else {
      for (int n = 0; n < temp.n_elem; n++) {
        _matrix[n] = temp._matrix[n];
      }
    }
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
    isNonVectorDetection();

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
    Mat temp = new Mat(_matrix);
    set_size(numberOfColumns, numberOfRows);

    int srcColumnPointer = 0;
    int destColumnPointer = 0;
    int length = Math.min(temp.n_rows, numberOfRows);
    for (int j = 0; j < n_cols; j++) {
      System.arraycopy(temp, srcColumnPointer, _matrix, destColumnPointer, length);
      srcColumnPointer += temp.n_rows;
      destColumnPointer += numberOfRows;
    }
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
        double value = _matrix[getElementPosition(i, j)];
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
   * @throws IOException An I/O error occured
   */
  public boolean load(String filename, String filetype) throws IOException {
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
    if (line == null) {
      reset();
      return false;
    }

    int numberOfColumns = line.trim().split("\\s+").length;
    List<double[]> matrix = new ArrayList<double[]>();
    do {
      String rowString[] = line.trim().split("\\s+");

      if (rowString.length != numberOfColumns) {
        reset();
        throw new IllegalArgumentException("All columns must have the same length.");
      }

      double[] rowDouble = new double[numberOfColumns];
      for (int j = 0; j < numberOfColumns; j++) {
        if (rowString[j].equals("Inf")) {
          rowDouble[j] = Double.POSITIVE_INFINITY;
        } else if (rowString[j].equals("-Inf")) {
          rowDouble[j] = Double.NEGATIVE_INFINITY;
        } else {
          rowDouble[j] = Double.valueOf(rowString[j]);
        }
      }
      matrix.add(rowDouble);

    } while ((line = reader.readLine()) != null);
    int numberOfRow = matrix.size();

    set_size(numberOfRow, numberOfColumns);
    for (int i = 0; i < numberOfRow; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        _matrix[getElementPosition(i, j)] = matrix.get(i)[j];
      }
    }
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
    int position = 0;
    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        transpose._matrix[getElementPosition(j, i)] = _matrix[position++];
      }
    }

    return transpose;
  }

  /**
   * Resets the matrix to an emtpy one.
   */
  public void reset() {
    _matrix = new double[]{};
    updateAttributes(0, 0);
  }

  /**
   * Sets the size of the matrix to be the same as {@code matrix}.
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
    String output = "(" + n_rows + ", " + n_cols + ")-matrix: [\n";
    for (int i = 0; i < n_rows; i++)
    {
      output += " ";

      for (int j = 0; j < n_cols; j++) {
        double value = _matrix[getElementPosition(i, j)];
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

  /**
   * Returns an element position based on column-major-ordering.
   * <p>
   * Performs no error detection.
   * 
   * @param i The row position
   * @param j The colum position
   * @return The element position
   */
  protected int getElementPosition(int i, int j) {
    return i + j * n_rows;
  }

  @Override
  protected int getElementPosition(int position) {
    return position;
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
   * Detects if a column position is invalid or out of bound and throws an exception if so.
   * 
   * @param j The column position
   * 
   * @throws ArrayIndexOutOfBoundsException The column position range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the column position range was ({@code j}, {@code j}).
   */
  protected void isColumnOutOfBoundsDetection(int j) throws ArrayIndexOutOfBoundsException {
    isColumnRangeOutOfBoundsDetection(j, j);
  }

  /**
   * Detects if a column position range is invalid or out of bound and throws an exception if so.
   * 
   * @param a The first row position
   * @param b The last row position
   * 
   * @throws ArrayIndexOutOfBoundsException The column position range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the column position range was ({@code a}, {@code b}).
   */
  protected void isColumnRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b >= n_rows) {
      throw new ArrayIndexOutOfBoundsException("The column position range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the column position range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if a row position is invalid or out of bound and throws an exception if so.
   * 
   * @param i The row position
   * 
   * @throws ArrayIndexOutOfBoundsException The row position range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the row position range was ({@code i}, {@code i}).
   */
  protected void isRowOutOfBoundsDetection(int i) throws ArrayIndexOutOfBoundsException {
    isRowRangeOutOfBoundsDetection(i, i);
  }

  /**
   * Detects if a row position range is out of bound and throws an exception if so.
   * 
   * @param a The first row position
   * @param b The last row position
   * 
   * @throws ArrayIndexOutOfBoundsException The row position range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the row position range was ({@code a}, {@code b}).
   */
  protected void isRowRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b >= n_rows) {
      throw new ArrayIndexOutOfBoundsException("The row position range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the row position range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if any element position is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The column positions
   */
  protected void isInvalidElementSelectionDetection(Mat selection) {
    for (double position : selection) {
      isNonIntergerPositionDetection(position);
      isElementOutOfBoundsDetection((int) position);
    }
  }

  /**
   * Detects if any column position is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The column positions
   */
  protected void isInvalidColumnSelectionDetection(Mat selection) {
    for (double position : selection) {
      isNonIntergerPositionDetection(position);
      isColumnOutOfBoundsDetection((int) position);
    }
  }

  /**
   * Detects if any row position is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The row positions
   */
  protected void isInvalidRowSelectionDetection(Mat selection) {
    for (double position : selection) {
      isNonIntergerPositionDetection(position);
      isRowOutOfBoundsDetection((int) position);
    }
  }

  /**
   * Detects if a position is not an integer and throws an exception if so.
   * 
   * @param position The position
   * 
   * @throws IllegalArgumentException The position must be an integer, but was not: {@code rowPositionDouble}.
   */
  protected void isNonIntergerPositionDetection(double position) throws IllegalArgumentException {
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
   * Detects if a value is not in a set and throws an exception if so.
   * 
   * @param value The value
   * @param set The set
   * 
   * @throws IllegalArgumentException The given value is not in the set, but were {@code value} and {@code set}.
   */
  protected void isNotInSetDetection(int value, int... set) throws IllegalArgumentException {
    if (!Arrays.asList(set).contains(value)) {
      throw new IllegalArgumentException("The given value is not in the set, but were " + value + " and " + Arrays.toString(set) + ".");
    }
  }

  @Override
  public Iterator<Double> iterator() {
    return new MatIterator(this);
  }

  /**
   * Converts a matrix from the Mat format into EJML's DenseMatrix64F format.
   * 
   * @param matrix The matrix in Mat format
   * @return The matrix in DenseMatrix64F format
   */
  static DenseMatrix64F convertMatToEJMLMat(Mat matrix) {
    DenseMatrix64F ejmlMatrix = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    int position = 0;
    for (int j = 0; j < matrix.n_cols; j++) {
      for (int i = 0; i < matrix.n_rows; i++) {
        ejmlMatrix.set(i, j, matrix._matrix[position++]);
      }
    }

    return ejmlMatrix;
  }

  /**
   * Converts a matrix from EJML's DenseMatrix64F format into the Mat format.
   * 
   * @param ejmlMatrix The matrix in DenseMatrix64F format
   * @return The matrix in Mat format
   */
  static Mat convertEJMLToMat(DenseMatrix64F ejmlMatrix) {
    Mat matrix = new Mat(ejmlMatrix.numRows, ejmlMatrix.numCols);

    int position = 0;
    for (int j = 0; j < matrix.n_cols; j++) {
      for (int i = 0; i < matrix.n_rows; i++) {
        matrix._matrix[position++] = ejmlMatrix.get(i, j);
      }
    }

    return matrix;
  }
}