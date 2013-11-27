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
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins {
  /**
   * 
   * Returns the matrices to be tested.
   * 
   * @return The test matrices
   * 
   * @throws IOException An I/O error occurred
   */
  @Parameters
  public static Collection<Object[]> getTestMatrices() throws IOException {
    Collection<Object[]> testMatrices = new ArrayList<Object[]>();

    String filenameA;
    String filenameB;
    String filenameExpected;

    int dimensions[] = {1, 2, 3, 4, 5, 10, 100};
    String matrices[] = {"numbered"};

    for (int numberOfRowsA : dimensions) {
      for (int numberOfColumnsA : dimensions) {
        for (String matrix : matrices) {
          filenameA = matrix + "." + numberOfRowsA + "x" + numberOfColumnsA + ".mat";

          for (int numberOfColumnsB : dimensions) {
            filenameB = matrix + "." + numberOfRowsA + "x" + numberOfColumnsB + ".mat";
            filenameExpected = matrix + "." + numberOfRowsA + "x" + numberOfColumnsA + "." + matrix + "." + numberOfRowsA + "x" + numberOfColumnsB + ".mat";

            testMatrices.add(new Object[]{filenameExpected, filenameA, filenameB});
           }

          for (int numberOfRowsB : dimensions) {
            filenameB = matrix + "." + numberOfRowsB + "x" + numberOfColumnsA + ".mat";
            filenameExpected = matrix + "." + numberOfRowsA + "x" + numberOfColumnsA + "." + matrix + "." + numberOfRowsB + "x" + numberOfColumnsA + ".mat";
            
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
   * Test method for {@link arma.Arma#join_cols(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testJoin_cols() throws IOException {
    Mat inputA = new Mat();
    Mat inputB = new Mat();

    inputA.load("./test/data/input/" + _filenameA);
    inputB.load("./test/data/input/" + _filenameB);
    
    if(inputA.n_cols != inputB.n_cols) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins/testJoin_vert." + _filenameExpected);
    assertMatElementWiseEquals(_filenameExpected, expected, Arma.join_cols(inputA, inputB));
  }

  /**
   * Test method for {@link arma.Arma#join_vert(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testJoin_vert() throws IOException {
    Mat inputA = new Mat();
    Mat inputB = new Mat();

    inputA.load("./test/data/input/" + _filenameA);
    inputB.load("./test/data/input/" + _filenameB);
    
    if(inputA.n_cols != inputB.n_cols) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins/testJoin_vert." + _filenameExpected);
    assertMatElementWiseEquals(_filenameExpected, expected, Arma.join_vert(inputA, inputB));
  }

  /**
   * Test method for {@link arma.Arma#join_rows(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testJoin_rows() throws IOException {
    Mat inputA = new Mat();
    Mat inputB = new Mat();

    inputA.load("./test/data/input/" + _filenameA);
    inputB.load("./test/data/input/" + _filenameB);
    
    if(inputA.n_rows != inputB.n_rows) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins/testJoin_horiz." + _filenameExpected);
    assertMatElementWiseEquals(_filenameExpected, expected, Arma.join_rows(inputA, inputB));
  }

  /**
   * Test method for {@link arma.Arma#join_horiz(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testJoin_horiz() throws IOException {
    Mat inputA = new Mat();
    Mat inputB = new Mat();

    inputA.load("./test/data/input/" + _filenameA);
    inputB.load("./test/data/input/" + _filenameB);
    
    if(inputA.n_rows != inputB.n_rows) {
      return;
    }
    
    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMatrixJoins/testJoin_horiz." + _filenameExpected);
    assertMatElementWiseEquals(_filenameExpected, expected, Arma.join_horiz(inputA, inputB));
  }

}
