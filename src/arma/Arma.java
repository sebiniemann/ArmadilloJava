package arma;

/**
 * Arma includes standalone interfaces similar to the Armadillo C++ Algebra Library by Conrad Sanderson et al., based on
 * DenseMatrix64F from Peter Abeles’ Efficient Java Matrix Library (EJML) Version 0.23 - 21.06.2013..
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * @version 0.9
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient Java Matrix Library</a>
 */
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.factory.QRDecomposition;
import org.ejml.ops.CommonOps;
import org.ejml.ops.NormOps;

public class Arma {
  public static double det(Mat matrix) {
    return CommonOps.det(matrix.memptr());
  }

  public static Mat abs(Mat columnVector) {
    DenseMatrix64F result = new DenseMatrix64F(columnVector.memptr());

    for (int i = 0; i < columnVector.n_elem; i++) {
      double element = columnVector.at(i);

      if (columnVector.at(i) < 0) {
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

  public static double sum(Mat parameter) {
    return CommonOps.elementSum(parameter.memptr());
  }

  public static Mat square(Mat parameter) {
    return parameter.elemTimes(parameter);
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
          throw new UnsupportedOperationException("Only comparision operators are supported.");
      }
    }

    return new Mat(result);
  }

  public static Mat find(double a, Op operation, Mat b) {
    return find(b, operation, a);
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

  public static boolean any(Mat columnVector) {
    for (int i = 0; i < columnVector.n_elem; i++) {
      if (columnVector.at(i) != 0) {
        return true;
      }
    }

    return false;
  }

  public static double norm(Mat parameter, int p) {
    return NormOps.normP(parameter.memptr(), p);
  }
}