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
import java.util.Comparator;

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
  public static Mat eye(final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
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
  public static <T extends AbstractMat> T linspace(final Class<T> return_type, final int start, final int end) throws RuntimeException {
    /*
     * The parameters "start" and "end" are validated within linspace(Class<T>, int, int, int).
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
  public static <T extends AbstractMat> T linspace(final Class<T> return_type, final int start, final int end, final int N) throws RuntimeException, NegativeArraySizeException {
    /*
     * The parameter "N" is validated within set_size(int).
     */
    if (end < start) {
      throw new RuntimeException("The specified value to start with (" + start + ") must be less than or equal the specified value to end with (" + end + ").");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
  public static <T extends AbstractVector> T ones(Class<T> return_type, int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
  public static Mat ones(int n_rows, int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
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
  public static <T extends AbstractVector> T randi(Class<T> return_type, int n_elem) throws NegativeArraySizeException {
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
  public static <T extends AbstractVector> T randi(Class<T> return_type, int n_elem, DistrParam distr_param) throws NegativeArraySizeException, RuntimeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    if (distr_param._b < distr_param._a) {
      throw new RuntimeException("The first end point (" + distr_param._a + ") of the interval must be less than or equal the last end point (" + distr_param._a + ").");
    }

    if (distr_param._b - distr_param._a > Integer.MAX_VALUE - 1) {
      throw new RuntimeException("The difference between the first end point (" + distr_param._a + ") and the last end point (" + distr_param._b + ") can be at most Integer.MAX_VALUE - 1.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
  public static Mat randi(int n_rows, int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within randi(Class<T>, int, int, DistrParam).
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
  public static Mat randi(int n_rows, int n_cols, DistrParam distr_param) throws NegativeArraySizeException, RuntimeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int).
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
  public static <T extends AbstractVector> T randu(Class<T> return_type, int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
  public static Mat randu(int n_rows, int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
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
  public static <T extends AbstractVector> T randn(Class<T> return_type, int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
  public static Mat randn(int n_rows, int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int).
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
  public static Mat repmat(AbstractMat A, int num_copies_per_row, int num_copies_per_col) throws NegativeArraySizeException {
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
        new ViewSubMat(result, i * A.n_rows, A.n_rows, 0, A.n_cols).inPlace(Op.EQUAL, A);
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
  public static Mat toeplitz(AbstractMat A) throws RuntimeException {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, A.n_elem);

    new ViewDiag(result, 0).fill(A._data[0]);
    for (int n = 1; n < A.n_elem; n++) {
      double value = A._data[n];

      new ViewDiag(result, n).fill(value);
      new ViewDiag(result, -n).fill(value);
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
   * @throws RuntimeException The second provided ({@code B.n_rows}, {@code B.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a vector.
   * @throws RuntimeException The second provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in
   *           shape to a vector.
   */
  public static Mat toeplitz(AbstractMat A, AbstractMat B) throws RuntimeException {
    if (A.is_empty()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (B.is_empty()) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    if (!B.is_vec()) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, B.n_elem);

    new ViewDiag(result, 0).fill(A._data[0]);
    for (int n = 1; n < A.n_elem; n++) {
      new ViewDiag(result, -n).fill(A._data[n]);
    }

    for (int n = 1; n < B.n_elem; n++) {
      new ViewDiag(result, n).fill(B._data[n]);
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
  public static Mat circ_toeplitz(AbstractMat A) throws RuntimeException {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, A.n_elem);

    new ViewDiag(result, 0).fill(A._data[0]);
    for (int n = 1; n < A.n_elem; n++) {
      double value = A._data[n];

      new ViewDiag(result, A.n_elem - n).fill(value);
      new ViewDiag(result, -n).fill(value);
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
  public static <T extends AbstractVector> T zeros(Class<T> return_type, int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    /*
     * All uninitialised matrices are already equal to a zero matrix.
     */
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
  public static Mat zeros(int n_rows, int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int).
     * All uninitialised matrices are already equal to a zero matrix.
     */

    return new Mat(n_rows, n_cols);
  }

  protected static void abs(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.abs(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The column vector
   */
  public static Col abs(Col X) {
    Col result = new Col(X.n_elem);
    abs(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The row vector
   */
  public static Row abs(Row X) {
    Row result = new Row(X.n_elem);
    abs(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat abs(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    abs(result._data, X._data);
    return result;
  }

  protected static void eps(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.ulp(X[n]);
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
    eps(result._data, X._data);
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
    eps(result._data, X._data);
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
    eps(result._data, X._data);
    return result;
  }

  protected static void exp(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.exp(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col exp(Col A) {
    Col result = new Col(A.n_elem);
    exp(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row exp(Row A) {
    Row result = new Row(A.n_elem);
    exp(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat exp(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp(result._data, A._data);
    return result;
  }

  protected static void exp2(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.pow(2, A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col exp2(Col A) {
    Col result = new Col(A.n_elem);
    exp2(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row exp2(Row A) {
    Row result = new Row(A.n_elem);
    exp2(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat exp2(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp2(result._data, A._data);
    return result;
  }

  protected static void exp10(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.pow(10, A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col exp10(Col A) {
    Col result = new Col(A.n_elem);
    exp10(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row exp10(Row A) {
    Row result = new Row(A.n_elem);
    exp10(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat exp10(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp10(result._data, A._data);
    return result;
  }

  protected static void trunc_exp(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      double exp = Math.exp(A[n]);

      if (!Double.isInfinite(exp)) {
        result[n] = exp;
      } else {
        result[n] = Double.MAX_VALUE;
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
    trunc_exp(result._data, A._data);
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
    trunc_exp(result._data, A._data);
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
    trunc_exp(result._data, A._data);
    return result;
  }

  protected static void log(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.log(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col log(Col A) {
    Col result = new Col(A.n_elem);
    log(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row log(Row A) {
    Row result = new Row(A.n_elem);
    log(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat log(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log(result._data, A._data);
    return result;
  }

  protected static void log2(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.log(A[n]) / Math.log(2);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col log2(Col A) {
    Col result = new Col(A.n_elem);
    log2(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row log2(Row A) {
    Row result = new Row(A.n_elem);
    log2(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat log2(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log2(result._data, A._data);
    return result;
  }

  protected static void log10(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.log10(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col log10(Col A) {
    Col result = new Col(A.n_elem);
    log10(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row log10(Row A) {
    Row result = new Row(A.n_elem);
    log10(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat log10(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log10(result._data, A._data);
    return result;
  }

  protected static void trunc_log(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      double value = A[n];

      if (value <= 0) {
        value = Double.MIN_NORMAL;
      } else if (Double.isInfinite(value)) {
        value = Double.MAX_VALUE;
      }

      result[n] = Math.log(value);
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
    trunc_log(result._data, A._data);
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
    trunc_log(result._data, A._data);
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
    trunc_log(result._data, A._data);
    return result;
  }

  protected static void pow(double[] result, double[] A, int p) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.pow(A[n], p);
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
    pow(result._data, A._data, p);
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
    pow(result._data, A._data, p);
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
    pow(result._data, A._data, p);
    return result;
  }

  protected static void sqrt(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.sqrt(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed square root of the provided one.
   * 
   * @param A The column vector
   */
  public static Col sqrt(Col A) {
    Col result = new Col(A.n_elem);
    sqrt(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed square root of the provided one.
   * 
   * @param A The row vector
   */
  public static Row sqrt(Row A) {
    Row result = new Row(A.n_elem);
    sqrt(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed square root of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat sqrt(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    sqrt(result._data, A._data);
    return result;
  }

  protected static void square(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.pow(A[n], 2);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed squared values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col square(Col A) {
    Col result = new Col(A.n_elem);
    square(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed squared values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row square(Row A) {
    Row result = new Row(A.n_elem);
    square(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed squared values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat square(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    square(result._data, A._data);
    return result;
  }

  protected static void floor(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.floor(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed floored values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col floor(Col A) {
    Col result = new Col(A.n_elem);
    floor(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed floored values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row floor(Row A) {
    Row result = new Row(A.n_elem);
    floor(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed floored values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat floor(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    floor(result._data, A._data);
    return result;
  }

  protected static void ceil(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.ceil(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col ceil(Col A) {
    Col result = new Col(A.n_elem);
    ceil(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row ceil(Row A) {
    Row result = new Row(A.n_elem);
    ceil(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat ceil(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    ceil(result._data, A._data);
    return result;
  }

  protected static void round(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.round(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The column vector
   */
  public static Col round(Col A) {
    Col result = new Col(A.n_elem);
    round(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The row vector
   */
  public static Row round(Row A) {
    Row result = new Row(A.n_elem);
    round(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The matrix
   */
  public static Mat round(Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    round(result._data, A._data);
    return result;
  }

  protected static void sign(double[] result, double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.signum(A[n]);
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
    sign(result._data, A._data);
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
    sign(result._data, A._data);
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
    sign(result._data, A._data);
    return result;
  }

  protected static void sin(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.sin(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed sine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col sin(Col X) {
    Col result = new Col(X.n_elem);
    sin(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed sine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row sin(Row X) {
    Row result = new Row(X.n_elem);
    sin(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed sine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat sin(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    sin(result._data, X._data);
    return result;
  }

  protected static void asin(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.asin(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col asin(Col X) {
    Col result = new Col(X.n_elem);
    asin(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row asin(Row X) {
    Row result = new Row(X.n_elem);
    asin(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat asin(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    asin(result._data, X._data);
    return result;
  }

  protected static void sinh(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.sinh(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col sinh(Col X) {
    Col result = new Col(X.n_elem);
    sinh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row sinh(Row X) {
    Row result = new Row(X.n_elem);
    sinh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat sinh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    sinh(result._data, X._data);
    return result;
  }

  protected static void asinh(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      double value = X[n];

      if (Double.isInfinite(value)) {
        result[n] = value;
      } else {
        result[n] = Math.log(value + Math.sqrt(Math.pow(value, 2) + 1));
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
    asinh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic sine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row asinh(Row X) {
    Row result = new Row(X.n_elem);
    asinh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic sine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat asinh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    asinh(result._data, X._data);
    return result;
  }

  protected static void cos(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.cos(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed cosine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col cos(Col X) {
    Col result = new Col(X.n_elem);
    cos(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed cosine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row cos(Row X) {
    Row result = new Row(X.n_elem);
    cos(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed cosine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat cos(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    cos(result._data, X._data);
    return result;
  }

  protected static void acos(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.acos(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col acos(Col X) {
    Col result = new Col(X.n_elem);
    acos(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row acos(Row X) {
    Row result = new Row(X.n_elem);
    acos(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat acos(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    acos(result._data, X._data);
    return result;
  }

  protected static void cosh(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.cosh(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col cosh(Col X) {
    Col result = new Col(X.n_elem);
    cosh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row cosh(Row X) {
    Row result = new Row(X.n_elem);
    cosh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat cosh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    cosh(result._data, X._data);
    return result;
  }

  protected static void acosh(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      double value = X[n];
      result[n] = Math.log(value + Math.sqrt(Math.pow(value, 2) - 1));
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The column vector
   */
  public static Col acosh(Col X) {
    Col result = new Col(X.n_elem);
    acosh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The row vector
   */
  public static Row acosh(Row X) {
    Row result = new Row(X.n_elem);
    acosh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat acosh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    acosh(result._data, X._data);
    return result;
  }

  protected static void tan(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.tan(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed tangent of the provided one.
   * 
   * @param X The column vector
   */
  public static Col tan(Col X) {
    Col result = new Col(X.n_elem);
    tan(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed tangent of the provided one.
   * 
   * @param X The row vector
   */
  public static Row tan(Row X) {
    Row result = new Row(X.n_elem);
    tan(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed tangent of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat tan(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    tan(result._data, X._data);
    return result;
  }

  protected static void atan(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.atan(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The column vector
   */
  public static Col atan(Col X) {
    Col result = new Col(X.n_elem);
    atan(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The row vector
   */
  public static Row atan(Row X) {
    Row result = new Row(X.n_elem);
    atan(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat atan(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    atan(result._data, X._data);
    return result;
  }

  protected static void tanh(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.tanh(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The column vector
   */
  public static Col tanh(Col X) {
    Col result = new Col(X.n_elem);
    tanh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The row vector
   */
  public static Row tanh(Row X) {
    Row result = new Row(X.n_elem);
    tanh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat tanh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    tanh(result._data, X._data);
    return result;
  }

  protected static void atanh(double[] result, double[] X) {
    for (int n = 0; n < X.length; n++) {
      double value = X[n];
      result[n] = 0.5 * Math.log((1 + value) / (1 - value));
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The column vector
   */
  public static Col atanh(Col X) {
    Col result = new Col(X.n_elem);
    atanh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The row vector
   */
  public static Row atanh(Row X) {
    Row result = new Row(X.n_elem);
    atanh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The matrix
   */
  public static Mat atanh(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    atanh(result._data, X._data);
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
    Col singularValues = new Col();
    svd(singularValues, A);
    return singularValues._data[0] / singularValues._data[singularValues.n_elem - 1];
  }

  /**
   * Returns the determinant of the provided matrix.
   * 
   * @param A The matrix
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   * @throws RuntimeException The factorisation could not be completed. The provided matrix appears to be singular.
   */
  public static double det(Mat A) throws RuntimeException {
    if (A.empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    double[] temp = Arrays.copyOf(A._data, A.n_elem);
    int[] pivotIndices = new int[Math.min(A.n_rows, A.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(A.n_rows, A.n_cols, temp, A.n_rows, pivotIndices, info);
    if (info.val != 0) {
      throw new RuntimeException("The factorisation could not be completed. The provided matrix appears to be singular.");
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
     * The parameters "A" and "B" are validated within dot(AbstractMat, AbstractMat).
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
   * @throws RuntimeException The factorisation could not be completed. The provided matrix appears to be singular.
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
      throw new RuntimeException("The factorisation could not be completed. The provided matrix appears to be singular.");
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
   * Returns the specified norm of the provided vector.
   * 
   * @param X The vector
   * @param p The norm
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-vector must have at least one element.
   * @throws IllegalArgumentException The norm indicator ({@code p}) must be strict greater than 0.
   */
  public static double norm(AbstractVector X, int p) throws RuntimeException, IllegalArgumentException {
    if (X.empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-vector must have at least one element.");
    }

    double norm = 0;

    if (p < 0) {
      throw new IllegalArgumentException("The norm indicator (" + p + ") must be strict greater than 0.");
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
   * @throws IllegalArgumentException For matrices in vector shape, the norm indicator ({@code p}) must be strict
   *           greater than 0.
   * @throws IllegalArgumentException For matrices in non-vector shape, the norm indicator ({@code p}) must be one of 1
   *           or 2.
   */
  public static double norm(Mat X, int p) throws RuntimeException, IllegalArgumentException {
    if (X.empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    double norm = 0;

    if (X.is_vec()) {
      if (p < 0) {
        throw new IllegalArgumentException("For matrices in vector shape, the norm indicator (" + p + ") must be strict greater than 0.");
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
        throw new IllegalArgumentException("For matrices in non-vector shape, the norm indicator (" + p + ") must be one of 1 or 2.");
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
          Col singularValues = new Col();
          svd(singularValues, X);
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
   * All singular values smaller than {@code Math.max(X.n_rows, X.n_cols) * Math.ulp(norm(X, 2))} are treated as zero.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static int rank(Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within rank(AbstractMat, double).
     */

    return rank(X, Math.max(X.n_rows, X.n_cols) * Math.ulp(norm(X, 2)));
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
    Col singularValues = new Col();
    svd(singularValues, X);

    int rank = 0;
    for (int n = 0; n < singularValues.n_elem; n++) {
      if (singularValues._data[n] > tolerance) {
        rank++;
      }
    }

    return rank;
  }

  /**
   * Returns the trace of the provided square matrix.
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
  public static Col diagvec(Mat A) {
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
  public static Col diagvec(Mat A, int k) throws IndexOutOfBoundsException {
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
  public static <T extends AbstractVector> T min(Class<T> return_type, Mat X) throws RuntimeException {
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
  public static <T extends AbstractVector> T min(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
   * @throws RuntimeException Both provided matrices ({@code A.n_rows}, {@code A.n_cols} and {@code B.n_rows},
   *           {@code B.n_cols}) must have the same shape.
   */
  public static Mat min(Mat A, Mat B) throws RuntimeException {
    if (A.n_rows != B.n_rows || A.n_cols != B.n_cols) {
      throw new RuntimeException("Both provided matrices (" + A.n_rows + ", " + A.n_cols + " and " + B.n_rows + ", " + B.n_cols + ") must have the same shape.");
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
  public static <T extends AbstractVector> T max(Class<T> return_type, Mat X) throws RuntimeException {
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
  public static <T extends AbstractVector> T max(Class<T> return_type, Mat X, int dim) throws RuntimeException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
   * @throws RuntimeException Both provided matrices ({@code A.n_rows}, {@code A.n_cols} and {@code B.n_rows},
   *           {@code B.n_cols}) must have the same shape.
   */
  public static Mat max(Mat A, Mat B) throws RuntimeException {
    if (A.n_rows != B.n_rows || A.n_cols != B.n_cols) {
      throw new RuntimeException("Both provided matrices (" + A.n_rows + ", " + A.n_cols + " and " + B.n_rows + ", " + B.n_cols + ") must have the same shape.");
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
  public static <T extends AbstractVector> T prod(Class<T> return_type, Mat X) throws RuntimeException {
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
  public static <T extends AbstractVector> T prod(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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

  protected static double sum(AbstractView V) {
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
  public static <T extends AbstractVector> T sum(Class<T> return_type, Mat X) throws RuntimeException {
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
  public static <T extends AbstractVector> T sum(Class<T> return_type, Mat X, int dim) throws RuntimeException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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

  protected static double mean(AbstractView V) {
    return sum(V) / V.n_elem;
  }

  /**
   * Returns the mean for each column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T mean(Class<T> return_type, Mat X) throws RuntimeException {
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
  public static <T extends AbstractVector> T mean(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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

    double[] temp = Arrays.copyOf(V._data, V.n_elem);
    Arrays.sort(temp);

    double median;
    if (V.n_elem % 2 == 0) {
      int middle = V.n_elem / 2;
      median = (temp[middle - 1] + temp[middle]) / 2;
    } else {
      median = temp[V.n_elem / 2];
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
  public static <T extends AbstractVector> T median(Class<T> return_type, Mat X) throws RuntimeException {
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
  public static <T extends AbstractVector> T median(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
   * Returns the standard deviation of the provided vector normalised by {@code V.n_elem -1}.
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
   * Returns the standard deviation of the provided vector normalised by {@code V.n_elem -1} ({@code norm_type} = 0) or
   * {@code V.n_elem} ({@code norm_type} = 1).
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

  protected static double stddev(AbstractView V, int norm_type) {
    double mean = mean(V);
    double variance = 0;

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      variance += Math.pow(V._data[V.iteratorNext()] - mean, 2);
    }

    return Math.sqrt((norm_type == 0 ? variance / (V.n_elem - 1) : variance / V.n_elem));
  }

  /**
   * Returns the standard deviation for each column of the provided matrix normalised by {@code V.n_elem -1}.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T stddev(Class<T> return_type, Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within stddev(Class<T>, Mat, int).
     */

    return stddev(return_type, X, 0);
  }

  /**
   * Returns the standard deviation for each column of the provided matrix normalised by {@code V.n_elem -1} (
   * {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T stddev(Class<T> return_type, Mat X, int norm_type) throws RuntimeException {
    /*
     * The parameter "X" is validated within stddev(Class<T>, Mat, int, int).
     */

    return stddev(return_type, X, norm_type, 0);
  }

  /**
   * Returns the standard deviation for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix
   * normalised by {@code V.n_elem -1} ( {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
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
  public static <T extends AbstractVector> T stddev(Class<T> return_type, Mat X, int norm_type, int dim) throws RuntimeException, IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
              result._data[j] = stddev(new ViewSubCol(X, j), norm_type);
            }
            break;
          case 1:
            if (X.n_cols < 1) {
              throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
            }

            result.set_size(X.n_rows);

            for (int i = 0; i < X.n_rows; i++) {
              result._data[i] = stddev(new ViewSubRow(X, i), norm_type);
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
   * Returns the variance of the provided vector normalised by {@code V.n_elem -1}.
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
   * Returns the variance of the provided vector normalised by {@code V.n_elem -1} ({@code norm_type} = 0) or
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

  protected static double var(AbstractView V, int norm_type) {
    double mean = mean(V);
    double variance = 0;

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      variance += Math.pow(V._data[V.iteratorNext()] - mean, 2);
    }

    return (norm_type == 0 ? variance / (V.n_elem - 1) : variance / V.n_elem);
  }

  /**
   * Returns the variance for each column of the provided matrix normalised by {@code V.n_elem -1}.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T var(Class<T> return_type, Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within var(Class<T>, Mat, int).
     */

    return var(return_type, X, 0);
  }

  /**
   * Returns the variance for each column of the provided matrix normalised by {@code V.n_elem -1} ( {@code norm_type} =
   * 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T var(Class<T> return_type, Mat X, int norm_type) throws RuntimeException {
    /*
     * The parameter "X" is validated within var(Class<T>, Mat, int, int).
     */

    return var(return_type, X, norm_type, 0);
  }

  /**
   * Returns the variance for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix normalised
   * by {@code V.n_elem -1} ( {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
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
  public static <T extends AbstractVector> T var(Class<T> return_type, Mat X, int norm_type, int dim) throws RuntimeException, IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

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
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static boolean all(AbstractVector V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    for (int n = 0; n < V.n_elem; n++) {
      if (V._data[n] == 0) {
        return false;
      }
    }

    return true;
  }

  protected static boolean all(AbstractView V) {
    V.iteratorReset();
    while (V.iteratorHasNext()) {
      if (V._data[V.iteratorNext()] == 0) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns a 1 for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix where all elements
   * are non-zero and a 0 otherwise.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T all(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        /*
         * All uninitialised matrices are already equal to a zero matrix.
         */
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          if (all(new ViewSubCol(X, j))) {
            result._data[j] = 1;
          }
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        /*
         * All uninitialised matrices are already equal to a zero matrix.
         */
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          if (all(new ViewSubRow(X, i))) {
            result._data[i] = 1;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns true if any element of the provided vector is non-zero.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static boolean any(AbstractVector V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    for (int n = 0; n < V.n_elem; n++) {
      if (V._data[n] != 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns a 1 for each column of the provided matrix where all elements are non-zero and a 0 otherwise.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T all(Class<T> return_type, Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within all(Class<T>, Mat, int).
     */

    return all(return_type, X, 0);
  }

  protected static boolean any(AbstractView V) {
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
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static <T extends AbstractVector> T any(Class<T> return_type, Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within any(Class<T>, Mat, int).
     */

    return any(return_type, X, 0);

  }

  /**
   * Returns a 1 for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix where any element
   * is non-zero and a 0 otherwise.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T any(Class<T> return_type, Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        /*
         * All uninitialised matrices are already equal to a zero matrix.
         */
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          if (any(new ViewSubCol(X, j))) {
            result._data[j] = 1;
          }
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        /*
         * All uninitialised matrices are already equal to a zero matrix.
         */
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          if (any(new ViewSubRow(X, i))) {
            result._data[i] = 1;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  protected static void conv(AbstractVector result, AbstractVector A, AbstractVector B) {
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
   * Returns the convolution between the first and second provided vector.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code B.n_rows}, {@code B.n_cols})-matrix must have at least one
   *           element.
   */
  public static Col conv(Col A, AbstractVector B) throws RuntimeException {
    if (A.empty()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-vector must have at least one element.");
    }

    if (B.empty()) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-vector must have at least one element.");
    }

    Col result = new Col(A.n_elem + B.n_elem - 1);
    conv(result, A, B);
    return result;
  }

  /**
   * Returns the convolution between the first and second provided vector.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code B.n_rows}, {@code B.n_cols})-matrix must have at least one
   *           element.
   */
  public static Row conv(Row A, AbstractVector B) throws RuntimeException {
    if (A.empty()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-vector must have at least one element.");
    }

    if (B.empty()) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-vector must have at least one element.");
    }

    Row result = new Row(A.n_elem + B.n_elem - 1);
    conv(result, A, B);
    return result;
  }

  /**
   * Returns the correlation between the first and second provided vector normalised by {@code X.n_elem} - 1.
   * 
   * @param X The first vector
   * @param Y The second vector
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code X.n_elem},
   *           {@code Y.n_elem}).
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   */
  public static double cor(AbstractVector X, AbstractVector Y) throws RuntimeException {
    /*
     * The parameters "X" and "Y" are validated within cor(AbstractVector, AbstractVector, int).
     */

    return cor(X, Y, 0);
  }

  /**
   * Returns the correlation between the first and second provided vector normalised by {@code X.n_elem -1} (
   * {@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The first vector
   * @param Y The second vector
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code X.n_elem},
   *           {@code Y.n_elem}).
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double cor(AbstractVector X, AbstractVector Y, int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameters "X", "Y" and "norm_type" are validated within cov(AbstractVector, AbstractVector, int).
     */

    return cov(X, Y, norm_type) / (stddev(X, norm_type) * stddev(Y, norm_type));
  }

  /**
   * Returns the autocorrelation of the provided vector normalised by {@code X.n_elem} - 1.
   * <p>
   * <b>Note:</b> The result will always be 1.
   * 
   * @param X The vector
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static double cor(AbstractVector X) throws RuntimeException {
    /*
     * The autocorrelation of a vector is always 1.
     */
    return 1;
  }

  /**
   * Returns the autocorrelation of the provided vector normalised by {@code X.n_elem -1} ({@code norm_type} = 0) or
   * {@code X.n_elem} ({@code norm_type} = 1).
   * <p>
   * <b>Note:</b> The result will always be 1.
   * 
   * @param X The vector
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double cor(AbstractVector X, int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The autocorrelation of a vector is always 1.
     */
    return 1;
  }

  /**
   * Returns the correlation between the first and second provided matrix, where the ({@code i}, {@code j})-th entry is
   * the correlation between the {@code i}th column of the first matrix and the {@code j}th column of the second matrix
   * normalised by {@code X.n_elem -1}.
   * 
   * @param X The first matrix
   * @param Y The second matrix
   * 
   * @throws RuntimeException Both provided matrices ({@code X.n_rows}, {@code X.n_cols} and {@code Y.n_rows},
   *           {@code Y.n_cols}) must have the same shape.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   */
  public static Mat cor(Mat X, Mat Y) throws RuntimeException {
    /*
     * The parameters "X" and "Y" are validated within cor(AbstractVector, AbstractVector, int).
     */

    return cor(X, Y, 0);
  }

  /**
   * Returns the correlation between the first and second provided matrix, where the ({@code i}, {@code j})-th entry is
   * the correlation between the {@code i}th column of the first matrix and the {@code j}th column of the second matrix
   * normalised by {@code X.n_elem -1} ( {@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The first matrix
   * @param Y The second matrix
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException Both provided matrices ({@code X.n_rows}, {@code X.n_cols} and {@code Y.n_rows},
   *           {@code Y.n_cols}) must have the same shape.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   */
  public static Mat cor(Mat X, Mat Y, int norm_type) throws RuntimeException {
    /*
     * The parameters "X", "Y" and "norm_type" are validated within cov(AbstractVector, AbstractVector, int).
     */

    Mat result = new Mat(X.n_cols, X.n_cols);

    Mat covariance = cov(X, Y, norm_type);
    int n = 0;
    for (int j = 0; j < X.n_cols; j++) {
      double stddevX = stddev(new ViewSubCol(X, j), 0);

      for (int jj = 0; jj < Y.n_cols; jj++) {
        result._data[n++] = covariance.at(j, jj) / (stddevX * stddev(new ViewSubCol(Y, jj), 0));
      }
    }

    return result;
  }

  /**
   * Returns the autocorrelation of the provided matrix, where the ({@code i}, {@code j})-th entry is
   * the correlation between the {@code i}th column and {@code j}th column normalised by {@code X.n_elem -1}.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat cor(Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within cor(Mat, int).
     */

    return cor(X, 0);
  }

  /**
   * Returns the autocorrelation of the provided matrix, where the ({@code i}, {@code j})-th entry is
   * the correlation between the {@code i}th column and {@code j}th column normalised by {@code X.n_elem -1} (
   * {@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat cor(Mat X, int norm_type) throws RuntimeException {
    /*
     * The parameters "X" and "norm_type" are validated within cor(Mat, Mat, int).
     */

    return cor(X, X, norm_type);
  }

  /**
   * Returns the covariance between the first and second provided vector normalised by {@code X.n_elem} - 1.
   * 
   * @param X The first vector
   * @param Y The second vector
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code X.n_elem},
   *           {@code Y.n_elem}).
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   */
  public static double cov(AbstractVector X, AbstractVector Y) throws RuntimeException {
    /*
     * The parameters "X" and "Y" are validated within cov(AbstractVector, AbstractVector, int).
     */

    return cov(X, Y, 0);
  }

  /**
   * Returns the covariance between the first and second provided vector normalised by {@code X.n_elem -1} (
   * {@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The first vector
   * @param Y The second vector
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code X.n_elem},
   *           {@code Y.n_elem}).
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double cov(AbstractVector X, AbstractVector Y, int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameters "X" and "Y" are validated within mean(AbstractVector).
     */

    if (X.n_elem != Y.n_elem) {
      throw new RuntimeException("Both provided matrices must have the same number of elements (" + X.n_elem + ", " + Y.n_elem + ").");
    }

    double meanX = mean(X);
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
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return covariance;
  }

  /**
   * Returns the covariance of the provided vector with itself normalised by {@code X.n_elem} - 1.
   * <p>
   * <b>Note:</b> This is better known as the variance of provided vector.
   * 
   * @param X The vector
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * 
   * @see var(AbstractVector)
   */
  public static double cov(AbstractVector X) throws RuntimeException {
    /*
     * The parameter "X" is validated within cov(AbstractVector, int).
     */

    return cov(X, 0);
  }

  /**
   * Returns the covariance of the provided vector with itself normalised by {@code X.n_elem -1} ({@code norm_type} = 0)
   * or {@code X.n_elem} ({@code norm_type} = 1).
   * <p>
   * <b>Note:</b> This is better known as the variance of provided vector.
   * 
   * @param X The vector
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   * 
   * @see var(AbstractVector, int)
   */
  public static double cov(AbstractVector X, int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameter "X" is validated within var(AbstractVector, int).
     */

    return var(X, norm_type);
  }

  /**
   * Returns the covariance between the first and second provided matrix, where the ({@code i}, {@code j})-th entry is
   * the covariance between the {@code i}th column of the first matrix and the {@code j}th column of the second matrix
   * normalised by {@code X.n_elem -1}.
   * 
   * @param X The first matrix
   * @param Y The second matrix
   * 
   * @throws RuntimeException Both provided matrices ({@code X.n_rows}, {@code X.n_cols} and {@code Y.n_rows},
   *           {@code Y.n_cols}) must have the same shape.
   * @throws RuntimeException The first provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one
   *           element.
   */
  public static Mat cov(Mat X, Mat Y) {
    /*
     * The parameters "X" and "Y" are validated within cov(Mat, Mat, int).
     */

    return cov(X, Y, 0);
  }

  /**
   * Returns the covariance between the first and second provided matrix, where the ({@code i}, {@code j})-th entry is
   * the covariance between the {@code i}th column of the first matrix and the {@code j}th column of the second matrix
   * normalised by {@code X.n_elem -1} ({@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The first matrix
   * @param Y The second matrix
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException Both provided matrices ({@code X.n_rows}, {@code X.n_cols} and {@code Y.n_rows},
   *           {@code Y.n_cols}) must have the same shape.
   * @throws RuntimeException The first provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one
   *           element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static Mat cov(Mat X, Mat Y, int norm_type) throws RuntimeException, IllegalArgumentException {
    if (X.n_rows != Y.n_rows || X.n_cols != Y.n_cols) {
      throw new RuntimeException("Both provided matrices (" + X.n_rows + ", " + X.n_cols + " and " + Y.n_rows + ", " + Y.n_cols + ") must have the same shape.");
    }

    if (X.is_empty()) {
      throw new RuntimeException("The first provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    if (Y.is_empty()) {
      throw new RuntimeException("The second provided (" + Y.n_rows + ", " + Y.n_cols + ")-matrix must have at least one element.");
    }

    Mat result = new Mat(X.n_cols, X.n_cols);

    int n = 0;
    switch (norm_type) {
      case 0:
        for (int j = 0; j < X.n_cols; j++) {
          int columnIndexX = j * X.n_rows;

          double meanX = mean(new ViewSubCol(X, j));

          for (int jj = 0; jj < Y.n_cols; jj++) {
            int columnIndexY = jj * Y.n_rows;

            double meanY = mean(new ViewSubCol(Y, jj));

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

          double meanX = mean(new ViewSubCol(X, j));

          for (int jj = 0; jj < Y.n_cols; jj++) {
            int columnIndexY = jj * Y.n_rows;

            double meanY = mean(new ViewSubCol(Y, jj));

            double covariance = 0;
            for (int i = 0; i < X.n_rows; i++) {
              covariance += (X._data[i + columnIndexX] - meanX) * (X._data[i + columnIndexY] - meanY);
            }
            result._data[n++] = covariance / X.n_elem;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the covariance of the provided matrix, where the ({@code i}, {@code j})-th entry is the correlation between
   * the {@code i}th column and {@code j}th column normalised by {@code X.n_elem -1}.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat cov(Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within cov(Mat, int).
     */

    return cov(X, 0);
  }

  /**
   * Returns the covariance of the provided matrix, where the ({@code i}, {@code j})-th entry is the correlation between
   * the {@code i}th column and {@code j}th column normalised by {@code X.n_elem -1} ({@code norm_type} = 0) or
   * {@code X.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static Mat cov(Mat X, int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameter "X" is validated within cov(Mat, Mat, int).
     */

    return cov(X, X, norm_type);
  }

  protected static void cross(double[] result, double[] A, double[] B) {
    result[0] = A[1] * B[2] - A[2] * B[1];
    result[1] = A[2] * B[0] - A[0] * B[2];
    result[2] = A[0] * B[1] - A[1] * B[0];
  }

  /**
   * Returns the cross product between the provided vectors.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   * @throws RuntimeException The first provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   */
  public static Col cross(Col A, AbstractMat B) throws RuntimeException {
    if (A.n_elem != 3) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    Col result = new Col(3);

    cross(result._data, A._data, B._data);
    return result;
  }

  /**
   * Returns the cross product between the provided vectors.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   * @throws RuntimeException The first provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   */
  public static Row cross(Row A, AbstractMat B) throws RuntimeException {
    if (A.n_elem != 3) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    Row result = new Row(3);

    cross(result._data, A._data, B._data);
    return result;
  }

  /**
   * Returns the cross product between the provided vectors.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   * @throws RuntimeException The first provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   */
  public static Mat cross(Mat A, AbstractMat B) throws RuntimeException {
    if (!A.is_vec() || A.n_elem != 3) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    Mat result = new Mat();
    result.copy_size(A);

    cross(result._data, A._data, B._data);
    return result;
  }

  protected static void cumsum(double[] result, double[] V) {
    result[0] = V[0];
    for (int n = 1; n < result.length; n++) {
      result[n] = result[n - 1] + V[n];
    }
  }

  /**
   * Returns the cumulative sum of the provided vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Col cumsum(Col V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Col result = new Col(V.n_elem);
    cumsum(result._data, V._data);
    return result;
  }

  /**
   * Returns the cumulative sum of the provided vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Row cumsum(Row V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Row result = new Row(V.n_elem);
    cumsum(result._data, V._data);
    return result;
  }

  protected static void cumsum(AbstractView result, AbstractView V) {
    V.iteratorReset();
    result.iteratorReset();

    int previousPosition = result.iteratorNext();

    result._data[previousPosition] = V._data[V.iteratorNext()];
    for (int n = 1; n < result.n_elem; n++) {
      int currentPosition = result.iteratorNext();
      result._data[currentPosition] = result._data[previousPosition] + V._data[V.iteratorNext()];
      previousPosition = currentPosition;
    }
  }

  /**
   * Returns the cumulative sum for each column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat cumsum(Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within cumsum(Mat, int).
     */

    return cumsum(X, 0);
  }

  /**
   * Returns the cumulative sum for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat cumsum(Mat X, int dim) throws RuntimeException, IllegalArgumentException {
    Mat result = new Mat(X.n_rows, X.n_cols);

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        for (int j = 0; j < X.n_cols; j++) {
          cumsum(new ViewSubCol(result, j), new ViewSubCol(X, j));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        for (int i = 0; i < X.n_rows; i++) {
          cumsum(new ViewSubRow(result, i), new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns a diagonal matrix with the provided vector set as the main diagonal.
   * 
   * @param X The vector
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-vector must have at least one element.
   */
  public static Mat diagmat(AbstractVector X) throws RuntimeException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-vector must have at least one element.");
    }

    Mat result = new Mat(X.n_elem, X.n_elem);
    new ViewDiag(result, 0).inPlace(Op.EQUAL, X);
    return result;
  }

  /**
   * Returns a diagonal matrix with all elements of the provided square matrix besides the main diagonal set to zero.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must be square.
   */
  public static Mat diagmat(Mat X) throws RuntimeException {
    if (X.empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    if (!X.is_square()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must be square.");
    }

    /*
     * All uninitialised matrices are already equal to a zero matrix.
     */
    Mat result = new Mat(X.n_rows, X.n_cols);
    new ViewDiag(result, 0).inPlace(Op.EQUAL, new ViewDiag(X, 0));
    return result;
  }

  /**
   * Returns a column vector containing the all positions of non-zero elements within the provided matrix.
   * 
   * @param X The matrix
   */
  public static Col find(AbstractMat X) {
    /*
     * The parameter "X" is validated within find(AbstractMat, int).
     */

    return find(X, 0);
  }

  /**
   * Returns a column vector containing the all ({@code k} = 0) or at most {@code k} ({@code k} > 0) positions of the
   * first non-zero elements within the provided matrix.
   * 
   * @param X The matrix
   * @param k The number of positions
   * 
   * @throws NegativeArraySizeException The specified number of positions ({@code k}) must be positive.
   */
  public static Col find(AbstractMat X, int k) throws NegativeArraySizeException {
    /*
     * The parameters "X" and "k" are validated within cumsum(AbstractMat, int, String).
     */

    return find(X, k, "first");
  }

  /**
   * Returns a column vector containing the all ({@code k} = 0) or at most {@code k} ({@code k} > 0) positions of the
   * first ({@code s} = 'first') or last ({@code s} = 'last') non-zero elements within the provided matrix.
   * 
   * @param X The matrix
   * @param k The number of positions
   * @param s The search direction
   * 
   * @throws NegativeArraySizeException The specified number of positions ({@code k}) must be positive.
   * @throws IllegalArgumentException The norm sorting order ({@code s}) must be one of 'first' or 'last'.
   */
  public static Col find(AbstractMat X, int k, String s) throws NegativeArraySizeException, IllegalArgumentException {
    if (k < 0) {
      throw new NegativeArraySizeException("The specified number of positions (" + k + ") must be positive.");
    }

    double[] temp = new double[X.n_elem];

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
            temp[index++] = n;
          }
        }
        break;
      case "last":
        for (int n = X.n_elem - 1; n >= 0 && index < limit; n--) {
          if (X._data[n] != 0) {
            temp[index++] = n;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The search direction (" + s + ") must be one of 'first' or 'last'.");
    }

    Col result = new Col(X.n_elem);
    result._data = Arrays.copyOf(temp, index);

    if (s.equals("last")) {
      revert(result._data, result._data);
    }

    return result;
  }

  /**
   * Returns a deep copy of the matrix with reverse order of its columns.
   * 
   * @param X The matrix
   */
  public static Mat fliplr(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);

    for (int j = 0; j < result.n_cols / 2; j++) {
      System.arraycopy(X._data, result.n_cols - (j + 1) * X.n_rows, result._data, j * X.n_rows, X.n_rows);
    }

    return result;
  }

  /**
   * Returns a deep copy of the matrix with reverse order of its rows.
   * 
   * @param X The matrix
   */
  public static Mat flipud(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);

    for (int i = 0; i < result.n_rows / 2; i++) {
      new ViewSubRow(result, i).inPlace(Op.EQUAL, new ViewSubRow(X, X.n_rows - (i + 1)));
    }

    return result;
  }

  protected static void hist(double[] result, double[] V, int n_bins) {
    double minimum = V[0];
    double maximum = V[0];
    for (int n = 1; n < V.length; n++) {
      double value = V[n];
      minimum = Math.min(minimum, value);
      maximum = Math.max(maximum, value);
    }

    if (Double.isInfinite(minimum)) {
      minimum = -Double.MAX_VALUE;
    }

    if (Double.isInfinite(maximum)) {
      maximum = Double.MAX_VALUE;
    }

    double[] edges = new double[n_bins + 1];
    edges[0] = Double.NEGATIVE_INFINITY;

    double stepLength = (maximum - minimum) / edges.length;
    for (int n = 1; n < n_bins; n++) {
      /*
       * While increasing the value step by step per stepLength will be faster, it will also reduce the precision.
       */
      edges[n] = minimum + stepLength * n;
    }

    edges[edges.length - 1] = Double.POSITIVE_INFINITY;

    double[] temp = new double[edges.length];
    histc(temp, V, edges);
    result = Arrays.copyOf(temp, n_bins);
    result[result.length - 1] += temp[temp.length - 1];
  }

  /**
   * Returns the histogramm of the provided vector by using 10 uniformly distributed bins placed in regards to the range
   * of values within the vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Col hist(Col V) throws RuntimeException {
    return hist(V, 10);
  }

  /**
   * Returns the histogramm of the provided vector by using the specified number of uniformly distributed bins placed in
   * regards to the range of values within the vector.
   * 
   * @param V The vector
   * @param n_bins The number of bins
   * 
   * @throws NegativeArraySizeException The specified number of bins ({@code n_bins}) must be positive.
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Col hist(Col V, int n_bins) throws RuntimeException, IllegalArgumentException {
    if (n_bins < 0) {
      throw new NegativeArraySizeException("The specified number of bins (" + n_bins + ") must be positive.");
    }

    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Col result = new Col(V.n_elem);
    hist(result._data, V._data, n_bins);
    return result;
  }

  /**
   * Returns the histogramm of the provided vector by using 10 uniformly distributed bins placed in regards to the range
   * of values within the vector.
   * 
   * @param V The vector
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Row hist(Row V) throws RuntimeException {
    return hist(V, 10);
  }

  /**
   * Returns the histogramm of the provided vector by using the specified number of uniformly distributed bins placed in
   * regards to the range of values within the vector.
   * 
   * @param V The vector
   * @param n_bins The number of bins
   * 
   * @throws NegativeArraySizeException The specified number of bins ({@code n_bins}) must be positive.
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Row hist(Row V, int n_bins) throws RuntimeException, IllegalArgumentException {
    if (n_bins < 0) {
      throw new NegativeArraySizeException("The specified number of bins (" + n_bins + ") must be positive.");
    }

    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Row result = new Row(V.n_elem);
    hist(result._data, V._data, n_bins);
    return result;
  }

  /**
   * Returns the histogramm for each column of the provided matrix by using 10 uniformly distributed bins placed in
   * regards to the range of values within the matrix.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static Mat hist(Mat X) throws RuntimeException {
    return hist(X, 10);
  }

  /**
   * Returns the histogramm for each column of the provided matrix by using the specified number of uniformly
   * distributed bins placed in regards to the range of values within the matrix.
   * 
   * @param X The matrix
   * @param n_bins The number of bins
   * 
   * @throws NegativeArraySizeException The specified number of bins ({@code n_bins}) must be positive.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static Mat hist(Mat X, int n_bins) throws NegativeArraySizeException, RuntimeException {
    return hist(X, n_bins, 0);
  }

  /**
   * Returns the histogramm for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix by using
   * the specified number of uniformly distributed bins placed in regards to the range of values within the matrix.
   * 
   * @param X The matrix
   * @param n_bins The number of bins
   * @param dim The dimension
   * 
   * @throws NegativeArraySizeException The specified number of bins ({@code n_bins}) must be positive.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat hist(Mat X, int n_bins, int dim) throws NegativeArraySizeException, RuntimeException, IllegalArgumentException {
    if (n_bins < 0) {
      throw new NegativeArraySizeException("The specified number of bins (" + n_bins + ") must be positive.");
    }

    Mat result = new Mat();

    switch (dim) {
      case 0:
        result.set_size(n_bins, X.n_cols);

        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        for (int j = 0; j < X.n_cols; j++) {
          new ViewSubCol(result, j).inPlace(Op.EQUAL, hist(X.col(j), n_bins));
        }
        break;
      case 1:
        result.set_size(X.n_rows, n_bins);

        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        for (int i = 0; i < X.n_rows; i++) {
          new ViewSubRow(result, i).inPlace(Op.EQUAL, hist(X.row(i), n_bins));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  protected static void hist(double[] result, double[] V, double[] centers) {
    double[] edges = new double[centers.length + 1];

    edges[0] = Double.NEGATIVE_INFINITY;

    double lastElement = centers[0];
    for (int n = 1; n < centers.length; n++) {
      double currentElement = centers[n];

      edges[n] = lastElement + (currentElement - lastElement) / 2;

      lastElement = currentElement;
    }

    edges[edges.length - 1] = Double.POSITIVE_INFINITY;

    double[] temp = new double[edges.length];
    histc(temp, V, edges);
    result = Arrays.copyOf(temp, centers.length);
    result[result.length - 1] += temp[temp.length - 1];
  }

  /**
   * Returns the histogramm of the provided vector for the provided, monotonically increasing bin centers.
   * 
   * @param V The vector
   * @param centers The bin centers
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Col hist(Col V, AbstractMat centers) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Col result = new Col(V.n_elem);
    hist(result._data, V._data, centers._data);
    return result;
  }

  /**
   * Returns the histogramm of the provided vector for the provided, monotonically increasing bin centers.
   * 
   * @param V The vector
   * @param centers The bin centers
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Row hist(Row V, AbstractMat centers) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Row result = new Row(V.n_elem);
    hist(result._data, V._data, centers._data);
    return result;
  }

  /**
   * Returns the histogramm for each column of the provided matrix for the provided, monotonically increasing bin
   * centers.
   * 
   * @param X The matrix
   * @param centers The bin centers
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static Mat hist(Mat X, AbstractMat centers) throws RuntimeException {
    return hist(X, centers, 0);
  }

  /**
   * Returns the histogramm for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix for the
   * provided, monotonically increasing bin centers.
   * 
   * @param X The matrix
   * @param centers The bin centers
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat hist(Mat X, AbstractMat centers, int dim) throws RuntimeException, IllegalArgumentException {
    Mat result = new Mat();

    switch (dim) {
      case 0:
        result.set_size(centers.n_elem, X.n_cols);

        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        for (int j = 0; j < X.n_cols; j++) {
          new ViewSubCol(result, j).inPlace(Op.EQUAL, hist(X.col(j), centers));
        }
        break;
      case 1:
        result.set_size(X.n_rows, centers.n_elem);

        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        for (int i = 0; i < X.n_rows; i++) {
          new ViewSubRow(result, i).inPlace(Op.EQUAL, hist(X.row(i), centers));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  protected static void histc(double[] result, double[] V, double[] edges) {
    for (int n = 0; n < V.length; n++) {
      double element = V[n];

      double edge = edges[0];

      if (element < edge) {
        continue;
      }

      for (int nn = 1; nn < edges.length; nn++) {
        edge = edges[nn];

        if (element < edge) {
          result[nn - 1]++;
          break;
        }
      }

      if (element == edge) {
        result[edges.length - 1]++;
      }
    }
  }

  /**
   * Returns the histogramm of the provided vector for the provided, monotonically increasing bin edges.
   * 
   * @param V The vector
   * @param centers The bin edges
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Col histc(Col V, AbstractMat edges) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Col result = new Col(V.n_elem);
    histc(result._data, V._data, edges._data);
    return result;
  }

  /**
   * Returns the histogramm of the provided vector for the provided, monotonically increasing bin edges.
   * 
   * @param V The vector
   * @param centers The bin edges
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Row histc(Row V, AbstractMat edges) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Row result = new Row(V.n_elem);
    histc(result._data, V._data, edges._data);
    return result;
  }

  /**
   * Returns the histogramm for each column of the provided matrix for the provided, monotonically increasing bin edges.
   * 
   * @param X The matrix
   * @param centers The bin edges
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   */
  public static Mat histc(Mat X, AbstractMat edges) {
    return hist(X, edges, 0);
  }

  /**
   * Returns the histogramm for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix for the
   * provided, monotonically increasing bin edges.
   * 
   * @param X The matrix
   * @param centers The bin edges
   * @param dim The dimension
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one row.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one column.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat histc(Mat X, AbstractMat edges, int dim) {
    Mat result = new Mat();

    switch (dim) {
      case 0:
        result.set_size(edges.n_elem, X.n_cols);

        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        for (int j = 0; j < X.n_cols; j++) {
          new ViewSubCol(result, j).inPlace(Op.EQUAL, histc(X.col(j), edges));
        }
        break;
      case 1:
        result.set_size(X.n_rows, edges.n_elem);

        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        for (int i = 0; i < X.n_rows; i++) {
          new ViewSubRow(result, i).inPlace(Op.EQUAL, histc(X.row(i), edges));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Replaces the matrix with its tranpose in-place.
   * 
   * @param X The matrix
   */
  public static void inplace_trans(Mat X) {
    int n = 0;
    for (int j = 0; j < X.n_cols; j++) {
      for (int i = 0; i < X.n_rows; i++) {
        if (i < j) {
          double temp = X._data[i + j * X.n_rows];
          X._data[i + j * X.n_rows] = X._data[n];
          X._data[n] = temp;
        } else if (i == j) {
          X._data[n] = X._data[n];
        }

        n++;
      }
    }
  }

  /**
   * Returns the concatenation of the provided matrices along their rows.
   * <p>
   * The first provided matrix will be placed left and second one right.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @throws RuntimeException Both matrices must have the same number of rows ({@code A.n_rows} and {@code B.n_rows}).
   */
  public static Mat join_rows(AbstractMat A, AbstractMat B) {
    return join_horiz(A, B);
  }

  /**
   * Returns the concatenation of the provided matrices along their rows.
   * <p>
   * The first provided matrix will be placed left and second one right.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @throws RuntimeException Both matrices must have the same number of rows ({@code A.n_rows} and {@code B.n_rows}).
   */
  public static Mat join_horiz(AbstractMat A, AbstractMat B) {
    if (A.n_rows != B.n_rows) {
      throw new RuntimeException("Both matrices must have the same number of rows (" + A.n_rows + " and " + B.n_rows + ").");
    }

    Mat result = new Mat(A.n_rows, A.n_cols + B.n_cols);

    System.arraycopy(A._data, 0, result._data, 0, A.n_elem);
    System.arraycopy(B._data, 0, result._data, A.n_elem, B.n_elem);

    return result;
  }

  /**
   * Returns the concatenation of the provided matrices along their columns.
   * <p>
   * The first provided matrix will be placed above and second one below.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @throws RuntimeException Both matrices must have the same number of columns ({@code A.n_cols} and {@code B.n_cols}
   *           ).
   */
  public static Mat join_cols(AbstractMat A, AbstractMat B) {
    return join_vert(A, B);
  }

  /**
   * Returns the concatenation of the provided matrices along their columns.
   * <p>
   * The first provided matrix will be placed above and second one below.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @throws RuntimeException Both matrices must have the same number of columns ({@code A.n_cols} and {@code B.n_cols}
   *           ).
   */
  public static Mat join_vert(AbstractMat A, AbstractMat B) {
    if (A.n_cols != B.n_cols) {
      throw new RuntimeException("Both matrices must have the same number of columns (" + A.n_cols + " and " + B.n_cols + ").");
    }

    Mat result = new Mat(A.n_rows + B.n_rows, A.n_cols);

    new ViewSubRows(result, 0, A.n_rows - 1).inPlace(Op.EQUAL, A);
    new ViewSubRows(result, A.n_rows, result.n_rows - 1).inPlace(Op.EQUAL, B);

    return result;
  }

  /**
   * Returns the kronecker product of the provided matrices.
   * 
   * @param A The left-hand matrix
   * @param B The right-hand matrix
   */
  public static Mat kron(AbstractMat A, AbstractMat B) {
    Mat result = repmat(B, A.n_rows, A.n_cols);

    int n = 0;
    for (int j = 0; j < A.n_cols; j++) {
      for (int i = 0; i < A.n_rows; i++) {
        AbstractView subView = new ViewSubMat(result, i * B.n_rows, j * B.n_cols, B.n_rows, B.n_cols);

        double value = A._data[n++];
        subView.iteratorReset();
        while (subView.iteratorHasNext()) {
          result._data[subView.iteratorNext()] *= value;
        }
      }
    }

    return result;
  }

  /**
   * Resizes the provided matrix to the specified number of rows and columns and reuses existing values in a column-wise
   * manner.
   * 
   * @param mat The matrix
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   */
  public static Mat reshape(Mat mat, int n_rows, int n_cols) {
    Mat result = new Mat(mat);
    result.reshape(n_rows, n_cols);
    return result;
  }

  /**
   * Resizes the matrix to the specified number of rows and columns and preserves existing values at their current
   * position.
   * 
   * @param mat The matrix
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   */
  public static Mat resize(Mat mat, int n_rows, int n_cols) {
    Mat result = new Mat(mat);
    result.resize(n_rows, n_cols);
    return result;
  }

  protected static void shuffle(double[] result, double[] V) {
    /*
     * Performs an inside-out version of the Fisher-Yates shuffle
     */
    result[0] = V[0];
    for (int n = 1; n < V.length; n++)
    {
      int nn = RNG._rng.nextInt(n + 1);

      if (n != nn) {
        result[n] = result[nn];
      }

      result[nn] = V[n];
    }
  }

  public static Col shuffle(Col V) {
    Col result = new Col(V.n_elem);
    shuffle(result._data, V._data);
    return result;
  }

  public static Row shuffle(Row V) {
    Row result = new Row(V.n_elem);
    shuffle(result._data, V._data);
    return result;
  }

  public static Mat shuffle(Mat X) {
    return shuffle(X, 0);
  }

  public static Mat shuffle(Mat X, int dim) {
    Mat result = new Mat();

    switch (dim) {
      case 0:
        if (X.n_rows < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one row.");
        }

        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          /*
           * Creates a deep copy of each column, since shuffling of shallow sub views is not yet implemented.
           */
          new ViewSubCol(result, j).inPlace(Op.EQUAL, shuffle(X.col(j)));
        }
        break;
      case 1:
        if (X.n_cols < 1) {
          throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one column.");
        }

        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          /*
           * Creates a deep copy of each row, since shuffling of shallow sub views is not yet implemented.
           */
          new ViewSubRow(result, i).inPlace(Op.EQUAL, shuffle(X.row(i)));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  protected static void sort(double[] result, double[] V, String sort_direction) {
    result = Arrays.copyOf(V, V.length);

    Arrays.sort(result);

    if (sort_direction == "descend") {
      revert(result, result);
    }
  }

  public static Col sort(Col V) {
    return sort(V, "ascend");
  }

  public static Col sort(Col V, String sort_direction) {
    Col result = new Col(V.n_elem);
    sort(result._data, V._data, sort_direction);
    return result;
  }

  public static Row sort(Row V) {
    return sort(V, "ascend");
  }

  public static Row sort(Row V, String sort_direction) {
    Row result = new Row(V.n_elem);
    sort(result._data, V._data, sort_direction);
    return result;
  }

  public static Mat sort(Mat X) {
    return sort(X, "ascend");
  }

  public static Mat sort(Mat X, String sort_direction) {
    return sort(X, sort_direction, 0);
  }

  public static Mat sort(Mat X, String sort_direction, int dim) {
    Mat result = new Mat();

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
          new ViewSubCol(result, j).inPlace(Op.EQUAL, sort(X.col(j)));
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
          new ViewSubRow(result, i).inPlace(Op.EQUAL, sort(X.row(i)));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  protected static void sort_index(double[] result, double[] V, String sort_direction) {
    stable_sort_index(result, V, sort_direction);
  }

  public static Col sort_index(Col V) {
    return sort(V, "ascend");
  }

  public static Col sort_index(Col V, String sort_direction) {
    Col result = new Col(V.n_elem);
    sort(result._data, V._data, sort_direction);
    return result;
  }

  public static Row sort_index(Row V) {
    return sort(V, "ascend");
  }

  public static Row sort_index(Row V, String sort_direction) {
    Row result = new Row(V.n_elem);
    sort(result._data, V._data, sort_direction);
    return result;
  }

  protected static void stable_sort_index(double[] result, final double[] V, String sort_direction) {
    Integer[] temp = new Integer[result.length];
    for (int n = 0; n < temp.length; n++) {
      temp[n] = n;
    }

    Arrays.sort(temp, new Comparator<Integer>() {
      @Override
      public int compare(final Integer a, final Integer b) {
        return Double.compare(V[a], V[b]);
      }
    });

    for (int n = 0; n < temp.length; n++) {
      result[n] = temp[n];
    }

    if (sort_direction == "descend") {
      revert(result, result);
    }
  }

  public static Col stable_sort_index(Col V) {
    return sort(V, "ascend");
  }

  public static Col stable_sort_index(Col V, String sort_direction) {
    Col result = new Col(V.n_elem);
    sort(result._data, V._data, sort_direction);
    return result;
  }

  public static Row stable_sort_index(Row V) {
    return sort(V, "ascend");
  }

  public static Row stable_sort_index(Row V, String sort_direction) {
    Row result = new Row(V.n_elem);
    sort(result._data, V._data, sort_direction);
    return result;
  }

  public static Mat symmatu(Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    Mat result = new Mat();
    result.copy_size(A);

    int n = 0;
    for (int j = 0; j < A.n_cols; j++) {
      for (int i = 0; i < A.n_rows; i++) {
        double value = A._data[n];

        if (i < j) {
          result._data[n] = value;
          result._data[i + j * result.n_rows] = value;
        } else if (i == j) {
          result._data[n] = value;
        }

        n++;
      }
    }

    return result;
  }

  public static Mat symmatl(Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    Mat result = new Mat();
    result.copy_size(A);

    int n = 0;
    for (int j = 0; j < A.n_cols; j++) {
      for (int i = 0; i < A.n_rows; i++) {
        double value = A._data[n];

        if (i > j) {
          result._data[n] = value;
          result._data[i + j * result.n_rows] = value;
        } else if (i == j) {
          result._data[n] = value;
        }

        n++;
      }
    }

    return result;
  }

  public static Row trans(Col A) {
    return A.t();
  }

  public static Col trans(Row A) {
    return A.t();
  }

  public static Mat trans(Mat A) {
    return A.t();
  }

  public static Mat trimatu(Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    /*
     * All uninitialised matrices are already equal to a zero matrix.
     */
    Mat result = new Mat();
    result.copy_size(A);

    int n = 0;
    for (int j = 0; j < A.n_cols; j++) {
      for (int i = 0; i < A.n_rows; i++) {
        if (i <= j) {
          result._data[n] = A._data[n];
        }
        n++;
      }
    }

    return result;
  }

  public static Mat trimatl(Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    /*
     * All uninitialised matrices are already equal to a zero matrix.
     */
    Mat result = new Mat();
    result.copy_size(A);

    int n = 0;
    for (int j = 0; j < A.n_cols; j++) {
      for (int i = 0; i < A.n_rows; i++) {
        if (i >= j) {
          result._data[n] = A._data[n];
        }
        n++;
      }
    }

    return result;
  }

  protected static void unique(AbstractMat result, double[] A) {
    double[] sortedA = Arrays.copyOf(A, A.length);
    Arrays.sort(sortedA);

    double[] temp = new double[sortedA.length];
    double currentValue = sortedA[0];
    temp[0] = currentValue;

    int n = 0;
    for (int nn = 0; nn < sortedA.length; nn++) {
      double value = sortedA[n];

      if (value > currentValue || Double.isInfinite(value)) {
        temp[n++] = value;
        currentValue = value;
      }
    }

    result.set_size(n);
    result._data = Arrays.copyOf(temp, temp.length);
  }

  public static Col unique(Col A) {
    Col result = new Col();
    unique(result, A._data);
    return result;
  }

  public static Row unique(Row A) {
    Row result = new Row();
    unique(result, A._data);
    return result;
  }

  public static Col unique(Mat A) {
    Col result = new Col();
    unique(result, A._data);
    return result;
  }

  public static <T extends AbstractVector> T vectorise(Class<T> return_type, Mat A) {
    return vectorise(return_type, A, 0);
  }

  public static <T extends AbstractVector> T vectorise(Class<T> return_type, Mat A, int dim) throws IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    result.set_size(A.n_elem);

    switch (dim) {
      case 0:
        result._data = Arrays.copyOf(A._data, A.n_elem);
        break;
      case 1:
        int n = 0;
        for (int i = 0; i < A.n_rows; i++) {
          for (int j = 0; j < A.n_cols; j++) {
            result._data[n++] = A._data[i + j * A.n_rows];
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  public static Mat chol(Mat X) {
    Mat R = new Mat();
    inv_sympd(R, X);
    return R;
  }

  public static void chol(Mat R, Mat X) throws RuntimeException {
    R.inPlace(Op.EQUAL, X);

    intW info = new intW(0);

    LAPACK.getInstance().dpotrf("U", X.n_rows, R._data, X.n_rows, info);
    if (info.val != 0) {
      throw new RuntimeException("The factorisation could not be completed. Ensure that the provided matrix is positive-definite.");
    }
  }

  public static <T extends AbstractVector> T eig_sym(Class<T> return_type, Mat X) throws RuntimeException {
    T eigval;

    try {
      eigval = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    eig_sym(eigval, X);
    return eigval;
  }

  public static void eig_sym(AbstractVector eigval, Mat X) throws RuntimeException {
    /*
     * The size of this arrays is defined by the specification of the LAPACK routine.
     */
    double[] work = new double[Math.max(1,  3 * (X.n_rows - 1))];
    intW info = new intW(0);

    LAPACK.getInstance().dsyev("N", "U", X.n_rows, X._data, X.n_rows, eigval._data, work, work.length, info);
    if (info.val != 0) {
      throw new RuntimeException("The algorithm failed to converge. Ensure that the provided matrix is symmetric.");
    }
  }

  public static void eig_sym(AbstractVector eigval, Mat eigvec, Mat X) throws RuntimeException {
    eigvec.inPlace(Op.EQUAL, X);

    /*
     * The size of this arrays is defined by the specification of the LAPACK routine.
     */
    double[] work = new double[Math.max(1,  3 * (X.n_rows - 1))];
    intW info = new intW(0);

    LAPACK.getInstance().dsyev("V", "U", X.n_rows, eigvec._data, X.n_rows, eigval._data, work, work.length, info);
    if (info.val != 0) {
      throw new RuntimeException("The algorithm failed to converge. Ensure that the provided matrix is symmetric.");
    }
  }

  public static Mat inv(Mat A) {
    Mat B = new Mat();
    inv(B, A);
    return B;
  }

  public static void inv(Mat B, Mat A) {
    B.inPlace(Op.EQUAL, A.i());
  }

  public static Mat inv_sympd(Mat A) {
    Mat B = new Mat();
    inv_sympd(B, A);
    return B;
  }

  public static void inv_sympd(Mat B, Mat A) {
    B.inPlace(Op.EQUAL, A);
    intW info = new intW(0);

    LAPACK.getInstance().dpotrf("U", A.n_rows, B._data, 0, info);
    if (info.val != 0) {
      throw new RuntimeException("The factorisation could not be completed. Ensure that the provided matrix is positive-definite.");
    }

    LAPACK.getInstance().dpotri("U", A.n_rows, B._data, 0, info);
    if (info.val != 0) {
      throw new RuntimeException("The inverse could not be computed. The provided matrix appears to be singular.");
    }
  }

  public static boolean lu(Mat L, Mat U, Mat P, Mat X) {
    U.inPlace(Op.EQUAL, X);

    int[] pivotIndices = new int[Math.min(X.n_rows, X.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(X.n_rows, X.n_cols, U._data, X.n_rows, pivotIndices, info);
    if (info.val != 0) {
      return false;
    }

    L.copy_size(U);

    for (int j = 0; j < X.n_cols; j++) {
      for (int i = 0; i < X.n_rows; i++) {
        if (i < j) {
          /*
           * strict upper triangle
           */
          L._data[i + j * L.n_rows] = 0;
        } else if (i == j) {
          /*
           * main diagonal
           */
          L._data[i + j * L.n_rows] = 1;
        } else {
          /*
           * strict lower triangle
           */
          L._data[i + j * L.n_rows] = U._data[i + j * U.n_rows];
          U._data[i + j * U.n_rows] = 0;
        }
      }
    }

    int[] pivotVector = new int[pivotIndices.length];
    for (int n = 0; n < pivotVector.length; n++) {
      pivotVector[n] = n;
    }

    for (int n = 0; n < pivotVector.length; n++) {
      int nn = pivotIndices[n] - 1;

      if (pivotVector[n] != pivotVector[nn]) {
        int temp = pivotVector[n];
        pivotVector[n] = pivotVector[nn];
        pivotVector[nn] = temp;
      }
    }

    P.zeros(X.n_rows, X.n_rows);
    for (int i = 0; i < P.n_rows; i++) {
      P._data[i + pivotIndices[i] * P.n_rows] = 1;
    }

    if (L.n_cols > U.n_rows) {
      L.shed_cols(U.n_rows, L.n_cols - 1);
    }

    if (U.n_rows > L.n_cols) {
      U.shed_rows(L.n_cols, U.n_rows - 1);
    }

    return true;
  }

  public static boolean lu(Mat L, Mat U, Mat X) {
    U.inPlace(Op.EQUAL, X);

    int[] pivotIndices = new int[Math.min(X.n_rows, X.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(X.n_rows, X.n_cols, U._data, X.n_rows, pivotIndices, info);
    if (info.val != 0) {
      return false;
    }

    L.copy_size(U);

    for (int j = 0; j < X.n_cols; j++)
    {
      for (int i = 0; i < X.n_rows; i++)
      {
        if (i < j) {
          /*
           * strict upper triangle
           */
          L._data[i + j * L.n_rows] = 0;
        } else if (i == j) {
          /*
           * main diagonal
           */
          L._data[i + j * L.n_rows] = 1;
        } else {
          /*
           * strict lower triangle
           */
          L._data[i + j * L.n_rows] = U._data[i + j * U.n_rows];
          U._data[i + j * U.n_rows] = 0;
        }
      }
    }

    if (L.n_cols > U.n_rows) {
      L.shed_cols(U.n_rows, L.n_cols - 1);
    }

    if (U.n_rows > L.n_cols) {
      U.shed_rows(L.n_cols, U.n_rows - 1);
    }

    return true;
  }

  public static Mat pinv(Mat A) {
    return pinv(A, Math.max(A.n_rows, A.n_cols) * norm(A, 2) * Datum.eps);
  }

  public static Mat pinv(Mat A, double tolerance) {
    Mat B = new Mat();
    pinv(B, A, tolerance);
    // TODO add exception if pinv returns false
    return B;
  }

  public static boolean pinv(Mat B, Mat A) {
    return pinv(B, A, Math.max(A.n_rows, A.n_cols) * norm(A, 2) * Datum.eps);
  }

  public static boolean pinv(Mat B, Mat A, double tolerance) {
    Mat U = new Mat();
    Col s = new Col();
    Mat V = new Mat();

    boolean status;

    if (A.n_cols > A.n_rows) {
      status = svd_econ(U, s, V, A.t());
    } else {
      status = svd_econ(U, s, V, A);
    }

    if (status == false) {
      return  false;
    }

    int count = 0;
    for (int n = 0; n < s.n_elem; n++) {
      if (s._data[n] > tolerance) {
        count++;
      }
    }
    
    if(count > 0) {
      /*
       * Contains all singular values that are larger than the specififed tolerance.
       */
      Col singularValues = new Col(count);
      
      int n = 0;
      for (int nn = 0; nn < s.n_elem; nn++) {
        double value = s._data[nn];
        if (value > tolerance) {
          singularValues._data[n++] = 1 / value;
        }
      }
      
      if(V.n_cols > count) {
        V = V.cols(0, count - 1);
      }
      
      if(U.n_cols > count) {
        U = U.cols(0, count - 1);
      }
      
      if(A.n_rows >= A.n_cols) {
        B.inPlace(Op.EQUAL, V.times(diagmat(singularValues).times(U.t())));
      } else {
        B.inPlace(Op.EQUAL, U.times(diagmat(singularValues).times(V.t())));
      }
    }
    
    return true;
  }

  public static Mat princomp(Mat X) {
    Mat coeff = new Mat();
    princomp(coeff, X);
    return coeff;
  }

  public static boolean princomp(Mat coeff, Mat X) {
    if(X.n_rows > 1) {
      Mat temp = new Mat(X);
      
      Mat U = new Mat();
      Col s = new Col();
      
      if(!svd(U, s, coeff, temp)) {
        return false;
      }
    } else {
      coeff.eye(X.n_cols, X.n_cols);
    }

    return true;
  }

  public static boolean princomp(Mat coeff, Mat score, Mat X) {
    if(X.n_rows > 1) {
      score.inPlace(Op.EQUAL, X);
      score.each_row(Op.MINUS, mean(Row.class, X));
      
      Mat U = new Mat();
      Col s = new Col();
      
      if(!svd(U, s, coeff, score)) {
        return false;
      }
      
      score.inPlace(Op.ELEMTIMES, coeff);
      
      if(X.n_rows <= X.n_cols) {
        new ViewSubCols(score, X.n_rows - 1, X.n_cols - 1).fill(0);
      }
    } else {
      coeff.eye(X.n_cols, X.n_cols);
      
      score.copy_size(X);
      score.zeros();
    }

    return true;
  }

  public static boolean princomp(Mat coeff, Mat score, Col latent, Mat X) {
    if(X.n_rows > 1) {
      score.inPlace(Op.EQUAL, X);
      score.each_row(Op.MINUS, mean(Row.class, X));
      
      Mat U = new Mat();
      Col s = new Col();
      
      if(!svd(U, s, coeff, score)) {
        return false;
      }
      
      s.inPlace(Op.ELEMDIVIDE, Math.sqrt(X.n_rows - 1));
      
      score.inPlace(Op.ELEMTIMES, coeff);
      
      if(X.n_rows <= X.n_cols) {
        new ViewSubCols(score, X.n_rows - 1, X.n_cols - 1).fill(0);
      
        Col s_temp = new Col(X.n_cols, Fill.ZEROS);
        new ViewSubCols(s_temp, 0, X.n_rows - 2).inPlace(Op.EQUAL, new ViewSubCols(s, 0, X.n_rows - 2));
        s.inPlace(Op.EQUAL, s_temp);
      }

      latent = square(s);
    } else {
      coeff.eye(X.n_cols, X.n_cols);
      
      score.copy_size(X);
      score.zeros();
      
      latent.set_size(X.n_cols);
      latent.zeros();
    }

    return true;
  }

  public static boolean princomp(Mat coeff, Mat score, Col latent, Col tsquared, Mat X) {
    if(X.n_rows > 1) {
      score.inPlace(Op.EQUAL, X);
      score.each_row(Op.MINUS, mean(Row.class, X));
      
      Mat U = new Mat();
      Col s = new Col();
      
      if(!svd(U, s, coeff, score)) {
        return false;
      }
      
      s.inPlace(Op.ELEMDIVIDE, Math.sqrt(X.n_rows - 1));
      
      score.inPlace(Op.ELEMTIMES, coeff);
      
      Mat S;
      if(X.n_rows <= X.n_cols) {
        new ViewSubCols(score, X.n_rows - 1, X.n_cols - 1).fill(0);
      
        Col s_temp = new Col(X.n_cols, Fill.ZEROS);
        new ViewSubCols(s_temp, 0, X.n_rows - 2).inPlace(Op.EQUAL, new ViewSubCols(s, 0, X.n_rows - 2));
        s.inPlace(Op.EQUAL, s_temp);

        new ViewSubCols(s_temp, 0, X.n_rows - 2).inPlace(Op.EQUAL, reciprocal(s_temp.rows(0, X.n_rows - 2)));
        
        S = score.times(diagmat(s_temp));
      } else {
        S = score.times(diagmat(reciprocal(s)));
      }

      tsquared.inPlace(Op.EQUAL, sum(Col.class, square(S), 1));
      latent = square(s);
    } else {
      coeff.eye(X.n_cols, X.n_cols);
      
      score.copy_size(X);
      score.zeros();
      
      latent.set_size(X.n_cols);
      latent.zeros();
      
      tsquared.set_size(X.n_rows);
      tsquared.zeros();
    }

    return true;
  }

  public static boolean qr(Mat Q, Mat R, Mat X) {
    if(X.empty()) {
      return false;
    }
    
    R = new Mat(X);
    
    double[] tau = new double[Math.min(X.n_rows, X.n_cols)];
    double[] work = new double[Math.max(1, X.n_cols)];
    intW info = new intW(0);
    
    LAPACK.getInstance().dgeqrf(X.n_rows, X.n_cols, R._data, X.n_rows, tau, work, work.length, info);
    
    if(info.val != 0) {
      return false;
    }
    
    Q.set_size(X.n_rows, X.n_rows);
    Q._data = Arrays.copyOf(R._data, Math.min(Q.n_elem, R.n_elem));

    for (int j = 0; j < R.n_cols; ++j) {
      for (int i = (j + 1); i < R.n_rows; ++i) {
        R._data[i + j * R.n_rows] = 0;
      }
    }
    
    LAPACK.getInstance().dorgqr(X.n_rows, X.n_rows, tau.length, Q._data, X.n_rows, tau, work, work.length, info);
    
    return (info.val == 0);
  }

  public static boolean qr_econ(Mat Q, Mat R, Mat X) {
    if(X.is_empty()) {
      return false;
    }
    
    if(X.n_rows <= X.n_cols) {
      return qr(Q, R, X);
    }
    
    Q = new Mat(X);
    
    double[] tau = new double[Math.min(X.n_rows, X.n_cols)];
    double[] work = new double[Math.max(1, Math.max(X.n_rows, X.n_cols))];
    intW info = new intW(0);
    
    LAPACK.getInstance().dgeqrf(X.n_rows, X.n_cols, Q._data, X.n_rows, tau, work, work.length, info);
    
    if(info.val != 0) {
      return false;
    }
    
    R.set_size(X.n_cols, X.n_cols);
    Q._data = Arrays.copyOf(R._data, Math.min(Q.n_elem, R.n_elem));

    for (int j = 0; j < R.n_cols; ++j) {
      for (int i = 0; i < j; ++i) {
        R._data[i + j * R.n_rows] = Q._data[i + j * Q.n_rows];
      }
      
      for(int i = (j + 1); i < Q.n_cols; ++i) {
        R._data[i + j * R.n_rows] = 0;
      }
    }
    
    LAPACK.getInstance().dorgqr(X.n_rows, X.n_cols, tau.length, Q._data, X.n_rows, tau, work, work.length, info);
    
    return (info.val == 0);
  }

  public static Mat solve(Mat A, Mat B) {
    Mat X = new Mat();
    solve(X, A, B);
    return X;
  }

  public static boolean solve(Mat X, Mat A, Mat B) {
    if(A.empty() || B.empty()) {
      return false;
    }
    
    int[] pivotIndices = new int[A.n_rows + 2];
    intW info = new intW(0);
    
    LAPACK.getInstance().dgesv(A.n_rows, B.n_cols, A._data, A.n_rows, pivotIndices, B._data, A.n_rows, info);
    
    return (info.val == 0);
  }

  public static <T extends AbstractVector> T svd(Class<T> return_type, Mat X) {
    T s;

    try {
      s = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("An internal error occured. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    svd(s, X);
    return s;
  }

  public static boolean svd(AbstractVector s, Mat X) {
    if(X.is_empty()) {
      return false;
    }
    
    double[] U = new double[1];
    s.set_size(Math.min(X.n_rows, X.n_cols));
    double[] V = new double[X.n_cols];

    double[] temp = Arrays.copyOf(X._data, X.n_elem);
    double[] work = new double[Math.max(1, Math.max(3 * Math.min(X.n_rows, X.n_cols) + Math.max(X.n_rows, X.n_cols), 5 * Math.min(X.n_rows, X.n_cols)))];
    intW info = new intW(0);
    
    LAPACK.getInstance().dgesvd("N", "N", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U, 1, V, 1, work, work.length, info);

    return (info.val == 0);
  }

  public static boolean svd(Mat U, AbstractVector s, Mat V, Mat X) {
    if(X.is_empty()) {
      return false;
    }
    
    U.set_size(X.n_rows, X.n_rows);
    s.set_size(Math.min(X.n_rows, X.n_cols));
    V.set_size(X.n_cols, X.n_cols);

    double[] temp = Arrays.copyOf(X._data, X.n_elem);
    double[] work = new double[Math.max(1, Math.max(3 * Math.min(X.n_rows, X.n_cols) + Math.max(X.n_rows, X.n_cols), 5 * Math.min(X.n_rows, X.n_cols)))];
    intW info = new intW(0);
    
    LAPACK.getInstance().dgesvd("A", "A", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U._data, U.n_rows, V._data, V.n_rows, work, work.length, info);
    
    inplace_trans(V);

    return (info.val == 0);
  }

  public static boolean svd_econ(Mat U, AbstractVector s, Mat V, Mat X) {
    return svd_econ(U, s, V, X, "both");
  }

  public static boolean svd_econ(Mat U, AbstractVector s, Mat V, Mat X, String side) {
    if(X.is_empty()) {
      return false;
    }
    
    s.set_size(Math.min(X.n_rows, X.n_cols));

    double[] temp = Arrays.copyOf(X._data, X.n_elem);
    double[] work = new double[3 * Math.max(1, Math.max(3 * Math.min(X.n_rows, X.n_cols) + Math.max(X.n_rows, X.n_cols), 5 * Math.min(X.n_rows, X.n_cols)))];
    intW info = new intW(0);
    
    switch (side) {
      case "left":
        U.set_size(X.n_rows, s.n_elem);
        V.reset();
        
        LAPACK.getInstance().dgesvd("S", "N", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U._data, U.n_rows, V._data, 1, work, work.length, info);
        break;
      case "right":
        U.reset();
        V.set_size(s.n_elem, X.n_cols);
        
        LAPACK.getInstance().dgesvd("N", "S", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U._data, 1, V._data, V.n_rows, work, work.length, info);
        break;
      case "both":
        U.set_size(X.n_rows, s.n_elem);
        V.set_size(s.n_elem, X.n_cols);
        
        LAPACK.getInstance().dgesvd("S", "S", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U._data, U.n_rows, V._data, V.n_rows, work, work.length, info);
        break;
      default:
        return false;
    }
    
    inplace_trans(V);
    
    return (info.val == 0);
  }

  public static Mat syl(Mat A, Mat B, Mat C) {
    Mat X = new Mat();
    syl(X, A, B, C);
    return X;
  }

  public static boolean syl(Mat X, Mat A, Mat B, Mat C) {
    if(A.is_empty() || B.is_empty() || C.is_empty()) {
      return false;
    }
    
    if(!A.is_square() || !B.is_square()) {
      return false;
    }
    
    if(C.n_rows != A.n_rows) {
      return false;
    }
    
    if(C.n_cols != B.n_cols) {
      return false;
    }
    

//  #if defined(ARMA_USE_LAPACK)
//    {
//    Mat<eT> Z1, Z2, T1, T2;
//    
//    const bool status_sd1 = auxlib::schur_dec(Z1, T1, A);
//    const bool status_sd2 = auxlib::schur_dec(Z2, T2, B);
//    
//    if( (status_sd1 == false) || (status_sd2 == false) )
//      {
//      return false;
//      }
    
//    blas_int     m = blas_int(T1.n_rows);
//    blas_int     n = blas_int(T2.n_cols);
//    
//    eT       scale = eT(0);
//    blas_int  info = 0;
//    
//    Mat<eT> Y = trans(Z1) * C * Z2;
//    
//    lapack::trsyl<eT>("N", "N", 1, &m, &n, T1.memptr(), &m, T2.memptr(), &n, Y.memptr(), &m, &scale, &info);
//    
//    //Y /= scale;
//    Y /= (-scale);
//    
//    X = Z1 * Y * trans(Z2);
//    
//    return (info >= 0);
  }

  /**
   * Returns true if all values of the provided matrix are neither NaN nor +/-infinity.
   * 
   * @param X The matrix
   */
  public static boolean is_finite(AbstractMat X) {
    return X.is_finite();
  }

  /**
   * Returns true if the value is neither NaN nor +/-infinity.
   * 
   * @param X The value
   */
  public static boolean is_finite(double X) {
    return (!Double.isInfinite(X) && !Double.isNaN(X));
  }

  public static Col negate(Col X) {
    Col result = new Col(X.n_elem);
    for(int n = 0; n < X.n_elem; n++) {
      result._data[n] = -X._data[n];
    }
    return result;
  }

  public static Row negate(Row X) {
    Row result = new Row(X.n_elem);
    for(int n = 0; n < X.n_elem; n++) {
      result._data[n] = -X._data[n];
    }
    return result;
  }

  public static Mat negate(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    for(int n = 0; n < X.n_elem; n++) {
      result._data[n] = -X._data[n];
    }
    return result;
  }

  public static Col reciprocal(Col X) {
    Col result = new Col(X.n_elem);
    for(int n = 0; n < X.n_elem; n++) {
      result._data[n] = 1 / X._data[n];
    }
    return result;
  }

  public static Row reciprocal(Row X) {
    Row result = new Row(X.n_elem);
    for(int n = 0; n < X.n_elem; n++) {
      result._data[n] = 1 / X._data[n];
    }
    return result;
  }

  public static Mat reciprocal(Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    for(int n = 0; n < X.n_elem; n++) {
      result._data[n] = 1 / X._data[n];
    }
    return result;
  }
  
  protected static void revert(double[] result, double[] X) {
    for (int n = 0; n < X.length / 2; n++) {
      double temp = X[n];
      result[n] = X[X.length - (n + 1)];
      result[X.length - (n + 1)] = temp;
    }
  }
  
  protected static void schur(Mat Z, Mat T, Mat A) {
//    arma_debug_check( (A.is_square() == false), "schur_dec(): given matrix is not square" );
//    
//    if(A.is_empty())
//      {
//      Z.reset();
//      T.reset();
//      return true;
//      }
//    
//    const uword A_n_rows = A.n_rows;
//    
//    Z.set_size(A_n_rows, A_n_rows);
//    T = A;
//    
//    char    jobvs    = 'V';                // get Schur vectors (Z)
//    char     sort    = 'N';                // do not sort eigenvalues/vectors
//    blas_int* select = 0;                  // pointer to sorting function
//    blas_int    n    = blas_int(A_n_rows);
//    blas_int sdim    = 0;                  // output for sorting
//    blas_int lwork   = 3 * ( (std::max)(blas_int(1), 3*n) );
//    blas_int info    = 0;
//    
//    podarray<eT>       work( static_cast<uword>(lwork) );
//    podarray<blas_int> bwork(A_n_rows);
//    
//    podarray<eT> wr(A_n_rows);             // output for eigenvalues
//    podarray<eT> wi(A_n_rows);             // output for eigenvalues
//    
//    lapack::gees(&jobvs, &sort, select, &n, T.memptr(), &n, &sdim, wr.memptr(), wi.memptr(), Z.memptr(), &n, work.memptr(), &lwork, bwork.memptr(), &info);
//    
//    return (info == 0);
  }

}