package arma;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.QRDecomposition;
import org.ejml.ops.CommonOps;
import org.ejml.ops.NormOps;

/**
 * Arma includes stand-alone interfaces similar to the Armadillo C++ Algebra Library by Conrad Sanderson et al., based
 * on DenseMatrix64F from Peter Abeles’ Efficient Java Matrix Library (EJML) Version 0.23 - 21.06.2013.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient Java Matrix Library</a>
 */
public class Arma {

  /**
   * Returns the determinant of the provided matrix. Fails if the matrix is not square.
   * 
   * @param matrix The provided matrix.
   * @return The determinant.
   */
  public static double det(Mat matrix) {
    return CommonOps.det(matrix.memptr());
  }

  /**
   * Creates a new matrix with the absolute value for all elements of the provides matrix.
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
   * Performs a <a href="http://en.wikipedia.org/wiki/QR_decomposition">QR decomposition</a> of the provided matrix
   * <code>X</code> into the product <code>QR</code>.
   * 
   * @param Q An orthogonal matrix.
   * @param R An upper triangular matrix.
   * @param X The matrix to be decomposed.
   */
  public static void qr(Mat Q, Mat R, Mat X) {
    QRDecomposition<DenseMatrix64F> qr = DecompositionFactory.qr(X.n_rows, X.n_cols);
    qr.decompose(X.memptr());

    DenseMatrix64F Qqr = qr.getQ(null, false);
    Q.set_size(Qqr.numRows, Qqr.numCols);
    Q.memptr().set(Qqr);

    DenseMatrix64F Rqr = qr.getR(null, false);
    R.set_size(Rqr.numRows, Rqr.numCols);
    R.memptr().set(Rqr);
  }

  /**
   * Creates a new matrix with all elements of the provides matrix element-wise floored.
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
   * Returns the sum of all elements of the provided matrix.
   * 
   * @param matrix The provided matrix.
   * @return The sum.
   */
  public static double accu(Mat matrix) {
    return CommonOps.elementSum(matrix.memptr());
  }

  /**
   * Creates a new matrix with all elements of the provides matrix element-wise squared.
   * 
   * @param matrix The provided matrix.
   * @return The created matrix.
   */
  public static Mat square(Mat matrix) {
    return matrix.elemTimes(matrix);
  }

  /**
   * Creates a new matrix with its values set to <code>1</code> for each element where <code>a operation b</code> holds
   * and <code>0</code> otherwise. The single provided right-hand side value is used for all operations. Only relational
   * operators are supported.
   * 
   * @param a The left-hand side operand.
   * @param operation The operation to be performed.
   * @param b The right-hand side operand.
   * @return The created matrix.
   * 
   * @throws UnsupportedOperationException Thrown if another operation besides relational operators is requested.
   */
  public static Mat find(Mat a, Op operation, double b) throws UnsupportedOperationException {
    DenseMatrix64F result = new DenseMatrix64F(a.n_elem, 1);
    DenseMatrix64F memptrA = a.memptr();

    for (int i = 0; i < a.n_elem; i++) {
      if (find(memptrA.get(i), operation, b)) {
        result.set(i, 1);
      }
    }

    return new Mat(result);
  }

  /**
   * Creates a new matrix with its values set to <code>1</code> for each element where <code>a operation b</code> holds
   * and <code>0</code> otherwise. The single provided left-hand side value is used for all operations. Only relational
   * operators are supported.
   * 
   * @param a The left-hand side operand.
   * @param operation The operation to be performed.
   * @param b The right-hand side operand.
   * @return The created matrix.
   * 
   * @throws UnsupportedOperationException Thrown if another operation besides relational operators is requested.
   */
  public static Mat find(double a, Op operation, Mat b) throws UnsupportedOperationException {
    switch (operation) {
      case STRICT_LESS:
        return find(b, Op.STRICT_GREATER, a);
      case LESS:
        return find(b, Op.GREATER, a);
      case GREATER:
        return find(b, Op.STRICT_LESS, a);
      case STRICT_GREATER:
        return find(b, Op.STRICT_LESS, a);
      case EQUAL:
      case NOT_EQUAL:
        return find(b, operation, a);
      default:
        throw new UnsupportedOperationException("Only relational operators are supported.");
    }
  }

  /**
   * Creates a new matrix with its values set to 1 for each element where <code>a operation b</code> holds and 0
   * otherwise. Only relational operators are supported.
   * 
   * @param a The left-hand side operand.
   * @param operation The operation to be performed.
   * @param b The right-hand side operand.
   * @return The created matrix.
   */
  public static Mat find(Mat a, Op operation, Mat b) {
    DenseMatrix64F result = new DenseMatrix64F(a.n_elem, 1);
    DenseMatrix64F memptrA = a.memptr();
    DenseMatrix64F memptrB = a.memptr();

    for (int i = 0; i < a.n_elem; i++) {
      if (find(memptrA.get(i), operation, memptrB.get(i))) {
        result.set(i, 1);
      }
    }

    return new Mat(result);
  }
  
  /**
   * Returns true if <code>a operation b</code> holds and 0
   * otherwise. Only relational operators are supported.
   * 
   * @param a The left-hand side operand.
   * @param operation The operation to be performed.
   * @param b The right-hand side operand.
   * @return The boolean.
   * 
   * @throws UnsupportedOperationException Thrown if another operation besides relational operators is requested.
   */
  private static boolean find(double a, Op operation, double b) throws UnsupportedOperationException {
    switch (operation) {
      case STRICT_LESS:
        if (a < b) {
          return true;
        }
        break;
      case LESS:
        if (a <= b) {
          return true;
        }
        break;
      case EQUAL:
        if (a == b) {
          return true;
        }
        break;
      case NOT_EQUAL:
        if (a != b) {
          return true;
        }
        break;
      case GREATER:
        if (a >= b) {
          return true;
        }
        break;
      case STRICT_GREATER:
        if (a > b) {
          return true;
        }
        break;
      default:
        throw new UnsupportedOperationException("Only relational operators are supported.");
    }

    return false;
  }

  /**
   * Returns <code>true</code> is any element of the provided matrix is non-zero and <code>false</code> otherwise.
   * 
   * @param matrix The provided matrix.
   * @return The truth value.
   */
  public static boolean any(Mat matrix) {
    DenseMatrix64F memptr = matrix.memptr();
    for (int i = 0; i < matrix.n_elem; i++) {
      if (memptr.get(i) != 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the (induced) <code>p</code>-norm of the provided matrix. If <code>matrix</code> is actually a vector,
   * <code>p</code> must be an integer greater than 0. Otherwise, <code>p</code> must be one of 1, 2.
   * 
   * @param matrix The provided matrix.
   * @param p The type of the norm.
   * @return The norm.
   */
  public static double norm(Mat matrix, int p) {
    // Error-checking should be done in NormOps.normP(DenseMatrix64F A, double p)
    return NormOps.normP(matrix.memptr(), p);
  }
}