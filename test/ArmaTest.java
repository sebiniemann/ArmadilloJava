import static org.junit.Assert.*;

import org.junit.Test;

import arma.Arma;
import arma.Mat;
import arma.Op;

/**
 * Test cases for arma.Arma.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * @version 0.9
 */
public class ArmaTest {

  /**
   * The following tests are applied to random matrices A = [a_1, a_2, ..., a_n], n of {1, ..., {@link Testutils#MAXIMAL_DIMENSION}}:
   * <ul>
   *   <li>|det([a_1, ..., c * a_j + d * v, ..., a_n]) - (c * det(A) + d * det([a_1, ..., a_{j-1}, v, ..., a_n]))| / det([a_1, ..., b * a_j + cv, ..., a_n]) < {@link Testutils#DELTA}</li>
   *   <li>|det([a_1, ..., a_j, a_{j+1}, ..., a_n]) + det([a_1, ..., a_{j+1}, a_j, ..., a_n])| / det(A) < {@link Testutils#DELTA}</li>
   *   <li>|det(I_n) - 1| < {@link Testutils#DELTA}</li>
   * </ul>
   */
  @Test
  public void det() {
    // Tests |det([a_1, ..., c * a_j + d * v, ..., a_n]) - c * det(A) + d * det([a_1, ..., a_{j-1}, v, ..., a_n])| / det([a_1, ..., b * a_j + cv, ..., a_n]) < {@link Testutils#DELTA}
    for (int n = 1; n < Testutils.MAXIMAL_DIMENSION; n++) {
      Mat A = Mat.randu(n, n, Testutils.getRNG());
      Mat B = new Mat(A);
      Mat C = new Mat(A);
      
      double c = Testutils.getRNG().nextDouble();
      double d = Testutils.getRNG().nextDouble();
      Mat v = Mat.randu(n, 1, Testutils.getRNG());
      int j = Testutils.getRNG().nextInt(n);

      B.col(j, Op.EQUAL, A.col(j).times(c).plus(v.times(d)));
      C.col(j, Op.EQUAL, v);

      assertEquals(0, (Arma.det(B) - (c * Arma.det(B) + d * Arma.det(C))) / Arma.det(B), Testutils.DELTA);
    }

    // Tests |det([a_1, ..., a_j, a_{j+1}, ..., a_n]) + det([a_1, ..., a_{j+1}, a_j, ..., a_n])| / det(A) < {@link Testutils#DELTA}
    for (int n = 2; n < Testutils.MAXIMAL_DIMENSION; n++) {
      Mat A = Mat.randu(n, n, Testutils.getRNG());
      Mat B = new Mat(A);

      int j = Testutils.getRNG().nextInt(n - 1);

      B.col(j+1, Op.EQUAL, A.col(j));
      B.col(j,   Op.EQUAL, A.col(j+1));

      assertEquals(0, (Arma.det(A) + Arma.det(B)) / Arma.det(A), Testutils.DELTA);
    }

    // Tests |det(I_n) - 1| < {@link Testutils#DELTA}
    for (int n = 1; n < Testutils.MAXIMAL_DIMENSION; n++) {
      assertEquals(1, Arma.det(Mat.eye(n, n)), Testutils.DELTA);
    }
  }

  @Test
  public void abs() {

  }

  @Test
  public void qr() {

  }

  @Test
  public void floor() {

  }

  @Test
  public void accu() {

  }

  @Test
  public void square() {

  }

  @Test
  public void find() {

  }

  @Test
  public void any() {

  }

  @Test
  public void norm() {

  }
}
