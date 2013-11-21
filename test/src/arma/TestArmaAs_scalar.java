package arma;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author niemann
 *
 */
public class TestArmaAs_scalar {

  /**
   * 
   */
  @Test
  public void testAs_scalar() {
    Mat testMatrix;
    
    testMatrix = new Mat(new double[]{0});
    assertEquals(0, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{1});
    assertEquals(1, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{2});
    assertEquals(2, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{Datum.eps});
    assertEquals(Datum.eps, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{Datum.inf});
    assertEquals(Datum.inf, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{Datum.e});
    assertEquals(Datum.e, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{12});
    assertEquals(12, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{123});
    assertEquals(123, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{1234});
    assertEquals(1234, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{12345});
    assertEquals(12345, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{123456});
    assertEquals(123456, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{1234567});
    assertEquals(1234567, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{12345678});
    assertEquals(12345678, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{123456789});
    assertEquals(123456789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.9});
    assertEquals(0.9, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.89});
    assertEquals(0.89, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.789});
    assertEquals(0.789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.6789});
    assertEquals(0.6789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.56789});
    assertEquals(0.56789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.456789});
    assertEquals(0.456789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.3456789});
    assertEquals(0.3456789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.23456789});
    assertEquals(0.23456789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{0.123456789});
    assertEquals(0.123456789, Arma.as_scalar(testMatrix), 0);
    
    testMatrix = new Mat(new double[]{-0});
    assertEquals(-0, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-1});
    assertEquals(-1, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-2});
    assertEquals(-2, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-Datum.eps});
    assertEquals(-Datum.eps, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-Datum.inf});
    assertEquals(-Datum.inf, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-Datum.e});
    assertEquals(-Datum.e, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-12});
    assertEquals(-12, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-123});
    assertEquals(-123, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-1234});
    assertEquals(-1234, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-12345});
    assertEquals(-12345, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-123456});
    assertEquals(-123456, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-1234567});
    assertEquals(-1234567, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-12345678});
    assertEquals(-12345678, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-123456789});
    assertEquals(-123456789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.9});
    assertEquals(-0.9, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.89});
    assertEquals(-0.89, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.789});
    assertEquals(-0.789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.6789});
    assertEquals(-0.6789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.56789});
    assertEquals(-0.56789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.456789});
    assertEquals(-0.456789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.3456789});
    assertEquals(-0.3456789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.23456789});
    assertEquals(-0.23456789, Arma.as_scalar(testMatrix), 0);
    testMatrix = new Mat(new double[]{-0.123456789});
    assertEquals(-0.123456789, Arma.as_scalar(testMatrix), 0);
  }

}
