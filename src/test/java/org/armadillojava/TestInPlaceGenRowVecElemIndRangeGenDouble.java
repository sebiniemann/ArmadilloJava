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
public class TestInPlaceGenRowVecElemIndRangeGenDouble extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemIndRange = {2}, GenDouble = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemIndRange);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genRowVecString;

  @Parameter(1)
  public Row       _genRowVec;

  protected Row    _copyOfGenRowVec;

  @Parameter(2)
  public String    _elemIndRangeString;

  @Parameter(3)
  public Span      _elemIndRange;

  protected Span   _copyOfElemIndRange;

  @Parameter(4)
  public String    _genDoubleString;

  @Parameter(5)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndRangeString + "," + _genDoubleString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemIndRange = new Span(_elemIndRange);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _elemIndRange = new Span(_copyOfElemIndRange);
    _genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testRowVecColsPlus() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.colsPlus"));
  }

  @Test
  public void testRowVecColsMinus() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.colsMinus"));
  }

  @Test
  public void testRowVecColsTimes() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.TIMES, _genDouble);

    assertMatEquals(_genRowVec, load("Row.colsTimes"));
  }

  @Test
  public void testRowColsDivide() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.cols(_elemIndRange._first, _elemIndRange._last, Op.DIVIDE, _genDouble);

    assertMatEquals(_genRowVec, load("Row.colsDivide"));
  }

  @Test
  public void testRowVecSubvecPlusA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.subvec(_elemIndRange, Op.PLUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.subvecPlus"));
  }

  @Test
  public void testRowVecSubvecPlusB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.subvecPlus"));
  }

  @Test
  public void testRowVecSubvecMinusA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.subvec(_elemIndRange, Op.MINUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.subvecMinus"));
  }

  @Test
  public void testRowVecSubvecMinusB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.subvecMinus"));
  }

  @Test
  public void testRowVecSubvecTimesA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.subvec(_elemIndRange, Op.TIMES, _genDouble);

    assertMatEquals(_genRowVec, load("Row.subvecTimes"));
  }

  @Test
  public void testRowVecSubvecTimesB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.TIMES, _genDouble);

    assertMatEquals(_genRowVec, load("Row.subvecTimes"));
  }

  @Test
  public void testRowSubvecDivideA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.subvec(_elemIndRange, Op.DIVIDE, _genDouble);

    assertMatEquals(_genRowVec, load("Row.subvecDivide"));
  }

  @Test
  public void testRowSubvecDivideB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    _genRowVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.DIVIDE, _genDouble);

    assertMatEquals(_genRowVec, load("Row.subvecDivide"));
  }

}