package arma;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author niemann
 * 
 */
@RunWith(Parameterized.class)
public class TestArmaDotAndNorm_dot {

  /**
   * Test data for miscellaneous functions
   * 
   * @return TestData
   * @throws IOException
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    String matrixA;
    String matrixB;
    String filename;
    String filenameA;
    String filenameB;

    int dimensions[] = {1, 2, 3, 4, 5, 10, 100};
    String matrices[] = {"zeros", "ones", "eye", "hankel", "hilbert"};

    for (int dimension : dimensions) {
      for (int a = 0; a < matrices.length; a++) {
        for (int b = a; b < matrices.length; b++) {
          matrixA = matrices[a];
          matrixB = matrices[b];

          if ((matrixA != "hilbert" && matrixB != "hilbert") || (dimension < 10)) {
            filename = matrixA + "." + matrixB + ".1x" + dimension + ".mat";
            filenameA = matrixA + "." + 1 + "x" + dimension + ".mat";
            filenameB = matrixB + "." + 1 + "x" + dimension + ".mat";

            testMatrices.add(new Object[]{filename, filenameA, filenameB});
          }
        }
      }
    }

    return testMatrices;
  }

  /**
   * 
   */
  @Parameter(0)
  public String _filename;

  /**
   * 
   */
  @Parameter(1)
  public String _filenameA;

  /**
   * 
   */
  @Parameter(2)
  public String _filenameB;

  /**
   * @throws IOException
   * 
   */
  @Test
  public void testDot() throws IOException {
    Mat inputA = new Mat();
    Mat inputB = new Mat();

    inputA.load("./test/data/input/" + _filenameA);
    inputB.load("./test/data/input/" + _filenameB);

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testDot." + _filename);
    assertEquals(_filename, expected.at(0), Arma.dot(inputA, inputB), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.dot(inputA, inputB.t()), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.dot(inputA.t(), inputB), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.dot(inputA.t(), inputB.t()), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.dot(inputB, inputA), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.dot(inputB, inputA.t()), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.dot(inputB.t(), inputA), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.dot(inputB.t(), inputA.t()), 1e-11);
  }

  /**
   * @throws IOException
   * 
   */
  @Test
  public void testNorm_dot() throws IOException {
    Mat inputA = new Mat();
    Mat inputB = new Mat();

    inputA.load("./test/data/input/" + _filenameA);
    inputB.load("./test/data/input/" + _filenameB);

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm_dot." + _filename);
    try {
      assertEquals(_filename, expected.at(0), Arma.norm_dot(inputA, inputB), 1e-11);
    } catch(Exception e) {
      System.out.println("debug");
      System.out.println(inputA);
      System.out.println(inputB);
    }
    assertEquals(_filename, expected.at(0), Arma.norm_dot(inputA, inputB.t()), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.norm_dot(inputA.t(), inputB), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.norm_dot(inputA.t(), inputB.t()), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.norm_dot(inputB, inputA), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.norm_dot(inputB, inputA.t()), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.norm_dot(inputB.t(), inputA), 1e-11);
    assertEquals(_filename, expected.at(0), Arma.norm_dot(inputB.t(), inputA.t()), 1e-11);
  }

}
