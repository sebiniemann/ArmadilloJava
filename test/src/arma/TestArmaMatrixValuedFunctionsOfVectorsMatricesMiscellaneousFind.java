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
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind {

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

    int elements[] = {0, 1, 2, 3, 4, 5, 10};

    for (int numberOfElements : elements) {
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
   * Test method for {@link arma.Arma#find(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testFindAbstractMat() throws IOException {
    if(_numberOfElements != 0) {
      return;
    }
    
    Mat input = new Mat();
    input.load("./test/data/input/series.mat");

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind/testFind.k0.first.mat");
    assertMatElementWiseEquals("", expected, Arma.find(input));
  }

  /**
   * Test method for {@link arma.Arma#find(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testFindAbstractMatInt() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/series.mat");

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind/testFind.k" + _numberOfElements + ".first.mat");
    assertMatElementWiseEquals("Number of elements: " + _numberOfElements, expected, Arma.find(input, _numberOfElements));
  }

  /**
   * Test method for {@link arma.Arma#find(arma.AbstractMat, int, java.lang.String)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testFindAbstractMatIntString() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/series.mat");

    Mat expected = new Mat();
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind/testFind.k" + _numberOfElements + ".first.mat");
    assertMatElementWiseEquals("", expected, Arma.find(input, _numberOfElements, "first"));
    
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousFind/testFind.k" + _numberOfElements + ".last.mat");
    assertMatElementWiseEquals("Number of elements: " + _numberOfElements, expected, Arma.find(input, _numberOfElements, "last"));
  }
}
