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
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize {

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
        testMatrices.add(new Object[]{"numbered.10x10." + numberOfRows + "x" + numberOfColumns + ".mat", numberOfRows, numberOfColumns});
      }
    }

    return testMatrices;
  }

  /**
   * The test matrix
   */
  @Parameter(0)
  public String _filenameExpected;

  /**
   * The number of rows
   */
  @Parameter(1)
  public int _numberOfRows;

  /**
   * The number of columns
   */
  @Parameter(2)
  public int _numberOfColumns;

  /**
   * Test method for {@link arma.Arma#reshape(arma.Mat, int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testReshapeMatIntInt() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/numbered.10x10.mat");
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize/testReshape.d0." + _filenameExpected);
    assertMatElementWiseEquals(_filenameExpected, expected, Arma.reshape(input, _numberOfRows, _numberOfColumns));
  }

  /**
   * Test method for {@link arma.Arma#reshape(arma.Mat, int, int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testReshapeMatIntIntInt() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/numbered.10x10.mat");
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize/testReshape.d0." + _filenameExpected);
    assertMatElementWiseEquals(_filenameExpected, expected, Arma.reshape(input, _numberOfRows, _numberOfColumns, 0));
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize/testReshape.d1." + _filenameExpected);
    assertMatElementWiseEquals(_filenameExpected, expected, Arma.reshape(input, _numberOfRows, _numberOfColumns, 1));
  }

  /**
   * Test method for {@link arma.Arma#resize(arma.Mat, int, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testResize() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/numbered.10x10.mat");

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReshapeResize/testResize." + _filenameExpected);
    assertMatElementWiseEquals(_filenameExpected, expected, Arma.resize(input, _numberOfRows, _numberOfColumns));
  }

}
