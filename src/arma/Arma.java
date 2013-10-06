package arma;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.QRDecomposition;
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
   * @throws IllegalArgumentException Thrown if the provided matrix is not square.
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
   * Performs a <a href="http://en.wikipedia.org/wiki/QR_decomposition">QR decomposition</a> on the matrix {@code X}.
   * <p>
   * The matrix {@code X} is decomposed into a orthogonal matrix {@code Q} and upper triangular matrix {@code R}, such
   * that {@code X = QR}.
   * <p>
   * The provided matrices {@code Q} and {@code R} are not touched if the decomposition fails.
   * 
   * @param Q An orthogonal matrix.
   * @param R An upper triangular matrix.
   * @param X The matrix to be decomposed.
   * @return False if the decomposition fails and true otherwise.
   */
  public static boolean qr(Mat Q, Mat R, Mat X) {
    QRDecomposition<DenseMatrix64F> qr = DecompositionFactory.qr(X.n_rows, X.n_cols);
    qr.decompose(X.memptr());

    DenseMatrix64F tempQ;
    DenseMatrix64F tempR;
    try {
      tempQ = qr.getQ(null, false);
      tempR = qr.getR(null, false);
    } catch(IllegalArgumentException exception) {
      return false;
    }

    Q.set_size(tempQ.numRows, tempQ.numCols);
    Q.memptr().set(tempQ);

    R.set_size(tempR.numRows, tempR.numCols);
    R.memptr().set(tempR);

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
   * Creates a column vector containing indices of all non-zero elements. Contains all indices of elements that satisfy
   * an operation if used together with {@link Op#evaluate(Mat, Op, Mat)}.
   * <p>
   * Contains all {@code k = 0} or the first ({@code s = "first"}) respectively last ({@code s = "last"}) {@code k}
   * indices at most.
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
      throw new IllegalArgumentException("The parameter s needs to be either 'first' or 'last'.");
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
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code matrix} is a vector and {@code p} not strict
   * greater than 0.
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code matrix} is not a vector and {@code p} not one
   * of 1, 2.
   * </ul>
   * 
   * @param matrix The provided matrix.
   * @param p The type of the norm.
   * @return The norm.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code matrix} is a vector and {@code p} not
   *           strict greater than 0or if {@code matrix} is not a vector and {@code p} not one of 1, 2.
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
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code matrix} is a vector and {@code p} not one of
   * '-inf', 'inf' or 'fro'.
   * <li>An {@code IllegalArgumentException} exception is thrown if {@code matrix} is not a vector and {@code p} not one
   * of 'inf' or 'fro'.
   * </ul>
   * 
   * @param matrix The provided matrix.
   * @param p The type of the norm.
   * @return The norm.
   * 
   * @throws IllegalArgumentException <b>Non-canonical:</b> Thrown if {@code matrix} is a vector and {@code p} not one
   *           of '-inf', 'inf' or 'fro' or if {@code matrix} is not a vector and {@code p} not one of 'inf' or 'fro'.
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
}