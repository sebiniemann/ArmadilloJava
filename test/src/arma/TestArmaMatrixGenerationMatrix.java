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
public class TestArmaMatrixGenerationMatrix {

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

    for (int numberOfRows : dimensions) {
      for (int numberOfColumns : dimensions) {
        testMatrices.add(new Object[]{numberOfRows, numberOfColumns});
      }
    }

    return testMatrices;
  }

  /**
   * The number of rows
   */
  @Parameter(0)
  public int _numberOfRows;

  /**
   * The number of columns
   */
  @Parameter(1)
  public int _numberOfColumns;

  /**
   * Test method for {@link arma.Arma#zeros(int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testZerosIntInt() throws IOException {
    String filename = "zeros." + _numberOfRows + "x" + _numberOfColumns + ".mat";

    Mat expected = new Mat();
    expected.load("./test/data/input/" + filename);
    assertMatElementWiseEquals(filename, expected, Arma.zeros(_numberOfRows, _numberOfColumns));
  }

  /**
   * Test method for {@link arma.Arma#ones(int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testOnesIntInt() throws IOException {
    String filename = "ones." + _numberOfRows + "x" + _numberOfColumns + ".mat";

    Mat expected = new Mat();
    expected.load("./test/data/input/" + filename);
    assertMatElementWiseEquals(filename, expected, Arma.ones(_numberOfRows, _numberOfColumns));
  }

  /**
   * Test method for {@link arma.Arma#eye(int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testEye() throws IOException {
    String filename = "eye." + _numberOfRows + "x" + _numberOfColumns + ".mat";

    Mat expected = new Mat();
    expected.load("./test/data/input/" + filename);
    assertMatElementWiseEquals(filename, expected, Arma.eye(_numberOfRows, _numberOfColumns));
  }

  /**
   * Test method for {@link arma.Arma#randu(int, int, java.util.Random)}.
   */
  @Test
  public void testRanduIntIntRandom() {
    if(_numberOfRows > 10 || _numberOfColumns > 10) {
      return;
    }
    
    RunningStatVec statistic = new RunningStatVec();
    Random rng = new Random();
    rng.setSeed(123456789);
    
    for (int n = 0; n < 1000; n++) {
      Mat actual = Arma.randu(_numberOfRows, _numberOfColumns, rng);
      assertEquals("Number of rows", _numberOfRows, actual.n_rows);
      assertEquals("Number of columns", _numberOfColumns, actual.n_cols);
      
      statistic.update(Arma.vectorise(actual));
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
   * Test method for {@link arma.Arma#randn(int, int, java.util.Random)}.
   */
  @Test
  public void testRandnIntIntRandom() {
    if(_numberOfRows > 10 || _numberOfColumns > 10) {
      return;
    }
    
    RunningStatVec statistic = new RunningStatVec();
    Random rng = new Random();
    rng.setSeed(123456789);
    
    for (int n = 0; n < 1000; n++) {
      Mat actual = Arma.randn(_numberOfRows, _numberOfColumns, rng);
      assertEquals("Number of rows", _numberOfRows, actual.n_rows);
      assertEquals("Number of columns", _numberOfColumns, actual.n_cols);
      
      statistic.update(Arma.vectorise(actual));
    }
    
    Mat mean = statistic.mean();
    Mat var = statistic.var();
    for(int n = 0; n < mean.n_elem; n++) {
      assertEquals("", 0, mean.at(n), 0.25);
      assertEquals("", 1, var.at(n), 0.25);
    }
  }

  /**
   * Test method for {@link arma.Arma#repmat(arma.AbstractMat, int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testRepmat() throws IOException {
    if(_numberOfRows > 10 || _numberOfColumns > 10) {
      return;
    }
    
    Mat input = new Mat();
    input.load("./test/data/input/numbered.10x10.mat");
    
    String filename = "numbered.10x10." + 10 * _numberOfRows + "x" + 10 * _numberOfColumns + ".mat";

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixGenerationMatrix/testRepmat." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.repmat(input, _numberOfRows, _numberOfColumns));
  }
}
