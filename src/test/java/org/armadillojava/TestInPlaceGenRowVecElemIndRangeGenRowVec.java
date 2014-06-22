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
public class TestInPlaceGenRowVecElemIndRangeGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemIndRange = {2}, GenRowVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemIndRange);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genRowVecAString;

  @Parameter(1)
  public Row     _genRowVecA;

  protected Row  _copyOfGenRowVecA;

  @Parameter(2)
  public String  _elemIndRangeString;

  @Parameter(3)
  public Span    _elemIndRange;

  protected Span _copyOfElemIndRange;

  @Parameter(4)
  public String  _genRowVecBString;

  @Parameter(5)
  public Row     _genRowVecB;

  protected Row  _copyOfGenRowVecB;

  @Before
  public void before() {
    _fileSuffix = _genRowVecAString + "," + _elemIndRangeString + "," + _genRowVecBString;

    _copyOfGenRowVecA = new Row(_genRowVecA);
    _copyOfElemIndRange = new Span(_elemIndRange);
    _copyOfGenRowVecB = new Row(_genRowVecB);
  }

  @After
  public void after() {
    _genRowVecA.inPlace(Op.EQUAL, _copyOfGenRowVecA);
    _elemIndRange = new Span(_copyOfElemIndRange);
    _genRowVecB = new Row(_copyOfGenRowVecB);
  }

  @Test
  public void testRowVecColsEqual() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.cols(_elemIndRange._first, _elemIndRange._last, Op.EQUAL, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsEqual"));
  }

  @Test
  public void testRowVecColsPlus() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.cols(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsPlus"));
  }

  @Test
  public void testRowVecColsMinus() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.cols(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsMinus"));
  }

  @Test
  public void testRowVecColsTimes() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.cols(_elemIndRange._first, _elemIndRange._last, Op.ELEMTIMES, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsElemTimes"));
  }

  @Test
  public void testRowColsDivide() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.cols(_elemIndRange._first, _elemIndRange._last, Op.ELEMDIVIDE, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsElemDivide"));
  }

  @Test
  public void testRowVecSubvecEqualA() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange, Op.EQUAL, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecEqual"));
  }

  @Test
  public void testRowVecSubvecEqualB() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.EQUAL, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecEqual"));
  }

  @Test
  public void testRowVecSubvecPlusA() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange, Op.PLUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecPlus"));
  }

  @Test
  public void testRowVecSubvecPlusB() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecPlus"));
  }

  @Test
  public void testRowVecSubvecMinusA() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange, Op.MINUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecMinus"));
  }

  @Test
  public void testRowVecSubvecMinusB() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecMinus"));
  }

  @Test
  public void testRowVecSubvecTimesA() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange, Op.ELEMTIMES, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecElemTimes"));
  }

  @Test
  public void testRowVecSubvecTimesB() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.ELEMTIMES, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecElemTimes"));
  }

  @Test
  public void testRowSubvecDivideA() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange, Op.ELEMDIVIDE, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecElemDivide"));
  }

  @Test
  public void testRowSubvecDivideB() throws IOException {
    assumeThat(_genRowVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVecB.n_cols, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genRowVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.ELEMDIVIDE, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.subvecElemDivide"));
  }

}