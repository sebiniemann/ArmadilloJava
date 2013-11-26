/**
 * 
 */
package arma;

import static arma.TestUtil.assertMatElementWiseEquals;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixGenerationVector {

  /**
   * Returns the matrices to be tested.
   * 
   * @return The test matrices
   * 
   * @throws IOException n I/O error occurred
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    int dimensions[] = {1, 2, 3, 4, 5, 10, 100};

    for (int numberOfElements : dimensions) {
      testMatrices.add(new Object[]{numberOfElements});
    }

    return testMatrices;
  }

  /**
   * The number of rows
   */
  @Parameter
  public int _numberOfElements;

  /**
   * Test method for {@link arma.Arma#zeros(int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testZerosInt() throws IOException {
    String filename = "zeros." + _numberOfElements + "x" + 1 + ".mat";

    Mat expected = new Mat();
    expected.load("./test/data/input/" + filename);
    assertMatElementWiseEquals(filename, expected, Arma.zeros(_numberOfElements));
  }

  /**
   * Test method for {@link arma.Arma#ones(int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testOnesInt() throws IOException {
    String filename = "ones." + _numberOfElements + "x" + 1 + ".mat";

    Mat expected = new Mat();
    expected.load("./test/data/input/" + filename);
    assertMatElementWiseEquals(filename, expected, Arma.ones(_numberOfElements));

  }
  
  /**
   * Test method for {@link arma.Arma#randu(int, java.util.Random)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testRanduIntRandom() throws IOException {
    RunningStatVec statistic = new RunningStatVec();
    Random rng = new Random();
    rng.setSeed(123456789);
    
    for (int n = 0; n < 1000; n++) {
      Mat actual = Arma.randu(_numberOfElements, rng);
      assertEquals("Number of rows", _numberOfElements, actual.n_rows);
      assertEquals("Number of columns", 1, actual.n_cols);
      
      statistic.update(actual);
    }
    
    Mat mean = statistic.mean();
    Mat max = statistic.max();
    Mat min = statistic.min();
    Mat var = statistic.var();
    for(int n = 0; n < mean.n_elem; n++) {
      assertEquals("", 0.5, mean.at(n), 0.25);
      assertTrue("", max.at(n) <= 1);
      assertTrue("", min.at(n) >= 0);
      assertEquals("", 1.0/12, var.at(n), 0.025);
    }
  }

  /**
   * Test method for {@link arma.Arma#randn(int, java.util.Random)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testRandnIntRandom() throws IOException {
    RunningStatVec statistic = new RunningStatVec();
    Random rng = new Random();
    rng.setSeed(123456789);
    
    for (int n = 0; n < 1000; n++) {
      Mat actual = Arma.randn(_numberOfElements, rng);
      assertEquals("Number of rows", _numberOfElements, actual.n_rows);
      assertEquals("Number of columns", 1, actual.n_cols);
      
      statistic.update(actual);
    }
    
    Mat mean = statistic.mean();
    Mat var = statistic.var();
    for(int n = 0; n < mean.n_elem; n++) {
      assertEquals("", 0, mean.at(n), 0.25);
      assertEquals("", 1, var.at(n), 0.25);
    }
  }
}
