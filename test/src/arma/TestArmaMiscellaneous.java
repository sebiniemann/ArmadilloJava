package arma;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author niemann
 *
 */
public class TestArmaMiscellaneous {

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testIs_finiteAbstractMat() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/elementwise.miscellaneous.mat");

    Mat testMatrix;

    for (double testValue : input) {
      testMatrix = new Mat(new double[]{testValue});
      assertEquals(!Double.isInfinite(testValue), Arma.is_finite(Arma.as_scalar(testMatrix)));
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testIs_finiteDouble() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/elementwise.miscellaneous.mat");

    for (double testValue : input) {
      assertEquals(!Double.isInfinite(testValue), Arma.is_finite(testValue));
    }
  }

}
