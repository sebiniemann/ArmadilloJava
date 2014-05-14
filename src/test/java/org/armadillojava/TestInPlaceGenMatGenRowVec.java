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
public class TestInPlaceGenMatGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, GenRowVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  @Parameter(2)
  public String _genRowVecString;

  @Parameter(3)
  public Row    _genRowVec;

  protected Mat _copyOfGenMat;
  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testMatInPlaceEqual() throws IOException {
    _genMat.inPlace(Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.inPlaceEqual"));
  }

  @Test
  public void testMatInPlacePlus() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.inPlace(Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.inPlacePlus"));
  }

  @Test
  public void testMatInPlaceMinus() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.inPlace(Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.inPlaceMinus"));
  }

  @Test
  public void testMatInPlaceTimes() throws IOException {
    assumeThat(_genMat.is_finite(), is(true));
    assumeThat(_genRowVec.is_finite(), is(true));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_rows));

    _genMat.inPlace(Op.TIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.inPlaceTimes"));
  }

  @Test
  public void testMatInPlaceElemTimes() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.inPlace(Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.inPlaceElemTimes"));
  }

  @Test
  public void testMatInPlaceElemDivide() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.inPlace(Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.inPlaceElemDivide"));
  }

  @Test
  public void testMatSwap() throws IOException {
    assumeThat(_genMat.is_rowvec(), is(true));
    
    _genMat.swap(_genRowVec);

    assertMatEquals(_genMat, load("Mat.swap"));
  }

  @Test
  public void testMatEach_colEqual() throws IOException {
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    _genMat.each_col(Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colEqual"));
  }

  @Test
  public void testMatEach_colPlus() throws IOException {
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    _genMat.each_col(Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colPlus"));
  }

  @Test
  public void testMatEach_colMinus() throws IOException {
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    _genMat.each_col(Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colMinus"));
  }

  @Test
  public void testMatEach_colElemTimes() throws IOException {
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    _genMat.each_col(Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colElemTimes"));
  }

  @Test
  public void testMatEach_colElemDivide() throws IOException {
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    _genMat.each_col(Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colElemDivide"));
  }

  @Test
  public void testMatEach_rowEqual() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.each_row(Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowEqual"));
  }

  @Test
  public void testMatEach_rowPlus() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.each_row(Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowPlus"));
  }

  @Test
  public void testMatEach_rowMinus() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.each_row(Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowMinus"));
  }

  @Test
  public void testMatEach_rowElemTimes() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.each_row(Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowElemTimes"));
  }

  @Test
  public void testMatEach_rowElemDivide() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    _genMat.each_row(Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowElemDivide"));
  }

  @Test
  public void testMatDiagEqual() throws IOException {
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols)));

    _genMat.diag(Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagEqual"));
  }

  @Test
  public void testMatDiagPlus() throws IOException {
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols)));

    _genMat.diag(Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagPlus"));
  }

  @Test
  public void testMatDiagMinus() throws IOException {
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols)));

    _genMat.diag(Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagMinus"));
  }

  @Test
  public void testMatDiagElemTimes() throws IOException {
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols)));

    _genMat.diag(Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagElemTimes"));
  }

  @Test
  public void testMatDiagElemDivide() throws IOException {
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols)));

    _genMat.diag(Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagElemDivide"));
  }

  @Test
  public void testMatCopy_size() throws IOException {
    _genMat.copy_size(_genRowVec);

    Mat expected = load("Mat.copy_size");

    assertThat(_genMat.n_rows, is(expected.n_rows));
    assertThat(_genMat.n_cols, is(expected.n_cols));
  }

}
