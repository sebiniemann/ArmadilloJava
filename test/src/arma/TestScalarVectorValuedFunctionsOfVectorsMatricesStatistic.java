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
 * @author Daniel Kiechle <kiechle@sra.uni-hannover.de>
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic {
  
  /**
   * Test data for trigonometric functions
   * @return TestData
   * 
   * @throws IOException 
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    Mat input = new Mat();

    input.load("./test/data/input/statistics.mat");
    testMatrices.add(new Object[] {new Mat(input)});
    
    return testMatrices;
  }
  
  /**
   * 
   */
  @Parameter
  public Mat _testMatrix;
  
  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMin() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.min(_testMatrix.col(j)), 1e-15);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.min(_testMatrix.row(i)), 1e-15);
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMinMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.minMat(_testMatrix));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMinMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.minMat(_testMatrix, 0));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMin.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.minMat(_testMatrix, 1));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMax() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.max(_testMatrix.col(j)), 1e-15);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.max(_testMatrix.row(i)), 1e-15);
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMaxMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.maxMat(_testMatrix));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMaxMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.maxMat(_testMatrix, 0));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMax.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.maxMat(_testMatrix, 1));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMean() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.mean(_testMatrix.col(j)), 1e-15);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.mean(_testMatrix.row(i)), 1e-15);
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMeanMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.meanMat(_testMatrix));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMeanMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.meanMat(_testMatrix, 0));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMean.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.meanMat(_testMatrix, 1));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMedian() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.median(_testMatrix.col(j)), 1e-15);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.median(_testMatrix.row(i)), 1e-15);
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMedianMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.medianMat(_testMatrix));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testMedianMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.medianMat(_testMatrix, 0));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testMedian.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.medianMat(_testMatrix, 1));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testStddevAbstractMat() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.stddev(_testMatrix.col(j)), 1e-9);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.stddev(_testMatrix.row(i)), 1e-9);
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testStddevAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.stddev(_testMatrix.col(j), 0), 1e-9);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.stddev(_testMatrix.col(j), 1), 1e-9);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.stddev(_testMatrix.row(i), 0), 1e-9);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.stddev(_testMatrix.row(i), 1), 1e-9);
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testStddevMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testStddevMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 1));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testStddevMatAbstractMatIntInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 0, 0));

    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 1, 0));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n0.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 0, 1));

    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testStddev.n1.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.stddevMat(_testMatrix, 1, 1));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testVarAbstractMat() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.var(_testMatrix.col(j)), 1e-9);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.var(_testMatrix.row(i)), 1e-9);
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testVarAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.var(_testMatrix.col(j), 0), 1e-9);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d0.mat");
    for(int j = 0; j < _testMatrix.n_cols; j++) {
      assertEquals(j + "th column", expected.at(j), Arma.var(_testMatrix.col(j), 1), 1e-9);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.var(_testMatrix.row(i), 0), 1e-9);
    }
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d1.mat");
    for(int i = 0; i < _testMatrix.n_rows; i++) {
      assertEquals(i + "th row", expected.at(i), Arma.var(_testMatrix.row(i), 1), 1e-9);
    }
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testVarMatAbstractMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testVarMatAbstractMatInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 0));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 1));
  }

  /**
   * @throws IOException 
   * 
   */
  @Test
  public void testVarMatAbstractMatIntInt() throws IOException {
    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 0, 0));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 1, 0));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n0.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 0, 1));
    
    expected.load("./test/data/expected/TestScalarVectorValuedFunctionsOfVectorsMatricesStatistic/testVar.n1.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.varMat(_testMatrix, 1, 1));
  }

}
