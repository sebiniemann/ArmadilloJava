package arma;

import java.util.ArrayList;
import java.util.Collection;

import arma.Mat;

/**
 * Provides several test matrices for parametrised interface tests as well as additional test parameters like the
 * maximal allowed numeric tolerances.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class TestData {

  /**
   * The maximal allowed numeric tolerances.
   */
  public static final double MAX_NUMERIC_TOLERANCE = 1e+15;

  /**
   * Returns a set of simple test matrices for parametrised interface tests with dimensions 1x1, 10x10 and 100x100
   * together with ground-truth values for some tests.
   * 
   * Provided matrices:
   * <ol>
   * <li>Zero matrices ({@link Mat#zeros(int, int)})</li>
   * <li>Matrices of ones ({@link Mat#ones(int, int)})</li>
   * <li>Identity matrices ({@link Mat#eye(int, int)})</li>
   * </ol>
   * 
   * Ground-truth values of the following are provided:
   * <ol>
   * <li>Determinant</li>
   * <li>
   * <ol>
   * <li>Induced matrix 1-norm</li>
   * <li>Induced matrix 2-norm</li>
   * </ol>
   * </li>
   * </ol>
   * 
   * @return The test matrices.
   */
  public static Collection<Object[]> getTestMatrices() {
    Collection<Object[]> matrices = new ArrayList<Object[]>();

    // Zero matrix
    matrices.add(new Object[]{Mat.zeros(1, 1), 0.0, 0.0, new double[]{0.0, 0.0}});
    matrices.add(new Object[]{Mat.zeros(10, 10), 0.0, 0.0, new double[]{0.0, 0.0}});
    matrices.add(new Object[]{Mat.zeros(100, 100), 0.0, 0.0, new double[]{0.0, 0.0}});

    // Matrix of ones
    matrices.add(new Object[]{Mat.ones(1, 1), 1.0, 1.0, new double[]{0.0, 0.0}});
    matrices.add(new Object[]{Mat.ones(10, 10), 0.0, 100.0, new double[]{10.0, 10.0}});
    matrices.add(new Object[]{Mat.ones(100, 100), 0.0, 10000.0, new double[]{100.0, 100.0}});

    // Identity matrix
    // Mat.eye(1, 1) is the same as Mat.ones(1, 1)
    matrices.add(new Object[]{Mat.eye(10, 10), 1.0, 10.0, new double[]{1.0, 1.0}});
    matrices.add(new Object[]{Mat.eye(100, 100), 1.0, 100.0, new double[]{1.0, 1.0}});

    return matrices;
  }

  /**
   * Returns a (non-vector) test matrices for parametrised interface tests composed of simple values (0.0, 1.0, 2.0, 3.0, -1.0, -123.0, -123456.0, 0.123, -0.000456) and
   * special values (PI, INF, NaN, E, sqrt(2), Machine epsilon). The values are arranged in a non-vector matrix.
   * 
   * @return The test matrix.
   */
  public static Collection<Object[]> getTestElementWise() {
    Collection<Object[]> matrices = new ArrayList<Object[]>();

    matrices.add(new Object[]{new Mat(new double[][]{{0.0, 1.0, 2.0, 3.0, 4.0, -1.0, -123.0, -123456.0}, {0.123, -0.000456, Datum.pi, Datum.inf, Datum.nan, Datum.e, Datum.sqrt2, Datum.eps}})});

    return matrices;
  }
}