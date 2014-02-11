/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package org.armadillojava;

import java.util.Arrays;

import org.netlib.util.intW;

import com.github.fommil.netlib.BLAS;
import com.github.fommil.netlib.LAPACK;

/**
 * Provides stand-alone linear algebra methods.
 * <p>
 * The interfaces are similar to the Armadillo C++ API by Conrad Sanderson et al., NICTA, Australia.
 * <p>
 * If not stated otherwise (marked as non-canonical), the provided interfaces are identical to Armadillo C++ (e.g. same
 * ordering of arguments, accepted values, ...).
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 */
public class Arma {

  /**
   * Returns a matrix with the specified number of rows and columns and all elements along the main diagonal set to 1
   * and all others to 0.
   * 
   * @param return_type The matrix type to be returned
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat eye(int n_rows, int n_cols) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
     */

    return new Mat(n_rows, n_cols, Fill.EYE);
  }

  /**
   * Returns a vector/matrix with 100 elements linear increasing from the specified value to start with to the specified
   * value to end with (both included).
   * <p>
   * If a non-vector type is to be returned, it will be in the shape of a column vector.
   * 
   * @param return_type The type of vector/matrix to be returned
   * @param start The first value
   * @param end The last value
   * 
   * @throws RuntimeException The specified value to start with ({@code start}) must be less than or equal the specified
   *           value to end with ({@code end}).
   */
  public static <T extends AbstractMat> T linspace(Class<T> return_type, int start, int end) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "start" and "end" are validated within linspace(Class<T>, int, int, int).
     */

    return linspace(return_type, start, end, 100);
  }

  /**
   * Returns a vector/matrix with the specified number of elements linear increasing from the specified value to start
   * with to the specified value to end with (both included).
   * <p>
   * If a matrix type is specified as return type, it will be in the shape of a column vector.
   * 
   * @param return_type The type of vector/matrix to be returned
   * @param start The value to start with
   * @param end The value to end with
   * @param N The number of elements
   * 
   * @throws RuntimeException The specified value to start with ({@code start}) must be less than or equal the specified
   *           value to end with ({@code end}).
   * @throws NegativeArraySizeException The specified number of elements ({@code N}) must be positive.
   */
  public static <T extends AbstractMat> T linspace(Class<T> return_type, int start, int end, int N) throws RuntimeException, NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "N" is validated within set_size(int).
     */

    if (end < start) {
      throw new RuntimeException("The specified value to start with (" + start + ") must be less than or equal the specified value to end with (" + end + ").");
    }

    T result = return_type.newInstance();
    result.set_size(N);

    if (N > 0) {
      double stepLength = (end - start) / (result.n_elem - 1);
      for (int n = 0; n < result.n_elem - 1; n++) {
        /*
         * While increasing the value step by step per stepLength will be faster, it will also reduce the precision.
         */
        result._data[n] = start + stepLength * n;
      }
      /*
       * This way, the first and last value will be exactly the same as provided, without any additional loss of
       * precision.
       */
      result._data[N] = end;
    }

    return result;
  }

  /**
   * Returns a vector of ones with the specified number of elements.
   * 
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T ones(Class<T> return_type, int n_elem) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result = return_type.newInstance();
    result.set_size(n_elem);
    result.fill(1);

    return result;
  }

  /**
   * Returns a matrix of ones with the specified number of rows and columns.
   * 
   * @param return_type The type of matrix to be returned
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat ones(int n_rows, int n_cols) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
     */

    return new Mat(n_rows, n_cols, Fill.ONES);
  }

  /**
   * Returns a vector with the specified number of elements and all values drawn from the discrete uniform distribution
   * [0, Integer.MAX_VALUE - 1].
   * 
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T randi(Class<T> return_type, int n_elem) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_elem" is validated within randi(Class<T>, int, DistrParam).
     */

    return randi(return_type, n_elem, new DistrParam(0, Integer.MAX_VALUE - 1));
  }

  /**
   * Returns a vector with the specified number of elements and values drawn from the discrete uniform distribution [
   * {@code distr_param._a}, {@code distr_param._b}].
   * 
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * @param distr_param The distribution parameter
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   * @throws The first end point ({@code distr_param._a}) of the interval must be less than or equal the last end point
   *           ({@code distr_param._b}).
   * @throws RuntimeException The difference between the first end point ({@code distr_param._a}) and the last end point
   *           ({@code distr_param._b}) can be at most Integer.MAX_VALUE - 1.
   */
  public static <T extends AbstractVector> T randi(Class<T> return_type, int n_elem, DistrParam distr_param) throws NegativeArraySizeException, RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    if (distr_param._b < distr_param._a) {
      throw new RuntimeException("The first end point (" + distr_param._a + ") of the interval must be less than or equal the last end point (" + distr_param._a + ").");
    }

    if (distr_param._b - distr_param._a > Integer.MAX_VALUE - 1) {
      throw new RuntimeException("The difference between the first end point (" + distr_param._a + ") and the last end point (" + distr_param._b + ") can be at most Integer.MAX_VALUE - 1.");
    }

    T result = return_type.newInstance();
    result.set_size(n_elem);

    for (int n = 0; n < result.n_elem; n++) {
      result._data[n] = distr_param._a + RNG._rng.nextInt(distr_param._b - distr_param._a + 1);
    }

    return result;
  }

  /**
   * Returns a matrix with the specified number of rows and columns and values drawn from the discrete uniform
   * distribution [0, Integer.MAX_VALUE - 1].
   * 
   * @param return_type The type of vector to be returned
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randi(int n_rows, int n_cols) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_rows" and "n_cols" are validated within randi(Class<T>, int, int, DistrParam).
     */

    return randi(n_rows, n_rows, new DistrParam(0, Integer.MAX_VALUE - 1));
  }

  /**
   * Returns a matrix with the specified number of rows and columns and values drawn from the discrete uniform
   * distribution [{@code distr_param._a}, {@code distr_param._b}].
   * 
   * @param return_type The type of matrix to be returned
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * @param distr_param The distribution parameter
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   * @throws The first end point ({@code distr_param._a}) of the interval must be less than or equal the last end point
   *           ({@code distr_param._b}).
   * @throws RuntimeException The difference between the first end point ({@code distr_param._a}) and the last end point
   *           ({@code distr_param._b}) can be at most Integer.MAX_VALUE - 1.
   */
  public static Mat randi(int n_rows, int n_cols, DistrParam distr_param) throws NegativeArraySizeException, RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_rows" and "n_cols" are validated within Mat(int, int).
     */

    if (distr_param._b < distr_param._a) {
      throw new RuntimeException("The first end point (" + distr_param._a + ") of the interval must be less than or equal the last end point (" + distr_param._a + ").");
    }

    if (distr_param._b - distr_param._a > Integer.MAX_VALUE - 1) {
      throw new RuntimeException("The difference between the first end point (" + distr_param._a + ") and the last end point (" + distr_param._b + ") can be at most Integer.MAX_VALUE - 1.");
    }

    Mat result = new Mat(n_rows, n_cols);

    for (int n = 0; n < result.n_elem; n++) {
      result._data[n] = distr_param._a + RNG._rng.nextInt(distr_param._b - distr_param._a + 1);
    }

    return result;
  }

  /**
   * Returns a vector with the specified number of elements and values drawn from the standard uniform
   * distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T randu(Class<T> return_type, int n_elem) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result = return_type.newInstance();
    result.set_size(n_elem);
    result.randu();

    return result;
  }

  /**
   * Returns a matrix with the specified number of rows and columns and values drawn from the standard uniform
   * distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param return_type The type of matrix to be returned
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randu(int n_rows, int n_cols) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
     */

    return new Mat(n_rows, n_cols, Fill.RANDU);
  }

  /**
   * Returns a vector with the specified number of elements and values drawn from the standard normal
   * distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T randn(Class<T> return_type, int n_elem) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result = return_type.newInstance();
    result.set_size(n_elem);
    result.randn();

    return result;
  }

  /**
   * Returns a matrix with the specified number of rows and columns and values drawn from the standard normal
   * distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param return_type The type of matrix to be returned
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randn(int n_rows, int n_cols) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_rows" and "n_cols" are validated within Mat(int, int).
     */

    return new Mat(n_rows, n_cols, Fill.RANDN);
  }

  /**
   * Creates a matrix by replicating the matrix by the specified times per row and column.
   * 
   * @param A The matrix
   * @param num_copies_per_row The number of copies per row
   * @param num_copies_per_col The number of copies per column
   * 
   * @throws NegativeArraySizeException The specified number of copies per row ({@code num_copies_per_row}) must be
   *           positive.
   * @throws NegativeArraySizeException The specified number of copies per column ({@code num_copies_per_col}) must be
   *           positive.
   */
  public static Mat repmat(AbstractMat A, int num_copies_per_row, int num_copies_per_col) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    if (num_copies_per_row < 0) {
      throw new NegativeArraySizeException("The specified number of copies per row (" + num_copies_per_row + ") must be positive.");
    }

    if (num_copies_per_col < 0) {
      throw new NegativeArraySizeException("The specified number of copies per column (" + num_copies_per_col + ") must be positive.");
    }

    Mat result = new Mat(A.n_rows * num_copies_per_row, A.n_cols * num_copies_per_col);

    if (!A.is_empty()) {
      /*
       * First, copy "A.n_cols" columns alongside the rows ...
       */
      for (int i = 0; i < num_copies_per_row; i++) {
        new ViewSubMat(result, i * A.n_rows, A.n_rows, 0, A.n_cols).inPlaceEqual(A);
      }

      /*
       * ... Secondly, copy "A.n_rows * num_copies_per_row" rows alongside the columns.
       * Copying alongside the columns is way faster and should therefore handle the largest amount of data to be
       * copied.
       */
      for (int j = 0; j < num_copies_per_row; j++) {
        System.arraycopy(result._data, 0, result._data, j * result.n_rows * A.n_cols, result.n_rows * A.n_cols);
      }
    }

    return result;
  }

  /**
   * Returns a toeplitz matrix with the provided vector as the first column of the matrix.
   * 
   * @param A The vector
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape to a
   *           vector.
   */
  public static Mat toeplitz(AbstractMat A) throws RuntimeException, InstantiationException, IllegalAccessException {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, A.n_elem);

    new ViewDiag(result, 0).inPlaceEqual(A._data[0]);
    for (int n = 1; n < A.n_elem; n++) {
      double value = A._data[n];

      new ViewDiag(result, n).inPlaceEqual(value);
      new ViewDiag(result, -n).inPlaceEqual(value);
    }

    return result;
  }

  /**
   * Returns a toeplitz matrix with the first provided vector as the first column and the second one as the first row of
   * the matrix.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a vector.
   * @throws RuntimeException The second provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in
   *           shape to a vector.
   */
  public static Mat toeplitz(AbstractMat A, AbstractMat B) throws RuntimeException, InstantiationException, IllegalAccessException {
    if (A.is_empty()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (A.is_empty()) {
      throw new RuntimeException("The second provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    if (!B.is_vec()) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, B.n_elem);

    new ViewDiag(result, 0).inPlaceEqual(A._data[0]);
    for (int n = 1; n < A.n_elem; n++) {
      new ViewDiag(result, -n).inPlaceEqual(A._data[n]);
    }

    for (int n = 1; n < B.n_elem; n++) {
      new ViewDiag(result, n).inPlaceEqual(B._data[n]);
    }

    return result;
  }

  /**
   * Returns a circulant toeplitz matrix with the provided vector as the first column of the matrix.
   * 
   * @param vector The vector
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape to a
   *           vector.
   */
  public static Mat circ_toeplitz(AbstractMat A) throws RuntimeException, InstantiationException, IllegalAccessException {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, A.n_elem);

    new ViewDiag(result, 0).inPlaceEqual(A._data[0]);
    for (int n = 1; n < A.n_elem; n++) {
      double value = A._data[n];

      new ViewDiag(result, A.n_elem - n).inPlaceEqual(value);
      new ViewDiag(result, -n).inPlaceEqual(value);
    }

    return result;
  }

  /**
   * Returns a null vector with the specified number of elements.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T zeros(Class<T> return_type, int n_elem) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     * All uninitialised matrices are already equal to a zero matrix.
     */

    T result = return_type.newInstance();
    result.set_size(n_elem);

    return result;
  }

  /**
   * Returns a zero matrix with the specified number of rows and columns.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat zeros(int n_rows, int n_cols) throws NegativeArraySizeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "n_rows" and "n_cols" are validated within Mat(int, int).
     * All uninitialised matrices are already equal to a zero matrix.
     */

    return new Mat(n_rows, n_cols);
  }

  protected static void abs(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.abs(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The column vector
   */
  public static Col abs(Col X) {
    Col result = new Col(X.n_elem);
    abs(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The row vector
   */
  public static Row abs(Row X) {
    Row result = new Row(X.n_elem);
    abs(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat abs(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    abs(result, X);
    return result;
  }

  protected static void eps(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.ulp(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed positive distances between each value and next larger
   * representable of the provided one.
   * 
   * @param X The column vector
   */
  public static Col eps(Col X) {
    Col result = new Col(X.n_elem);
    eps(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed positive distances between each value and next larger
   * representable of the provided one.
   * 
   * @param X The row vector
   */
  public static Row eps(Row X) {
    Row result = new Row(X.n_elem);
    eps(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed positive distances between each value and next larger
   * representable of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat eps(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    eps(result, X);
    return result;
  }

  protected static void exp(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.exp(A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col exp(Col A) {
    Col result = new Col(A.n_elem);
    exp(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row exp(Row A) {
    Row result = new Row(A.n_elem);
    exp(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat exp(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp(result, A);
    return result;
  }

  protected static void exp2(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.pow(2, A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col exp2(Col A) {
    Col result = new Col(A.n_elem);
    exp2(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row exp2(Row A) {
    Row result = new Row(A.n_elem);
    exp2(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat exp2(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp2(result, A);
    return result;
  }

  protected static void exp10(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.pow(10, A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col exp10(Col A) {
    Col result = new Col(A.n_elem);
    exp10(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row exp10(Row A) {
    Row result = new Row(A.n_elem);
    exp10(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat exp10(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp10(result, A);
    return result;
  }

  protected static void trunc_exp(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      double exp = Math.exp(A._data[n]);

      if (!Double.isInfinite(exp)) {
        result._data[n] = exp;
      } else {
        result._data[n] = Double.MAX_VALUE;
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e exponential values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the largest representable number.
   * 
   * @param A The column vector
   */
  public static Col trunc_exp(Col A) {
    Col result = new Col(A.n_elem);
    trunc_exp(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e exponential values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the largest representable number.
   * 
   * @param A The row vector
   */
  public static Row trunc_exp(Row A) {
    Row result = new Row(A.n_elem);
    trunc_exp(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e exponential values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the largest representable number.
   * 
   * @param A The matrix
   */
  public static Mat trunc_exp(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    trunc_exp(result, A);
    return result;
  }

  protected static void log(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.log(A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col log(Col A) {
    Col result = new Col(A.n_elem);
    log(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row log(Row A) {
    Row result = new Row(A.n_elem);
    log(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat log(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log(result, A);
    return result;
  }

  protected static void log2(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.log(A._data[n]) / Math.log(2);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col log2(Col A) {
    Col result = new Col(A.n_elem);
    log2(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row log2(Row A) {
    Row result = new Row(A.n_elem);
    log2(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat log2(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log2(result, A);
    return result;
  }

  protected static void log10(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.log10(A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col log10(Col A) {
    Col result = new Col(A.n_elem);
    log10(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row log10(Row A) {
    Row result = new Row(A.n_elem);
    log10(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat log10(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log10(result, A);
    return result;
  }

  protected static void trunc_log(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      double value = A._data[n];

      if (value <= 0) {
        value = Double.MIN_NORMAL;
      } else if (Double.isInfinite(value)) {
        value = Double.MAX_VALUE;
      }

      result._data[n] = Math.log(value);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e logarithmic values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the base-e logarithmic of the largest representable
   * number and values that would return a complex number are truncated to the base-e logarithmic of the smallest
   * representable number.
   * 
   * @param A The column vector
   */
  public static Col trunc_log(Col A) {
    Col result = new Col(A.n_elem);
    trunc_log(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e logarithmic values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the base-e logarithmic of the largest representable
   * number and values that would return a complex number are truncated to the base-e logarithmic of the smallest
   * representable number.
   * 
   * @param A The row vector
   */
  public static Row trunc_log(Row A) {
    Row result = new Row(A.n_elem);
    trunc_log(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e logarithmic values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the base-e logarithmic of the largest representable
   * number and values that would return a complex number are truncated to the base-e logarithmic of the smallest
   * representable number.
   * 
   * @param A The matrix
   */
  public static Mat trunc_log(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    trunc_log(result, A);
    return result;
  }

  protected static void pow(AbstractMat result, AbstractMat A, int p) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.pow(A._data[n], p);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed raise to the specified power of the provided one.
   * 
   * @param A The column vector
   * @param p The power
   */
  public static Col pow(Col A, int p) {
    Col result = new Col(A.n_elem);
    pow(result, A, p);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed raise to the specified power of the provided one.
   * 
   * @param A The row vector
   * @param p The power
   */
  public static Row pow(Row A, int p) {
    Row result = new Row(A.n_elem);
    pow(result, A, p);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed raise to the specified power of the provided one.
   * 
   * @param A The matrix
   * @param p The power
   */
  public static Mat pow(Mat A, int p) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    pow(result, A, p);
    return result;
  }

  protected static void sqrt(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.sqrt(A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed square root of the provided one.
   * 
   * @param A The column vector
   */
  public static Col sqrt(Col A) {
    Col result = new Col(A.n_elem);
    sqrt(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed square root of the provided one.
   * 
   * @param A The row vector
   */
  public static Row sqrt(Row A) {
    Row result = new Row(A.n_elem);
    sqrt(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed square root of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat sqrt(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    sqrt(result, A);
    return result;
  }

  protected static void square(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.pow(A._data[n], 2);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed squared values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col square(Col A) {
    Col result = new Col(A.n_elem);
    square(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed squared values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row square(Row A) {
    Row result = new Row(A.n_elem);
    square(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed squared values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat square(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    square(result, A);
    return result;
  }

  protected static void floor(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.floor(A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed floored values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col floor(Col A) {
    Col result = new Col(A.n_elem);
    floor(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed floored values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row floor(Row A) {
    Row result = new Row(A.n_elem);
    floor(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed floored values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat floor(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    floor(result, A);
    return result;
  }

  protected static void ceil(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.ceil(A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col ceil(Col A) {
    Col result = new Col(A.n_elem);
    ceil(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row ceil(Row A) {
    Row result = new Row(A.n_elem);
    ceil(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat ceil(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    ceil(result, A);
    return result;
  }

  protected static void round(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.round(A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col round(Col A) {
    Col result = new Col(A.n_elem);
    round(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row round(Row A) {
    Row result = new Row(A.n_elem);
    round(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat round(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    round(result, A);
    return result;
  }

  protected static void sign(AbstractMat result, AbstractMat A) {
    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.signum(A._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed sign values of the provided one.
   * <p>
   * The sign value is -1 for negative, 0 for zero and 1 for non-negative values.
   * 
   * @param A The column vector
   */
  public static Col sign(Col A) {
    Col result = new Col(A.n_elem);
    sign(result, A);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed sign values of the provided one.
   * <p>
   * The sign value is -1 for negative, 0 for zero and 1 for non-negative values.
   * 
   * @param A The row vector
   */
  public static Row sign(Row A) {
    Row result = new Row(A.n_elem);
    sign(result, A);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed sign values of the provided one.
   * <p>
   * The sign value is -1 for negative, 0 for zero and 1 for non-negative values.
   * 
   * @param A The matrix
   */
  public static Mat sign(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    sign(result, A);
    return result;
  }

  protected static void sin(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.sin(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed sine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col sin(Col X) {
    Col result = new Col(X.n_elem);
    sin(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed sine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row sin(Row X) {
    Row result = new Row(X.n_elem);
    sin(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed sine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat sin(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    sin(result, X);
    return result;
  }

  protected static void asin(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.asin(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col asin(Col X) {
    Col result = new Col(X.n_elem);
    asin(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row asin(Row X) {
    Row result = new Row(X.n_elem);
    asin(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat asin(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    asin(result, X);
    return result;
  }

  protected static void sinh(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.sinh(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col sinh(Col X) {
    Col result = new Col(X.n_elem);
    sinh(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row sinh(Row X) {
    Row result = new Row(X.n_elem);
    sinh(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat sinh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    sinh(result, X);
    return result;
  }

  protected static void asinh(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      double value = X._data[n];

      if (Double.isInfinite(value)) {
        result._data[n] = value;
      } else {
        result._data[n] = Math.log(value + Math.sqrt(Math.pow(value, 2) + 1));
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse hyperbolic sine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col asinh(Col X) {
    Col result = new Col(X.n_elem);
    asinh(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic sine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row asinh(Row X) {
    Row result = new Row(X.n_elem);
    asinh(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic sine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat asinh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    asinh(result, X);
    return result;
  }

  protected static void cos(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.cos(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed cosine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col cos(Col X) {
    Col result = new Col(X.n_elem);
    cos(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed cosine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row cos(Row X) {
    Row result = new Row(X.n_elem);
    cos(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed cosine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat cos(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    cos(result, X);
    return result;
  }

  protected static void acos(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.acos(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col acos(Col X) {
    Col result = new Col(X.n_elem);
    acos(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row acos(Row X) {
    Row result = new Row(X.n_elem);
    acos(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat acos(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    acos(result, X);
    return result;
  }

  protected static void cosh(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.cosh(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col cosh(Col X) {
    Col result = new Col(X.n_elem);
    cosh(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row cosh(Row X) {
    Row result = new Row(X.n_elem);
    cosh(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat cosh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    cosh(result, X);
    return result;
  }

  protected static void acosh(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      double value = X._data[n];
      result._data[n] = Math.log(value + Math.sqrt(Math.pow(value, 2) - 1));
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col acosh(Col X) {
    Col result = new Col(X.n_elem);
    acosh(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row acosh(Row X) {
    Row result = new Row(X.n_elem);
    acosh(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat acosh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    acosh(result, X);
    return result;
  }

  protected static void tan(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.tan(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed tangent of the provided one.
   * 
   * @param X The column vector
   */
  public static Col tan(Col X) {
    Col result = new Col(X.n_elem);
    tan(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed tangent of the provided one.
   * 
   * @param X The row vector
   */
  public static Row tan(Row X) {
    Row result = new Row(X.n_elem);
    tan(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed tangent of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat tan(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    tan(result, X);
    return result;
  }

  protected static void atan(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.atan(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The column vector
   */
  public static Col atan(Col X) {
    Col result = new Col(X.n_elem);
    atan(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The row vector
   */
  public static Row atan(Row X) {
    Row result = new Row(X.n_elem);
    atan(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat atan(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    atan(result, X);
    return result;
  }

  protected static void tanh(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = Math.tanh(X._data[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The column vector
   */
  public static Col tanh(Col X) {
    Col result = new Col(X.n_elem);
    tanh(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The row vector
   */
  public static Row tanh(Row X) {
    Row result = new Row(X.n_elem);
    tanh(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat tanh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    tanh(result, X);
    return result;
  }

  protected static void atanh(AbstractMat result, AbstractMat X) {
    for (int n = 0; n < X.n_elem; n++) {
      double value = X._data[n];
      result._data[n] = 0.5 * Math.log((1 + value) / (1 - value));
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The column vector
   */
  public static Col atanh(Col X) {
    Col result = new Col(X.n_elem);
    atanh(result, X);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The row vector
   */
  public static Row atanh(Row X) {
    Row result = new Row(X.n_elem);
    atanh(result, X);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat atanh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    atanh(result, X);
    return result;
  }

  /**
   * Returns the sum of all elements of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static double accu(AbstractMat X) throws RuntimeException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    double sum = X._data[0];

    for (int n = 1; n < X.n_elem; n++) {
      sum += X._data[n];
    }

    return sum;
  }

  /**
   * Returns the value of the provided (1,1)-matrix as a scalar.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have exactly one element.
   */
  public static double as_acalar(AbstractMat X) throws RuntimeException {
    if (X.n_elem != 1) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have exactly one element.");
    }

    return X._data[0];
  }

  /**
   * Returns the spectral condition number of the provided matrix.
   * 
   * @param A the matrix
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   */
  public static double cond(Mat A) throws RuntimeException {
    /*
     * The parameter "A" is validated within svd(AbstractMat).
     */

    // TODO Add exceptions thrown by svd(AbstractMat).
    Col singularValues = svd(A);

    return singularValues._data[0] / singularValues._data[singularValues.n_elem - 1];
  }

  /**
   * Returns the determinant of the provided matrix.
   * 
   * @param A The matrix
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   */
  public static double det(Mat A) throws RuntimeException {
    if (A.empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    /*
     * The following computation is based on how its done in Armadillo C++ 4.100.0.
     */
    double[] temp = Arrays.copyOf(A._data, A.n_elem);
    int[] pivotIndices = new int[Math.min(A.n_rows, A.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(A.n_rows, A.n_cols, temp, A.n_rows, pivotIndices, info);
    if (info.val != 0) {
      throw new RuntimeException("The matrix appears to be singular.");
    }

    double determinant = temp[0];
    boolean positiveSign = true;

    for (int i = 0; i < A.n_rows; i++) {
      determinant *= temp[i + i * A.n_rows];

      if (i != pivotIndices[i] - 1) {
        positiveSign = !positiveSign;
      }
    }

    return (positiveSign ? determinant : -determinant);
  }

  /**
   * Returns the dot product, assuming both provided matrices are vectors.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code A.n_elem},
   *           {@code B.n_elem}).
   */
  public static double dot(AbstractMat A, AbstractMat B) throws RuntimeException {
    if (A.n_elem != B.n_elem) {
      throw new RuntimeException("Both provided matrices must have the same number of elements (" + A.n_elem + ", " + B.n_elem + ").");
    }

    return BLAS.getInstance().ddot(A.n_elem, A._data, 1, B._data, 1);
  }

  /**
   * Returns the normalised dot product, assuming both provided matrices are vectors.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code A.n_elem},
   *           {@code B.n_elem}).
   */
  public static double norm_dot(AbstractMat A, AbstractMat B) throws RuntimeException {
    /*
     * The parameter "A" and "B" are validated within dot(AbstractMat, AbstractMat).
     */

    return dot(A, B) / Math.sqrt(dot(A, A) * dot(B, B));
  }

  /**
   * Calculates the base-e logarithmic determinant of the provided matrix and stores the value in {@code val} and sign
   * in {@code sign}.
   * <p>
   * <b>Note:</b> Unfortunately, the storage variables must be of the mutable type double[], respectively int[].
   * 
   * @param val The value storage
   * @param sign The sign storage
   * @param A The matrix
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   */
  public static void log_det(double[] val, int[] sign, Mat A) throws RuntimeException {
    if (A.empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    /*
     * The following computation is based on how its done in Armadillo C++ 4.100.0
     */
    double[] temp = Arrays.copyOf(A._data, A.n_elem);
    int[] pivotIndices = new int[Math.min(A.n_rows, A.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(A.n_rows, A.n_cols, temp, A.n_rows, pivotIndices, info);
    if (info.val != 0) {
      throw new RuntimeException("The matrix appears to be singular.");
    }

    double determinant = temp[0];
    boolean positiveSign = true;

    for (int i = 0; i < A.n_rows; i++) {
      double value = temp[i + i * A.n_rows];

      determinant += Math.log(Math.abs(value));

      if (i != pivotIndices[i] - 1 ^ value < 0) {
        positiveSign = !positiveSign;
      }
    }

    val[0] = determinant;
    sign[0] = (positiveSign ? 1 : -1);
  }

  /**
   * Returns the specified norm of the provided matrix.
   * <p>
   * If the provided matrix is not equivalent in shape to a vector, an induced matrix norm is computed.
   * 
   * @param X The matrix
   * @param p The norm
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException For vectors, the norm indicator ({@code p}) must be strict greater than 0.
   * @throws IllegalArgumentException For non-vectors, the norm indicator ({@code p}) must be one of 1 or 2.
   */
  public static double norm(AbstractMat X, int p) throws RuntimeException, IllegalArgumentException {
    if (X.empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    double norm = 0;

    if (X.is_vec()) {
      if (p < 0) {
        throw new IllegalArgumentException("For vectors, the norm indicator (" + p + ") must be strict greater than 0.");
      }

      switch (p) {
        case 1:
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.abs(X._data[n]);
          }
          break;
        case 2:
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.pow(X._data[n], 2);
          }
          norm = Math.sqrt(norm);
          break;
        default:
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.pow(Math.abs(X._data[n]), p);
          }
          norm = Math.pow(norm, 1 / p);
          break;
      }

    } else {
      if (p != 1 && p != 2) {
        throw new IllegalArgumentException("For non-vectors, the norm indicator (" + p + ") must be one of 1 or 2.");
      }

      switch (p) {
        case 1:
          norm = Double.NEGATIVE_INFINITY;
          for (int j = 0; j < X.n_cols; j++) {
            int columnIndex = j * X.n_rows;
            double sum = 0;
            for (int i = 0; i < X.n_rows; i++) {
              sum += Math.abs(X._data[i + columnIndex]);
            }

            norm = Math.max(norm, sum);
          }
          break;
        case 2:
          Col singularValues = svd((Mat) X);
          norm = singularValues._data[0];
          break;
      }
    }

    return norm;
  }

  /**
   * Returns the specified norm of the provided matrix.
   * <p>
   * If the provided matrix is not equivalent in shape to a vector, an induced matrix norm is computed.
   * 
   * @param X The matrix
   * @param p The norm
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException For vectors, the norm indicator ({@code p}) must be one of '-inf', 'inf' or 'fro'.
   * @throws IllegalArgumentException For non-vectors, the norm indicator ({@code p}) must be one of 'inf' or 'fro'.
   */
  public static double norm(AbstractMat X, String p) throws RuntimeException, IllegalArgumentException {
    if (X.empty()) {
      throw new RuntimeException("The provided matrix must have at least one element.");
    }

    double norm = 0;

    if (X.is_vec()) {
      switch (p) {
        case "-inf":
          for (int n = 0; n < X.n_elem; n++) {
            norm = Math.max(norm, Math.abs(X._data[n]));
          }
          break;
        case "inf":
          for (int n = 0; n < X.n_elem; n++) {
            norm = Math.min(norm, Math.abs(X._data[n]));
          }
          norm = Math.sqrt(norm);
          break;
        case "fro":
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.pow(X._data[n], 2);
          }
          norm = Math.sqrt(norm);
          break;
        default:
          throw new IllegalArgumentException("For vectors, the norm indicator (" + p + ") must be one of '-inf', 'inf' or 'fro'.");
      }

    } else {
      switch (p) {
        case "inf":
          norm = Double.NEGATIVE_INFINITY;
          for (int i = 0; i < X.n_rows; i++) {

            double sum = 0;
            for (int j = 0; j < X.n_cols; j++) {
              sum += Math.abs(X._data[i + j * X.n_rows]);
            }

            norm = Math.max(norm, sum);
          }
          break;
        case "fro":
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.pow(X._data[n], 2);
          }
          norm = Math.sqrt(norm);
          break;
        default:
          throw new IllegalArgumentException("For non-vectors, the norm indicator (" + p + ") must be one of 'inf' or 'fro'.");
      }
    }

    return norm;
  }

  /**
   * Returns the rank of the provided matrix based on singular value decomposition.
   * <p>
   * All singular values smaller than {@code Math.max(X.n_rows, X.n_cols) * Math.ulp(X.max())} are treated as zero.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static int rank(Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within rank(AbstractMat, double).
     */

    return rank(X, Math.max(X.n_rows, X.n_cols) * Math.ulp(X.max()));
  }

  /**
   * Returns the rank of the provided matrix based on singular value decomposition.
   * <p>
   * All singular values smaller than the specified tolerance are treated as zero.
   * 
   * @param X The matrix
   * @param tolerance The tolerance
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static int rank(Mat X, double tolerance) throws RuntimeException {
    /*
     * The parameter "X" is validated within svd(AbstractMat).
     */

    // TODO Add exceptions thrown by svd(AbstractMat).
    Col singularValues = svd(X);

    int rank = 0;
    for (int n = 0; n < singularValues.n_elem; n++) {
      if (singularValues._data[n] > tolerance) {
        rank++;
      }
    }

    return rank;
  }

  /**
   * Returns the trace of the matrix.
   * 
   * @param matrix The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must be square.
   */
  public static double trace(Mat X) throws RuntimeException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    if (!X.is_square()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must be square.");
    }

    double trace = X._data[0];
    for (int i = 1; i < X.n_rows; i++) {
      trace += X._data[i + i * X.n_rows];
    }

    return trace;
  }

  /**
   * Returns a deep copy of the main diagonal of the provided matrix as a column vector.
   * 
   * @param A The matrix
   */
  public static <T extends Col> T diagvec(Mat A) {
    return diagvec(A, 0);
  }

  /**
   * Returns a deep copy of the {@code k}th diagonal of the provided matrix as a column vector.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, its the main diagonal.
   * <li>For {@code k} > 0, its the {@code k}th super-diagonal.
   * <li>For {@code k} < 0, its the {@code k}th sub-diagonal.
   * </ul>
   * 
   * @param A The matrix
   * @param k The diagonal position
   * 
   * @throws IndexOutOfBoundsException The diagonal index ({@code k}) is out of bounds.
   */
  public static <T extends Col> T diagvec(Mat A, int k) throws IndexOutOfBoundsException {
    /*
     * The parameter "k" is validated within A.diag(int).
     */

    return A.diag(k);
  }

  /**
   * Returns the smallest value within the provided vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double min(AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within V.min().
     */

    return V.min();
  }

  /**
   * Returns the smallest value for each column of the provided sub view.
   * 
   * @param X The sub view
   */
  protected static double min(AbstractView V) {
    double minimum = V._data[V.iteratorNext()];

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      minimum = Math.min(minimum, V._data[V.iteratorNext()]);
    }

    return minimum;
  }

  /**
   * Returns the smallest value for each column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided matrix must have at least one row.
   */
  public static <T extends AbstractVector> T min(Class<T> return_type, Mat X) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "V" is validated within min(Class<T>, Mat, int).
     */

    return min(return_type, X, 0);
  }

  /**
   * Returns the smallest value for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T min(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException, InstantiationException, IllegalAccessException {
    T result = return_type.newInstance();

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = min(new ViewSubCol(X, j));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = min(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the element-wise smallest values between the provided matrices the matrix.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @throws RuntimeException Both provided matrices ({@code A.n_rows}, {@code B.n_cols} and {@code A.n_rows},
   *           {@code B.n_cols}) must have the same shape.
   */
  public static Mat min(Mat A, Mat B) throws RuntimeException {
    if (A.n_rows != B.n_rows || A.n_cols != B.n_cols) {
      throw new RuntimeException("Both provided matrices (" + A.n_rows + ", " + B.n_cols + " and " + A.n_rows + ", " + B.n_cols + ") must have the same shape.");
    }

    Mat result = new Mat(A.n_rows, A.n_cols);

    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.min(A._data[n], B._data[n]);
    }

    return result;
  }

  /**
   * Returns the largest value within the provided vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double max(AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within V.max().
     */

    return V.max();
  }

  protected static double max(AbstractView V) {
    double maximum = V._data[V.iteratorNext()];

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      maximum = Math.max(maximum, V._data[V.iteratorNext()]);
    }

    return maximum;
  }

  /**
   * Returns the largest value for each column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T max(Class<T> return_type, Mat X) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "V" is validated within max(Class<T>, Mat, int).
     */

    return max(return_type, X, 0);
  }

  /**
   * Returns the largest value for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T max(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException, InstantiationException, IllegalAccessException {
    T result = return_type.newInstance();

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = max(new ViewSubCol(X, j));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = max(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the element-wise largest values between the provided matrices the matrix.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @throws RuntimeException Both provided matrices ({@code A.n_rows}, {@code B.n_cols} and {@code A.n_rows},
   *           {@code B.n_cols}) must have the same shape.
   */
  public static Mat max(Mat A, Mat B) throws RuntimeException {
    if (A.n_rows != B.n_rows || A.n_cols != B.n_cols) {
      throw new RuntimeException("Both provided matrices (" + A.n_rows + ", " + B.n_cols + " and " + A.n_rows + ", " + B.n_cols + ") must have the same shape.");
    }

    Mat result = new Mat(A.n_rows, A.n_cols);

    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.max(A._data[n], B._data[n]);
    }

    return result;
  }

  /**
   * Returns the product of all elements of the provided vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static double prod(AbstractVector V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    double prod = V._data[0];

    for (int n = 1; n < V.n_elem; n++) {
      prod *= V._data[n];
    }

    return prod;
  }

  protected static double prod(AbstractView V) {
    double product = 0;

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      product *= V._data[V.iteratorNext()];
    }

    return product;
  }

  /**
   * Returns the product of all elements per column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T prod(Class<T> return_type, Mat X) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within prod(Class<T>, Mat, int).
     */

    return prod(return_type, X, 0);
  }

  /**
   * Returns the product of all elements per column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T prod(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException, InstantiationException, IllegalAccessException {
    T result = return_type.newInstance();

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = prod(new ViewSubCol(X, j));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = prod(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the sum of all elements of the provided vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double sum(AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within accu(AbstractMat).
     */

    return accu(V);
  }

  protected static double sum(AbstractView V) throws RuntimeException {
    double sum = 0;

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      sum += V._data[V.iteratorNext()];
    }

    return sum;
  }

  /**
   * Returns the sum of all elements per column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T sum(Class<T> return_type, Mat X) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within sum(Class<T>, Mat, int).
     */

    return sum(return_type, X, 0);
  }

  /**
   * Returns the sum of all elements per column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T sum(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException, InstantiationException, IllegalAccessException {
    T result = return_type.newInstance();

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = sum(new ViewSubCol(X, j));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = sum(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the mean of the provided vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double mean(AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within sum(AbstractVector).
     */

    return sum(V) / V.n_elem;
  }

  protected static double mean(AbstractView V) throws RuntimeException {
    return sum(V) / V.n_elem;
  }

  /**
   * Returns the mean for each column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T mean(Class<T> return_type, Mat X) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within mean(Class<T>, Mat, int).
     */

    return mean(return_type, X, 0);
  }

  /**
   * Returns the mean for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T mean(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException, InstantiationException, IllegalAccessException {
    T result = return_type.newInstance();

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = mean(new ViewSubCol(X, j));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = mean(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the median of the provided vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static double median(AbstractVector V) throws RuntimeException {
    /*
     * The current algorithm runs in O(n log n), however using a selection algorithm could solve the problem in O(n).
     * See ICS 161: Design and Analysis of Algorithms, Lecture notes for January 30, 1996:
     * http://www.ics.uci.edu/~eppstein/161/960130.html
     */
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    AbstractVector sort = sort(V);

    double median;
    if (V.n_elem % 2 == 0) {
      int middle = V.n_elem / 2;
      median = (sort._data[middle - 1] + sort._data[middle]) / 2;
    } else {
      median = sort._data[V.n_elem / 2];
    }

    return median;
  }

  /**
   * Returns the median for each column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T median(Class<T> return_type, Mat X) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within median(Class<T>, Mat, int).
     */
    
    return median(return_type, X, 0);
  }

  /**
   * Returns the median for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T median(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException, InstantiationException, IllegalAccessException {
    T result = return_type.newInstance();

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }
        
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          /*
           * Creates a deep copy of each column, since sorting of shallow sub views is not yet implemented.
           */
          result._data[j] = median(X.col(j));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }
        
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          /*
           * Creates a deep copy of each row, since sorting of shallow sub views is not yet implemented.
           */
          result._data[i] = median(X.row(i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the standard deviation of the provided vector with normalisation by {@code V.n_elem -1}.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double stddev(AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within stddev(AbstractVector, int).
     */
    
    return stddev(V, 0);
  }

  /**
   * Returns the standard deviation of the provided vector with normalisation by {@code V.n_elem -1} ({@code norm_type}
   * = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param V The vector
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double stddev(AbstractVector V, int norm_type) throws RuntimeException {
    /*
     * The parameter "V" is validated within var(AbstractVector, int).
     */
    
    return Math.sqrt(var(V, norm_type));
  }

  /**
   * Returns the standard deviation for each column of the provided matrix with normalisation by {@code V.n_elem -1}.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T stddev(Class<T> return_type, Mat X) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within stddev(Class<T>, Mat, int).
     */
    
    return stddev(return_type, X, 0);
  }

  /**
   * Returns the standard deviation for each column of the provided matrix with normalisation by {@code V.n_elem -1} (
   * {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T stddev(Class<T> return_type, Mat X, int norm_type) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within stddev(Class<T>, Mat, int, int).
     */
    
    return stddev(return_type, X, norm_type, 0);
  }

  /**
   * Returns the standard deviation for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix
   * with normalisation by {@code V.n_elem -1} ( {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T stddev(Class<T> return_type, Mat X, int norm_type, int dim) throws RuntimeException, IllegalArgumentException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within var(Class<T>, Mat, int, int).
     */
    
    T result = var(return_type, X, norm_type, dim);
    /*
     * This will calculates the square root of each value in-place.
     */
    sqrt(result, result);
    return result;
  }

  /**
   * Returns the variance of the provided vector with normalisation by {@code V.n_elem -1}.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double var(AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within var(AbstractVector, int).
     */
    
    return var(V, 0);
  }

  /**
   * Returns the variance of the provided vector with normalisation by {@code V.n_elem -1} ({@code norm_type} = 0) or
   * {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param V The vector
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double var(AbstractVector V, int norm_type) throws RuntimeException {
    /*
     * The parameter "V" is validated within mean(AbstractVector).
     */
    
    double mean = mean(V);

    double variance = 0;
    for (int n = 0; n < V.n_elem; n++) {
      variance += Math.pow(V._data[n] - mean, 2);
    }

    switch (norm_type) {
      case 0:
        variance /= (V.n_elem - 1);
        break;
      case 1:
        variance /= V.n_elem;
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return variance;
  }

  protected static double var(AbstractView V, int norm_type) throws RuntimeException {
    double mean = mean(V);
    double variance = 0;

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      variance += Math.pow(V._data[V.iteratorNext()] - mean, 2);
    }

    return (norm_type == 0 ? variance / (V.n_elem - 1) : variance / V.n_elem);
  }

  /**
   * Returns the variance for each column of the provided matrix with normalisation by {@code V.n_elem -1}.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T var(Class<T> return_type, Mat X) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within var(Class<T>, Mat, int).
     */
    
    return var(return_type, X, 0);
  }

  /**
   * Returns the variance for each column of the provided matrix with normalisation by {@code V.n_elem -1} (
   * {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T var(Class<T> return_type, Mat X, int norm_type) throws RuntimeException, InstantiationException, IllegalAccessException {
    /*
     * The parameter "X" is validated within var(Class<T>, Mat, int, int).
     */
    
    return var(return_type, X, norm_type, 0);
  }

  /**
   * Returns the variance for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix
   * with normalisation by {@code V.n_elem -1} ( {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T var(Class<T> return_type, Mat X, int norm_type, int dim) throws RuntimeException, IllegalArgumentException, InstantiationException, IllegalAccessException {
    T result = return_type.newInstance();

    switch (norm_type) {
      case 0:
      case 1:
        switch (dim) {
          case 0:
            if (X.n_rows < 1) {
              throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
            }

            result.set_size(X.n_cols);

            for (int j = 0; j < X.n_cols; j++) {
              result._data[j] = var(new ViewSubCol(X, j), norm_type);
            }
            break;
          case 1:
            if (X.n_cols < 1) {
              throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
            }

            result.set_size(X.n_rows);

            for (int i = 0; i < X.n_rows; i++) {
              result._data[i] = var(new ViewSubRow(X, i), norm_type);
            }
            break;
          default:
            throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
        }
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns true if all elements of the provided vector are non-zero.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided vector must have at least one element.
   */
  public static boolean all(AbstractVector V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided vector must have at least one element.");
    }

    for (int n = 0; n < V.n_elem; n++) {
      if (V._data[n] == 0) {
        return false;
      }
    }

    return true;
  }

  protected static boolean all(AbstractView V) throws RuntimeException {
    V.iteratorReset();
    while (V.iteratorHasNext()) {
      if (V._data[V.iteratorNext()] == 0) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns a 1 for each column of the provided matrix where all elements are non-zero and a 0 otherwise.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided matrix must have at least one row.
   */
  public static Mat all(Mat X) throws RuntimeException {
    // X is validated within all
    return all(X, 0);
  }

  /**
   * Returns a 1 for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix where all elements
   * are non-zero and a 0 otherwise.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided matrix must have at least one row.
   * @throws RuntimeException The provided matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension must either be 0 or 1.
   */
  public static Mat all(Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    Mat result = null;

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided matrix must have at least one row.");
        }

        // All uninitialised matrices are already set to zero
        result = new Mat(1, X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          if (all(new ViewSubCol(X, j))) {
            result._data[j] = 1;
          }
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided matrix must have at least one column.");
        }

        // All uninitialised matrices are already set to zero
        result = new Mat(X.n_rows, 1);

        for (int i = 0; i < X.n_rows; i++) {
          if (all(new ViewSubRow(X, i))) {
            result._data[i] = 1;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns true if any element of the provided vector is non-zero.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided vector must have at least one element.
   */
  public static boolean any(AbstractVector V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided vector must have at least one element.");
    }

    for (int n = 0; n < V.n_elem; n++) {
      if (V._data[n] != 0) {
        return true;
      }
    }

    return false;
  }

  protected static boolean any(AbstractView V) throws RuntimeException {
    V.iteratorReset();
    while (V.iteratorHasNext()) {
      if (V._data[V.iteratorNext()] != 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns a 1 for each column of the provided matrix where any element is non-zero and a 0 otherwise.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided matrix must have at least one row.
   */
  public static Mat any(Mat X) throws RuntimeException {
    // X is validated within any
    return any(X, 0);

  }

  /**
   * Returns a 1 for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix where any element
   * is non-zero and a 0 otherwise.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided matrix must have at least one row.
   * @throws RuntimeException The provided matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension must either be 0 or 1.
   */
  public static Mat any(Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    Mat result = null;

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided matrix must have at least one row.");
        }

        // All uninitialised matrices are already set to zero
        result = new Mat(1, X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          if (any(new ViewSubCol(X, j))) {
            result._data[j] = 1;
          }
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided matrix must have at least one column.");
        }

        // All uninitialised matrices are already set to zero
        result = new Mat(X.n_rows, 1);

        for (int i = 0; i < X.n_rows; i++) {
          if (any(new ViewSubRow(X, i))) {
            result._data[i] = 1;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension must either be 0 or 1.");
    }

    return result;
  }

  protected static void conv(AbstractMat result, AbstractMat A, AbstractMat B) {
    for (int n = 0; n < result.n_elem; n++) {
      int min = Math.max(0, n - B.n_elem + 1);
      int max = Math.min(A.n_elem, n + 1);

      for (int nn = min; nn < max; nn++)
      {
        result._data[n] += A._data[nn] * B._data[n - nn];
      }
    }
  }

  /**
   * Returns the convolution between the first and second provided vector as a column vector.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The second provided matrix must be equivalent in shape to a vector.
   */
  public static Col conv(Col A, AbstractVector B) throws RuntimeException {
    if (!B.is_vec()) {
      throw new RuntimeException("The second provided matrix must be equivalent in shape to a vector.");
    }

    Col result = new Col(A.n_elem + B.n_elem - 1);
    conv(result, A, B);
    return result;
  }

  /**
   * Returns the convolution between the first and second provided vector as a row vector.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The second provided matrix must be equivalent in shape to a vector.
   */
  public static Row conv(Row A, AbstractVector B) throws RuntimeException {
    if (!B.is_vec()) {
      throw new RuntimeException("The second provided matrix must be equivalent in shape to a vector.");
    }

    Row result = new Row(A.n_elem + B.n_elem - 1);
    conv(result, A, B);
    return result;
  }

  /**
   * Returns the convolution between the first and second provided vector in tsame vector shape as the first provided
   * one.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The second provided matrix must be equivalent in shape to a vector.
   */
  public static Mat conv(Mat A, AbstractVector B) throws RuntimeException {
    if (!A.is_vec()) {
      throw new RuntimeException("The first provided matrix must be equivalent in shape to a vector.");
    }

    if (!B.is_vec()) {
      throw new RuntimeException("The second provided matrix must be equivalent in shape to a vector.");
    }

    Mat result;
    if (A.is_colvec()) {
      result = new Mat(A.n_elem + B.n_elem - 1, 1);
    } else {
      result = new Mat(1, A.n_elem + B.n_elem - 1);
    }

    conv(result, A, B);
    return result;
  }

  public static double cor(AbstractVector X, AbstractVector Y) {
    // X and Y are validated within cor
    return cor(X, Y, 0);
  }

  public static double cor(AbstractVector X, AbstractVector Y, int norm_type) {
    // X, Y and norm_type are validated within cov
    return cov(X, Y, norm_type) / Math.sqrt(cov(X, Y, norm_type) * cov(X, Y, norm_type));
  }

  public static double cor(AbstractVector X) {
    // X is validated within cor
    return cor(X, 0);
  }

  public static double cor(AbstractVector X, int norm_type) {
    // X and norm_type are validated within cor
    return cor(X, X, norm_type);
  }

  public static Mat cor(Mat X, Mat Y) {
    // X and Y are validated within cor
    return cor(X, Y, 0);
  }

  public static Mat cor(Mat X, Mat Y, int norm_type) {
    // X, Y and norm_type are validated within cov
    Mat covariance = cov(X, Y, norm_type);

    Mat selfCovariance;
    if (norm_type != 0) {
      selfCovariance = cov(X, Y, 0);
    } else {
      selfCovariance = covariance;
    }

    Mat result = new Mat(X.n_cols, X.n_cols);

    int n = 0;
    for (int j = 0; j < X.n_cols; j++) {
      for (int jj = 0; jj < X.n_cols; jj++) {
        result._data[n++] = covariance.at(j, jj) / Math.sqrt(selfCovariance.at(j, j) * selfCovariance.at(jj, jj));
      }
    }

    return result;
  }

  public static Mat cor(Mat X) {
    // X is validated within cor
    return cor(X, 0);
  }

  public static Mat cor(Mat X, int norm_type) {
    // X and norm_type are validated within cor
    return cor(X, X, norm_type);
  }

  public static double cov(AbstractVector X, AbstractVector Y) {
    // X and Y are validated within cov
    return cov(X, Y, 0);
  }

  public static double cov(AbstractVector X, AbstractVector Y, int norm_type) {
    if (X.n_elem != Y.n_elem) {
      throw new RuntimeException("Both provided vectors must have the same number of elements.");
    }

    // X is validated within mean
    double meanX = mean(X);
    // Y is validated within mean
    double meanY = mean(Y);

    double covariance = 0;
    for (int n = 0; n < X.n_elem; n++) {
      covariance += (X._data[n] - meanX) * (X._data[n] - meanY);
    }

    switch (norm_type) {
      case 0:
        covariance /= (X.n_elem - 1);
        break;
      case 1:
        covariance /= X.n_elem;
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation must either be 0 or 1.");
    }

    return covariance;
  }

  public static double cov(AbstractVector X) {
    // X is validated within cov
    return cov(X, 0);
  }

  public static double cov(AbstractVector X, int norm_type) {
    // X and norm_type are validated within cov
    return cov(X, X, norm_type);
  }

  public static Mat cov(Mat X, Mat Y) {
    // X and Y are validated within cov
    return cov(X, Y, 0);
  }

  public static Mat cov(Mat X, Mat Y, int norm_type) {
    // X and Y are validated within cov
    if (X.n_rows != Y.n_rows || X.n_cols != Y.n_cols) {
      throw new RuntimeException("Both provided matrices must have the same size.");
    }

    Mat result = new Mat(X.n_cols, X.n_cols);

    int n = 0;
    switch (norm_type) {
      case 0:
        for (int j = 0; j < X.n_cols; j++) {
          int columnIndexX = j * X.n_rows;

          double meanX = 0;
          for (int i = 0; i < X.n_rows; i++) {
            meanX += X._data[i + columnIndexX];
          }
          meanX /= X.n_rows;

          for (int jj = 0; jj < Y.n_cols; jj++) {
            int columnIndexY = jj * Y.n_rows;

            double meanY = 0;
            for (int i = 0; i < X.n_rows; i++) {
              meanY += Y._data[i + columnIndexY];
            }
            meanY /= Y.n_rows;

            double covariance = 0;
            for (int i = 0; i < X.n_rows; i++) {
              covariance += (X._data[i + columnIndexX] - meanX) * (X._data[i + columnIndexY] - meanY);
            }
            result._data[n++] = covariance / (X.n_elem - 1);
          }
        }
        break;
      case 1:
        for (int j = 0; j < X.n_cols; j++) {
          int columnIndexX = j * X.n_rows;

          double meanX = 0;
          for (int i = 0; i < X.n_rows; i++) {
            meanX += X._data[i + columnIndexX];
          }
          meanX /= X.n_rows;

          for (int jj = 0; jj < Y.n_cols; jj++) {
            int columnIndexY = jj * Y.n_rows;

            double meanY = 0;
            for (int i = 0; i < X.n_rows; i++) {
              meanY += Y._data[i + columnIndexY];
            }
            meanY /= Y.n_rows;

            double covariance = 0;
            for (int i = 0; i < X.n_rows; i++) {
              covariance += (X._data[i + columnIndexX] - meanX) * (X._data[i + columnIndexY] - meanY);
            }
            result._data[n++] = covariance / X.n_elem;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation must either be 0 or 1.");
    }

    return result;
  }

  public static Mat cov(Mat X) {
    // X is validated within cov
    return cov(X, 0);
  }

  public static Mat cov(Mat X, int norm_type) {
    // X and norm_type are validated within cov
    return cov(X, X, norm_type);
  }

  protected static void cross(AbstractMat result, AbstractMat A, AbstractMat B) {
    result._data[0] = A._data[1] * B._data[2] - A._data[2] * B._data[1];
    result._data[1] = A._data[2] * B._data[0] - A._data[0] * B._data[2];
    result._data[2] = A._data[0] * B._data[1] - A._data[1] * B._data[0];
  }

  public static Col cross(Col A, AbstractMat B) {
    if (A.n_elem != 3) {
      throw new RuntimeException("The first provided matrix must be a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided matrix must be a 3-dimensional vector.");
    }

    Col result = new Col(3);
    conv(result, A, B);
    return result;
  }

  public static Row cross(Row A, AbstractMat B) {
    if (A.n_elem != 3) {
      throw new RuntimeException("The first provided matrix must be a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided matrix must be a 3-dimensional vector.");
    }

    Row result = new Row(3);
    conv(result, A, B);
    return result;
  }

  public static Mat cross(Mat A, AbstractMat B) {
    if (!A.is_vec() || A.n_elem != 3) {
      throw new RuntimeException("The first provided matrix must be a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided matrix must be a 3-dimensional vector.");
    }

    Mat result;
    if (A.is_colvec()) {
      result = new Mat(3, 1);
    } else {
      result = new Mat(1, 3);
    }

    cross(result, A, B);
    return result;
  }

  protected static void cumsum(AbstractVector result, AbstractVector V) {
    result._data[0] = V._data[0];
    for (int n = 1; n < result.n_elem; n++) {
      result._data[n] = result._data[n - 1] + V._data[n];
    }
  }

  public static Col cumsum(Col V) {
    Col result = new Col(V.n_elem);
    cumsum(result, V);
    return result;
  }

  public static Row cumsum(Row V) {
    Row result = new Row(V.n_elem);
    cumsum(result, V);
    return result;
  }

  protected static Mat cumsum(AbstractView V) {
    Mat result = new Mat();
    result.copy_size(V);

    V.iteratorReset();
    result._data[0] = V._data[V.iteratorNext()];
    for (int n = 1; n < result.n_elem; n++) {
      result._data[n] = result._data[n - 1] + V._data[V.iteratorNext()];
    }

    return result;
  }

  public static Mat cumsum(Mat X) {
    // X is validated within cumsum
    return cumsum(X, 0);
  }

  public static Mat cumsum(Mat X, int dim) {
    Mat result = new Mat(X.n_rows, X.n_cols);

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided matrix must have at least one row.");
        }

        for (int j = 0; j < X.n_cols; j++) {
          ViewSubCol subView = new ViewSubCol(X, j);
          subView.inPlaceEqual(cumsum(subView));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided matrix must have at least one column.");
        }

        for (int i = 0; i < X.n_rows; i++) {
          ViewSubRow subView = new ViewSubRow(X, i);
          subView.inPlaceEqual(cumsum(subView));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension must either be 0 or 1.");
    }

    return result;
  }

  public static Mat diagmat(AbstractVector X) {
    Mat result = new Mat(X.n_elem, X.n_elem);
    new ViewDiag(result, 0).inPlaceEqual(X);
    return result;
  }

  public static Mat diagmat(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    new ViewDiag(result, 0).inPlaceEqual(new ViewDiag(X, 0));
    return result;
  }

  public static Col find(AbstractMat X) {
    return find(X, 0);
  }

  public static Col find(AbstractMat X, int k) {
    return find(X, k, "first");
  }

  public static Col find(AbstractMat X, int k, String s) {
    // matrix.isElementOutOfBoundsDetection(k);

    Mat result = new Mat(X.n_elem, 1);

    int limit;
    if (k > 0) {
      limit = k;
    } else {
      limit = X.n_elem;
    }

    int index = 0;
    switch (s) {
      case "first":
        for (int n = 0; n < X.n_elem && index < limit; n++) {
          if (X._data[n] != 0) {
            result._data[index++] = n;
          }
        }
        break;
      case "last":
        for (int n = X.n_elem - 1; n >= 0 && index < limit; n--) {
          if (X._data[n] != 0) {
            result._data[index++] = n;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The parameter s needs to be either 'first' or 'last', but was " + s);
    }

    // Saves current values of the elements since length <= n_elem
    result.reshape(index, 1);

    if (s.equals("last")) {
      for (int n = 0; n < result.n_elem / 2; n++) {
        // swap? Or better: protected void revert(AbstractVector result, AbstractVector X)
        double temp = result._data[n];
        result._data[n] = result._data[result.n_elem - (n + 1)];
        result._data[result.n_elem - (n + 1)] = temp;
      }
    }

    return result;
  }

  public static Mat fliplr(Mat X) {
    // copy + swap is quite inefficient
    Mat result = new Mat(X.);

    for (int j = 0; j < result.n_cols / 2; j++) {
      
      result.swap_cols(j, result.n_cols - (j + 1));
    }

    return result;
  }

  public static Mat flipud(Mat X) {
    // copy + swap is quite inefficient
    Mat result = new Mat(matrix);

    for (int i = 0; i < result.n_rows / 2; i++) {
      result.swap_rows(i, result.n_rows - (i + 1));
    }

    return result;
  }

  protected static void hist(AbstractVector result, AbstractVector V) {

  }

  public static Col hist(Col V) {
    return hist(V, 10);
  }

  public static Col hist(Col V, int n_bins) {

  }

  public static Row hist(Row V) {
    return hist(V, 10);
  }

  public static Row hist(Row V, int n_bins) {

  }

  protected static Mat hist(AbstractView X, int n_bins) {

  }

  public static Mat hist(Mat X) {
    return hist(X, 10);
  }

  public static Mat hist(Mat X, int n_bins) {
    return hist(X, n_bins, 0);
  }

  public static Mat hist(Mat X, int n_bins, int dim) {

  }

  protected static void hist(AbstractVector result, AbstractVector V, AbstractMat centers) {

  }

  public static Col hist(Col V, AbstractMat centers) {

  }

  public static Row hist(Row V, AbstractMat centers) {

  }

  protected static Mat hist(AbstractView X, AbstractMat centers) {

  }

  public static Mat hist(Mat X, AbstractMat centers) {
    return hist(X, centers, 0);
  }

  public static Mat hist(Mat X, AbstractMat centers, int dim) {

  }

  protected static void histc(AbstractVector result, AbstractVector V, AbstractMat edges) {

  }

  public static Col histc(Col V, AbstractMat edges) {

  }

  public static Row histc(Row V, AbstractMat edges) {

  }

  protected static Mat histc(AbstractView X, AbstractMat edges) {

  }

  public static Mat histc(Mat X, AbstractMat edges) {
    return hist(X, edges, 0);
  }

  public static Mat histc(Mat X, AbstractMat edges, int dim) {

  }

  public static void inplace_trans(Mat X) {

  }

  public static Col join_rows(Col A, AbstractMat B) {

  }

  public static Col join_rows(AbstractMat A, AbstractMat B) {

  }

  public static Mat join_rows(AbstractMat A, AbstractMat B) {

  }

  public static Mat join_horiz(AbstractMat A, AbstractMat B) {

  }

  public static Mat join_cols(AbstractMat A, AbstractMat B) {

  }

  public static Mat join_vert(AbstractMat A, AbstractMat B) {

  }

  protected static Col sort(AbstractVector V) {

  }

  public static Col sort(Col V) {

  }

  public static Col sort(Row V) {

  }

  public static Col svd(Mat X) {
    if (X.empty()) {
      throw new RuntimeException("The provided matrix must have at least one element.");
    }

  }

}