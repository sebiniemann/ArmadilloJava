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
public class TestInPlaceGenMatRowIndRangeColIndRangeGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowIndRange = {2}, ColIndRange = {4}, GenRowVec = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowIndRange);
    inputClasses.add(InputClass.ColIndRange);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _rowIndRangeString;

  @Parameter(3)
  public Span      _rowIndRange;

  protected Span   _copyOfRowIndRange;

  @Parameter(4)
  public String    _colIndRangeString;

  @Parameter(5)
  public Span    _colIndRange;

  protected Span _copyOfColIndRange;

  @Parameter(6)
  public String    _genRowVecString;

  @Parameter(7)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndRangeString + "," + _colIndRangeString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowIndRange = new Span(_rowIndRange);
    _copyOfColIndRange = new Span(_colIndRange);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowIndRange = new Span(_copyOfRowIndRange);
    _colIndRange = new Span(_copyOfColIndRange);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testMatSubmatEqualA() throws IOException {
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));

    if(!_rowIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    }

    if(!_colIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.submat(_rowIndRange, _colIndRange, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatEqual"));
  }

  @Test
  public void testMatSubmatEqualB() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));
    assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMat.submat(_rowIndRange._first, _colIndRange._first, _rowIndRange._last, _colIndRange._last, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatEqual"));
  }

  @Test
  public void testMatSubmatPlusA() throws IOException {
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));

    if(!_rowIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    }

    if(!_colIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.submat(_rowIndRange, _colIndRange, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatPlus"));
  }

  @Test
  public void testMatSubmatPlusB() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));
    assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMat.submat(_rowIndRange._first, _colIndRange._first, _rowIndRange._last, _colIndRange._last, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatPlus"));
  }

  @Test
  public void testMatSubmatMinusA() throws IOException {
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));

    if(!_rowIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    }

    if(!_colIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.submat(_rowIndRange, _colIndRange, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatMinus"));
  }

  @Test
  public void testMatSubmatMinusB() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));
    assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMat.submat(_rowIndRange._first, _colIndRange._first, _rowIndRange._last, _colIndRange._last, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatMinus"));
  }

  @Test
  public void testMatSubmatElemTimesA() throws IOException {
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));

    if(!_rowIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    }

    if(!_colIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.submat(_rowIndRange, _colIndRange, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatElemTimes"));
  }

  @Test
  public void testMatSubmatElemTimesB() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));
    assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMat.submat(_rowIndRange._first, _colIndRange._first, _rowIndRange._last, _colIndRange._last, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatElemTimes"));
  }

  @Test
  public void testMatSubmatElemDivideA() throws IOException {
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));

    if(!_rowIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    }

    if(!_colIndRange._isEntireRange) {
      assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));
    } else {
      assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    }

    _genMat.submat(_rowIndRange, _colIndRange, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatElemDivide"));
  }

  @Test
  public void testMatSubmatElemDivideB() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));
    assumeThat(_genRowVec.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMat.submat(_rowIndRange._first, _colIndRange._first, _rowIndRange._last, _colIndRange._last, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatElemDivide"));
  }

}
