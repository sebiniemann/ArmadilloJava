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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.QRDecomposition;
import org.ejml.factory.SingularValueDecomposition;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.NormOps;

/**
 * Provides interfaces to non-member functions that are similar to the Armadillo C++ Algebra Library (Armadillo) by
 * Conrad Sanderson et al., based on DenseMatrix64F from Peter Abeles' Efficient Java Matrix Library (EJML) Version 0.23
 * from 21.06.2013.
 * <p>
 * If not stated otherwise (marked as non-canonical), the provided interfaces should be identical to Armadillo (e.g.
 * same ordering of arguments, accepted values, ...). However, this project is based on EJML to provide a pure Java
 * solution, which is why numeric results may slightly differ from the Armadillo C++ Algebra Library.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient Java Matrix Library</a>
 */
public class Arma {

  /**
   * Returns a zero column vector with {@code numberOfElements} elements.
   * 
   * @param numberOfElements The number of elements
   * @return The vector
   */
  public static Mat zeros(int numberOfElements) {
    return zeros(numberOfElements, 1);
  }

  /**
   * Creates a zero matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @return The matrix
   */
  public static Mat zeros(int numberOfRows, int numberOfColumns) {
    return new Mat(numberOfRows, numberOfColumns, Fill.ZEROS);
  }

  /**
   * Returns a column vector of ones with {@code numberOfElements} elements.
   * 
   * @param numberOfElements The number of elements
   * @return The vector
   */
  public static Mat ones(int numberOfElements) {
    return ones(numberOfElements, 1);
  }

  /**
   * Creates a matrix of ones with {@code numberOfRows} rows and {@code numberOfColumns} columns.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @return The matrix
   */
  public static Mat ones(int numberOfRows, int numberOfColumns) {
    return new Mat(numberOfRows, numberOfColumns, Fill.ONES);
  }

  /**
   * Creates a identity of ones with {@code numberOfRows} rows and {@code numberOfColumns} columns.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @return The matrix
   */
  public static Mat eye(int numberOfRows, int numberOfColumns) {
    return new Mat(numberOfRows, numberOfColumns, Fill.EYE);
  }

  /**
   * Creates a column vector with {@code numberOfRows} rows and {@code numberOfColumns} columns and values drawn from
   * the standard uniform distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param numberOfElements The number of elements
   * @param rng The pseudorandom generator
   * @return The vector
   */
  public static Mat randu(int numberOfElements, Random rng) {
    return randu(numberOfElements, 1, rng);
  }

  /**
   * Creates a matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns and values drawn from the
   * standard uniform distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param rng The pseudorandom generator
   * @return The matrix
   */
  public static Mat randu(int numberOfRows, int numberOfColumns, Random rng) {
    return new Mat(numberOfRows, numberOfColumns, Fill.RANDU, rng);
  }

  /**
   * Creates a column vector with {@code numberOfRows} rows and {@code numberOfColumns} columns and values drawn from
   * the standard normal distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param numberOfElements The number of elements
   * @param rng The pseudorandom generator
   * @return The vector
   */
  public static Mat randn(int numberOfElements, Random rng) {
    return randn(numberOfElements, 1, rng);
  }

  /**
   * Creates a matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns and values drawn from the
   * standard normal distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param rng The pseudorandom generator
   * @return The matrix
   */
  public static Mat randn(int numberOfRows, int numberOfColumns, Random rng) {
    return new Mat(numberOfRows, numberOfColumns, Fill.RANDN, rng);
  }

  /**
   * Creates a matrix by copying the matrix {@code numberOfCopiesPerRow} times per row and
   * {@code numberOfCopiesPerColumn} per column.
   * 
   * @param matrix The matrix
   * @param numberOfCopiesPerRow The number of copies per row
   * @param numberOfCopiesPerColumn The number of copies per column
   * @return The matrix
   */
  public static Mat repmat(Mat matrix, int numberOfCopiesPerRow, int numberOfCopiesPerColumn) {
    Mat.isInvalidPositionDetection(numberOfCopiesPerRow);
    Mat.isInvalidPositionDetection(numberOfCopiesPerColumn);

    Mat result = new Mat(matrix.n_rows * numberOfCopiesPerRow, matrix.n_cols * numberOfCopiesPerColumn);

    int srcColumnPointer = 0;
    for (int j = 0; j < matrix.n_cols; j++) {
      int destColumnPointer = j * numberOfCopiesPerColumn * matrix.n_rows;
      for (int copyJ = 0; copyJ < numberOfCopiesPerColumn; copyJ++) {
        for (int copyI = 0; copyI < numberOfCopiesPerRow; copyI++) {
          System.arraycopy(matrix._matrix, srcColumnPointer, result, destColumnPointer, matrix.n_rows);
          destColumnPointer += matrix.n_rows;
        }
        destColumnPointer += numberOfCopiesPerColumn * matrix.n_rows * (matrix.n_cols - 1);
      }
      srcColumnPointer += matrix.n_rows;
    }

    return result;
  }

