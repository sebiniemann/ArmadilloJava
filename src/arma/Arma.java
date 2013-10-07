package arma;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.QRDecomposition;
import org.ejml.factory.SingularValueDecomposition;
import org.ejml.ops.CommonOps;
import org.ejml.ops.NormOps;

/**
 * Provides interfaces to non-member functions that are similar to the Armadillo C++ Algebra Library (Armadillo) by
 * Conrad Sanderson et al., based on DenseMatrix64F from Peter Abeles’ Efficient Java Matrix Library (EJML) Version 0.23
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
   * Computes the determinant of the provided matrix.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown instead of C++'s std::logic_error if
   * the provided matrix is not square.
   * 
   * @param matrix The provided matrix.
   * @return The determinant.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if the provided matrix is not square.
   */
  public static double det(Mat matrix) throws IllegalArgumentException {
    if (!matrix.is_square()) {
      throw new IllegalArgumentException("The provided matrix needs to be square .");
    }

    return CommonOps.det(matrix.memptr());
  }

  /**
   * Creates a matrix with element-wise computed absolute values of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat abs(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.memptr());
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      double element = memptr.get(i);

      if (element < 0) {
        result.set(i, -element);
      }
    }

    return new Mat(result);
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
    qr.decompose(x.memptr());

    DenseMatrix64F tempQ;
    DenseMatrix64F tempR;
    try {
      tempQ = qr.getQ(null, false);
      tempR = qr.getR(null, false);
    } catch(IllegalArgumentException exception) {
      return false;
    }

    q.set_size(tempQ.numRows, tempQ.numCols);
    q.memptr().set(tempQ);

    r.set_size(tempR.numRows, tempR.numCols);
    r.memptr().set(tempR);

    return true;
  }

  /**
   * Creates a matrix with element-wise floored values of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat floor(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.floor(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Computes the sum of all elements of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The sum.
   */
  public static double accu(Mat matrix) {
    return CommonOps.elementSum(matrix.memptr());
  }

  /**
   * Creates a matrix with element-wise squared values of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat square(Mat matrix) {
    return matrix.elemTimes(matrix);
  }

  /**
   * Creates a matrix with element-wise computed square root of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat sqrt(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.sqrt(memptr.get(i)));
    }

    return new Mat(result);
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
    return new Mat(result);
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
   * 
   * @param matrix The provided matrix.
   * @param s The element to to begin with.
   * @return The created column vector.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code k} is negative or strict greater then
   *           {@code a.}{@link Mat#n_elem n_elem} as well as if {@code s} is neither 'first' nor 'last'.
   * 
   * @see #find(Mat, int, String)
   */
  public static Mat find(Mat matrix, String s) throws IllegalArgumentException {
    return find(matrix, 0, s);
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
   * Returns true if any element of the provided matrix is non-zero or if an operation is satisfy if used
   * together with {@link Op#evaluate(Mat, Op, Mat)}.
   * 
   * @param matrix The provided matrix.
   * @return The boolean result.
   */
  public static boolean any(Mat matrix) {
    DenseMatrix64F memptrA = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      if (memptrA.get(i) != 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Computes the {@code p}-norm of the provided matrix.
   * <p>
   * If {@code matrix} is not a vector, an induced norm is computed.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code matrix} is a vector and {@code p} is not
   * strict greater than 0.
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code matrix} is not a vector and {@code p} is not
   * one of 1, 2.
   * </ul>
   * 
   * @param matrix The provided matrix.
   * @param p The type of the norm.
   * @return The norm.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code matrix} is a vector and {@code p} is not
   *           strict greater than 0or if {@code matrix} is not a vector and {@code p} is not one of 1, 2.
   */
  public static double norm(Mat matrix, int p) throws IllegalArgumentException {
    if (matrix.is_vec()) {
      if (p < 0) {
        throw new IllegalArgumentException("For vectors, p must be strict greater than 0 but was " + p);
      }
    } else {
      if (p != 1 && p != 2) {
        throw new IllegalArgumentException("For non-vector matrices, p must be one of 1 or 2 but was" + p);
      }
    }

    return NormOps.normP(matrix.memptr(), p);
  }

  /**
   * Computes the {@code p}-norm of the provided matrix.
   * <p>
   * If {@code matrix} is not a vector, an induced norm is computed.
   * <p>
   * "-inf" stand for the minimum norm, "inf" for the maximum norm and "fro" for the Frobenius norm.
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code matrix} is a vector and {@code p} is not one
   * of '-inf', 'inf' or 'fro'.
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code matrix} is not a vector and {@code p} is not
   * one of 'inf' or 'fro'.
   * </ul>
   * 
   * @param matrix The provided matrix.
   * @param p The type of the norm.
   * @return The norm.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code matrix} is a vector and {@code p} is not
   *           one of '-inf', 'inf' or 'fro' or if {@code matrix} is not a vector and {@code p} is not one of 'inf' or
   *           'fro'.
   * 
   * @see #norm(Mat, int)
   */
  public static double norm(Mat matrix, String p) throws IllegalArgumentException {
    if (matrix.is_vec()) {
      switch (p) {
        case "-inf":
          return CommonOps.elementMinAbs(matrix.memptr());
        case "inf":
          return CommonOps.elementMaxAbs(matrix.memptr());
        case "fro":
          return NormOps.normF(matrix.memptr());
        default:
          throw new IllegalArgumentException("For vectors, p must be one of '-inf', 'inf' or 'fro' but was " + p);
      }
    } else {
      switch (p) {
        case "inf":
          return NormOps.inducedPInf(matrix.memptr());
        case "fro":
          return NormOps.normF(matrix.memptr());
        default:
          throw new IllegalArgumentException("For non-vector matrices, p must be one of 'inf' or 'fro' but was" + p);
      }
    }
  }

  /**
   * Creates a matrix with element-wise computed cosine of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat cos(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.cos(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed inverse cosine of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat acos(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.acos(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed hyperbolic cosine of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat cosh(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.cosh(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed inverse hyperbolic cosine of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat acosh(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      double value = memptr.get(i);
      result.set(i, Math.log(value + Math.sqrt(value + 1) * Math.sqrt(value - 1)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed sine of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat sin(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.sin(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed inverse sine of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat asin(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.asin(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed hyperbolic sine of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat sinh(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.sinh(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed inverse hyperbolic sine of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat asinh(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      double value = memptr.get(i);
      result.set(i, Math.log(value + Math.sqrt(Math.pow(value, 2) + 1)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed tangent of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat tan(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.tan(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed inverse tangent of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat atan(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.atan(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed hyperbolic tangent of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat tanh(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.tanh(memptr.get(i)));
    }

    return new Mat(result);
  }

  /**
   * Creates a matrix with element-wise computed inverse hyperbolic tangent of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat atanh(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
    DenseMatrix64F memptr = matrix.memptr();

    for (int i = 0; i < matrix.n_elem; i++) {
      double value = memptr.get(i);
      result.set(i, 0.5 * Math.log((1 + value) / (1 - value)));
    }

    return new Mat(result);
  }

  /**
   * Computes the sum of all elements for each col ({@code dimension = 0}, default) or row ({@code dimension = 1}) of
   * the provided matrix.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if {@code dimension} is not one of 0
   * or 1.
   * 
   * @param matrix The provided matrix.
   * @param dimension The direction.
   * @return The sum.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code dimension} is not one of 0 or 1.
   */
  public static Mat sum(Mat matrix, int dimension) throws IllegalArgumentException {
    if (dimension == 0) {
      DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
      CommonOps.sumCols(matrix.memptr(), result);
      return new Mat(result);
    } else if (dimension == 1) {
      DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);
      CommonOps.sumRows(matrix.memptr(), result);
      return new Mat(result);
    } else {
      throw new IllegalArgumentException("The parameter dimension needs to be either '0' or '1' but was " + dimension);
    }
  }

  /**
   * Computes the sum of all elements for each col ({@code dimension = 0}, default) or row ({@code dimension = 1}) of
   * the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The sum.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code dimension} is not one of 0 or 1.
   * 
   * @see #sum(Mat, int)
   */
  public static Mat sum(Mat matrix) throws IllegalArgumentException {
    return sum(matrix, 0);
  }

  /**
   * Computes the mean of all elements for each col ({@code dimension = 0}, default) or row ({@code dimension = 1}) of
   * the provided matrix.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if {@code dimension} is not one of 0
   * or 1.
   * 
   * @param matrix The provided matrix.
   * @param dimension The direction.
   * @return The mean.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code dimension} is not one of 0 or 1.
   */
  public static Mat mean(Mat matrix, int dimension) throws IllegalArgumentException {
    if (dimension == 0) {
      DenseMatrix64F result = new DenseMatrix64F(1, matrix.n_cols);
      DenseMatrix64F memptr = matrix.memptr();

      for (int j = 0; j < matrix.n_cols; j++) {
        double total = 0;
        for (int i = 0; i < matrix.n_rows; i++) {
          total += memptr.get(i, j);
        }

        result.set(j, total / matrix.n_rows);
      }

      return new Mat(result);
    } else if (dimension == 1) {
      DenseMatrix64F result = new DenseMatrix64F(1, matrix.n_cols);
      DenseMatrix64F memptr = matrix.memptr();

      for (int i = 0; i < matrix.n_rows; i++) {
        double total = 0;
        for (int j = 0; j < matrix.n_cols; j++) {
          total += memptr.get(i, j);
        }

        result.set(i, total / matrix.n_cols);
      }

      return new Mat(result);
    } else {
      throw new IllegalArgumentException("The parameter dimension needs to be either '0' or '1' but was " + dimension);
    }
  }

  /**
   * Computes the mean of all elements for each col ({@code dimension = 0}, default) or row ({@code dimension = 1}) of
   * the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The mean.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code dimension} is not one of 0 or 1.
   * 
   * @see #mean(Mat, int)
   */
  public static Mat mean(Mat matrix) throws IllegalArgumentException {
    return mean(matrix, 0);
  }

  // public static Mat median(Mat matrix, int dimension) {
  //
  // }
  //
  // public static Mat median(Mat matrix) {
  // return median(matrix, 0);
  // }
  //
  // public static Mat stddev(Mat matrix, int normType, int dimension) {
  //
  // }
  //
  // public static Mat stddev(Mat matrix, int normType) {
  // return stddev(matrix, normType, 0);
  // }
  //
  // public static Mat stddev(Mat matrix, int dimension) {
  // return stddev(matrix, 0, dimension);
  // }
  //
  // public static Mat stddev(Mat matrix) {
  // return stddev(matrix, 0, 0);
  // }
  //
  // public static Mat var(Mat matrix, int normType, int dimension) {
  //
  // }
  //
  // public static Mat var(Mat matrix, int normType) {
  // return var(matrix, normType, 0);
  // }
  //
  // public static Mat var(Mat matrix, int dimension) {
  // return var(matrix, 0, dimension);
  // }
  //
  // public static Mat var(Mat matrix) {
  // return var(matrix, 0, 0);
  // }

  /**
   * Concatenation of the matrices {@code a} and {@code b} along their rows. Matrix {@code a} will be placed left and
   * {@code b} right.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if {@code a.}{@link Mat#n_rows n_rows}
   * {@code != b.n_rows}.
   * 
   * @param a First matrix.
   * @param b Second matrix.
   * @return The concatenated matrix.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code a.n_rows != b.n_rows}.
   * 
   * @see #join_horiz(Mat, Mat)
   */
  public static Mat join_rows(Mat a, Mat b) throws IllegalArgumentException {
    if (a.n_rows != b.n_rows) {
      throw new IllegalArgumentException("The provided matrices must have the same number of rows (" + a.n_rows + " != " + b.n_rows + ").");
    }

    DenseMatrix64F result = new DenseMatrix64F(a.n_rows, a.n_cols + b.n_cols);

    // Add matrix a
    DenseMatrix64F memptrA = a.memptr();
    for (int i = 0; i < a.n_rows; i++) {
      for (int j = 0; j < a.n_cols; j++) {
        result.set(i, j, memptrA.get(i, j));
      }
    }

    // Add matrix b
    DenseMatrix64F memptrB = b.memptr();
    for (int i = 0; i < b.n_rows; i++) {
      for (int j = 0; j < b.n_cols; j++) {
        result.set(i, j + a.n_cols, memptrB.get(i, j));
      }
    }

    return new Mat(result);
  }

  /**
   * Concatenation of the matrices {@code a} and {@code b} along their columns. Matrix {@code a} will be placed above
   * and {@code b} bellow.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if {@code a.}{@link Mat#n_cols n_cols}
   * {@code != b.n_cols}.
   * 
   * @param a First matrix.
   * @param b Second matrix.
   * @return The concatenated matrix.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code a.n_cols != b.n_cols}.
   * 
   * @see #join_vert(Mat, Mat)
   */
  public static Mat join_cols(Mat a, Mat b) throws IllegalArgumentException {
    if (a.n_cols != b.n_cols) {
      throw new IllegalArgumentException("The provided matrices must have the same number of columns (" + a.n_cols + " != " + b.n_cols + ").");
    }

    DenseMatrix64F result = new DenseMatrix64F(a.n_rows + b.n_rows, a.n_cols);

    // Add matrix a
    DenseMatrix64F memptrA = a.memptr();
    for (int i = 0; i < a.n_rows; i++) {
      for (int j = 0; j < a.n_cols; j++) {
        result.set(i, j, memptrA.get(i, j));
      }
    }

    // Add matrix b
    DenseMatrix64F memptrB = b.memptr();
    for (int i = 0; i < b.n_rows; i++) {
      for (int j = 0; j < b.n_cols; j++) {
        result.set(i + a.n_rows, j, memptrB.get(i, j));
      }
    }

    return new Mat(result);
  }

  /**
   * Concatenation of the matrices {@code a} and {@code b} along their rows. Matrix {@code a} will be placed left and
   * {@code b} right.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if {@code a.}{@link Mat#n_rows n_rows}
   * {@code != b.n_rows}.
   * 
   * @param a First matrix.
   * @param b Second matrix.
   * @return The concatenated matrix.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code a.n_rows != b.n_rows}.
   * 
   * @see #join_rows(Mat, Mat)
   */
  public static Mat join_horiz(Mat a, Mat b) throws IllegalArgumentException {
    return join_rows(a, b);
  }

  /**
   * Concatenation of the matrices {@code a} and {@code b} along their columns. Matrix {@code a} will be placed above
   * and {@code b} bellow.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if {@code a.}{@link Mat#n_cols n_cols}
   * {@code != b.n_cols}.
   * 
   * @param a First matrix.
   * @param b Second matrix.
   * @return The concatenated matrix.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code a.n_cols != b.n_cols}.
   * 
   * @see #join_cols(Mat, Mat)
   */
  public static Mat join_vert(Mat a, Mat b) throws IllegalArgumentException {
    return join_cols(a, b);
  }

  /**
   * Converts a provided (1,1)-matrix into a scalar of type double.
   * <p>
   * <b>Non-canonical:</b> A {@code IllegalArgumentException} exception is thrown if {@code matrix.}{@link Mat#n_rows
   * n_rows} {@code  > 1 || matrix.}{@link Mat#n_cols n_cols}{@code  > 1}.
   * 
   * @param matrix The provided matrix.
   * @return The scalar.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code matrix.}{@link Mat#n_rows n_rows}
   *           {@code  > 1 || matrix.}{@link Mat#n_cols n_cols}{@code  > 1}.
   */
  public static double as_scalar(Mat matrix) throws IllegalArgumentException {
    if (matrix.n_rows != 1 || matrix.n_cols != 1) {
      throw new IllegalArgumentException("The provided matrices must be a (1,1)-matrix but was (" + matrix.n_rows + ", " + matrix.n_cols + ").");
    }

    return matrix.memptr().get(0);
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

    if (!CommonOps.solve(a.memptr(), b.memptr(), x)) {
      throw new RuntimeException("The algorithm was unable to solve the matrix.");
    };

    return new Mat(x);
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
    svd.decompose(x.memptr());

    DenseMatrix64F s;
    try {
      s = svd.getW(null);
      // check catch
    } catch(IllegalArgumentException exception) {
      throw new RuntimeException("The algorithm was unable to decompose the matrix.");
    }

    return new Mat(s);
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
    svd.decompose(x.memptr());

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
    u.memptr().set(tempU);

    s.set_size(tempS.numRows, tempS.numCols);
    s.memptr().set(tempS);

    v.set_size(tempV.numRows, tempV.numCols);
    v.memptr().set(tempV);

    return true;
  }
}