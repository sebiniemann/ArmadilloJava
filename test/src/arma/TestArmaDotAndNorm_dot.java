/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
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
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class TestArmaDotAndNorm_dot {

  /**
   * Returns the matrices to be tested.
   * 
   * @return The test matrices 
   * @throws IOException An I/O error occurred
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    String matrixA;
    String matrixB;
    String filenameA;
    String filenameB;
    String filenameExpected;

    int dimensions[] = {1, 2, 3, 4, 5, 10, 100};
    String matrices[] = {"zeros", "ones", "eye", "hankel", "hilbert"};

    for (int dimension : dimensions) {
      for (int a = 0; a < matrices.length; a++) {
        for (int b = a; b < matrices.length; b++) {
          matrixA = matrices[a];
          matrixB = matrices[b];

          if ((matrixA != "hilbert" && matrixB != "hilbert") || (dimension < 10)) {
            filenameA = matrixA + "." + 1 + "x" + dimension + ".mat";
            filenameB = matrixB + "." + 1 + "x" + dimension + ".mat";
            filenameExpected = matrixA + "." + matrixB + ".1x" + dimension + ".mat";

            testMatrices.add(new Object[]{filenameExpected, filenameA, filenameB});
          }
        }
      }
    }

    return testMatrices;
  }

  /**
   * The filename of the expected matrix
   */
  @Parameter(0)
  public String _filenameExpected;

  /**
   * The filename of the first test matrix
   */
  @Parameter(1)
  public String _filenameA;

  /**
   * The filename of the second test matrix
   */
  @Parameter(2)
  public String _filenameB;

  /**
   * Test method for {@link arma.Arma#dot(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testDot() throws IOException {
    Mat inputA = new Mat();
    Mat inputB = new Mat();

    inputA.load("./test/data/input/" + _filenameA);
    inputB.load("./test/data/input/" + _filenameB);

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testDot." + _filenameExpected);
    assertEquals(_filenameExpected, expected.at(0), Arma.dot(inputA, inputB), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.dot(inputA, inputB.t()), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.dot(inputA.t(), inputB), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.dot(inputA.t(), inputB.t()), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.dot(inputB, inputA), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.dot(inputB, inputA.t()), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.dot(inputB.t(), inputA), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.dot(inputB.t(), inputA.t()), 1e-11);
  }

  /**
   * Test method for {@link arma.Arma#norm_dot(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testNorm_dot() throws IOException {
    Mat inputA = new Mat();
    Mat inputB = new Mat();

    inputA.load("./test/data/input/" + _filenameA);
    inputB.load("./test/data/input/" + _filenameB);

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarValuedFunctionsOfVectorsMatrices/testNorm_dot." + _filenameExpected);
    assertEquals(_filenameExpected, expected.at(0), Arma.norm_dot(inputA, inputB), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.norm_dot(inputA, inputB.t()), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.norm_dot(inputA.t(), inputB), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.norm_dot(inputA.t(), inputB.t()), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.norm_dot(inputB, inputA), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.norm_dot(inputB, inputA.t()), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.norm_dot(inputB.t(), inputA), 1e-11);
    assertEquals(_filenameExpected, expected.at(0), Arma.norm_dot(inputB.t(), inputA.t()), 1e-11);
  }

}
