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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import static arma.TestUtil.assertMatEquals;

/**
 * Contains all test cases for non-parametrised interface tests of operations in {@link Arma}.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class TestArmaIndividual {

  /**
   * Tests {@link Arma#find(Mat, int, String)} and overloaded methods by assertion of the following:
   * 
   * Returns {{0}, {3}, {4}, {5}} with {@code k} = (default) and {@code s} = (default),
   * returns {{0}} with {@code k} = 2 and {@code s} = (default),
   * returns {{0}, {3}, {4}, {5}} with {@code k} = (default) and {@code s} = "first",
   * returns {{0}, {3}, {4}, {5}} with {@code k} = (default) and {@code s} = "last",
   * returns {{0}} with {@code k} = 2 and {@code s} = "first",
   * returns {{4}, {5}} with {@code k} = 2 and {@code s} = "last" for:
   * <ul>
   * <li>{{1, 0, 1}, {0, 1, 1}}</li>
   * </ul>
   * 
   * Returns {{0}} true for:
   * <ul>
   * <li>{{1}}</li>
   * <li>{{-1}}</li>
   * <li>{{{@link Datum#inf}</li>
   * <li>{{{@link Datum#nan}</li>
   * <li>{{Double.MIN_VALUE}}</li>
   * </ul>
   * 
   * Returns an empty matrix for:
   * <ul>
   * <li>{{0}}</li>
   * <li>{{0, 0, 0}, {0, 0, 0}}</li>
   * </ul>
   */
  @Test
  public void testFind() {
    Mat testMatrix;
    Mat expected;
    Mat actual;

    testMatrix = new Mat(new double[][]{{1, 0, 1}, {0, 1, 1}});
    expected = new Mat(new double[][]{{0}, {3}, {4}, {5}});
    actual = Arma.find(testMatrix);
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{1, 0, 1}, {0, 1, 1}});
    expected = new Mat(new double[][]{{0}});
    actual = Arma.find(testMatrix, 2);
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{1, 0, 1}, {0, 1, 1}});
    expected = new Mat(new double[][]{{0}, {3}, {4}, {5}});
    actual = Arma.find(testMatrix, "first");
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{1, 0, 1}, {0, 1, 1}});
    expected = new Mat(new double[][]{{5}, {4}, {3}, {0}});
    actual = Arma.find(testMatrix, "last");

    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{1, 0, 1}, {0, 1, 1}});
    expected = new Mat(new double[][]{{0}});
    actual = Arma.find(testMatrix, 2, "first");
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{1, 0, 1}, {0, 1, 1}});
    expected = new Mat(new double[][]{{5}, {4}});
    actual = Arma.find(testMatrix, 2, "last");

    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{1}});
    expected = new Mat(new double[][]{{0}});
    actual = Arma.find(testMatrix);
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{-1}});
    expected = new Mat(new double[][]{{0}});
    actual = Arma.find(testMatrix);
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{Datum.inf}});
    expected = new Mat(new double[][]{{0}});
    actual = Arma.find(testMatrix);
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{Datum.nan}});
    expected = new Mat(new double[][]{{0}});
    actual = Arma.find(testMatrix);
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{Double.MIN_VALUE}});
    expected = new Mat(new double[][]{{0}});
    actual = Arma.find(testMatrix);
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{0}});
    expected = new Mat();
    actual = Arma.find(testMatrix);
    assertMatEquals(expected, actual, 0);

    testMatrix = new Mat(new double[][]{{0, 0, 0}, {0, 0, 0}});
    expected = new Mat();
    actual = Arma.find(testMatrix);
    assertMatEquals(expected, actual, 0);
  }

  /**
   * Tests {@link Arma#any(Mat)} by assertion of the following:
   * 
   * Returns true for:
   * <ul>
   * <li>{{1, 0, 1}, {0, 1, 1}}</li>
   * <li>{{1}}</li>
   * <li>{{-1}}</li>
   * <li>{{{@link Datum#inf}</li>
   * <li>{{{@link Datum#nan}</li>
   * <li>{{Double.MIN_VALUE}}</li>
   * </ul>
   * 
   * Returns false for:
   * <ul>
   * <li>{{0}}</li>
   * <li>{{0, 0, 0}, {0, 0, 0}}</li>
   * </ul>
   */
  @Test
  public void testAny() {
    Mat testMatrix;

    testMatrix = new Mat(new double[][]{{1}});
    assertTrue(Arma.any(testMatrix));

    testMatrix = new Mat(new double[][]{{1, 0, 1}, {0, 1, 1}});
    assertTrue(Arma.any(testMatrix));

    testMatrix = new Mat(new double[][]{{-1}});
    assertTrue(Arma.any(testMatrix));

    testMatrix = new Mat(new double[][]{{Datum.inf}});
    assertTrue(Arma.any(testMatrix));

    testMatrix = new Mat(new double[][]{{Datum.nan}});
    assertTrue(Arma.any(testMatrix));

    testMatrix = new Mat(new double[][]{{Double.MIN_VALUE}});
    assertTrue(Arma.any(testMatrix));

    testMatrix = new Mat(new double[][]{{0}});
    assertFalse(Arma.any(testMatrix));

    testMatrix = new Mat(new double[][]{{0, 0, 0}, {0, 0, 0}});
    assertFalse(Arma.any(testMatrix));
  }

  /**
   * Tests {@link Arma#norm(Mat, int)} and {@link Arma#norm(Mat, String)} for vectors by comparing the results for
   * following vectors with a pre-computed ground truth.
   * 
   * Vectors used:
   * <ul>
   * <li>Mat.zeros(10, 1)</li>
   * <li>Mat.ones(10, 1)</li>
   * <li>Mat.eye(10, 1)</li>
   * </ul>
   */
  @Test
  public void testNorm() {
    Mat testMatrix;

    testMatrix = Mat.zeros(10, 1);
    assertEquals(0, Arma.norm(testMatrix, 1), TestData.NUMERIC_TOLERANCE);
    assertEquals(0, Arma.norm(testMatrix, 2), TestData.NUMERIC_TOLERANCE);
    assertEquals(0, Arma.norm(testMatrix, "-inf"), TestData.NUMERIC_TOLERANCE);
    assertEquals(0, Arma.norm(testMatrix, "inf"), TestData.NUMERIC_TOLERANCE);
    assertEquals(0, Arma.norm(testMatrix, "fro"), TestData.NUMERIC_TOLERANCE);

    testMatrix = Mat.ones(10, 1);
    assertEquals(10, Arma.norm(testMatrix, 1), TestData.NUMERIC_TOLERANCE);
    assertEquals(Math.sqrt(10), Arma.norm(testMatrix, 2), TestData.NUMERIC_TOLERANCE);
    assertEquals(1, Arma.norm(testMatrix, "-inf"), TestData.NUMERIC_TOLERANCE);
    assertEquals(1, Arma.norm(testMatrix, "inf"), TestData.NUMERIC_TOLERANCE);
    assertEquals(Math.sqrt(10), Arma.norm(testMatrix, "fro"), TestData.NUMERIC_TOLERANCE);

    testMatrix = Mat.eye(10, 1);
    assertEquals(1, Arma.norm(testMatrix, 1), TestData.NUMERIC_TOLERANCE);
    assertEquals(1, Arma.norm(testMatrix, 2), TestData.NUMERIC_TOLERANCE);
    assertEquals(0, Arma.norm(testMatrix, "-inf"), TestData.NUMERIC_TOLERANCE);
    assertEquals(1, Arma.norm(testMatrix, "inf"), TestData.NUMERIC_TOLERANCE);
    assertEquals(1, Arma.norm(testMatrix, "fro"), TestData.NUMERIC_TOLERANCE);
  }
}
