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
 * @version 0.9
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient Java Matrix Library</a>
 */
public class Arma {
  /**
   * Return the determinant of the provided matrix. Fails if the matrix is not square.
   * 
   * @param matrix The provided matrix.
   * @return The determinant.
   */
  public static double det(Mat matrix) {
    return CommonOps.det(matrix.memptr());
  }

  /**
   * Creates a new matrix with 
   * 
   * @param matrix
   * @return
   */
  public static Mat abs(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.memptr());

    for (int i = 0; i < matrix.n_elem; i++) {
      double element = matrix.at(i);

      if (element < 0) {
        result.set(i, -element);
      }
    }

    return new Mat(result);
  }

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

  public static Mat floor(Mat matrix) {
    DenseMatrix64F result = new DenseMatrix64F(matrix.n_rows, matrix.n_cols);

    for (int i = 0; i < matrix.n_elem; i++) {
      result.set(i, Math.floor(matrix.at(i)));
    }

    return new Mat(result);
  }

  public static double sum(Mat element) {
    return CommonOps.elementSum(element.memptr());
  }

  public static Mat square(Mat element) {
    return parameter.elemTimes(element);
  }

  public static Mat find(Mat a, Op operation, double b) {
    DenseMatrix64F result = new DenseMatrix64F(a.n_elem, 1);

    for (int i = 0; i < a.n_elem; i++) {
      switch (operation) {
        case STRICT_LESS:
          if (a.at(i) < b) {
            result.set(i, 1);
          }
          break;
        case LESS:
          if (a.at(i) <= b) {
            result.set(i, 1);
          }
          break;
        case EQUAL:
          if (a.at(i) == b) {
            result.set(i, 1);
          }
          break;
        case NOT_EQUAL:
          if (a.at(i) != b) {
            result.set(i, 1);
          }
          break;
        case GREATER:
          if (a.at(i) >= b) {
            result.set(i, 1);
          }
          break;
        case STRICT_GREATER:
          if (a.at(i) > b) {
            result.set(i, 1);
          }
          break;
        default:
          throw new UnsupportedOperationException("Only comparison operators are supported.");
      }
    }

    return new Mat(result);
  }

  public static Mat find(double a, Op operation, Mat b) {
    switch (operation) {
      case STRICT_LESS:
        return find(b, Op.STRICT_GREATER, a);
      case LESS:
        return find(b, Op.GREATER, a);
      case GREATER:
        return find(b, Op.STRICT_LESS, a);
      case STRICT_GREATER:
        return find(b, Op.STRICT_LESS, a);
      default:
        // Catches Op.EQUAL and Op.NOT_EQUAL
        // Error-checking should be done in find
        return find(b, operation, a);
    }

  }

  public static Mat find(Mat a, Op operation, Mat b) {
    DenseMatrix64F result = new DenseMatrix64F(a.n_elem, 1);

    for (int i = 0; i < a.n_elem; i++) {
      switch (operation) {
        case STRICT_LESS:
          if (a.at(i) < b.at(i)) {
            result.set(i, 1);
          }
          break;
        case LESS:
          if (a.at(i) <= b.at(i)) {
            result.set(i, 1);
          }
          break;
        case EQUAL:
          if (a.at(i) == b.at(i)) {
            result.set(i, 1);
          }
          break;
        case NOT_EQUAL:
          if (a.at(i) != b.at(i)) {
            result.set(i, 1);
          }
          break;
        case GREATER:
          if (a.at(i) >= b.at(i)) {
            result.set(i, 1);
          }
          break;
        case STRICT_GREATER:
          if (a.at(i) > b.at(i)) {
            result.set(i, 1);
          }
          break;
        default:
          throw new UnsupportedOperationException("Only comparision operators are supported.");
      }
    }

    return new Mat(result);
  }

  public static boolean any(Mat element) {
    for (int i = 0; i < element.n_elem; i++) {
      if (element.at(i) != 0) {
        return true;
      }
    }

    return false;
  }

  public static double norm(Mat element, int p) {
    return NormOps.normP(element.memptr(), p);
  }
}