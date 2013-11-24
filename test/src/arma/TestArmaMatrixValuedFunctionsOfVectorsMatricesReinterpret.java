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
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret {
  
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
          if (!matrix.equals("hilbert") || (numberOfRows < 10 && numberOfColumns < 10)) {
            filename = matrix + "." + numberOfRows + "x" + numberOfColumns + ".mat";
            input.load("./test/data/input/" + filename);
            testMatrices.add(new Object[]{filename, new Mat(input)});
          }
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
   * Test method for {@link arma.Arma#diagmat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testDiagmat() throws IOException {
    if(!_testMatrix.is_square() && !_testMatrix.is_vec()) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testDiagmat." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.diagmat(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#trimatu(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testTrimatu() throws IOException {
    if(!_testMatrix.is_square()) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testTrimatu." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.trimatu(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#trimatl(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testTrimatl() throws IOException {
    if(!_testMatrix.is_square()) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testTrimatl." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.trimatl(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#symmatu(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSimmatu() throws IOException {
    if(!_testMatrix.is_square()) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testSymmatu." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.symmatu(_testMatrix));
  }

  /**
   * Test method for {@link arma.Arma#symmatl(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testSimmatl() throws IOException {
    if(!_testMatrix.is_square()) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesReinterpret/testSymmatl." + _filename);
    assertMatElementWiseEquals(_filename, expected, Arma.symmatl(_testMatrix));
  }

}
