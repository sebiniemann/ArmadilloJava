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
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;

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
public class TestInPlaceGenMatRowIndColIndRangeGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}, ColIndRange = {4}, GenRowVec = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.ColIndRange);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _rowIndString;

  @Parameter(3)
  public int    _rowInd;

  protected int _copyOfRowInd;

  @Parameter(4)
  public String _colIndRangeString;

  @Parameter(5)
  public Span    _colIndRange;

  protected Span _copyOfColIndRange;

  @Parameter(6)
  public String _genRowVecString;

  @Parameter(7)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndString + "," + _colIndRangeString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfColIndRange = new Span(_colIndRange);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInd = new Integer(_copyOfRowInd);
    _colIndRange = new Span(_copyOfColIndRange);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testMatRowEqual() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.row(_rowInd, _colIndRange, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowEqual"));
  }

  @Test
  public void testMatRowPlus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.row(_rowInd, _colIndRange, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowPlus"));
  }

  @Test
  public void testMatRowMinus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.row(_rowInd, _colIndRange, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowMinus"));
  }

  @Test
  public void testMatRowElemTimes() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.row(_rowInd, _colIndRange, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowElemTimes"));
  }

  @Test
  public void testMatRowElemDivide() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.row(_rowInd, _colIndRange, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowElemDivide"));
  }

}
