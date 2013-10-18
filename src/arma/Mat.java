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

import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.RandomMatrices;

/**
 * Provides a real-valued dense matrix with double precision and member functions as well as attributes similar to the Armadillo C++ Algebra Library (Armadillo) by Conrad Sanderson et al., based on DenseMatrix64F from Peter Abeles' Efficient Java Matrix Library (EJML) Version 0.23 from 21.06.2013.
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
   * Reference to the internal data representation of the matrix.
   */
  private DenseMatrix64F _matrix;

  /**
   * The number of rows of the matrix.
   */
  public int             n_rows;

  /**
   * The number of columns of the matrix.
   */
  public int             n_cols;

  /**
   * The number of elements of the matrix (same as {@link #n_rows} * {@link #n_cols}) .
   */
  public int             n_elem;

  /**
   * Creates a matrix by referencing to the provided one.
   * 
   * @param matrix The matrix to be referenced.
   */
  Mat(DenseMatrix64F matrix) {
    _matrix = matrix;
    updateAttributes();
  }

  /**
   * Creates an empty matrix with {@link #n_elem} = 0.
   */
  public Mat() {
    this(new DenseMatrix64F());
  }


  /**
   * @param matrix
   */
  public Mat(double[] matrix) {
    
  }

  /**
   * Creates a matrix with the same number of rows and columns as well as elements as the provided two-dimensional
   * array. The array is assumed to have a structure of {@code array[rows][columns]}.
   * 
   * @param matrix The two-dimensional array.
   */
  public Mat(double[][] matrix) {
    // Error-checking must be done in DenseMatrix64F since this() needs to be the first statement.
    this(new DenseMatrix64F(matrix));
  }

  /**
   * Creates a matrix by copying the provided one.
   * 
   * @param matrix The provided matrix.
   */
  public Mat(Mat matrix) {
    this(new DenseMatrix64F(matrix.memptr()));
  }
  
  /**
   * <b>Non-canonical:</b> Creating a matrix by a string is not supported. Use {@link #Mat(double[][])} instead.
   * <p>
   * This constructor only exists to for clarification.
   * 
   * @param matrix The provided string.
   * 
   * @throws UnsupportedOperationException Creating a matrix by a string is not supported. Use Mat(double[][]) instead.
   */
  public Mat(String matrix) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Creating a matrix by a string is not supported. Use Mat(double[][]) instead.");
  }
  
  /**
   * @param numberOfElements
   */
  public Mat(int numberOfElements) {

  }
  
  /**
   * Creates a new matrix with {@link #n_rows}{@code = numberOfRows} and {@link #n_cols}{@code = numberOfColumns}.
   * <p>
   * Same as {@link #Mat(int, int, Fill)} with fill type {@link Fill#NONE}.
   * 
   * @param numberOfRows The number of rows.
   * @param numberOfColumns The number of columns.
   */
  public Mat(int numberOfRows, int numberOfColumns) {
    this(numberOfRows, numberOfColumns, Fill.NONE);
  }
  
  /**
   * @param numberOfElements
   * @param fillType 
   */
  public Mat(int numberOfElements, Fill fillType) {

  }
  
  /**
   * Creates a matrix with {@link #n_rows}{@code = numberOfRows} and {@link #n_cols}{@code = numberOfColumns} filled
   * according to {@code fillType}.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if {@code fillType} is one of
   * {@code RANDU} or {@code RANDN}. Use {@link #Mat(int, int, Fill, Random)} instead.
   * 
   * @param numberOfRows The number of rows.
   * @param numberOfColumns The number of columns.
   * @param fillType The fill type.
   * 
   * @throws IllegalArgumentException Thrown if {@code fillType} is one of {@code RANDU} or {@code RANDN}. Use
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
        throw new IllegalArgumentException("Use Mat(int, int, Fill, Random) instead.");
    }

    updateAttributes();
  }
  
  /**
   * @param numberOfElements
   * @param fillType 
   * @param rng 
   */
  public Mat(int numberOfElements, Fill fillType, Random rng) {

  }
  
  /**
   * Creates a random matrix with {@link #n_rows}{@code = numberOfRows} and {@link #n_cols}{@code = numberOfColumns}
   * filled according to {@code fillType} with either uniformly or normally distributed pseudorandom values.
   * <p>
   * <b>Non-canonical:</b> An {@code IllegalArgumentException} exception is thrown if {@code fillType} is not one of
   * {@code RANDU} or {@code RANDN}. Use {@link #Mat(int, int, Fill)} instead.
   * 
   * @param numberOfRows The number of rows.
   * @param numberOfColumns The number of columns.
   * @param fillType The fill type.
   * @param rng The pseudorandom generator.
   * 
   * @throws IllegalArgumentException Thrown if {@code fillType} is not one of {@code RANDU} or {@code RANDN}. Use
   *           {@link #Mat(int, int, Fill)} instead.
   */
  public Mat(int numberOfRows, int numberOfColumns, Fill fillType, Random rng) throws IllegalArgumentException {
    switch (fillType) {
      case NONE:
      case ZEROS:
      case ONES:
      case EYE:
        throw new IllegalArgumentException("Use Mat(int, int, Fill) instead.");
      case RANDU:
        _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);

        int numberOfElements = _matrix.getNumElements();
        for (int i = 0; i < numberOfElements; i++) {
          _matrix.set(i, rng.nextGaussian());
        }
        break;
      case RANDN:
        _matrix = RandomMatrices.createRandom(numberOfRows, numberOfColumns, rng);
        break;
    }

    updateAttributes();
  }
  
  /**
   * Returns the value of the element at the <i>n</i>th row and <i>j</i>th column.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>Performs a boundary checks. <b>Note:</b> There is no element access provided without boundary checks.
   * <li>A {@code IllegalArgumentException} exception is thrown instead of C++'s std::logic_error if the requested
   * position is out of bound.
   * </ul>
   * 
   * @param i The row of the element.
   * @param j The column of the element.
   * @return The value of the element at the <i>n</i>th row and <i>j</i>th column.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the requested position is out of bound.
   * 
   * @see #at(int)
   */
  public double at(int i, int j) throws IllegalArgumentException {
    if (i >= n_rows || j >= n_cols) {
      throw new IllegalArgumentException("The requested position (" + i + ", " + j + ") is out of bound. n_rows = " + n_rows + ", n_cols = " + n_cols);
    }

    return _matrix.get(i, j);
  }
  
  /**
   * @param i
   * @param j
   * @param operand
   */
  public void at(int i, int j, Op operand) {

  }

  /**
   * Performs a right-hand side operation on the value of the element at the <i>n</i>th row and <i>j</i>th column. The
   * value of the requested element will be overwritten by the result of the operation.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>Performs a boundary checks. <b>Note:</b> There is no element access provided without boundary checks.
   * <li>A {@code IllegalArgumentException} exception is thrown instead of C++'s std::logic_error if the requested
   * position is out of bound.
   * <li>A {@code UnsupportedOperationException} exception is thrown if another operation besides arithmetic operators or equality is requested.
   * </ul>
   * 
   * @param i The row of the left-hand side operand.
   * @param j The column of the left-hand side operand.
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the requested position is out of bound.
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operation besides arithmetic operators or equality is
   *           requested.
   * 
   * @see #at(int, Op, double)
   */
  public void at(int i, int j, Op operation, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    _matrix.set(i, j, Op.getResult(memptr().get(i, j), operation, operand));
  }
  
  /**
   * Returns the value of the <i>n</i>th element of a column-major-ordered one-dimensional view of the matrix.
   * <p>
   * <b>Note:</b> {@link #at(int, int) at(i, j)}{@code = }{@link #at(int, int) at(i + j * n_rows)}.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>Performs a boundary checks. <b>Note:</b> There is no element access provided without boundary checks.
   * <li>A {@code IllegalArgumentException} exception is thrown instead of C++'s std::logic_error if the requested
   * position is out of bound.
   * </ul>
   * 
   * @param n The position of the element.
   * @return The value of the <i>n</i>th element.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the requested position is out of bound.
   * 
   * @see #at(int, int)
   */
  public double at(int n) throws IllegalArgumentException {
    if (n >= n_elem) {
      throw new IllegalArgumentException("The requested position (" + n + ") is out of bound. n_elem = " + n_elem);
    }

    return _matrix.get(convertMajorOrdering(n));
  }
  
  /**
   * Performs a right-hand side operation on the value of the <i>n</i>th element of a column-major-ordered
   * one-dimensional view of the matrix. The value of the requested element will be overwritten by the result of the
   * operation.
   * <p>
   * <b>Note:</b> {@link #at(int, int) at(i, j)} = {@link #at(int, int) at(i + j * n_rows)}.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>Performs a boundary checks. <b>Note:</b> There is no element access provided without boundary checks.
   * <li>A {@code IllegalArgumentException} exception is thrown instead of C++'s std::logic_error if the requested
   * position is out of bound.
   * <li>A {@code UnsupportedOperationException} exception is thrown if another operation besides arithmetic operators or equality is requested.
   * </ul>
   * 
   * @param n The position of the left-hand side operand.
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the requested position is out of bound.
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operation besides arithmetic operators or equality is
   *           requested.
   * 
   * @see #at(int, int, Op, double)
   */
  public void at(int n, Op operation, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    _matrix.set(n, Op.getResult(at(n), operation, operand));
  }

  /**
   * Return a copy of the <i>j</i>th column as a ({@link #n_rows}, 1) matrix.
   * <p>
   * <b>Non-canonical:</b> >A {@code IllegalArgumentException} exception is thrown if the requested column is out of bound.
   * 
   * @param j The column to be copied.
   * @return The copied column.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the requested column is out of bound.
   */
  public Mat col(int j) throws IllegalArgumentException {
    if (j >= n_cols) {
      throw new IllegalArgumentException("The requested column (" + j + ") is out of bound. n_cols = " + n_cols);
    }
    
    DenseMatrix64F result = new DenseMatrix64F(n_rows, 1);

    // n_rows = result.getNumElements()
    for (int n = 0; n < n_rows; n++) {
      result.set(n, _matrix.get(n, j));
    }

    return new Mat(result);
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the <i>j</i>th column.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>A {@code IllegalArgumentException} exception is thrown if the provided right-hand side operand is not a column-vector. Use {@link #cols(int, int, Op, Mat)} instead.
   * <li>A {@code IllegalArgumentException} exception is thrown if the number of rows of the left-hand side does not match with the provided right-hand side operand.
   * <li>A {@code IllegalArgumentException} exception is thrown if the requested column is out of bound.
   * position is out of bound.
   * <li>A {@code UnsupportedOperationException} exception is thrown if another operation besides arithmetic operators or equality is requested.
   * </ul>
   * 
   * @param j The column of the left-hand side operands.
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the provided right-hand side operand is not a column-vector or if the number of rows of the left-hand side does not match with the provided right-hand side operand or if the requested column is out of bound.
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operation besides arithmetic operators or equality is
   *           requested.
   */
  public void col(int j, Op operation, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    if (!operand.is_colvec()) {
      throw new IllegalArgumentException("The provided right-hand side operand needs to be a column-vector. Use cols(int, int, Op, Mat) instead.");
    }
    
    if (j >= n_cols) {
      throw new IllegalArgumentException("The requested column (" + j + ") is out of bound. n_cols = " + n_cols);
    }
    
    if (operand.n_rows != n_rows) {
      throw new IllegalArgumentException("The number of rows of the left-hand side operand (n_rows = " + n_rows + ") does not match with the provided right-hand side operand (n_rows = " + operand.n_rows + ").");
    }
    
    DenseMatrix64F memptrOperand = operand.memptr();
    for (int i = 0; i < n_rows; i++) {
      at(i, j, operation, memptrOperand.get(i));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the <i>j</i>th column. The single provided
   * right-hand side operand is used for all operations.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>A {@code IllegalArgumentException} exception is thrown if the requested column is out of bound.
   * <li>A {@code UnsupportedOperationException} exception is thrown if another operation besides arithmetic operators or equality is requested.
   * </ul>
   * 
   * @param j The column of the left-hand side operands.
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the requested column is out of bound.
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operation besides arithmetic operators or equality is
   *           requested.
   */
  public void col(int j, Op operation, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    if (j >= n_cols) {
      throw new IllegalArgumentException("The requested column (" + j + ") is out of bound. n_cols = " + n_cols);
    }
    
    for (int n = 0; n < n_rows; n++) {
      at(n, j, operation, operand);
    }
  }
  
  /**
   * Return a copy of the <i>i</i>th row as a (1, {@link #n_cols}) matrix.
   * <p>
   * <b>Non-canonical:</b> >A {@code IllegalArgumentException} exception is thrown if the requested row is out of bound.
   * 
   * @param i The row to be copied.
   * @return The copied row.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the requested row is out of bound.
   */
  public Mat row(int i) throws IllegalArgumentException {
    if (i >= n_rows) {
      throw new IllegalArgumentException("The requested row (" + i + ") is out of bound. n_rows = " + n_rows);
    }
    
    DenseMatrix64F result = new DenseMatrix64F(1, n_cols);

    // n_cols = result.getNumElements()
    for (int n = 0; n < n_cols; n++) {
      result.set(n, _matrix.get(i, n));
    }

    return new Mat(result);
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the <i>i</i>th row.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>A {@code IllegalArgumentException} exception is thrown if the provided right-hand side operand is not a row-vector. Use {@link #rows(int, int, Op, Mat)} instead.
   * <li>A {@code IllegalArgumentException} exception is thrown if the number of columns of the left-hand side does not match with the provided right-hand side operand.
   * <li>A {@code IllegalArgumentException} exception is thrown if the requested row is out of bound.
   * position is out of bound.
   * <li>A {@code UnsupportedOperationException} exception is thrown if another operation besides arithmetic operators or equality is requested.
   * </ul>
   * 
   * @param i The row of the left-hand side operands.
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the provided right-hand side operand is not a row-vector or if the number of columns of the left-hand side does not match with the provided right-hand side operand or if the requested row is out of bound.
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operation besides arithmetic operators or equality is
   *           requested.
   */
  public void row(int i, Op operation, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    if (!operand.is_rowvec()) {
      throw new IllegalArgumentException("The provided right-hand side operand needs to be a row-vector. Use rows(int, int, Op, Mat) instead.");
    }
    
    if (i >= n_rows) {
      throw new IllegalArgumentException("The requested row (" + i + ") is out of bound. n_rows = " + n_rows);
    }
    
    if (operand.n_cols != n_cols) {
      throw new IllegalArgumentException("The number of columns of the left-hand side operand (n_cols = " + n_cols + ") does not match with the provided right-hand side operand (n_cols = " + operand.n_cols + ").");
    }

    DenseMatrix64F memptrOperand = operand.memptr();
    for (int n = 0; n < n_cols; n++) {
      at(i, n, operation, memptrOperand.get(n));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements of the <i>i</i>th row. The single provided
   * right-hand side operand is used for all operations.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>A {@code IllegalArgumentException} exception is thrown if the requested row is out of bound.
   * <li>A {@code UnsupportedOperationException} exception is thrown if another operation besides arithmetic operators or equality is requested.
   * </ul>
   * 
   * @param i The row of the left-hand side operands.
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the requested row is out of bound.
   * @throws UnsupportedOperationException <b>Non-canonical:</b> Thrown if another operation besides arithmetic operators or equality is
   *           requested.
   */
  public void row(int i, Op operation, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    if (i >= n_rows) {
      throw new IllegalArgumentException("The requested row (" + i + ") is out of bound. n_rows = " + n_rows);
    }
    
    for (int n = 0; n < n_cols; n++) {
      at(i, n, operation, operand);
    }
  }



  /**
   * @param first 
   * @param last 
   * @return
   */
  public Mat cols(int first, int last) {
    if (first > last) {
      throw new IllegalArgumentException();
    }
    
    if (first < 0 || last >= n_cols) {
      throw new IllegalArgumentException();
    }
    
    DenseMatrix64F result = new DenseMatrix64F(n_rows, last - first + 1);

    for (int j = first; j <= last; j++) {
      for (int i = 0; i < n_rows; i++) {
        result.set(i, j, _matrix.get(i, j));
      }
    }

    return new Mat(result);
  }
  
  /**
   * @param a
   * @param b
   * @param operation 
   */
  public void cols(int a, int b, Op operation) {

  }

  /**
   * @param first
   * @param last
   * @param operation
   * @param operand
   */
  public void cols(int first, int last, Op operation, Mat operand) {
    if (first > last) {
      throw new IllegalArgumentException();
    }
    
    if (first < 0 || last >= n_cols) {
      throw new IllegalArgumentException();
    }
    
    if (operand.n_rows != n_rows) {
      throw new IllegalArgumentException("The number of rows of the left-hand side operand (n_rows = " + n_rows + ") does not match with the provided right-hand side operand (n_rows = " + operand.n_rows + ").");
    }

    DenseMatrix64F memptrOperand = operand.memptr();
    int operandJ = 0;
    for (int j = first; j <= last; j++) {
      for (int i = 0; i < n_rows; i++) {
        at(i, j, operation, memptrOperand.get(i, operandJ));
      }
      operandJ++;
    }
  }

  /**
   * @param first
   * @param last
   * @param operation
   * @param operand
   */
  public void cols(int first, int last, Op operation, double operand) {
    if (first > last) {
      throw new IllegalArgumentException();
    }
    
    if (first < 0 || last >= n_cols) {
      throw new IllegalArgumentException();
    }
    
    for (int j = first; j <= last; j++) {
      for (int i = 0; i < n_rows; i++) {
        at(i, j, operation, operand);
      }
    }
  }
  
  /**
   * @param a
   * @param b
   * @param i
   * @return
   */
  public Mat cols(int a, int b, int i) {
    return null;
  }
  
  /**
   * @param a
   * @param b
   * @param i
   * @param operation 
   */
  public void cols(int a, int b, int i, Op operation) {

  }
  
  /**
   * @param a
   * @param b
   * @param i
   * @param operation 
   * @param operand 
   */
  public void cols(int a, int b, int i, Op operation, Mat operand) {

  }
  
  /**
   * @param a
   * @param b
   * @param i
   * @param operation 
   * @param operand 
   */
  public void cols(int a, int b, int i, Op operation, double operand) {

  }

  /**
   * @param first 
   * @param last 
   * @return
   */
  public Mat rows(int first, int last) {
    if (first > last) {
      throw new IllegalArgumentException();
    }
    
    if (first < 0 || last >= n_rows) {
      throw new IllegalArgumentException();
    }
    
    DenseMatrix64F result = new DenseMatrix64F(last - first + 1, n_cols);

    for (int i = first; i <= last; i++) {
      for (int j = 0; j < n_cols; i++) {
        result.set(i, j, _matrix.get(i, j));
      }
    }

    return new Mat(result);
  }

  /**
   * @param first
   * @param last
   * @param operation
   * @param operand
   */
  public void rows(int first, int last, Op operation, Mat operand) {
    if (first > last) {
      throw new IllegalArgumentException();
    }
    
    if (first < 0 || last >= n_rows) {
      throw new IllegalArgumentException();
    }
    
    if (operand.n_rows != n_rows) {
      throw new IllegalArgumentException("The number of rows of the left-hand side operand (n_rows = " + n_rows + ") does not match with the provided right-hand side operand (n_rows = " + operand.n_rows + ").");
    }
    
    DenseMatrix64F memptrOperand = operand.memptr();
    int operandI = 0;
    for (int i = first; i <= last; i++) {
      for (int j = 0; j < n_cols; i++) {
        at(i, j, operation, memptrOperand.get(operandI, j));
      }
      operandI++;
    }
  }

  /**
   * @param first
   * @param last
   * @param operation
   * @param operand
   */
  public void rows(int first, int last, Op operation, double operand) {
    if (first > last) {
      throw new IllegalArgumentException();
    }
    
    if (first < 0 || last >= n_rows) {
      throw new IllegalArgumentException();
    }
    
    for (int i = first; i <= last; i++) {
      for (int j = 0; j < n_cols; i++) {
        at(i, j, operation, operand);
      }
    }
  }
  
  /**
   * @param a
   * @param b
   * @param j
   * @return
   */
  public Mat rows(int a, int b, int j) {
    return null;
  }
  
  /**
   * @param a
   * @param b
   * @param j
   * @param operation 
   */
  public void rows(int a, int b, int j, Op operation) {

  }
  
  /**
   * @param a
   * @param b
   * @param j
   * @param operation 
   * @param operand 
   */
  public void rows(int a, int b, int j, Op operation, Mat operand) {

  }
  
  /**
   * @param a
   * @param b
   * @param j
   * @param operation 
   * @param operand 
   */
  public void rows(int a, int b, int j, Op operation, double operand) {

  }
  
  /**
   * @return
   */
  public Mat submat() {
    return null;
  }
  
  /**
   * @param operation 
   */
  public void submat(Op operation) {

  }
  
  /**
   * @param operation 
   * @param operand 
   */
  public void submat(Op operation, Mat operand) {

  }
  
  /**
   * @param operation 
   * @param operand 

   */
  public void submat(Op operation, double operand) {

  }
  
  /**
   * @param ai 
   * @param bi 
   * @param aj 
   * @param bj 
   * @return
   */
  public Mat submat(int ai, int bi, int aj, int bj) {
    return null;
  }
  
  /**
   * @param ai 
   * @param bi 
   * @param aj 
   * @param bj 
   * @param operation 
   */
  public void submat(int ai, int bi, int aj, int bj, Op operation) {

  }
  
  /**
   * @param ai 
   * @param bi 
   * @param aj 
   * @param bj 
   * @param operation 
   * @param operand 

   */
  public void submat(int ai, int bi, int aj, int bj, Op operation, Mat operand) {

  }
  
  /**
   * @param ai 
   * @param bi 
   * @param aj 
   * @param bj 
   * @param operation 
   * @param operand 
   */
  public void submat(int ai, int bi, int aj, int bj, Op operation, double operand) {

  }
  
  /**
   * @param operation 
   * @param operand 
   * @return
   */
  public Mat subvec() {
    return null;
  }
  
  /**
   * @param operation 
   * @param operand 
   */
  public void subvec(Op operation) {
  }
  
  /**
   * @param operation 
   * @param operand 
   */
  public void subvec(Op operation, Mat operand) {

  }
  
  /**
   * @param operation 
   * @param operand 
   */
  public void subvec(Op operation, double operand) {

  }
  
  /**
   * @param a 
   * @param b 
   * @param operation 
   * @param operand 
   * @return
   */
  public Mat subvec(int a, int b) {
    return null;
  }
  
  /**
   * @param a 
   * @param b 
   * @param operation 
   * @param operand 
   */
  public void subvec(int a, int b, Op operation) {

  }
  
  /**
   * @param a 
   * @param b 
   * @param operation 
   * @param operand 
   */
  public void subvec(int a, int b, Op operation, Mat operand) {

  }
  
  /**
   * @param a 
   * @param b 
   * @param operation 
   * @param operand 
   */
  public void subvec(int a, int b, Op operation, double operand) {

  }

  /**
   * Returns a column vector containing all elements for which selection.at(n) > 0 holds.
   * 
   * @param selection The selection to be used.
   * @return The copy of the selected elements.
   * 
   * @throws IllegalArgumentException Thrown if the number of elements of the matrix does not match with the provided
   *           selection.
   */
  public Mat elem(Mat selection) throws IllegalArgumentException {
    if (selection.n_elem != n_elem) {
      throw new IllegalArgumentException("The number of elements of the matrix (n_elem = " + n_elem + ") does not match with the provided selection (n_elem = " + selection.n_elem + ").");
    }

    DenseMatrix64F memptrSelection = selection.memptr();

    DenseMatrix64F result = new DenseMatrix64F(n_elem, 1);
    int index = 0;
    for (int i = 0; i < n_elem; i++) {
      if (memptrSelection.get(i) > 0) {
        result.set(index, _matrix.get(i));
        index++;
      }
    }

    if (index > 0) {
      // Saves current values of the elements since length <= n_elem
      result.reshape(index, 1);
    } else {
      result = new DenseMatrix64F();
    }
    return new Mat(result);
  }
  
  /**
   * @param I
   * @param operation
   */
  public void elem(Mat I, Op operation) {
    
  }

  /**
   * Performs the provided right-hand side element-wise operation on all elements for which selection.at(n) > 0 holds.
   * 
   * @param selection The selection to be used.
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException 
   * @throws UnsupportedOperationException 
   */
  public void elem(Mat selection, Op operation, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
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
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException 
   * @throws UnsupportedOperationException 
   */
  public void elem(Mat selection, Op operation, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    for (int i = 0; i < selection.n_elem; i++) {
      if (selection.at(i) > 0) {
        at(i, operation, operand);
      }
    }
  }

  /**
   * @param J
   * @return
   */
  public Mat cols(Mat J) {
    return null;
  }

  /**
   * @param J
   * @param operation
   */
  public void cols(Mat J, Op operation) {

  }

  /**
   * @param J
   * @param operation
   * @param operand
   */
  public void cols(Mat J, Op operation, Mat operand) {

  }

  /**
   * @param J
   * @param operation
   * @param operand
   */
  public void cols(Mat J, Op operation, double operand) {

  }

  /**
   * @param J
   * @return
   */
  public Mat rows(Mat J) {
    return null;
  }

  /**
   * @param J
   * @param operation
   */
  public void rows(Mat J, Op operation) {

  }

  /**
   * @param J
   * @param operation
   * @param operand
   */
  public void rows(Mat J, Op operation, Mat operand) {

  }

  /**
   * @param J
   * @param operation
   * @param operand
   */
  public void rows(Mat J, Op operation, double operand) {

  }
  
  /**
   * @param I
   * @param J
   * @return
   */
  public Mat submat(Mat I, Mat J) {
    return null;
  }
  
  /**
   * @param I
   * @param J
   * @param operation 
   */
  public void submat(Mat I, Mat J, Op operation) {

  }
  
  /**
   * @param I
   * @param J
   * @param operation 
   * @param operand 
   */
  public void submat(Mat I, Mat J, Op operation, Mat operand) {

  }
  
  /**
   * @param I
   * @param J
   * @param operation 
   * @param operand 
   */
  public void submat(Mat I, Mat J, Op operation, double operand) {

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
   * @param operation
   */
  public void diag(Op operation) {
    
  }
  
  /**
   * Performs a right-hand side element-wise operation on all elements on the main diagonal.
   * 
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException Thrown if the number of elements on main diagonal does not match with the provided
   *           right-hand side operand.
   * @throws UnsupportedOperationException 
   */
  public void diag(Op operation, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    if (!operand.is_vec()) {
      throw new IllegalArgumentException("The provided right-hand side operand needs to be a vector.");
    }
    
    int length = Math.min(n_cols, n_rows);
    if (operand.n_elem != length) {
      throw new IllegalArgumentException("The number of elements on main diagonal (n_elem = " + length + ") does not match with the provided right-hand side operand (n_elem = " + operand.n_elem + ").");
    }

    DenseMatrix64F memptrOperand = operand.memptr();
    for (int i = 0; i < length; i++) {
      at(i, i, operation, memptrOperand.get(i));
    }
  }

  /**
   * Performs a right-hand side element-wise operation on all elements on the main diagonal. The single provided
   * right-hand side operand is used for all operations.
   * 
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException 
   * @throws UnsupportedOperationException 
   */
  public void diag(Op operation, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    int length = Math.min(n_cols, n_rows);
    for (int i = 0; i < length; i++) {
      at(i, i, operation, operand);
    }
  }
  
  /**
   * @param k
   * @return
   */
  public Mat diag(int k) {
    return null;
  }

  /**
   * @param k
   * @param operation
   */
  public void diag(int k, Op operation) {
    
  }

  /**
   * @param k
   * @param operation
   * @param operand
   */
  public void diag(int k, Op operation, Mat operand) {
    
  }

  /**
   * @param k
   * @param operation
   * @param operand
   */
  public void diag(int k, Op operation, double operand) {
    
  }
  
  /**
   * @param operation
   */
  public void each_col(Op operation) {
    
  }
  
  /**
   * Performs the provided right-hand side element-wise operation on each column.
   * 
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException 
   * @throws UnsupportedOperationException 
   * 
   * @see #col(int, Op, Mat)
   */
  public void each_col(Op operation, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    for (int j = 0; j < n_cols; j++) {
      col(j, operation, operand);
    }
  }

  /**
   * Performs the provided right-hand side element-wise operation on each column. The single provided right-hand side
   * operand is used for all operations.
   * 
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException 
   * @throws UnsupportedOperationException 
   * 
   * @see #col(int, Op, double)
   */
  public void each_col(Op operation, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    for (int j = 0; j < n_cols; j++) {
      col(j, operation, operand);
    }
  }
  
  /**
   * @param I 
   * @param operation
   */
  public void each_col(Mat I, Op operation) {
    
  }
  
  /**
   * @param I 
   * @param operation
   * @param operand 
   */
  public void each_col(Mat I, Op operation, Mat operand) {
    
  }
  
  /**
   * @param I 
   * @param operation
   * @param operand 
   */
  public void each_col(Mat I, Op operation, double operand) {
    
  }

  /**
   * @param operation
   */
  public void each_row(Op operation) {
    
  }
  
  /**
   * Performs the provided right-hand side element-wise operation on each row.
   * 
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException 
   * @throws UnsupportedOperationException 
   * 
   * @see #row(int, Op, Mat)
   */
  public void each_row(Op operation, Mat operand) throws IllegalArgumentException, UnsupportedOperationException {
    for (int i = 0; i < n_rows; i++) {
      row(i, operation, operand);
    }
  }

  /**
   * Performs the provided right-hand side element-wise operation on each row. The single provided right-hand side
   * operand is used for all operations.
   * 
   * @param operation The operation to be performed.
   * @param operand The right-hand side operand.
   * 
   * @throws IllegalArgumentException 
   * @throws UnsupportedOperationException 
   * 
   * @see #row(int, Op, double)
   */
  public void each_row(Op operation, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    for (int i = 0; i < n_rows; i++) {
      row(i, operation, operand);
    }
  }
  
  /**
   * @param I 
   * @param operation
   */
  public void each_row(Mat I, Op operation) {
    
  }
  
  /**
   * @param I 
   * @param operation
   * @param operand 
   */
  public void each_row(Mat I, Op operation, Mat operand) {
    
  }
  
  /**
   * @param I 
   * @param operation
   * @param operand 
   */
  public void each_row(Mat I, Op operation, double operand) {
    
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
   * @see #elemTimes(double)
   */
  public Mat times(double operand) {
    return elemTimes(operand);
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
   * @return
   */
  public Mat negate() {
    return null;
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
   * 
   */
  public void zeros() {

  }

  /**
   * @param numberOfElements
   */
  public void zeros(int numberOfElements) {

  }

  /**
   * @param numberOfRows
   * @param numberOfColumns
   */
  public void zeros(int numberOfRows, int numberOfColumns) {

  }

  /**
   * 
   */
  public void ones() {

  }

  /**
   * @param numberOfElements
   */
  public void ones(int numberOfElements) {

  }

  /**
   * @param numberOfRows
   * @param numberOfColumns
   */
  public void ones(int numberOfRows, int numberOfColumns) {

  }

  /**
   * @param numberOfRows
   * @param numberOfColumns
   */
  public void eye(int numberOfRows, int numberOfColumns) {

  }

  /**
   * 
   */
  public void randu() {

  }

  /**
   * @param numberOfElements
   */
  public void randu(int numberOfElements) {

  }

  /**
   * @param numberOfRows
   * @param numberOfColumns
   * @param rng
   */
  public void randu(int numberOfRows, int numberOfColumns, Random rng) {

  }

  /**
   * 
   */
  public void randn() {

  }

  /**
   * @param numberOfElements
   */
  public void randn(int numberOfElements) {

  }
  
  /**
   * @param numberOfRows
   * @param numberOfColumns
   * @param rng
   */
  public void randn(int numberOfRows, int numberOfColumns, Random rng) {

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
    // EJML fails if DenseMatrix64F was not properly initialised.
    if (n_rows == 0 && n_cols == 0) {
      _matrix = new DenseMatrix64F(numberOfRows, numberOfColumns);
    } else {
      _matrix.reshape(numberOfRows, numberOfColumns);
    }

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
   * Returns true if the matrix is a column-vector and false otherwise.
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
  int convertMajorOrdering(int n) throws IllegalArgumentException {
    if (n < 0) {
      throw new IllegalArgumentException("The value of the provided position must be non-negative.");
    }

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