  /**
   * Creates a toeplitz matrix with {@code The} as the first column of the matrix.
   * 
   * @param vector The vector
   * @return The matrix
   */
  public static Mat toeplitz(Mat vector) {
    vector.isNonVectorDetection();

    if (vector.n_elem > 0) {
      Mat result = new Mat(vector.n_elem, vector.n_elem);

      result.diag(Op.EQUAL, vector._matrix[0]);
      for (int n = 1; n < vector.n_elem; n++) {
        double element = vector._matrix[n];
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
  public static Mat toeplitz(Mat vector1, Mat vector2) {
    vector1.isNonVectorDetection();
    vector2.isNonVectorDetection();

    if (vector1.n_elem > 0 && vector2.n_elem > 0) {
      Mat result = new Mat(vector1.n_elem, vector2.n_elem);

      result.diag(Op.EQUAL, vector1._matrix[0]);
      for (int n = 1; n < vector1.n_elem; n++) {
        result.diag(n, Op.EQUAL, vector1._matrix[n]);
      }
      for (int n = 1; n < vector2.n_elem; n++) {
        result.diag(-n, Op.EQUAL, vector2._matrix[n]);
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
  public static Mat circ_toeplitz(Mat vector) {
    vector.isNonVectorDetection();

    if (vector.n_elem > 0) {
      Mat result = new Mat(vector.n_elem, vector.n_elem);

      result.diag(Op.EQUAL, vector._matrix[0]);
      for (int n = 1; n < vector.n_elem; n++) {
        double element = vector._matrix[n];
        result.diag(vector.n_elem - n, Op.EQUAL, element);
        result.diag(-n, Op.EQUAL, element);
      }

      return result;
    } else {
      return new Mat();
    }
  }

  /**
   * Creates a column vector with 100 elements linear increasing from value {@code a} to {@code b}.
   * 
   * @param startValue The start value
   * @param endValue The end value
   * @return The matrix
   */
  public static Mat linspace(double startValue, double endValue) {
    return linspace(startValue, endValue, 100);
  }

  /**
   * Creates a column vector with {@code numberOfElements} elements linear increasing from value {@code a} to {@code b}.
   * 
   * @param startValue The start value
   * @param endValue The end value
   * @param numberOfElements The number of elements
   * @return The matrix
   */
  public static Mat linspace(double startValue, double endValue, int numberOfElements) {
    Mat.isInvalidPositionDetection(numberOfElements);

    Mat result = new Mat(numberOfElements, 1);

    double stepLength = (endValue - startValue) / numberOfElements;
    for (int n = 0; n < result.n_elem; n++) {
      // Increasing a value step by step by stepLength might be faster, but also reduces its precision
      result._matrix[n] = startValue + stepLength * n;
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed sine of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat sin(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.sin(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed inverse sine of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat asin(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.asin(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed hyperbolic sine of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat sinh(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.sinh(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed inverse hyperbolic sine of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat asinh(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      double element = matrix._matrix[n];
      result._matrix[n] = Math.log(element + Math.sqrt(Math.pow(element, 2) + 1));
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed cosine of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat cos(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.cos(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed inverse cosine of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix.
   */
  public static Mat acos(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.acos(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed hyperbolic cosine of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat cosh(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.cosh(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed inverse hyperbolic cosine of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat acosh(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      double element = matrix._matrix[n];
      result._matrix[n] = Math.log(element + Math.sqrt(element + 1) * Math.sqrt(element - 1));
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed tangent of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat tan(Mat matrix) {
    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.tan(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed inverse tangent of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat atan(Mat matrix) {
    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.atan(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed hyperbolic tangent of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat tanh(Mat matrix) {
    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.tanh(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed inverse hyperbolic tangent of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat atanh(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);
    for (int n = 0; n < matrix.n_elem; n++) {
      double element = matrix._matrix[n];
      result._matrix[n] = 0.5 * Math.log((1 + element) / (1 - element));
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed absolute values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat abs(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.abs(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise determined positive distances between each value and next larger one in
   * magnitude of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat eps(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.ulp(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed base-e exponential values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat exp(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.exp(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed base-2 exponential values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat exp2(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.pow(2, matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed base-10 exponential values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat exp10(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.pow(10, matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed base-e exponential values of the matrix.
   * <p>
   * Values that would result in infinity are truncated to the largest representable number.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat trunc_exp(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      double exp = Math.exp(matrix._matrix[n]);
      if (!Double.isInfinite(exp)) {
        result._matrix[n] = exp;
      } else {
        result._matrix[n] = Double.MAX_VALUE;
      }
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed base-e logarithmic values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat log(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.log(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed base-2 logarithmic values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat log2(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.log(matrix._matrix[n]) / Math.log(2);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed base-10 logarithmic values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat log10(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.log10(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed base-e logarithmic values of the matrix.
   * <p>
   * Values that would result in infinity are truncated to the base-e logarithmic of the largest representable number
   * and values that would result in a complex number are truncated to the base-e logarithmic of the smallest
   * representable number.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat trunc_log(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      double value = matrix._matrix[n];
      if (Double.isInfinite(value)) {
        value = Double.MAX_VALUE;
      } else if (value <= 0) {
        value = Double.MIN_NORMAL;
      }

      result._matrix[n] = Math.log(value);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed raise to power of {@code power} of the matrix.
   * 
   * @param matrix The matrix
   * @param power The power
   * @return The matrix
   */
  public static Mat pow(Mat matrix, int power) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.pow(matrix._matrix[n], power);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise computed square root of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat sqrt(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.sqrt(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise squared values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat square(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.pow(matrix._matrix[n], 2);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise floored values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat floor(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.floor(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise rounded up values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat ceil(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.ceil(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise rounded values of the matrix.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat round(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.round(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Creates a matrix with element-wise determined sign values of the matrix.
   * <p>
   * The sign value is -1 for negative, 0 for zero and 1 for non-negative values.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat sign(Mat matrix) {
    matrix.isEmptyDetection();

    Mat result = new Mat(matrix.n_rows, matrix.n_cols);

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] = Math.signum(matrix._matrix[n]);
    }

    return result;
  }

  /**
   * Returns the sum of all elements of the matrix.
   * 
   * @param matrix The matrix
   * @return The sum
   */
  public static double accu(Mat matrix) {
    matrix.isEmptyDetection();

    double sum = 0;
    for (double element : matrix) {
      sum += element;
    }
    return sum;
  }

  /**
   * Returns the dot product between two vectors.
   * 
   * @param vector1 The first vector
   * @param vector2 The second vector
   * @return The dot product
   */
  public static double dot(Mat vector1, Mat vector2) {
    vector1.isNonVectorDetection();
    vector2.isNonVectorDetection();
    vector1.isEmptyDetection();
    Mat.isNonEqualNumberOfElementsDetection(vector1.n_elem, vector2.n_elem);

    double dotProduct = 0;
    for (int n = 0; n < vector1.n_elem; n++) {
      dotProduct += vector1._matrix[n] * vector2._matrix[n];
    }

    return dotProduct;
  }

  /**
   * Returns the normalised dot product between two vectors.
   * 
   * @param vector1 The first vector
   * @param vector2 The second vector
   * @return The dot product
   * 
   * @throws IllegalArgumentException The vector1 must really be a vector, but was a ({@link Mat#n_rows
   *           selection.n_rows}, {@link Mat#n_cols selection.n_cols})-matrix.
   * @throws IllegalArgumentException The vector2 must really be a vector, but was a ({@code selection.n_rows},
   *           {@code selection.n_cols})-matrix.
   * @throws IllegalArgumentException The number of elements of the left-hand side operand must match with the right
   *           hand side operand, but were {@link Mat#n_elem vector1.n_elem} and {@code vector2.n_elem}.
   */
  public static double norm_dot(Mat vector1, Mat vector2) throws IllegalArgumentException {
    return dot(vector1, vector2) / Math.sqrt(dot(vector1, vector1) * dot(vector2, vector2));
  }

  /**
   * Returns the determinant of the matrix.
   * 
   * @param matrix The matrix
   * @return The determinant
   */
  public static double det(Mat matrix) {
    matrix.isNotSquareDetection();
    matrix.isEmptyDetection();

    return CommonOps.det(Mat.convertMatToEJMLMat(matrix));
  }

  /**
   * Sets {@code value} to the absolute value of the base-e logarithmic determinant and {@sign} to the sign of the
   * determinant of the matrix.
   * <p>
   * Note: The value must be of the mutable type double[] and the sign of the mutable type int[].
   * 
   * @param value The value
   * @param sign The sign
   * @param matrix The matrix
   */
  public static void log_det(double[] value, int[] sign, Mat matrix) {
    double determinant = det(matrix);

    value[0] = Math.log(Math.abs(determinant));
    sign[0] = (int) Math.signum(determinant);
  }

  /**
   * Computes the {@code p}-norm of the matrix.
   * <p>
   * If {@code matrix} is not a vector, an induced norm is computed.
   * 
   * @param matrix The matrix
   * @param p The type
   * @return The norm
   * 
   * @throws IllegalArgumentException For vectors, p must be strict greater than 0, but was {@code p}.
   */
  public static double norm(Mat matrix, int p) throws IllegalArgumentException {
    if (matrix.is_vec()) {
      if (p < 0) {
        throw new IllegalArgumentException("For vectors, p must be strict greater than 0, but was " + p);
      }
    } else {
      if (p != 1 && p != 2) {
        throw new IllegalArgumentException("For non-vector matrices, p must be one of 1 or 2, but was " + p);
      }
    }

    return NormOps.normP(Mat.convertMatToEJMLMat(matrix), p);
  }

  /**
   * Computes the {@code p}-norm of the matrix.
   * <p>
   * If {@code matrix} is not a vector, an induced norm is computed.
   * <p>
   * {@code -inf} stands for the minimum norm, {@code inf} for the maximum norm and {@code fro} for the Frobenius norm.
   * 
   * @param matrix The matrix
   * @param p The type
   * @return The norm
   * 
   * @throws IllegalArgumentException For vectors, p must be one of '-inf', 'inf' or 'fro', but was {@code p}.
   * @throws IllegalArgumentException For non-vector matrices, p must be one of 'inf' or 'fro', but was {@code p}.
   */
  public static double norm(Mat matrix, String p) throws IllegalArgumentException {
    if (matrix.is_vec()) {
      switch (p) {
        case "-inf":
          return CommonOps.elementMinAbs(Mat.convertMatToEJMLMat(matrix));
        case "inf":
          return CommonOps.elementMaxAbs(Mat.convertMatToEJMLMat(matrix));
        case "fro":
          return NormOps.normF(Mat.convertMatToEJMLMat(matrix));
        default:
          throw new IllegalArgumentException("For vectors, p must be one of '-inf', 'inf' or 'fro', but was " + p);
      }
    } else {
      switch (p) {
        case "inf":
          return NormOps.inducedPInf(Mat.convertMatToEJMLMat(matrix));
        case "fro":
          return NormOps.normF(Mat.convertMatToEJMLMat(matrix));
        default:
          throw new IllegalArgumentException("For non-vector matrices, p must be one of 'inf' or 'fro', but was " + p);
      }
    }
  }

  /**
   * Returns the rank of the matrix.
   * <p>
   * All values smaller than {@code max(size(A))*eps(norm(A))} are treated as zero.
   * 
   * @param matrix The matrix
   * @return The rank
   */
  public static double rank(Mat matrix) {
    return rank(matrix, Math.max(matrix.n_rows, matrix.n_cols) * Math.ulp(norm(matrix, 2)));
  }

  /**
   * Returns the rank of the matrix.
   * <p>
   * All values smaller than {@code tolerance} are treated as zero.
   * 
   * @param matrix The matrix
   * @param tolerance The tolerance
   * @return The rank
   */
  public static double rank(Mat matrix, double tolerance) {
    matrix.isEmptyDetection();

    return MatrixFeatures.rank(Mat.convertMatToEJMLMat(matrix));
  }

  /**
   * Returns the trace of the matrix.
   * 
   * @param matrix The matrix
   * @return The trace
   */
  public static double trace(Mat matrix) {
    matrix.isEmptyDetection();

    double trace = matrix._matrix[0];
    for (int n = 1; n < matrix.n_elem; n++) {
      trace += matrix._matrix[matrix.getElementPosition(n, n)];
    }

    return trace;
  }

  /**
   * Converts a (1,1)-matrix into a scalar.
   * 
   * @param matrix The matrix
   * @return The scalar
   * 
   * @throws IllegalArgumentException The matrices must be a (1,1)-matrix, but was a ({@link Mat#n_rows matrix.n_rows},
   *           {@link Mat#n_cols matrix.n_cols})-matrix.
   */
  public static double as_scalar(Mat matrix) throws IllegalArgumentException {
    if (matrix.n_rows != 1 || matrix.n_cols != 1) {
      throw new IllegalArgumentException("The matrices must be a (1,1)-matrix, but was a (" + matrix.n_rows + ", " + matrix.n_cols + ")-matrix.");
    }

    return matrix._matrix[0];
  }

  /**
   * Returns the smallest value of the vector.
   * 
   * @param vector The vector
   * @return The minimum
   */
  public static double min(Mat vector) {
    vector.isEmptyDetection();
    vector.isNonVectorDetection();

    double minimum = vector._matrix[0];
    for (int n = 1; n < vector.n_elem; n++) {
      minimum = Math.min(minimum, vector._matrix[n]);
    }

    return minimum;
  }

  /**
   * Returns the smallest value for each column of the matrix as a column vector.
   * 
   * @param matrix The matrix
   * @return The matrix
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  public static Mat minMat(Mat matrix) {
    return minMat(matrix, 0);
  }

  /**
   * Returns the smallest value for each column ({@code dimension} = 0) or row ({@code dimension} = 1) of the matrix as
   * a column vector.
   * 
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat minMat(Mat matrix, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;

    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result._matrix[j] = min(matrix.col(j));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result._matrix[i] = min(matrix.row(i));
      }
    }

    return result;
  }

  /**
   * Returns the largest value of the vector.
   * 
   * @param vector The vector
   * @return The maximum
   */
  public static double max(Mat vector) {
    vector.isEmptyDetection();
    vector.isNonVectorDetection();

    double maximum = vector._matrix[0];
    for (int n = 1; n < vector.n_elem; n++) {
      maximum = Math.max(maximum, vector._matrix[n]);
    }

    return maximum;
  }

  /**
   * Returns the largest value for each column of the matrix as a column vector.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat maxMat(Mat matrix) {
    return maxMat(matrix, 0);
  }

  /**
   * Returns the largest value for each column ({@code dimension} = 0) or row ({@code dimension} = 1) of the matrix as a
   * column vector.
   * 
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat maxMat(Mat matrix, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;

    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result._matrix[j] = max(matrix.col(j));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result._matrix[i] = max(matrix.row(i));
      }
    }

    return result;
  }

  /**
   * Returns the mean of the vector.
   * 
   * @param vector The vector
   * @return The mean
   */
  public static double mean(Mat vector) {
    vector.isEmptyDetection();
    vector.isNonVectorDetection();

    return sum(vector) / vector.n_elem;
  }

  /**
   * Returns the mean for each column of the matrix as a column vector.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat meanMat(Mat matrix) {
    return meanMat(matrix, 0);
  }

  /**
   * Returns the mean for each column ({@code dimension} = 0) or row ({@code dimension} = 1) of the matrix as a column
   * vector.
   * 
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat meanMat(Mat matrix, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result = sumMat(matrix, dimension);

    int count;
    if (dimension == 0) {
      count = matrix.n_rows;
    } else {
      count = matrix.n_cols;
    }

    for (int n = 0; n < matrix.n_elem; n++) {
      result._matrix[n] /= count;
    }

    return result;
  }

  /**
   * Returns the median of the vector.
   * 
   * @param vector The vector
   * @return The median
   */
  public static double median(Mat vector) {
    vector.isEmptyDetection();
    vector.isNonVectorDetection();

    Mat sortedColumn = sort(vector);

    double median;
    if (vector.n_elem % 2 == 1) {
      int middle = vector.n_elem / 2;
      median = (sortedColumn._matrix[middle - 1] + sortedColumn._matrix[middle]) / 2;
    } else {
      median = sortedColumn._matrix[vector.n_elem / 2];
    }

    return median;
  }

  /**
   * Returns the median for each column of the matrix as a column vector.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat medianMat(Mat matrix) {
    return medianMat(matrix, 0);
  }

  /**
   * Returns the median for each column ({@code dimension} = 0) or row ({@code dimension} = 1) of the matrix as a column
   * vector.
   * 
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat medianMat(Mat matrix, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result._matrix[j] = median(matrix.col(j));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result._matrix[i] = median(matrix.row(i));
      }
    }

    return result;
  }

  /**
   * Returns the standard deviation of the vector.
   * 
   * @param vector The vector
   * @return The standard deviation
   */
  public static double stddev(Mat vector) {
    return stddev(vector, 0);
  }

  /**
   * Returns the standard deviation of the vector.
   * 
   * @param vector The vector
   * @param normType The normalisation
   * @return The standard deviation
   */
  public static double stddev(Mat vector, int normType) {
    return Math.sqrt(var(vector, normType));
  }

  /**
   * Returns the standard deviation for each column of the matrix with normalisation by {@code count} - 1 as a column
   * vector.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat stddevMat(Mat matrix) {
    return stddevMat(matrix, 0, 0);
  }

  /**
   * Returns the standard deviation for each column of the matrix as a column vector.
   * <p>
   * Performs either normalisation by {@code count} - 1 ({@code normType} = 0) or {@code count} ({@code normType} = 1).
   * 
   * @param matrix The matrix
   * @param normType The normalisation
   * @return The matrix
   */
  public static Mat stddevMat(Mat matrix, int normType) {
    return stddevMat(matrix, normType, 0);
  }

  /**
   * Returns the standard deviation for each column ({@code dimension} = 0) or row ({@code dimension} = 1) of the matrix
   * as a column vector.
   * <p>
   * Performs either normalisation by {@code count} - 1 ({@code normType} = 0) or {@code count} ({@code normType} = 1).
   * 
   * @param matrix The matrix
   * @param normType The normalisation
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat stddevMat(Mat matrix, int normType, int dimension) {
    Mat result = varMat(matrix, dimension);

    for (int n = 0; n < result.n_elem; n++) {
      result._matrix[n] = Math.sqrt(result._matrix[n]);
    }

    return result;
  }

  /**
   * Returns the variance of the vector.
   * 
   * @param vector The vector
   * @return The variance
   */
  public static double var(Mat vector) {
    return stddev(vector, 0);
  }

  /**
   * Returns the variance of the vector.
   * 
   * @param vector The vector
   * @param normType The normalisation
   * @return The variance
   */
  public static double var(Mat vector, int normType) {
    vector.isEmptyDetection();
    vector.isNonVectorDetection();
    Mat.isNotInSetDetection(normType, 0, 1);

    double variance = sum(square(vector.minus(mean(vector))));
    if (normType == 0) {
      variance /= (vector.n_elem - 1);
    } else {
      variance /= vector.n_elem;
    }

    return variance;
  }

  /**
   * Returns the variance for each column of the matrix with normalisation by {@code count} - 1 as a column vector.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat varMat(Mat matrix) {
    return varMat(matrix, 0, 0);
  }

  /**
   * Returns the variance for each column of the matrix as a column vector.
   * <p>
   * Performs either normalisation by {@code count} - 1 ({@code normType} = 0) or {@code count} ({@code normType} = 1).
   * 
   * @param matrix The matrix
   * @param normType The normalisation
   * @return The matrix
   */
  public static Mat varMat(Mat matrix, int normType) {
    return varMat(matrix, normType, 0);
  }

  /**
   * Returns the variance for each column ({@code dimension} = 0) or row ({@code dimension} = 1) of the matrix as a
   * column vector.
   * <p>
   * Performs either normalisation by {@code count} - 1 ({@code normType} = 0) or {@code count} ({@code normType} = 1).
   * 
   * @param matrix The matrix
   * @param normType The normalisation
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat varMat(Mat matrix, int normType, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result._matrix[j] = var(matrix.col(j));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result._matrix[i] = var(matrix.row(i));
      }
    }

    return result;
  }

  /**
   * Returns true if any element of the vector is non-zero.
   * 
   * @param vector The vector
   * @return Whether any element is non-zero
   */
  public static boolean any(Mat vector) {
    vector.isNonVectorDetection();

    for (double element : vector) {
      if (element != 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns true if any element of the matrix is non-zero.
   * 
   * @param matrix The matrix
   * @return Whether any element is non-zero
   */
  public static Mat anyMat(Mat matrix) {
    return anyMat(matrix, 0);
  }

  /**
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat anyMat(Mat matrix, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result._matrix[j] = any(matrix.col(j)) ? 1 : 0;
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result._matrix[i] = any(matrix.row(i)) ? 1 : 0;
      }
    }

    return result;
  }

  /**
   * Returns true if all element of the vector are non-zero.
   * 
   * @param vector The vector
   * @return Whether any element is non-zero
   */
  public static boolean all(Mat vector) {
    vector.isNonVectorDetection();

    if (vector.n_elem < 1) {
      return false;
    }

    for (double element : vector) {
      if (element == 0) {
        return false;
      }
    }

    return true;
  }

  /**
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat allMat(Mat matrix) {
    return allMat(matrix, 0);
  }

  /**
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat allMat(Mat matrix, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result._matrix[j] = all(matrix.col(j)) ? 1 : 0;
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result._matrix[i] = all(matrix.row(i)) ? 1 : 0;
      }
    }

    return result;
  }

  /**
   * Returns the product of the vector.
   * 
   * @param vector The vector
   * @return The product
   */
  public static double prod(Mat vector) {
    vector.isEmptyDetection();
    vector.isNonVectorDetection();

    double product = 1;
    for (double element : vector) {
      product *= element;
    }

    return product;
  }

  /**
   * Returns the product of all elements for each column of the matrix as a column vector.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat prodMat(Mat matrix) {
    return prodMat(matrix, 0);
  }

  /**
   * Returns the product of all elements for each column ({@code dimension} = 0) or row ({@code dimension} = 1) of the
   * matrix as a column vector.
   * 
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat prodMat(Mat matrix, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result._matrix[j] = prod(matrix.col(j));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result._matrix[i] = prod(matrix.row(i));
      }
    }

    return result;
  }

  /**
   * Returns the sum of all elements for each column of the matrix as a column vector.
   * 
   * @param vector The vector
   * @return The sum
   */
  public static double sum(Mat vector) {
    return accu(vector);
  }

  /**
   * Returns the sum of all elements for each column of the matrix as a column vector.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat sumMat(Mat matrix) {
    return sumMat(matrix, 0);
  }

  /**
   * Returns the sum of all elements for each column ({@code dimension} = 0) or row ({@code dimension} = 1) of the
   * matrix as a column vector.
   * 
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat sumMat(Mat matrix, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result._matrix[j] = sum(matrix.col(j));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result._matrix[i] = sum(matrix.row(i));
      }
    }

    return result;
  }

  /**
   * Concatenation of the matrices {@code matrix1} and {@code matrix2} along their columns.
   * <p>
   * Matrix {@code matrix1} will be placed above and {@code matrix2} bellow.
   * 
   * @param matrix1 First matrix
   * @param matrix2 Second matrix
   * @return The matrix
   */
  public static Mat join_cols(Mat matrix1, Mat matrix2) {
    Mat.isNonEqualNumberOfElementsDetection(matrix1.n_cols, matrix2.n_cols);

    Mat result = new Mat(matrix1.n_rows + matrix2.n_rows, matrix1.n_cols);
    result.submat(0, 0, matrix1.n_rows - 1, result.n_cols - 1, Op.EQUAL, matrix1);
    result.submat(matrix1.n_rows, 0, result.n_rows - 1, result.n_cols - 1, Op.EQUAL, matrix2);

    return result;
  }

  /**
   * Concatenation of the matrices {@code matrix1} and {@code matrix2} along their columns.
   * <p>
   * Matrix {@code matrix1} will be placed above and {@code matrix2} bellow.
   * 
   * @param matrix1 First matrix
   * @param matrix2 Second matrix
   * @return The matrix
   * 
   * @throws IllegalArgumentException The provided matrices must have the same number of columns, but were
   *           {@link Mat#n_cols matrix1.n_cols} and {@code matrix2.n_cols}.
   */
  public static Mat join_vert(Mat matrix1, Mat matrix2) {
    return join_cols(matrix1, matrix2);
  }

  /**
   * Concatenation of the matrices {@code matrix1} and {@code matrix2} along their rows.
   * <p>
   * Matrix {@code matrix1} will be placed left and {@code matrix2} right.
   * 
   * @param matrix1 First matrix
   * @param matrix2 Second matrix
   * @return The matrix
   */
  public static Mat join_rows(Mat matrix1, Mat matrix2) {
    Mat.isNonEqualNumberOfElementsDetection(matrix1.n_rows, matrix2.n_rows);

    Mat result = new Mat(matrix1.n_rows, matrix1.n_cols + matrix2.n_cols);
    result.submat(0, 0, result.n_rows - 1, matrix1.n_cols - 1, Op.EQUAL, matrix1);
    result.submat(0, matrix1.n_cols, result.n_rows - 1, result.n_cols - 1, Op.EQUAL, matrix2);

    return result;
  }

  /**
   * Concatenation of the matrices {@code matrix1} and {@code matrix2} along their rows.
   * <p>
   * Matrix {@code matrix1} will be placed left and {@code matrix2} right.
   * 
   * @param matrix1 First matrix
   * @param matrix2 Second matrix
   * @return The matrix
   */
  public static Mat join_horiz(Mat matrix1, Mat matrix2) {
    return join_rows(matrix1, matrix2);
  }

  /**
   * Sorts the vector in ascending order.
   * 
   * @param vector The vector
   * @return The vector
   */
  public static Mat sort(Mat vector) {
    return sort(vector, 0);
  }

  /**
   * Sorts the vector either in ascending ({@code sortType} = 0) or descending ({@code sortType} = 1) order.
   * 
   * @param vector The vector
   * @param sortType The sortType
   * @return The vector
   */
  public static Mat sort(Mat vector, int sortType) {
    vector.isNonVectorDetection();
    Mat.isNotInSetDetection(sortType, 0, 1);

    Mat result = new Mat(vector);
    Arrays.sort(result._matrix);

    if (sortType == 1) {
      // Why is there no native implementation of a descending sort on primitives in Java ...
      for (int n = 0; n < vector.n_elem / 2; n++) {
        double temp = result._matrix[n];
        result._matrix[n] = result._matrix[vector.n_elem - (n + 1)];
        result._matrix[vector.n_elem - (n + 1)] = temp;
      }
    }

    return result;
  }

  /**
   * Sorts the matrix either in ascending order per column.
   * 
   * @param matrix The matrix
   * @return The matrix
   */
  public static Mat sortMat(Mat matrix) {
    return sortMat(matrix, 0, 0);
  }

  /**
   * Sorts the matrix either in ascending ({@code sortType} = 0) or descending ({@code sortType} = 1) order per column.
   * 
   * @param matrix The matrix
   * @param sortType The sortType
   * @return The matrix
   */
  public static Mat sortMat(Mat matrix, int sortType) {
    return sortMat(matrix, sortType, 0);
  }

  /**
   * Sorts the matrix either in ascending ({@code sortType} = 0) or descending ({@code sortType} = 1) order either per
   * column ({@code sortType} = 0) or row ({@code sortType} = 1).
   * 
   * @param matrix The matrix
   * @param sortType The sortType
   * @param dimension The dimension
   * @return The matrix
   */
  public static Mat sortMat(Mat matrix, int sortType, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result.col(j, Op.EQUAL, sort(matrix.col(j), sortType));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result.row(i, Op.EQUAL, sort(matrix.row(i), sortType));
      }
    }

    return result;
  }

  /**
   * 
   * 
   * @param vector The vector
   * @return The vector
   */
  public static Mat sort_index(Mat vector) {
    return sort_index(vector, 0);
  }

  /**
   * 
   * 
   * @param vector The vector
   * @param sortType The sortType
   * @return The vector
   */
  public static Mat sort_index(Mat vector, int sortType) {
    return stable_sort_index(vector, sortType);
  }

  /**
   * 
   * 
   * @param vector The vector
   * @return The vector
   */
  public static Mat stable_sort_index(Mat vector) {
    return stable_sort_index(vector, 0);
  }

  /**
   * 
   * 
   * @param vector The vector
   * @param sortType The sortType
   * @return The vector
   */
  public static Mat stable_sort_index(Mat vector, int sortType) {
    vector.isNonVectorDetection();
    Mat.isNotInSetDetection(sortType, 0, 1);

    // TreeMap will do the job, nothing to do here – great!
    TreeMap<Double, List<Integer>> map = new TreeMap<Double, List<Integer>>();
    for (int i = 0; i < vector.n_elem; i++) {
      List<Integer> index = map.get(vector._matrix[i]);
      if (index == null) {
        index = new ArrayList<Integer>();
        map.put(vector._matrix[i], index);
      }
      index.add(i);
    }

    Mat result = new Mat();
    result.copy_size(vector);
    int position = 0;
    for (List<Integer> indices : map.values()) {
      for (int index : indices) {
        result._matrix[position++] = index;
      }
    }

    if (sortType == 1) {
      // Why is there no native implementation of a descending sort on primitives in Java ...
      for (int n = 0; n < vector.n_elem / 2; n++) {
        double temp = result._matrix[n];
        result._matrix[n] = result._matrix[vector.n_elem - (n + 1)];
        result._matrix[vector.n_elem - (n + 1)] = temp;
      }
    }

    return result;
  }

  /**
   * @param matrix The matrix
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of rows
   * @return
   */
  public static Mat reshape(Mat matrix, int numberOfRows, int numberOfColumns) {
    return reshape(matrix, numberOfRows, numberOfColumns, 0);
  }

  /**
   * @param matrix The matrix
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param dimension The dimension
   * @return
   */
  public static Mat reshape(Mat matrix, int numberOfRows, int numberOfColumns, int dimension) {
    Mat temp = new Mat(matrix);
    temp.reshape(numberOfRows, numberOfColumns, dimension);
    return temp;
  }

  /**
   * @param matrix The matrix
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @return
   */
  public static Mat resize(Mat matrix, int numberOfRows, int numberOfColumns) {
    Mat temp = new Mat(matrix);
    temp.resize(numberOfRows, numberOfColumns);
    return temp;
  }

  /**
   * @param matrix The matrix
   * @return
   */
  public static Mat cor(Mat matrix) {
    return cor(matrix, 0);
  }

  /**
   * @param matrix
   * @param normType
   * @return
   */
  public static Mat cor(Mat matrix, int normType) {
    return cor(matrix, matrix, normType);
  }

  /**
   * @param matrix1
   * @param matrix2
   * @return
   */
  public static Mat cor(Mat matrix1, Mat matrix2) {
    return cor(matrix1, matrix2, 0);
  }

  /**
   * @param matrix1
   * @param matrix2
   * @param normType
   * @return
   */
  public static Mat cor(Mat matrix1, Mat matrix2, int normType) {
    return null;
  }

  /**
   * @param matrix
   * @return
   */
  public static Mat cov(Mat matrix) {
    return cov(matrix, matrix, 0);
  }

  /**
   * @param matrix
   * @param normType
   * @return
   */
  public static Mat cov(Mat matrix, int normType) {
    return null;
  }

  /**
   * @param matrix1
   * @param matrix2
   * @return
   */
  public static Mat cov(Mat matrix1, Mat matrix2) {
    return cov(matrix1, matrix2, 0);
  }

  /**
   * @param matrix1
   * @param matrix2
   * @param normType
   * @return
   */
  public static Mat cov(Mat matrix1, Mat matrix2, int normType) {
    return null;
  }

  /**
   * @param matrix
   * @return
   */
  public static Mat hist(Mat matrix) {
    return hist(matrix, 10);
  }

  /**
   * @param matrix
   * @param numberOfBins
   * @return
   */
  public static Mat hist(Mat matrix, int numberOfBins) {
    return null;
  }

  /**
   * @param matrix
   * @return
   */
  public static Mat histMat(Mat matrix) {
    return histMat(matrix, 10);
  }

  /**
   * @param matrix
   * @param numberOfBins
   * @return
   */
  public static Mat histMat(Mat matrix, int numberOfBins) {
    return histMat(matrix, numberOfBins, 0);
  }

  /**
   * @param matrix
   * @param numberOfBins
   * @param dimension
   * @return
   */
  public static Mat histMat(Mat matrix, int numberOfBins, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result.col(j, Op.EQUAL, hist(matrix.col(j), numberOfBins));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result.row(i, Op.EQUAL, hist(matrix.row(i), numberOfBins));
      }
    }

    return result;
  }

  /**
   * @param matrix
   * @param centers
   * @return
   */
  public static Mat hist(Mat matrix, Mat centers) {
    return null;
  }

  /**
   * @param matrix
   * @param centers
   * @return
   */
  public static Mat histMat(Mat matrix, Mat centers) {
    return histMat(matrix, centers, 0);
  }

  /**
   * @param matrix
   * @param centers
   * @param dimension
   * @return
   */
  public static Mat histMat(Mat matrix, Mat centers, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result.col(j, Op.EQUAL, hist(matrix.col(j), centers));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result.row(i, Op.EQUAL, hist(matrix.row(i), centers));
      }
    }

    return result;
  }

  /**
   * @param matrix
   * @param edges
   * @return
   */
  public static Mat histc(Mat matrix, Mat edges) {
    return null;
  }

  /**
   * @param matrix
   * @param edges
   * @return
   */
  public static Mat histcMat(Mat matrix, Mat edges) {
    return histcMat(matrix, edges, 0);
  }

  /**
   * @param matrix
   * @param edges
   * @param dimension
   * @return
   */
  public static Mat histcMat(Mat matrix, Mat edges, int dimension) {
    matrix.isEmptyDetection();
    Mat.isNotInSetDetection(dimension, 0, 1);

    Mat result;
    if (dimension == 0) {
      result = new Mat(matrix.n_cols, 1);
      for (int j = 0; j < matrix.n_cols; j++) {
        result.col(j, Op.EQUAL, histc(matrix.col(j), edges));
      }
    } else {
      result = new Mat(matrix.n_rows, 1);
      for (int i = 0; i < matrix.n_rows; i++) {
        result.row(i, Op.EQUAL, histc(matrix.row(i), edges));
      }
    }

    return result;
  }

  /**
   * @param A
   * @return
   */
  public static Mat fliplr(Mat A) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat flipud(Mat A) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat diagmat(Mat A) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat trimatu(Mat A) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat trimatl(Mat A) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat simmatu(Mat A) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat simmatl(Mat A) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat trans(Mat A) {
    return null;
  }

  /**
   * Creates a column vector containing indices of all non-zero elements. Contains all indices of elements that satisfy
   * an operation if used together with {@link Op#evaluate(Mat, Op, Mat)}.
   * 
   * @param matrix The provided matrix.
   * @return The created column vector.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code k} is negative or strict greater then
   *           {@code a.}{@link Mat#n_elem n_elem} as well as if {@code s} is neither 'first' nor 'last'.
   * 
   * @see #find(Mat, int, String)
   */
  public static Mat find(Mat matrix) throws IllegalArgumentException {
    return find(matrix, 0, "first");
  }

  /**
   * Creates a column vector containing indices of all non-zero elements. Contains all indices of elements that satisfy
   * an operation if used together with {@link Op#evaluate(Mat, Op, Mat)}.
   * 
   * @param matrix The provided matrix.
   * @param k The number of elements to be evaluated.
   * @return The created column vector.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code k} is negative or strict greater then
   *           {@code a.}{@link Mat#n_elem n_elem} as well as if {@code s} is neither 'first' nor 'last'.
   * 
   * @see #find(Mat, int, String)
   */
  public static Mat find(Mat matrix, int k) throws IllegalArgumentException {
    return find(matrix, k, "first");
  }

  /**
   * Creates a column vector containing indices of all non-zero elements. Contains all indices of elements that satisfy
   * an operation if used together with {@link Op#evaluate(Mat, Op, Mat)}.
   * <p>
   * Contains all {@code k = 0} (default) or the first ({@code s = "first"}, default) respectively last (
   * {@code s = "last"}) {@code k} indices at most.
   * <p>
   * Only relational operators are supported.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>The stored indices are of type double.
   * <li>The contained indices are either ordered from smallest to largest ({@code s = "first"}) or vice versa (
   * {@code s = "last"}).
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code k} is negative or strict greater then
   * {@code a.}{@link Mat#n_elem n_elem}.
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code s} is neither 'first' nor 'last'.
   * </ul>
   * 
   * @param matrix The provided matrix.
   * @param k The number of elements to be evaluated.
   * @param s The element to to begin with.
   * @return The created column vector.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code k} is negative or strict greater then
   *           {@code a.}{@link Mat#n_elem n_elem} as well as if {@code s} is neither 'first' nor 'last'.
   */
  public static Mat find(Mat matrix, int k, String s) throws IllegalArgumentException {
    if (k < 0 || k > matrix.n_elem) {
      throw new IllegalArgumentException("The parameter k must be non-negative and (non-strict) lower than n_elem = " + matrix.n_elem + ".");
    }

    if (!s.equals("first") && !s.equals("last")) {
      throw new IllegalArgumentException("The parameter s needs to be either 'first' or 'last' but was " + s);
    }

    DenseMatrix64F result = new DenseMatrix64F(matrix.n_elem, 1);

    int index = 0;
    if (s.equals("first")) {
      int limit;
      if (k > 0) {
        limit = k;
      } else {
        limit = matrix.n_elem;
      }

      for (int i = 0; i < limit; i++) {
        if (matrix.at(i) != 0) {
          result.set(index, i);
          index++;
        }
      }
    } else {
      int limit;
      if (k > 0) {
        limit = matrix.n_elem - 1 - k;
      } else {
        limit = -1;
      }

      for (int i = matrix.n_elem - 1; i > limit; i--) {
        if (matrix.at(i) != 0) {
          result.set(index, i);
          index++;
        }
      }
    }

    if (index > 0) {
      // Saves current values of the elements since length <= n_elem
      result.reshape(index, 1);
    } else {
      result = new DenseMatrix64F();
    }
    return Mat.convertEJMLToMat(result);
  }

  /**
   * Returns the main diagonal of the matrix as a column vector.
   * 
   * @param matrix The matrix
   * @return The diagonal
   */
  public static Mat diagvec(Mat matrix) {
    return diagvec(matrix, 0);
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
   * @param matrix The matrix
   * @param k The diagonal position
   * @return The diagonal
   */
  public static Mat diagvec(Mat matrix, int k) {
    return matrix.diag(k);
  }

  /**
   * @param matrix
   * @return
   */
  public static Mat cumsum(Mat matrix) {
    return cumsum(matrix, 0);
  }

  /**
   * @param matrix
   * @param dimension
   * @return
   */
  public static Mat cumsum(Mat matrix, int dimension) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @return
   */
  public static Mat conv(Mat A, Mat B) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @return
   */
  public static Mat cross(Mat A, Mat B) {
    if (!A.is_vec() || !B.is_vec()) {

    }

    if (A.n_elem != 3 || B.n_elem != 3) {

    }

    return new Mat(new double[][]{{
      A.at(1) * B.at(2) - A.at(2) * B.at(1)
    }, {
      A.at(2) * B.at(0) - A.at(0) * B.at(2)
    }, {
      A.at(0) * B.at(1) - A.at(1) * B.at(0)
    }});
  }

  /**
   * @param A
   * @param B
   * @return
   */
  public static Mat kron(Mat A, Mat B) {
    return null;
  }

  /**
   * @param matrix
   * @return
   */
  public static Mat shuffle(Mat matrix) {
    return shuffle(matrix, 0);
  }

  /**
   * @param matrix
   * @param dimension
   * @return
   */
  public static Mat shuffle(Mat matrix, int dimension) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat unique(Mat A) {
    return null;
  }

  /**
   * @param matrix The matrix
   * @return The vector
   */
  public static Mat vectorise(Mat matrix) {
    return vectorise(matrix, 0);
  }

  /**
   * @param matrix The matrix
   * @param dimension The dimension
   * @return The vector
   */
  public static Mat vectorise(Mat matrix, int dimension) {
    Mat.isNotInSetDetection(dimension, 0, 1);

    if (dimension == 0) {
      return reshape(matrix, matrix.n_elem, 1);
    } else {
      return reshape(matrix, 1, matrix.n_elem);
    }
  }

  /**
   * @param X
   * @return
   */
  public static Mat chol(Mat X) {
    return null;
  }

  /**
   * @param R
   * @param X
   * @return
   */
  public static Mat chol(Mat R, Mat X) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat eig_sym(Mat A) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @return
   */
  public static Mat eig_sym(Mat A, Mat B) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @param C
   * @return
   */
  public static Mat eig_sym(Mat A, Mat B, Mat C) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat inv(Mat A) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @return
   */
  public static Mat inv(Mat A, Mat B) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @param C
   * @return
   */
  public static Mat lu(Mat A, Mat B, Mat C) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @param C
   * @param D
   * @return
   */
  public static Mat lu(Mat A, Mat B, Mat C, Mat D) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat pinv(Mat A) {
    return null;
  }

  /**
   * @param A
   * @param tolerance
   * @return
   */
  public static Mat pinv(Mat A, double tolerance) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @return
   */
  public static Mat pinv(Mat A, Mat B) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @param tolerance
   * @return
   */
  public static Mat pinv(Mat A, Mat B, double tolerance) {
    return null;
  }

  /**
   * @param A
   * @return
   */
  public static Mat princomp(Mat A) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @return
   */
  public static Mat princomp(Mat A, Mat B) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @param C
   * @return
   */
  public static Mat princomp(Mat A, Mat B, Mat C) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @param C
   * @param D
   * @return
   */
  public static Mat princomp(Mat A, Mat B, Mat C, Mat D) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @param C
   * @param D
   * @param E
   * @return
   */
  public static Mat princomp(Mat A, Mat B, Mat C, Mat D, Mat E) {
    return null;
  }

  /**
   * Performs a <a href="http://en.wikipedia.org/wiki/QR_decomposition">QR decomposition</a> on the matrix {@code x}.
   * <p>
   * The matrix {@code x} is decomposed into a orthogonal matrix {@code q} and upper triangular matrix {@code r}, such
   * that {@code x = qr}.
   * <p>
   * The provided matrices {@code q} and {@code r} are not touched if the decomposition fails.
   * 
   * @param q An orthogonal matrix.
   * @param r An upper triangular matrix.
   * @param x The matrix to be decomposed.
   * @return False if the decomposition fails and true otherwise.
   */
  public static boolean qr(Mat q, Mat r, Mat x) {
    QRDecomposition<DenseMatrix64F> qr = DecompositionFactory.qr(x.n_rows, x.n_cols);
    qr.decompose(Mat.convertMatToEJMLMat(x));

    DenseMatrix64F tempQ;
    DenseMatrix64F tempR;
    try {
      tempQ = qr.getQ(null, false);
      tempR = qr.getR(null, false);
    } catch(IllegalArgumentException exception) {
      return false;
    }

    q.set_size(tempQ.numRows, tempQ.numCols);
    q = Mat.convertEJMLToMat(tempQ);

    r.set_size(tempR.numRows, tempR.numCols);
    r = Mat.convertEJMLToMat(tempR);

    return true;
  }

  /**
   * Solves a system of linear equations {@code ax = b} with unknown {@code x}.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if the number of rows in {@code a} and
   * {@code b} are unequal.
   * 
   * @param a The matrix {@code a}.
   * @param b The matrix {@code b}.
   * @return The matrix {@code x}.
   * 
   * @throws RuntimeException Thrown if the algorithm was unable to solve the provided matrix.
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the number of rows in {@code a} and {@code b} are
   *           unequal.
   */
  public static Mat solve(Mat a, Mat b) throws RuntimeException, IllegalArgumentException {
    if (a.n_rows != b.n_rows) {
      throw new IllegalArgumentException("The provided matrices must have the same number of rows (" + a.n_rows + " != " + b.n_rows + ").");
    }

    DenseMatrix64F x = new DenseMatrix64F();

    if (!CommonOps.solve(Mat.convertMatToEJMLMat(a), Mat.convertMatToEJMLMat(b), x)) {
      throw new RuntimeException("The algorithm was unable to solve the matrix.");
    };

    return Mat.convertEJMLToMat(x);
  }

  /**
   * Solves a system of linear equations {@code ax = b} with unknown {@code x}.
   * <p>
   * Return false if no solution was found and true otherwise. The provided matrices {@code x} is not touched if the
   * decomposition fails.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if the number of rows in {@code a} and
   * {@code b} are unequal.
   * 
   * @param a The matrix {@code a}.
   * @param b The matrix {@code b}.
   * @param x The matrix {@code x}.
   * @return The boolean value.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the number of rows in {@code a} and {@code b} are
   *           unequal.
   */
  public static boolean solve(Mat x, Mat a, Mat b) throws IllegalArgumentException {
    try {
      x = solve(a, b);
    } catch(RuntimeException exception) {
      return false;
    }
    return true;
  }

  /**
   * Performs a <a href="http://en.wikipedia.org/wiki/Singular_value_decomposition">singular value decomposition</a> on
   * the matrix {@code x}.
   * <p>
   * The matrix {@code x} is decomposed into vector {@code s} such that {@code x = u * diagmat(s) * v'}. The matrices
   * {@code u} and {@code v} are not returned.
   * 
   * @param x The matrix to be decomposed.
   * @return The matrix {@code s}.
   * 
   * @throws RuntimeException Thrown if the algorithm was unable to solve the provided matrix.
   */
  public static Mat svd(Mat x) throws RuntimeException {
    SingularValueDecomposition<DenseMatrix64F> svd = DecompositionFactory.svd(x.n_rows, x.n_cols, false, false, false);
    svd.decompose(Mat.convertMatToEJMLMat(x));

    DenseMatrix64F s;
    try {
      s = svd.getW(null);
      // check catch
    } catch(IllegalArgumentException exception) {
      throw new RuntimeException("The algorithm was unable to decompose the matrix.");
    }

    return Mat.convertEJMLToMat(s);
  }

  /**
   * Performs a <a href="http://en.wikipedia.org/wiki/Singular_value_decomposition">singular value decomposition</a> on
   * the matrix {@code x}.
   * <p>
   * The matrix {@code x} is decomposed into vector {@code s} such that {@code x = u * diagmat(s) * v'}. The matrices
   * {@code u} and {@code v} are not returned.
   * <p>
   * The provided matrix {@code s} is not touched if the decomposition fails.
   * 
   * @param s The matrix {@code s}.
   * @param x The matrix to be decomposed.
   * @return False if the decomposition fails and true otherwise.
   */
  public static boolean svd(Mat s, Mat x) {
    try {
      s = svd(x);
    } catch(RuntimeException exception) {
      return false;
    }
    return true;
  }

  /**
   * Performs a <a href="http://en.wikipedia.org/wiki/Singular_value_decomposition">singular value decomposition</a> on
   * the matrix {@code x}.
   * <p>
   * The matrix {@code x} is decomposed into matrix {@code u}, {@code v} and a vector {@code s} such that
   * {@code x = u * diagmat(s) * v'}.
   * <p>
   * The provided matrices {@code u}, {@code s} and {@code v} are not touched if the decomposition fails.
   * 
   * @param u The matrix {@code u}.
   * @param s The matrix {@code s}.
   * @param v The matrix {@code v}.
   * @param x The matrix to be decomposed.
   * @return False if the decomposition fails and true otherwise.
   */
  public static boolean svd(Mat u, Mat s, Mat v, Mat x) {
    SingularValueDecomposition<DenseMatrix64F> svd = DecompositionFactory.svd(x.n_rows, x.n_cols, true, true, false);
    svd.decompose(Mat.convertMatToEJMLMat(x));

    DenseMatrix64F tempU;
    DenseMatrix64F tempS;
    DenseMatrix64F tempV;
    try {
      tempU = svd.getU(null, false);
      tempS = svd.getW(null);
      tempV = svd.getV(null, false);
    } catch(IllegalArgumentException exception) {
      return false;
    }

    u.set_size(tempU.numRows, tempU.numCols);
    u = Mat.convertEJMLToMat(tempU);

    s.set_size(tempS.numRows, tempS.numCols);
    s = Mat.convertEJMLToMat(tempS);

    v.set_size(tempV.numRows, tempV.numCols);
    v = Mat.convertEJMLToMat(tempV);

    return true;
  }

  /**
   * @param A
   * @param B
   * @param C
   * @return
   */
  public static Mat syl(Mat A, Mat B, Mat C) {
    return null;
  }

  /**
   * @param A
   * @param B
   * @param C
   * @param D
   * @return
   */
  public static Mat syl(Mat A, Mat B, Mat C, Mat D) {
    return null;
  }

  /**
   * Returns true if the matrix contains only finite values and false otherwise.
   * 
   * @param matrix The matrix
   * @return Whether the matrix is finite
   */
  public static boolean is_finite(Mat matrix) {
    return matrix.is_finite();
  }

  /**
   * Returns true if the value is neither NaN nor +/-infinity.
   * 
   * @param value The value
   * @return Whether the value is finite
   */
  public static boolean is_finite(double value) {
    return (!Double.isInfinite(value) && !Double.isNaN(value));
  }
}