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
public class TestInPlaceGenMatRowIndRangeGenMat extends TestClass {

  @Parameters(name = "{index}: GenMatA = {0}, RowIndRange = {2}, GenMatB = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowIndRange);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genMatAString;

  @Parameter(1)
  public Mat     _genMatA;

  protected Mat  _copyOfGenMatA;

  @Parameter(2)
  public String  _rowIndRangeString;

  @Parameter(3)
  public Span    _rowIndRange;

  protected Span _copyOfRowIndRange;

  @Parameter(4)
  public String  _genMatBString;

  @Parameter(5)
  public Mat     _genMatB;

  protected Mat  _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _rowIndRangeString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfRowIndRange = new Span(_rowIndRange._first, _rowIndRange._last);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _rowIndRange = new Span(_copyOfRowIndRange._first, _copyOfRowIndRange._last);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatRowsEqual() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));

    _genMatA.rows(_rowIndRange._first, _rowIndRange._last, Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsEqual"));
  }

  @Test
  public void testMatRowsPlus() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));

    _genMatA.rows(_rowIndRange._first, _rowIndRange._last, Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsPlus"));
  }

  @Test
  public void testMatRowsMinus() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));

    _genMatA.rows(_rowIndRange._first, _rowIndRange._last, Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsMinus"));
  }

  @Test
  public void testMatRowsElemTimes() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));

    _genMatA.rows(_rowIndRange._first, _rowIndRange._last, Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsElemTimes"));
  }

  @Test
  public void testMatRowsElemDivide() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));
    assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));

    _genMatA.rows(_rowIndRange._first, _rowIndRange._last, Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsElemDivide"));
  }

}
