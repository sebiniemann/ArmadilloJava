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
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Daniel Kiechle <kiechle@sra.uni-hannover.de>
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class MatTest {

  /**
   * Returns the verctors to be tested.
   * 
   * @return The test value
   * 
   */
  @Parameters
  public static Collection<Object[]> getTestVectors() throws IOException {
    Collection<Object[]> testVectors = new ArrayList<Object[]>();
    for (int x = 0; x < 10; x++) {
      Object[] input = new Object[4];
      double[] rows = new double[(int) (Math.random() * 100)];
      for (int i = 0; i != input.length; i++) {
        for (int j = 0; j != rows.length; j++) {
          rows[j] = Math.random() * (100 - (-100)) - 100;
        }
        input[i] = rows;
      }
      testVectors.add(input);
    }
    return testVectors;
  }

  /**
   * The test vectors
   */
  @Parameter(value = 0)
  public double[] _row1;

  @Parameter(value = 1)
  public double[] _row2;

  @Parameter(value = 2)
  public double[] _row3;

  @Parameter(value = 3)
  public double[] _row4;

  /**
   * Test method for {@link arma.Mat#Mat(double[])}.
   */
  @Test
  public void testMatDoubleArray() {
    Mat expected = new Mat(_row1);
    for (int i = 0; i < _row1.length; i++) {
      assertEquals((Double) _row1[i], (Double) expected.at(i));
    }
  }

  /**
   * Test method for {@link arma.Mat#Mat(double[][])}.
   */
  @Test
  public void testMatDoubleArrayArray() {
    double[][] input = new double[4][_row1.length];
    for (int i = 0; i != _row1.length; i++)
      input[0][i] = _row1[i];
    for (int i = 0; i != _row2.length; i++)
      input[1][i] = _row2[i];
    for (int i = 0; i != _row3.length; i++)
      input[2][i] = _row3[i];
    for (int i = 0; i != _row4.length; i++)
      input[3][i] = _row4[i];
    Mat expected = new Mat(input);
    for (int i = 0; i < _row1.length; i++)
      assertEquals((Double) _row1[i], (Double) expected.at(0, i));
    for (int i = 0; i < _row1.length; i++)
      assertEquals((Double) _row2[i], (Double) expected.at(1, i));
    for (int i = 0; i < _row1.length; i++)
      assertEquals((Double) _row3[i], (Double) expected.at(2, i));
    for (int i = 0; i < _row1.length; i++)
      assertEquals((Double) _row4[i], (Double) expected.at(3, i));

  }

  /**
   * Test method for {@link arma.Mat#Mat(arma.Mat)}.
   * 
   * @throws IOException
   */
  @Test
  public void testMatMat() throws IOException {
    Mat input = new Mat();
    input.load("./test/data/input/numbered.100x100.mat");
    Mat expected = new Mat(input);
    assertMatElementWiseEquals("", expected, input);
  }

  /**
   * Test method for {@link arma.Mat#Mat(int)}.
   */
  @Test
  public void testMatInt() {
    Mat expected = new Mat(_row1.length);
    assertEquals(expected.n_rows, _row1.length);
  }

  /**
   * Test method for {@link arma.Mat#Mat(int, int)}.
   */
  @Test
  public void testMatIntInt() {
    Mat expected = new Mat(_row1.length, _row2.length);
    assertEquals(expected.n_rows, _row1.length);
    assertEquals(expected.n_cols, _row2.length);
  }

  /**
   * Test method for {@link arma.Mat#Mat(int, arma.Fill)}.
   * 
   * @throws IOException
   */
  @Test
  public void testMatIntFill() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    for (int d : dimmensions) {
      Mat expected = new Mat(d, Fill.ONES);
      input.load("./test/data/input/ones." + d + "x1.mat");
      assertMatElementWiseEquals("", expected, input);

      expected = new Mat(d, Fill.ZEROS);
      input.load("./test/data/input/zeros." + d + "x1.mat");
      assertMatElementWiseEquals("", expected, input);

      expected = new Mat(d, Fill.EYE);
      input.load("./test/data/input/eye." + d + "x1.mat");
      assertMatElementWiseEquals("", expected, input);

      expected = new Mat(d, Fill.NONE);
      input = new Mat(d, 1);
      assertMatElementWiseEquals("", expected, input);
    }
  }

  /**
   * Test method for {@link arma.Mat#Mat(int, int, arma.Fill)}.
   * 
   * @throws IOException
   */
  @Test
  public void testMatIntIntFill() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        Mat expected = new Mat(r, c, Fill.ONES);
        input.load("./test/data/input/ones." + r + "x" + c + ".mat");
        assertMatElementWiseEquals("", expected, input);

        expected = new Mat(r, c, Fill.ZEROS);
        input.load("./test/data/input/zeros." + r + "x" + c + ".mat");
        assertMatElementWiseEquals("", expected, input);

        expected = new Mat(r, c, Fill.EYE);
        input.load("./test/data/input/eye." + r + "x" + c + ".mat");
        assertMatElementWiseEquals("", expected, input);

        expected = new Mat(r, c, Fill.NONE);
        input = new Mat(r, c);
        assertMatElementWiseEquals("", expected, input);
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#Mat(int, arma.Fill, java.util.Random)}.
   */
  @Test
  public void testMatIntFillRandom() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#Mat(int, int, arma.Fill, java.util.Random)}.
   */
  @Test
  public void testMatIntIntFillRandom() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#zeros(int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testZerosInt() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    Mat expected = new Mat(0);
    for (int d : dimmensions) {
      expected.zeros(d);
      input.load("./test/data/input/zeros." + d + "x1.mat");
      assertMatElementWiseEquals("", expected, input);
    }
  }

  /**
   * Test method for {@link arma.Mat#zeros(int, int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testZerosIntInt() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    Mat expected = new Mat(0, 0);
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        expected.zeros(r, c);
        input.load("./test/data/input/zeros." + r + "x" + c + ".mat");
        assertMatElementWiseEquals("", expected, input);
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#ones(int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testOnesInt() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    Mat expected = new Mat(0);
    for (int d : dimmensions) {
      expected.ones(d);
      input.load("./test/data/input/ones." + d + "x1.mat");
      assertMatElementWiseEquals("", expected, input);
    }
  }

  /**
   * Test method for {@link arma.Mat#ones(int, int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testOnesIntInt() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    Mat expected = new Mat(0, 0);
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        expected.ones(r, c);
        input.load("./test/data/input/ones." + r + "x" + c + ".mat");
        assertMatElementWiseEquals("", expected, input);
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#eye(int, int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testEyeIntInt() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    Mat expected = new Mat(0, 0);
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        expected.eye(r, c);
        input.load("./test/data/input/eye." + r + "x" + c + ".mat");
        assertMatElementWiseEquals("", expected, input);
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#randu(int, java.util.Random)}.
   */
  @Test
  public void testRanduIntRandom() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#randu(int, int, java.util.Random)}.
   */
  @Test
  public void testRanduIntIntRandom() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#randn(int, java.util.Random)}.
   */
  @Test
  public void testRandnIntRandom() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#randn(int, int, java.util.Random)}.
   */
  @Test
  public void testRandnIntIntRandom() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#insert_cols(int, arma.Mat)}.
   * 
   * @throws IOException
   */
  @Test
  public void testInsert_colsIntMat() throws IOException {
    int[] dimensions = {1, 2, 3, 4, 5, 10, 100};
    String filenameA, filenameB;
    Mat inputA = new Mat();
    Mat inputB = new Mat();
    Mat expected = new Mat();

    for (int numberOfColumnsA : dimensions) {
      filenameA = "ones.1x" + numberOfColumnsA + ".mat";

      for (int numberOfColumnsB : dimensions) {
        if (numberOfColumnsB > numberOfColumnsA) continue;
        filenameB = "zeros.1x" + numberOfColumnsB + ".mat";

        for (int i = 0; i < numberOfColumnsA - 1; i++) {
          expected.load("./test/data/expected/TestMat/testInsert_cols.ones.1x" + numberOfColumnsA + ".zeros.1x" + numberOfColumnsB + ".addOn" + i + ".mat");
          inputA.load("./test/data/input/" + filenameA);
          inputB.load("./test/data/input/" + filenameB);
          inputA.insert_cols(i, inputB);
          assertMatElementWiseEquals("", expected, inputA);
        }
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#insert_cols(int, int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testInsert_colsIntInt() throws IOException {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#insert_cols(int, int, boolean)}.
   */
  @Test
  public void testInsert_colsIntIntBoolean() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#insert_rows(int, arma.Mat)}.
   * 
   * @throws IOException
   */
  @Test
  public void testInsert_rowsIntMat() throws IOException {
    int[] dimensions = {1, 2, 3, 4, 5, 10, 100};
    String filenameA, filenameB;
    Mat inputA = new Mat();
    Mat inputB = new Mat();
    Mat expected = new Mat();

    for (int numberOfColumnsA : dimensions) {
      filenameA = "ones." + numberOfColumnsA + "x1.mat";

      for (int numberOfColumnsB : dimensions) {
        if (numberOfColumnsB > numberOfColumnsA) continue;
        filenameB = "zeros." + numberOfColumnsB + "x1.mat";

        for (int i = 0; i < numberOfColumnsA - 1; i++) {
          expected.load("./test/data/expected/TestMat/testInsert_rows.ones." + numberOfColumnsA + "x1.zeros." + numberOfColumnsB + "x1.addOn" + i + ".mat");
          inputA.load("./test/data/input/" + filenameA);
          inputB.load("./test/data/input/" + filenameB);
          inputA.insert_rows(i, inputB);
          assertMatElementWiseEquals("", expected, inputA);
        }
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#insert_rows(int, int)}.
   */
  @Test
  public void testInsert_rowsIntInt() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#insert_rows(int, int, boolean)}.
   */
  @Test
  public void testInsert_rowsIntIntBoolean() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#shed_col(int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testShed_col() throws IOException {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#shed_cols(int, int)}.
   */
  @Test
  public void testShed_cols() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#shed_row(int)}.
   */
  @Test
  public void testShed_row() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#shed_rows(int, int)}.
   */
  @Test
  public void testShed_rows() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#set_size(int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testSet_sizeInt() throws IOException {
    Mat expected = new Mat();
    Mat input = new Mat();
    expected.load("./test/data/input/numbered.1x100.mat");
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    for (int c : dimmensions) {
      input.load("./test/data/input/numbered.1x" + c + ".mat");
      expected.resize(1, c);
      assertEquals(expected.size(), input.size());
    }
  }

  /**
   * Test method for {@link arma.Mat#set_size(int, int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testSet_sizeIntInt() throws IOException {
    Mat expected = new Mat();
    Mat input = new Mat();
    expected.load("./test/data/input/numbered.100x100.mat");
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        input.load("./test/data/input/numbered." + r + "x" + c + ".mat");
        expected.resize(r, c);
        assertEquals(expected.size(), input.size());
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#reshape(int, int)}.
   */
  @Test
  public void testReshapeIntInt() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#reshape(int, int, int)}.
   */
  @Test
  public void testReshapeIntIntInt() {
    // fail("Not yet implemented");
  }

  /**
   * Test method for {@link arma.Mat#resize(int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testResizeInt() throws IOException {
    Mat expected = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    for (int c : dimmensions) {
      expected.load("./test/data/input/numbered." + 1 + "x" + c + ".mat");
      Mat input = expected;
      expected.resize(1, (int) (c + (Math.random() * (c - 1) - 1)));
      input.resize(expected.n_rows, expected.n_cols);
      assertMatElementWiseEquals("", expected, input);
    }
  }

  /**
   * Test method for {@link arma.Mat#resize(int, int)}.
   * 
   * @throws IOException
   */
  @Test
  public void testResizeIntInt() throws IOException {
    Mat expected = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        expected.load("./test/data/input/numbered." + r + "x" + c + ".mat");
        Mat input = expected;
        expected.resize((int) (r - (Math.random() * (r - 1) - 1)), (int) (c + (Math.random() * (c - 1) - 1)));
        input.resize(expected.n_rows, expected.n_cols);
        assertMatElementWiseEquals("", expected, input);
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#clear()}.
   * 
   * @throws IOException
   */
  @Test
  public void testClear() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    Mat expected = new Mat();
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        input.load("./test/data/input/numbered." + r + "x" + c + ".mat");
        input.clear();
        assertMatElementWiseEquals("", expected, input);
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#reset()}.
   * 
   * @throws IOException
   */
  @Test
  public void testReset() throws IOException {
    Mat input = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    Mat expected = new Mat(0, 0);
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        input.load("./test/data/input/numbered." + r + "x" + c + ".mat");
        input.reset();
        assertMatElementWiseEquals("", expected, input);
      }
    }
  }

  /**
   * Test method for {@link arma.Mat#copy_size(arma.AbstractMat)}.
   * 
   * @throws IOException
   */
  @Test
  public void testCopy_size() throws IOException {
    Mat expected = new Mat();
    int[] dimmensions = {1, 2, 3, 4, 5, 10, 100};
    for (int r : dimmensions) {
      for (int c : dimmensions) {
        expected.load("./test/data/input/numbered." + r + "x" + c + ".mat");
        Mat input = new Mat((int) (r - (Math.random() * (r - 1) - 1)), (int) (c + (Math.random() * (c - 1) - 1)));
        expected.copy_size(input);
        assertEquals(expected.size(), input.size());
      }
    }
  }

}
