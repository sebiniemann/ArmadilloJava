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

/**
 * Provides interfaces to non-member functions that are similar to the Armadillo C++ Algebra Library (Armadillo) by
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
public class Arma {

  /**
   * Returns an identity matrix with the specified number of rows and columns.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat eye(int n_rows, int n_cols) throws NegativeArraySizeException {
    // n_rows and n_cols are validated within the constructor
    return new Mat(n_rows, n_cols, Fill.EYE);
  }

  /**
   * Returns a column vector with 100 elements linear increasing from value {@code start} to {@code end} (both
   * including).
   * 
   * @param start The first value
   * @param end The last value
   */
  public static Col linspace(int start, int end) {
    return linspace(start, end, 100);
  }

  /**
   * Returns a column vector with {@code N} elements linear increasing from value {@code start} to {@code end} (both
   * including).
   * 
   * @param start The first value
   * @param end The last value
   * @param N The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code N}) must be positive.
   */
  public static Col linspace(int start, int end, int N) throws NegativeArraySizeException {
    if (N < 0) {
      throw new NegativeArraySizeException("The specified number of elements (" + N + ") must be positive.");
    }

    Col result = new Col(N);

    if (N > 0) {
      double stepLength = (end - start) / (result.n_elem - 1);
      for (int n = 0; n < result.n_elem - 1; n++) {
        // Increasing a value step by step per stepLength might be faster, but also reduces precision
        result._data[n] = start + stepLength * n;
      }

      result._data[N] = end;
    }

    return result;
  }

  /**
   * Returns a column vector of ones with the specified number of elements.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static Col ones(int n_elem) throws NegativeArraySizeException {
    // n_elem is validated within the constructor
    return new Col(n_elem, Fill.ONES);
  }

  /**
   * Returns a matrix of ones with the specified number of rows and columns.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat ones(int n_rows, int n_cols) throws NegativeArraySizeException {
    // n_rows and n_cols are validated within the constructor
    return new Mat(n_rows, n_cols, Fill.ONES);
  }

  /**
   * Returns an matrix with the specified number of elements and values drawn from the discrete uniform distribution [0,
   * Integer.MAX_VALUE - 1].
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static Col randi(int n_elem) throws NegativeArraySizeException {
    return randi(n_elem, new DistrParam(0, Integer.MAX_VALUE - 1));
  }

  /**
   * Returns an matrix with the specified number of elements and values drawn from the discrete uniform distribution [
   * {@code distr_param._a}, {@code distr_param._b}].
   * 
   * @param n_elem The number of elements
   * @param distr_param The distribution parameter
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   * @throws The first end point of the interval must be less than or equal the last end point.
   * @throws RuntimeException The difference between the first end point ({@code distr_param._a}) and the last end point
   *           ({@code distr_param._b}) can be at most Integer.MAX_VALUE - 1.
   */
  public static Col randi(int n_elem, DistrParam distr_param) throws NegativeArraySizeException, RuntimeException {
    if (distr_param._a > distr_param._b) {
      throw new RuntimeException("The first end point of the interval must be less than or equal the last end point.");
    }

    if (distr_param._b - distr_param._a > Integer.MAX_VALUE - 1) {
      throw new RuntimeException("The difference between the first end point (" + distr_param._a + ") and the last end point (" + distr_param._b + ") can be at most Integer.MAX_VALUE - 1.");
    }

    // n_elem is validated within the constructor
    Col result = new Col(n_elem);

    for (int n = 0; n < result.n_elem; n++) {
      result._data[n] = distr_param._a + RNG._rng.nextInt(distr_param._b - distr_param._a + 1);
    }

    return result;
  }

  /**
   * Returns an matrix with the specified number of rows and columns and values drawn from the discrete uniform
   * distribution [0, Integer.MAX_VALUE - 1].
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randi(int n_rows, int n_cols) throws NegativeArraySizeException {
    return randi(n_rows, n_rows, new DistrParam(0, Integer.MAX_VALUE - 1));
  }

  /**
   * Returns an matrix with the specified number of rows and columns and values drawn from the discrete uniform
   * distribution [{@code distr_param._a}, {@code distr_param._b}].
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * @param distr_param The distribution parameter
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   * @throws The first end point of the interval must be less than or equal the last end point.
   * @throws RuntimeException The difference between the first end point ({@code distr_param._a}) and the last end point
   *           ({@code distr_param._b}) can be at most Integer.MAX_VALUE - 1.
   */
  public static Mat randi(int n_rows, int n_cols, DistrParam distr_param) throws NegativeArraySizeException, RuntimeException {
    if (distr_param._a > distr_param._b) {
      throw new RuntimeException("The first end point of the interval must be less than or equal the last end point.");
    }

    if (distr_param._b - distr_param._a > Integer.MAX_VALUE - 1) {
      throw new RuntimeException("The difference between the first end point (" + distr_param._a + ") and the last end point (" + distr_param._b + ") can be at most Integer.MAX_VALUE - 1.");
    }

    // n_rows and n_cols are validated within the constructor
    Mat result = new Mat(n_rows, n_cols);

    for (int n = 0; n < result.n_elem; n++) {
      result._data[n] = distr_param._a + RNG._rng.nextInt(distr_param._b - distr_param._a + 1);
    }

    return result;
  }

  /**
   * Returns a column vector with the specified number of elements and values drawn from the standard uniform
   * distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static Col randu(int n_elem) throws NegativeArraySizeException {
    // n_elem is validated within the constructor
    return new Col(n_elem, Fill.RANDU);
  }

  /**
   * Returns an matrix with the specified number of rows and columns and values drawn from the standard uniform
   * distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randu(int n_rows, int n_cols) throws NegativeArraySizeException {
    // n_rows and n_cols are validated within the constructor
    return new Mat(n_rows, n_cols, Fill.RANDU);
  }

  /**
   * Returns a column vector with the specified number of elements and values drawn from the standard normal
   * distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param n_elem The number of elements
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static Col randn(int n_elem) throws NegativeArraySizeException {
    // n_elem is validated within the constructor
    return new Col(n_elem, Fill.RANDN);
  }

  /**
   * Returns an matrix with the specified number of rows and columns and values drawn from the standard normal
   * distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randn(int n_rows, int n_cols) throws NegativeArraySizeException {
    // n_rows and n_cols are validated within the constructor
    return new Mat(n_rows, n_cols, Fill.RANDN);
  }

  /**
   * Creates a matrix by replicating the matrix by the specified times per row and column.
   * 
   * @param matrix The matrix
   * @param num_copies_per_row The number of copies per row
   * @param num_copies_per_col The number of copies per column
   * 
   * @throws NegativeArraySizeException The specified number of copies per row ({@code num_copies_per_row}) must be positive.
   * @throws NegativeArraySizeException The specified number of copies per column ({@code num_copies_per_col}) must be positive.
   */
  public static Mat repmat(AbstractMat matrix, int num_copies_per_row, int num_copies_per_col) {
    if(num_copies_per_row < 0) {
      throw new NegativeArraySizeException("The specified number of copies per row (" + num_copies_per_row + ") must be positive.");
    }
    
    if(num_copies_per_col < 0) {
      throw new NegativeArraySizeException("The specified number of copies per column (" + num_copies_per_col + ") must be positive.");
    }
    
    Mat result = new Mat(matrix.n_rows * num_copies_per_row, matrix.n_cols * num_copies_per_col);

    // TODO use sub views

    // First, copy alongside the rows
    for (int i = 0; i < num_copies_per_row; i++) {
      new ViewSubMat(result, i * matrix.n_rows, (i + 1) * matrix.n_rows - 1, 0, matrix.n_cols - 1).inPlaceEqual(matrix);
      result.rows(i * matrix.n_rows, (i + 1) * matrix.n_rows - 1, Op.EQUAL, matrix);
    }

    // Secondly, copy alongside the columns
    for (int j = 0; j < num_copies_per_row; j++) {
      System.arraycopy(result._data, 0, result._data, j * result.n_rows * matrix.n_cols, result.n_rows * matrix.n_cols);
    }

    return result;
  }

  /**
   * Creates a toeplitz matrix with {@code The} as the first column of the matrix.
   * 
   * @param vector The vector
   * @return The matrix
   */
  public static Mat toeplitz(AbstractMat vector) {
    // TODO fix
    // vector.isNonVectorDetection();

    if (vector.n_elem > 0) {
      Mat result = new Mat(vector.n_elem, vector.n_elem);

      vector.iteratorReset();
      result.diag(Op.EQUAL, vector._matrix[vector.iteratorNext()]);
      for (int n = 1; n < vector.n_elem; n++) {
        double element = vector._matrix[vector.iteratorNext()];
        result.diag(n, Op.EQUAL, element);
        result.diag(-n, Op.EQUAL, element);
      }

      return result;
    } else {
      return new Mat();
    }
  }

  /**
   * Creates a toeplitz matrix with {@code vector1} as the first column and {@code vector2} the first row of the matrix.
   * 
   * @param vector1 The first vector
   * @param vector2 The second vector
   * @return The matrix
   */
  public static Mat toeplitz(AbstractMat vector1, AbstractMat vector2) {
    // TODO fix
    // vector1.isNonVectorDetection();
    // vector2.isNonVectorDetection();

    if (vector1.n_elem > 0 && vector2.n_elem > 0) {
      Mat result = new Mat(vector1.n_elem, vector2.n_elem);

      vector1.iteratorReset();
      result.diag(Op.EQUAL, vector1._matrix[vector1.iteratorNext()]);
      for (int n = 1; n < vector1.n_elem; n++) {
        result.diag(-n, Op.EQUAL, vector1._matrix[vector1.iteratorNext()]);
      }

      vector2.iteratorReset();
      vector2.iteratorNext(); // Skip this one
      for (int n = 1; n < vector2.n_elem; n++) {
        result.diag(n, Op.EQUAL, vector2._matrix[vector2.iteratorNext()]);
      }

      return result;
    } else {
      return new Mat();
    }
  }

  /**
   * Creates a circulant toeplitz matrix with {@code The} as the first column of the matrix.
   * 
   * @param vector The vector
   * @return The matrix
   */
  public static Mat circ_toeplitz(AbstractMat vector) {
    // TODO fix
    // vector.isNonVectorDetection();

    if (vector.n_elem > 0) {
      Mat result = new Mat(vector.n_elem, vector.n_elem);

      vector.iteratorReset();
      result.diag(Op.EQUAL, vector._matrix[vector.iteratorNext()]);
      for (int n = 1; n < vector.n_elem; n++) {
        double element = vector._matrix[vector.iteratorNext()];
        result.diag(vector.n_elem - n, Op.EQUAL, element);
        result.diag(-n, Op.EQUAL, element);
      }

      return result;
    } else {
      return new Mat();
    }
  }

  /**
   * Returns a null column vector with the specified number of elements.
   * 
   * @param n_elem The number of elements
   */
  public static Col zeros(int n_elem) {
    return new Col(n_elem, Fill.ZEROS);
  }

  /**
   * Returns a zero matrix with the specified number of rows and columns.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   */
  public static Mat zeros(int n_rows, int n_cols) {
    return new Mat(n_rows, n_cols, Fill.ZEROS);
  }
  
}