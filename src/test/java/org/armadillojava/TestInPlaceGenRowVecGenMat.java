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
public class TestInPlaceGenRowVecGenMat extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, GenMat = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  @Parameter(2)
  public String _genMatString;

  @Parameter(3)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;
  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _genMatString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
  }

  @Test
  public void testRowInPlaceEqual() throws IOException {
	assumeThat(_genMat.is_rowvec(), is(true));
	assumeThat(_genMat.n_elem, is(_genRowVec.n_elem));
	_genRowVec.inPlace(Op.EQUAL, _genMat);

    assertMatEquals(_genRowVec, load("Row.inPlaceEqual"));
  }

  @Test
  public void testRowInPlacePlus() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genRowVec.inPlace(Op.PLUS, _genMat);

    assertMatEquals(_genRowVec, load("Row.inPlacePlus"));
  }

  @Test
  public void testRowInPlaceMinus() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genRowVec.inPlace(Op.MINUS, _genMat);

    assertMatEquals(_genRowVec, load("Row.inPlaceMinus"));
  }

  @Test
  public void testRowInPlaceTimes() throws IOException {
    assumeThat(_genRowVec.is_finite(), is(true));
    assumeThat(_genMat.is_finite(), is(true));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genRowVec.inPlace(Op.TIMES, _genMat);

    assertMatEquals(_genRowVec, load("Row.inPlaceTimes"));
  }

  @Test
  public void testRowInPlaceElemTimes() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genRowVec.inPlace(Op.ELEMTIMES, _genMat);

    assertMatEquals(_genRowVec, load("Row.inPlaceElemTimes"));
  }

  @Test
  public void testRowInPlaceElemDivide() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genRowVec.inPlace(Op.ELEMDIVIDE, _genMat);

    assertMatEquals(_genRowVec, load("Row.inPlaceElemDivide"));
  }

  @Test
  public void testRowSwap() throws IOException {
    assumeThat(_genMat.is_rowvec(), is(true));
    
    _genRowVec.swap(_genMat);

    assertMatEquals(_genRowVec, load("Row.swap"));
  }

  @Test
  public void testRowCopy_size() throws IOException {
	assumeThat(_genMat.is_rowvec(), is(true));
	_genRowVec.copy_size(_genMat);

    Mat expected = load("Row.copy_size");

    assertThat(_genRowVec.n_cols, is(expected.n_cols));
    assertThat(_genRowVec.n_rows, is(expected.n_rows));
  }
  
  @Test
  public void testRowRow() throws IOException {
    assumeThat(_genMat.is_rowvec(), is(true));
    
    _genRowVec = new Row(_genMat);

    assertMatEquals(_genRowVec, load("Row.row"));
  }

}
