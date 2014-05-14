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
public class TestInPlaceGenMatRowIndColIndRangeGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}, ColIndRange = {4}, GenMat = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.ColIndRange);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatAString;

  @Parameter(1)
  public Mat    _genMatA;

  protected Mat _copyOfGenMatA;

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
  public String _genMatBString;

  @Parameter(7)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _rowIndString + "," + _colIndRangeString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfColIndRange = new Span(_colIndRange);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _rowInd = new Integer(_copyOfRowInd);
    _colIndRange = new Span(_copyOfColIndRange);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatRowEqual() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.is_rowvec(), is(true));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
      assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    }

    _genMatA.row(_rowInd, _colIndRange, Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowEqual"));
  }

  @Test
  public void testMatRowPlus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.is_rowvec(), is(true));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
      assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    }

    _genMatA.row(_rowInd, _colIndRange, Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowPlus"));
  }

  @Test
  public void testMatRowMinus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.is_rowvec(), is(true));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
      assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    }

    _genMatA.row(_rowInd, _colIndRange, Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowMinus"));
  }

  @Test
  public void testMatRowElemTimes() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.is_rowvec(), is(true));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
      assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    }

    _genMatA.row(_rowInd, _colIndRange, Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowElemTimes"));
  }

  @Test
  public void testMatRowElemDivide() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.is_rowvec(), is(true));
    if(!_colIndRange._isEntireRange) {
      assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
      assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    }

    _genMatA.row(_rowInd, _colIndRange, Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowElemDivide"));
  }

}
