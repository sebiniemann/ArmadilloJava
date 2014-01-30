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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Sebastian Niemann <niemann@sra.uni-hannovr.de>
 */
@RunWith(Parameterized.class)
public class TestArmaMatrixValuedFunctionsOfVectorsMatricesMiscellaneousShuffle {

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
    String filename;

    int dimensions[] = {1, 2, 3, 4, 5};
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
   * Test method for {@link arma.Arma#shuffle(arma.AbstractMat, java.util.Random)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testShuffleMatAbstractMat() throws IOException {
    RunningStatVec statistic = new RunningStatVec();
    Random rng = new Random();
    rng.setSeed(123456789);

    Mat actual;

    Set<Integer> indicies = new HashSet<Integer>();

    for (int n = 0; n < 1000; n++) {
      actual = Arma.shuffle(_testMatrix, rng);

      for (int i = 0; i < _testMatrix.n_rows; i++) {
        indicies.add(i);
      }

      assertEquals("Number of rows", _testMatrix.n_rows, actual.n_rows);
      assertEquals("Number of columns", _testMatrix.n_cols, actual.n_cols);
      for (int i = 0; i < actual.n_rows; i++) {
        for (int ii : indicies) {
          if (actual.at(i) == _testMatrix.at(ii)) {
            assertMatElementWiseEquals("" + _testMatrix + actual, _testMatrix.row(ii), actual.row(i));
            indicies.remove(ii);
            break;
          }

        }
      }

      if (_testMatrix.n_rows > 1) {
        // actual.n_rows - 1 will be equal the largest element in the first row for the specified input matrix
        // "numbered.*"
        statistic.update(actual.col(0).elemDivide((_testMatrix.n_rows - 1.0) / 2));
      }
    }

    if (_testMatrix.n_rows > 1) {
      Mat mean = statistic.mean();
      for (int n = 0; n < mean.n_elem; n++) {
        assertEquals("", 1, mean.at(n), 1e-1);
      }
    }
  }

  /**
   * Test method for {@link arma.Arma#shuffle(arma.AbstractMat, int, java.util.Random)}.
   * 
   * @throws IOException An I/O error occurred
   */
  @Test
  public void testShuffleMatAbstractMatInt() throws IOException {
    RunningStatVec statistic = new RunningStatVec();
    Random rng = new Random();
    rng.setSeed(123456789);

    Mat actual;

    Set<Integer> indicies = new HashSet<Integer>();

    for (int n = 0; n < 1000; n++) {
      actual = Arma.shuffle(_testMatrix, 0, rng);

      for (int i = 0; i < _testMatrix.n_rows; i++) {
        indicies.add(i);
      }

      assertEquals("Number of rows", _testMatrix.n_rows, actual.n_rows);
      assertEquals("Number of columns", _testMatrix.n_cols, actual.n_cols);
      for (int i = 0; i < actual.n_rows; i++) {
        for (int ii : indicies) {
          if (actual.at(i) == _testMatrix.at(ii)) {
            assertMatElementWiseEquals("", _testMatrix.row(ii), actual.row(i));
            indicies.remove(ii);
            break;
          }

        }
      }

      if (_testMatrix.n_rows > 1) {
        // actual.n_rows - 1 will be equal the largest element in the first row for the specified input matrix
        // "numbered.*"
        statistic.update(actual.col(0).elemDivide((_testMatrix.n_rows - 1.0) / 2));
      }
    }

    if (_testMatrix.n_rows > 1) {
      Mat mean = statistic.mean();
      for (int n = 0; n < mean.n_elem; n++) {
        assertEquals("", 1, mean.at(n), 1e-1);
      }
    }

    statistic.reset();
    rng.setSeed(123456789);

    for (int n = 0; n < 1000; n++) {
      actual = Arma.shuffle(_testMatrix, 1, rng);

      for (int i = 0; i < _testMatrix.n_rows; i++) {
        indicies.add(i);
      }

      assertEquals("Number of rows", _testMatrix.n_rows, actual.n_rows);
      assertEquals("Number of columns", _testMatrix.n_cols, actual.n_cols);
      for (int j = 0; j < actual.n_cols; j++) {
        for (int jj : indicies) {
          if (actual.at(0, j) == _testMatrix.at(0, jj)) {
            assertMatElementWiseEquals("", _testMatrix.col(jj), actual.col(j));
            indicies.remove(jj);
            break;
          }

        }
      }

      if (_testMatrix.n_cols > 1) {
        statistic.update(actual.row(0).elemDivide(_testMatrix.at(0, _testMatrix.n_cols - 1) / 2));
      }
    }

    if (_testMatrix.n_cols > 1) {
      Mat mean = statistic.mean();
      for (int n = 0; n < mean.n_elem; n++) {
        assertEquals("", 1, mean.at(n), 1e-1);
      }
    }
  }
}
