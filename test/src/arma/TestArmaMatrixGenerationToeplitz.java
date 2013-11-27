/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
/**
 * 
 */
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
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixGenerationToeplitz {

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
   * The number of elements
   */
  @Parameter
  public int _numberOfElements;

  /**
   * Test method for {@link arma.Arma#toeplitz(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testToeplitzAbstractMat() throws IOException {
    String filename = "numbered." + _numberOfElements + "x" + 1 + ".mat";

    Mat input = new Mat();
    input.load("./test/data/input/" + filename);

    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixGenerationToeplitz/testToeplitz." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.toeplitz(input));

    expected.load("./test/data/expected/TestArmaMatrixGenerationToeplitz/testToeplitz." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.toeplitz(input.t()));
  }

  /**
   * Test method for {@link arma.Arma#toeplitz(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testToeplitzAbstractMatAbstractMat() throws IOException {
    String filename = "numbered." + _numberOfElements + "x" + 2 + ".mat";

    Mat input = new Mat();
    input.load("./test/data/input/" + filename);

    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixGenerationToeplitz/testToeplitz." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.toeplitz(input.col(0), input.col(1)));

    expected.load("./test/data/expected/TestArmaMatrixGenerationToeplitz/testToeplitz." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.toeplitz(input.col(0), input.col(1).t()));

    expected.load("./test/data/expected/TestArmaMatrixGenerationToeplitz/testToeplitz." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.toeplitz(input.col(0).t(), input.col(1)));

    expected.load("./test/data/expected/TestArmaMatrixGenerationToeplitz/testToeplitz." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.toeplitz(input.col(0).t(), input.col(1).t()));
  }

  /**
   * Test method for {@link arma.Arma#circ_toeplitz(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCirc_toeplitz() throws IOException {
    String filename = "numbered." + _numberOfElements + "x" + 1 + ".mat";

    Mat input = new Mat();
    input.load("./test/data/input/" + filename);

    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixGenerationToeplitz/testCirc_toeplitz." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.circ_toeplitz(input));

    expected.load("./test/data/expected/TestArmaMatrixGenerationToeplitz/testCirc_toeplitz." + filename);
    assertMatElementWiseEquals(filename, expected, Arma.circ_toeplitz(input.t()));
  }
}
