/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 * Sebastian Niemann - Lead developer
 * Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

import static org.armadillojava.TestUtil.assertMatEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestInPlaceGenMatGenMat extends TestClass {

  @Parameters(name = "{index}: GenMatA = {0}, GenMatB = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatAString;

  @Parameter(1)
  public Mat    _genMatA;

  @Parameter(2)
  public String _genMatBString;

  @Parameter(3)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatA;
  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatInPlaceEqual() throws IOException {
    _genMatA.inPlace(Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.inPlaceEqual"));
  }

  @Test
  public void testMatInPlacePlus() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.inPlace(Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.inPlacePlus"));
  }

  @Test
  public void testMatInPlaceMinus() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.inPlace(Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.inPlaceMinus"));
  }

  @Test
  public void testMatInPlaceTimes() throws IOException {
    assumeThat(_genMatA.is_finite(), is(true));
    assumeThat(_genMatB.is_finite(), is(true));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_rows));

    _genMatA.inPlace(Op.TIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.inPlaceTimes"));
  }

  @Test
  public void testMatInPlaceElemTimes() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.inPlace(Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.inPlaceElemTimes"));
  }

  @Test
  public void testMatInPlaceElemDivide() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.inPlace(Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.inPlaceElemDivide"));
  }

  @Test
  public void testMatSwap() throws IOException {
    _genMatA.swap(_genMatB);

    assertMatEquals(_genMatA, load("Mat.swap"));
  }

  @Test
  public void testMatEach_colEqual() throws IOException {
    assumeThat(_genMatB.is_colvec(), is(true));
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));

    _genMatA.each_col(Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_colEqual"));
  }

  @Test
  public void testMatEach_colPlus() throws IOException {
    assumeThat(_genMatB.is_colvec(), is(true));
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));

    _genMatA.each_col(Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_colPlus"));
  }

  @Test
  public void testMatEach_colMinus() throws IOException {
    assumeThat(_genMatB.is_colvec(), is(true));
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));

    _genMatA.each_col(Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_colMinus"));
  }

  @Test
  public void testMatEach_colElemTimes() throws IOException {
    assumeThat(_genMatB.is_colvec(), is(true));
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));

    _genMatA.each_col(Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_colElemTimes"));
  }

  @Test
  public void testMatEach_colElemDivide() throws IOException {
    assumeThat(_genMatB.is_colvec(), is(true));
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));

    _genMatA.each_col(Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_colElemDivide"));
  }

  @Test
  public void testMatEach_rowEqual() throws IOException {
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.each_row(Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowEqual"));
  }

  @Test
  public void testMatEach_rowPlus() throws IOException {
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.each_row(Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowPlus"));
  }

  @Test
  public void testMatEach_rowMinus() throws IOException {
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.each_row(Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowMinus"));
  }

  @Test
  public void testMatEach_rowElemTimes() throws IOException {
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.each_row(Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowElemTimes"));
  }

  @Test
  public void testMatEach_rowElemDivide() throws IOException {
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    _genMatA.each_row(Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowElemDivide"));
  }

  @Test
  public void testMatCopy_size() throws IOException {
    _genMatA.copy_size(_genMatB);

    Mat expected = load("Mat.copy_size");

    assertThat(_genMatA.n_rows, is(expected.n_rows));
    assertThat(_genMatA.n_cols, is(expected.n_cols));
  }

}
