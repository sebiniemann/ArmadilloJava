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

import static arma.TestUtil.assertMatElementWiseEquals;

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
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesSwap {

  /**
   * Returns the matrices to be tested.
   * 
   * @return The test matrices
   * @throws IOException An I/O error occurred
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    Mat input = new Mat();
    String filename;

    int dimensions[] = {1, 2, 3, 4, 5, 10, 100};
    String matrices[] = {"numbered"};

    for (int numberOfRows : dimensions) {
      for (int numberOfColumns : dimensions) {
        for (String matrix : matrices) {
          filename = matrix + "." + numberOfRows + "x" + numberOfColumns + ".mat";
          input.load("./test/data/input/" + filename);
          testMatrices.add(new Object[]{filename, new Mat(input)});
        }
      }
    }

    return testMatrices;
  }

  /**
   * The filename of the test matrix
   */
  @Parameter(0)
  public String _filename;

  /**
   * The test matrix
   */
  @Parameter(1)
  public Mat    _testMatrix;

  /**
   * Test method for {@link arma.Arma#fliplr(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testFliplr() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSwap/testFliplr." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.fliplr(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#flipud(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testFlipud() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesSwap/testFlipud." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.flipud(_testMatrix));
  }

}
