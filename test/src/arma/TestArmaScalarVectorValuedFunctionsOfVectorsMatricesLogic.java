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
 * @author Daniel Kiechle <kiechle@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic {

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

    input.load("./test/data/input/logic.mat");
    testMatrices.add(new Object[]{new Mat(input)});

    return testMatrices;
  }

  /**
   * The test matrix
   */
  @Parameter
  public Mat _testMatrix;

  /**
   * Test method for {@link arma.Arma#any(AbstractMat)}.
   * 
   * @throws IOException
   */
  @Test
  public void testAnyMat() throws IOException {

  }

  /**
   * Test method for {@link arma.Arma#anyMat(AbstractMat)}.
   * 
   * @throws IOException
   */
  @Test
  public void testAnyMatMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.anyMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#anyMat(AbstractMat, int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testAnyMatMatInt() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.anyMat(_testMatrix, 0));

    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAny.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.anyMat(_testMatrix, 1));
  }

  /**
   * Test method for {@link arma.Arma#all(AbstractMat)}.
   */
  @Test
  public void testAllMat() {

  }

  /**
   * Test method for {@link arma.Arma#allMat(AbstractMat)}.
   * 
   * @throws IOException
   */
  @Test
  public void testAllMatMat() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.allMat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#allMat(AbstractMat, int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testAllMatMatInt() throws IOException {
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.allMat(_testMatrix, 0));
<<<<<<< HEAD
 
=======

>>>>>>> bd3438b17828778acceea423ba932cb435d003a0
    expected.load("./test/data/expected/TestArmaScalarVectorValuedFunctionsOfVectorsMatricesLogic/testAll.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.allMat(_testMatrix, 1));
  }

}
