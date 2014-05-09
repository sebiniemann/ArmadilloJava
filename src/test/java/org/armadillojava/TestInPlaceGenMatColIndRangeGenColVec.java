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
public class TestInPlaceGenMatColIndRangeGenColVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ColIndRange = {2}, GenColVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ColIndRange);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _colIndRangeString;

  @Parameter(3)
  public Span      _colIndRange;

  protected Span   _copyOfColIndRange;

  @Parameter(4)
  public String    _genColVecString;

  @Parameter(5)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _colIndRangeString + "," + _genColVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfColIndRange = new Span(_colIndRange);
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _colIndRange = new Span(_copyOfColIndRange);
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
  }

  @Test
  public void testMatColsEqual() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));
    assumeThat(_colIndRange._last - _colIndRange._first + 1, is(1));

    _genMat.cols(_colIndRange._first, _colIndRange._last, Op.EQUAL, _genColVec);

    assertMatEquals(_genMat, load("Mat.colsEqual"));
  }

  @Test
  public void testMatColsPlus() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));
    assumeThat(_colIndRange._last - _colIndRange._first + 1, is(1));

    _genMat.cols(_colIndRange._first, _colIndRange._last, Op.PLUS, _genColVec);

    assertMatEquals(_genMat, load("Mat.colsPlus"));
  }

  @Test
  public void testMatColsMinus() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));
    assumeThat(_colIndRange._last - _colIndRange._first + 1, is(1));

    _genMat.cols(_colIndRange._first, _colIndRange._last, Op.MINUS, _genColVec);

    assertMatEquals(_genMat, load("Mat.colsMinus"));
  }

  @Test
  public void testMatColsTimes() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));
    assumeThat(_colIndRange._last - _colIndRange._first + 1, is(1));

    _genMat.cols(_colIndRange._first, _colIndRange._last, Op.ELEMTIMES, _genColVec);

    assertMatEquals(_genMat, load("Mat.colsElemTimes"));
  }

  @Test
  public void testMatColsDivide() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));
    assumeThat(_colIndRange._last - _colIndRange._first + 1, is(1));

    _genMat.cols(_colIndRange._first, _colIndRange._last, Op.ELEMDIVIDE, _genColVec);

    assertMatEquals(_genMat, load("Mat.colsElemDivide"));
  }

}
