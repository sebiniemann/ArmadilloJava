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
public class TestInPlaceGenMatRowIndRangeGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowIndRange = {2}, GenRowVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowIndRange);
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
  public String    _genRowVecString;

  @Parameter(5)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndRangeString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowIndRange = new Span(_rowIndRange);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowIndRange = new Span(_copyOfRowIndRange);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testMatColsEqual() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsEqual"));
  }

  @Test
  public void testMatColsPlus() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsPlus"));
  }

  @Test
  public void testMatColsMinus() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsMinus"));
  }

  @Test
  public void testMatColsTimes() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsElemTimes"));
  }

  @Test
  public void testMatColsDivide() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    assumeThat(_rowIndRange._last - _rowIndRange._first + 1, is(1));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsElemDivide"));
  }

}
