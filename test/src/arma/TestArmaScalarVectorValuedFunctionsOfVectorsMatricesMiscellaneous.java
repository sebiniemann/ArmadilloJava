/**
 * 
 */
package arma;

import static arma.TestUtil.assertMatElementWiseEquals;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous {

  /**
   * Returns the matrices to be tested.
   * 
   * @return The test matrices 
   * 
   * @throws IOException An I/O error occurred
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    Mat input = new Mat();

    input.load("./test/data/input/series.mat");
    testMatrices.add(new Object[]{new Mat(input)});

    return testMatrices;
  }

  /**
   * The test matrix
   */
  @Parameter
  public Mat _testMatrix;
  
  /**
   * Test method for {@link arma.Arma#prod(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testProd() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testProd.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.prod(_testMatrix.col(j)), expected.at(j) * 1e-15);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testProd.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.prod(_testMatrix.row(i)), expected.at(i) * 1e-12);
    }
  }

  /**
   * Test method for {@link arma.Arma#prodMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testProdMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testProd.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.prodMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#prodMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testProdMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testProd.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.prodMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testProd.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.prodMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#sum(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSum() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testSum.d0.mat");
    for (int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.sum(_testMatrix.col(j)), 1e-13);
    }

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testSum.d1.mat");
    for (int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.sum(_testMatrix.row(i)), 1e-13);
    }
  }

  /**
   * Test method for {@link arma.Arma#sumMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSumMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testSum.d0.mat");
    Mat actual = Arma.sumMat(_testMatrix, 0);

    assertEquals("Number of rows", expected.n_rows, actual.n_rows);
    assertEquals("Number of columns", expected.n_cols, actual.n_cols);
    for (int n = 0; n < expected.n_elem; n++) {
      assertEquals("at position " + n, expected.at(n), actual.at(n), 1e-13);
    }
  }

  /**
   * Test method for {@link arma.Arma#sumMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSumMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testSum.d0.mat");
    Mat actual = Arma.sumMat(_testMatrix, 0);

    assertEquals("Number of rows", expected.n_rows, actual.n_rows);
    assertEquals("Number of columns", expected.n_cols, actual.n_cols);
    for (int n = 0; n < expected.n_elem; n++) {
      assertEquals("at position " + n, expected.at(n), actual.at(n), 1e-13);
    }
    
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesMiscellaneous/testSum.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.sumMat(_testMatrix, 1));
  }

}
