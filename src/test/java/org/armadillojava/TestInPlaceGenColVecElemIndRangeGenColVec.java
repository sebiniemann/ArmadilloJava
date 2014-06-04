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
public class TestInPlaceGenColVecElemIndRangeGenColVec extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemIndRange = {2}, GenColVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemIndRange);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genColVecAString;

  @Parameter(1)
  public Col     _genColVecA;

  protected Col  _copyOfGenColVecA;

  @Parameter(2)
  public String  _elemIndRangeString;

  @Parameter(3)
  public Span    _elemIndRange;

  protected Span _copyOfElemIndRange;

  @Parameter(4)
  public String  _genColVecBString;

  @Parameter(5)
  public Col     _genColVecB;

  protected Col  _copyOfGenColVecB;

  @Before
  public void before() {
    _fileSuffix = _genColVecAString + "," + _elemIndRangeString + "," + _genColVecBString;

    _copyOfGenColVecA = new Col(_genColVecA);
    _copyOfElemIndRange = new Span(_elemIndRange);
    _copyOfGenColVecB = new Col(_genColVecB);
  }

  @After
  public void after() {
    _genColVecA.inPlace(Op.EQUAL, _copyOfGenColVecA);
    _elemIndRange = new Span(_copyOfElemIndRange);
    _genColVecB = new Col(_copyOfGenColVecB);
  }

  @Test
  public void testColVecRowsEqual() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.rows(_elemIndRange._first, _elemIndRange._last, Op.EQUAL, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsEqual"));
  }

  @Test
  public void testColVecRowsPlus() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.rows(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsPlus"));
  }

  @Test
  public void testColVecRowsMinus() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.rows(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsMinus"));
  }

  @Test
  public void testColVecRowsTimes() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.rows(_elemIndRange._first, _elemIndRange._last, Op.ELEMTIMES, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsElemTimes"));
  }

  @Test
  public void testColRowsDivide() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.rows(_elemIndRange._first, _elemIndRange._last, Op.ELEMDIVIDE, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsElemDivide"));
  }

  @Test
  public void testColVecSubvecEqualA() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange, Op.EQUAL, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecEqual"));
  }

  @Test
  public void testColVecSubvecEqualB() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.EQUAL, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecEqual"));
  }

  @Test
  public void testColVecSubvecPlusA() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange, Op.PLUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecPlus"));
  }

  @Test
  public void testColVecSubvecPlusB() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecPlus"));
  }

  @Test
  public void testColVecSubvecMinusA() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange, Op.MINUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecMinus"));
  }

  @Test
  public void testColVecSubvecMinusB() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecMinus"));
  }

  @Test
  public void testColVecSubvecTimesA() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange, Op.ELEMTIMES, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecElemTimes"));
  }

  @Test
  public void testColVecSubvecTimesB() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.ELEMTIMES, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecElemTimes"));
  }

  @Test
  public void testColSubvecDivideA() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange, Op.ELEMDIVIDE, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecElemDivide"));
  }

  @Test
  public void testColSubvecDivideB() throws IOException {
    assumeThat(_genColVecA.in_range(_elemIndRange), is(true));
    assumeThat(_genColVecB.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVecA.subvec(_elemIndRange._first, _elemIndRange._last, Op.ELEMDIVIDE, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.subvecElemDivide"));
  }

}