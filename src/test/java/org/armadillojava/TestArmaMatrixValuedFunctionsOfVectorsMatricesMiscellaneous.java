/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.armadillojava;

import static org.armadillojava.TestUtil.assertMatElementWiseEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous {

  /**
   * Test method for {@link arma.Arma#cumsum(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCumsum() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/series.mat");

    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCumsum.d0.mat");
    for (int j = 0; j < input.n_cols; j++) {
      assertMatElementWiseEquals("", expected.col(j), Arma.cumsum(input.col(j)));
    }

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCumsum.d1.mat");
    for (int i = 0; i < input.n_rows; i++) {
      assertMatElementWiseEquals("", expected.row(i), Arma.cumsum(input.row(i)));
    }
  }

  /**
   * Test method for {@link arma.Arma#cumsumMat(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCumsumMatAbstractMat() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/series.mat");

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCumsum.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.cumsumMat(input));
  }

  /**
   * Test method for {@link arma.Arma#cumsumMat(arma.AbstractMat, int)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCumsumMatAbstractMatInt() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/series.mat");

    Mat expected = new Mat();

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCumsum.d0.mat");
    assertMatElementWiseEquals("", expected, Arma.cumsumMat(input, 0));

    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCumsum.d1.mat");
    assertMatElementWiseEquals("", expected, Arma.cumsumMat(input, 1));
  }

  /**
   * Test method for {@link arma.Arma#conv(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testConv() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/series.mat");

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testConv.mat");

    assertMatElementWiseEquals("", expected, Arma.conv(input.col(0), input.col(1)));
    assertMatElementWiseEquals("", expected, Arma.conv(input.col(0), input.col(1).t()));

    assertMatElementWiseEquals("", expected.t(), Arma.conv(input.col(0).t(), input.col(1)));
    assertMatElementWiseEquals("", expected.t(), Arma.conv(input.col(0).t(), input.col(1).t()));
  }

  /**
   * Test method for {@link arma.Arma#cross(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testCross() throws IOException {
    String matrices[] = {"zeros", "ones", "eye", "hankel", "hilbert"};

    String filenameA;
    Mat inputA = new Mat();

    String filenameB;
    Mat inputB = new Mat();

    Mat expected = new Mat();

    for (String matrixA : matrices) {
      for (String matrixB : matrices) {
        filenameA = matrixA + ".3x1.mat";
        inputA.load("./test/data/input/" + filenameA);

        filenameB = matrixB + ".3x1.mat";
        inputB.load("./test/data/input/" + filenameB);

        expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testCross." + matrixA + "." + matrixB + ".mat");

        assertMatElementWiseEquals(matrixA + "." + matrixB, expected, Arma.cross(inputA, inputB));
        assertMatElementWiseEquals(matrixA + "." + matrixB, expected, Arma.cross(inputA, inputB.t()));
        assertMatElementWiseEquals(matrixA + "." + matrixB, expected.t(), Arma.cross(inputA.t(), inputB));
        assertMatElementWiseEquals(matrixA + "." + matrixB, expected.t(), Arma.cross(inputA.t(), inputB.t()));
      }
    }
  }

  /**
   * Test method for {@link arma.Arma#kron(arma.AbstractMat, arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testKron() throws IOException {
    Mat inputA = new Mat();
    inputA.load("./test/data/input/numbered.10x10.mat");

    Mat inputB = new Mat();
    Mat expected = new Mat();
    String filename;

    int dimensions[] = {1, 2, 3, 4, 5, 10};

    for (int numberOfRows : dimensions) {
      for (int numberOfColumns : dimensions) {
        filename = "numbered." + numberOfRows + "x" + numberOfColumns + ".mat";
        inputB.load("./test/data/input/" + filename);

        expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testKron.numbered.10x10." + filename);
        assertMatElementWiseEquals(filename, expected, Arma.kron(inputA, inputB));
      }
    }
  }

  /**
   * Test method for {@link arma.Arma#unique(arma.AbstractMat)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testUnique() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/series.mat");

    Mat expected = new Mat();
    expected.load("./test/data/expected/TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneous/testUnique.mat");

    assertMatElementWiseEquals("", expected, Arma.unique(input));

    Col inputCol = Arma.vectorise(input);

    assertMatElementWiseEquals("", expected, Arma.unique(inputCol));
    assertMatElementWiseEquals("", expected.t(), Arma.unique(inputCol).t());
  }

}
