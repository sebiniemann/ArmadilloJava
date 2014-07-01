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
public class TestInPlaceGenRowVecElemIndRangeGenColVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemIndRange = {2}, GenColVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemIndRange);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genRowVecString;

  @Parameter(1)
  public Row     _genRowVec;

  protected Row  _copyOfGenRowVec;

  @Parameter(2)
  public String  _elemIndRangeString;

  @Parameter(3)
  public Span    _elemIndRange;

  protected Span _copyOfElemIndRange;

  @Parameter(4)
  public String  _genColVecString;

  @Parameter(5)
  public Col     _genColVec;

  protected Col  _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndRangeString + "," + _genColVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemIndRange = new Span(_elemIndRange);
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _elemIndRange = new Span(_copyOfElemIndRange);
    _genColVec = new Col(_copyOfGenColVec);
  }

  @Test
  public void testRowVecColsEqual() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.EQUAL, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsEqual"));
  }

  @Test
  public void testRowVecColsPlus() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsPlus"));
  }

  @Test
  public void testRowVecColsMinus() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsMinus"));
  }

  @Test
  public void testRowVecColsElemTimes() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.ELEMTIMES, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsElemTimes"));
  }

  @Test
  public void testRowColsElemDivide() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.ELEMDIVIDE, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsElemDivide"));
  }

  @Test
  public void testRowVecSubvecEqualA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange, Op.EQUAL, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecEqual"));
  }

  @Test
  public void testRowVecSubvecEqualB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.EQUAL, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecEqual"));
  }


  @Test
  public void testRowVecSubvecPlusA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange, Op.PLUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecPlus"));
  }


  @Test
  public void testRowVecSubvecPlusB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecPlus"));
  }

  @Test
  public void testRowVecSubvecMinusA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange, Op.MINUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecMinus"));
  }

  @Test
  public void testRowVecSubvecMinusB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecMinus"));
  }

  @Test
  public void testRowVecSubvecElemTimesA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange, Op.ELEMTIMES, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecElemTimes"));
  }

  @Test
  public void testRowVecSubvecElemTimesB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.ELEMTIMES, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecElemTimes"));
  }

  @Test
  public void testRowSubvecElemDivideA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange, Op.ELEMDIVIDE, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecElemDivide"));
  }

  @Test
  public void testRowSubvecElemDivideB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genColVec.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.ELEMDIVIDE, _genColVec);

    assertMatEquals(_genRowVec, load("Row.subvecElemDivide"));
  }

}