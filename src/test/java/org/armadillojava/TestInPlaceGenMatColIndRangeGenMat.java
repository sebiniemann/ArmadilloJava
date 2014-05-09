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
public class TestInPlaceGenMatColIndRangeGenMat extends TestClass {

  @Parameters(name = "{index}: GenMatA = {0}, ColIndRange = {2}, GenMatB = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ColIndRange);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genMatAString;

  @Parameter(1)
  public Mat     _genMatA;

  protected Mat  _copyOfGenMatA;

  @Parameter(2)
  public String  _colIndRangeString;

  @Parameter(3)
  public Span    _colIndRange;

  protected Span _copyOfColIndRange;

  @Parameter(4)
  public String  _genMatBString;

  @Parameter(5)
  public Mat     _genMatB;

  protected Mat  _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _colIndRangeString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfColIndRange = new Span(_colIndRange);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _colIndRange = new Span(_copyOfColIndRange);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatColsEqual() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMatA.cols(_colIndRange._first, _colIndRange._last, Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colsEqual"));
  }

  @Test
  public void testMatColsPlus() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMatA.cols(_colIndRange._first, _colIndRange._last, Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colsPlus"));
  }

  @Test
  public void testMatColsMinus() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMatA.cols(_colIndRange._first, _colIndRange._last, Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colsMinus"));
  }

  @Test
  public void testMatColsElemTimes() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMatA.cols(_colIndRange._first, _colIndRange._last, Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colsElemTimes"));
  }

  @Test
  public void testMatColsElemDivide() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    assumeThat(_genMatB.n_cols, is(_colIndRange._last - _colIndRange._first + 1));

    _genMatA.cols(_colIndRange._first, _colIndRange._last, Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colsElemDivide"));
  }

}
